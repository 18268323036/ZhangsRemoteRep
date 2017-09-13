package com.cy.driver.service.impl;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.HttpUtils;
import com.cy.driver.common.util.JsonUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.DriverProtocolBO;
import com.cy.driver.domain.ScdProtocolBO;
import com.cy.driver.domain.TransportProtocolBO;
import com.cy.driver.service.OrderReceivableService;
import com.cy.driver.service.ProtocolService;
import com.cy.order.service.OrderService;
import com.cy.order.service.ReceivablesService;
import com.cy.order.service.dto.ContractSaveDTO;
import com.cy.order.service.dto.DriverReceiveDTO;
import com.cy.order.service.dto.TransactionCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.base.Response;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.WebUserInfoService;
import com.cy.pass.service.dto.CompanyInfoDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.platformpay.service.PayTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/11.
 */
@Service
public class OrderReceivableServiceImpl implements OrderReceivableService {

    private Logger LOG  = LoggerFactory.getLogger(OrderReceivableServiceImpl.class);

    @Resource
    private ReceivablesService receivablesService;

    @Resource
    private OrderService orderService;

    @Resource
    private DriverUserInfoService driverUserInfoService;

    @Resource
    private WebUserInfoService webUserInfoService;

    @Resource
    private PayTradeService payTradeService;

    @Resource
    private HttpUtils httpUtils;

    @Resource
    private ProtocolService protocolService;

    private String transportProtocolUrl;

    @Value("${order.transport.protocol.url}")
    private void setTransportProtocolUrl(String transportProtocolUrl) {
        this.transportProtocolUrl = transportProtocolUrl;
    }

    /**
     *
     * @param orderNo 订单号
     * @param deliveryTime 发货时间
     * @param secondName    乙方
     * @param startAdd  装货地
     * @param endAdd    卸货地
     * @param cargoName 货物名称
     * @param weight    重量
     * @param volume    体积
     * @param carCost   租车费用
     * @param advanceCost   预付款
     * @param driverCard    司机身份证号码
     * @param mobilePhone   手机
     * @param carNumber 车牌号码
     * @param receivedAccount   指定收款帐户
     * @param deputePerson  代开发票委托人
     * @param cashFare  现金费用
     * @param oilFare  油卡费用
     * @param passageFare  通行费
     * @return
     */
    private TransportProtocolBO generatePro(String orderNo, String deliveryTime, String secondName, String startAdd
    , String endAdd, String cargoName, String weight, String volume, String carCost, String advanceCost, String driverCard
    , String mobilePhone , String carNumber, String receivedAccount, String deputePerson, String cashFare, String oilFare, String passageFare) throws  Exception{

            Map paramsMap = new HashMap<String, String>();
            paramsMap.put("orderNo",orderNo );
            paramsMap.put("deliveryTime", deliveryTime);
            paramsMap.put("secondName", secondName);
            paramsMap.put("startAdd", startAdd);
            paramsMap.put("endAdd", endAdd);
            paramsMap.put("cargoName", cargoName);
            paramsMap.put("weight", weight);
            paramsMap.put("volume", volume);
            paramsMap.put("carCost", carCost);
            paramsMap.put("advanceCost", advanceCost);
            paramsMap.put("driverCard", driverCard);
            paramsMap.put("mobilePhone", mobilePhone);
            paramsMap.put("carNumber", carNumber);
            paramsMap.put("receivedAccount", receivedAccount);
            paramsMap.put("deputePerson", deputePerson);
            paramsMap.put("cashFare", cashFare);
            paramsMap.put("oilFare", oilFare);
            paramsMap.put("passageFare", passageFare);
//            String paramsStr = HttpPostUtil.toQueryString(paramsMap);
            String json = httpUtils.doPostRequest(transportProtocolUrl,paramsMap);
             TransportProtocolBO transportProtocolBO = JsonUtil.jsonToBean(json, TransportProtocolBO.class);
        return transportProtocolBO;
    }

    /**
     * 确认收款
     *
     * @param orderId 订单ID
     * @param driverId 司机ID
     *  @param needReceiveFair 待收运费
     * @return
     */
    @Override
    public String receivabile(Long orderId, Long driverId, BigDecimal needReceiveFair ,BigDecimal needReceiveOilFare ){

        //数据库中数据是否存在该用户
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUInfoDTORes = driverUserInfoService.getDriverUserInfo(driverId);
        if(!driverUInfoDTORes.isSuccess() || driverUInfoDTORes.getData() == null){
            return "-1";
        }
        DriverUserInfoDTO driverUserInfoDTO = driverUInfoDTORes.getData();
        Response<TransactionInfoDTO> tranInfoDTORes = orderService.getTransactionInfo(orderId);
        if(tranInfoDTORes.isSuccess()) {
            if (tranInfoDTORes.getData() == null) {
                return "-1";
            }

            TransactionInfoDTO transactionInfoDTO = tranInfoDTORes.getData();

            //查询货主企业信息
            com.cy.pass.service.dto.base.Response<CompanyInfoDTO> response2 = webUserInfoService.getCompanyDetail(transactionInfoDTO.getCompanyId());
            if (!response2.isSuccess() || response2.getData() == null) {
                if (LOG.isErrorEnabled())
                    LOG.error("确定收款失败，查询货主企业信息失败");
                return "-1";
            }

            Long webCapitalAccountId = response2.getData().getCapitalAccountId();//付款方资金帐号ID
            if (webCapitalAccountId == null || webCapitalAccountId.longValue() <= 0) {
                return "-1";
            }

            //判断 订单中的司机id和token中取出的司机id是否不一样
            if (transactionInfoDTO.getDriverId().longValue() != driverId.longValue()) {
                return "-1";
            }
            //判断 订单的司机待收运费金额是否<=0
            if (SystemsUtil.bigAdd(transactionInfoDTO.getNeedReceiveFair(), transactionInfoDTO.getNeedReceiveOilFare()).compareTo(new BigDecimal("0")) != 1) {
                return "-1";
            }

            if (SystemsUtil.bigAdd(transactionInfoDTO.getNeedReceiveFair(), transactionInfoDTO.getNeedReceiveOilFare()).compareTo(SystemsUtil.bigAdd(needReceiveFair, needReceiveOilFare)) == -1) {
                return "-1";
            }
            Response<Long> response = null;

            TransportProtocolBO transportProtocolBO = null;
            //签署运输协议
            if (SystemsUtil.bigAdd(transactionInfoDTO.getRealReceviedFair(),transactionInfoDTO.getRealReceiveOilFare()).compareTo(new BigDecimal("0")) != 1) {
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("待收运费 service== {}", needReceiveFair);
                    }
                    if (transactionInfoDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_COMMON) {
                        Response<TransactionCargoDTO> response1 = orderService.findCargoById(transactionInfoDTO.getTransactionCargoId());
                        if (!response1.isSuccess() || response1.getData() == null) {
                            return "-1";
                        }
                        TransactionCargoDTO transactionCargoDTO = response1.getData();

                        //装货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                        String sAddress = SystemsUtil.buildAddress(transactionCargoDTO.getStartProvince(), transactionCargoDTO.getStartCity(), transactionCargoDTO.getStartCounty());
                        //卸货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                        String eAddress = SystemsUtil.buildAddress(transactionCargoDTO.getEndProvince(), transactionCargoDTO.getEndCity(), transactionCargoDTO.getEndCounty());
                        /** 发货时间=发货时间存在取发货时间，发货时间不存在取装货时间 */
                        String deliveryTimeStr = "";
                        if (transactionInfoDTO.getDeliveryTime() != null) {
                            deliveryTimeStr = DateUtil.dateToStr(transactionInfoDTO.getDeliveryTime());
                        } else {
                            deliveryTimeStr = DateUtil.dateToStr(transactionCargoDTO.getRequestStartTime());
                        }
                        transportProtocolBO = generatePro(transactionInfoDTO.getOrderNumber(), deliveryTimeStr,
                                driverUserInfoDTO.getName(), sAddress, eAddress, transactionCargoDTO.getCargoName(),
                                SystemsUtil.subZeroAndDot(transactionCargoDTO.getCargoWeight()), SystemsUtil.subZeroAndDot(transactionCargoDTO.getCargoCubage()), transactionInfoDTO.getRealNeedpayFair().toString(),
                                transactionInfoDTO.getNeedPrepayFair().toString(), driverUserInfoDTO.getIdentityLicenseNum(), driverUserInfoDTO.getCode(), driverUserInfoDTO.getCarNumber(),
                                "", "", SystemsUtil.getFare(transactionInfoDTO.getCashFare()), SystemsUtil.getFare(transactionInfoDTO.getOilFare()), "");
                        if (transportProtocolBO.getCode() != 0) {
                            return "-1";
                        }
                    } else {
                        ScdProtocolBO proto = protocolService.signElectronicProtocol(transactionInfoDTO.getId(), null, null);
                        if (proto == null) {
                            return "-1";
                        }
                        transportProtocolBO = new TransportProtocolBO();
                        transportProtocolBO.setData(proto.getDocUrl());
                    }
                } catch (Exception e) {
                    if (LOG.isErrorEnabled()) LOG.error("生成运输协议出错", e);
                    return "-1";
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("待收运费 == {}", needReceiveFair);
                }
                // 运输协议
                ContractSaveDTO contractSaveDTO = new ContractSaveDTO();
                DriverReceiveDTO driverReceiveDTO = new DriverReceiveDTO();
                driverReceiveDTO.setDriverId(driverId);
                driverReceiveDTO.setCapitalAccountId(driverUserInfoDTO.getCapitalAccountId());
                driverReceiveDTO.setDriverName(driverUserInfoDTO.getName());
                driverReceiveDTO.setReceiveFair(needReceiveFair);
                driverReceiveDTO.setReceiveOilFare(needReceiveOilFare);
                driverReceiveDTO.setTransactionId(transactionInfoDTO.getId());
                contractSaveDTO.setPartyAName(Constants.FIRST_PARTY);
                contractSaveDTO.setPartyBName(driverUserInfoDTO.getName());
                contractSaveDTO.setEffectiveBegin(new Date());
                contractSaveDTO.setDocPath(transportProtocolBO.getData());//
                contractSaveDTO.setContractCode(Constants.PROTOCOL_DRIVER_STR + transactionInfoDTO.getOrderNumber());
                response = receivablesService.firstSignContract(driverReceiveDTO, contractSaveDTO);
            } else//不签署运输协议
            {
                DriverReceiveDTO driverReceiveDTO = new DriverReceiveDTO();
                driverReceiveDTO.setDriverId(driverId);
                driverReceiveDTO.setCapitalAccountId(driverUserInfoDTO.getCapitalAccountId());
                driverReceiveDTO.setReceiveFair(needReceiveFair);
                driverReceiveDTO.setReceiveOilFare(needReceiveOilFare);
                driverReceiveDTO.setTransactionId(transactionInfoDTO.getId());
                driverReceiveDTO.setDriverName(driverUserInfoDTO.getName());
                response = receivablesService.receiVablesNotSign(driverReceiveDTO);
            }
            if (response == null || response.getData() == null) {
                return "-1";
            }


            Long businessEventId = response.getData();//司机确认收款记录ID
            //如果收款id返回0说明只收油卡不收现金，那么直接返回收款成功
            if(businessEventId.intValue()==0){
                return "0";
            }
            if (LOG.isDebugEnabled())
                LOG.debug("{}司确认收款，金额={}", driverId, needReceiveFair);

            if (needReceiveFair.compareTo(BigDecimal.ZERO) == 1) {
                //调用dubbo-pay的司机确认收款接口，成功返回平台指令id  2001司机收到运费
                com.cy.platformpay.service.dto.base.Response<Long> response3 = payTradeService.receptFee(2001, businessEventId, driverUserInfoDTO.getCapitalAccountId(), webCapitalAccountId, needReceiveFair);
                if (!response3.isSuccess() || response3.getData() == null) {

                    LOG.error("{}司确认收款失败，支付平台反返回失败,失败信息={}", driverId, response3.getMessage());
                    return "-1";
                }
                Long platformCommandID = response3.getData();//指令ID
                Response response4 = receivablesService.updateState(businessEventId, platformCommandID);
                if (response4.isSuccess()) {
                    return "0";
                }
            }else{
                if(businessEventId!=null){
                    return "0";
                }
            }
        }
        return "-1";
    }

    /**
     * 生成运输协议
     *
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @return -1 成功
     */
    @Override
    public DriverProtocolBO transprotPro(Long orderId, Long driverId) {
        DriverProtocolBO driverProtocolBO = new DriverProtocolBO();
        try{
            //数据库中数据是否存在该用户
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUInfoDTORes = driverUserInfoService.getDriverUserInfo(driverId);
            if (!driverUInfoDTORes.isSuccess() || driverUInfoDTORes.getData() == null) {
                return driverProtocolBO;
            }
            DriverUserInfoDTO driverUserInfoDTO = driverUInfoDTORes.getData();
            Response<TransactionInfoDTO> tranInfoDTORes = orderService.getTransactionInfo(orderId);
            if (tranInfoDTORes.isSuccess()) {
                if (tranInfoDTORes.getData() == null) {
                    return driverProtocolBO;
                }
                TransactionInfoDTO transactionInfoDTO = tranInfoDTORes.getData();
                //判断 订单中的司机id和token中取出的司机id是否不一样
                if (transactionInfoDTO.getDriverId().longValue() != driverId.longValue()) {
                    return driverProtocolBO;
                }
                Response<Long> response = null;
                Response<TransactionCargoDTO> response1 = orderService.findCargoById(transactionInfoDTO.getTransactionCargoId());
                if (!response1.isSuccess() || response1.getData() == null) {
                    return driverProtocolBO;
                }
                TransactionCargoDTO transactionCargoDTO = response1.getData();
                //装货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                String sAddress = SystemsUtil.buildAddress(transactionCargoDTO.getStartTown(), transactionCargoDTO.getStartProvince(), transactionCargoDTO.getStartCity(), transactionCargoDTO.getStartCounty());
                //卸货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                String eAddress = SystemsUtil.buildAddress(transactionCargoDTO.getEndTown(), transactionCargoDTO.getEndProvince(), transactionCargoDTO.getEndCity(), transactionCargoDTO.getEndCounty());

                driverProtocolBO.setOrderNo(transactionInfoDTO.getOrderNumber());
                /** 发货时间=发货时间存在取发货时间，发货时间不存在取装货时间 */
                if (transactionInfoDTO.getDeliveryTime() != null) {
                    driverProtocolBO.setDeliveryTime(DateUtil.dateToStr(transactionInfoDTO.getDeliveryTime()));
                } else {
                    if (transactionCargoDTO != null) {
                        driverProtocolBO.setDeliveryTime(DateUtil.dateToStr(transactionCargoDTO.getRequestStartTime()));
                    }
                }
                driverProtocolBO.setSecondName(driverUserInfoDTO.getName());
                driverProtocolBO.setStartAdd(sAddress);
                driverProtocolBO.setEndAdd(eAddress);
                driverProtocolBO.setCargoName(transactionCargoDTO.getCargoName());
                driverProtocolBO.setWeight(SystemsUtil.buildCargoWeightIncludeUnit(transactionCargoDTO.getCargoWeight()));
                driverProtocolBO.setVolume(SystemsUtil.buildCargoCubageIncludeUnit(transactionCargoDTO.getCargoCubage()));
                driverProtocolBO.setCarCost(transactionInfoDTO.getRealNeedpayFair().toString());//租车费用 总运费
                driverProtocolBO.setAdvanceCost(transactionInfoDTO.getNeedPrepayFair().toString());//预付款
                driverProtocolBO.setDriverCard(driverUserInfoDTO.getIdentityLicenseNum());
                driverProtocolBO.setMobilePhone(driverUserInfoDTO.getCode());
                driverProtocolBO.setCarNumber(driverUserInfoDTO.getCarNumber());
                driverProtocolBO.setCash(SystemsUtil.getFare(transactionInfoDTO.getCashFare()));
                driverProtocolBO.setOilCard(SystemsUtil.getFare(transactionInfoDTO.getOilFare()));
                driverProtocolBO.setReceivedAccount("");
                driverProtocolBO.setDeputePerson("");
            }
        }catch (Exception e){
            LOG.error("运输协议出错");
            return driverProtocolBO;
        }
        return driverProtocolBO;
    }

}
