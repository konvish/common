package com.kong.common.domain;

/**
 * 数据状态
 * Created by kong on 2016/1/3.
 */
public enum StatusEnum {
    N(0, "新增"),
    U(1, "更新"),
    D(-1, "删除");

    private int code;
    private String name;

    private StatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
