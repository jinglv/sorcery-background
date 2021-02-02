package com.sorcery.api.dto.jenkins;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/22
 */
@ApiModel(value = "更新Jenkins对象")
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateJenkinsDTO extends BaseDTO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "Jenkins主键", required = true)
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "Jenkins名称", required = true)
    private String name;

    /**
     * 测试命令
     */
    @ApiModelProperty(value = "测试命令前缀", required = true)
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    @ApiModelProperty(value = "Jenkins的baseUrl", required = true)
    private String url;

    /**
     * Jenkins登录用户名
     */
    @ApiModelProperty(value = "Jenkins用户名称", required = true)
    private String jenkinsUsername;

    /**
     * Jenkins登录密码
     */
    @ApiModelProperty(value = "Jenkins用户密码", required = true)
    private String jenkinsPassword;

    /**
     * 备注
     */
    @ApiModelProperty(value = "Jenkins备注")
    private String remark;

    /**
     *
     */
    @ApiModelProperty(value = "是否设置为默认服务器 1 是 0 否", required = true)
    private Integer defaultJenkinsFlag;

    /**
     *
     */
    @ApiModelProperty(value = "命令运行的测试用例类型  1 文本 2 文件", required = true)
    private Integer commandRunCaseType;

    /**
     *
     */
    @ApiModelProperty(value = "测试用例后缀名 如果case为文件时，此处必填", required = true)
    private String commandRunCaseSuffix;
}
