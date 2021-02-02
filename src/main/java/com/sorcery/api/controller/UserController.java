package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.dto.user.LoginUserDTO;
import com.sorcery.api.dto.user.RegisterUserDTO;
import com.sorcery.api.entity.User;
import com.sorcery.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private final TokenDb tokenDb;

    public UserController(UserService userService, TokenDb tokenDb) {
        this.userService = userService;
        this.tokenDb = tokenDb;
    }

    /**
     * 用户注册接口
     *
     * @param registerUser 用户注册请求信息
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "用户注册", notes = "用户注册接口")
    @PostMapping("register")
    public ResultDTO<User> save(@RequestBody RegisterUserDTO registerUser) {
        log.info("用户注册传入参数：{}", JSONUtil.parse(registerUser));
        if (Objects.isNull(registerUser)) {
            return ResultDTO.fail("用户信息不能为空");
        }
        String username = registerUser.getUsername();
        if (ObjectUtils.isEmpty(username)) {
            return ResultDTO.fail("用户名不能为空");
        }
        String password = registerUser.getPassword();
        if (ObjectUtils.isEmpty(password)) {
            return ResultDTO.fail("用户密码不能为空");
        }
        User user = new User();
        // CopyUtils.copyPropertiesCglib(registerUser, user);
        user.setUsername(registerUser.getUsername())
                .setPassword(registerUser.getPassword())
                .setEmail(registerUser.getEmail());
        return userService.save(user);
    }

    /**
     * 用户登录接口
     *
     * @param loginUser 用户登录请求信息
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("login")
    public ResultDTO<TokenDTO> login(@RequestBody LoginUserDTO loginUser) {
        log.info("用户登录传入参数：{}", JSONUtil.parse(loginUser));
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            return ResultDTO.fail("用户名或密码不能为空");
        }
        return userService.login(username, password);
    }

    /**
     * 判断请求是否为登录状态
     *
     * @param request 前端请求
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "是否已经登录", notes = "是否已经登录接口")
    @GetMapping("isLogin")
    public ResultDTO<TokenDTO> isLogin(HttpServletRequest request) {
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);
        TokenDTO tokenDto = null;
        if (loginFlag) {
            log.info("用户已登录");
            tokenDto = tokenDb.getTokenDto(token);
        } else {
            log.info("用户未登录");
        }
        return ResultDTO.success("成功", tokenDto);
    }

    /**
     * 用户退出登录
     *
     * @param request 前端请求
     * @return 返回请求接口结果
     */
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @DeleteMapping("logout")
    public ResultDTO<TokenDTO> logout(HttpServletRequest request) {
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);
        TokenDTO tokenDto = null;
        if (!loginFlag) {
            return ResultDTO.fail("用户未登录，无需退出");
        } else {
            log.info("用户退出登录");
            tokenDto = tokenDb.removeTokenDto(token);
        }
        return ResultDTO.success("成功", tokenDto);
    }
}
