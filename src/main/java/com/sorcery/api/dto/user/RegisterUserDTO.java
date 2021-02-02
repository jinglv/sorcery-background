package com.sorcery.api.dto.user;

import com.sorcery.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jingLv
 * @date 2021/01/18
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "注册用户对象", description = "用户对象user")
@Data
public class RegisterUserDTO extends BaseDTO {
    /**
     * 接口传入的用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    /**
     * 接口传入的密码
     */
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    /**
     * 接口传入的邮箱
     */
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
}
