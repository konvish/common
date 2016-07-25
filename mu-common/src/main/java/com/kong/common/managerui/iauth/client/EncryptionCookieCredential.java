package com.kong.common.managerui.iauth.client;

import com.kong.common.managerui.iauth.core.Credential;
import com.kong.security.utils.Cryptos;
import com.kong.security.utils.Digests;
import com.kong.security.utils.Encodes;
/**
 * Created by Administrator on 2016/1/3.
 */
public class EncryptionCookieCredential extends Credential {
    public EncryptionCookieCredential() {
    }

    public static String generateKey(String input) {
        return Encodes.encodeHex(Digests.md5(input.getBytes())).substring(0, 5);
    }

    public static String generateSecret() {
        return Encodes.encodeBase64(Cryptos.generateSecret(6));
    }
}