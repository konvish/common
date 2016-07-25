package com.kong.common.domain;

/**
 * 业务状态   正常 10 、  停用 20 、  逻辑删除 30
 * Created by kong on 2016/1/3.
 */
public enum BizStatusEnum {
    N(10, "正常"),
    S(20, "停用"),
    D(30, "逻辑删除");

    private int code;
    private String name;

    private BizStatusEnum(int code, String name){
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
