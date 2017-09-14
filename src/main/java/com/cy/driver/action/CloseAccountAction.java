package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.CloseAcountService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.QueryOrderService;
import com.cy.platformpay.service.CapitalAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yanst on 2015/12/14.
 * 注销账户
 */
@Scope("prototype")
@RestController("closeAccountAction")
public class CloseAccountAction extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(CloseAccountAction.class);

    @Resource
    private CapitalAccountService capitalAccountService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private CloseAcountService closeAcountService;

    /**
     * 注销账户
     * @param request
     * @param response
     * @param mobilephone
     * @param authCode
     * @param reason
     * @return
     */
    @RequestMapping(value = "/closeAccount", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOSE_ACCOUNT)
    @Log(type = LogEnum.CLOSE_ACCOUNT)
    public Object closeAccount(HttpServletRequest request, HttpServletResponse response, String mobilephone, String authCode, String reason) {
        try{
            updRespHeadSuccess(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20234);
//            if (StringUtils.isEmpty(mobilephone) || StringUtils.isEmpty(authCode) ) {
//                if (LOG.isErrorEnabled())
//                    LOG.error("注销账户：参数不能为空。");
//                updRespHeadError(response);
//                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
//            }
//
//            //验证
//            Response<Boolean> result = driverUserHandlerService.validAuthCode(mobilephone, authCode, AuthCodeType.APPLY_LOG_OUT.getValue());
//            if (!result.isSuccess()) {
//                updRespHeadError(response);
//                //参数不完整
//                if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
//                    if (LOG.isErrorEnabled()) LOG.error("验证 验证码（服务端）校验：参数不完整.");
//                    updRespHeadError(response);
//                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
//                }
//                //验证码失效
//                if (result.getCode() == CodeTable.LI_VALID_TIMES.getCode()) {
//                    if (LOG.isErrorEnabled()) LOG.error("验证 验证码（服务端）校验：验证码失效.");
//                    updRespHeadError(response);
//                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
//                }
//                //号码未注册
//                if (result.getCode() == CodeTable.INVALID_CODE.getCode()) {
//                    if (LOG.isErrorEnabled()) LOG.error("验证 验证码（服务端）校验：账户未注册.");
//                    updRespHeadError(response);
//                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20107);
//                }
//                updRespHeadError(response);
//                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
//            }
//
//            // 验证订单
//            com.cy.order.service.dto.base.Response<Boolean> result2 =  queryOrderService.validateDriverOrder(findUserId(request));
//            if(result2.isSuccess() && result2.getData() != null){
//                //判断资金账户是否有钱
//                com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO> result1 =  capitalAccountService.getAccount(findcapitalAccountId(request));
//                if (!result1.isSuccess() || result1.getData() == null){
//                    if (LOG.isErrorEnabled())
//                        LOG.error("调用 platformpay判断资金账户是否有钱出现异常或者返回数据为null。{}", result1.getMessage());
//                    updRespHeadError(response);
//                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20223);
//                }else{
//                    if (result1.getData().getTotalMoney().compareTo(new BigDecimal("0")) != 0) {
//                        if (LOG.isErrorEnabled())
//                            LOG.error("您的账户存在余额或者欠款不能注销");
//                        updRespHeadError(response);
//                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20223);
//                    }
//                    LogOffApplyDTO logOffApplyDTO = new LogOffApplyDTO();
//                    logOffApplyDTO.setUserKind(SysPushCounterEnum.UserKind.DRIVER_USER.getCode());
//                    logOffApplyDTO.setUserId(findUserId(request));
//                    logOffApplyDTO.setReason(reason);
//                    Response<Long> result3 = closeAcountService.saveApply(logOffApplyDTO);
//                    if(result3.isSuccess() && result3.getData() != null){
//                        updRespHeadSuccess(response);
//                        return null;
//                    }
//                }
//            }else if(result2.getCode() == com.cy.order.service.dto.base.CodeTable.ORDER_UNFINISH.getCode()
//                    ||result2.getCode() == com.cy.order.service.dto.base.CodeTable.ORDER_PAY_UNFINISH.getCode()){
//                if (LOG.isErrorEnabled())
//                    LOG.error("您的账户存在未完成的订单不能注销,result2={}", JSON.toJSONString(result2));
//                updRespHeadError(response);
//                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20224);
//            }
        }catch (Exception e){
            LOG.error("账户注销失败", e);
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20225);
    }
}
