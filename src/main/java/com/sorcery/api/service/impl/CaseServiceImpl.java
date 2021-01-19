package com.sorcery.api.service.impl;

import cn.hutool.core.lang.Assert;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.dao.CaseMapper;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.cases.QueryCaseListDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Cases;
import com.sorcery.api.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Slf4j
@Service
public class CaseServiceImpl implements CaseService {

    private final CaseMapper caseMapper;

    public CaseServiceImpl(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    /**
     * 新增测试用例
     *
     * @param cases 测试用例信息
     * @return 返回接口测试用例保存结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<Cases> save(Cases cases) {
        cases.setDelFlag(Constants.DEL_FLAG_ONE);
        int result = caseMapper.insertUseGeneratedKeys(cases);
        Assert.isFalse(result != 1, "新增测试用例失败");
        return ResultDto.success("成功", cases);
    }

    /**
     * 删除测试用例信息
     *
     * @param caseId       测试用例主键id
     * @param createUserId 创建人用户id
     * @return 返回接口测试用例删除结果
     */
    @Override
    public ResultDto<Cases> delete(Integer caseId, Integer createUserId) {
        Cases queryCase = new Cases();
        queryCase.setId(caseId);
        queryCase.setCreateUserId(createUserId);
        queryCase.setDelFlag(Constants.DEL_FLAG_ONE);
        Cases result = caseMapper.selectOne(queryCase);
        // 如果为空，则提示
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试用例信息");
        }
        result.setDelFlag(Constants.DEL_FLAG_ZERO);
        int update = caseMapper.updateByPrimaryKey(result);
        Assert.isFalse(update != 1, "修改测试用例失败");
        return ResultDto.success("成功");
    }

    /**
     * 修改测试用例信息
     *
     * @param cases 测试用例更新信息
     * @return 返回接口测试用例更新结果
     */
    @Override
    public ResultDto<Cases> update(Cases cases) {
        return null;
    }

    /**
     * 根据id查询测试用例
     *
     * @param caseId       测试用例主键id
     * @param createUserId 创建人用户id
     * @return 返回接口测试用例查询结果
     */
    @Override
    public ResultDto<Cases> getById(Integer caseId, Integer createUserId) {
        return null;
    }

    /**
     * 查询Jenkins信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口测试用例分页查询结果
     */
    @Override
    public ResultDto<PageTableResponse<Cases>> list(PageTableRequest<QueryCaseListDto> pageTableRequest) {
        return null;
    }

    /**
     * 根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
     *
     * @param createUserId 创建人用户id
     * @param caseId       测试用例主键id
     * @return 返回接口测试用例分页结果
     */
    @Override
    public String getCaseDataById(Integer createUserId, Integer caseId) {
        return null;
    }
}
