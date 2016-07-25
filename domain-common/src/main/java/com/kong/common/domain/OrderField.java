package com.kong.common.domain;

/**
 * 排序规则
 * Created by kong on 2016/1/3.
 */
public class OrderField extends BaseWrapper {
    /* 排序字段名称 */
    private String field;

    /* 排序符 == > < */
    private String op;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
