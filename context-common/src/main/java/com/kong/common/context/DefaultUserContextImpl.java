package com.kong.common.context;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Administrator on 2016/1/3.
 */
public class DefaultUserContextImpl implements IUserContext{
    protected Map<String, Object> contexts = new HashMap<String, Object>();

    /**
     * 设置上下文，子类重载
     */
    public void setContexts(Map<String, Object> contexts){
        this.contexts = contexts;
    }


    public Map<String, Object> getContexts() {
        return contexts;
    }
}
