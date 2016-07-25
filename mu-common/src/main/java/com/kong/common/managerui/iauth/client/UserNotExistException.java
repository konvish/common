package com.kong.common.managerui.iauth.client;

import com.kong.common.managerui.iauth.core.exception.CannotAuthException;

/**
 * Created by Administrator on 2016/1/3.
 */
public class UserNotExistException extends CannotAuthException {
    public UserNotExistException() {
        super("用户不存在。");
    }

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotExistException(Throwable cause) {
        super(cause);
    }

    public UserNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
