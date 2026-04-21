package com.zh.annotation;

import java.lang.annotation.*;

/**
 * 日志
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
	String description() default "";
}
