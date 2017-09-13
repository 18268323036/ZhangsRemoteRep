package com.cy.driver.service.impl;

import com.cy.driver.service.SavePushTokenService;
import com.cy.pass.service.DriverTelephoneInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wyh on 2015/7/24.
 */
@Service("savePushTokenService")
public class SavePushTokenServiceImpl implements SavePushTokenService {
    @Resource
    private DriverTelephoneInfoService driverTelephoneInfoService;

    @Override
    public boolean savePushToken(long driverId, String pushToken) {
        return driverTelephoneInfoService.savePushToken(driverId, pushToken);
    }
}
