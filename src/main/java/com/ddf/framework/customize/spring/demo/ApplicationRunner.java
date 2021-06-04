package com.ddf.framework.customize.spring.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.ddf.framework.customize.spring.beans.context.AnnotationConfigApplicationContext;
import com.ddf.framework.customize.spring.demo.model.JdbcProperties;
import com.ddf.framework.customize.spring.demo.model.TestA;
import com.ddf.framework.customize.spring.demo.service.TaskService;
import com.ddf.framework.customize.spring.demo.service.TransactionalService;
import com.ddf.framework.customize.spring.demo.service.impl.CglibTransactionalComponent;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        // 这里先手动获取代理执行事务
        final TransactionalService transactionalService = context.getBean(TransactionalService.class);
        final ExecutorService service = Executors.newFixedThreadPool(2);
        // value 的集合大小为偶数测试正常事务提交
        service.execute(() -> {
            transactionalService.transfer("ddf", "chen", 100L);
        });
        // value 的集合大小为奇数测试正常事务回滚
        service.execute(() -> {
            transactionalService.transfer("ddf", "chen", 101L);
        });


        final CglibTransactionalComponent transaction = (CglibTransactionalComponent) context.getBean("cglibTransaction");
        // value 的集合大小为偶数测试正常事务提交
        service.execute(() -> {
            transactionalService.transfer("ddf", "chen", 50L);
        });
        // value 的集合大小为奇数测试正常事务回滚
        service.execute(() -> {
            transactionalService.transfer("ddf", "chen", 51L);
        });
        service.shutdown();
    }
}
