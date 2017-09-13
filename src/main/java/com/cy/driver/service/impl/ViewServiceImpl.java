package com.cy.driver.service.impl;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.ScdProtocolBO;
import com.cy.driver.service.*;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.TransactionCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.pass.service.dto.CompanyInfoDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.platformpay.service.dto.AccountBankDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wyh on 2016/1/14.
 */
@Service("viewService")
public class ViewServiceImpl implements ViewService {
    private static final Logger LOG = LoggerFactory.getLogger(ViewServiceImpl.class);
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private BankCardHandlerService bankCardHandlerService;

    /**
     * 司机承运签订协议转换
     */
    @Override
    public ScdProtocolBO converterScdProtocol(Long orderId, OrderAndCargoDTO orderAndCargoDTO, DistributeInfoDTO distributeDTO){
        if (orderId != null) {
            orderAndCargoDTO = queryOrderService.getOrderDetails(orderId);
        }
        if (orderAndCargoDTO == null) {
            return null;
        }
        TransactionInfoDTO orderDTO = orderAndCargoDTO.getOrderInfo();
        TransactionCargoDTO cargoDTO = orderAndCargoDTO.getCargoInfo();
        if (orderDTO.getDistributeId() == null) {
            LOG.error("司机承运签订协议转换失败，派单id为空，orderId={}", orderId);
            return null;
        }
        if (orderId != null) {
            distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
        }
        if (distributeDTO == null) {
            return null;
        }
        CompanyInfoDTO subConCompany = webUserHandleService.getCompanyInfo(distributeDTO.getSubcontractorCompanyId());
        if (subConCompany == null) {
            return null;
        }
        DriverUserInfoDTO driverInfo = driverUserHandlerService.getDriverInfo(orderDTO.getDriverId());
        if (driverInfo == null) {
            return null;
        }
        ScdProtocolBO bo = new ScdProtocolBO();
        bo.setTotalFareAllotModel(distributeDTO.getTotalFareAllotModel());
        if (distributeDTO.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_ALL) {
            bo.setTotalFare(BigDecimal.ZERO);
            bo.setPrepayFare(BigDecimal.ZERO);
            bo.setOilCard(SystemsUtil.getFare(BigDecimal.ZERO));
            bo.setCash(SystemsUtil.getFare(BigDecimal.ZERO));
        } else if(distributeDTO.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_SUBONLYCASH){
            bo.setTotalFare(orderDTO.getRealNeedpayFair());
            bo.setPrepayFare(orderDTO.getNeedPrepayFair());
            bo.setOilCard(SystemsUtil.getFare(orderDTO.getOilFare()));
            bo.setCash(SystemsUtil.getFare(orderDTO.getCashFare()));
        }else{
            bo.setTotalFare(orderDTO.getRealNeedpayFair());
            bo.setPrepayFare(orderDTO.getNeedPrepayFair());
            bo.setOilCard(SystemsUtil.getFare(BigDecimal.ZERO));
            bo.setCash(SystemsUtil.getFare(orderDTO.getCashFare()));
        }
        bo.setOrderNumber(orderDTO.getOrderNumber());
        /** 发货时间=发货时间存在取发货时间，发货时间不存在取装货时间 */
        if (orderDTO.getDeliveryTime() != null) {
            bo.setDeliveryTime(DateUtil.dateToStr(orderDTO.getDeliveryTime()));
        } else {
            if (cargoDTO != null) {
                bo.setDeliveryTime(DateUtil.dateToStr(cargoDTO.getRequestStartTime()));
            }
        }
        bo.setFirstName(subConCompany.getCompanyName());
        bo.setCardNumber(subConCompany.getIdCardNumber());
        bo.setSecondName(driverInfo.getName());

        if (cargoDTO != null) {
            bo.setStartAddress(SystemsUtil.buildAddress(cargoDTO.getStartProvince(), cargoDTO.getStartCity(), cargoDTO.getStartCounty()));
            bo.setEndAddress(SystemsUtil.buildAddress(cargoDTO.getEndProvince(), cargoDTO.getEndCity(), cargoDTO.getEndCounty()));
            bo.setCargoName(cargoDTO.getCargoName());
            bo.setCargoWeight(SystemsUtil.buildCargoWeightIncludeUnit(cargoDTO.getCargoWeight()));
            bo.setCargoCubage(SystemsUtil.buildCargoCubageIncludeUnit(cargoDTO.getCargoCubage()));
        }
        bo.setDriverCardNumber(driverInfo.getIdentityLicenseNum());
        bo.setDriverMobilePhone(driverInfo.getCode());
        bo.setCarNumber(driverInfo.getCarNumber());
        if (driverInfo.getCapitalAccountId() != null) {
            List<AccountBankDTO> cardList = bankCardHandlerService.listByAccountId(driverInfo.getCapitalAccountId());
            if (cardList != null && cardList.size() > 0 ) {
                AccountBankDTO accountBankDTO = cardList.get(cardList.size() - 1);
                bo.setPaymentAccount(accountBankDTO.getDecodeAccountCode());
            }
        }
        return bo;
    }
}
