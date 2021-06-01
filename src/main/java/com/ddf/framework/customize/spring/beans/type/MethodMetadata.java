package com.ddf.framework.customize.spring.beans.type;

import java.lang.reflect.Method;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/01 16:14
 */
public interface MethodMetadata {

    /**
     * 获取创建bean的方法
     *
     * @return
     */
    Method getMethod();
}
