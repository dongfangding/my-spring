package com.ddf.framework.customize.spring.beans.context;

import com.ddf.framework.customize.spring.beans.type.MethodMetadata;

/**
 * <p>基于注解的自定义实例的BeanDefinition对象</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/01 15:51
 */
public class AnnotationGenericBeanDefinition extends AbstractBeanDefinition implements AnnotationBeanDefinition {

    /**
     * bean实例
     */
    private final MethodMetadata methodMetadata;

    public AnnotationGenericBeanDefinition(String beanName, MethodMetadata methodMetadata) {
        super(beanName, methodMetadata.getMethod().getReturnType());
        this.methodMetadata = methodMetadata;
    }

    /**
     * 获取创建BEAN的方法
     *
     * @return
     */
    @Override
    public MethodMetadata getMethodMetadata() {
        return this.methodMetadata;
    }
}
