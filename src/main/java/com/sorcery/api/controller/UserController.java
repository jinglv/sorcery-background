package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.Token;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.user.LoginUser;
import com.sorcery.api.dto.user.RegisterUser;
import com.sorcery.api.entity.User;
import com.sorcery.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "是否已经登录", notes = "是否已经登录接口")
    @GetMapping("isLogin")
    public ResultDto<TokenDto> isLogin(HttpServletRequest request) {
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);
        TokenDto tokenDto = null;
        if (loginFlag) {
            log.info("用户已登录");
            tokenDto = tokenDb.getTokenDto(token);
        } else {
            log.info("用户未登录");
        }
        return ResultDto.success("成功", tokenDto);
    }

    @ApiModelProperty(value = "是否已经登录", notes = "是否已经登录接口")
    @DeleteMapping("logout")
    public ResultDto<TokenDto> logout(HttpServletRequest request) {
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);
        TokenDto tokenDto = null;
        if (!loginFlag) {
            return ResultDto.fail("用户未登录，无需退出");
        } else {
            log.info("用户退出登录");
            tokenDto = tokenDb.removeTokenDto(token);
        }
        return ResultDto.success("成功", tokenDto);
    }
}
