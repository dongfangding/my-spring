package com.ddf.framework.customize.spring.beans.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Autowired;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.demo.service.TransactionalService;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import lombok.SneakyThrows;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/02 17:04
 */
@Component
public class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private PlatformTransactionManage platformTransactionManage;


    /**
     * 插入
     *
     * @param name
     * @param value
     */
    @SneakyThrows
    @Override
    public void insert(String name, String value) {
        final Connection connection = platformTransactionManage.getConnection();
        System.out.println("connection = " + connection);
        String sql = "insert into `spring_transactional`(name, value) values (?, ?)";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, value);
        statement.execute();
        statement.close();
    }

    /**
     * 更新name
     *
     * @param id
     * @param name
     */
    @SneakyThrows
    @Override
    public void update(Integer id, String name) {
        final Connection connection = platformTransactionManage.getConnection();
        String sql = "update `spring_transactional` set name = ? where id = ?";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, id);
        statement.execute();
        statement.close();
    }

    /**
     * 删除记录
     *
     * @param id
     */
    @SneakyThrows
    @Override
    public void delete(Integer id) {
        final Connection connection = platformTransactionManage.getConnection();
        String sql = "delete `spring_transactional` where id = ?";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }
}
