package com.cy.driver.cloudService;

import com.cy.rdcservice.service.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxy 2016/7/28
 */
public interface CloudCommonWaybillService {

    /**
     * 查询首页显示的各类订单数量
     * @param driverId
     * @param queryOrderStateList
     * @return
     */
    List<Long> queryWaybillNum(Long driverId, List<Integer> queryOrderStateList);

    /**
     * 查询订单/运单列表
     * @param driverId
     * @param page
     * @param filterState
     * @param queryOrderState
     * @return
     */
    Map<String,Object> queryWaybillList(Long driverId, Integer page, Integer filterState, Integer queryOrderState);

    /**
     * 查询订单详情
     * @param waybillNum
     * @return
     */
    WaybillDetailDTO queryWaybillDetail(Long waybillNum);

    /**
     * 查询运单签收信息
     */
    List<Integer> querySignInInfo(List<Long> waybillNum);

    /**
     * 改变运单状态
     * @param waybillStateModifyDTO
     * @return
     */
    boolean modifyWaybillStatus(WaybillStateModifyDTO waybillStateModifyDTO, ProtocolDTO protocolDTO);

    /**
     * 承运方报价运单
     * @param orderId
     * @param quoteAmount
     * @param advancePayment
     */
    boolean driverQuoteWaybill(Long orderId, BigDecimal quoteAmount, BigDecimal advancePayment);

    /**
     * 司机转单
     */
    Long turnWaybill(WaybillTurnOrderDTO waybillTurnOrderDTO, Integer takeWay);

    /**
     * 查询子运单信息
     */
    WaybillInfoDTO getByParentOrderId(Long waybillId);
}
