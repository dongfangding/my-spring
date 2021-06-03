package com.ddf.framework.customize.spring.demo.model;

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

    @Value(value = "jdbc:mysql://106.75.227.151:3306/framework_customize?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&tinyInt1isBit=false")
    private String jdbcUrl;

    @Value("com.mysql.cj.jdbc.Driver")
    private String driverClassName;

    @Value("develop")
    private String username;

    @Value("12345678")
    private String password;
}
