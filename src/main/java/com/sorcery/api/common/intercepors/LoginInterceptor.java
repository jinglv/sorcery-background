package com.sorcery.api.common.intercepors;

import com.sorcery.api.common.exception.ServiceException;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 登录拦截器
 *
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
     * @param request  请求request
     * @param response 响应response
     * @param handler  处理者
     * @return 返回是否登录的状态
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求request携带的token是已登录状态
        String tokenStr = request.getHeader(UserConstants.LOGIN_TOKEN);

        String requestUri = request.getRequestURI();
        log.info("request.getRequestURI() " + requestUri);

        // 如果为swagger文档地址,直接通过
        boolean swaggerFlag = requestUri.contains("swagger")
                // 过滤spring默认错误页面
                || "/error".equals(requestUri)
                // 过滤csrf
                || "/csrf".equals(requestUri)
                // 过滤http://ip:port/v2/api-docs
                || "/favicon.ico".equals(requestUri)
                || "/".equals(requestUri);
        if (swaggerFlag) {
            return true;
        }

        // 判断请求是否包含token
        if (ObjectUtils.isEmpty(tokenStr)) {
            response.setStatus(401);
            ServiceException.throwEx("客户端未传token, Request Path" + requestUri);
        }

        // 获取token
        TokenDTO tokenDto = tokenDb.getTokenDto(tokenStr);
        // 如果token为空（用户未登录）
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
