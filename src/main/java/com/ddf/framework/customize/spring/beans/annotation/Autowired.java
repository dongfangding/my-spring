package com.ddf.framework.customize.spring.beans.annotation;

import com.ddf.framework.customize.spring.beans.exception.NoSuchBeanDefinitionException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>自动装配注解</p >
 * 模式按照class注入，如果发现有多个class，则使用字段名作为bean name注入， 都处理不了抛出异常
 *
 * TODO
 * 1. 暂时只支持字段注入
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 14:18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {

    /**
     * 要注入的bean是否必须存在， 比如存在，不存在则会抛出{@link NoSuchBeanDefinitionException} 异常
     *
     * @return
     */
    boolean required() default true;
}
