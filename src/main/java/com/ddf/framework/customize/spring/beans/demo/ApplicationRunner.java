package com.ddf.framework.customize.spring.beans.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.ddf.framework.customize.spring.beans.context.AnnotationConfigApplicationContext;
import com.ddf.framework.customize.spring.beans.demo.model.JdbcProperties;
import com.ddf.framework.customize.spring.beans.demo.model.TestA;
import com.ddf.framework.customize.spring.beans.demo.service.TaskService;
import com.ddf.framework.customize.spring.beans.demo.service.TransactionalService;
import com.ddf.framework.customize.spring.jdbc.factory.TransactionProxyFactory;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 11:57
 */
public class ApplicationRunner {

    public static void main(String[] args) throws InterruptedException {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomizeDemoConfig.class);

        // 测试IOC
        final TaskService taskService = context.getBean(TaskService.class);
        taskService.doTask("ddf", "我要开始装逼了");

        final JdbcProperties jdbcProperties = context.getBean(JdbcProperties.class);
        System.out.println("获取到的bean的结果: " + jdbcProperties);

        // 测试通过@Bean和配置类注入有依赖和无依赖的bean
        TestA testA = context.getBean(TestA.class);
        testA.delegate();

        // 测试事务
        // 获取数据源
        final DruidDataSource dataSource = context.getBean(DruidDataSource.class);
        // 获取事务管理器
        final PlatformTransactionManage platformTransactionManage = context.getBean(PlatformTransactionManage.class);
        System.out.println(Thread.currentThread().getName() + "获取连接1: " + platformTransactionManage.getConnection());
        System.out.println(Thread.currentThread().getName() + "获取连接2: " + platformTransactionManage.getConnection());
        System.out.println(Thread.currentThread().getName() + "获取连接3: " + platformTransactionManage.getConnection());


        // TODO 事务的代理框架层面是如何处理的？什么时候生成代理？是为每个有事务的方法单独存储代理还是公用一个代理？
        final TransactionProxyFactory factory = new TransactionProxyFactory(platformTransactionManage);

        final TransactionalService transactionalService = context.getBean(TransactionalService.class);
        final TransactionalService proxyTransactionalService = (TransactionalService) factory.getTransactionProxy(transactionalService);
        System.out.println(transactionalService);
        proxyTransactionalService.insert("ddf1", "value1");
        new Thread(() -> {
            proxyTransactionalService.insert("ss", "bb");
        }).start();
        Thread.sleep(10000);
    }
}
