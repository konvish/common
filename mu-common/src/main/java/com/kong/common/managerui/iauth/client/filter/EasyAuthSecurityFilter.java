package com.kong.common.managerui.iauth.client.filter;

import com.kong.common.managerui.iauth.core.Authenticator;
import com.kong.common.managerui.iauth.core.filter.DelegableSecurityFilter;
/**
 * Created by Administrator on 2016/1/3.
 */
public class EasyAuthSecurityFilter extends DelegableSecurityFilter {
    private Authenticator authenticator;

    public EasyAuthSecurityFilter() {
    }

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }
}