package com.cy.driver.service;

import com.cy.driver.domain.DriverProtocolBO;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/9/11.
 */
public interface OrderReceivableService {

    /**
     * 确认收款
     *
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @param needReceiveFair 待收运费
     * @return 0 成功
     */
    public String receivabile(Long orderId, Long driverId, BigDecimal needReceiveFair, BigDecimal needReceiveOilFare);

    /**
     * 生成运输协议
     *
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @return 0 成功
     */
    public DriverProtocolBO transprotPro(Long orderId, Long driverId);

}
