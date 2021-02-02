package com.sorcery.api.dto.task;

import com.sorcery.api.dto.BaseDTO;
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
@ApiModel(value = "新增测试任务类", description = "请求参数类")
@Data
public class TaskDTO extends BaseDTO {
    @ApiModelProperty(value = "测试任务信息", required = true)
    private AddTaskDTO testTask;

    @ApiModelProperty(value = "测试用例的id列表", example = "12", required = true)
    private List<Integer> caseIdList;
}
