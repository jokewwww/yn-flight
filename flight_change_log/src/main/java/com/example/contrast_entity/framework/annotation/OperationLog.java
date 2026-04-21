package com.example.contrast_entity.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String value() default "";
}
