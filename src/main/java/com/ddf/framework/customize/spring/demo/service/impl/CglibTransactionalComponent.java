package com.ddf.framework.customize.spring.demo.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.ddf.framework.customize.spring.beans.annotation.Autowired;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.Transactional;
import com.ddf.framework.customize.spring.jdbc.transactional.PlatformTransactionManage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.SneakyThrows;

/**
 * <p>测试未实现接口的事务代理</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/03 09:55
 */
@Component("cglibTransaction")
public class CglibTransactionalComponent {

    @Autowired
    private PlatformTransactionManage platformTransactionManage;

    public static final String TRANSFER_FROM_SQL = "update spring_transaction_user_balance set balance = balance - ? where name = ?";
    public static final String TRANSFER_TO_SQL = "update spring_transaction_user_balance set balance = balance + ? where name = ?";


    /**
     * 转账
     *
     * @param from
     * @param to
     * @param amount
     */
    @SneakyThrows
    @Transactional
    public void transfer(String from, String to, Long amount) {
        final Connection connection = platformTransactionManage.getConnection();
        // 转账方
        final PreparedStatement fromStatement = connection.prepareStatement(TRANSFER_FROM_SQL);
        fromStatement.setLong(1, amount);
        fromStatement.setString(2, from);
        fromStatement.execute();

        if (amount % 2 != 0) {
            throw new RuntimeException("手动异常，测试回滚" + amount);
        }

        final Connection toConn = platformTransactionManage.getConnection();
        // 转账方
        final PreparedStatement toStatement = toConn.prepareStatement(TRANSFER_TO_SQL);
        toStatement.setLong(1, amount);
        toStatement.setString(2, to);
        toStatement.execute();
    }

    /**
     * 插入
     *
     * @param values
     */
    @SneakyThrows
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
}
