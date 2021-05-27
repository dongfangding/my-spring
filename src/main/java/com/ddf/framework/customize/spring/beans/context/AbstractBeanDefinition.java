package com.ddf.framework.customize.spring.beans.context;

import java.util.Objects;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 20:37
 */
@Data
public abstract class AbstractBeanDefinition implements BeanDefinition {

    private final String beanName;

    private final Class<?> beanClass;

    public AbstractBeanDefinition(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractBeanDefinition that = (AbstractBeanDefinition) o;
        return Objects.equals(getBeanName(), that.getBeanName()) && Objects.equals(getBeanClass(), that.getBeanClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBeanName(), getBeanClass());
    }
}
