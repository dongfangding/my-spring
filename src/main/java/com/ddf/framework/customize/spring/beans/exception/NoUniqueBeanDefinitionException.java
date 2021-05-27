package com.ddf.framework.customize.spring.beans.exception;

import java.util.List;

/**
 * <p>根据Class查询到的Bean不唯一异常</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 10:36
 */
public class NoUniqueBeanDefinitionException extends NoSuchBeanDefinitionException {

    public NoUniqueBeanDefinitionException(Class<?> type, List<String> beanNamesFound) {
        super("找到[" + type.getName() +"]的bean的数量不唯一， " + beanNamesFound.toString());
    }
}
