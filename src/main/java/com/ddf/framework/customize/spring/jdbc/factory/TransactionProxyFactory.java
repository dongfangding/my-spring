package com.ddf.framework.customize.spring.jdbc.factory;

import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    public Object getTransactionProxy(Class<?> object) {
        if (object.getInterfaces().length > 0) {

        }
        return null;
    }


    /**
     * 获取JDK的动态代理
     *
     * @param clazz
     * @return
     */
    public Object getJdkTransactionProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(this.getClass()
                .getClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                platformTransactionManage.beginTransaction();
                try {
                    final Object returnObj = method.invoke(proxy, args);
                    platformTransactionManage.commitTransaction();
                    return returnObj;
                } catch (Exception e) {
                    platformTransactionManage.rollbackTransaction();
                    throw e;
                }
            }
        });
    }
}
