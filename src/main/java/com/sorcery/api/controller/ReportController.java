package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.dto.report.AllureReportDTO;
import com.sorcery.api.dto.report.TaskReportDTO;
import com.sorcery.api.entity.Task;
import com.sorcery.api.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Slf4j
@Api(tags = "测试报告管理")
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final TokenDb tokenDb;

    public ReportController(ReportService reportService, TokenDb tokenDb) {
        this.reportService = reportService;
        this.tokenDb = tokenDb;
    }

    /**
     * 根据任务id获取allure报告
     *
     * @param request HttpServletRequest
     * @param taskId  任务id
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据任务id获取allure报告")
    @GetMapping("{taskId}")
    public ResultDTO<AllureReportDTO> getAllureReport(HttpServletRequest request, @PathVariable Integer taskId) {
        log.info("根据任务id获取Allure测试报告地址，taskId：{} ", taskId);
        if (Objects.isNull(taskId)) {
            return ResultDTO.fail("任务id不能为空");
        }
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return reportService.getAllureReport(tokenDto, taskId);
    }

    /**
     * 根据任务类型获取任务统计信息 - 饼状图
     *
     * @param request HttpServletRequest
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据任务类型获取任务统计信息 - 饼状图")
    @GetMapping("type")
    public ResultDTO<TaskReportDTO> getTaskByType(HttpServletRequest request) {
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("根据任务类型获取任务统计信息,请求参数：{} ", JSONUtil.parse(tokenDto));
        return reportService.getTaskByType(tokenDto);
    }

    /**
     * 根据任务状态获取任务统计信息 - 饼状图
     *
     * @param request HttpServletRequest
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据任务状态获取任务统计信息 - 饼状图")
    @GetMapping("status")
    public ResultDTO<TaskReportDTO> getTaskByStatus(HttpServletRequest request) {
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("根据任务类型获取任务统计信息,请求参数：{}", JSONUtil.parse(tokenDto));
        return reportService.getTaskByStatus(tokenDto);
    }

    /**
     * 任务中用例的数量统计信息 - 折线图
     *
     * @param request HttpServletRequest
     * @param start   按时间倒叙开始序号
     * @param end     按时间倒叙结束序号
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "任务中用例的数量统计信息 - 折线图")
    @GetMapping("count")
    public ResultDTO<List<Task>> getTaskByCaseCount(HttpServletRequest request
            , @RequestParam(value = "start", required = false) Integer start
            , @RequestParam(value = "end", required = false) Integer end) {
        TokenDTO tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("根据任务类型获取任务统计信息,请求参数：{}", JSONUtil.parse(tokenDto));
        return reportService.getTaskByCaseCount(tokenDto, start, end);
    }

}
