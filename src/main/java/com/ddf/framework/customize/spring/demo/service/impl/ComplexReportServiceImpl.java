package com.ddf.framework.customize.spring.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Component;
import com.ddf.framework.customize.spring.demo.service.ReportService;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 13:29
 */
@Component(value = "reportService")
public class ComplexReportServiceImpl implements ReportService {
    @Override
    public void report(String name) {
        System.out.println("锣鼓声天，鞭炮齐鸣的报告: "+ name);
    }
}
