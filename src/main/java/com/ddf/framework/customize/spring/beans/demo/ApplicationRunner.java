package com.ddf.framework.customize.spring.beans.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.ddf.framework.customize.spring.beans.context.AnnotationConfigApplicationContext;
import com.ddf.framework.customize.spring.beans.demo.model.JdbcProperties;
import com.ddf.framework.customize.spring.beans.demo.service.TaskService;
import com.ddf.framework.customize.spring.jdbc.factory.SimpleDataSource;
import com.ddf.framework.customize.spring.jdbc.properties.ConnectionProperties;
import com.ddf.framework.customize.spring.jdbc.transactional.DataSourceTransactionManage;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 11:57
 */
public class ApplicationRunner {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                "com.ddf.framework.customize.spring.beans.demo");

        // 测试IOC
        final TaskService taskService = context.getBean(TaskService.class);
        taskService.doTask("ddf", "我要开始装逼了");

        final JdbcProperties jdbcProperties = context.getBean(JdbcProperties.class);
        System.out.println("获取到的bean的结果: " + jdbcProperties);


        // 测试事务
        final ConnectionProperties connectionProperties = new ConnectionProperties();
        connectionProperties.setUrl("jdbc:mysql://localhost:3306/zdy_mybatis?useUnicode=true&amp;characterEncoding=UTF8&amp;useSSL=false&amp;serverTimezone=GMT%2B8&amp;zeroDateTimeBehavior=convertToNull&amp;allowMultiQueries=true&amp;autoReconnect=true&amp;failOverReadOnly=false&amp;maxReconnects=10&amp;tinyInt1isBit=false")
                .setDriverClassName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("123456");
        final DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/zdy_mybatis?useUnicode=true&amp;characterEncoding=UTF8&amp;useSSL=false&amp;serverTimezone=GMT%2B8&amp;zeroDateTimeBehavior=convertToNull&amp;allowMultiQueries=true&amp;autoReconnect=true&amp;failOverReadOnly=false&amp;maxReconnects=10&amp;tinyInt1isBit=false");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");

        // 准备数据源
        final SimpleDataSource source = new SimpleDataSource(druidDataSource);
        // 数据源交给事务管理器
        PlatformTransactionManage platformTransactionManage = new DataSourceTransactionManage(source);
    }
}
