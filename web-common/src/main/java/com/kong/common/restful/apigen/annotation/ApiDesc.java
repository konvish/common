package com.kong.common.restful.apigen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kong on 2016/2/2.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDesc {
    String module() default "";

    String value() default "";

    String owner() default "";

    String returnDesc() default "";
}