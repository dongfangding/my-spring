package com.ddf.framework.customize.spring.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Service;
import com.ddf.framework.customize.spring.demo.service.ReportService;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 13:28
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public void report(String name) {
        System.out.println("报道任务1： " + name);
    }
}
