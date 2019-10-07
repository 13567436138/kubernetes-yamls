package com.aosom.aosombase.nacos.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface MoreSiteValue {

    String value();

    boolean autoRefreshed() default false;
}
