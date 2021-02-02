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
@ApiModel(value = "用户登录对象", description = "用户对象user")
@Data
public class LoginUserDTO extends BaseDTO {
    /**
     * 登录用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = true, dataType = "String", notes = "唯一不可重复", example = "tester")
    private String username;

    /**
     * 登录用户密码
     */
    @ApiModelProperty(value = "用户名", name = "password", required = true, dataType = "String", notes = "字母+数字，6-18位", example = "tester123")
    private String password;
}
