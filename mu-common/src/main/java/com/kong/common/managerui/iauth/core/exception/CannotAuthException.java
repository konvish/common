package com.kong.common.managerui.iauth.core.exception;

/**
 * Created by Administrator on 2016/1/3.
 */
public class CannotAuthException extends RuntimeException {
    public CannotAuthException() {
        super("认证失败。认证过程出错。");
    }

    public CannotAuthException(String message) {
        super(message);
    }

    public CannotAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotAuthException(Throwable cause) {
        super(cause);
    }

    public CannotAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
