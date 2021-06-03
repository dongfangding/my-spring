package com.ddf.framework.customize.spring.beans.demo.service.impl;

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
@Component
public class CglibTransactionalComponent {

    @Autowired
    private PlatformTransactionManage platformTransactionManage;

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
