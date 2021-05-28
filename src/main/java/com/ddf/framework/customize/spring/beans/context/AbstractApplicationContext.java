package com.ddf.framework.customize.spring.beans.context;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ddf.framework.customize.spring.beans.annotation.Autowired;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.Configuration;
import com.ddf.framework.customize.spring.beans.annotation.Service;
import com.ddf.framework.customize.spring.beans.annotation.Transactional;
import com.ddf.framework.customize.spring.beans.annotation.Value;
import com.ddf.framework.customize.spring.beans.exception.BeansException;
import com.ddf.framework.customize.spring.beans.exception.NoSuchBeanDefinitionException;
import com.ddf.framework.customize.spring.beans.exception.NoUniqueBeanDefinitionException;
import com.ddf.framework.customize.spring.jdbc.factory.TransactionProxyFactory;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 09:35
 */
@Slf4j
public abstract class AbstractApplicationContext implements ApplicationContext {

    /**
     * bean 定义集合
     */
    private final Set<BeanDefinition> beanDefinitionSet = new HashSet<>(128);

    /**
     * bean name  to   BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(128);

    /**
     * bean name to class instance
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(128);

    /**
     * class type to class instance array
     */
    private final Map<Class<?>, List<String>> singletonBeanNamesByType = new ConcurrentHashMap(64);

    private TransactionProxyFactory transactionProxyFactory;

    /**
     * 刷新容器
     */
    protected void refresh() {
        log.info("开始刷新容器>>>>>>>>>>>>>");
        postSingletonBeans();
        log.info("容器刷新完成>>>>>>>>>>>>>");
    }

    /**
     * 添加BeanDefinition集合
     *
     * @param beanDefinition
     */
    public void addBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitionSet.add(beanDefinition);
    }

    /**
     * 注册BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * 根据bean name获取BeanDefinition对象
     *
     * @param beanName
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    protected BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return beanDefinitionMap.get(beanName);
    }


    /**
     * 获取BeanDefinition name集合
     *
     * @return
     */
    protected Set<String> getBeanDefinitionNames() {
        return beanDefinitionMap.keySet();
    }

    /**
     * 根据beanName获取Bean对象
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        if (!this.singletonObjects.containsKey(beanName)) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return this.singletonObjects.get(beanName);
    }

    /**
     * 根据beanName获取Bean对象，内部提供强转
     *
     * @param beanName
     * @param requiredType
     * @return
     * @throws BeansException
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
        return (T) getBean(beanName);
    }

    /**
     * 根据class来获取Bean对象
     *
     * @param requiredType Bean的class类型
     * @return
     * @throws BeansException                如果bean未被创建的化
     * @throws NoSuchBeanDefinitionException 如果未找到Bean定义
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        final List<String> beanNames = singletonBeanNamesByType.get(requiredType);
        if (CollectionUtil.isEmpty(beanNames)) {
            throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
        }
        return (T) this.singletonObjects.get(beanNames.get(0));
    }

    /**
     * 处理单例bean
     *
     */
    private void postSingletonBeans() {
        if (CollectionUtil.isEmpty(beanDefinitionSet)) {
            log.warn("未初始化BeanDefinition集合>>>>>>>>>");
            return;
        }
        for (BeanDefinition definition : beanDefinitionSet) {
            addSingleBean(definition);
        }

        // 填充bean 属性
        populateBeanProperties();
    }


    /**
     * 添加单例bean
     *
     * @param definition
     */
    private void addSingleBean(BeanDefinition definition) {
        final Class<?> currClazz = definition.getBeanClass();
        final String name = definition.getBeanName();
        try {
            // 存储BeanDefinition Map
            registerBeanDefinition(definition.getBeanName(), definition);

            final Object obj = currClazz.newInstance();
            // bean name 映射实例
            this.singletonObjects.put(name, obj);

            Class<?> clazzMapKey;
            // bean clazz 映射实例, 如果有接口，使用接口class做映射
            if (currClazz.getInterfaces().length > 0) {
                clazzMapKey = currClazz.getInterfaces()[0];
            } else {
                clazzMapKey = currClazz;
            }

            // 一个class接口对应多个bean name
            List<String> currClazzBeanNames = singletonBeanNamesByType.get(clazzMapKey);
            if (CollectionUtil.isEmpty(currClazzBeanNames)) {
                currClazzBeanNames = new ArrayList<>();
            }
            currClazzBeanNames.add(name);
            singletonBeanNamesByType.put(clazzMapKey, currClazzBeanNames);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充Bean的属性
     */
    private void populateBeanProperties() {
        final Map<String, Object> singletonObjects = this.singletonObjects;
        if (CollectionUtil.isEmpty(singletonObjects)) {
            log.warn("没有需要填充的单例Bean对象>>>>>>>>");
            return;
        }
        singletonObjects.forEach((name, instance) -> {
            final Class<?> currClazz = instance.getClass();
            final Field[] fields = ReflectUtil.getFields(currClazz);
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class) && !field.isAnnotationPresent(Value.class)) {
                    continue;
                }
                // 填充bean的依赖注入
                populateBeanDependency(instance, field);
                // 处理bean的{@link Value} 注解属性注入
                populateBeanFieldValue(instance, field);
            }
        });
    }


    /**
     * 处理bean的依赖注入
     *
     * @param instance
     * @param field
     */
    private void populateBeanDependency(Object instance, Field field) {
        final Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
        if (Objects.isNull(autowiredAnnotation)) {
            return;
        }
        Object dependencyBean;
        // 一个Class对应多个bean name
        if (singletonBeanNamesByType.get(field.getType()).size() > 1) {
            // 从单例池中取， 字段名即为bean name
            dependencyBean = this.singletonObjects.get(field.getName());
            if (Objects.isNull(dependencyBean) && autowiredAnnotation.required()) {
                throw new NoUniqueBeanDefinitionException(field.getType(), singletonBeanNamesByType.get(field.getType()));
            }
        } else {
            // 一个class 只对应一个bean name, 从class映射bean缓存中取
            dependencyBean = this.singletonObjects.get(this.singletonBeanNamesByType.get(field.getType()).get(0));
            if (Objects.isNull(dependencyBean) && autowiredAnnotation.required()) {
                throw new NoSuchBeanDefinitionException(field.getType());
            }
        }
        ReflectUtil.setFieldValue(instance, field, dependencyBean);
        //                String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        //                final Method method = ReflectUtil.getMethod(currClazz, methodName, field.getType());
        //                ReflectUtil.invoke(instance, method, dependencyBean);
    }


    /**
     * 处理bean的{@link Value} 注解属性注入
     *
     * @param instance
     * @param field
     */
    private void populateBeanFieldValue(Object instance, Field field) {
        final Value valueAnnotation = field.getAnnotation(Value.class);
        if (Objects.isNull(valueAnnotation)) {
            return;
        }
        final String fieldValue = valueAnnotation.value();
        if (StrUtil.isBlank(fieldValue)) {
            return;
        }
        // 这里需要处理fieldValue与实际字段类型的转换
        ReflectUtil.setFieldValue(instance, field.getName(), fieldValue);
    }

    private void proxyTransaction(Class<?> currClazz) {
        if (Objects.nonNull(transactionProxyFactory)) {
            transactionProxyFactory = new TransactionProxyFactory(getBean(PlatformTransactionManage.class));
        }
        final Method[] methods = currClazz.getMethods();
        final boolean hasTransaction = Arrays.stream(methods)
                .anyMatch(val -> val.isAnnotationPresent(Transactional.class));
        if (hasTransaction) {

        }
    }

    /**
     * 判断一个类是否有IOC标记的注解,统一收口
     *
     * @param clazz
     * @return
     */
    public boolean hasIocAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(
                Configuration.class);
    }
}
