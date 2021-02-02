package com.sorcery.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据token获取的用户信息
 *
 * @author jingLv
 * @date 2021/01/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TokenDTO extends BaseDTO {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 默认Jenkins服务器
     */
    private Integer defaultJenkinsId;
    /**
     * 接口需要的token
     */
    private String token;
}
