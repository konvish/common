package com.kong.common.utils;

import com.kong.common.domain.UserDomain;
/**
 * TODO 一句话描述该类用途
 * Created by kong on 2016/1/3.
 */
@Deprecated
public class SessionCacheFactory {

    private static class SessionCacheHolder {
        private static SessionCache<String, UserDomain> instance = new SessionCache<String, UserDomain>(500);
    }

    private SessionCacheFactory() {
    }

    public static SessionCache<String, UserDomain> getInstance() {
        return SessionCacheHolder.instance;
    }

}