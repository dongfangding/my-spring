package com.ddf.framework.customize.spring.beans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>指定扫描包注解</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 10:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

    /**
     * 要扫描的包
     *
     * @return
     */
    String[] basePackages() default {};
}
