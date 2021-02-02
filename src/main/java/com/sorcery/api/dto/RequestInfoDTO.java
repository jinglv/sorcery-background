package com.sorcery.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RequestInfoDTO extends BaseDTO {
    /**
     * 请求的接口地址
     */
    @ApiModelProperty(value = "请求的接口地址，用于拼装命令", hidden = true)
    private String requestUrl;
    /**
     * 请求的服务器地址
     */
    @ApiModelProperty(value = "请求的服务器地址，用于拼装命令", hidden = true)
    private String baseUrl;

    /**
     * 文件服务器地址
     */
    @ApiModelProperty(value = "文件服务器地址，用于拼装命令", hidden = true)
    private String fileServer;

    /**
     * token信息
     */
    @ApiModelProperty(value = "token信息，用于拼装命令", hidden = true)
    private String token;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型", example = "2")
    private Integer operType;
}
