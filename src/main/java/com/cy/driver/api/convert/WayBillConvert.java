package com.cy.driver.api.convert;

import com.cy.driver.api.domain.req.WaybillDetailVO;
import com.cy.driver.api.domain.res.CarrierDetail;
import com.cy.driver.api.domain.res.WayBillDetail;
import com.cy.driver.api.domain.res.WayBillDetailCargoList;
import com.cy.driver.api.domain.res.WayBillList;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.pass.service.dto.DriverItemStatDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.rdcservice.service.dto.*;
import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import com.cy.saas.business.model.dto.CarrierItemDTO;
import com.cy.saas.business.model.dto.CarrierTradeDTO;
import com.cy.saas.business.model.dto.WaybillDetailsDTO;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.search.service.dto.response.OrderInfoDTO;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanst 2016/6/1 16:56
 */
public class WayBillConvert {

    /**
     * 订单列表转换
     *
     * @return
     */
    public static List<WayBillList> wayBillListConvert(List<OrderInfoDTO> orderInfoDTOs) {
        List<WayBillList> list = new ArrayList<>();
        for (OrderInfoDTO orderInfoDTO : orderInfoDTOs) {
            WayBillList wayBillList = new WayBillList();
//            wayBillList.setWaitSignIn();
            wayBillList.setOrderId(String.valueOf(orderInfoDTO.getOrderId()));
            wayBillList.setOrderLock(orderInfoDTO.getOrderLock());
            wayBillList.setCompanyId(orderInfoDTO.getCreateCompanyId());

            if (StringUtils.isEmpty(orderInfoDTO.getCreateCompanyName())) {
                wayBillList.setCompanyName(orderInfoDTO.getCreateUserName());
            } else {
                wayBillList.setCompanyName(orderInfoDTO.getCreateCompanyName());
            }
            if(orderInfoDTO.getOrderSource()==2){
                //0表示无需签协议，1表示需要签协议
                wayBillList.setHaveCarrierSign("1");
            }else {
                if(orderInfoDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    if (orderInfoDTO.getTotalFareAllotModel() != null) {
                        if (orderInfoDTO.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_ALL) {
                            wayBillList.setHaveCarrierSign("1");
                        } else {
                            wayBillList.setHaveCarrierSign("0");
                        }
                    }
                }else{
                    wayBillList.setHaveCarrierSign("0");
                }
            }
            wayBillList.setOrderSource(orderInfoDTO.getOrderSource());
            wayBillList.setCancleSource(orderInfoDTO.getCancelSource());
            wayBillList.setCargoName(orderInfoDTO.getCargoName());
            wayBillList.setCarrierName(orderInfoDTO.getCargoName());
            wayBillList.setOrderStatusCode(
                    SystemsUtil.findWayBillAppOrderCode(orderInfoDTO.getOrderTradeStart(),
                            orderInfoDTO.getOrderLock(),
                            orderInfoDTO.getCancelSource(), orderInfoDTO.getOrderSource(), orderInfoDTO.getOrderTurnedState()));
            wayBillList.setStateName(waybillStateNameConvert2(wayBillList.getOrderStatusCode()));
            wayBillList.setCarLength(SystemsUtil.buildCarLenUnit(orderInfoDTO.getCarLength()));
            wayBillList.setCreateOwnUserId(orderInfoDTO.getCreateOwnUserId());
            wayBillList.setCreateUserId(orderInfoDTO.getCreateUserId());
            wayBillList.setCreateUserName(orderInfoDTO.getCreateUserName());
            wayBillList.setCreateUserType(orderInfoDTO.getCreateUserType());
            wayBillList.setCargoTotalQuantity(SystemsUtil.buildQuantity(orderInfoDTO.getCargoTotalQuantity()));
            if(orderInfoDTO.getCargoDepartureMobile()!=null){
                wayBillList.setContactPhone(orderInfoDTO.getCargoDepartureMobile());
            }else{
                wayBillList.setContactPhone(orderInfoDTO.getCargoDepartureTelephone());
            }
            wayBillList.setCargoCubage(SystemsUtil.buildVolumeUnit(orderInfoDTO.getCargoCubage()));
            if (orderInfoDTO.getCreateUserType() == 3) {
                wayBillList.setCargoWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(orderInfoDTO.getCargoWeight())));
            } else {
                wayBillList.setCargoWeight(SystemsUtil.buildWeightUnit(orderInfoDTO.getCargoWeight()));
            }
            wayBillList.setOrderSource(orderInfoDTO.getOrderSource());
            if(orderInfoDTO.getOrderTransportAssessIdent()!=null) {
                wayBillList.setIsComment(String.valueOf(orderInfoDTO.getOrderTransportAssessIdent()));
            }
            if(orderInfoDTO.getOrderSubAssessIdent()!=null){
                wayBillList.setOrderSubAssessIdent(String.valueOf(orderInfoDTO.getOrderSubAssessIdent()));
            }
            wayBillList.setStartAddress(SystemsUtil.buildAddress(orderInfoDTO.getCargoDepartureProvinceValue(), orderInfoDTO.getCargoDepartureCityValue(), orderInfoDTO.getCargoDepartureCountyValue()));
            wayBillList.setEndAddress(SystemsUtil.buildAddress(orderInfoDTO.getCargoReceiveProvinceValue(), orderInfoDTO.getCargoReceiveCityValue(), orderInfoDTO.getCargoReceiveCountyValue()));
            wayBillList.setRequestStartTime(SystemsUtil.cargoOrderStartTime(orderInfoDTO.getCargoRequestStartTime()));
            wayBillList.setRequestEndTime(SystemsUtil.cargoOrderEndTime(orderInfoDTO.getCargoRequestEndTime()));
            if (orderInfoDTO.getTransportFareState() == null) {
                wayBillList.setTransportFareState(0);
            } else {
                wayBillList.setTransportFareState(orderInfoDTO.getTransportFareState());
            }
            wayBillList.setOrderQuotedAmount(orderInfoDTO.getOrderRealNeedpayFair());//货主报价(价格为0表示价格面议)
            wayBillList.setOrderQuotedPrepay(orderInfoDTO.getOrderNeedPrepayFair());//货主报价预付款
            wayBillList.setFinalPayment(orderInfoDTO.getOrderRealNeedpayFair());//确认后的最终运费价格
            wayBillList.setFinalAdvancePayment(orderInfoDTO.getOrderNeedPrepayFair());//确认后的最终预付款
            wayBillList.setTransportQuote(orderInfoDTO.getTransportTotalFare());//司机报价
            wayBillList.setTransportQuotePrepay(orderInfoDTO.getTransportPrepayFare());//司机报价预付款
            wayBillList.setPlatformCode(orderInfoDTO.getPlatformCode());
            wayBillList.setTotalCollectionPayment(orderInfoDTO.getTotalCollectionPayment());

            list.add(wayBillList);
        }
        return list;
    }

    /**
     * 合并WaybillDetailVO，UserLoginInfoDTO至WaybillInfoDTO
     *
     * @return
     */
    public static WaybillTurnOrderDTO waybillDetailUserInfoMerge(WaybillDetailVO waybillDetailVO, UserLoginInfoDTO userLoginInfoDTO, DriverUserInfoDTO deliverDriver) {
        WaybillTurnOrderDTO waybillTurnOrderDTO = new WaybillTurnOrderDTO();
        waybillTurnOrderDTO.setCreateUserContact(deliverDriver.getCode());
        waybillTurnOrderDTO.setCreateUserName(deliverDriver.getName());
        waybillTurnOrderDTO.setCreateUserId(deliverDriver.getId());
        waybillTurnOrderDTO.setCreateUserType((byte) 1);
        waybillTurnOrderDTO.setDepartureTelephone(deliverDriver.getCode());
        waybillTurnOrderDTO.setDepartureAddress(waybillDetailVO.getStartAddress());
        waybillTurnOrderDTO.setDepartureProvinceCode(waybillDetailVO.getStartProvinceCode());
        waybillTurnOrderDTO.setDepartureProvinceValue(waybillDetailVO.getStartProvinceValue());
        waybillTurnOrderDTO.setDepartureCityCode(waybillDetailVO.getStartCityCode());
        waybillTurnOrderDTO.setDepartureCityValue(waybillDetailVO.getStartCityValue());
        waybillTurnOrderDTO.setDepartureCountyCode(waybillDetailVO.getStartCountyCode());
        waybillTurnOrderDTO.setDepartureCountyValue(waybillDetailVO.getStartCountyValue());
        waybillTurnOrderDTO.setNeedStartTime(DateUtil.parseDate(waybillDetailVO.getStartTime(), DateUtil.F_DATE));
        waybillTurnOrderDTO.setParentWaybillId(Long.valueOf(waybillDetailVO.getOrderId()));
        waybillTurnOrderDTO.setPrepayFare(BigDecimal.valueOf(Double.valueOf(waybillDetailVO.getOrderQuotedPrepay())));
        waybillTurnOrderDTO.setTotalFare(BigDecimal.valueOf(Double.valueOf(waybillDetailVO.getOrderQuotedAmount())));
        waybillTurnOrderDTO.setRemark(waybillDetailVO.getRemark());
        waybillTurnOrderDTO.setTransportOwnUserId(userLoginInfoDTO.getParentId());
        waybillTurnOrderDTO.setTransportMobile(userLoginInfoDTO.getMobilephone());
        waybillTurnOrderDTO.setTransportHeadImgUrl(userLoginInfoDTO.getHeadImg());
        waybillTurnOrderDTO.setTransportType((byte) 3);
        waybillTurnOrderDTO.setTransportUserId(userLoginInfoDTO.getId());
        waybillTurnOrderDTO.setTransportUserName(userLoginInfoDTO.getName());
        waybillTurnOrderDTO.setWaybillNature((byte) 1);
        return waybillTurnOrderDTO;
    }

    /**
     * 合并WaybillDetailVO，DriverUserInfoDTO至WaybillInfoDTO
     *
     * @return
     */
    public static WaybillTurnOrderDTO waybillDetailUserInfoMerge(WaybillDetailVO waybillDetailVO, DriverUserInfoDTO driverUserInfoDTO, DriverUserInfoDTO deliverDriver) {
        WaybillTurnOrderDTO waybillTurnOrderDTO = new WaybillTurnOrderDTO();
        waybillTurnOrderDTO.setCreateUserContact(deliverDriver.getCode());
        waybillTurnOrderDTO.setCreateUserName(deliverDriver.getName());
        waybillTurnOrderDTO.setCreateUserId(deliverDriver.getId());
        waybillTurnOrderDTO.setCreateUserType((byte) 1);
        waybillTurnOrderDTO.setDepartureTelephone(deliverDriver.getCode());
        waybillTurnOrderDTO.setDepartureAddress(waybillDetailVO.getStartAddress());
        waybillTurnOrderDTO.setDepartureProvinceCode(waybillDetailVO.getStartProvinceCode());
        waybillTurnOrderDTO.setDepartureProvinceValue(waybillDetailVO.getStartProvinceValue());
        waybillTurnOrderDTO.setDepartureCityCode(waybillDetailVO.getStartCityCode());
        waybillTurnOrderDTO.setDepartureCityValue(waybillDetailVO.getStartCityValue());
        waybillTurnOrderDTO.setDepartureCountyCode(waybillDetailVO.getStartCountyCode());
        waybillTurnOrderDTO.setDepartureCountyValue(waybillDetailVO.getStartCountyValue());
        waybillTurnOrderDTO.setNeedStartTime(DateUtil.parseDate(waybillDetailVO.getStartTime(), DateUtil.F_DATE));
        waybillTurnOrderDTO.setParentWaybillId(Long.valueOf(waybillDetailVO.getOrderId()));
        waybillTurnOrderDTO.setPrepayFare(BigDecimal.valueOf(Double.valueOf(waybillDetailVO.getOrderQuotedPrepay())));
        waybillTurnOrderDTO.setTotalFare(BigDecimal.valueOf(Double.valueOf(waybillDetailVO.getOrderQuotedAmount())));
        waybillTurnOrderDTO.setRemark(waybillDetailVO.getRemark());
        waybillTurnOrderDTO.setTransportMobile(driverUserInfoDTO.getCode());
        waybillTurnOrderDTO.setTransportType((byte) 1);
        waybillTurnOrderDTO.setTransportCarnumber(driverUserInfoDTO.getCarNumber());
        waybillTurnOrderDTO.setTransportUserId(driverUserInfoDTO.getId());
        waybillTurnOrderDTO.setTransportUserName(driverUserInfoDTO.getName());
        waybillTurnOrderDTO.setWaybillNature((byte) 2);
        return waybillTurnOrderDTO;
    }

    /**
     * 合并WaybillDetailVO，DriverUserInfoDTO至WaybillInfoDTO
     *
     * @return
     */
    public static WaybillTurnOrderDTO waybillDetailUserInfoMerge(WaybillDetailVO waybillDetailVO, DriverUserInfoDTO deliverDriver) {
        WaybillTurnOrderDTO waybillTurnOrderDTO = new WaybillTurnOrderDTO();
        waybillTurnOrderDTO.setCreateUserContact(deliverDriver.getCode());
        waybillTurnOrderDTO.setCreateUserName(deliverDriver.getName());
        waybillTurnOrderDTO.setCreateUserId(deliverDriver.getId());
        waybillTurnOrderDTO.setCreateUserType((byte) 1);
        waybillTurnOrderDTO.setDepartureTelephone(deliverDriver.getCode());
        waybillTurnOrderDTO.setDepartureAddress(waybillDetailVO.getStartAddress());
        waybillTurnOrderDTO.setDepartureProvinceCode(waybillDetailVO.getStartProvinceCode());
        waybillTurnOrderDTO.setDepartureProvinceValue(waybillDetailVO.getStartProvinceValue());
        waybillTurnOrderDTO.setDepartureCityCode(waybillDetailVO.getStartCityCode());
        waybillTurnOrderDTO.setDepartureCityValue(waybillDetailVO.getStartCityValue());
        waybillTurnOrderDTO.setDepartureCountyCode(waybillDetailVO.getStartCountyCode());
        waybillTurnOrderDTO.setDepartureCountyValue(waybillDetailVO.getStartCountyValue());
        waybillTurnOrderDTO.setNeedStartTime(DateUtil.parseDate(waybillDetailVO.getStartTime(), DateUtil.F_DATE));
        waybillTurnOrderDTO.setParentWaybillId(Long.valueOf(waybillDetailVO.getOrderId()));
        waybillTurnOrderDTO.setPrepayFare(BigDecimal.valueOf(Double.valueOf(waybillDetailVO.getOrderQuotedPrepay())));
        waybillTurnOrderDTO.setTotalFare(BigDecimal.valueOf(Double.valueOf(waybillDetailVO.getOrderQuotedAmount())));
        waybillTurnOrderDTO.setRemark(waybillDetailVO.getRemark());
        waybillTurnOrderDTO.setTransportMobile(waybillDetailVO.getTransportMobile());
        waybillTurnOrderDTO.setTransportType((byte) 1);
        waybillTurnOrderDTO.setTransportCarnumber(waybillDetailVO.getCarNumber());
        waybillTurnOrderDTO.setTransportUserName(waybillDetailVO.getTransportName());
        waybillTurnOrderDTO.setWaybillNature((byte) 1);

        return waybillTurnOrderDTO;
    }


    /**
     * 运单详情转换(云配)
     *
     * @param waybillDetailDTO  运单信息
     * @param userLoginInfoDTO  云配用户信息
     * @param userItemStatDTO   云配用户业务信息
     * @param driverUserInfoDTO 快到司机信息
     * @param driverItemStatDTO 快到司机业务信息
     * @param imgAddress        快到司机照片信息
     * @return
     */
    public static WayBillDetail wayBillDetailConvert(WaybillDetailDTO waybillDetailDTO, UserLoginInfoDTO userLoginInfoDTO, UserItemStatDTO userItemStatDTO, DriverUserInfoDTO driverUserInfoDTO, DriverItemStatDTO driverItemStatDTO, String imgAddress) {
        WayBillDetail wayBillDetail = new WayBillDetail();
        WaybillInfoDTO waybillInfoDTO = waybillDetailDTO.getWaybillInfoDTO();
        wayBillDetail.setOrderId(String.valueOf(waybillInfoDTO.getId()));
        wayBillDetail.setWaybillNum(waybillInfoDTO.getWaybillNum());
        wayBillDetail.setOrderStatusName(SystemsUtil.findWaybillAppOrderValue(waybillInfoDTO.getState(),
                waybillInfoDTO.getWaybillLock(),
                waybillInfoDTO.getCancelSource(), 2, waybillInfoDTO.getTurnedState()));
        wayBillDetail.setOrderStatusCode(SystemsUtil.findWayBillAppOrderCode(waybillInfoDTO.getState(),
                waybillInfoDTO.getWaybillLock(),
                waybillInfoDTO.getCancelSource(), 2, waybillInfoDTO.getTurnedState()));
        wayBillDetail.setOrderLock(Integer.valueOf(waybillInfoDTO.getWaybillLock()));
        wayBillDetail.setCancleSource(Integer.valueOf(waybillInfoDTO.getCancelSource()));
        wayBillDetail.setCancleCause(waybillInfoDTO.getCancelCause());
        wayBillDetail.setTurnedState(Integer.valueOf(waybillInfoDTO.getTurnedState()));
        wayBillDetail.setCreateTime(DateUtil.dateFormat(waybillInfoDTO.getCreateTime(), DateUtil.F_DATETIME));
        wayBillDetail.setCompanyName(waybillInfoDTO.getCompanyName());
        wayBillDetail.setCreateUserId(waybillInfoDTO.getCreateUserId());
        wayBillDetail.setCreateUserType(Integer.valueOf(waybillInfoDTO.getCreateUserType()));
        wayBillDetail.setOwnerName(waybillInfoDTO.getCreateUserName());
        wayBillDetail.setPhotosAddress(waybillInfoDTO.getCreateHeadImgUrl());
        //创建人信息
        if (userLoginInfoDTO != null) {
            wayBillDetail.setOrderPhone(userLoginInfoDTO.getMobilephone());
        }
        if (driverUserInfoDTO != null) {
            wayBillDetail.setOrderPhone(driverUserInfoDTO.getCode());
        }
        //创建人业务信息
        if (userItemStatDTO != null) {
            wayBillDetail.setTransactionNumber(userItemStatDTO.getFinishOrderNum());
        }
        if (driverItemStatDTO != null) {
            wayBillDetail.setTransactionNumber(driverItemStatDTO.getFinishOrderNum());
        }
        wayBillDetail.setConsignorAddress(SystemsUtil.buildAddress(waybillInfoDTO.getDepartureProvinceValue(), waybillInfoDTO.getDepartureCityValue(), waybillInfoDTO.getDepartureCountyValue()));
        wayBillDetail.setStartTime(SystemsUtil.cargoOrderStartTime(waybillInfoDTO.getNeedStartTime()));
        wayBillDetail.setConsignorName(waybillInfoDTO.getDepartureContact());
        wayBillDetail.setConsignorMobilePhone(waybillInfoDTO.getDepartureMobile());
        wayBillDetail.setConsignorLongitude("");
        wayBillDetail.setConsignorLatitude("");
        wayBillDetail.setConsigneeAddress(SystemsUtil.buildAddress(waybillInfoDTO.getReceiveProvinceValue(), waybillInfoDTO.getReceiveCityValue(), waybillInfoDTO.getReceiveCountyValue()));
        wayBillDetail.setEndTime(SystemsUtil.cargoOrderEndTime(waybillInfoDTO.getNeedEndTime()));
        wayBillDetail.setConsigneeName(waybillInfoDTO.getReceiveContact());
        wayBillDetail.setConsigneeMobilePhone(waybillInfoDTO.getReceiveMobile());
        wayBillDetail.setConsigneeLongitude("");
        wayBillDetail.setConsigneeLatitude("");
        wayBillDetail.setTotalCollectionPayment(waybillInfoDTO.getTotalCollectionPayment().doubleValue());
        wayBillDetail.setFromCarrierCount(waybillDetailDTO.getCarrierAssemDTOList().size());
        wayBillDetail.setCarrierAmountNum(SystemsUtil.buildQuantity(waybillInfoDTO.getTotalQuantity()));
        wayBillDetail.setCarrierAmountWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(waybillInfoDTO.getTotalWeight())));
        wayBillDetail.setCarrierAmountVolume(SystemsUtil.buildVolumeUnit(waybillInfoDTO.getTotalCubage()));
        wayBillDetail.setMark(waybillInfoDTO.getRemark());
        wayBillDetail.setSiteCode(waybillInfoDTO.getSiteCode());
        wayBillDetail.setIsComment(waybillInfoDTO.getCreaterAssessIdent() ? 1 : 0);

        //判断运单状态决定如何放置报价字段
        //运单已锁定，直接用最终报价
        if (waybillInfoDTO.getWaybillLock() == 1) {
            wayBillDetail.setFinalPayment(waybillInfoDTO.getTotalFare().doubleValue());
            wayBillDetail.setFinalAdvancePayment(waybillInfoDTO.getPrepayFare().doubleValue());
        }
        //运单待接单，用货主报价和司机报价
        if (waybillInfoDTO.getState() == 1) {
            if (waybillInfoDTO.getTransportTotalFare() != null) {
                wayBillDetail.setTransportQuote(waybillInfoDTO.getTransportTotalFare().doubleValue());
            }
            if (waybillInfoDTO.getTransportPrepayFare() != null) {
                wayBillDetail.setTransportQuotePrepay(waybillInfoDTO.getTransportPrepayFare().doubleValue());
            }
            wayBillDetail.setOrderQuotedAmount(waybillInfoDTO.getTotalFare().doubleValue());
            wayBillDetail.setOrderQuotedPrepay(waybillInfoDTO.getPrepayFare().doubleValue());
        } else {
            //其他情况都直接用最终报价，包括订单取消
            wayBillDetail.setFinalPayment(waybillInfoDTO.getTotalFare().doubleValue());
            wayBillDetail.setFinalAdvancePayment(waybillInfoDTO.getPrepayFare().doubleValue());
        }
        wayBillDetail.setTransportFareState(Integer.valueOf(waybillInfoDTO.getTransportFareState()));
        wayBillDetail.setCarrierDetail(carrierListConvert(waybillDetailDTO.getCarrierAssemDTOList()));
        return wayBillDetail;
    }


    /**
     * 运单详情转换(saas)
     *
     * @param waybillDetailsDTO  运单信息
     * @param accountUserDetails2DTO  saas用户信息
     * @param userItemStatDTO   云配用户业务信息
     * @param driverUserInfoDTO 快到司机信息
     * @param driverItemStatDTO 快到司机业务信息
     * @param imgAddress        快到司机照片信息
     * @return
     */
    public static WayBillDetail wayBillDetailsConvert(WaybillDetailsDTO waybillDetailsDTO, AccountUserDetails2DTO accountUserDetails2DTO, com.cy.saas.business.model.dto.UserItemStatDTO userItemStatDTO, DriverUserInfoDTO driverUserInfoDTO, DriverItemStatDTO driverItemStatDTO, String imgAddress) {
        WayBillDetail wayBillDetail = new WayBillDetail();
        WaybillInfo waybillInfo = waybillDetailsDTO.getWaybillInfo();
        wayBillDetail.setOrderId(String.valueOf(waybillInfo.getId()));
        wayBillDetail.setWaybillNum(waybillInfo.getWaybillNum());
        wayBillDetail.setOrderStatusName(SystemsUtil.findWaybillAppOrderValue(waybillInfo.getState(),
                waybillInfo.getWaybillLock(),
                waybillInfo.getCancelSource(), 2, waybillInfo.getTurnedState()));
        wayBillDetail.setOrderStatusCode(SystemsUtil.findWayBillAppOrderCode(waybillInfo.getState(),
                waybillInfo.getWaybillLock(),
                waybillInfo.getCancelSource(), 2, waybillInfo.getTurnedState()));
        wayBillDetail.setOrderLock(Integer.valueOf(waybillInfo.getWaybillLock()));
        wayBillDetail.setCancleSource(Integer.valueOf(waybillInfo.getCancelSource()));
        wayBillDetail.setCancleCause(waybillInfo.getCancelCause());
        wayBillDetail.setTurnedState(Integer.valueOf(waybillInfo.getTurnedState()));
        wayBillDetail.setCreateTime(DateUtil.dateFormat(waybillInfo.getCreateTime(), DateUtil.F_DATETIME));
        wayBillDetail.setCompanyName(waybillInfo.getCompanyName());
        wayBillDetail.setCreateUserId(waybillInfo.getCreateUserId());
        wayBillDetail.setCreateUserType(Integer.valueOf(waybillInfo.getCreateUserType()));
        wayBillDetail.setOwnerName(waybillInfo.getCreateUserName());
        wayBillDetail.setPhotosAddress(waybillInfo.getCreateHeadImgUrl());
        //创建人信息
        if(accountUserDetails2DTO != null) {
            if(accountUserDetails2DTO.getAccountType().intValue() == 0) {
                wayBillDetail.setOrderPhone(accountUserDetails2DTO.getAccountUser().getLoginCode());
            }else if(accountUserDetails2DTO.getAccountType().intValue() == 1) {
                wayBillDetail.setOrderPhone(accountUserDetails2DTO.getAccountUserChild().getLoginCode());
            }
        }
        if (driverUserInfoDTO != null) {
            wayBillDetail.setOrderPhone(driverUserInfoDTO.getCode());
        }
        //创建人业务信息
        if (userItemStatDTO != null) {
            wayBillDetail.setTransactionNumber(userItemStatDTO.getFinishOrderNum());
        }
        if (driverItemStatDTO != null) {
            wayBillDetail.setTransactionNumber(driverItemStatDTO.getFinishOrderNum());
        }
        wayBillDetail.setConsignorAddress(SystemsUtil.buildAddress(waybillInfo.getDepartureProvinceValue(), waybillInfo.getDepartureCityValue(), waybillInfo.getDepartureCountyValue()));
        wayBillDetail.setStartTime(SystemsUtil.cargoOrderStartTime(waybillInfo.getNeedStartTime()));
        wayBillDetail.setConsignorName(waybillInfo.getDepartureContact());
        wayBillDetail.setConsignorMobilePhone(waybillInfo.getDepartureMobile());
        wayBillDetail.setConsignorLongitude("");
        wayBillDetail.setConsignorLatitude("");
        wayBillDetail.setConsigneeAddress(SystemsUtil.buildAddress(waybillInfo.getReceiveProvinceValue(), waybillInfo.getReceiveCityValue(), waybillInfo.getReceiveCountyValue()));
        wayBillDetail.setEndTime(SystemsUtil.cargoOrderEndTime(waybillInfo.getNeedEndTime()));
        wayBillDetail.setConsigneeName(waybillInfo.getReceiveContact());
        wayBillDetail.setConsigneeMobilePhone(waybillInfo.getReceiveMobile());
        wayBillDetail.setConsigneeLongitude("");
        wayBillDetail.setConsigneeLatitude("");
        wayBillDetail.setTotalCollectionPayment(waybillInfo.getTotalCollectionPayment().doubleValue());
        if(waybillDetailsDTO.getCarrierTradeDTOList() != null) {
            wayBillDetail.setFromCarrierCount(waybillDetailsDTO.getCarrierTradeDTOList().size());
            wayBillDetail.setCarrierDetail(carrierListsConvert(waybillDetailsDTO.getCarrierTradeDTOList()));
        }else {
            wayBillDetail.setFromCarrierCount(0);
        }
        wayBillDetail.setCarrierAmountNum(SystemsUtil.buildQuantity(waybillInfo.getTotalQuantity()));
        wayBillDetail.setCarrierAmountWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(waybillInfo.getTotalWeight())));
        wayBillDetail.setCarrierAmountVolume(SystemsUtil.buildVolumeUnit(waybillInfo.getTotalCubage()));
        wayBillDetail.setMark(waybillInfo.getRemark());
        wayBillDetail.setSiteCode(waybillInfo.getSiteCode());
        wayBillDetail.setIsComment(waybillInfo.getCreaterAssessIdent() ? 1 : 0);

        //判断运单状态决定如何放置报价字段
        //运单已锁定，直接用最终报价
        if (waybillInfo.getWaybillLock() == 1) {
            wayBillDetail.setFinalPayment(waybillInfo.getTotalFare().doubleValue());
            wayBillDetail.setFinalAdvancePayment(waybillInfo.getPrepayFare().doubleValue());
        }
        //运单待接单，用货主报价和司机报价
        if (waybillInfo.getState() == 1) {
            if (waybillInfo.getTransportTotalFare() != null) {
                wayBillDetail.setTransportQuote(waybillInfo.getTransportTotalFare().doubleValue());
            }
            if (waybillInfo.getTransportPrepayFare() != null) {
                wayBillDetail.setTransportQuotePrepay(waybillInfo.getTransportPrepayFare().doubleValue());
            }
            wayBillDetail.setOrderQuotedAmount(waybillInfo.getTotalFare().doubleValue());
            wayBillDetail.setOrderQuotedPrepay(waybillInfo.getPrepayFare().doubleValue());
        } else {
            //其他情况都直接用最终报价，包括订单取消
            wayBillDetail.setFinalPayment(waybillInfo.getTotalFare().doubleValue());
            wayBillDetail.setFinalAdvancePayment(waybillInfo.getPrepayFare().doubleValue());
        }
        wayBillDetail.setTransportFareState(Integer.valueOf(waybillInfo.getTransportFareState()));
        return wayBillDetail;
    }

    /**
     * 托单列表转换(云配)
     */
    public static List<CarrierDetail> carrierListConvert(List<CarrierAssemDTO> carrierAssemDTOs) {
        List<CarrierDetail> carrierDetails = new ArrayList<>();
        for (CarrierAssemDTO carrierAssemDTO : carrierAssemDTOs) {
            CarrierDetail carrierDetail = new CarrierDetail();
            carrierDetail.setCarrierId(String.valueOf(carrierAssemDTO.getInfoDTO().getId()));
            carrierDetail.setCarrierNum(carrierAssemDTO.getInfoDTO().getCarrierNum());
            carrierDetail.setConsignorAddress(SystemsUtil.buildAddress(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue(),
                    carrierAssemDTO.getAddressDTO().getDepartureCityValue(), carrierAssemDTO.getAddressDTO().getDepartureCountyValue()));
            carrierDetail.setConsigneeAddress(SystemsUtil.buildAddress(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue(),
                    carrierAssemDTO.getAddressDTO().getReceiveCityValue(), carrierAssemDTO.getAddressDTO().getReceiveCountyValue()));
            carrierDetail.setConsignorLongitude("");
            carrierDetail.setConsignorLatitude("");
            carrierDetail.setConsigneeLongitude("");
            carrierDetail.setConsigneeLatitude("");
            carrierDetail.setAmountNum(SystemsUtil.buildQuantity(carrierAssemDTO.getInfoDTO().getTotalQuantity()));
            carrierDetail.setAmountWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(carrierAssemDTO.getInfoDTO().getTotalWeight())));
            carrierDetail.setAmountVolume(SystemsUtil.buildVolumeUnit(carrierAssemDTO.getInfoDTO().getTotalCubage()));
            carrierDetail.setState(carrierAssemDTO.getInfoDTO().getState());
            carrierDetail.setCargoDetail(carrierDetailConvert(carrierAssemDTO.getDetailDTOList()));
            carrierDetails.add(carrierDetail);
        }
        return carrierDetails;
    }

    /**
     * 托单列表转换(saas)
     */
    public static List<CarrierDetail> carrierListsConvert(List<CarrierTradeDTO> carrierTradeDTOList) {
        List<CarrierDetail> carrierDetails = new ArrayList<>();
        for (CarrierTradeDTO carrierTradeDTO : carrierTradeDTOList) {
            CarrierDetail carrierDetail = new CarrierDetail();
            carrierDetail.setCarrierId(String.valueOf(carrierTradeDTO.getCarrierInfo().getId()));
            carrierDetail.setCarrierNum(carrierTradeDTO.getCarrierInfo().getCarrierNum());
            carrierDetail.setConsignorAddress(SystemsUtil.buildAddress(carrierTradeDTO.getCarrierAddress().getDepartureProvinceValue(),
                    carrierTradeDTO.getCarrierAddress().getDepartureCityValue(), carrierTradeDTO.getCarrierAddress().getDepartureCountyValue()));
            carrierDetail.setConsigneeAddress(SystemsUtil.buildAddress(carrierTradeDTO.getCarrierAddress().getReceiveProvinceValue(),
                    carrierTradeDTO.getCarrierAddress().getReceiveCityValue(), carrierTradeDTO.getCarrierAddress().getReceiveCountyValue()));
            carrierDetail.setConsignorLongitude("");
            carrierDetail.setConsignorLatitude("");
            carrierDetail.setConsigneeLongitude("");
            carrierDetail.setConsigneeLatitude("");
            carrierDetail.setAmountNum(SystemsUtil.buildQuantity(carrierTradeDTO.getCarrierInfo().getTotalQuantity()));
            carrierDetail.setAmountWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(carrierTradeDTO.getCarrierInfo().getTotalWeight())));
            carrierDetail.setAmountVolume(SystemsUtil.buildVolumeUnit(carrierTradeDTO.getCarrierInfo().getTotalCubage()));
            carrierDetail.setState(carrierTradeDTO.getCarrierInfo().getState());
            carrierDetail.setCargoDetail(carrierDetailsConvert(carrierTradeDTO.getCarrierItemDTOList()));
            carrierDetails.add(carrierDetail);
        }
        return carrierDetails;
    }


    /**
     * 托单详情转换(云配)
     */
    public static CarrierDetail carrierDetailConvert2(CarrierAssemDTO carrierAssemDTO) {
        CarrierDetail carrierDetail = new CarrierDetail();
        carrierDetail.setCarrierId(String.valueOf(carrierAssemDTO.getInfoDTO().getId()));
        carrierDetail.setCarrierNum(carrierAssemDTO.getInfoDTO().getCarrierNum());
        carrierDetail.setConsignorAddress(SystemsUtil.buildAddress(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue(),
                carrierAssemDTO.getAddressDTO().getDepartureCityValue(), carrierAssemDTO.getAddressDTO().getDepartureCountyValue(),
                carrierAssemDTO.getAddressDTO().getDepartureAddress()));
        carrierDetail.setConsigneeAddress(SystemsUtil.buildAddress(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue(),
                carrierAssemDTO.getAddressDTO().getReceiveCityValue(), carrierAssemDTO.getAddressDTO().getReceiveCountyValue(),
                carrierAssemDTO.getAddressDTO().getReceiveAddress()));
        carrierDetail.setConsignorLongitude("");
        carrierDetail.setConsignorLatitude("");
        carrierDetail.setConsigneeLongitude("");
        carrierDetail.setConsigneeLatitude("");
        carrierDetail.setAmountNum(SystemsUtil.buildQuantity(carrierAssemDTO.getInfoDTO().getTotalQuantity()));
        carrierDetail.setAmountWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(carrierAssemDTO.getInfoDTO().getTotalWeight())));
        carrierDetail.setAmountVolume(SystemsUtil.buildVolumeUnit(carrierAssemDTO.getInfoDTO().getTotalCubage()));
        carrierDetail.setState(carrierAssemDTO.getInfoDTO().getState());
        carrierDetail.setCompanyName(carrierAssemDTO.getInfoDTO().getCompanyName());
        carrierDetail.setOwnerName(carrierAssemDTO.getInfoDTO().getCreateUserName());
        carrierDetail.setCargoName(carrierAssemDTO.getInfoDTO().getCargoName());
        carrierDetail.setConsignorName(carrierAssemDTO.getAddressDTO().getDepartureContact());
        carrierDetail.setConsignorPhone(carrierAssemDTO.getAddressDTO().getDepartureMobile());
        carrierDetail.setConsigneeName(carrierAssemDTO.getAddressDTO().getReceiveContact());
        carrierDetail.setConsigneePhone(carrierAssemDTO.getAddressDTO().getReceiveMobile());
        carrierDetail.setConsigneeTelephone(carrierAssemDTO.getAddressDTO().getReceiveTelephone());
        carrierDetail.setTakeWay(carrierAssemDTO.getInfoDTO().getTakeWay());
        if (carrierAssemDTO.getInfoDTO().getCollectionPayment() != null) {
            carrierDetail.setCollectionPayment(String.valueOf(carrierAssemDTO.getInfoDTO().getCollectionPayment()));
        }
        if (carrierAssemDTO.getInfoDTO().getTotalFare() != null) {
            carrierDetail.setTotalFare(String.valueOf(carrierAssemDTO.getInfoDTO().getTotalFare()));
        }
        if (carrierAssemDTO.getInfoDTO().getPrepayFare() != null) {
            carrierDetail.setPrepayFare(String.valueOf(carrierAssemDTO.getInfoDTO().getPrepayFare()));
        }
        carrierDetail.setRemark(carrierAssemDTO.getInfoDTO().getRemark());
        carrierDetail.setCargoDetail(carrierDetailConvert(carrierAssemDTO.getDetailDTOList()));
        return carrierDetail;
    }

    /**
     * 托单详情转换(saas)
     */
    public static CarrierDetail saCarrierDetailConvert2(com.cy.saas.business.model.dto.CarrierAssemDTO saCarrierAssemDTO) {
        CarrierDetail carrierDetail = new CarrierDetail();
        carrierDetail.setCarrierId(String.valueOf(saCarrierAssemDTO.getInfoDTO().getId()));
        carrierDetail.setCarrierNum(saCarrierAssemDTO.getInfoDTO().getCarrierNum());
        carrierDetail.setConsignorAddress(SystemsUtil.buildAddress(saCarrierAssemDTO.getAddressDTO().getDepartureProvinceValue(),
                saCarrierAssemDTO.getAddressDTO().getDepartureCityValue(), saCarrierAssemDTO.getAddressDTO().getDepartureCountyValue(),
                saCarrierAssemDTO.getAddressDTO().getDepartureAddress()));
        carrierDetail.setConsigneeAddress(SystemsUtil.buildAddress(saCarrierAssemDTO.getAddressDTO().getReceiveProvinceValue(),
                saCarrierAssemDTO.getAddressDTO().getReceiveCityValue(), saCarrierAssemDTO.getAddressDTO().getReceiveCountyValue(),
                saCarrierAssemDTO.getAddressDTO().getReceiveAddress()));
        carrierDetail.setConsignorLongitude("");
        carrierDetail.setConsignorLatitude("");
        carrierDetail.setConsigneeLongitude("");
        carrierDetail.setConsigneeLatitude("");
        carrierDetail.setAmountNum(SystemsUtil.buildQuantity(saCarrierAssemDTO.getInfoDTO().getTotalQuantity()));
        carrierDetail.setAmountWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(saCarrierAssemDTO.getInfoDTO().getTotalWeight())));
        carrierDetail.setAmountVolume(SystemsUtil.buildVolumeUnit(saCarrierAssemDTO.getInfoDTO().getTotalCubage()));
        carrierDetail.setState(saCarrierAssemDTO.getInfoDTO().getState());
        carrierDetail.setCompanyName(saCarrierAssemDTO.getInfoDTO().getCompanyName());
        carrierDetail.setOwnerName(saCarrierAssemDTO.getInfoDTO().getCreateUserName());
        carrierDetail.setCargoName(saCarrierAssemDTO.getInfoDTO().getCargoName());
        carrierDetail.setConsignorName(saCarrierAssemDTO.getAddressDTO().getDepartureContact());
        carrierDetail.setConsignorPhone(saCarrierAssemDTO.getAddressDTO().getDepartureMobile());
        carrierDetail.setConsigneeName(saCarrierAssemDTO.getAddressDTO().getReceiveContact());
        carrierDetail.setConsigneePhone(saCarrierAssemDTO.getAddressDTO().getReceiveMobile());
        carrierDetail.setConsigneeTelephone(saCarrierAssemDTO.getAddressDTO().getReceiveTelephone());
        carrierDetail.setTakeWay(saCarrierAssemDTO.getInfoDTO().getTakeWay());
        if (saCarrierAssemDTO.getInfoDTO().getCollectionPayment() != null) {
            carrierDetail.setCollectionPayment(String.valueOf(saCarrierAssemDTO.getInfoDTO().getCollectionPayment()));
        }
        if (saCarrierAssemDTO.getInfoDTO().getTotalFare() != null) {
            carrierDetail.setTotalFare(String.valueOf(saCarrierAssemDTO.getInfoDTO().getTotalFare()));
        }
        if (saCarrierAssemDTO.getInfoDTO().getPrepayFare() != null) {
            carrierDetail.setPrepayFare(String.valueOf(saCarrierAssemDTO.getInfoDTO().getPrepayFare()));
        }
        carrierDetail.setRemark(saCarrierAssemDTO.getInfoDTO().getRemark());
        carrierDetail.setCargoDetail(saCarrierDetailConvert(saCarrierAssemDTO.getDetailDTOList()));
        return carrierDetail;
    }

    /**
     * 托单下的货物详情转换(saas)
     */
    public static List<WayBillDetailCargoList> saCarrierDetailConvert(List<com.cy.saas.business.model.dto.CarrierDetailDTO> detailDTOList) {
        List<WayBillDetailCargoList> carrierDetails = new ArrayList<>();
        for (com.cy.saas.business.model.dto.CarrierDetailDTO carrierDetailDTO : detailDTOList) {
            WayBillDetailCargoList wayBillDetailCargoList = new WayBillDetailCargoList();
            wayBillDetailCargoList.setCargoName(carrierDetailDTO.getCargoName());
            wayBillDetailCargoList.setNumber(SystemsUtil.buildQuantity(carrierDetailDTO.getQuantity()));
            wayBillDetailCargoList.setVolume(SystemsUtil.buildVolumeUnit(carrierDetailDTO.getCubage()));
            wayBillDetailCargoList.setWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(carrierDetailDTO.getWeight())));
            wayBillDetailCargoList.setPacking(carrierDetailDTO.getPacking());
            wayBillDetailCargoList.setCargoType(Integer.valueOf(carrierDetailDTO.getCargoType()));
            wayBillDetailCargoList.setPackingStr(waybillPackingConvert(carrierDetailDTO.getPacking()));
            wayBillDetailCargoList.setCargoTypeStr(waybillStateConvert(carrierDetailDTO.getCargoType()));
            carrierDetails.add(wayBillDetailCargoList);
        }
        return carrierDetails;
    }



    /**
     * 托单下的货物详情转换(云配)
     */
    public static List<WayBillDetailCargoList> carrierDetailConvert(List<CarrierDetailDTO> detailDTOList) {
        List<WayBillDetailCargoList> carrierDetails = new ArrayList<>();

        for (CarrierDetailDTO carrierDetailDTO : detailDTOList) {
            WayBillDetailCargoList wayBillDetailCargoList = new WayBillDetailCargoList();
            wayBillDetailCargoList.setCargoName(carrierDetailDTO.getCargoName());
            wayBillDetailCargoList.setNumber(SystemsUtil.buildQuantity(carrierDetailDTO.getQuantity()));
            wayBillDetailCargoList.setVolume(SystemsUtil.buildVolumeUnit(carrierDetailDTO.getCubage()));
            wayBillDetailCargoList.setWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(carrierDetailDTO.getWeight())));
            wayBillDetailCargoList.setPacking(carrierDetailDTO.getPacking());
            wayBillDetailCargoList.setCargoType(Integer.valueOf(carrierDetailDTO.getCargoType()));
            wayBillDetailCargoList.setPackingStr(waybillPackingConvert(carrierDetailDTO.getPacking()));
            wayBillDetailCargoList.setCargoTypeStr(waybillStateConvert(carrierDetailDTO.getCargoType()));
            carrierDetails.add(wayBillDetailCargoList);
        }
        return carrierDetails;
    }


    /**
     * 托单下的货物详情转换(saas)
     */
    public static List<WayBillDetailCargoList> carrierDetailsConvert(List<CarrierItemDTO> detailDTOList) {
        List<WayBillDetailCargoList> carrierDetails = new ArrayList<>();
        for (CarrierItemDTO carrierItemDTO : detailDTOList) {
            WayBillDetailCargoList wayBillDetailCargoList = new WayBillDetailCargoList();
            wayBillDetailCargoList.setCargoName(carrierItemDTO.getCarrierDetail().getCargoName());
            wayBillDetailCargoList.setNumber(SystemsUtil.buildQuantity(carrierItemDTO.getCarrierDetail().getQuantity()));
            wayBillDetailCargoList.setVolume(SystemsUtil.buildVolumeUnit(carrierItemDTO.getCarrierDetail().getCubage()));
            wayBillDetailCargoList.setWeight(SystemsUtil.buildWeightUnit(SystemsUtil.kgToTonne(carrierItemDTO.getCarrierDetail().getWeight())));
            wayBillDetailCargoList.setPacking(carrierItemDTO.getCarrierDetail().getPacking());
            wayBillDetailCargoList.setCargoType(Integer.valueOf(carrierItemDTO.getCarrierDetail().getCargoType()));
            wayBillDetailCargoList.setPackingStr(waybillPackingConvert(carrierItemDTO.getCarrierDetail().getPacking()));
            wayBillDetailCargoList.setCargoTypeStr(waybillStateConvert(carrierItemDTO.getCarrierDetail().getCargoType()));
            carrierDetails.add(wayBillDetailCargoList);
        }
        return carrierDetails;
    }


    /**
     * 托单详情包装名转换   包装（1塑料 2纸 3木（纤维） 4金属 5编织袋 6无 7其他）
     */
    public static String waybillPackingConvert(Integer packing) {
        switch (packing.intValue()) {
            case 1:
                return "塑料";
            case 2:
                return "纸";
            case 3:
                return "木(纤维)";
            case 4:
                return "金属";
            case 5:
                return "编织袋";
            case 6:
                return "无";
            case 7:
                return "其他";
        }
        return null;
    }


    /**
     * 托单详情货物类型名转换   货物类型(1重货 2泡货)
     */
    public static String waybillStateConvert(Byte cargoType) {
        switch (cargoType.intValue()) {
            case 1:
                return "重货";
            case 2:
                return "泡货";
        }
        return null;
    }


    /**
     * 订单/运单状态转换
     */
    public static Integer waybillStateConvert(Integer waybillState, Integer orderIsTurned, Integer orderLock) {
        if (orderIsTurned == 1) {
            return 6;
        }

        if (orderLock == 1) {
            return 8;
        }

        if (waybillState.intValue() == 1)
            return 1;
        else if (waybillState.intValue() == 12)
            return 2;
        else if (waybillState.intValue() == 3)
            return 3;
        else if (waybillState.intValue() == 7)
            return 4;
        else if (waybillState.intValue() == 5)
            return 7;
        else if (waybillState.intValue() == 6)
            return 5;
        return null;
    }

    /**
     * 订单/运单名称转换
     */

    public static String waybillStateNameConvert(Integer waybillState, Integer orderIsTurned, Integer orderLock) {
        if (orderLock == 1) {
            return "已冻结";
        }

        if (orderIsTurned == 1) {
            return "已转单";
        }

        if (waybillState.intValue() == 1)
            return "待接单";
        else if (waybillState.intValue() == 2)
            return "待装货";
        else if (waybillState.intValue() == 3)
            return "待卸货";
        else if (waybillState.intValue() == 4)
            return "已卸货";
        else if (waybillState.intValue() == 5)
            return "交易已取消";
        else if (waybillState.intValue() == 7)
            return "已完成";
        return null;
    }

    /**
     * 订单/运单名称转换
     */

    public static String waybillStateNameConvert2(Integer orderStatusCode) {
        switch (orderStatusCode.intValue()) {
            case -3:
                return "货主提出取消订单";
            case -2:
                return "司机提出取消订单";
            case -1:
                return "订单已取消";
            case 1:
                return "货主已订车";
            case 2:
                return "待发货";
            case 3:
                return "等待装货";
            case 4:
                return "等待卸货";
            case 5:
                return "已卸货";
            case 6:
                return "已完成";
            case 8:
                return "已冻结";
            case 9:
                return "已转单";
            default:
                return null;
        }
    }



    /**
     * 转单运单名称转换
     */

    public static String waybillStateName(String state) {
        if ("1".equals(state))
            return "待接单";
        else if ("12".equals(state))
            return "已接单";
        else if ("6".equals(state))
            return "交易取消";
        else if ("3".equals(state))
            return "已装货";
        else if ("5".equals(state))
            return "交易已取消";
        else if ("7".equals(state))
            return "已卸货";
        else if ("9".equals(state))
            return "司机未安装App";
        else if ("11".equals(state))
            return "拒接接单";
        return null;
    }
}

