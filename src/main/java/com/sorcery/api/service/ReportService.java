package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.dto.report.AllureReportDTO;
import com.sorcery.api.dto.report.TaskReportDTO;
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
    ResultDTO<AllureReportDTO> getAllureReport(TokenDTO tokenDto, Integer taskId);

    /**
     * 根据任务类型获取任务统计信息
     *
     * @param tokenDto token信息
     * @return 接口返回报告查询结果
     */
    ResultDTO<TaskReportDTO> getTaskByType(TokenDTO tokenDto);

    /**
     * 根据任务状态获取任务统计信息
     *
     * @param tokenDto token信息
     * @return 接口返回报告查询结果
     */
    ResultDTO<TaskReportDTO> getTaskByStatus(TokenDTO tokenDto);

    /**
     * 任务中用例的数量统计信息
     *
     * @param tokenDto token信息
     * @param start    按时间倒叙开始序号
     * @param end      按时间倒叙结束序号
     * @return 接口返回报告查询结果
     */
    ResultDTO<List<Task>> getTaskByCaseCount(TokenDTO tokenDto, Integer start, Integer end);
}
