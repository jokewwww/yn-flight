package com.higer.read_kafka.configuration;

import java.lang.annotation.*;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/6/11 12:50
 * @Description:
 */
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChangeStatus {
    String value() default "";
}
