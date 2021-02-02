package com.sorcery.api.dto.report;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "执行测试任务类", description = "请求参数类")
@Data
public class TaskDataDTO extends BaseDTO {
    /**
     * 任务数量
     */
    @ApiModelProperty(value = "任务数量", required = true)
    private Integer taskCount;

    /**
     * 分类的key
     */
    @ApiModelProperty(value = "分类的key", required = true)
    private Integer taskKey;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
}
