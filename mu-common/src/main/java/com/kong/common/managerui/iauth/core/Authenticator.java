package com.kong.common.managerui.iauth.core;

import com.kong.common.managerui.iauth.core.exception.CannotAuthException;
import com.kong.common.managerui.iauth.core.handler.AbstractTokenBundledHandler;
import com.kong.common.managerui.iauth.core.handler.TokenHandler;
import com.kong.common.managerui.iauth.core.token.Token;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class Authenticator {
    public Authenticator() {
    }

    public abstract List<TokenHandler> getTokenHandlers();

    public abstract boolean isNeedAuthentication(BaseRequest var1);

    public abstract void init(List<TokenHandler> var1);

    public List<Token> getTokens(BaseRequest baseRequest) {
        ArrayList tokens = new ArrayList();
        Iterator i$ = this.getTokenHandlers().iterator();

        while(i$.hasNext()) {
            TokenHandler tokenHandler = (TokenHandler)i$.next();
            Token token = tokenHandler.loadTokenFromRequest(baseRequest);
            if(token != null) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    public abstract boolean isLogout(BaseRequest var1);

    public void authentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        BaseRequestFactory requestFactory = this.getRequestFactory();
        BaseRequest baseRequest = requestFactory.buildFromHttpServletRequest(req, res, this);
        if(!this.isNeedAuthentication(baseRequest)) {
            chain.doFilter(req, res);
        } else {
            try {
                TokenHandler ex = (TokenHandler)this.getTokenHandlers().get(0);
                ex.invoke(baseRequest);

                while(baseRequest.hasToken()) {
                    TokenHandler i$ = this.getTokenHandler(baseRequest.getToken().getTokenType());
                    if(i$ == null) {
                        throw new CannotAuthException(String.format("没有找到%s处理器", new Object[]{baseRequest.getToken().getTokenType()}));
                    }

                    try {
                        i$.invoke(baseRequest);
                        if(!i$.callWhenAuthenticationSuccess(baseRequest)) {
                            return;
                        }
                    } catch (RuntimeException var9) {
                        i$.callWhenAuthenticationError(baseRequest, var9);
                        return;
                    }
                }

                if(this.isLogout(baseRequest)) {
                    Iterator i$1 = this.getTokenHandlers().iterator();

                    while(i$1.hasNext()) {
                        TokenHandler tokenHandler = (TokenHandler)i$1.next();
                        tokenHandler.clear(baseRequest);
                    }

                    this.redirectTologout(baseRequest);
                    return;
                }

                this.callWhenAuthenticatornSuccess(baseRequest);
                chain.doFilter(req, res);
            } catch (RuntimeException var10) {
                this.callWhenAuthenticatiornError(baseRequest, var10);
            }

        }
    }

    private TokenHandler getTokenHandler(String tokenType) {
        Iterator i$ = this.getTokenHandlers().iterator();

        TokenHandler tokenHandler;
        do {
            if(!i$.hasNext()) {
                return null;
            }

            tokenHandler = (TokenHandler)i$.next();
        } while(!this.checkTokenType(tokenType, tokenHandler));

        return tokenHandler;
    }

    /** @deprecated */
    @Deprecated
    protected boolean checkTokenType(BaseRequest baseRequest, TokenHandler tokenHandler) {
        return tokenHandler instanceof TokenHandler ?true:this.checkTokenType(baseRequest.getToken().getTokenType(), tokenHandler);
    }

    protected boolean checkTokenType(String tokenType, TokenHandler tokenHandler) {
        return tokenHandler.getHandleTokenType() == null?false:tokenHandler.getHandleTokenType().equals(tokenType);
    }

    public void embedment(BaseRequest baseRequest, String tokenType) {
        TokenHandler tokenHandler = this.getTokenHandler(tokenType);
        if(tokenHandler == null) {
            throw new CannotAuthException(String.format("没有找到%s解析器", new Object[]{baseRequest.getToken().getTokenType()}));
        } else {
            if(tokenHandler instanceof AbstractTokenBundledHandler) {
                AbstractTokenBundledHandler bundledTokenHandler = (AbstractTokenBundledHandler)tokenHandler;
                if(bundledTokenHandler.isBundled()) {
                    tokenHandler.embed(baseRequest);
                    Iterator i$ = bundledTokenHandler.getOthers().iterator();

                    while(i$.hasNext()) {
                        Authentication bundle = (Authentication)i$.next();
                        bundle.embed(baseRequest);
                    }
                }
            } else {
                tokenHandler.embed(baseRequest);
            }

        }
    }

    public abstract BaseRequestFactory getRequestFactory();

    public abstract void destroy();

    public abstract void redirectTologin(BaseRequest var1, RuntimeException var2) throws IOException;

    public abstract void setResponseForAjax(BaseRequest var1);

    public abstract void redirectTologout(BaseRequest var1) throws IOException;

    public abstract void callWhenAuthenticatornSuccess(BaseRequest var1);

    public abstract void callWhenEmbedmentSuccess(BaseRequest var1);

    public abstract void callWhenAuthenticatiornError(BaseRequest var1, RuntimeException var2) throws IOException;
}
