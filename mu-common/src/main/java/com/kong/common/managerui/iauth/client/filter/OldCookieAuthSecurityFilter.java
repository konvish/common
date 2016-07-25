package com.kong.common.managerui.iauth.client.filter;

import com.kong.security.utils.Digests;
import com.kong.security.utils.Encodes;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/1/3.
 */
@Deprecated
public class OldCookieAuthSecurityFilter implements Filter {
    private String HTTP_UCM_HOST;

    public OldCookieAuthSecurityFilter() {
    }

    private Cookie getCookieFromRequest(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie[] arr$ = cookies;
        int len$ = cookies.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Cookie cookie = arr$[i$];
            if(cookie.getName().equalsIgnoreCase(name)) {
                return cookie;
            }
        }

        return null;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        String userAgent = req.getHeader("user-agent");
        String userKey = Encodes.encodeHex(Digests.md5(userAgent.getBytes())).substring(0, 5);
        Cookie tokenCookie = this.getCookieFromRequest(req, "token");
        Cookie credentialCookie = this.getCookieFromRequest(req, userKey);
        if(tokenCookie != null && credentialCookie != null) {
            String token = tokenCookie.getValue();
            String credential = credentialCookie.getValue();
        } else {
            res.sendRedirect(this.HTTP_UCM_HOST + "/login?from=");
        }
    }

    public void destroy() {
    }
}
