package com.kong.common.domain;

/**
 * Created by Administrator on 2016/1/3.
 */
public class IntegerWrapper extends BaseWrapper {
    private int i;

    public IntegerWrapper() {
    }

    public IntegerWrapper(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
