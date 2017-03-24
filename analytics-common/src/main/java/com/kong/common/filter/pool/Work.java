package com.kong.common.filter.pool;

import java.util.Map;

/**
 * 接口工作任务
 * Created by kong on 2016/1/3.
 */
public interface Work {
    /**
     * 终止执行
     */
    void terminate();

    /**
     * 执行任务
     * @param var1 map
     */
    void execute(Map<String, Object> var1);
}
