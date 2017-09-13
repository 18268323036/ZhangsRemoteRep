package com.cy.driver.service;

import com.cy.driver.domain.ScdProtocolBO;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;

/**
 * 协议服务
 * Created by wyh on 2016/1/14.
 */
public interface ProtocolService {

    /**
     * 司机生成分包订单协议
     * @param orderId
     * @param orderAndCargoDTO
     * @param distributeDTO
     * @return
     * @author wyh
     */
    ScdProtocolBO signElectronicProtocol(Long orderId, OrderAndCargoDTO orderAndCargoDTO, DistributeInfoDTO distributeDTO);
}
