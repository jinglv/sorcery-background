package com.sorcery.api.dto.report;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "执行测试任务类", description = "请求参数类")
@Data
public class TaskReportDTO extends BaseDTO {
    /**
     * 任务总和
     */
    @ApiModelProperty(value = "任务总和", required = true)
    private Integer taskSum;

    /**
     * 任务数据对象
     */
    @ApiModelProperty(value = "任务数据对象", required = true)
    private List<TaskDataDTO> taskDataDTOList;

}
