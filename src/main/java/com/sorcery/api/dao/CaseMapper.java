package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.dto.cases.QueryCaseListDto;
import com.sorcery.api.entity.Cases;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jinglv
 * @date 2021/01/19
 */
@Repository
public interface CaseMapper extends MySqlExtensionMapper<Cases> {

    /**
     * 统计总数
     *
     * @param params
     * @return
     */
    Integer count(@Param("params") QueryCaseListDto params);

    /**
     * 列表分页查询
     *
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Cases> list(@Param("params") QueryCaseListDto params, @Param("pageNum") Integer pageNum,
                     @Param("pageSize") Integer pageSize);
}