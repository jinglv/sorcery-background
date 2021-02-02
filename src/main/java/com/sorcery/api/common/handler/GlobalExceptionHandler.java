package com.sorcery.api.common.handler;


import com.sorcery.api.common.exception.ServiceException;
import com.sorcery.api.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常捕获类，主要分业务异常和其他异常两种
 * ControllerAdvice:全局捕获异常，异常集中处理，更好的使业务逻辑与异常处理剥离开，定义在类上
 * ExceptionHandler(value=Exception.class)统一处理某一类异常，声明该方法用于捕获value所指的类型的异常（注意：当该异常的子父类都被声明时，按照先子后父的顺序进行捕获）
 * ResponseStatus：将某正异常映射为HTTP状态码，可以用在方法上，也可以用在类上（自定义运行时异常类）
 * resultFormat(T ex):将异常转换为统一响应的对象
 *
 * @author jingLv
 * @date 2021/01/13
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 自定义业务异常
     *
     * @param se 定义业务异常类
     * @return 统一异常处理结果
     */
    @ExceptionHandler(ServiceException.class)
    public ResultDTO<ServiceException> serviceExceptionHandler(ServiceException se) {
        log.error("业务异常，", se);
        return resultFormat(se);
    }

    /**
     * Exception异常
     *
     * @param ex Exception异常类
     * @return 统一异常处理结果
     */
    @ExceptionHandler(Exception.class)
    public ResultDTO<Exception> exception(Exception ex) {
        return resultFormat(ex);
    }

    /**
     * Throwable异常，Http状态为500
     *
     * @param th Throwable异常类
     * @return 统一异常处理结果
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDTO<Throwable> exception(Throwable th) {
        log.error("服务暂不可用:{}", th.getMessage());
        return resultFormat(th);

    }

    /**
     * 异常结果统一格式化
     *
     * @param ex  异常类型
     * @param <T> 泛型类型
     * @return 返回结果ResultDto
     */
    private <T extends Throwable> ResultDTO<T> resultFormat(T ex) {
        log.error("全局异常捕获，", ex);
        ResultDTO<T> resultDto = ResultDTO.newInstance();
        resultDto.setAsFailure();
        if (ex instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) ex;
            resultDto.setMessage(serviceException.getMessage());
        } else {
            resultDto.setMessage("服务暂不可用，" + ex.getMessage());
        }
        return resultDto;
    }
}
