package com.kong.common.utils;

import com.kong.common.exception.BizException;
/**
 * Created by Administrator on 2016/1/3.
 */
public class Exceptions {
    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static BizException bizException(Throwable ex) {
        if (ex instanceof BizException) {

            return (BizException) ex;
        } else {
            return new BizException("error code",ex.getMessage(),ex.getCause());
        }
    }
}
