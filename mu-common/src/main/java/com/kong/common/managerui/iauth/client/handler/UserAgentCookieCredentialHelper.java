package com.kong.common.managerui.iauth.client.handler;

import com.kong.common.managerui.iauth.client.DefaultAuthRequest;
import com.kong.common.managerui.iauth.client.EncryptionCookieCredential;
import com.kong.common.managerui.iauth.client.token.AccessToken;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.Credential;
import com.kong.common.managerui.iauth.core.exception.AuthNotPassException;
import com.kong.common.managerui.iauth.core.exception.TokenNotExistException;
import com.kong.common.managerui.iauth.core.token.EmbedToken;
import com.kong.common.managerui.iauth.utils.HttpRequestConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * Created by Administrator on 2016/1/3.
 */
public class UserAgentCookieCredentialHelper implements HttpRequestConstant {
    private static Logger logger = LoggerFactory.getLogger(UserAgentCookieCredentialHelper.class);
    private boolean defaultSecret = false;

    public UserAgentCookieCredentialHelper() {
    }

    private Credential generateCredential(String key, String secret) {
        EncryptionCookieCredential credential = new EncryptionCookieCredential();
        credential.setKey(key);
        credential.setSecret(secret);
        return credential;
    }

    private Credential generateCredential(HttpServletRequest res, boolean generateSecret) {
        String userAgent = res.getHeader("user-agent");
        return generateSecret?this.generateCredential(EncryptionCookieCredential.generateKey(userAgent), EncryptionCookieCredential.generateSecret()):this.generateCredential(EncryptionCookieCredential.generateKey(userAgent), (String)null);
    }

    public void checkCredential(BaseRequest baseRequest) {
        AccessToken token = (AccessToken)baseRequest.getToken();
        if(token == null) {
            throw new TokenNotExistException("AccessToken不存在。");
        } else {
            if(token.isSecretRequired()) {
                Credential credential = this.generateCredential(baseRequest.getRequest(), false);
                String userKey = credential.getKey();
                Cookie secretCookie = ((DefaultAuthRequest)baseRequest).getCookieFromRequest(userKey);
                if(secretCookie == null) {
                    throw new AuthNotPassException("验证cookie不存在。");
                }

                credential.setSecret(secretCookie.getValue());
                if(!token.getSecret().equals(credential.getSecret())) {
                    throw new AuthNotPassException("验证cookie不匹配。");
                }

                ((DefaultAuthRequest)baseRequest).setCredential(credential);
            }

        }
    }

    public void clearCredentialEmbedment(BaseRequest baseRequest) {
        Credential credential = ((DefaultAuthRequest)baseRequest).getCredential();
        if(credential != null) {
            ((DefaultAuthRequest)baseRequest).removeCookieToResponse(credential.getKey());
        }

    }

    public void embedCredential(BaseRequest baseRequest) {
        AccessToken token = (AccessToken)((EmbedToken)baseRequest.getToken()).getEmbedToken();
        if(token == null) {
            throw new TokenNotExistException("进行埋点时，获取不到AccessToken");
        } else {
            Credential credential = this.generateCredential(baseRequest.getRequest(), true);
            token.setSecret(credential.getSecret());
            Cookie secretCookie = new Cookie(credential.getKey(), credential.getSecret());
            secretCookie.setPath("/");
            ((DefaultAuthRequest)baseRequest).addCookieToResponse(new Cookie[]{secretCookie});
        }
    }
}
