package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.saasService.SaasPayUserService;
import com.cy.driver.service.AccountService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.platformpay.service.dto.base.CodeTable;
import com.cy.platformpay.service.dto.base.Response;
import com.cy.saas.pay.model.enums.PayUserNatureEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2015/9/8.
 *提现密码管理
 */
@Scope("prototype")
@Controller("withdrawPasswordHandlerAction")
@RequestMapping(value = "/safeSSL")
public class WithdrawPasswordHandlerAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(WithdrawPasswordHandlerAction.class);

    @Resource
    private AccountService accountService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private SaasPayUserService saasPayUserService;


    /**
     * 设置提现密码
     * @param  withdrawPassword 提现密码
     *
     */
    @RequestMapping(value = "/setWithdrawPassword", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SET_WITHDRAW_PASSWORD)
    @Log(type = LogEnum.SET_WITHDRAW_PASSWORD)
    public Object setWithdrawPassword(HttpServletRequest request, HttpServletResponse response, String withdrawPassword){
        try {
            if(StringUtils.isEmpty(withdrawPassword))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20097);
            }

            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> resultDriverInfo = driverUserHandlerService.getDriverUserInfo(findUserId(request));
            if(!resultDriverInfo.isSuccess() || resultDriverInfo.getData() == null){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }

            //提现密码与登录密码不能一致
            if(withdrawPassword.equalsIgnoreCase(resultDriverInfo.getData().getPassword())){
                logger.error("提现密码与登录密码不能一致");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20220);
            }
            Response<Boolean> resultData = accountService.resetCashPwd(findUserId(request), withdrawPassword.toUpperCase());
            if(resultData.getCode() == CodeTable.ERROR_ACCOUNT_NOT_EXIST.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20099);
            }
            if(resultData.isSuccess()){
                if(saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                    saasPayUserService.resetTradePassword(withdrawPassword,findUserId().toString());
                }
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("设置提现密码出错", e);
            }
        }
        return findException(response);
    }

    /**
     *修改提现密码
     *@param passwordOld	旧密码
     * @param withdrawPassword	提现密码
     *
     */
    @RequestMapping(value = "/updateWithdrawPassword", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_WITHDRAW_PASSWORD)
    @Log(type = LogEnum.UPDATE_WITHDRAW_PASSWORD)
    public Object updateWithdrawPassword(HttpServletRequest request, HttpServletResponse response, String passwordOld, String withdrawPassword){
        try {
            if(StringUtils.isEmpty(passwordOld)||StringUtils.isEmpty(withdrawPassword))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20097);
            }

            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> resultDriverInfo = driverUserHandlerService.getDriverUserInfo(findUserId(request));
            if(!resultDriverInfo.isSuccess() || resultDriverInfo.getData() == null){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }

            //提现密码与登录密码不能一致
            if(withdrawPassword.equalsIgnoreCase(resultDriverInfo.getData().getPassword())){
                logger.error("提现密码与登录密码不能一致");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20220);
            }

            Response<Boolean> resultData = accountService.updateCashPwd(findUserId(request), passwordOld.toUpperCase(), withdrawPassword.toUpperCase());
            if(resultData.isSuccess()){
                if(saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                    saasPayUserService.resetTradePassword(withdrawPassword,findUserId().toString());
                }
                updRespHeadSuccess(response);
                return null;
            }
            //资金账户不存在
            if(resultData.getCode() == CodeTable.ERROR_ACCOUNT_NOT_EXIST.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20099);
            }
            //原密码不正确
            if(resultData.getCode() == CodeTable.ERROR_PASSWORD.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20213);
            }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("修改提现密码出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 找回提现密码（身份验证）
     *@param name	姓名
     * @param identityLicenseNum	身份证
     *
     */
    @RequestMapping(value = "/findPasswordIdentity", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_PASSWORD_IDENTITY)
    @Log(type = LogEnum.FIND_PASSWORD_IDENTITY)
    public Object findPasswordIdentity(HttpServletRequest request, HttpServletResponse response, String name, String identityLicenseNum){
        try {
            if(StringUtils.isEmpty(name)||StringUtils.isEmpty(identityLicenseNum))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20097);
            }
            //校验身份证
            if(!ValidateUtil.validateIdentityLicenseNum(identityLicenseNum))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20098);
            }
            String  str = accountService.findPasswordIdentity(findUserId(request), name, identityLicenseNum.toUpperCase());
            if("1".equals(str)){//用户姓名不存在
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20100);
            }
            if("2".equals(str)){//用户身份证不匹配
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20207);
            }
            if("0".equals(str)){
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("找回提现密码（身份验证）出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 找回提现密码（手机号码验证）
     *@param name	姓名
     *@param identityLicenseNum	身份证
     *@param  verificationCode 验证码
     *
     */
    @RequestMapping(value = "/findPasswordMobile", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_PASSWORD_MOBILE)
    @Log(type = LogEnum.FIND_PASSWORD_MOBILE)
    public Object findPasswordMobile(HttpServletRequest request, HttpServletResponse response, String name, String identityLicenseNum, String verificationCode){
        try {
            if(StringUtils.isEmpty(name)||StringUtils.isEmpty(identityLicenseNum)||StringUtils.isEmpty(verificationCode))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            //校验身份证
            if(!ValidateUtil.validateIdentityLicenseNum(identityLicenseNum))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20098);
            }
            String str = accountService.findPasswordMobile(findUserId(request), name, identityLicenseNum.toUpperCase(), verificationCode);
            if("1".equals(str)){//用户姓名不存在
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20100);
            }
            if("2".equals(str)){//用户身份证不匹配
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20207);
            }
            if("3".equals(str)){//验证码不正确
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
            }
            if("0".equals(str)){
                updRespHeadSuccess(response);
                return null;
            }

        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("找回提现密码（手机号码验证）出错", e);
            }
        }
        return findException(response);
    }


    /**
     * 3.6.5	找回提现密码
     *@param name	姓名
     *@param identityLicenseNum	身份证
     *@param  verificationCode 验证码
     *@param passwordNew 新密码
     *
     */
    @RequestMapping(value = "/findWithdrawPassword", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_WITHDRAW_PASSWORD)
    @Log(type = LogEnum.FIND_WITHDRAW_PASSWORD)
    public Object findWithdrawPassword(HttpServletRequest request, HttpServletResponse response, String name, String identityLicenseNum, String verificationCode, String passwordNew){
        try {
            if(StringUtils.isEmpty(name)||StringUtils.isEmpty(identityLicenseNum)||StringUtils.isEmpty(verificationCode)||StringUtils.isEmpty(passwordNew))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20097);
            }
            //校验身份证
            if(!ValidateUtil.validateIdentityLicenseNum(identityLicenseNum))
            {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20098);
            }
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> resultDriverInfo = driverUserHandlerService.getDriverUserInfo(findUserId(request));
            if(!resultDriverInfo.isSuccess() || resultDriverInfo.getData() == null){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }

            //提现密码与登录密码不能一致
            if(passwordNew.equalsIgnoreCase(resultDriverInfo.getData().getPassword())){
                logger.error("提现密码与登录密码不能一致");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20220);
            }
            String str = accountService.findWithdrawPassword(findcapitalAccountId(request), findUserId(request),name,identityLicenseNum.toUpperCase(),verificationCode,passwordNew.toUpperCase());
            if("1".equals(str)){//用户姓名不存在
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20100);
            }
            if("2".equals(str)){//用户身份证不匹配
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20207);
            }
            if("3".equals(str)){//验证码不正确
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
            }
            if("4".equals(str)){//找回密码失败
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20208);
            }
            if("0".equals(str)){
                if(saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                    saasPayUserService.resetTradePassword(passwordNew,findUserId().toString());
                }
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("找回提现密码出错", e);
            }
        }
        return findException(response);
    }

}
