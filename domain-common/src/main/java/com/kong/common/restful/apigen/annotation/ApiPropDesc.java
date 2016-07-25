package com.kong.common.restful.apigen.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 类属性描述
 * Created by kong on 2016/1/3.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPropDesc {
    /**
     * 属性描述
     * @return
     */
    String value() default "";
}
