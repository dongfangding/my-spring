package com.ddf.framework.customize.spring.beans.type;

import java.lang.reflect.Method;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/01 16:18
 */
public class StandardMethodMetadata implements MethodMetadata {

    private final Method method;

    public StandardMethodMetadata(Method method) {
        this.method = method;
    }

    /**
     * 获取创建bean的方法
     *
     * @return
     */
    @Override
    public Method getMethod() {
        return this.method;
    }
}
