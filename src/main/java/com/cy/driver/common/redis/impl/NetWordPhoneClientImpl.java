package com.cy.driver.common.redis.impl;

import com.cy.driver.common.redis.NetWordPhoneClient;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2015/9/8.
 */
public class NetWordPhoneClientImpl extends RedisService implements NetWordPhoneClient {
    private static Logger logger = LoggerFactory.getLogger(NetWordPhoneClientImpl.class);
    private static final String NUMBER_LIMIT = "cargo:network:limit:";//网络电话次数限制


    /**
     * 获取当前司机拨打网络电话的次数
     * @param driverId 司机ID
     * @return
     */
    public String getNumberLimit(String driverId){
        return super.getStr(NUMBER_LIMIT+driverId);
    }

    /**
     * 存放当前司机拨打网络电话的次数
     * @param driverId 司机ID
     * @return true允许拨打 false不允许拨打
     */
    public void putNumberLimit(String driverId, String cargoId){
        try {
            String cargoIdsStr = super.getStr(NUMBER_LIMIT + driverId);
            if (StringUtils.isEmpty(cargoIdsStr)) {
                cargoIdsStr = cargoId;
            }else{
                String[] cargoIds = cargoIdsStr.split(",");
                if (cargoIds.length >= 3) {
                    return;
                }
                boolean flag = true;
                for (String id : cargoIds) {
                    if (id.equals(cargoId)) {
                        flag = false;
                    }
                }
                if(flag){
                    cargoIdsStr += "," + cargoId;
                }
            }
            super.setStr(NUMBER_LIMIT + driverId, cargoIdsStr, DateUtil.getTomorrowDate(new Date(), 1));
        } catch (Exception e) {
            logger.error("存放当前司机拨打网络电话的次数redis出错", e);
        }
    }

    /**
     * 判断是否可以拨打电话
     * @param driverId
     * @param cargoId
     * @return 0允许打电话 1不允许打电话
     */
    @Override
    public String haveCallNumber(String driverId, String cargoId, Byte state){
        try {
            if(state != null && state.intValue() == 3){//已认证用户
                return "0";
            }
            String cargoIdsStr = super.getStr(NUMBER_LIMIT + driverId);
            if (StringUtils.isEmpty(cargoIdsStr)) {
                return "0";
            } else {
                String[] cargoIds = cargoIdsStr.split(",");
                for (String id : cargoIds) {
                    if (id.equals(cargoId)) {
                        return "0";//看过的允许查看
                    }
                }
                if (cargoIds.length >= 3) {
                    return "1";
                } else {
                    return "0";
                }
            }
        } catch (Exception e) {
            logger.error("判断是否可以拨打电话出错", e);
        }
        return "0";
    }

    /**
     * 移除当前司机拨打网络电话的次数
     * @param driverId 司机ID
     * @return
     */
    public void removeNumberLimit(String driverId){
        super.del(NUMBER_LIMIT+driverId);
    }
}
