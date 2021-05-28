package com.ddf.framework.customize.spring.beans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>标识配置类</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/28 09:27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {
}
