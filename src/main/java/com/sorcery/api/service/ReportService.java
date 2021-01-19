package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.report.AllureReportDto;
import com.sorcery.api.dto.report.TaskReportDto;
import com.sorcery.api.entity.Task;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/19
 */
public interface ReportService {
    /**
     * 获取allure报告
     *
     * @param tokenDto token信息
     * @param taskId   执行任务id
     * @return 返回接口allure测试报告
     */
    ResultDto<AllureReportDto> getAllureReport(TokenDto tokenDto, Integer taskId);

    /**
     * 根据任务类型获取任务统计信息
     *
     * @param tokenDto token信息
     * @return 接口返回报告查询结果
     */
    ResultDto<TaskReportDto> getTaskByType(TokenDto tokenDto);

    /**
     * 根据任务状态获取任务统计信息
     *
     * @param tokenDto token信息
     * @return 接口返回报告查询结果
     */
    ResultDto<TaskReportDto> getTaskByStatus(TokenDto tokenDto);

    /**
     * 任务中用例的数量统计信息
     *
     * @param tokenDto token信息
     * @param start    按时间倒叙开始序号
     * @param end      按时间倒叙结束序号
     * @return 接口返回报告查询结果
     */
    ResultDto<List<Task>> getTaskByCaseCount(TokenDto tokenDto, Integer start, Integer end);
}
