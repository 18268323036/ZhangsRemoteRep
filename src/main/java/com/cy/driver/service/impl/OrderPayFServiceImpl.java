package com.cy.driver.service.impl;

import com.cy.driver.service.OrderPayFService;
import com.cy.order.service.OrderPayService;
import com.cy.order.service.dto.base.Response;
import com.cy.order.service.dto.order.ApplyPayInfo2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yanst 2016/4/25 17:13
 */
@Service("orderPayFService")
public class OrderPayFServiceImpl implements OrderPayFService {

    private Logger LOG = LoggerFactory.getLogger(OrderPayFServiceImpl.class);

    @Resource
    private OrderPayService orderPayService;

    @Override
    public ApplyPayInfo2DTO getPayInfo(Long orderId) {

        Response<ApplyPayInfo2DTO> response = orderPayService.getLastPayRecord(orderId);
        if (response == null) {
            if (LOG.isErrorEnabled()) {
                LOG.error("调用底层获取最新的支付记录失败，返回对象为空，orderId={}", orderId);
            }
            return null;
        }
        if (!response.isSuccess()) {
            if (LOG.isErrorEnabled()) {
                LOG.error("调用底层获取最新的支付记录出错，orderId={}，返回信息response={}", orderId, response.getData());
            }
        }
        return response.getData();
    }

}
