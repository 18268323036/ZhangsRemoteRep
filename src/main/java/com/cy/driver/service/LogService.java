package com.cy.driver.service;

import com.cy.driver.common.syslog.LogEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangxy 2016/9/18
 */
public interface LogService {

    void saveLogInfo(Long userId, HttpServletRequest request, LogEnum logEnum);
}
