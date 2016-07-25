package com.kong.common.filter.common;

/**
 * Created by Administrator on 2016/1/3.
 */
public class Util {
    private Util() {
    }

    public static boolean notBlank(String val) {
        return val != null && val.length() > 0;
    }
}
