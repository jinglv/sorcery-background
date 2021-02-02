package com.sorcery.api.dto.jenkins;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@ApiModel(value = "添加Jenkins对象")
@EqualsAndHashCode(callSuper = true)
@Data
public class JenkinsDTO extends BaseDTO {
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
     * 用户名
     */
    @ApiModelProperty(value = "Jenkins用户名称", required = true)
    private String jenkinsUsername;

    /**
     * 密码
     */
    @ApiModelProperty(value = "Jenkins用户密码", required = true)
    private String jenkinsPassword;

    /**
     * 备注
     */
    @ApiModelProperty(value = "Jenkins备注")
    private String remark;

    /**
     * 是否默认Jenkins服务
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
