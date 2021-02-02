package com.sorcery.api.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.Token;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dao.UserMapper;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.entity.User;
import com.sorcery.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * 用户操作服务实现
 *
 * @author jingLv
 * @date 2021/01/18
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final TokenDb tokenDb;

    public UserServiceImpl(UserMapper userMapper, TokenDb tokenDb) {
        this.userMapper = userMapper;
        this.tokenDb = tokenDb;
    }

    /**
     * 通过id获取用户信息
     *
     * @param id user id主键
     * @return 返回接口用户结果
     */
    @Override
    public ResultDTO<User> getById(Integer id) {
        User queryUser = new User();
        queryUser.setId(id);
        User user = userMapper.selectOne(queryUser);
        if (Objects.isNull(user)) {
            return ResultDTO.fail("用户不存在");
        }
        return ResultDTO.success("成功", user);
    }

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 返回接口用户结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<User> save(User user) {
        // 获取用户名
        String username = user.getUsername();
        // 获取密码
        String password = user.getPassword();
        // 查询条件
        User queryUser = new User();
        queryUser.setUsername(username);
        // 根据用户名在数据库中查询数据
        User queryResult = userMapper.selectOne(queryUser);
        if (Objects.nonNull(queryResult)) {
            return ResultDTO.fail("用户名已存在");
        }
        // 密码进行加密，并保存
        String newPwd = DigestUtils.md5DigestAsHex((UserConstants.MD5_HEX_SIGN + username + password).getBytes());
        user.setPassword(newPwd);
        // 插入用户注册数据
        int insert = userMapper.insert(user);
        Assert.isFalse(insert != 1, "用户注册失败");
        return ResultDTO.success("成功", user);
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回接口用户登录结果
     */
    @Override
    public ResultDTO<TokenDTO> login(String username, String password) {
        // 登录密码进行加密，后续进行查询
        String pwd = DigestUtils.md5DigestAsHex((UserConstants.MD5_HEX_SIGN + username + password).getBytes());
        // 实例用户信息
        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        // 根据用户名密码查询用户
        User queryUser = userMapper.selectOne(user);
        Assert.notNull(queryUser, "用户信息已存在，查询条件User={}", JSONUtil.parse(user));
        // Token信息
        Token token = new Token();
        String tokenStr = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + username + password).getBytes());
        token.setToken(tokenStr);
        // 将登陆信息存入token对象中
        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setUserId(queryUser.getId())
                .setUsername(queryUser.getUsername())
                .setDefaultJenkinsId(queryUser.getDefaultJenkinsId())
                .setToken(tokenStr);
        log.info("登录存入的token信息：{}", JSONUtil.parse(tokenDto));
        // 将Token信息存入TokenDb中
        TokenDTO loginToken = tokenDb.addTokenDto(tokenStr, tokenDto);
        log.info("登陆完成的信息：{}", JSONUtil.parse(loginToken));
        return ResultDTO.success("成功", tokenDto);
    }
}
