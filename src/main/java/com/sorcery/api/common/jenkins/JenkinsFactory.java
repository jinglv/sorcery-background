package com.sorcery.api.common.jenkins;

import cn.hutool.json.JSONUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.sorcery.api.common.exception.ServiceException;
import com.sorcery.api.dao.JenkinsMapper;
import com.sorcery.api.entity.Jenkins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 获取Jenkins服务工厂类
 *
 * @author jingLv
 * @date 2021/01/25
 */
@Slf4j
@Component
public class JenkinsFactory {

    private final JenkinsMapper jenkinsMapper;

    public JenkinsFactory(JenkinsMapper jenkinsMapper) {
        this.jenkinsMapper = jenkinsMapper;
    }


    /**
     * 获取Jenkins的java客户端的JenkinsServer
     *
     * @param createUserId 创建人用户id
     * @param jenkinsId    Jenkins主键id
     * @return JenkinsServer
     */
    public JenkinsServer getJenkinsServer(Integer createUserId, Integer jenkinsId) {
        return new JenkinsServer(getJenkinsHttpClient(createUserId, jenkinsId));
    }


    /**
     * 获取Jenkins的java客户端的JenkinsServer
     *
     * @param jenkinsHttpClient JenkinsHttpClient
     * @return JenkinsServer
     */
    public JenkinsServer getJenkinsServer(JenkinsHttpClient jenkinsHttpClient) {
        return new JenkinsServer(jenkinsHttpClient);
    }


    /**
     * 获取Jenkins的java客户端的JenkinsHttpClient
     *
     * @param createUserId 创建人用户id
     * @param jenkinsId    Jenkins主键id
     * @return JenkinsHttpClient
     */
    public JenkinsHttpClient getJenkinsHttpClient(Integer createUserId, Integer jenkinsId) {
        JenkinsHttpClient jenkinsHttpClient;
        try {
            // 获取数据库存储的Jenkins信息（Jenkins的URL，Jenkins用户名，Jenkins密码）
            Jenkins result = getJenkins(createUserId, jenkinsId);
            log.info("根据创建人用户id和Jenkins Id获取Jenkins信息：{}", JSONUtil.toJsonStr(result));
            jenkinsHttpClient = new JenkinsHttpClient(new URI(result.getUrl()), result.getUserName(), result.getPassword());
        } catch (URISyntaxException e) {
            String tips = "获取Jenkins服务异常" + e.getMessage();
            log.error(tips, e);
            throw new ServiceException(tips);
        }
        return jenkinsHttpClient;
    }

    /**
     * 根据环境信息获取数据库中配置的Jenkins服务信息（Jenkins的登录名和密码）
     *
     * @param createUserId 创建人id
     * @param jenkinsId    Jenkins主键id
     * @return 返回Jenkins信息
     */
    public Jenkins getJenkins(Integer createUserId, Integer jenkinsId) {
        Jenkins queryJenkins = new Jenkins();
        queryJenkins.setId(jenkinsId);
        queryJenkins.setCreateUserId(createUserId);

        Jenkins result = jenkinsMapper.selectOne(queryJenkins);
        if (Objects.isNull(result)) {
            throw new ServiceException("未查到Jenkins信息");
        }
        return result;
    }
}
