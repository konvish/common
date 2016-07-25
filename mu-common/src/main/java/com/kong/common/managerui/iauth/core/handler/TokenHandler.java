package com.kong.common.managerui.iauth.core.handler;

import com.kong.common.managerui.iauth.core.Authentication;
import com.kong.common.managerui.iauth.core.BaseRequest;
import com.kong.common.managerui.iauth.core.token.Token;
import java.io.IOException;
/**
 * Created by Administrator on 2016/1/3.
 */
public interface TokenHandler extends Authentication {
    String getHandleTokenType();

    Token loadTokenFromRequest(BaseRequest var1);

    void callWhenAuthenticationError(BaseRequest var1, RuntimeException var2) throws IOException;

    boolean callWhenAuthenticationSuccess(BaseRequest var1) throws IOException;
}
