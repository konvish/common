package com.kong.common.context;

import java.util.Map;

/**
 * 获取业务上下文接口，需要业务方实现
 * Created by kong on 2016/1/3.
 */
public interface IUserContext {
    /** 用户id key  */
    public static final String UID = "uid";

    /**
     * 获取业务上下文
     * @return
     */
    public Map<String, Object> getContexts();
}
