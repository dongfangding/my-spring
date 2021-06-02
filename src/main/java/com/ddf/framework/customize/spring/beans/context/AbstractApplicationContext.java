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
import com.ddf.framework.customize.spring.beans.type.MethodMetadata;
import com.ddf.framework.customize.spring.beans.type.StandardMethodMetadata;
import com.ddf.framework.customize.spring.jdbc.factory.TransactionProxyFactory;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import com.ddf.framework.customize.spring.support.util.ContextUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
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
     * bean name  to   BeanDefinition
     */
    private final Map<Class<?>, List<BeanDefinition>> beanDefinitionClassMap = new ConcurrentHashMap<>(128);

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
        // 存储BeanDefinition Map, 用以处理bean依赖时，判断依赖的对象有没有定义
        registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
        // 存储class对应BeanDefinition列表， 用以处理bean依赖时，如果依赖的bean是使用@Bean的有参依赖构造时，通过class查找bean，
        // 查询多个用形参名作为bean name, 一个的话直接使用
        final Class<?> classBeanMapKey = ContextUtil.getBeanInterfaceClass(beanDefinition.getBeanClass());
        List<BeanDefinition> definitions = this.beanDefinitionClassMap.get(classBeanMapKey);
        if (Objects.isNull(definitions)) {
            definitions = new ArrayList<>();
        }
        definitions.add(beanDefinition);
        this.beanDefinitionClassMap.put(classBeanMapKey, definitions);
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
            throw new NoSuchBeanDefinitionException(requiredType);
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
    }


    /**
     * 添加单例bean
     *
     * @param definition
     */
    @SneakyThrows
    private Object addSingleBean(BeanDefinition definition) {
        final Class<?> currClazz = definition.getBeanClass();
        final String beanName = definition.getBeanName();
        Object instance = null;
        // 暂时先简单解决， 如果一个bean依赖于另外一个bean， 那么就会提前创建这个bean, 然后这个bean自己的创建流程发现已经创建了就跳过
        if (this.singletonObjects.containsKey(definition.getBeanName())) {
            return this.singletonObjects.get(definition.getBeanName());
        }
        if (definition instanceof GenericBeanDefinition) {
            instance = currClazz.newInstance();
            doCreateBean(currClazz, beanName, instance);
        } else if (definition instanceof AnnotationBeanDefinition) {
            // 创建用户基于注解注入自定义实例化方法的bean
            AnnotationBeanDefinition annotationBeanDefinition = (AnnotationBeanDefinition) definition;
            final MethodMetadata metadata = annotationBeanDefinition.getMethodMetadata();
            if (metadata instanceof StandardMethodMetadata) {
                StandardMethodMetadata standardMethodMetadata = (StandardMethodMetadata) metadata;
                final Method factoryMethod = standardMethodMetadata.getMethod();
                final Class<?>[] types = factoryMethod.getParameterTypes();
                final Parameter[] parameters = factoryMethod.getParameters();
                if (types.length == 0) {
                    final Object methodBean = singletonBeanNamesByType.get(factoryMethod.getReturnType());
                    if (Objects.nonNull(methodBean)) {
                        return methodBean;
                    }
                    return doCreateBean(factoryMethod.getReturnType(), ContextUtil.getBeanNameByMethod(factoryMethod), factoryMethod.getReturnType().newInstance());
                }
                Object[] parameterObj = new Object[types.length];
                // 解析当前方法的入参， 每个入参都从ioc容器中取出， 如果这个时候还没创建，则执行创建bean流程
                // fixme 如果依赖的容器是一个@Bean修饰的，那么执行创建的时候就得要找到那个方法去执行才可以，否则调用无参构造会有问题
                for (int i = 0; i < types.length; i++) {
                    Class<?> type = types[i];
                    Object paramObj;
                    try {
                        paramObj = getBean(type);
                    } catch (BeansException beansException) {
                        paramObj = null;
                    }
                    if (Objects.nonNull(paramObj)) {
                        parameterObj[i] = paramObj;
                    } else {
//                        final Object obj = addSingleBean(
//                                getBeanDefinitionByClassOrName(type, parameters[i].getName(), true));
//                        parameterObj[i] = doCreateBean(type, ContextUtil.getBeanNameByClass(type), obj);
                        parameterObj[i] = addSingleBean(
                                getBeanDefinitionByClassOrName(type, parameters[i].getName(), true));
                    }
                }
                instance = factoryMethod.invoke(factoryMethod.getDeclaringClass()
                        .newInstance(), parameterObj);
                doCreateBean(factoryMethod.getReturnType(), ContextUtil.getBeanMethodName(factoryMethod), instance);
            }
        } else {
            throw new RuntimeException("不支持的bean定义");
        }

        // 填充bean 属性
        if (Objects.nonNull(instance)) {
            populateBeanProperties(beanName, instance);
        }
        return instance;
    }

    /**
     * 填充Bean的属性
     */
    private void populateBeanProperties(String beanName, Object bean) {
        final Class<?> currClazz = bean.getClass();
        final Field[] fields = ReflectUtil.getFields(currClazz);
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class) && !field.isAnnotationPresent(Value.class)) {
                continue;
            }
            // 填充bean的依赖注入
            populateBeanDependency(beanName, bean, field);
            // 处理bean的{@link Value} 注解属性注入
            populateBeanFieldValue(bean, field);
        }
    }

    /**
     * 创建bean
     *
     * @param currClazz
     * @param name
     * @return
     */
    @SneakyThrows
    private Object doCreateBean(Class<?> currClazz, String name, Object obj) {
        // bean name 映射实例
        this.singletonObjects.put(name, obj);

        Class<?> clazzMapKey = ContextUtil.getBeanInterfaceClass(currClazz);

        // 一个class接口对应多个bean name
        List<String> currClazzBeanNames = singletonBeanNamesByType.get(clazzMapKey);
        if (CollectionUtil.isEmpty(currClazzBeanNames)) {
            currClazzBeanNames = new ArrayList<>();
        }
        currClazzBeanNames.add(name);
        singletonBeanNamesByType.put(clazzMapKey, currClazzBeanNames);
        return obj;
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
                populateBeanDependency(name, instance, field);
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
    private void populateBeanDependency(String beanName, Object instance, Field field) {
        final Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
        if (Objects.isNull(autowiredAnnotation)) {
            return;
        }
        Object dependencyBean;
        if (!beanDefinitionClassMap.containsKey(field.getType())) {
            if (!this.beanDefinitionMap.containsKey(beanName)) {
                throw new NoSuchBeanDefinitionException(field.getType());
            }
            addSingleBean(this.beanDefinitionMap.get(beanName));
        }
        // 一个Class对应多个bean name
        if (beanDefinitionClassMap.get(field.getType()).size() > 1) {
            // 从单例池中取， 字段名即为bean name
            dependencyBean = this.singletonObjects.get(field.getName());
            if (Objects.isNull(dependencyBean)) {
                dependencyBean = addSingleBean(this.beanDefinitionMap.get(field.getName()));
            }
            if (Objects.isNull(dependencyBean) && autowiredAnnotation.required()) {
                throw new NoUniqueBeanDefinitionException(field.getType(), singletonBeanNamesByType.get(field.getType()));
            }
        } else {
            // 一个class 只对应一个bean name, 从class映射bean缓存中取
            final List<String> dependencyBeanNameList = this.singletonBeanNamesByType.get(field.getType());
            if (CollectionUtil.isEmpty(dependencyBeanNameList)) {
                dependencyBean = addSingleBean(this.beanDefinitionClassMap.get(field.getType()).get(0));
            } else {
                dependencyBean = this.singletonObjects.get(this.singletonBeanNamesByType.get(field.getType()).get(0));
            }
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


    public BeanDefinition getBeanDefinitionByClassOrName(Class<?> clazz, String beanName, boolean isRequired) {
        final List<BeanDefinition> definitions = this.beanDefinitionClassMap.get(clazz);
        if (CollectionUtil.isEmpty(definitions)) {
            if (isRequired) {
                throw new NoSuchBeanDefinitionException(clazz);
            }
            return null;
        }
        if (definitions.size() == 1) {
            return definitions.get(0);
        }
        final BeanDefinition definition = this.beanDefinitionMap.get(beanName);
        if (Objects.isNull(definition) && isRequired) {
            throw new NoSuchBeanDefinitionException(clazz);
        }
        return definition;
    }
}
