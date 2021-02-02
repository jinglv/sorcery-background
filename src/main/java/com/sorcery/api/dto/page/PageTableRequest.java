package com.sorcery.api.dto.page;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求对象，分页查询参数
 *
 * @author jingLv
 * @date 2021/01/13
 */
@ApiModel(value = "列表查询的分页参数", description = "请求参数类")
@Data
public class PageTableRequest<Dto extends BaseDTO> implements Serializable {

    private static final long serialVersionUID = -8088252233450804552L;
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer pageNum = 1;
    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数据量", required = true, example = "10")
    private Integer pageSize = 10;
    /**
     * 特定查询参数
     */
    @ApiModelProperty(value = "特定查询参数", required = true, example = "status=1")
    private Dto params;
}
