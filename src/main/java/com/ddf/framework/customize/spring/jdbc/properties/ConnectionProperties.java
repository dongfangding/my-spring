package com.ddf.framework.customize.spring.jdbc.properties;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Jdbc数据源连接属性</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 17:52
 */
@Data
@Accessors(chain = true)
public class ConnectionProperties {

    private String driverClassName;

    private String url;

    private String username;

    private String password;
}
