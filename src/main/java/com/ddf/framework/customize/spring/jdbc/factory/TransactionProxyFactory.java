package com.ddf.framework.customize.spring.jdbc.factory;

import cn.hutool.core.util.ReflectUtil;
import com.ddf.framework.customize.spring.beans.annotation.Transactional;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.lang.reflect.Proxy;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 19:49
 */
public class TransactionProxyFactory {

    private final PlatformTransactionManage platformTransactionManage;

    public TransactionProxyFactory(PlatformTransactionManage platformTransactionManage) {
        this.platformTransactionManage = platformTransactionManage;
    }

    public Object getTransactionProxy(Object object) {
        if (object.getClass().getInterfaces().length > 0) {
            return getJdkTransactionProxy(object);
        }
        return getCglibTransactionProxy(object);
    }

    /**
     * 获取JDK的动态代理
     *
     * @param object
     * @return
     */
    @SneakyThrows
    public Object getJdkTransactionProxy(Object object) {
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), (proxy, method, args) -> {
            // FIXME 暂时采用在执行的时候判断是否是需要开启事务的
            if (!ReflectUtil.getMethodByName(proxy.getClass(), method.getName()).isAnnotationPresent(Transactional.class)) {
                return method.invoke(object, args);
            }
            platformTransactionManage.beginTransaction();
            System.out.println(Thread.currentThread().getName() + "开启事务： " + platformTransactionManage.getConnection());
            try {
                final Object returnObj = method.invoke(object, args);
                platformTransactionManage.commitTransaction();
                System.out.println(Thread.currentThread().getName() + "提交事务： " + platformTransactionManage.getConnection());
                return returnObj;
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + "回滚事务： " + platformTransactionManage.getConnection());
                platformTransactionManage.rollbackTransaction();
                throw e;
            }
        });
    }

    /**
     * 使用cglib动态代理生成代理对象
     * @param obj 委托对象
     * @return
     */
    public Object getCglibTransactionProxy(Object obj) {
        return Enhancer.create(obj.getClass(), (MethodInterceptor) (o, method, objects, methodProxy) -> {
            Object result;
            try {
                // 开启事务(关闭事务的自动提交)
                platformTransactionManage.beginTransaction();
                result = method.invoke(obj, objects);
                // 提交事务
                platformTransactionManage.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                // 回滚事务
                platformTransactionManage.rollbackTransaction();
                throw e;
            }
            return result;
        });
    }
}
