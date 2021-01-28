package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.common.utils.StrUtils;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.RequestInfoDto;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.dto.task.*;
import com.sorcery.api.entity.Task;
import com.sorcery.api.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResultDto<Task> save(HttpServletRequest request, @RequestBody TaskDto taskDto) {
        log.info("添加测试任务，请求参数：{}", JSONUtil.parse(taskDto));
        if (Objects.isNull(taskDto)) {
            return ResultDto.fail("测试任务入参不能为空");
        }
        AddTaskDto addTaskDto = taskDto.getTestTask();
        if (Objects.isNull(addTaskDto)) {
            return ResultDto.fail("测试任务不能为空");
        }
        if (Objects.isNull(addTaskDto.getName())) {
            return ResultDto.fail("测试任务名称不能为空");
        }
        List<Integer> caseIdList = taskDto.getCaseIdList();
        if (Objects.isNull(caseIdList) || caseIdList.size() == 0) {
            return ResultDto.fail("测试用例不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
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
    public ResultDto<Task> update(HttpServletRequest request, @RequestBody UpdateTaskDto updateTaskDto) {
        log.info("修改测试任务信息，请求参数：{}", JSONUtil.parse(updateTaskDto));
        if (Objects.isNull(updateTaskDto)) {
            return ResultDto.fail("测试任务信息不能为空");
        }
        Integer taskId = updateTaskDto.getId();
        String name = updateTaskDto.getName();
        if (Objects.isNull(taskId)) {
            return ResultDto.fail("任务id不能为空");
        }
        if (ObjectUtils.isEmpty(name)) {
            return ResultDto.fail("任务名称不能为空");
        }
        Task task = new Task();
        // CopyUtil.copyPropertiesCglib(updateHogwartsTestTaskDto, hogwartsTestTask);
        task.setId(updateTaskDto.getId())
                .setName(updateTaskDto.getName())
                .setRemark(updateTaskDto.getRemark());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
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
    public ResultDto<Task> getById(HttpServletRequest request, @PathVariable Integer taskId) {
        log.info("根据任务Id查询，taskId:{} ", taskId);
        if (Objects.isNull(taskId)) {
            return ResultDto.success("任务Id不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
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
    public ResultDto<Task> delete(HttpServletRequest request, @PathVariable Integer taskId) {
        log.info("根据任务Id删除测试任务，taskId:{}", taskId);
        if (Objects.isNull(taskId)) {
            return ResultDto.fail("任务Id不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
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
    public ResultDto<PageTableResponse<Task>> list(HttpServletRequest request, PageTableRequest<QueryTaskListDto> pageTableRequest) {
        log.info("测试任务列表查询，请求分页查询参数：{}", JSONUtil.parse(pageTableRequest));
        if (Objects.isNull(pageTableRequest)) {
            return ResultDto.success("列表查询参数不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryTaskListDto params = pageTableRequest.getParams();
        if (Objects.isNull(params)) {
            params = new QueryTaskListDto();
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
    public ResultDto<Task> updateStatus(HttpServletRequest request, @RequestBody UpdateTaskStatusDto updateTaskStatusDto) {
        log.info("修改测试任务状态,请求参数：{} ", JSONUtil.parse(updateTaskStatusDto));
        if (Objects.isNull(updateTaskStatusDto)) {
            return ResultDto.fail("修改测试任务状态信息不能为空");
        }
        Integer taskId = updateTaskStatusDto.getTaskId();
        String buildUrl = updateTaskStatusDto.getBuildUrl();
        Integer status = updateTaskStatusDto.getStatus();
        if (Objects.isNull(taskId)) {
            return ResultDto.success("任务id不能为空");
        }
        if (ObjectUtils.isEmpty(buildUrl)) {
            return ResultDto.success("任务构建地址不能为空");
        }
        if (ObjectUtils.isEmpty(status)) {
            return ResultDto.success("任务状态码不能为空");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setBuildUrl(buildUrl);
        task.setStatus(status);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        task.setCreateUserId(tokenDto.getUserId());
        return taskService.updateStatus(task);
    }

    /**
     * 测试任务执行
     *
     * @param request      HttpServletRequest
     * @param startTestDto 测试任务执行请求参数
     * @return 返回请求接口结果
     * @throws Exception 异常
     */
    @PostMapping("start")
    @ApiOperation(value = "测试任务执行", notes = "测试任务执行", httpMethod = "POST", response = ResultDto.class)
    public ResultDto<String> testStart(HttpServletRequest request
            , @ApiParam(name = "修改测试任务状态对象", required = true) @RequestBody StartTestDto startTestDto) throws Exception {
        log.info("执行测试任务，请求参数：{}", JSONUtil.parse(startTestDto));
        if (Objects.isNull(startTestDto)) {
            return ResultDto.fail("执行测试请求不能为空");
        }
        if (Objects.isNull(startTestDto.getTaskId())) {
            return ResultDto.fail("测试任务id不能为空");
        }
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        log.info("token信息：{}", token);

        Task task = new Task();
        task.setId(startTestDto.getTaskId());
        task.setTestCommand(startTestDto.getTestCommand());
        
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        task.setCreateUserId(tokenDto.getUserId());
        task.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        String url = request.getRequestURL().toString();
        log.info("请求地址:{}", url);
        url = StrUtils.getHostAndPort(request.getRequestURL().toString());

        RequestInfoDto requestInfoDto = new RequestInfoDto();
        requestInfoDto.setBaseUrl(url);
        requestInfoDto.setRequestUrl(url);
        requestInfoDto.setToken(token);
        log.info("请求参数:{}", JSONUtil.parse(requestInfoDto));
        return taskService.startTask(tokenDto, requestInfoDto, task);
    }
}
