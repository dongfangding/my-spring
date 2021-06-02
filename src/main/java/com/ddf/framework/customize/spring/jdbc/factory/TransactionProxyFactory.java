package com.ddf.framework.customize.spring.jdbc.factory;

import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
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

    public Object getTransactionProxy(Object object) {
        if (object.getClass().getInterfaces().length > 0) {
            return getJdkTransactionProxy(object);
        }
        return object;
    }

    /**
     * 获取JDK的动态代理
     *
     * @param object
     * @return
     */
    public Object getJdkTransactionProxy(Object object) {
        System.out.println(object);
        return Proxy.newProxyInstance(TransactionProxyFactory.class
                .getClassLoader(), object.getClass().getInterfaces(), (proxy, method, args) -> {
                    platformTransactionManage.beginTransaction();
                    try {
                        final Object returnObj = method.invoke(proxy, args);
                        platformTransactionManage.commitTransaction();
                        return returnObj;
                    } catch (Exception e) {
                        platformTransactionManage.rollbackTransaction();
                        throw e;
                    }
                });
    }
}
