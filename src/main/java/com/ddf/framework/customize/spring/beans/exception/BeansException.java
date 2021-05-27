package com.ddf.framework.customize.spring.beans.exception;

import com.sun.istack.internal.Nullable;

/**
 * <p>Bean相关异常抽象类</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 10:25
 */
public abstract class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    /**
     * Create a new BeansException with the specified message
     * and root cause.
     * @param msg the detail message
     * @param cause the root cause
     */
    public BeansException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
