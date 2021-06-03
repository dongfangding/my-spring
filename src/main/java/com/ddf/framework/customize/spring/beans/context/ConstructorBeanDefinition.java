package com.ddf.framework.customize.spring.beans.context;

import com.ddf.framework.customize.spring.beans.type.ConstructorMetadata;

/**
 * <p>基于有参构造自定义实例的IOC容器</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/01 15:44
 */
public interface ConstructorBeanDefinition extends BeanDefinition {

    /**
     * 获取创建BEAN的方法
     *
     * @return
     */
    ConstructorMetadata getConstructorMetadata();
}
