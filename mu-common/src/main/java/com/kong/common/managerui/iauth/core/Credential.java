package com.kong.common.managerui.iauth.core;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class Credential {
    private String secret;
    private String key;
    private Map<String, Object> additionalInformation = Collections.emptyMap();

    public Credential() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}