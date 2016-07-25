package com.kong.common.managerui.iauth.client.token.storage;

import com.kong.common.managerui.domain.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/1/3.
 */
@Repository
public interface UserStore {
    User storeUser(Object var1, User var2);

    User readUser(Object var1);

    void postpone(Object var1);

    void removeUser(String var1);
}
