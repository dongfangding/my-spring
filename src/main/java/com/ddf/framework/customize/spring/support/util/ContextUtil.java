package com.ddf.framework.customize.spring.support.util;

import cn.hutool.core.util.StrUtil;
import com.ddf.framework.customize.spring.beans.annotation.Bean;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/28 09:43
 */
public class ContextUtil {

    private ContextUtil() {}

    /**
     * 获取bean name 定义格式
     *
     * @param clazz
     * @return
     */
    public static String getBeanNameByClass(Class<?> clazz) {
        return clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
    }

    /**
     * 获取bean name 定义格式
     *
     * @param method
     * @return
     */
    public static String getBeanNameByMethod(Method method) {
        return method.getName().substring(0, 1).toLowerCase() + method.getName().substring(1);
    }

    /**
     * 获取Method bean的bean name
     *
     * @param method
     * @return
     */
    public static String getBeanMethodName(Method method) {
        final Bean annotation = method.getAnnotation(Bean.class);
        if (Objects.isNull(annotation) || StrUtil.isBlank(annotation.value())) {
            return getBeanNameByMethod(method);
        }
        return annotation.value();
    }
}
