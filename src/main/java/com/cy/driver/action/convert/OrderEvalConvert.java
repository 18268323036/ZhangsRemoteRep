package com.cy.driver.action.convert;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.OrderEvalDetailsBO;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.TransactionCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.assess.OrderAssessDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.pass.service.dto.OwnerItemStatDTO;

/**
 * 订单评价信息
 * Created by wyh on 2016/4/23.
 */
public class OrderEvalConvert {

    /**
     * 订单评价详情信息转换
     */
    public static OrderEvalDetailsBO orderEvalDetailsBO(OrderAndCargoDTO orderAndCargoDTO, DistributeInfoDTO distributeDTO, OwnerItemStatDTO ownerItemStatDTO, String ownerHeadImg, OrderAssessDTO orderAssessDTO) {
        TransactionInfoDTO orderDTO = orderAndCargoDTO.getOrderInfo();
        TransactionCargoDTO cargoDTO = orderAndCargoDTO.getCargoInfo();

        OrderEvalDetailsBO orderEvalDetailsBO = new OrderEvalDetailsBO();
        orderEvalDetailsBO.setAuthState(Constants.AUTH_YES);
        if (orderDTO != null) {
            orderEvalDetailsBO.setOrderId(orderDTO.getId());
            orderEvalDetailsBO.setTransactionKind(orderDTO.getTransactionKind());
            if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                orderEvalDetailsBO.setCompanyName(distributeDTO.getSubcontractorName());
                orderEvalDetailsBO.setContactPhone(distributeDTO.getSubcontractorMobilePhone());
                orderEvalDetailsBO.setContactName(StrUtil.callJoin(distributeDTO.getSubcontractorName()));
            } else {
                if (cargoDTO != null) {
                    orderEvalDetailsBO.setCompanyName(cargoDTO.getCompanyName());
                    if (StringUtils.isBlank(cargoDTO.getContactMobilephone())) {
                        orderEvalDetailsBO.setContactPhone(cargoDTO.getContactTelephone());
                    } else {
                        orderEvalDetailsBO.setContactPhone(cargoDTO.getContactMobilephone());
                    }
                    orderEvalDetailsBO.setContactName(StrUtil.callJoin(cargoDTO.getContactName()));
                }
            }
            orderEvalDetailsBO.setTotalFare(orderDTO.getRealNeedpayFair());
            orderEvalDetailsBO.setPrepayFare(orderDTO.getNeedPrepayFair());
            orderEvalDetailsBO.setDriverAssessIdent(orderDTO.getDriverAssessIdent());
            orderEvalDetailsBO.setOilCard(SystemsUtil.getFare(orderDTO.getOilFare()));
        }
        if (cargoDTO != null) {
            orderEvalDetailsBO.setStartAddress(
                    SystemsUtil.buildAddress(cargoDTO.getStartProvince(),
                            cargoDTO.getStartCity(),
                            cargoDTO.getStartCounty()));//起始地
            orderEvalDetailsBO.setEndAddress(SystemsUtil.buildAddress(cargoDTO.getEndProvince(),
                    cargoDTO.getEndCity(),
                    cargoDTO.getEndCounty()));//目的地
            orderEvalDetailsBO.setRequestStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));//要求装货时间
            orderEvalDetailsBO.setRequestEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));//要求卸货时间
        }
        if (ownerItemStatDTO != null)
            orderEvalDetailsBO.setTransactionNum(ownerItemStatDTO.getFinishOrderNum());
        orderEvalDetailsBO.setOwnerHeadImg(ownerHeadImg);
        if (orderAssessDTO != null) {
            orderEvalDetailsBO.setEval(eval(orderAssessDTO.getAssessType()));
            orderEvalDetailsBO.setEvalContent(orderAssessDTO.getContent());
        }
        return orderEvalDetailsBO;
    }

    private static Integer eval(Integer assessType) {
        /** 评价类型 1:差评 2:中评 3:好评 */
        if (assessType != null) {
            switch (assessType.intValue()) {
                case 3:
                    return Constants.ASSESS_SCORE_GOOD;
                case 2:
                    return Constants.ASSESS_SCORE_MIDDLE;
                case 1:
                    return Constants.ASSESS_SCORE_BAD;
            }
        }
        return null;
    }
}
