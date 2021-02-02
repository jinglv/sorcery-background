package com.sorcery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础返回对象
 *
 * @author jingLv
 * @date 2021/01/13
 */

@ApiModel(value = "基础返回对象", description = "基础返回对象")
public class ResultDTO<T> extends BaseDTO {
    
    /**
     * 响应码
     */
    @ApiModelProperty(value = "返回结果码 1 成功 0 失败", required = true, example = "1", allowableValues = "1,0")
    @Setter
    @Getter
    private Integer resultCode;

    /**
     * 响应信息
     */
    @ApiModelProperty(value = "提示信息", example = "成功", allowableValues = "成功,失败")
    @Setter
    @Getter
    private String message;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应结果数据")
    @Setter
    @Getter
    private T data;

    public static <T> ResultDTO<T> newInstance() {
        return new ResultDTO<>();
    }

    /**
     * 设置为成功状态
     */
    public void setAsSuccess() {
        this.resultCode = 1;
    }

    public static <T> ResultDTO<T> success(String message) {
        return success(message, null);
    }

    public static <T> ResultDTO<T> success(String message, T data) {
        ResultDTO<T> resultDto = new ResultDTO<>();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }

    /**
     * 设置为失败状态
     */
    public void setAsFailure() {
        this.resultCode = 0;
    }

    public static <T> ResultDTO<T> fail(String message) {
        return fail(message, null);
    }

    public static <T> ResultDTO<T> fail(String message, T data) {
        ResultDTO<T> resultDto = new ResultDTO<>();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }
}
