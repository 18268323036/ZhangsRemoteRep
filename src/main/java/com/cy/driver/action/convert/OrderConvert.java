package com.cy.driver.action.convert;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.AppOrderState;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.*;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.TransactionCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.order.service.dto.distribute.DistributeOrderDTO;
import com.cy.order.service.dto.order.ApplyPayInfoDTO;
import com.cy.pass.service.dto.OwnerItemStatDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单转换
 * Created by wyh on 2016/4/20.
 */
public class OrderConvert {

    /**
     * 订单列表信息，司机3.4版之前
     */
    public static OrderInfoListBO pageOrder1(PageBase<DistributeOrderDTO> pageBase){
        OrderInfoListBO listBo = new OrderInfoListBO();
        List<OrderInfoBO> orderBoList = new ArrayList<OrderInfoBO>();
        listBo.setAllNums(pageBase.getTotalNum());
        listBo.setAllPage(pageBase.getTotalPage());
        if (pageBase.getListData() != null && pageBase.getListData().size() > 0) {
            for (DistributeOrderDTO item : pageBase.getListData()) {
                if (item.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    /** 分包订单 */
                    orderBoList.add(distributeOrder1(item));
                } else {
                    /** 普通订单 */
                    orderBoList.add(commonOrder1(item));
                }
            }
        }
        listBo.setOrderList(orderBoList);
        return listBo;
    }

    /**
     * 普通订单信息转换，司机3.4版之前
     */
    private static OrderInfoBO commonOrder1(DistributeOrderDTO dto){
        OrderInfoBO orderBO = new OrderInfoBO();
        TransactionInfoDTO orderDTO = dto.getTransactionInfoDTO();
        TransactionCargoDTO cargoDTO = dto.getTransactionCargoDTO();
        /** 订单信息 */
        if (orderDTO != null) {
            orderBO.setOrderId(orderDTO.getId() == null ? "" : orderDTO.getId().toString());//订单id
            orderBO.setOrderStatusCode(
                    SystemsUtil.findAppOrderCode(orderDTO.getTradeStart(),
                            orderDTO.getOrderLock(),
                            orderDTO.getTradeCancelOrigin()));//客户端订单状态编号
            orderBO.setOrderStatusName(
                    SystemsUtil.findAppOrderValue(orderDTO.getTradeStart(),
                            orderDTO.getOrderLock(),
                            orderDTO.getTradeCancelOrigin()));//客户端订单状态编号名称
            orderBO.setBidPrice(
                    SystemsUtil.buildAmountUnit(orderDTO.getRealNeedpayFair()));//成交价格(单位：元)
            orderBO.setMyQuote(
                    SystemsUtil.buildQuoteUnit(orderDTO.getQuoteAmount(),
                            orderDTO.getQuoteType()));//我的报价(带单位：元/车、元/吨、元/方)
            orderBO.setInvoiceNum(orderDTO.getInvoiceNum() == null ? 0 : orderDTO.getInvoiceNum());//发货单上传数量(从0开始)
            orderBO.setReceiptNum(orderDTO.getReceiptNum() == null ? 0 : orderDTO.getReceiptNum());//回单上传数量(从0开始)
            orderBO.setDriverAssessIdent(orderDTO.getDriverAssessIdent() == null ? 0 : orderDTO.getDriverAssessIdent().intValue());//评价标识(0未评价、1已评价)
            orderBO.setTotalFare(SystemsUtil.getTotalFare(orderDTO.getRealNeedpayFair()));
            orderBO.setPrepayFare(SystemsUtil.showAppMoney(orderDTO.getNeedPrepayFair()));
        }
        /** 货源信息 */
        if (cargoDTO != null) {
            orderBO.setCompanyName(cargoDTO.getCompanyName());//公司名称
            orderBO.setStartAddress(
                    SystemsUtil.buildAddress(cargoDTO.getStartProvince(),
                            cargoDTO.getStartCity(),
                            cargoDTO.getStartCounty()));//起始地
            orderBO.setEndAddress(SystemsUtil.buildAddress(cargoDTO.getEndProvince(),
                    cargoDTO.getEndCity(),
                    cargoDTO.getEndCounty()));//目的地
            orderBO.setRequestStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));//要求装货时间
            orderBO.setRequestEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));//要求卸货时间
            orderBO.setCargoName(cargoDTO.getCargoName());//货物名称
            orderBO.setWeight(
                    SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight()));//重量(单位：吨)
            orderBO.setVolume(
                    SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));//体积(单位：方)
            orderBO.setCarLength(
                    SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));//要求的车长(单位：米)
            orderBO.setCarriageTypeName(
                    SystemData.findCarriageTypeName(cargoDTO.getCarriageType()));//车厢类型名称
            orderBO.setVehicleTypeName(
                    SystemData.findVehicleTypeName(cargoDTO.getVehicleType()));//车辆类型名称
        }
        orderBO.setHaveCarrierSign(0);
        return orderBO;
    }

    /**
     * 分包订单信息转换，司机3.4版之前
     */
    private static OrderInfoBO distributeOrder1(DistributeOrderDTO dto){
        OrderInfoBO orderBO = new OrderInfoBO();
        TransactionInfoDTO orderDTO = dto.getTransactionInfoDTO();
        TransactionCargoDTO cargoDTO = dto.getTransactionCargoDTO();
        DistributeInfoDTO distributeDTO = dto.getDistributeInfoDTO();
        /** 派单信息 */
        if (distributeDTO != null) {
            orderBO.setCompanyName(distributeDTO.getSubcontractorName());//公司名称
            if (distributeDTO.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_ALL) {
                orderBO.setHaveCarrierSign(1);
            } else {
                orderBO.setHaveCarrierSign(0);
            }
            /** 订单信息 */
            if (orderDTO != null) {
                orderBO.setOrderId(orderDTO.getId() == null ? "" : orderDTO.getId().toString());//订单id
                orderBO.setOrderStatusCode(
                        SystemsUtil.findAppDistributeOrderCode(orderDTO.getTradeStart(),
                                orderDTO.getOrderLock(),
                                orderDTO.getTradeCancelOrigin(), orderDTO.getCancelStep()));//客户端订单状态编号
                orderBO.setOrderStatusName(
                        SystemsUtil.findAppDistributeOrderValue(orderDTO.getTradeStart(),
                                orderDTO.getOrderLock(),
                                orderDTO.getTradeCancelOrigin(), orderDTO.getCancelStep()));//客户端订单状态编号名称
                orderBO.setBidPrice(
                        SystemsUtil.buildAmountUnit(orderDTO.getRealNeedpayFair()));//成交价格(单位：元)
                orderBO.setMyQuote(
                        SystemsUtil.buildQuoteUnit(orderDTO.getQuoteAmount(),
                                orderDTO.getQuoteType()));//我的报价(带单位：元/车、元/吨、元/方)
                orderBO.setInvoiceNum(orderDTO.getInvoiceNum() == null ? 0 : orderDTO.getInvoiceNum());//发货单上传数量(从0开始)
                orderBO.setReceiptNum(orderDTO.getReceiptNum() == null ? 0 : orderDTO.getReceiptNum());//回单上传数量(从0开始)
                orderBO.setTotalFare(SystemsUtil.getTotalFare(orderDTO.getRealNeedpayFair()));
                orderBO.setPrepayFare(SystemsUtil.showAppMoney(orderDTO.getNeedPrepayFair()));
            }
            /** 货源信息 */
            if (cargoDTO != null) {
                orderBO.setStartAddress(
                        SystemsUtil.buildAddress(cargoDTO.getStartProvince(),
                                cargoDTO.getStartCity(),
                                cargoDTO.getStartCounty()));//起始地
                orderBO.setEndAddress(SystemsUtil.buildAddress(cargoDTO.getEndProvince(),
                        cargoDTO.getEndCity(),
                        cargoDTO.getEndCounty()));//目的地
                orderBO.setRequestStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));//要求装货时间
                orderBO.setRequestEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));//要求卸货时间
                orderBO.setCargoName(cargoDTO.getCargoName());//货物名称
                orderBO.setWeight(
                        SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight()));//重量(单位：吨)
                orderBO.setVolume(
                        SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));//体积(单位：方)
                orderBO.setCarLength(
                        SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));//要求的车长(单位：米)
                orderBO.setCarriageTypeName(
                        SystemData.findCarriageTypeName(cargoDTO.getCarriageType()));//车厢类型名称
                orderBO.setVehicleTypeName(
                        SystemData.findVehicleTypeName(cargoDTO.getVehicleType()));//车辆类型名称
            }
        }
        orderBO.setDriverAssessIdent(Integer.valueOf(orderDTO.getDriverAssessIdent()));//评价标识(0未评价、1已评价)
        return orderBO;
    }

    /**
     * 订单列表转换，司机3.4版本
     */
    public static PageBase<OrderPageInfoBO> pageOrder2(PageBase<DistributeOrderDTO> pageBase) {
        PageBase<OrderPageInfoBO> result = new PageBase();
        result.setTotalNum(pageBase.getTotalNum());
        result.setTotalPage(pageBase.getTotalPage());
        List<OrderPageInfoBO> list = new ArrayList();
        if (pageBase.getListData() != null && pageBase.getListData().size() > 0) {
            for (DistributeOrderDTO item : pageBase.getListData()) {
                list.add(pageOrder2(item));
            }
        }
        result.setListData(list);
        return result;
    }

    /**
     * 分页列表订单信息转换（司机3.4版本）
     */
    private static OrderPageInfoBO pageOrder2(DistributeOrderDTO dto) {
        OrderPageInfoBO orderBO = new OrderPageInfoBO();
        if (dto.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
            /** 分包订单 */
            distributeOrder2(dto.getTransactionInfoDTO(), dto.getTransactionCargoDTO(), dto.getDistributeInfoDTO(), dto.getTransactionKind(), orderBO);
        } else {
            /** 普通订单 */
            commonOrder2(dto.getTransactionInfoDTO(), dto.getTransactionCargoDTO(), dto.getTransactionKind(), orderBO);
        }
        return orderBO;
    }

    /**
     * 普通订单转换（司机3.4版本）
     */
    private static void commonOrder2(TransactionInfoDTO orderDTO, TransactionCargoDTO cargoDTO, Byte transactionKind, OrderPageInfoBO orderBO){
        orderBO.setTransactionKind(transactionKind);
        orderBO.setAuthState(Constants.AUTH_YES);
        /** 订单信息 */
        if (orderDTO != null) {
            orderBO.setOrderId(orderDTO.getId());
            orderBO.setCreateUserId(String.valueOf(orderDTO.getDeployUserid()));//创建人id
            AppOrderState state = AppOrderState.convertAppOrderState(orderDTO.getTradeStart(), orderDTO.getOrderLock(), orderDTO.getTradeCancelOrigin(), orderDTO.getDriverAssessIdent());
            orderBO.setOrderStatusCode(state.getCode());//客户端订单状态编号
            orderBO.setOrderStatusName(state.getOrderValue2());//客户端订单状态编号名称
            orderBO.setMyQuote(
                    SystemsUtil.buildQuoteUnit(orderDTO.getQuoteAmount(),
                            orderDTO.getQuoteType()));//我的报价(带单位：元/车、元/吨、元/方)
            orderBO.setTotalFare(orderDTO.getRealNeedpayFair());
            orderBO.setPrepayFare(orderDTO.getNeedPrepayFair());
            //油卡信息
            orderBO.setOilCard(orderDTO.getOilFare());
            orderBO.setNeedReceiveOilCard(orderDTO.getNeedReceiveOilFare());
            orderBO.setNeedReceiveCash(orderDTO.getNeedReceiveFair());
            orderBO.setCash(orderDTO.getCashFare());

            orderBO.setHaveAudit(haveAudit(orderDTO.getOrderLock().intValue(), orderDTO.getCancelStep().intValue()));
            orderBO.setPromptName(promptName(orderDTO.getTradeCancelOrigin(), orderDTO.getOrderLock()));
            orderBO.setIsShowProceeds(SystemsUtil.isShowProceeds(SystemsUtil.bigAdd(orderDTO.getNeedReceiveFair(),orderDTO.getNeedReceiveOilFare())));

        }
        /** 货源信息 */
        if (cargoDTO != null) {
            orderBO.setCompanyName(cargoDTO.getCompanyName());//公司名称
            if (StringUtils.isBlank(cargoDTO.getContactMobilephone())) {
                orderBO.setContactPhone(cargoDTO.getContactTelephone());
            } else {
                orderBO.setContactPhone(cargoDTO.getContactMobilephone());
            }
            orderBO.setStartAddress(
                    SystemsUtil.buildAddress(cargoDTO.getStartProvince(),
                            cargoDTO.getStartCity(),
                            cargoDTO.getStartCounty()));//起始地
            orderBO.setEndAddress(SystemsUtil.buildAddress(cargoDTO.getEndProvince(),
                    cargoDTO.getEndCity(),
                    cargoDTO.getEndCounty()));//目的地
            orderBO.setRequestStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));//要求装货时间
            orderBO.setRequestEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));//要求卸货时间
            orderBO.setCargoName(cargoDTO.getCargoName());//货物名称
            orderBO.setWeight(
                    SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight()));//重量(单位：吨)
            orderBO.setVolume(
                    SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));//体积(单位：方)
            orderBO.setCarLength(
                    SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));//要求的车长(单位：米)
            orderBO.setCarriageTypeName(
                    SystemData.findCarriageTypeName(cargoDTO.getCarriageType()));//车厢类型名称
            orderBO.setVehicleTypeName(
                    SystemData.findVehicleTypeName(cargoDTO.getVehicleType()));//车辆类型名称
        }
        orderBO.setHaveCarrierSign(0);
    }

    /**
     * 分包订单信息转换（司机3.4版本）
     */
    private static void distributeOrder2(TransactionInfoDTO orderDTO, TransactionCargoDTO cargoDTO, DistributeInfoDTO distributeDTO, Byte transactionKind, OrderPageInfoBO orderBO){
        orderBO.setTransactionKind(transactionKind);
        orderBO.setAuthState(Constants.AUTH_YES);
        /** 派单信息 */
        if (distributeDTO != null) {
            orderBO.setCompanyName(distributeDTO.getSubcontractorName());//公司名称
            orderBO.setContactPhone(distributeDTO.getSubcontractorMobilePhone());//联系号码
            /** 订单信息 */
            if (orderDTO != null) {
                if (orderDTO.getTradeStart().intValue() == SystemsUtil.SERVICE_ORDER_DSJQR) {
                    if (distributeDTO.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_ALL) {
                        orderBO.setHaveCarrierSign(1);
                    } else {
                        orderBO.setHaveCarrierSign(0);
                    }
                } else {
                    orderBO.setHaveCarrierSign(0);
                }
                orderBO.setOrderId(orderDTO.getId());//订单id

                AppOrderState state = AppOrderState.convertAppOrderState(orderDTO.getTradeStart(), orderDTO.getOrderLock(), orderDTO.getTradeCancelOrigin(), orderDTO.getDriverAssessIdent());
                orderBO.setOrderStatusCode(state.getCode());//客户端订单状态编号
                orderBO.setOrderStatusName(state.getDisValue2());//客户端订单状态编号名称
                /** 我的报价(带单位：元/车、元/吨、元/方) */
                orderBO.setMyQuote(
                        SystemsUtil.buildQuoteUnit(orderDTO.getQuoteAmount(),
                                orderDTO.getQuoteType()));
                orderBO.setTotalFare(orderDTO.getRealNeedpayFair());
                orderBO.setPrepayFare(orderDTO.getNeedPrepayFair());
                orderBO.setHaveAudit(haveAudit(orderDTO.getOrderLock().intValue(), orderDTO.getCancelStep().intValue()));
                orderBO.setPromptName(promptName(orderDTO.getTradeCancelOrigin(), orderDTO.getOrderLock()));
                orderBO.setIsShowProceeds(SystemsUtil.isShowProceeds(SystemsUtil.bigAdd(orderDTO.getNeedReceiveFair(),orderDTO.getNeedReceiveOilFare())));
                orderBO.setOilCard(orderDTO.getOilFare());
                orderBO.setNeedReceiveOilCard(orderDTO.getNeedReceiveOilFare());
                orderBO.setNeedReceiveCash(orderDTO.getNeedReceiveFair());
                orderBO.setCash(orderBO.getCash());
            }
            /** 货源信息 */
            if (cargoDTO != null) {
                orderBO.setStartAddress(
                        SystemsUtil.buildAddress(cargoDTO.getStartProvince(),
                                cargoDTO.getStartCity(),
                                cargoDTO.getStartCounty()));//起始地
                orderBO.setEndAddress(SystemsUtil.buildAddress(cargoDTO.getEndProvince(),
                        cargoDTO.getEndCity(),
                        cargoDTO.getEndCounty()));//目的地
                orderBO.setRequestStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));//要求装货时间
                orderBO.setRequestEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));//要求卸货时间
                orderBO.setCargoName(cargoDTO.getCargoName());//货物名称
                orderBO.setWeight(
                        SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight()));//重量(单位：吨)
                orderBO.setVolume(
                        SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));//体积(单位：方)
                orderBO.setCarLength(
                        SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));//要求的车长(单位：米)
                orderBO.setCarriageTypeName(
                        SystemData.findCarriageTypeName(cargoDTO.getCarriageType()));//车厢类型名称
                orderBO.setVehicleTypeName(
                        SystemData.findVehicleTypeName(cargoDTO.getVehicleType()));//车辆类型名称
            }
        }
    }

    private static Integer haveAudit(int orderLock, int cancelStep) {
        if (orderLock == 0) {
            return 0;
        } else {
            /** cancelStep 1到司机审核、2到企业审核、5到分包商审核 */
            if (cancelStep == 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static String promptName(int tradeCancelOrigin, int orderLock) {
        if (orderLock == 0) {
            return "";
        } else {
            /** 0 正常订单，1 司机取消，2货主取消，3司机取消后,货主取消 ，4 系统取消、5分包商取消 */
            if (tradeCancelOrigin == 1) {
                return "司机发起取消订单，等待对方同意";
            } else {
                return "货主发起取消订单";
            }
        }
    }

    /**
     * 订单详情信息转换（司机3.4版本）
     */
    public static OrderDetailsBO orderDetailsBO(OrderAndCargoDTO orderAndCargoDTO, DistributeInfoDTO distributeDTO,
                                                OwnerItemStatDTO ownerItemStatDTO, String ownerHeadImg, ApplyPayInfoDTO applyPayInfoDTO) {
        TransactionInfoDTO orderDTO = orderAndCargoDTO.getOrderInfo();
        TransactionCargoDTO cargoDTO = orderAndCargoDTO.getCargoInfo();
        OrderDetailsBO orderDetailsBO = new OrderDetailsBO();
        if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
            distributeOrder2(orderDTO, cargoDTO, distributeDTO, orderDTO.getTransactionKind(), orderDetailsBO);
        } else {
            commonOrder2(orderDTO, cargoDTO, orderDTO.getTransactionKind(), orderDetailsBO);
        }
        if(applyPayInfoDTO != null){
            orderDetailsBO.setPayTime(DateUtil.dateFormat(applyPayInfoDTO.getPayedTime(), DateUtil.F_DATETIME));
        }
        orderDetailsBO(orderDTO, cargoDTO, distributeDTO, ownerItemStatDTO, orderDetailsBO, ownerHeadImg);
        return orderDetailsBO;
    }

    /**
     * 订单详情信息转换（司机3.4版本）
     */
    private static void orderDetailsBO(TransactionInfoDTO orderDTO, TransactionCargoDTO cargoDTO, DistributeInfoDTO distributeDTO, OwnerItemStatDTO ownerItemStatDTO, OrderDetailsBO orderDetailsBO, String ownerHeadImg) {
        if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
            orderDetailsBO.setContactName(StrUtil.callJoin(distributeDTO.getSubcontractorName()));
        } else {
            orderDetailsBO.setContactName(StrUtil.callJoin(cargoDTO.getContactName()));
        }
        if (ownerItemStatDTO != null)
            orderDetailsBO.setTransactionNum(ownerItemStatDTO.getFinishOrderNum());
        if (orderDTO != null) {
            orderDetailsBO.setOrderNo(orderDTO.getOrderNumber());
            orderDetailsBO.setCreateUserId(String.valueOf(orderDTO.getDeployUserid()));
            orderDetailsBO.setOrderCreateTime(DateUtil.dateTimeToStr(orderDTO.getCreateTime()));
            orderDetailsBO.setInvoiceNum(orderDTO.getInvoiceNum());
            orderDetailsBO.setReceiptNum(orderDTO.getReceiptNum());
            orderDetailsBO.setDriverAssessIdent(orderDTO.getDriverAssessIdent());
            orderDetailsBO.setIsSignProtocol(SystemsUtil.isSignProtocol(orderDTO.getRealReceviedFair()));
            if(orderDTO.getNeedReceiveOilFare()!=null) {
                orderDetailsBO.setPayeeAmount(orderDTO.getNeedReceiveFair().add(orderDTO.getNeedReceiveOilFare()));
            }
            orderDetailsBO.setCancelCause(orderDTO.getCancelCause());
        }
        if (cargoDTO != null) {
            orderDetailsBO.setCargoCreateTime(DateUtil.dateTimeToStr(cargoDTO.getCreateTime()));
            orderDetailsBO.setCargoRemark(cargoDTO.getRemark());
        }
        orderDetailsBO.setOwnerHeadImg(ownerHeadImg);
    }
}
