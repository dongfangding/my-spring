package com.ddf.framework.customize.spring.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Autowired;
import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.Transactional;
import com.ddf.framework.customize.spring.demo.service.TransactionalService;
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
    @Override
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

        final Connection toConnection = platformTransactionManage.getConnection();
        // 转账方
        final PreparedStatement toStatement = toConnection.prepareStatement(TRANSFER_TO_SQL);
        toStatement.setLong(1, amount);
        toStatement.setString(2, to);
        toStatement.execute();


    }
}
