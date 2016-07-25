package com.kong.common.exception;

/**
 * Created by kong on 2016/2/1.
 */
public enum BizExceptionEnum {
    /** 不能为空 */
    REQUIRED("0008888", "不能为空"),
    /** 特定长度 */
    LENGTH("0007777", "长度必须为："),
    /** 最大长度 */
    MAXLENGTH("0009999", "不能超过长度："),
    /** 已经存在 */
    EXISTS("0005555", " 所填写信息不唯一"),
    /** 不存在 */
    NOTEXISTS("0004444", "不存在"),
    /** 新增失败 */
    ADDERROR("0003333", "新增失败"),
    /** 修改失败 */
    UPDATEERROR("0002222", "修改失败"),
    /** 删除失败 */
    DELERROR("0001111", "删除失败"),
    /** 没有相应的操作权限 */
    DENY("0001112", "没有相应的操作权限"),
    /** 登录验权失败 */
    AUTHERROR("0001113", "登录验权失败");


    private String code;
    private String desc;

    private BizExceptionEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
