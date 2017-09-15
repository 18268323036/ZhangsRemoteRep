package com.cy.driver.common.redis.impl;

import com.cy.driver.common.redis.PasswordClient;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2015/9/16.
 */
public class PasswordClientImpl extends RedisService implements PasswordClient {

    private final Logger LOG  = LoggerFactory.getLogger(PasswordClientImpl.class);

    private static final String DRIVER_PASSWORD_WITHDRAW = "driver:password:withdraw";//司机提现密码

    private static final int WITHDRAW_ERROR_NUM = 5;//司机提现密码输入错误次数

    /**
     * 移除司机当天提现密码  出错次数
     *
     * @param driverId 司机ID
     */
    public void removeDriverWithdram(Long driverId){
        super.del(DRIVER_PASSWORD_WITHDRAW+driverId);
    }


    /**
     * 判断司机提现密码连续输错次数
     *
     * @param driverId 司机ID
     *@reurn boolean :true 超过
     */
    public boolean havePassLimit(Long driverId){
        String str = (String)super.getStr(DRIVER_PASSWORD_WITHDRAW+driverId);
        Integer count = 1;
        if(StringUtils.isNotBlank(str)){
            count = Integer.valueOf(str)+1;
        }
        super.setStr(DRIVER_PASSWORD_WITHDRAW + driverId, count.toString(), DateUtil.getTomorrowDate(new Date(), 1));
        return count > WITHDRAW_ERROR_NUM ? true :false;
    }

}
