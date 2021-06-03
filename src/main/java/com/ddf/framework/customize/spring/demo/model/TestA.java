package com.ddf.framework.customize.spring.demo.model;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/06/01 17:22
 */
public class TestA {

    private TestB testB;

    public void delegate() {
        System.out.println("[TestA]: 开始委托TestB做一些事情>>>>>>>");
        testB.doSomething();
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }
}
