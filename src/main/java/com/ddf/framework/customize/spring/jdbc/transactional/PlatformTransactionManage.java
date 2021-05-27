package com.ddf.framework.customize.spring.jdbc.transactional;

/**
 * <p>事务管理器</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 18:09
 */
public interface PlatformTransactionManage {

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
