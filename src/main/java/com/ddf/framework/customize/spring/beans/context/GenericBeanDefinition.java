package com.ddf.framework.customize.spring.beans.context;

/**
 * <p>通用BeanDefinition对象</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 20:43
 */
public class GenericBeanDefinition extends AbstractBeanDefinition {

    public GenericBeanDefinition(String beanName, Class<?> beanClass) {
        super(beanName, beanClass);
    }
}
