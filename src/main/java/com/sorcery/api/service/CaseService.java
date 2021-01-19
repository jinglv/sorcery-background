package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.cases.QueryCaseListDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Cases;

/**
 * @author jingLv
 * @date 2021/01/19
 */
public interface CaseService {
    /**
     * 新增测试用例
     *
     * @param cases 测试用例信息
     * @return 返回接口测试用例保存结果
     */
    ResultDto<Cases> save(Cases cases);

    /**
     * 删除测试用例信息
     *
     * @param caseId       测试用例主键id
     * @param createUserId 创建人用户id
     * @return 返回接口测试用例删除结果
     */
    ResultDto<Cases> delete(Integer caseId, Integer createUserId);

    /**
     * 修改测试用例信息
     *
     * @param cases 测试用例更新信息
     * @return 返回接口测试用例更新结果
     */
    ResultDto<Cases> update(Cases cases);

    /**
     * 根据id查询测试用例
     *
     * @param caseId       测试用例主键id
     * @param createUserId 创建人用户id
     * @return 返回接口测试用例查询结果
     */
    ResultDto<Cases> getById(Integer caseId, Integer createUserId);

    /**
     * 查询Jenkins信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口测试用例分页查询结果
     */
    ResultDto<PageTableResponse<Cases>> list(PageTableRequest<QueryCaseListDto> pageTableRequest);

    /**
     * 根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
     *
     * @param createUserId 创建人用户id
     * @param caseId       测试用例主键id
     * @return 返回接口测试用例分页结果
     */
    String getCaseDataById(Integer createUserId, Integer caseId);
}
