package com.kong.common.service;

/**
 * 是否需要数据权限注入
 * Created by kong on 2016/1/3.
 */
public interface IDataPermAware {
    /**
     * 是否进行数据权限
     * @return true 需要数据权限控制
     */
    public boolean getEnableDataPerm();
}
