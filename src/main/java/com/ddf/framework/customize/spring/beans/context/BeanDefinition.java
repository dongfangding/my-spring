package com.ddf.framework.customize.spring.beans.context;

/**
 * <p>Bean定义顶层类</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 20:31
 */
public interface BeanDefinition {

    /**
     * bean name
     *
     * @return
     */
    String getBeanName();

    /**
     * bean class
     *
     * @return
     */
    Class<?> getBeanClass();


    /**
     * 获取一个已经实例的类，不需要自己再实例化
     *
     * @return
     */
    Object getInstance();

}
