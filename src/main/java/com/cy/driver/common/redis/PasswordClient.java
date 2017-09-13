package com.cy.driver.common.redis;

/**
 * Created by Administrator on 2015/9/16.
 * 密码缓存处理
 *
 */
public interface PasswordClient {

    /**
     * 移除司机当天提现密码  出错次数
     *
     * @param driverId 司机ID
     */
    public void removeDriverWithdram(Long driverId);


    /**
     * 判断司机提现密码连续输错次数
     *
     * @param driverId 司机ID
     *@reurn boolean :true 超过
     */
    public boolean havePassLimit(Long driverId);

}
