package com.ddf.framework.customize.spring.beans.demo.model;

import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.beans.annotation.Value;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 16:46
 */
@Component
@Data
public class JdbcProperties {

    @Value("1000")
    private Integer id;

    @Value(value = "jdbc:mysql://localhost:3306/db")
    private String jdbcUrl;

    @Value("com.mysql.cj.driver")
    private String driverClassName;

    @Value("root")
    private String username;

    @Value("password")
    private String password;
}
