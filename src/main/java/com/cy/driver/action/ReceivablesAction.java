package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.ConfigData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.OrderReceivableService;
import com.cy.driver.service.PointFService;
import com.cy.driver.service.QueryOrderService;
import com.cy.order.service.dto.OrderAndCargoDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/11.
 * 司机确认收款处理
 */
//@Scope("prototype")
@Controller("receivablesAction")
@RequestMapping(value = "/safeSSL")
public class ReceivablesAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(QueryOrderAction.class);

    @Resource
    private OrderReceivableService orderReceivableService;
    @Resource
    private PointFService pointFService;
    @Resource
    private ConfigData configData;
    @Resource
    private QueryOrderService queryOrderService;

    /**
     *司机确认收款
     * @param request
     * @param response
     * @param orderId
     * @param needReceiveFair 待收运费
     * @return
     */
    @RequestMapping(value = "/confirmCollection", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CONFIRM_COLLECTION)
    @Log(type = LogEnum.CONFIRM_COLLECTION)
    public Object confirmCollection(HttpServletRequest request, HttpServletResponse response,
                                    Long orderId , String needReceiveFair, String needReceiveOilCard) {
        try{
            if(logger.isDebugEnabled()){
                logger.debug("待收运费 == {},待收油卡费用=={},单号=={}", needReceiveFair, needReceiveOilCard, orderId);
            }
            if(orderId == null || (StringUtils.isEmpty(needReceiveFair) && StringUtils.isEmpty(needReceiveOilCard))){
                return findErrorParam(response);
            }
            BigDecimal fee = null;
            if(needReceiveFair!=null) {
                fee = new BigDecimal(needReceiveFair.replace("元", ""));
            }
            BigDecimal needReceiveOilCard1 = null;
            if(needReceiveOilCard!=null) {
                needReceiveOilCard1 = new BigDecimal(needReceiveOilCard.replace("元", ""));
            }else {
                OrderAndCargoDTO orderAndCargoDTO = queryOrderService.getOrderDetails2(orderId);
                if (orderAndCargoDTO.getOrderInfo().getOilFare() != null) {
                    if (orderAndCargoDTO.getOrderInfo().getPayAllFare().compareTo(BigDecimal.ZERO) == 0) {
                        needReceiveOilCard1 = orderAndCargoDTO.getOrderInfo().getOilFare();
                    }
                }
            }
            String resultStr = orderReceivableService.receivabile(orderId, findUserId(request), fee,needReceiveOilCard1);
            if("0".equals(resultStr)){
                updRespHeadSuccess(response);
                //3.4司机收款积分奖励-3.4版
                Double mValue = configData.getMvalue();
                if(mValue.compareTo(new Double("0")) > 0){
                    String str = "ROUND_HALF_UP("+needReceiveFair+"/"+mValue+")";
                    BigDecimal finalAmount = new BigDecimal(mValue);
                    BigDecimal eventPercent = fee.divide(finalAmount);// 50/100=0.5
                    int amount = eventPercent.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP).intValue();//(finalAmount * 0.5)/1 四舍五入
                    pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_OUTTER, LogEnum.CONFIRM_COLLECTION.getEventCode(),amount,str,convert2InSource(),null);
                }
                return new HashMap();
            }

        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("司机确认收款出错", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20211);
    }
}
