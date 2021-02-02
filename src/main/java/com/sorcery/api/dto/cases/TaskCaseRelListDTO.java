package com.sorcery.api.dto.cases;

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
@ApiModel(value = "查询任务关联的测试用例详情列表对象")
@Data
public class TaskCaseRelListDTO extends BaseDTO {
    @ApiModelProperty(value = "用例标识")
    private String caseSign;

    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    @ApiModelProperty(value = "创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
