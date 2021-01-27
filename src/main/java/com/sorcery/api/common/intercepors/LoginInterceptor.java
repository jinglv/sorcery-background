package com.sorcery.api.common.intercepors;

import com.sorcery.api.common.exception.ServiceException;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/27
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final TokenDb tokenDb;

    public LoginInterceptor(TokenDb tokenDb) {
        this.tokenDb = tokenDb;
    }

    /**
     * 这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenStr = request.getHeader(UserConstants.LOGIN_TOKEN);

        String requestUri = request.getRequestURI();
        log.info("request.getRequestURI() " + requestUri);

        //如果为swagger文档地址,直接通过
        boolean swaggerFlag = requestUri.contains("swagger")
                //过滤spring默认错误页面
                || "/error".equals(requestUri)
                //过滤csrf
                || "/csrf".equals(requestUri)
                //过滤http://127.0.0.1:8093/v2/api-docs
                || "/favicon.ico".equals(requestUri)
                //演示map local 不用校验是否登录
                || "/report/showMapLocal".equals(requestUri)
                || "/".equals(requestUri);
        if (swaggerFlag) {
            return true;
        }

        //如果请求中含有token
        if (ObjectUtils.isEmpty(tokenStr)) {
            response.setStatus(401);
            ServiceException.throwEx("客户端未传token " + requestUri);
        }

        //获取token
        TokenDto tokenDto = tokenDb.getTokenDto(tokenStr);
        //如果user未登录
        if (Objects.isNull(tokenDto)) {
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
            //此处直接抛出异常
            response.setStatus(401);
            ServiceException.throwEx("用户未登录");
            return false;
        } else {
            //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
