package com.ddf.framework.customize.spring.beans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>事务注解类</p >
 * 1. 仅支持方法级别
 * 2. 事务隔离级别等复杂处理未支持
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 14:23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transactional {
}
