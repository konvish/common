package com.kong.common.managerui.iauth.client.handler;

import com.kong.common.managerui.domain.User;
import com.kong.common.managerui.iauth.client.DefaultAuthRequest;
import com.kong.common.managerui.iauth.client.UserNotExistException;
import com.kong.common.managerui.iauth.client.token.AccessToken;
import com.kong.common.managerui.iauth.client.token.storage.UserStore;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.Principal;
import com.kong.common.managerui.iauth.core.exception.CannotAuthException;
import com.kong.common.managerui.iauth.core.exception.TokenNotExistException;
import com.kong.common.managerui.iauth.core.handler.AbstractTokenHandler;
import com.kong.common.managerui.iauth.core.token.EmbedToken;
import com.kong.common.managerui.iauth.core.token.Token;
import com.kong.common.managerui.iauth.core.token.storage.TokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import java.io.IOException;


/**
 * Created by Administrator on 2016/1/3.
 */
public class AccessTokenHandler extends AbstractTokenHandler {
    private static Logger logger = LoggerFactory.getLogger(AccessTokenHandler.class);
    private UserAgentCookieCredentialHelper userAgentCookieCredentialHandlerHelper = new UserAgentCookieCredentialHelper();
    private String handleTokenType = "access_token";
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private UserStore userStore;
    private boolean userDetailRequired = true;

    public AccessTokenHandler() {
    }

    public TokenStore getTokenStore() {
        return this.tokenStore;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public boolean isUserDetailRequired() {
        return this.userDetailRequired;
    }

    /** @deprecated */
    @Deprecated
    public void setUserDetailRequired(boolean userDetailRequired) {
        this.userDetailRequired = userDetailRequired;
    }

    public void invoke(BaseRequest baseRequest) {
        AccessToken token = (AccessToken)baseRequest.getToken();
        if(token == null) {
            throw new TokenNotExistException("获取不到AccessToken。");
        } else {
            Principal principal = new Principal();
            principal.setToken(token);
            if(this.isUserDetailRequired()) {
                try {
                    User e = this.userStore.readUser(Long.valueOf(token.getUserId()));
                    if(e == null) {
                        throw new CannotAuthException("userStore中不存在此用户。");
                    }

                    principal.setOwner(e);
                } catch (Exception var5) {
                    throw new CannotAuthException("从userStore获取用户失败。", var5);
                }
            }

            ((DefaultAuthRequest)baseRequest).setPrincipal(principal);
            this.userAgentCookieCredentialHandlerHelper.checkCredential(baseRequest);
        }
    }

    public void clear(BaseRequest baseRequest) {
        ((DefaultAuthRequest)baseRequest).removeCookieToResponse("iauth-access-token");
        this.userAgentCookieCredentialHandlerHelper.clearCredentialEmbedment(baseRequest);
    }

    public void embed(BaseRequest baseRequest) {
        EmbedToken embedToken = (EmbedToken)baseRequest.getToken();
        Object userId = embedToken.getAdditionalInformation().get("userId");
        User user = null;

        try {
            user = this.userStore.readUser(userId);
            if(user == null) {
                throw new UserNotExistException("userStore中不存在此用户。");
            }
        } catch (Exception var7) {
            throw new UserNotExistException("从userStore获取用户失败。", var7);
        }

        AccessToken accessToken = new AccessToken();
        accessToken.setUserId(Long.valueOf(String.valueOf(user.getId())).longValue());
        this.tokenStore.store(accessToken.getValue(), accessToken);
        Cookie tokenCookie = new Cookie("iauth-access-token", accessToken.getValue());
        tokenCookie.setPath("/");
        ((DefaultAuthRequest)baseRequest).addCookieToResponse(new Cookie[]{tokenCookie});
        embedToken.setEmbedToken(accessToken);
        this.userAgentCookieCredentialHandlerHelper.embedCredential(baseRequest);
        this.tokenStore.store(accessToken.getValue(), accessToken);
    }

    public String getHandleTokenType() {
        return this.handleTokenType;
    }

    public Token loadTokenFromRequest(BaseRequest baseRequest) {
        Cookie tokenCookie = ((DefaultAuthRequest)baseRequest).getCookieFromRequest("iauth-access-token");
        if(tokenCookie == null) {
            return null;
        } else {
            String tokenValue = tokenCookie.getValue();
            Token storedToken = this.tokenStore.readToken(tokenValue);
            return storedToken;
        }
    }

    public void callWhenAuthenticationError(BaseRequest baseRequest, RuntimeException ex) throws IOException {
        logger.error("访问失败。访问出现异常:" + ex.getMessage(), ex);
        String requestType = baseRequest.getRequest().getHeader("X-Requested-With");
        if(requestType != null && !requestType.equals("")) {
            baseRequest.getAuthenticator().setResponseForAjax(baseRequest);
        }

        baseRequest.getAuthenticator().redirectTologin(baseRequest, ex);
    }

    public boolean callWhenAuthenticationSuccess(BaseRequest baseRequest) throws IOException {
        baseRequest.consumeToken();
        return true;
    }

    public static void main(String[] args) {
        System.out.println(String.format("该用户%s(%s)没有权限登录。", new Object[]{"gbdai", "1"}));
    }
}
