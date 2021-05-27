package com.ddf.framework.customize.spring.jdbc.factory;

import com.ddf.framework.customize.spring.jdbc.transactional.DataSourceHolder;
import javax.sql.DataSource;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 18:16
 */
public class SimpleDataSource extends DataSourceHolder implements DataSourceFactory {

    public SimpleDataSource(DataSource dataSourced) {
        super(dataSourced);
    }
}
