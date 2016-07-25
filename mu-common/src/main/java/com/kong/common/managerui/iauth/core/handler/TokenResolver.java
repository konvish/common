package com.kong.common.managerui.iauth.core.handler;

import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.exception.TokenNotExistException;
import com.kong.common.managerui.iauth.core.handler.AbstractTokenHandler;
import com.kong.common.managerui.iauth.core.token.Token;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kong on 2016/2/1.
 */
public class TokenResolver extends AbstractTokenHandler {
    private static Logger logger = LoggerFactory.getLogger(TokenResolver.class);

    public TokenResolver() {
    }

    public void callWhenAuthenticationError(BaseRequest baseRequest, RuntimeException ex) throws IOException {
        logger.error("Token解析出错。解析器异常: " + ex.getMessage(), ex);
        baseRequest.getAuthenticator().redirectTologin(baseRequest, ex);
    }

    public boolean callWhenAuthenticationSuccess(BaseRequest baseRequest) throws IOException {
        logger.info("Token解析完成。");
        return true;
    }

    public void embed(BaseRequest baseRequest) {
        assert false;

    }

    public void invoke(BaseRequest baseRequest) {
        List tokens = baseRequest.getAuthenticator().getTokens(baseRequest);
        if(tokens.isEmpty()) {
            throw new TokenNotExistException("未登录。");
        } else {
            Collections.sort(tokens, new Comparator() {
                public int compare(Token o1, Token o2) {
                    return o2.getWeight() - o1.getWeight();
                }
            });
            baseRequest.setTokens(tokens);
        }
    }

    public void clear(BaseRequest baseRequest) {
    }

    public String getHandleTokenType() {
        return null;
    }

    public Token loadTokenFromRequest(BaseRequest baseRequest) {
        return null;
    }
}
