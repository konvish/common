package com.kong.common.managerui.iauth.client;

import com.google.common.base.Strings;
import com.kong.common.managerui.iauth.core.Authenticator;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.BaseRequestFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/1/3.
 */
public class DefaultAuthRequestFactory implements BaseRequestFactory {
    public DefaultAuthRequestFactory() {
    }

    public BaseRequest buildFromHttpServletRequest(HttpServletRequest request, HttpServletResponse response, Authenticator authenticator) {
        DefaultAuthRequest baseRequest = new DefaultAuthRequest();
        baseRequest.setRequest(request);
        baseRequest.setResponse(response);
        baseRequest.setAuthenticator(authenticator);
        if(!Strings.isNullOrEmpty(request.getParameter("debug"))) {
            ((DefaultAuthRequest)baseRequest).setDebug(true);
        }

        return baseRequest;
    }
}