package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.cases.TaskCaseRelDetailDto;
import com.sorcery.api.dto.cases.TaskCaseRelListDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.service.TaskCaseRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/22
 */
@Slf4j
@Api(tags = "任务与用例关联管理")
@RestController
@RequestMapping("/cases/rel")
public class TaskCaseRelController {
    private final TaskCaseRelService taskCaseRelService;
    private final TokenDb tokenDb;

    public TaskCaseRelController(TaskCaseRelService taskCaseRelService, TokenDb tokenDb) {
        this.taskCaseRelService = taskCaseRelService;
        this.tokenDb = tokenDb;
    }

    /**
     * 任务与用例关联管理详情
     *
     * @param request          servlet request
     * @param pageTableRequest 分页请求参数
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "任务与用例关联管理详情")
    @GetMapping("detail")
    public ResultDto<PageTableResponse<TaskCaseRelDetailDto>> list(HttpServletRequest request, PageTableRequest<TaskCaseRelListDto> pageTableRequest) {
        log.info("任务与用例关联管理列表查询，请求参数：{}", JSONUtil.parse(pageTableRequest));
        if (Objects.isNull(pageTableRequest)) {
            return ResultDto.fail("列表查询参数不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        TaskCaseRelListDto params = pageTableRequest.getParams();
        if (Objects.isNull(params)) {
            params = new TaskCaseRelListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);
        return taskCaseRelService.listDetail(pageTableRequest);
    }
}
