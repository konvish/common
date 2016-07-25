package com.kong.common.managerui.iauth.client;

import com.kong.cloudstack.context.CloudContextFactory;
import com.kong.cloudstack.dynconfig.DynConfigClient;
import com.kong.cloudstack.dynconfig.DynConfigClientFactory;
import com.kong.cloudstack.dynconfig.IChangeListener;
import com.kong.cloudstack.dynconfig.domain.Configuration;
import com.kong.common.context.DefaultUserContextImpl;
import com.kong.common.context.UserContextHolder;
import com.kong.common.domain.UserDomain;
import com.kong.common.managerui.domain.User;
import com.kong.common.managerui.iauth.client.DefaultAuthRequest;
import com.kong.common.managerui.iauth.client.DefaultAuthRequestFactory;
import com.kong.common.managerui.iauth.client.token.AccessToken;
import com.kong.common.managerui.iauth.client.token.storage.UserStore;
import com.kong.common.managerui.iauth.core.Authenticator;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.BaseRequestFactory;
import com.kong.common.managerui.iauth.core.Principal;
import com.kong.common.managerui.iauth.core.exception.CannotAuthException;
import com.kong.common.managerui.iauth.core.handler.EmbedTokenHandler;
import com.kong.common.managerui.iauth.core.handler.TokenHandler;
import com.kong.common.managerui.iauth.core.handler.TokenResolver;
import com.kong.common.managerui.iauth.core.token.storage.TokenStore;
import com.kong.common.managerui.iauth.utils.HttpRequestConstant;
import com.kong.common.managerui.iauth.utils.LogErrorUtil;
import com.kong.common.managerui.iauth.utils.UrlStringUtil;
import com.kong.common.utils.UserContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/1/3.
 */
@Component
public class DefaultAuthenticator extends Authenticator implements HttpRequestConstant {
    private static Logger logger = LoggerFactory.getLogger(DefaultAuthenticator.class);
    private String debugKey = null;
    private String SECRET_KEY = null;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private UserStore userStore;
    private String redirect_url = "/login";
    private List<TokenHandler> tokenHandlers = Collections.emptyList();
    private DefaultAuthRequestFactory authRequestFactory = new DefaultAuthRequestFactory();

    public void setDebugKey(String debugKey) {
        this.debugKey = debugKey;
    }

    public DefaultAuthenticator() {
    }

    public DefaultAuthenticator(List<TokenHandler> tokenHandlers) {
        this.init(tokenHandlers);
    }

    public void init(List<TokenHandler> tokenHandlers) {
        boolean conditions = tokenHandlers.size() > 0;
        if(!(tokenHandlers.get(0) instanceof TokenResolver)) {
            conditions = false;
        }

        if(!(tokenHandlers.get(1) instanceof EmbedTokenHandler)) {
            conditions = false;
        }

        if(!conditions) {
            throw new CannotAuthException();
        } else {
            this.tokenHandlers = tokenHandlers;
            this.initRedirectUrl();
            DynConfigClient dynConfigClient = DynConfigClientFactory.getClient();

            try {
                this.SECRET_KEY = dynConfigClient.getConfig("ucm", "common", "secretKey");
            } catch (Exception var5) {
                logger.info("SECRET_KEY没有进行配置，采用默认值: " + this.SECRET_KEY);
            }

            dynConfigClient.registerListeners("ucm", "common", "tokenExpireTime", new IChangeListener() {
                public Executor getExecutor() {
                    return Executors.newSingleThreadExecutor();
                }

                public void receiveConfigInfo(final Configuration configuration) {
                    System.out.println("=================" + configuration);
                    this.getExecutor().execute(new Runnable() {
                        public void run() {
                            System.out.println("========ASYN=========" + configuration);
                            DefaultAuthenticator.this.SECRET_KEY = configuration.getConfig();
                        }
                    });
                }
            });
        }
    }

    public boolean isLogout(BaseRequest baseRequest) {
        String uri = baseRequest.getRequest().getRequestURI();
        return uri.endsWith("/logout");
    }

    public BaseRequestFactory getRequestFactory() {
        return this.authRequestFactory;
    }

    public List<TokenHandler> getTokenHandlers() {
        return this.tokenHandlers;
    }

    public boolean isNeedAuthentication(BaseRequest baseRequest) {
        return this.isNeedAuthentication();
    }

    private boolean isNeedAuthentication() {
        return this.SECRET_KEY == null?true:!this.SECRET_KEY.equals(this.debugKey);
    }

    private void initRedirectUrl() {
        DynConfigClient dynConfigClient = DynConfigClientFactory.getClient();

        try {
            this.redirect_url = dynConfigClient.getConfig("ucm", "common", "uchost");
        } catch (Exception var3) {
            logger.info("uchost没有进行配置，采用默认值: " + this.redirect_url);
        }

        dynConfigClient.registerListeners("ucm", "common", "uchost", new IChangeListener() {
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }

            public void receiveConfigInfo(final Configuration configuration) {
                System.out.println("=================" + configuration);
                this.getExecutor().execute(new Runnable() {
                    public void run() {
                        System.out.println("========ASYN=========" + configuration);
                        DefaultAuthenticator.this.redirect_url = configuration.getConfig();
                    }
                });
            }
        });
    }

    public void destroy() {
        this.tokenHandlers.clear();
    }

    public void redirectTologin(BaseRequest baseRequest, RuntimeException ex) throws IOException {
        if(((DefaultAuthRequest)baseRequest).isDebug()) {
            throw ex;
        } else {
            String url = baseRequest.getRequest().getRequestURL().toString();
            HashMap params = new HashMap();
            params.put("from", url);
            params.put("error", ex.getMessage());
            params.put("log", LogErrorUtil.toString(ex));
            DefaultAuthRequest request = (DefaultAuthRequest)baseRequest;
            if(request.getPrincipal() != null && request.getPrincipal().getOwner() != null) {
                User appKey = (User)request.getPrincipal().getOwner();
                params.put("username", appKey.getName());
            }

            String appKey1 = CloudContextFactory.getCloudContext().getApplicationName();
            String product = CloudContextFactory.getCloudContext().getProductCode();
            params.put("appKey", appKey1);
            params.put("product", product);
            this.redirectTologinWithParams(baseRequest.getResponse(), params);
        }
    }

    public void redirectTologout(BaseRequest baseRequest) throws IOException {
        baseRequest.getResponse().sendRedirect("/");
    }

    public void callWhenAuthenticatornSuccess(BaseRequest baseRequest) {
        Principal principal = ((DefaultAuthRequest)baseRequest).getPrincipal();
        if(null != principal && principal.getName() != null) {
            if(principal.getOwner() != null) {
                UserContext.setCurrentUser((UserDomain)principal.getOwner());
                DefaultUserContextImpl userContext = new DefaultUserContextImpl();
                HashMap datas = new HashMap(4);
                datas.put("uid", ((User)principal.getOwner()).getId());
                datas.put("product", ((User)principal.getOwner()).getProduct());
                ((DefaultUserContextImpl)userContext).setContexts(datas);
                UserContextHolder.setUserContext(userContext);
            }

            if(principal.getToken() != null) {
                baseRequest.getRequest().setAttribute("iauth-access-token", principal.getToken());
                baseRequest.getRequest().setAttribute("iauth-user-key", Long.valueOf(((AccessToken)principal.getToken()).getUserId()));
            }

            this.tokenStore.postpone(principal.getToken().getValue());
            this.userStore.postpone(Long.valueOf(String.valueOf(((User)principal.getOwner()).getId())));
        } else {
            throw new CannotAuthException();
        }
    }

    public void callWhenEmbedmentSuccess(BaseRequest baseRequest) {
        Principal principal = ((DefaultAuthRequest)baseRequest).getPrincipal();

        assert principal != null;

    }

    public void setResponseForAjax(BaseRequest baseRequest) {
        try {
            String e = baseRequest.getRequest().getRequestURL().toString();
            baseRequest.getResponse().setHeader("sessionstatus", "timeout");
            HashMap params = new HashMap();
            params.put("from", e);
            DefaultAuthRequest request = (DefaultAuthRequest)baseRequest;
            if(request.getPrincipal() != null && request.getPrincipal().getOwner() != null) {
                User appKey = (User)request.getPrincipal().getOwner();
                params.put("username", appKey.getName());
            }

            String appKey1 = CloudContextFactory.getCloudContext().getApplicationName();
            String product = CloudContextFactory.getCloudContext().getProductCode();
            params.put("appKey", appKey1);
            params.put("product", product);
            String paramsString = UrlStringUtil.paramsMapToURLString(params);
            String result = this.redirect_url + (paramsString.length() > 1?paramsString.toString():"");
            PrintWriter out = baseRequest.getResponse().getWriter();
            out.println(result);
            out.flush();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

    }

    public void callWhenAuthenticatiornError(BaseRequest baseRequest, RuntimeException ex) throws IOException {
        logger.error("拒绝访问。验证出现异常: " + ex.getMessage(), ex);
        String requestType = baseRequest.getRequest().getHeader("X-Requested-With");
        if(requestType != null && requestType.equals("XMLHttpRequest")) {
            baseRequest.getAuthenticator().setResponseForAjax(baseRequest);
        }

        this.redirectTologin(baseRequest, ex);
    }

    private void redirectTologinWithParams(HttpServletResponse res, Map<String, String> params) throws IOException {
        String paramsString = UrlStringUtil.paramsMapToURLString(params);
        String result = this.redirect_url + (paramsString.length() > 1?paramsString.toString():"");
        logger.info(CloudContextFactory.getCloudContext().getApplicationName() + "跳转到ucm登录页面: url" + result);
        res.sendRedirect(result);
    }
}
