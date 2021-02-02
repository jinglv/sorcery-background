package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.dto.report.TaskDataDTO;
import com.sorcery.api.dto.task.QueryTaskListDTO;
import com.sorcery.api.entity.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jinglv
 * @date 2021/01/19
 */
@Repository
public interface TaskMapper extends MySqlExtensionMapper<Task> {

    /**
     * 根据任务类型进行查询
     *
     * @param createUserId 创建人id
     * @return 返回统计数据
     */
    List<TaskDataDTO> getTaskByType(@Param("createUserId") Integer createUserId);

    /**
     * 根据任务状态进行查询
     *
     * @param createUserId 创建人id
     * @return 返回统计数据
     */
    List<TaskDataDTO> getTaskByStatus(@Param("createUserId") Integer createUserId);

    /**
     * 根据任务数量进行查询
     *
     * @param createUserId 创建人id
     * @param start        按时间倒叙开始序号
     * @param end          按时间倒叙结束序号
     * @return 返回统计数据
     */
    List<Task> getCaseCountByTask(@Param("createUserId") Integer createUserId, @Param("start") Integer start,
                                  @Param("end") Integer end);

    /**
     * 统计总数
     *
     * @param params 查询参数
     * @return 返回统计结果
     */
    Integer count(@Param("params") QueryTaskListDTO params);

    /**
     * 列表分页查询
     *
     * @param params   查询参数
     * @param pageNum  每页数量
     * @param pageSize 页数
     * @return 返回查询结果
     */
    List<Task> list(@Param("params") QueryTaskListDTO params, @Param("pageNum") Integer pageNum,
                    @Param("pageSize") Integer pageSize);

}