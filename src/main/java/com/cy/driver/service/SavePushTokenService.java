package com.cy.driver.service;

/**
 * Created by wyh on 2015/7/24.
 */
public interface SavePushTokenService {

    /**
     * 保存推送的token
     * @param driverId
     * @param pushToken
     * @return
     */
    boolean savePushToken(long driverId, String pushToken);
}
