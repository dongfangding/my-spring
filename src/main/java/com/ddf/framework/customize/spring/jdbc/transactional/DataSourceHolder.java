package com.ddf.framework.customize.spring.jdbc.transactional;

import com.ddf.framework.customize.spring.jdbc.factory.DataSourceFactory;
import com.ddf.framework.customize.spring.jdbc.properties.ConnectionProperties;
import java.sql.Connection;
import java.util.Objects;
import javax.sql.DataSource;
import lombok.SneakyThrows;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 18:14
 */
public class DataSourceHolder implements DataSourceFactory {

    private final DataSource dataSource;

    private final ThreadLocal<Connection> currentThreadDataSource = new ThreadLocal<>();

    protected DataSourceHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取同一线程的数据源连接
     *
     * @return
     */
    @SneakyThrows
    public Connection getThreadLocalConnection() {
        Connection connection = currentThreadDataSource.get();;
        if (Objects.isNull(connection)) {
            connection = dataSource.getConnection();
            currentThreadDataSource.set(connection);
        }
        return connection;
    }


    /**
     * 移除当前线程的数据源连接
     */
    public void removeThreadLocalConnection() {
        currentThreadDataSource.remove();
    }

    /**
     * 获取连接属性
     *
     * @return
     */
    @Override
    public ConnectionProperties getConnectionProperties() {
        return null;
    }

    /**
     * 获取数据源连接
     *
     * @return
     */
    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
