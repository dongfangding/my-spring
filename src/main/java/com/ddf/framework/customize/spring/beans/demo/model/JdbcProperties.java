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

    @Value(value = "jdbc:mysql://localhost:3306/zdy_mybatis?useUnicode=true&amp;characterEncoding=UTF8&amp;useSSL=false&amp;serverTimezone=GMT%2B8&amp;zeroDateTimeBehavior=convertToNull&amp;allowMultiQueries=true&amp;autoReconnect=true&amp;failOverReadOnly=false&amp;maxReconnects=10&amp;tinyInt1isBit=false")
    private String jdbcUrl;

    @Value("com.mysql.cj.jdbc.Driver")
    private String driverClassName;

    @Value("root")
    private String username;

    @Value("123456")
    private String password;
}
