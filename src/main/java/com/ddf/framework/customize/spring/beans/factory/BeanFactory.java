package com.ddf.framework.customize.spring.beans.factory;

import com.ddf.framework.customize.spring.beans.exception.BeansException;
import com.ddf.framework.customize.spring.beans.exception.NoSuchBeanDefinitionException;

/**
 * <p>顶层Bean工厂</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 10:23
 */
public interface BeanFactory {

    /**
     * 根据beanName获取Bean对象
     *
     * @param beanName
     * @throws BeansException
     * @return
     */
    Object getBean(String beanName) throws BeansException;

    /**
     * 根据beanName获取Bean对象，内部提供强转
     *
     * @param beanName
     * @param requiredType
     * @throws BeansException
     * @return
     */
    <T> T getBean(String beanName, Class<T> requiredType) throws BeansException;

    /**
     * 根据class来获取Bean对象
     *
     * @param requiredType Bean的class类型
     * @param <T>
     * @return
     * @throws BeansException 如果bean未被创建的化
     * @throws NoSuchBeanDefinitionException 如果未找到Bean定义
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;


}
