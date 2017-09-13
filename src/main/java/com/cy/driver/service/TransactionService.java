package com.cy.driver.service;

import com.cy.order.service.dto.TransactionCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;

/**
 * 订单信息
 * Created by yanst on 2015/12/23.
 */
public interface TransactionService {

    /**
     * 修改订单是否存在定位信息异常
     * @param orderDTO
     * @param cargoDTO
     * @return
     * @author yanst
     */
    boolean updateOrderWarn(TransactionInfoDTO orderDTO, TransactionCargoDTO cargoDTO, int orderState);
}
