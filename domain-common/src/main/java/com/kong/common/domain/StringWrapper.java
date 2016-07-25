package com.kong.common.domain;

/**
 * Created by Administrator on 2016/1/3.
 */
public class StringWrapper extends BaseWrapper{
    private String s;

    public StringWrapper() {
    }

    public StringWrapper(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

}
