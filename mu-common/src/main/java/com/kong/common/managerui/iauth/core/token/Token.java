package com.kong.common.managerui.iauth.core.token;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public interface Token extends Serializable {
    String ACCESS_TOKEN = "access_token";
    String EMBED_TOKEN = "embed_token";

    int getWeight();

    String getValue();

    String getSecret();

    boolean isSecretRequired();

    long getExpireSecond();

    String getTokenType();

    Date getBirthday();

    Map<String, Object> getAdditionalInformation();
}
