package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.entity.User;

/**
 * 用户操作服务层
 *
 * @author jingLv
 * @date 2021/01/18
 */
public interface UserService {
    /**
     * 通过id获取用户信息
     *
     * @param id user id主键
     * @return 返回接口用户查询结果
     */
    ResultDTO<User> getById(Integer id);

    /**
     * 保存用户信息（用户注册）
     *
     * @param user 用户信息
     * @return 返回接口用户保存结果
     */
    ResultDTO<User> save(User user);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回接口用户登录结果
     */
    ResultDTO<TokenDTO> login(String username, String password);
}
