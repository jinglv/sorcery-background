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
@ApiModel(value = "查询测试用例信息列表对象")
@Data
public class QueryCaseListDTO extends BaseDTO {
    @ApiModelProperty(value = "测试用例名称")
    private String caseName;

    @ApiModelProperty(value = "创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
