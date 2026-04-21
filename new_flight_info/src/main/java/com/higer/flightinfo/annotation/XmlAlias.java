package com.higer.flightinfo.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAlias {

    String name() default "";
}
