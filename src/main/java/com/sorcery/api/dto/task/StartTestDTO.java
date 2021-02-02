package com.sorcery.api.dto.task;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "执行测试任务类", description = "请求参数类")
@Data
public class StartTestDTO extends BaseDTO {
    /**
     * ID
     */
    @ApiModelProperty(value = "测试任务id", required = true, example = "112")
    private Integer taskId;

    /**
     * 执行测试的命令脚本
     */
    @ApiModelProperty(value = "执行测试的命令脚本", example = "mvn test")
    private String testCommand;
}
