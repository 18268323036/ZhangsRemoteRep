package com.cy.driver.service;

import com.cy.order.service.dto.OrderInvoiceDTO;
import com.cy.order.service.dto.OrderReceiptPathDTO;
import com.cy.order.service.dto.base.Response;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 * 订单 电子单处理
 */
public interface OrdersECTNService {

    /**
     * 上传发货单
     * @param orderId   订单ID
     * @param invoiceNo 发货单号
     * @param imgUrlList 图片md5 url
     * @return
     */
    public Response<Boolean> uploadInvoice(Long orderId, String invoiceNo, List<String> imgUrlList);;

    /**
     * 查看发货单
     * @param orderId  订单ID
     * @return
     */
    public Response<OrderInvoiceDTO> lookInvoice(Long orderId);

    /**
     * 上传回单
     * @param orderId   订单ID
     * @param imgUrlList 图片md5 url
     * @return
     */
    public Response<Boolean> uploadRecTeipt(Long orderId, List<String> imgUrlList);

    /**
     * 查看回单
     * @param orderId  订单ID
     * @return
     */
    public Response<List<OrderReceiptPathDTO>> lookReceipt(Long orderId);
}
