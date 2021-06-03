package com.ddf.framework.customize.spring.beans.type;

import java.lang.reflect.Constructor;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/03 19:46
 */
public interface ConstructorMetadata {

    /**
     * 获取构造方法
     *
     * @return
     */
    Constructor getConstructor();
}
