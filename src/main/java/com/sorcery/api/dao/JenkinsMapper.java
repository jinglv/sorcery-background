package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.dto.jenkins.QueryJenkinsListDTO;
import com.sorcery.api.entity.Jenkins;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jinglv
 * @date 2021/01/19
 */
@Repository
public interface JenkinsMapper extends MySqlExtensionMapper<Jenkins> {

    /**
     * 统计总数
     *
     * @param params
     * @return
     */
    Integer count(@Param("params") QueryJenkinsListDTO params);

    /**
     * 列表分页查询
     *
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Jenkins> list(@Param("params") QueryJenkinsListDTO params,
                       @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
}