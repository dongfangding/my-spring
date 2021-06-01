package com.ddf.framework.customize.spring.jdbc.transactional;

import java.sql.Connection;

/**
 * <p>事务管理器</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 18:09
 */
public interface PlatformTransactionManage {

    /**
     * 获取连接
     *
     * @return
     */
    Connection getConnection();

    /**
     * 开始事务
     */
    void beginTransaction();

    /**
     * 提交事务
     */
    void commitTransaction();

    /**
     * 回滚事务
     */
    void rollbackTransaction();
}
