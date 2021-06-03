package com.ddf.framework.customize.spring.demo.service;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/02 17:02
 */
public interface TransactionalService {

    /**
     * 转账
     *
     * @param from
     * @param to
     * @param amount
     */
    void transfer(String from, String to, Long amount);

}
