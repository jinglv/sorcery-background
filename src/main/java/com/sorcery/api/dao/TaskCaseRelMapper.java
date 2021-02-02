package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.dto.cases.TaskCaseRelDetailDTO;
import com.sorcery.api.dto.cases.TaskCaseRelListDTO;
import com.sorcery.api.entity.TaskCaseRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jinglv
 * @date 2021/01/19
 */
@Repository
public interface TaskCaseRelMapper extends MySqlExtensionMapper<TaskCaseRel> {

    List<TaskCaseRelDetailDTO> listDetail(@Param("params") TaskCaseRelListDTO params, @Param("pageNum") Integer pageNum,
                                          @Param("pageSize") Integer pageSize);
}