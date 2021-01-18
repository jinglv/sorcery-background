package com.sorcery.api.constants;

/**
 * 用户相关常量
 *
 * @author jingLv
 * @date 2021/01/18
 */
public interface UserConstants {

    /**
     * MD5
     */
    String MD5_HEX_SIGN = "sorcery";

    /**
     * 登陆token(nginx中默认header无视下划线)
     */
    String LOGIN_TOKEN = "token";
}
