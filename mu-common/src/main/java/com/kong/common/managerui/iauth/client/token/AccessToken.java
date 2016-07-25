package com.kong.common.managerui.iauth.client.token;

import com.kong.common.managerui.iauth.core.token.Token;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/1/3.
 */
public class AccessToken implements Token {
    private final int weight = 10;
    private String tokenType = "access_token".toLowerCase();
    private String value;
    private String secret;
    private Date birthday;
    private Map<String, Object> additionalInformation = Collections.emptyMap();
    private long userId;

    public AccessToken() {
        this.value = UUID.randomUUID().toString();
        this.birthday = new Date();
    }

    public AccessToken(String value) {
        this.value = value;
    }

    public AccessToken(String value, String secret) {
        this.value = value;
        this.secret = secret;
    }

    public int getWeight() {
        return 10;
    }

    public String getValue() {
        return this.value;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return this.secret;
    }

    public boolean isSecretRequired() {
        return this.secret != null;
    }

    public long getExpireSecond() {
        return 1800L;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof AccessToken)) {
            return false;
        } else if(!super.equals(o)) {
            return false;
        } else {
            AccessToken that = (AccessToken)o;
            return !this.value.equals(that.getValue())?false:!this.isSecretRequired() || this.secret.equals(that.getSecret());
        }
    }
}
