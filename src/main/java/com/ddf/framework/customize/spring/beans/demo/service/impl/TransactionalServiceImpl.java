package com.ddf.framework.customize.spring.beans.demo.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.ddf.framework.customize.spring.beans.annotation.Autowired;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.Transactional;
import com.ddf.framework.customize.spring.beans.demo.service.TransactionalService;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
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
     * @param values
     */
    @SneakyThrows
    @Override
    @Transactional
    public void insert(List<Integer> values) {
        // 模拟多次插入，前面成功，后面失败
        for (Integer value : values) {
            final Connection connection = platformTransactionManage.getConnection();
            System.out.println(Thread.currentThread().getName() + ": " + connection);
            String sql = "insert into `spring_transactional`(name, value) values (?, ?)";
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, RandomUtil.randomString(10));
            statement.setString(2, value.toString());
            statement.execute();
            statement.close();
        }
        if (values.size() % 2 != 0) {
            throw new RuntimeException("手动异常，测试回滚");
        }
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
