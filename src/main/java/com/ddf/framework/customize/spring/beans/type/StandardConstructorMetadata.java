package com.ddf.framework.customize.spring.beans.type;

import java.lang.reflect.Constructor;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/03 19:46
 */
public class StandardConstructorMetadata implements ConstructorMetadata {

    private Constructor constructor;

    public StandardConstructorMetadata(Constructor constructor) {
        this.constructor = constructor;
    }

    /**
     * 获取构造方法
     *
     * @return
     */
    @Override
    public Constructor getConstructor() {
        return constructor;
    }
}
