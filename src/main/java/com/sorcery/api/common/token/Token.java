package com.sorcery.api.common.token;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Restful方式登陆token
 *
 * @author jingLv
 * @date 2021/01/18
 */
@Data
public class Token implements Serializable {

    private static final long serialVersionUID = -9059118205638859690L;

    /**
     * 登录需要的token
     */
    private String token;

    /**
     * 到期时间
     */
    private Date expireTime;
}
