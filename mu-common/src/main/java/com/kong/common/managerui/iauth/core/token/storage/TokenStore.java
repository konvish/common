package com.kong.common.managerui.iauth.core.token.storage;

import com.kong.common.managerui.iauth.core.token.Token;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/1/3.
 */
@Repository
public interface TokenStore {
    Token readToken(String var1);

    void postpone(String var1);

    void store(String var1, Token var2);

    void removeToken(String var1);
}
