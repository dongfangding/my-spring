package com.ddf.framework.customize.spring.beans.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Autowired;
import com.ddf.framework.customize.spring.beans.annotation.Service;
import com.ddf.framework.customize.spring.beans.demo.service.HelloService;
import com.ddf.framework.customize.spring.beans.demo.service.ReportService;
import com.ddf.framework.customize.spring.beans.demo.service.TaskService;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 13:25
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private HelloService helloService;
    @Autowired
    private ReportService reportServiceImpl;
    @Autowired
    private ReportService complexReportServiceImpl;

    /**
     * 任务描述
     *
     * @param taskDescription
     * @return
     */
    @Override
    public String doTask(String name, String taskDescription) {
        helloService.hello(name);
        reportServiceImpl.report(name);
        complexReportServiceImpl.report(name);
        System.out.println("去吧，皮卡丘：" + taskDescription);
        return null;
    }
}
