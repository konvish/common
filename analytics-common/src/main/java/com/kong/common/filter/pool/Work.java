package com.kong.common.filter.pool;

import java.util.Map;

/**
 * Created by kong on 2016/1/3.
 */
public interface Work {
    void terminate();

    void execute(Map<String, Object> var1);
}
