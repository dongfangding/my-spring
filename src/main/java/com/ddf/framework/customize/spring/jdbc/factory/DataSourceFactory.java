package com.ddf.framework.customize.spring.jdbc.factory;

import com.ddf.framework.customize.spring.jdbc.properties.ConnectionProperties;
import javax.sql.DataSource;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 17:51
 */
public interface DataSourceFactory {

    /**
     * 获取连接属性
     *
     * @return
     */
    ConnectionProperties getConnectionProperties();

    /**
     * 获取数据源连接
     *
     * @return
     */
    DataSource getDataSource();
}
