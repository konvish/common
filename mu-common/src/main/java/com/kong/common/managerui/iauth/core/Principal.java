package com.kong.common.managerui.iauth.core;

import com.kong.common.managerui.iauth.core.token.Token;

import javax.security.auth.Subject;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public class Principal<T> implements java.security.Principal {
    private T owner;
    private Token token;
    private Map<String, Object> additionalInformation = Collections.emptyMap();

    public Principal() {
    }

    public T getOwner() {
        return this.owner;
    }

    public void setOwner(T owner) {
        this.owner = owner;
    }

    public Token getToken() {
        return this.token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getSecret() {
        return this.token == null?null:this.token.getSecret();
    }

    public String getName() {
        return this.token == null && this.owner == null?null:"passed";
    }

    public boolean implies(Subject subject) {
        return false;
    }
}

