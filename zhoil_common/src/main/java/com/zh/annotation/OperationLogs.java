package com.zh.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogs {
    String value() default "";
}
