package com.ddf.framework.customize.spring.beans.factory;

import com.ddf.framework.customize.spring.beans.exception.BeansException;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 20:30
 */
public class DefaultListableBeanFactory implements BeanFactory {

    /**
     * 根据beanName获取Bean对象
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        return null;
    }

    /**
     * 根据beanName获取Bean对象，内部提供强转
     *
     * @param name
     * @param requiredType
     * @return
     * @throws BeansException
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return null;
    }

    /**
     * 根据class来获取Bean对象
     *
     * @param requiredType Bean的class类型
     * @return
     * @throws BeansException                如果bean未被创建的化
     * @throws NoSuchBeanDefinitionException 如果未找到Bean定义
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }
}
