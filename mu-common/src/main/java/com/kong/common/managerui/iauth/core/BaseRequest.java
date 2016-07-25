package com.kong.common.managerui.iauth.core;

import com.kong.common.managerui.iauth.core.token.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class BaseRequest implements Serializable {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<Token> tokens;
    private Authenticator authenticator;

    public BaseRequest() {
    }

    public boolean hasToken() {
        return this.getToken() != null;
    }

    public Token getToken() {
        return this.tokens != null && this.tokens.size() != 0?(Token)this.tokens.get(0):null;
    }

    public void consumeToken() {
        if(this.tokens != null) {
            this.tokens.remove(0);
        }

    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
