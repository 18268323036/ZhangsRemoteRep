package com.cy.driver.service.impl;

import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.IPUtil;
import com.cy.driver.service.LogService;
import com.cy.syslog.service.DriverLogService;
import com.cy.syslog.service.dto.LogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zhangxy 2016/9/18 10:31
 */
@Service("logService")
public class LogServiceImpl implements LogService {
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private DriverLogService driverLogService;



    @Override
    public void saveLogInfo(Long userId, HttpServletRequest request, LogEnum logEnum) {
        LogDTO logDTO = new LogDTO();
        logDTO.setUserDriverId(userId);
        logDTO.setCreateTime(new Date());
        logDTO.setOperationType(logEnum.getOperationType());
        logDTO.setRemark("司机app接口:"+logEnum.getRemark());
        logDTO.setOperationName(logEnum.getOperationName());
        logDTO.setRequestFromIp(IPUtil.findRequestIp(request));
        driverLogService.saveLog(logDTO);
    }
}
