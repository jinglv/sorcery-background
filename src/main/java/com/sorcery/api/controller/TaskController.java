package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.task.AddTaskDto;
import com.sorcery.api.dto.task.TaskDto;
import com.sorcery.api.entity.Task;
import com.sorcery.api.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
