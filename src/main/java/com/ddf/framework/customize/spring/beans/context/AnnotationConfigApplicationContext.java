package com.ddf.framework.customize.spring.beans.context;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.ComponentScan;
import com.ddf.framework.customize.spring.beans.annotation.Service;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;

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

    protected void scan() {
        // 从配置类中解析要扫描的包
        if (configurationClass != null) {
            if (configurationClass.isAnnotationPresent(ComponentScan.class)) {
                final ComponentScan annotation = configurationClass.getAnnotation(ComponentScan.class);
                final String[] basePackagesValue = annotation.basePackages();
                if (basePackagesValue.length == 0) {
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
                    .filter(clazz ->
                            clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Component.class))
                    .collect(Collectors.toList()));
        }
        String value = null;
        for (Class<?> aClass : matchedClazzSet) {
            if (aClass.isAnnotationPresent(Service.class)) {
                value = aClass.getAnnotation(Service.class).value();
            } else if (aClass.isAnnotationPresent(Component.class)) {
                value = aClass.getAnnotation(Component.class).value();
            }
            if (StrUtil.isBlank(value)) {
                value = aClass.getSimpleName().substring(0, 1).toLowerCase() + aClass.getSimpleName().substring(1);
            }
            super.addBeanDefinition(new GenericBeanDefinition(value, aClass));
        }
    }
}
