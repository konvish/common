package org.jeecgframework.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created by kong on 2016/1/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Excel {
    String cellFormula() default "";

    String databaseFormat() default "yyyyMMddHHmmss";

    String exportFormat() default "";

    String format() default "";

    int height() default 10;

    int imageType() default 1;

    String importFormat() default "";

    String suffix() default "";

    boolean isWrap() default true;

    int[] mergeRely() default {};

    boolean mergeVertical() default false;

    String name();

    boolean needMerge() default false;

    String orderNum() default "0";

    String[] replace() default {};

    String savePath() default "upload";

    int type() default 1;

    int width() default 10;
}
