package com.kong.common.service;

/**
 * 数据权限接口   （业务相关接口）
 * Created by kong on 2016/1/3.
 */
public interface IDataPermService {
    /**
     * 拼装当前用户特定业务模型主体的数据权限sql
     * @param resUrl 资源url
     * @return
     */
    public String makeDataPermSql(String resUrl);
}
