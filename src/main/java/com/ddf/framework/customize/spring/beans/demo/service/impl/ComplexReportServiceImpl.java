package com.ddf.framework.customize.spring.beans.demo.service.impl;

import com.ddf.framework.customize.spring.beans.annotation.Service;
import com.ddf.framework.customize.spring.beans.demo.service.ReportService;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2021/05/27 13:29
 */
@Service
public class ComplexReportServiceImpl implements ReportService {
    @Override
    public void report(String name) {
        System.out.println("锣鼓声天，鞭炮齐鸣的报告: "+ name);
    }
}
