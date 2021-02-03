package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.common.utils.StrUtils;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.RequestInfoDTO;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.dto.task.*;
import com.sorcery.api.entity.Task;
import com.sorcery.api.entity.User;
import com.sorcery.api.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/25
 */
@Slf4j
@Api(tags = "测试任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final TokenDb tokenDb;

    public TaskController(TaskService taskService, TokenDb tokenDb) {
        this.taskService = taskService;
        this.tokenDb = tokenDb;
    }

    /**
     * 添加测试任务
     *
     * @param request HttpServletRequest
     * @param taskDto 测试任务信息
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "添加测试任务")
    @PostMapping
    public ResultDTO<Task> save(HttpServletRequest request, @RequestBody TaskDTO taskDto) {
        log.info("添加测试任务，请求参数：{}", JSONUtil.parse(taskDto));
        if (Objects.isNull(taskDto)) {
            return ResultDTO.fail("测试任务入参不能为空");
        }
        AddTaskDTO addTaskDto = taskDto.getTestTask();
        if (Objects.isNull(addTaskDto)) {
            return ResultDTO.fail("测试任务不能为空");
        }
        if (Objects.isNull(addTaskDto.getName())) {
            return ResultDTO.fail("测试任务名称不能为空");
        }
        List<Integer> caseIdList = taskDto.getCaseIdList();
        if (Objects.isNull(caseIdList) || caseIdList.size() == 0) {
            return ResultDTO.fail("测试用例不能为空");
        }
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("添加测试任务，token信息：{}", JSONUtil.parse(tokenDto));
        addTaskDto.setCreateUserId(tokenDto.getUserId());
        addTaskDto.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        return taskService.save(taskDto, Constants.TASK_TYPE_ONE);
    }

    /**
     * 修改测试任务
     *
     * @param request       HttpServletRequest
     * @param updateTaskDto 更新测试任务信息
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "修改测试任务")
    @PutMapping
    public ResultDTO<Task> update(HttpServletRequest request, @RequestBody UpdateTaskDTO updateTaskDto) {
        log.info("修改测试任务信息，请求参数：{}", JSONUtil.parse(updateTaskDto));
        if (Objects.isNull(updateTaskDto)) {
            return ResultDTO.fail("测试任务信息不能为空");
        }
        Integer taskId = updateTaskDto.getId();
        String name = updateTaskDto.getName();
        if (Objects.isNull(taskId)) {
            return ResultDTO.fail("任务id不能为空");
        }
        if (ObjectUtils.isEmpty(name)) {
            return ResultDTO.fail("任务名称不能为空");
        }
        Task task = new Task();
        // CopyUtil.copyPropertiesCglib(updateHogwartsTestTaskDto, hogwartsTestTask);
        task.setId(updateTaskDto.getId())
                .setName(updateTaskDto.getName())
                .setRemark(updateTaskDto.getRemark());
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        task.setCreateUserId(tokenDto.getUserId());
        return taskService.update(task);
    }

    /**
     * 根据任务Id查询测试任务信息
     *
     * @param request HttpServletRequest
     * @param taskId  任务id
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "根据任务Id查询测试任务信息")
    @GetMapping("{taskId}")
    public ResultDTO<Task> getById(HttpServletRequest request, @PathVariable Integer taskId) {
        log.info("根据任务Id查询，taskId:{} ", taskId);
        if (Objects.isNull(taskId)) {
            return ResultDTO.success("任务Id不能为空");
        }
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return taskService.getById(taskId, tokenDto.getUserId());
    }

    /**
     * 根据任务Id删除测试任务
     *
     * @param request HttpServletRequest
     * @param taskId  任务id
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "根据任务Id删除测试任务")
    @DeleteMapping("{taskId}")
    public ResultDTO<Task> delete(HttpServletRequest request, @PathVariable Integer taskId) {
        log.info("根据任务Id删除测试任务，taskId:{}", taskId);
        if (Objects.isNull(taskId)) {
            return ResultDTO.fail("任务Id不能为空");
        }
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return taskService.delete(taskId, tokenDto.getUserId());
    }

    /**
     * 分页查询测试任务
     *
     * @param request          HttpServletRequest
     * @param pageTableRequest 分页查询参数
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("list")
    public ResultDTO<PageTableResponse<Task>> list(HttpServletRequest request, PageTableRequest<QueryTaskListDTO> pageTableRequest) {
        log.info("测试任务列表查询，请求分页查询参数：{}", JSONUtil.parse(pageTableRequest));
        if (Objects.isNull(pageTableRequest)) {
            return ResultDTO.success("列表查询参数不能为空");
        }
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryTaskListDTO params = pageTableRequest.getParams();
        if (Objects.isNull(params)) {
            params = new QueryTaskListDTO();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);
        return taskService.list(pageTableRequest);
    }

    /**
     * 修改测试任务状态
     *
     * @param request             HttpServletRequest
     * @param updateTaskStatusDto 修改测试任务状态信息
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "修改测试任务状态")
    @PutMapping("status")
    public ResultDTO<Task> updateStatus(HttpServletRequest request, @RequestBody UpdateTaskStatusDTO updateTaskStatusDto) {
        log.info("修改测试任务状态,请求参数：{} ", JSONUtil.parse(updateTaskStatusDto));
        if (Objects.isNull(updateTaskStatusDto)) {
            return ResultDTO.fail("修改测试任务状态信息不能为空");
        }
        Integer taskId = updateTaskStatusDto.getTaskId();
        String buildUrl = updateTaskStatusDto.getBuildUrl();
        Integer status = updateTaskStatusDto.getStatus();
        if (Objects.isNull(taskId)) {
            return ResultDTO.success("任务id不能为空");
        }
        if (ObjectUtils.isEmpty(buildUrl)) {
            return ResultDTO.success("任务构建地址不能为空");
        }
        if (ObjectUtils.isEmpty(status)) {
            return ResultDTO.success("任务状态码不能为空");
        }
        Task task = new Task();
        task.setId(taskId)
                .setBuildUrl(buildUrl)
                .setStatus(status);
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        task.setCreateUserId(tokenDto.getUserId());
        return taskService.updateStatus(task);
    }

    /**
     * 测试任务执行
     *
     * @param request      HttpServletRequest
     * @param startTestDto 测试任务执行请求参数
     * @return 返回请求接口结果
     */
    @PostMapping("start")
    @ApiOperation(value = "测试任务执行", notes = "测试任务执行", httpMethod = "POST", response = ResultDTO.class)
    public ResultDTO<User> testStart(HttpServletRequest request
            , @ApiParam(name = "修改测试任务状态对象", required = true) @RequestBody StartTestDTO startTestDto) throws IOException {
        log.info("执行测试任务，请求参数：{}", JSONUtil.parse(startTestDto));
        if (Objects.isNull(startTestDto)) {
            return ResultDTO.fail("执行测试请求不能为空");
        }
        if (Objects.isNull(startTestDto.getTaskId())) {
            return ResultDTO.fail("测试任务id不能为空");
        }
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        log.info("token信息：{}", token);

        Task task = new Task();
        task.setId(startTestDto.getTaskId());
        task.setTestCommand(startTestDto.getTestCommand());
        // 从TokenDb中获取TokenDTO
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        task.setCreateUserId(tokenDto.getUserId());
        task.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        String url = request.getRequestURL().toString();
        log.info("请求地址:{}", url);
        url = StrUtils.getHostAndPort(request.getRequestURL().toString());

        // 组装RequestInfoDTO参数
        RequestInfoDTO requestInfoDto = new RequestInfoDTO();
        requestInfoDto.setBaseUrl(url);
        requestInfoDto.setRequestUrl(url);
        requestInfoDto.setToken(token);
        log.info("请求参数:{}", JSONUtil.parse(requestInfoDto));
        // 调用startTask并返回结果
        return taskService.startTask(tokenDto, requestInfoDto, task);
    }
}
