package com.spike.demo.annotation;

import java.lang.annotation.*;

/**
 * @ClassName AccessLimit
 * @Description 自定义注解
 * @Author simonsfan
 * @Date 2018/12/19
 * Version  1.0
 */
@Documented
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface AccessLimit {
    /**
     * 默认1秒内限制4次
     * @return
     */
    int threshold() default 1;

    //单位: 秒
    int time() default 4;
}
