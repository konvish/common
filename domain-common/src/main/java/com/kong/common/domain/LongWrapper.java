package com.kong.common.domain;

/**
 * Created by kong on 2016/1/3.
 */
public class LongWrapper extends BaseWrapper {
    private long l;

    public LongWrapper() {
    }

    public LongWrapper(long l) {
        this.l = l;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }
}
