package com.kong.common.domain;

/**
 * Created by kong on 2016/1/3.
 */
public class DoubleWrapper extends BaseWrapper {
    private double d;


    public DoubleWrapper() {

    }

    public DoubleWrapper(double d) {
        this.d = d;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
