package com.ncse.fdds.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XMLValue {
    String value();//转换名称

    String fromDateFormat() default "yyyy-MM-dd HH:mm:ss";//解析时间格式

    String toDateFormat() default "yyyy-MM-dd HH:mm:ss";//转换时间格式
}
