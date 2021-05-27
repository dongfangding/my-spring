package com.ddf.framework.customize.spring.jdbc.transactional;

import lombok.SneakyThrows;

/**
 * <p>基于数据源的事务管理器</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 18:12
 */
public class DataSourceTransactionManage implements PlatformTransactionManage {

    private final DataSourceHolder dataSourceHolder;

    public DataSourceTransactionManage(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }

    /**
     * 开始事务
     */
    @SneakyThrows
    @Override
    public void beginTransaction() {
        dataSourceHolder.getThreadLocalConnection().setAutoCommit(false);
    }

    /**
     * 提交事务
     */
    @SneakyThrows
    @Override
    public void commitTransaction() {
        dataSourceHolder.getThreadLocalConnection().commit();
    }

    /**
     * 回滚事务
     */
    @SneakyThrows
    @Override
    public void rollbackTransaction() {
        dataSourceHolder.getThreadLocalConnection().rollback();
    }
}
