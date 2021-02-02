package com.sorcery.api.common.token;

import com.sorcery.api.dto.TokenDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 存储用户登录的token信息（类似缓存管理器）
 *
 * @author jingLv
 * @date 2021/01/18
 */
@Component
public class TokenDb {
    /**
     * 定义用于缓存token的map(token=key)， key就是token字符串
     */
    private final Map<String, TokenDTO> tokenMap = new HashMap<>();

    /**
     * 获取在线用户操作（未使用）
     *
     * @return 返回在线用户的数量
     */
    public int getTokenMapSize() {
        return tokenMap.size();
    }

    /**
     * 根据token获取TokenDto
     *
     * @param token 生成的token
     * @return 返回已获取token的TokenDto
     */
    public TokenDTO getTokenDto(String token) {
        if (ObjectUtils.isEmpty(token)) {
            return new TokenDTO();
        }
        return tokenMap.get(token);
    }

    /**
     * 用户登录时获取token和TokenDto
     * 也可以实现成登录用户互踢，2种方式，1是id前后缀，2是id-token=map的key-value
     *
     * @param token    生成的token
     * @param tokenDto 接口传入根据token获取的用户信息
     * @return 返回已添加token的token信息
     */
    public TokenDTO addTokenDto(String token, TokenDTO tokenDto) {
        if (Objects.isNull(tokenDto)) {
            return null;
        }
        return tokenMap.put(token, tokenDto);
    }

    /**
     * 退出登录时，移除token
     *
     * @param token 生成的token
     * @return 返回已移除的token的token信息
     */
    public TokenDTO removeTokenDto(String token) {
        if (Objects.isNull(token)) {
            return null;
        }
        return tokenMap.remove(token);
    }

    /**
     * 验证token是否为null，判断用户是否登录
     *
     * @param token 生成的token
     * @return 返回判断token是否为空
     */
    public boolean isLogin(String token) {
        return tokenMap.get(token) != null;
    }

}
