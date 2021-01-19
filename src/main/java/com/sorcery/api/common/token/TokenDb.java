package com.sorcery.api.common.token;

import com.sorcery.api.dto.TokenDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/18
 */
@Component
public class TokenDb {
    /**
     * key就是token字符串
     */
    private final Map<String, TokenDto> tokenMap = new HashMap<>();

    public int getTokenMapSize() {
        return tokenMap.size();
    }

    public TokenDto getTokenDto(String token) {

        if (ObjectUtils.isEmpty(token)) {
            return new TokenDto();
        }

        return tokenMap.get(token);
    }

    /**
     * 也可以实现成登录用户互踢，2种方式，1是id前后缀，2是id-token=map的key-value
     *
     * @param token
     * @param tokenDto
     * @return
     */
    public TokenDto addTokenDto(String token, TokenDto tokenDto) {
        if (Objects.isNull(tokenDto)) {
            return null;
        }
        return tokenMap.put(token, tokenDto);
    }

    /**
     * 移除token
     *
     * @param token
     * @return
     */
    public TokenDto removeTokenDto(String token) {
        if (Objects.isNull(token)) {
            return null;
        }
        return tokenMap.remove(token);
    }

    /**
     * 验证token是否为null，判断用户是否登录
     *
     * @param token
     * @return
     */
    public boolean isLogin(String token) {
        return tokenMap.get(token) != null;
    }

}
