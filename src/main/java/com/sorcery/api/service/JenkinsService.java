package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.jenkins.QueryJenkinsListDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Jenkins;

/**
 * @author jingLv
 * @date 2021/01/19
 */
public interface JenkinsService {
    /**
     * 新增Jenkins信息
     *
     * @param tokenDto token信息
     * @param jenkins  Jenkins信息
     * @return 返回接口Jenkins保存结果
     */
    ResultDto<Jenkins> save(TokenDto tokenDto, Jenkins jenkins);

    /**
     * 删除Jenkins信息
     *
     * @param tokenDto  token信息
     * @param jenkinsId jenkins主键id
     * @return 返回接口Jenkins删除结果
     */
    ResultDto<Jenkins> delete(Integer jenkinsId, TokenDto tokenDto);

    /**
     * 修改Jenkins信息
     *
     * @param tokenDto token信息
     * @param jenkins  jenkins更新信息
     * @return 返回接口Jenkins更新结果
     */
    ResultDto<Jenkins> update(TokenDto tokenDto, Jenkins jenkins);

    /**
     * 根据id查询Jenkins信息
     *
     * @param jenkinsId    jenkins主键id
     * @param createUserId 创建人用户id
     * @return 返回接口Jenkins查询结果
     */
    ResultDto<Jenkins> getById(Integer jenkinsId, Integer createUserId);

    /**
     * 查询Jenkins信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口Jenkins分页查询结果
     */
    ResultDto<PageTableResponse<Jenkins>> list(PageTableRequest<QueryJenkinsListDto> pageTableRequest);

}
