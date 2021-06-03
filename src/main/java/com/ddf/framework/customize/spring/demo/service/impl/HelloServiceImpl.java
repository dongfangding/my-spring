package com.ddf.framework.customize.spring.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Service;
import com.ddf.framework.customize.spring.demo.service.HelloService;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 11:55
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public void hello(String name) {
        System.out.println("你好呀，" + name + "!");
    }
}
