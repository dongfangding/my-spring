package com.ddf.framework.customize.spring.beans.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.ddf.framework.customize.spring.beans.annotation.Bean;
import com.ddf.framework.customize.spring.beans.annotation.ComponentScan;
import com.ddf.framework.customize.spring.beans.annotation.Configuration;
import com.ddf.framework.customize.spring.beans.demo.model.JdbcProperties;
import com.ddf.framework.customize.spring.beans.demo.model.TestA;
import com.ddf.framework.customize.spring.beans.demo.model.TestB;
import com.ddf.framework.customize.spring.jdbc.transactional.DataSourceTransactionManage;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/01 17:22
 */
@Configuration
@ComponentScan(basePackages = {"com.ddf.framework.customize.spring.beans.demo"})
public class CustomizeDemoConfig {

    @Bean
    public TestA testA(TestB testB) {
        final TestA testA = new TestA();
        testA.setTestB(testB);
        return testA;
    }

    @Bean
    public TestB testB() {
        return new TestB();
    }

    @Bean
    public DruidDataSource dataSource(JdbcProperties jdbcProperties) {
        final DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(jdbcProperties.getJdbcUrl());
        druidDataSource.setDriverClassName(jdbcProperties.getDriverClassName());
        druidDataSource.setUsername(jdbcProperties.getUsername());
        druidDataSource.setPassword(jdbcProperties.getPassword());
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManage platformTransactionManage(DruidDataSource dataSource) {
        return new DataSourceTransactionManage(dataSource);
    }
}
