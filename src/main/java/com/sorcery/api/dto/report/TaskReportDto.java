package com.sorcery.api.dto.report;

import com.sorcery.api.dto.BaseDto;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "执行测试任务类", description = "请求参数类")
@Data
public class TaskReportDto extends BaseDto {
    /**
     * 任务总和
     */
    @ApiModelProperty(value = "任务总和", required = true)
    @NotNull
    private Integer taskSum;

    /**
     * 任务数据对象
     */
    @ApiModelProperty(value = "任务数据对象", required = true)
    private List<TaskDataDto> taskDataDtoList;

}