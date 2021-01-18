package com.sorcery.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jingLv
 * @date 2021/01/18
 */
@Data
@Accessors(chain = true)
public class TokenDto {
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
