package com.ddf.framework.customize.spring.jdbc.transactional;

import com.ddf.framework.customize.spring.jdbc.factory.SimpleDataSource;
import java.sql.Connection;
import javax.sql.DataSource;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * <p>基于数据源的事务管理器</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 18:12
 */
@Data
public class DataSourceTransactionManage implements PlatformTransactionManage {

    private final DataSourceHolder dataSourceHolder;

    public DataSourceTransactionManage(DataSource dataSource) {
        this.dataSourceHolder = new SimpleDataSource(dataSource);
    }

    /**
     * 获取连接
     *
     * @return
     */
    @Override
    public Connection getConnection() {
        return dataSourceHolder.getThreadLocalConnection();
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
