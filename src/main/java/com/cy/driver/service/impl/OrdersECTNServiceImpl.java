package com.cy.driver.service.impl;

import com.cy.driver.service.OrdersECTNService;
import com.cy.order.service.OrderECTNService;
import com.cy.order.service.dto.OrderInvoiceDTO;
import com.cy.order.service.dto.OrderReceiptPathDTO;
import com.cy.order.service.dto.base.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 */
@Service("ordersECTNService")
public class OrdersECTNServiceImpl implements OrdersECTNService {

    @Resource
    private OrderECTNService orderECTNService;

    @Override
    public Response<Boolean> uploadInvoice(Long orderId, String invoiceNo, List<String> imgUrlList)
    {
        return orderECTNService.assertInvoice(orderId, invoiceNo, imgUrlList);
    }

    @Override
    public Response<OrderInvoiceDTO> lookInvoice(Long orderId)
    {
        return orderECTNService.listInvoice(orderId);
    }

    @Override
    public Response<Boolean> uploadRecTeipt(Long orderId, List<String> imgUrlList)
    {
        return orderECTNService.assertReceipt(orderId, imgUrlList);
    }

    @Override
    public Response<List<OrderReceiptPathDTO>> lookReceipt(Long orderId)
    {
        return orderECTNService.listReceipt(orderId);
    }

}
