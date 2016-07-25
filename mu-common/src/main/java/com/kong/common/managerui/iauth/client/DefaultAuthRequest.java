package com.kong.common.managerui.iauth.client;

import com.kong.common.managerui.domain.User;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.Credential;
import com.kong.common.managerui.iauth.core.Principal;

import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public class DefaultAuthRequest extends BaseRequest {
    private boolean isDebug = false;
    private Principal<User> principal;
    private Credential credential;
    private Map<String, Object> additionalInformation = Collections.emptyMap();

    public DefaultAuthRequest() {
    }

    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Principal<User> getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(Principal<User> principal) {
        this.principal = principal;
    }

    public Credential getCredential() {
        return this.credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Cookie getCookieFromRequest(String name) {
        Cookie[] cookies = this.getRequest().getCookies();
        if(cookies == null) {
            return null;
        } else {
            Cookie[] arr$ = cookies;
            int len$ = cookies.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Cookie cookie = arr$[i$];
                if(cookie.getName().equalsIgnoreCase(name)) {
                    return cookie;
                }
            }

            return null;
        }
    }

    public void addCookieToResponse(Cookie... cookies) {
        Cookie[] arr$ = cookies;
        int len$ = cookies.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Cookie cookie = arr$[i$];
            this.getResponse().addCookie(cookie);
        }

    }

    public void removeCookieToResponse(String name) {
        Cookie cookie = new Cookie(name, (String)null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        this.addCookieToResponse(new Cookie[]{cookie});
    }

    public boolean isDebug() {
        return this.isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }
}
