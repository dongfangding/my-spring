package com.ddf.framework.customize.spring.jdbc.factory;

import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.lang.reflect.Proxy;
import lombok.SneakyThrows;

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
    @SneakyThrows
    public Object getJdkTransactionProxy(Object object) {
        return Proxy.newProxyInstance(object.getClass()
                .getClassLoader(), object.getClass()
                .getInterfaces(), (proxy, method, args) -> {
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
}
