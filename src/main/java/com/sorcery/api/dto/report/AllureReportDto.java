package com.sorcery.api.dto.report;

import lombok.Data;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Data
public class AllureReportDto {
    /**
     * 任务id
     */
    private Integer taskId;
    /**
     * Allure测试报告Url
     */
    private String allureReportUrl;
}
