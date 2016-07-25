package com.kong.common.managerui.iauth.core.handler;

import com.kong.common.managerui.iauth.core.BundledAuthentication;
import com.kong.common.managerui.iauth.utils.HttpRequestConstant;
/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class AbstractTokenBundledHandler extends BundledAuthentication implements TokenHandler, HttpRequestConstant {
    public AbstractTokenBundledHandler() {
    }
}
