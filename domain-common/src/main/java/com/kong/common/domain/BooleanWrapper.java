package com.kong.common.domain;

/**
 * Created by kong on 2016/1/3.
 */
public class BooleanWrapper extends BaseWrapper{
    private boolean b;

    public BooleanWrapper() {
    }

    public BooleanWrapper(boolean b) {
        this.b = b;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }
}
