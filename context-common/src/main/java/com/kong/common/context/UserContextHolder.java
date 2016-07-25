package com.kong.common.context;

/**
 * Created by kong on 2016/1/3.
 */
public class UserContextHolder {
    private static ThreadLocal<IUserContext> userContextThreadLocal = new ThreadLocal<IUserContext>();

    public static void setUserContext(IUserContext userContext){
        userContextThreadLocal.set(userContext);
    }

    public static IUserContext getUserContext(){
        return userContextThreadLocal.get();
    }

}
