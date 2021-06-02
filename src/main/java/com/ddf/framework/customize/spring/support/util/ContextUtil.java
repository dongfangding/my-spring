package com.ddf.framework.customize.spring.support.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ddf.framework.customize.spring.beans.annotation.Bean;
import java.lang.annotation.Annotation;
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


    /**
     * 判断一个类是否有@Transactional注解, 类和方法上只要有一个有，就满足条件
     *
     * @param clazz
     * @return
     */
    public static boolean hasTransactionAnnotationOnClassMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass)) {
            return Boolean.TRUE;
        }
        final Method[] methods = ReflectUtil.getMethods(clazz);
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }



    /**
     * 注册bean的时候，存储的class key是接口， 因为注入的时候是接口
     *
     * @param clazz
     * @return
     */
    public static Class<?> getBeanInterfaceClass(Class<?> clazz) {
        // bean clazz 映射实例, 如果有接口，使用接口class做映射
        // FIXME 多个接口如何确定是哪个接口呢？
        if (clazz.getInterfaces().length == 1) {
            return clazz.getInterfaces()[0];
        }
        return clazz;
    }
}
