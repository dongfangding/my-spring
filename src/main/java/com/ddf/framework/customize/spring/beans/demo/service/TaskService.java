package com.ddf.framework.customize.spring.beans.demo.service;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 13:24
 */
public interface TaskService {

    /**
     * 任务描述
     *
     * @param name
     * @param taskDescription
     * @return
     */
    String doTask(String name, String taskDescription);
}
