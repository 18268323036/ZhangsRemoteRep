package com.cy.driver.service;

import com.cy.order.service.dto.order.ApplyPayInfo2DTO;

/**
 * @author yanst 2016/4/25
 */
public interface OrderPayFService {

    /**
     * 获取订单最新一条支付信息
     * @param orderId
     * @return
     */
    ApplyPayInfo2DTO getPayInfo(Long orderId);
}
