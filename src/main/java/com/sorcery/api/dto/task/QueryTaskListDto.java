package com.sorcery.api.dto.task;

import com.sorcery.api.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "查询任务信息列表对象")
@Data
public class QueryTaskListDto extends BaseDto {
    @ApiModelProperty(value = "Jenkins名称")
    private String name;

    @ApiModelProperty(value = "创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}