package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.Token;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.user.LoginUser;
import com.sorcery.api.dto.user.RegisterUser;
import com.sorcery.api.entity.User;
import com.sorcery.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/18
 */
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户注册", notes = "用户注册接口")
    @PostMapping("register")
    public ResultDto<User> save(@RequestBody RegisterUser registerUser) {
        log.info("用户注册传入参数：{}", JSONUtil.parse(registerUser));
        if (Objects.isNull(registerUser)) {
            return ResultDto.fail("用户信息不能为空");
        }
        String username = registerUser.getUsername();
        if (ObjectUtils.isEmpty(username)) {
            return ResultDto.fail("用户名不能为空");
        }
        String password = registerUser.getPassword();
        if (ObjectUtils.isEmpty(password)) {
            return ResultDto.fail("用户密码不能为空");
        }
        User user = new User();
        // CopyUtils.copyPropertiesCglib(registerUser, user);
        user.setUsername(registerUser.getUsername()).setPassword(registerUser.getPassword()).setEmail(registerUser.getEmail());
        return userService.save(user);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("login")
    public ResultDto<Token> login(@RequestBody LoginUser loginUser) {
        log.info("用户登录传入参数：{}", JSONUtil.parse(loginUser));
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            return ResultDto.fail("用户名或密码不能为空");
        }
        return userService.login(username, password);
    }
}
