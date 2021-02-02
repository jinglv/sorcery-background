package com.sorcery.api.dto.cases;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "修改测试用例对象")
@Data
public class UpdateCaseDTO extends BaseDTO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "测试用例主键", required = true)
    private Integer id;

    /**
     * 测试用例数据
     */
    @ApiModelProperty(value = "测试用例数据", required = true)
    private String caseData;

    /**
     * 用例名称
     */
    @ApiModelProperty(value = "测试用例名称", required = true)
    private String caseName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "测试用例备注", required = true)
    private String remark;
}
