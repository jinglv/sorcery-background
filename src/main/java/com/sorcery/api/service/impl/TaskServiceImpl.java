package com.sorcery.api.service.impl;

import com.sorcery.api.dto.RequestInfoDto;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.dto.task.QueryTaskListDto;
import com.sorcery.api.dto.task.TaskDto;
import com.sorcery.api.entity.Task;
import com.sorcery.api.service.TaskService;

import java.io.IOException;

/**
 * @author jingLv
 * @date 2021/01/21
 */
public class TaskServiceImpl implements TaskService {
    /**
     * 新增测试任务信息
     *
     * @param taskDto  task信息
     * @param taskType task类型
     * @return 返回接口task保存结果
     */
    @Override
    public ResultDto<Task> save(TaskDto taskDto, Integer taskType) {
        return null;
    }

    /**
     * 删除测试任务信息
     *
     * @param taskId       taskId主键
     * @param createUserId 创建人用户id
     * @return 返回接口task删除结果
     */
    @Override
    public ResultDto<Task> delete(Integer taskId, Integer createUserId) {
        return null;
    }

    /**
     * 修改测试任务信息
     *
     * @param task task信息
     * @return 返回接口task更新结果
     */
    @Override
    public ResultDto<Task> update(Task task) {
        return null;
    }

    /**
     * 根据id查询
     *
     * @param taskId       askId主键
     * @param createUserId 创建人用户id
     * @return 返回接口task查询结果
     */
    @Override
    public ResultDto<Task> getById(Integer taskId, Integer createUserId) {
        return null;
    }

    /**
     * 查询测试任务信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口task分页查询结果
     */
    @Override
    public ResultDto<PageTableResponse<Task>> list(PageTableRequest<QueryTaskListDto> pageTableRequest) {
        return null;
    }

    /**
     * 开始执行测试任务信息
     *
     * @param tokenDto       token信息
     * @param requestInfoDto 接口请求信息
     * @param task           任务信息
     * @return 返回执行结果
     * @throws IOException io异常
     */
    @Override
    public ResultDto<Object> startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, Task task) throws IOException {
        return null;
    }

    /**
     * 修改测试任务状态信息
     *
     * @param task task信息
     * @return 返回接口task修改状态结果
     */
    @Override
    public ResultDto<Task> updateStatus(Task task) {
        return null;
    }
}
