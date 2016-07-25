package com.kong.common.managerui.iauth.core.handler;

import com.google.common.base.Strings;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.exception.TokenNotExistException;
import com.kong.common.managerui.iauth.core.token.EmbedToken;
import com.kong.common.managerui.iauth.core.token.Token;
import com.kong.common.managerui.iauth.core.token.storage.TokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Administrator on 2016/1/3.
 */
public class EmbedTokenHandler extends AbstractTokenHandler {
    private static Logger logger = LoggerFactory.getLogger(EmbedTokenHandler.class);
    @Autowired
    private TokenStore tokenStore;
    private String handleTokenType = "embed_token";

    public EmbedTokenHandler() {
    }

    public void invoke(BaseRequest baseRequest) throws IOException {
        EmbedToken token = (EmbedToken)baseRequest.getToken();
        if(token == null) {
            throw new TokenNotExistException("EmbedToken不存在。");
        } else {
            baseRequest.getAuthenticator().embedment(baseRequest, token.getEmbedTokenType());
        }
    }

    public void clear(BaseRequest baseRequest) {
        assert false;

    }

    public void callWhenAuthenticationError(BaseRequest baseRequest, RuntimeException ex) throws IOException {
        logger.error("埋点失败。埋点处理出现异常: " + ex.getMessage(), ex);
        baseRequest.getAuthenticator().redirectTologin(baseRequest, ex);
    }

    public boolean callWhenAuthenticationSuccess(BaseRequest baseRequest) throws IOException {
        String url = baseRequest.getRequest().getRequestURI();
        logger.info("埋点完成。重定向到当前访问路径: uri=" + url);
        baseRequest.getResponse().sendRedirect(url);
        baseRequest.consumeToken();
        return false;
    }

    public void embed(BaseRequest baseRequest) {
        assert false;

    }

    public String getHandleTokenType() {
        return this.handleTokenType;
    }

    public Token loadTokenFromRequest(BaseRequest baseRequest) {
        String tokenParam = baseRequest.getRequest().getParameter("iauth-embed-token");
        if(Strings.isNullOrEmpty(tokenParam)) {
            return null;
        } else {
            Token storedToken = this.tokenStore.readToken(tokenParam);
            if(storedToken != null) {
                this.tokenStore.removeToken(storedToken.getValue());
            }

            return storedToken;
        }
    }
}
