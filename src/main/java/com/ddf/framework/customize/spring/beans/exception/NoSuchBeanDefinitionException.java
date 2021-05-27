package com.ddf.framework.customize.spring.beans.exception;

/**
 * <p>未找到BeanDefinition定义异常</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 10:31
 */
public class NoSuchBeanDefinitionException extends BeansException {

    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
    }

    public NoSuchBeanDefinitionException(Class<?> clazz) {
        super("No bean class '" + clazz + "' available");
    }

    public NoSuchBeanDefinitionException(String name, String message) {
        super("No bean named '" + name + "' available: " + message);
    }
}
