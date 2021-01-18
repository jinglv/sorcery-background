package com.sorcery.api.common.exception;

import lombok.Setter;

/**
 * 自定义异常类
 *
 * @author jingLv
 * @date 2021/01/13
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1187908056498545158L;

    @Setter
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public ServiceException(final String message, Throwable th) {
        super(message, th);
        this.message = message;
    }

    public ServiceException(final String message) {
        this.message = message;
    }

    public static void throwEx(String message) {
        throw new ServiceException(message);
    }
}
