package com.kong.common.managerui.iauth.core;

import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
@Deprecated
public interface Bundled<T> {
    void setBundled(boolean var1);

    boolean isBundled();

    void setBundledTo(T var1);

    T getBundledTo();

    List<T> getOthers();
}
