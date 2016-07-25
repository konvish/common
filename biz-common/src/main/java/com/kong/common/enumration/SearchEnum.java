package com.kong.common.enumration;

import org.apache.commons.lang.StringUtils;
/**
 * 搜索枚举
 * Created by kong on 2016/1/3.
 */
public enum SearchEnum {
    eq("eq", "="), ne("ne", "!="), lt("lt", "<"), le("le", "<="), gt("gt", ">"), ge(
            "ge", ">="),lk("lk","like");

    private String code;

    private String des;

    private SearchEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    /**
     *
     * @param code
     * @return
     */

    public static SearchEnum codeOf(String code) {
        for (SearchEnum searchEnum : SearchEnum.values()) {
            if (StringUtils.equals(code, searchEnum.getCode())) {
                return searchEnum;
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public static SearchEnum nameOf(String name) {
        for (SearchEnum searchEnum : SearchEnum.values()) {
            if (StringUtils.equals(name, searchEnum.name())) {
                return searchEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
