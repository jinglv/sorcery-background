package com.sorcery.api.service;

import com.sorcery.api.dto.RequestInfoDTO;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.dto.task.QueryTaskListDTO;
import com.sorcery.api.dto.task.TaskDTO;
import com.sorcery.api.entity.Task;
import com.sorcery.api.entity.User;

import java.io.IOException;

/**
 * @author jingLv
 * @date 2021/01/19
 */
public interface TaskService {
    /**
     * 新增测试任务信息
     *
     * @param taskDto  task信息
     * @param taskType task类型
     * @return 返回接口task保存结果
     */
    ResultDTO<Task> save(TaskDTO taskDto, Integer taskType);

    /**
     * 删除测试任务信息
     *
     * @param taskId       taskId主键
     * @param createUserId 创建人用户id
     * @return 返回接口task删除结果
     */
    ResultDTO<Task> delete(Integer taskId, Integer createUserId);

    /**
     * 修改测试任务信息
     *
     * @param task task信息
     * @return 返回接口task更新结果
     */
    ResultDTO<Task> update(Task task);

    /**
     * 根据id查询
     *
     * @param taskId       askId主键
     * @param createUserId 创建人用户id
     * @return 返回接口task查询结果
     */
    ResultDTO<Task> getById(Integer taskId, Integer createUserId);

    /**
     * 查询测试任务信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口task分页查询结果
     */
    ResultDTO<PageTableResponse<Task>> list(PageTableRequest<QueryTaskListDTO> pageTableRequest);

    /**
     * 开始执行测试任务信息
     *
     * @param tokenDto       token信息
     * @param requestInfoDto 接口请求信息
     * @param task           任务信息
     * @return 返回执行结果
     */
    ResultDTO<User> startTask(TokenDTO tokenDto, RequestInfoDTO requestInfoDto, Task task) throws IOException;

    /**
     * 修改测试任务状态信息
     *
     * @param task task信息
     * @return 返回接口task修改状态结果
     */
    ResultDTO<Task> updateStatus(Task task);
}
