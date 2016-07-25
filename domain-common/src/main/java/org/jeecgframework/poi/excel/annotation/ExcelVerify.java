package org.jeecgframework.poi.excel.annotation;

/**
 * Created by kong on 2016/1/29.
 */
public @interface ExcelVerify {
    boolean interHandler() default true;

    boolean isEmail() default false;

    boolean isMobile() default false;

    boolean isTel() default false;

    int maxLength() default -1;

    int minLength() default -1;

    boolean notNull() default false;

    String regex() default "";

    String regexTip() default "数据不符合规范";
}
