package com.aosom.aosombase.nacos.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MoreSiteEntity {

    String groupId();

    String dataId();
}
