package com.kong.common.managerui.iauth.core.filter;

import com.kong.common.managerui.iauth.core.Authenticator;
import com.kong.common.managerui.iauth.core.exception.CannotAuthException;
import com.kong.common.managerui.iauth.core.handler.TokenHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class DelegableSecurityFilter implements Filter {
    private List<TokenHandler> tokenHandlers = Collections.emptyList();

    public DelegableSecurityFilter() {
    }

    public abstract Authenticator getAuthenticator();

    public void setAuthentications(List<TokenHandler> tokenHandlers) {
        this.tokenHandlers = tokenHandlers;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        if(this.getAuthenticator() == null) {
            throw new CannotAuthException();
        } else {
            this.getAuthenticator().init(this.tokenHandlers);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(this.getAuthenticator() == null) {
            throw new CannotAuthException("验证模块不存在，请检查配置文件。");
        } else {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse res = (HttpServletResponse)response;
            this.getAuthenticator().authentication(req, res, chain);
        }
    }

    public void destroy() {
        if(this.getAuthenticator() == null) {
            throw new CannotAuthException();
        } else {
            this.getAuthenticator().destroy();
        }
    }
}
