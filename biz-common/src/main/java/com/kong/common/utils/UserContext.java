package com.kong.common.utils;

import com.kong.common.domain.UserDomain;
/**
 * 用户上下文
 * Created by kong on 2016/1/3.
 */
public class UserContext {
    private static ThreadLocal<UserDomain> context = new ThreadLocal<UserDomain>();

    public static UserDomain getCurrentUser(){
        return context.get();
    }

    public static void setCurrentUser(UserDomain user){
        //缓存记录
//        SessionCacheFactory.getInstance().put(user.getName(), user);
        context.set(user);
    }

    /**
     * 应该显示调用
     */
    public static void removeCurrentUser() {
        context.remove();
    }

    /**
     * 将缓存的数据放入当前线程
     * @param userName
     */
    @Deprecated
    public static void setCurrentUser(String userName){
        context.set(SessionCacheFactory.getInstance().get(userName));
    }

    @Deprecated
    public static void removeUser(String userName){
        SessionCacheFactory.getInstance().remove(userName);
    }
}
