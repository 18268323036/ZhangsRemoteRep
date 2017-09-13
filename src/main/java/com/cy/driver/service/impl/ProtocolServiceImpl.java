package com.cy.driver.service.impl;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.initdata.ConfigData;
import com.cy.driver.common.util.HttpUtils;
import com.cy.driver.common.util.JsonUtil;
import com.cy.driver.domain.ScdProtocolBO;
import com.cy.driver.domain.TransportProtocolBO;
import com.cy.driver.service.ProtocolService;
import com.cy.driver.service.ViewService;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyh on 2016/1/14.
 */
@Service("protocolService")
public class ProtocolServiceImpl implements ProtocolService {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolServiceImpl.class);

    @Resource
    private ViewService viewService;
    @Resource
    private ConfigData configData;

    /**
     * 司机生成分包订单协议
     */
    @Override
    public ScdProtocolBO signElectronicProtocol(Long orderId, OrderAndCargoDTO orderAndCargoDTO, DistributeInfoDTO distributeDTO){
        try{
            ScdProtocolBO proto = viewService.converterScdProtocol(orderId, orderAndCargoDTO, distributeDTO);
            Map paramsMap = new HashMap<String, String>();
            /** 订单号 */
            paramsMap.put("orderNo", proto.getOrderNumber());
            /** 发货时间 */
            paramsMap.put("deliveryTime", proto.getDeliveryTime());
            /** 甲方 */
            paramsMap.put("firstName", proto.getFirstName());
            /** 分包商身份证号 */
            paramsMap.put("subConCardNo", proto.getCardNumber());
            /** 乙方 */
            paramsMap.put("secondName", proto.getSecondName());
            /** 装货地 */
            paramsMap.put("startAdd", proto.getStartAddress());
            /** 卸货地 */
            paramsMap.put("endAdd", proto.getEndAddress());
            /** 货物名称 */
            paramsMap.put("cargoName", proto.getCargoName());
            /** 重量 */
            paramsMap.put("weight", proto.getCargoWeight() == null ? "" : proto.getCargoWeight().toString());
            /** 体积 */
            paramsMap.put("volume", proto.getCargoCubage() == null ? "" : proto.getCargoCubage().toString());
            /** 租车费用 */
            paramsMap.put("carCost", proto.getTotalFare().toString());
            /** 预付款 */
            paramsMap.put("advanceCost", proto.getPrepayFare().toString());
            /** 司机身份证号 */
            paramsMap.put("driverCardNo", proto.getDriverCardNumber());
            /** 司机手机 */
            paramsMap.put("driverMobile", proto.getDriverMobilePhone());
            /** 车牌号码 */
            paramsMap.put("carNumber", proto.getCarNumber());
            /** 指定收款账户号 */
            paramsMap.put("acceptFeeAccount", proto.getPaymentAccount());
            /** 代开发票委托人 */
            paramsMap.put("deputePerson", proto.getEntrustPerson() == null ? "" : proto.getEntrustPerson());
            /** 1:按订单支付租车费用 2:按甲乙双方约定另行支付费用 */
            if (proto.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_ALL) {
                paramsMap.put("payType", "2");
            } else {
                paramsMap.put("payType", "1");
            }
            /** 司机收取现金 */
            paramsMap.put("cashFare",proto.getCash());
            /** 司机收取油卡 */
            paramsMap.put("oilFare",proto.getOilCard());
            /** 司机收取通行费 */
            paramsMap.put("passageFare","");
            String json = HttpUtils.doPostRequest(configData.getDistributeProtocolUrl(), paramsMap);
            if (StringUtils.isBlank(json)) {
                LOG.error("司机生成分包订单协议失败,返回json为空");
                return null;
            }
            TransportProtocolBO transportProtocolBO = JsonUtil.jsonToBean(json, TransportProtocolBO.class);
            if (transportProtocolBO.getCode() != 0) {
                LOG.error("司机生成分包订单协议失败,返回json为空");
                return null;
            }
            proto.setDocUrl(transportProtocolBO.getData());
            return proto;
        }
        catch (Exception e){
            LOG.error("司机生成分包订单协议出现异常", e);
        }
        return null;
    }
}
