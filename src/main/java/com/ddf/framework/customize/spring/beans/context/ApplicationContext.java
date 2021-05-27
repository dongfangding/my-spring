package com.ddf.framework.customize.spring.beans.context;

import com.ddf.framework.customize.spring.beans.factory.ListableBeanFactory;

/**
 * <p>IOC上下文</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 20:52
 */
public interface ApplicationContext extends ListableBeanFactory {


    /**
     * 注册BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
