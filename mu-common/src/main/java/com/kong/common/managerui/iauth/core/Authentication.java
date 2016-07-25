package com.kong.common.managerui.iauth.core;

import java.io.IOException;

/**
 * Created by Administrator on 2016/1/3.
 */
public interface Authentication {
    void embed(BaseRequest var1);

    void invoke(BaseRequest var1) throws IOException;

    void clear(BaseRequest var1);
}