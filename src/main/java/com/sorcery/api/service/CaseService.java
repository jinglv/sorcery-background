package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.cases.QueryCaseListDTO;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Cases;

/**
 * 测试用例服务层
 *
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
    ResultDTO<Cases> save(Cases cases);

    /**
     * 删除测试用例信息
     *
     * @param caseId       测试用例主键id
     * @param createUserId 创建人用户id
     * @return 返回接口测试用例删除结果
     */
    ResultDTO<Cases> delete(Integer caseId, Integer createUserId);

    /**
     * 修改测试用例信息
     *
     * @param cases 测试用例更新信息
     * @return 返回接口测试用例更新结果
     */
    ResultDTO<Cases> update(Cases cases);

    /**
     * 根据id查询测试用例
     *
     * @param caseId       测试用例主键id
     * @param createUserId 创建人用户id
     * @return 返回接口测试用例查询结果
     */
    ResultDTO<Cases> getById(Integer caseId, Integer createUserId);

    /**
     * 查询测试用例信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口测试用例分页查询结果
     */
    ResultDTO<PageTableResponse<Cases>> list(PageTableRequest<QueryCaseListDTO> pageTableRequest);

    /**
     * 根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
     *
     * @param createUserId 创建人用户id
     * @param caseId       测试用例主键id
     * @return 返回接口测试用例分页结果
     */
    ResultDTO<String> getCaseDataById(Integer createUserId, Integer caseId);
}
