package com.cy.driver.service;

import com.cy.driver.domain.ScdProtocolBO;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;

/**
 * Created by wyh on 2016/1/14.
 */
public interface ViewService {

    /**
     * 司机承运签订协议转换
     * @param orderId 订单id
     * @param orderAndCargoDTO 订单货源信息
     * @param distributeDTO 派单信息
     * @return
     * @author wyh
     */
    ScdProtocolBO converterScdProtocol(Long orderId, OrderAndCargoDTO orderAndCargoDTO, DistributeInfoDTO distributeDTO);
}
