package com.kong.common.managerui.iauth.core.token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/1/3.
 */
public class EmbedToken implements Token {
    private final int weight = 100;
    private String tokenType = "embed_token".toLowerCase();
    private String value = UUID.randomUUID().toString();
    private Date birthday = new Date();
    private Map<String, Object> additionalInformation = new HashMap();
    private String embedTokenType;
    private Token embedToken;

    public EmbedToken() {
    }

    public String getEmbedTokenType() {
        return this.embedTokenType;
    }

    public void setEmbedTokenType(String embedTokenType) {
        this.embedTokenType = embedTokenType;
    }

    public int getWeight() {
        return 100;
    }

    public String getValue() {
        return this.value;
    }

    public String getSecret() {
        return null;
    }

    public boolean isSecretRequired() {
        return false;
    }

    public long getExpireSecond() {
        return 5L;
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

    public Token getEmbedToken() {
        return this.embedToken;
    }

    public void setEmbedToken(Token embedToken) {
        this.embedToken = embedToken;
    }
}
