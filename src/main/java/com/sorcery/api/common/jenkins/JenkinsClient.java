package com.sorcery.api.common.jenkins;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.QueueReference;
import com.sorcery.api.common.exception.ServiceException;
import com.sorcery.api.common.utils.JenkinsUtils;
import com.sorcery.api.dao.UserMapper;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.jenkins.OperateJenkinsJobDto;
import com.sorcery.api.entity.Jenkins;
import com.sorcery.api.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/25
 */
@Slf4j
@Component
public class JenkinsClient {
    private final UserMapper userMapper;
    private final JenkinsFactory jenkinsFactory;

    public JenkinsClient(UserMapper userMapper, JenkinsFactory jenkinsFactory) {
        this.userMapper = userMapper;
        this.jenkinsFactory = jenkinsFactory;
    }

    private final String PREFIX_TIPS = "调用Jenkins异常-";

    /**
     * 执行Jenkins中的job
     *
     * @param operateJenkinsJobDto 操作Jenkins job信息
     * @return 返回执行结果
     */
    public ResultDto<String> operateJenkinsJob(OperateJenkinsJobDto operateJenkinsJobDto) throws IOException {
        log.info("执行Jenkins的Job信息：{}", JSONUtil.toJsonStr(operateJenkinsJobDto));
        Jenkins jenkins = operateJenkinsJobDto.getJenkins();
        TokenDto tokenDto = operateJenkinsJobDto.getTokenDto();
        Map<String, String> params = operateJenkinsJobDto.getParams();

        User queryUser = new User();
        queryUser.setId(tokenDto.getUserId());
        User resultUser = userMapper.selectOne(queryUser);

        //拼接Job名称
        String jobName = JenkinsUtils.getStartTestJobName(tokenDto.getUserId());
        String jobSign = JenkinsUtils.getJobSignByName(jobName);
        log.info("拼接Job名称：{}", jobName);
        log.info("拼接Job标识：{}", jobSign);

        if (ObjectUtils.isEmpty(jobSign)) {
            return ResultDto.fail("Jenkins的Job标识不符合规范");
        }
        FileReader fileReader = new FileReader(new File("src/main/resources/jenkins/" + jobSign + ".xml"));
        // 获取Jenkins Job的配置文件的内容
        String jobXml = fileReader.readString();
        log.info("解析Jenkins配置文件内容:{}", jobXml);
        if (ObjectUtils.isEmpty(jobXml)) {
            return ResultDto.fail("Job配置信息不能为空");
        }
        //获取根据job类型获取数据库中对应的job名称
        String dbJobName = resultUser.getStartTestJobName();
        if (ObjectUtils.isEmpty(dbJobName)) {
            log.info("新建Jenkins执行测试的Job");
            createOrUpdateJob(jobName, jobXml, tokenDto.getUserId(), tokenDto.getDefaultJenkinsId(), 1);
            resultUser.setStartTestJobName(jobName);
            userMapper.updateByPrimaryKeySelective(resultUser);
        } else {
            createOrUpdateJob(jobName, jobXml, tokenDto.getUserId(), tokenDto.getDefaultJenkinsId(), 0);
        }
        try {
            JenkinsHttpClient jenkinsHttpClient = jenkinsFactory.getJenkinsHttpClient(tokenDto.getUserId(), tokenDto.getDefaultJenkinsId());
            Job job = getJob(jobName, jenkinsHttpClient, jenkins.getUrl());
            build(job, params);
            return ResultDto.success("成功");
        } catch (Exception e) {
            String tips = PREFIX_TIPS + "操作Jenkins的Job异常" + e.getMessage();
            log.error(tips, e);
            throw new ServiceException(tips);
        }
    }

    /**
     * 创建或更新Jenkins的Job
     *
     * @param jobName       job名称
     * @param jobXml        job xml配置文件
     * @param createUserId  创建人用户id
     * @param jenkinsId     Jenkins主键id
     * @param createJobFlag 1 是 0 否
     */
    public void createOrUpdateJob(String jobName, String jobXml, Integer createUserId, Integer jenkinsId, Integer createJobFlag) {
        JenkinsServer jenkinsServer = jenkinsFactory.getJenkinsServer(createUserId, jenkinsId);
        try {
            if (Objects.nonNull(createJobFlag) && createJobFlag == 1) {
                jenkinsServer.createJob(null, jobName, jobXml, true);
            } else {
                jenkinsServer.updateJob(null, jobName, jobXml, true);
            }
            ResultDto.success("成功");
        } catch (Exception e) {
            String tips = PREFIX_TIPS + "创建或更新Jenkins的Job时异常" + e.getMessage();
            log.error(tips, e);
            throw new ServiceException(tips);
        }
    }

    /**
     * 获取Job
     *
     * @param jobName           Jenkins Job名称
     * @param jenkinsHttpClient jenkinsHttpClient
     * @param jenkinsBaseUrl    Jenkins基础Url
     * @return 返回Job
     */
    private Job getJob(String jobName, JenkinsHttpClient jenkinsHttpClient, String jenkinsBaseUrl) {
        ///job/test_aitestbuild/api/json  404  jobName后面需加/，后面改源码
        Job job = new Job(jobName, jenkinsBaseUrl + "job/" + jobName + "/");
        job.setClient(jenkinsHttpClient);
        return job;
    }


    /**
     * 构建无参Jenkins Job
     *
     * @param job Jenkins Job
     * @return QueueReference
     * @throws IOException 抛出io异常
     */
    private QueueReference build(Job job) throws IOException {
        return build(job, null);
    }

    /**
     * 构建带参Jenkins Job
     *
     * @param job    Jenkins Job
     * @param params Job需要的参数
     * @return QueueReference
     * @throws IOException 抛出io异常
     */
    private QueueReference build(Job job, Map<String, String> params) throws IOException {
        QueueReference queueReference;
        if (Objects.isNull(params)) {
            queueReference = job.build(true);
        } else {
            queueReference = job.build(params, true);
        }
        log.info("构建带参Jenkins Job：{}", JSONUtil.toJsonStr(queueReference));
        return queueReference;
    }
}
