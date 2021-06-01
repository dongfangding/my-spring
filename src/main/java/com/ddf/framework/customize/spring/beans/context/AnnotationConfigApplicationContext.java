package com.ddf.framework.customize.spring.beans.context;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ddf.framework.customize.spring.beans.annotation.Bean;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.ComponentScan;
import com.ddf.framework.customize.spring.beans.annotation.Configuration;
import com.ddf.framework.customize.spring.beans.annotation.Service;
import com.ddf.framework.customize.spring.beans.type.StandardMethodMetadata;
import com.ddf.framework.customize.spring.support.util.ContextUtil;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/26 20:58
 */
@Data
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    /**
     * 要扫描的包
     */
    private String[] basePackages;

    private Class<?> configurationClass;

    public AnnotationConfigApplicationContext(String... basePackages) {
        this.basePackages = basePackages;
        scan();
        refresh();
    }

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
        scan();
        refresh();
    }


    /**
     * 扫描包，组装BeanDefinition对象
     */
    protected void scan() {
        // 从配置类中解析要扫描的包
        if (configurationClass != null) {
            if (configurationClass.isAnnotationPresent(ComponentScan.class)) {
                final ComponentScan annotation = configurationClass.getAnnotation(ComponentScan.class);
                final String[] basePackagesValue = annotation.basePackages();
                if (basePackagesValue.length != 0) {
                    this.basePackages = basePackagesValue;
                } else {
                    this.basePackages = new String[] {configurationClass.getPackage().getName()};
                }
            }
        }
        String[] basePackageScan = this.basePackages;
        Set<Class<?>> matchedClazzSet = new HashSet<>(128);
        // 在指定包中扫描添加了IOC标记的类
        for (String aPackage : basePackageScan) {
            final Set<Class<?>> classes = ClassUtil.scanPackage(aPackage);
            matchedClazzSet.addAll(classes.stream()
                    .filter(this::hasIocAnnotation)
                    .collect(Collectors.toList()));
        }
        String value = null;
        for (Class<?> clazz : matchedClazzSet) {
            // 如果类为配置类，则额外处理
            parseConfigurationClass(clazz);

            // 当前类为容器类
            if (clazz.isAnnotationPresent(Service.class)) {
                value = clazz.getAnnotation(Service.class).value();
            } else if (clazz.isAnnotationPresent(Component.class)) {
                value = clazz.getAnnotation(Component.class).value();
            } else if (clazz.isAnnotationPresent(Configuration.class)) {
                value = ContextUtil.getBeanNameByClass(clazz);
            }
            if (StrUtil.isBlank(value)) {
                value = ContextUtil.getBeanNameByClass(clazz);
            }
            super.addBeanDefinition(new GenericBeanDefinition(value, clazz));
        }
    }


    /**
     * 解析配置类
     *
     * @param clazz
     */
    @SneakyThrows
    private void parseConfigurationClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            return;
        }
        final Method[] methods = ReflectUtil.getMethods(clazz);
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }

            final Bean beanAnnotation = method.getAnnotation(Bean.class);
            String beanName = beanAnnotation.value();
            if (StrUtil.isBlank(beanName)) {
                beanName = ContextUtil.getBeanNameByMethod(method);
            }
            super.addBeanDefinition(new AnnotationGenericBeanDefinition(beanName, new StandardMethodMetadata(method)));
        }
    }
}
