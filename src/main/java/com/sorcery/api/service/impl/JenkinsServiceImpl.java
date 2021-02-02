package com.sorcery.api.service.impl;

import cn.hutool.core.lang.Assert;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.dao.JenkinsMapper;
import com.sorcery.api.dao.UserMapper;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.dto.jenkins.QueryJenkinsListDTO;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Jenkins;
import com.sorcery.api.entity.User;
import com.sorcery.api.service.JenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Slf4j
@Service
public class JenkinsServiceImpl implements JenkinsService {

    private final JenkinsMapper jenkinsMapper;
    private final UserMapper userMapper;
    private final TokenDb tokenDb;

    public JenkinsServiceImpl(JenkinsMapper jenkinsMapper, UserMapper userMapper, TokenDb tokenDb) {
        this.jenkinsMapper = jenkinsMapper;
        this.userMapper = userMapper;
        this.tokenDb = tokenDb;
    }

    /**
     * 新增Jenkins信息
     *
     * @param tokenDto token信息
     * @param jenkins  Jenkins信息
     * @return 返回接口Jenkins保存结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Jenkins> save(TokenDTO tokenDto, Jenkins jenkins) {
        int insert = jenkinsMapper.insertUseGeneratedKeys(jenkins);
        Assert.isFalse(insert != 1, "新增Jenkins信息失败");
        Integer defaultJenkinsFlag = jenkins.getDefaultJenkinsFlag();
        if (Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag == 1) {
            Integer createUserId = jenkins.getCreateUserId();
            User user = new User();
            user.setId(createUserId);
            user.setDefaultJenkinsId(user.getId());
            // 更新token信息中的默认JenkinsId
            tokenDto.setDefaultJenkinsId(jenkins.getId());
            tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
            int update = userMapper.updateByPrimaryKeySelective(user);
            Assert.isFalse(update != 1, "更新用户信息失败");
        }
        return ResultDTO.success("成功", jenkins);
    }

    /**
     * 删除Jenkins信息
     *
     * @param jenkinsId jenkins主键id
     * @param tokenDto  token信息
     * @return 返回接口Jenkins删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Jenkins> delete(Integer jenkinsId, TokenDTO tokenDto) {
        Jenkins queryJenkins = new Jenkins();
        queryJenkins.setId(jenkinsId);
        queryJenkins.setCreateUserId(tokenDto.getUserId());
        Jenkins result = jenkinsMapper.selectOne(queryJenkins);
        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDTO.fail("未查到Jenkins信息");
        }
        User queryUser = new User();
        queryUser.setId(tokenDto.getUserId());
        User resultUser = userMapper.selectOne(queryUser);
        Integer defaultJenkinsId = resultUser.getDefaultJenkinsId();
        if (Objects.nonNull(defaultJenkinsId) && defaultJenkinsId.equals(jenkinsId)) {
            User user = new User();
            user.setId(tokenDto.getUserId());
            user.setDefaultJenkinsId(null);
            //更新token信息中的默认JenkinsId
            tokenDto.setDefaultJenkinsId(null);
            tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
            int update = userMapper.updateByPrimaryKey(user);
            Assert.isFalse(update != 1, "更新用户信息失败");
        }
        int delete = jenkinsMapper.deleteByPrimaryKey(jenkinsId);
        Assert.isFalse(delete != 1, "删除Jenkins信息失败");
        return ResultDTO.success("成功");
    }

    /**
     * 修改Jenkins信息
     *
     * @param tokenDto token信息
     * @param jenkins  jenkins更新信息
     * @return 返回接口Jenkins更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Jenkins> update(TokenDTO tokenDto, Jenkins jenkins) {
        Jenkins queryJenkins = new Jenkins();
        // 根据jenkinsId查询Jenkins信息
        queryJenkins.setId(jenkins.getId());
        queryJenkins.setCreateUserId(tokenDto.getUserId());
        Jenkins result = jenkinsMapper.selectOne(queryJenkins);
        // 如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDTO.fail("未查到Jenkins信息");
        }
        jenkins.setCreateTime(result.getCreateTime());
        jenkins.setUpdateTime(new Date());
        int update = jenkinsMapper.updateByPrimaryKey(jenkins);
        Assert.isFalse(update != 1, "更新Jenkins信息失败");
        Integer defaultJenkinsFlag = jenkins.getDefaultJenkinsFlag();
        if (Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag == 1) {
            Integer createUserId = jenkins.getCreateUserId();
            User user = new User();
            user.setId(createUserId);
            user.setDefaultJenkinsId(jenkins.getId());
            //更新token信息中的默认JenkinsId
            tokenDto.setDefaultJenkinsId(jenkins.getId());
            tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
            int updateUser = userMapper.updateByPrimaryKey(user);
            Assert.isFalse(updateUser != 1, "更新用户信息失败");
        }
        return ResultDTO.success("成功");
    }

    /**
     * 根据id查询Jenkins信息
     *
     * @param jenkinsId    jenkins主键id
     * @param createUserId 创建人用户id
     * @return 返回接口Jenkins查询结果
     */
    @Override
    public ResultDTO<Jenkins> getById(Integer jenkinsId, Integer createUserId) {
        Jenkins queryJenkins = new Jenkins();
        queryJenkins.setId(jenkinsId);
        queryJenkins.setCreateUserId(createUserId);
        Jenkins result = jenkinsMapper.selectOne(queryJenkins);
        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            ResultDTO.fail("未查到Jenkins信息");
        }
        User queryUser = new User();
        queryUser.setId(createUserId);
        User resultUser = userMapper.selectOne(queryUser);
        Integer defaultJenkinsId = resultUser.getDefaultJenkinsId();
        if (result.getId().equals(defaultJenkinsId)) {
            result.setDefaultJenkinsFlag(1);
        }
        return ResultDTO.success("成功", result);
    }

    /**
     * 查询Jenkins信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口Jenkins分页查询结果
     */
    @Override
    public ResultDTO<PageTableResponse<Jenkins>> list(PageTableRequest<QueryJenkinsListDTO> pageTableRequest) {
        QueryJenkinsListDTO params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();
        Integer createUserId = params.getCreateUserId();

        User queryUser = new User();
        queryUser.setId(createUserId);
        User resultUser = userMapper.selectOne(queryUser);
        Integer defaultJenkinsId = resultUser.getDefaultJenkinsId();
        //总数
        Integer recordsTotal = jenkinsMapper.count(params);
        //分页查询数据
        List<Jenkins> jenkinsList = jenkinsMapper.list(params, (pageNum - 1) * pageSize, pageSize);
        //查找默认Jenkins
        for (Jenkins jenkins : jenkinsList) {
            if (jenkins.getId().equals(defaultJenkinsId)) {
                jenkins.setDefaultJenkinsFlag(1);
            }
        }
        PageTableResponse<Jenkins> jenkinsPageTableResponse = new PageTableResponse<>();
        jenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        jenkinsPageTableResponse.setData(jenkinsList);
        return ResultDTO.success("成功", jenkinsPageTableResponse);
    }
}
