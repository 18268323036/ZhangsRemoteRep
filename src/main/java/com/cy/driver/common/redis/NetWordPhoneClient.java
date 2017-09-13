package com.cy.driver.common.redis;

/**
 * Created by Administrator on 2015/9/8.
 * 网络电话缓存
 */
public interface NetWordPhoneClient {


    /**
     * 获取当前司机拨打网络电话的次数
     * @param driverId 司机ID
     * @return
     */
    public String getNumberLimit(String driverId);

    /**
     * 存放当前司机拨打网络电话的次数
     * @param driverId 司机ID
     * @return
     */
    public void putNumberLimit(String driverId, String cargoId);

    /**
     * 判断是否可以拨打电话
     * @param driverId
     * @param cargoId
     * @return 0允许打电话 1不允许打电话
     */
    String haveCallNumber(String driverId, String cargoId, Byte state);

    /**
     * 移除当前司机拨打网络电话的次数
     * @param driverId 司机ID
     * @return
     */
    public void removeNumberLimit(String driverId);
    
}
