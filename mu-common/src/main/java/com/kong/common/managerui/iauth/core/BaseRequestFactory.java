package com.kong.common.managerui.iauth.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/1/3.
 */
public interface BaseRequestFactory {
    BaseRequest buildFromHttpServletRequest(HttpServletRequest var1, HttpServletResponse var2, Authenticator var3);
}
