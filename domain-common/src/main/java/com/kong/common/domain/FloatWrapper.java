package com.kong.common.domain;

/**
 * Created by kong on 2016/1/3.
 */
public class FloatWrapper extends BaseWrapper {
    private float f;

    public FloatWrapper() {

    }
    public FloatWrapper(float f) {
        this.f = f;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }
}
