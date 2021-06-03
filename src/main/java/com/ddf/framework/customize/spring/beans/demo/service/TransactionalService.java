package com.ddf.framework.customize.spring.beans.demo.service;

import java.util.List;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/02 17:02
 */
public interface TransactionalService {

    /**
     * 插入
     *
     * @param values
     */
    void insert(List<Integer> values);

    /**
     * 更新name
     *
     * @param id
     * @param name
     */
    void update(Integer id, String name);

    /**
     * 删除记录
     *
     * @param id
     */
    void delete(Integer id);
}
