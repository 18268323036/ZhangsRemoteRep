package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.Enum.AuthCodeType;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mr on 2015/6/24.
 */
@Scope("prototype")
@Controller("resetAction")
public class ResetPasswdAction extends BaseAction {
    private Logger LOG = LoggerFactory.getLogger(RegistAction.class);
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @RequestMapping(value = "/nextForgetPassword", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_FORGET_NEXT_DRIVER_USERINFO)
    @Log(type = LogEnum.NEXT_FOR_GET_PASSWORD)
    public Object nextForgetPassword(HttpServletRequest request, HttpServletResponse response, String mobilephone, String verificationCode) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if (StringUtils.isEmpty(mobilephone) || !ValidateUtil.validateTelePhone(mobilephone)) {
            if (LOG.isErrorEnabled()) LOG.error("忘记密码下一步校验:账户格式不正确");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20032);
        }
        if(StringUtils.isBlank(verificationCode)){
            if (LOG.isErrorEnabled()) LOG.error("忘记密码下一步校验:验证码不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20106);
        }

        try{
            Response<Boolean> result = driverUserHandlerService.validAuthCode(mobilephone,verificationCode, AuthCodeType.FORGET_PASSWD.getValue());
            if(!result.isSuccess()){
                updRespHeadError(response);
                //参数不完整
                if(result.getCode() == CodeTable.INVALID_ARGS.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("忘记密码下一步(服务端)校验：参数不完整.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                //验证码失效
                if(result.getCode() == CodeTable.LI_VALID_TIMES.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("忘记密码下一步(服务端)校验：验证码失效.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
                }
                //号码未注册
                if(result.getCode() == CodeTable.INVALID_CODE.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("忘记密码下一步(服务端)校验：账户未注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30001);
                }
                //货主已经注册
                if(result.getCode() == CodeTable.AREADLYREG.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：货主已经注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20096);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            //成功
            updRespHeadSuccess(response);
            return resultMap;
        }catch(Exception e){
            LOG.error("忘记密码下一步校验出错,",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_RESET_PASSWD_DRIVER_USERINFO)
    public Object resetPassword(HttpServletRequest request, HttpServletResponse response,
                                String mobilephone, String passwordNew, String verificationCode) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if (StringUtils.isEmpty(mobilephone) || !ValidateUtil.validateTelePhone(mobilephone)) {
            if (LOG.isErrorEnabled()) LOG.error("重置密码校验：账户格式不正确.");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20032);
        }
        if(StringUtils.isEmpty(passwordNew)){
            if (LOG.isErrorEnabled()) LOG.error("重置密码校验：密码格式不正确.");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20046);
        }
        if(StringUtils.isBlank(verificationCode)){
            if (LOG.isErrorEnabled()) LOG.error("重置密码校验：验证码不能为空.");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20106);
        }

        try{
            //1.校验验证码
            Response<Boolean> result = driverUserHandlerService.validAuthCode(mobilephone, verificationCode,AuthCodeType.FORGET_PASSWD.getValue());
            if(!result.isSuccess()){
                if (LOG.isErrorEnabled()) LOG.error("重置密码验证码(服务端)校验：response.message="+result.getMessage());
                updRespHeadError(response);
                //参数不完整
                if(result.getCode() == CodeTable.INVALID_ARGS.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("重置密码验证码(服务端)校验：参数不完整.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                //验证码失效
                if(result.getCode() == CodeTable.LI_VALID_TIMES.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("重置密码验证码(服务端)校验：验证码不正确或者已失效.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
                }
                //号码已注册
                if(result.getCode() == CodeTable.VALID_CODE.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("重置密码验证码(服务端)校验：手机号已被注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_20009);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            //2.重置密码
            String md5PassWd = passwordNew;
            Response<Long> resetResponse = driverUserHandlerService.resetPasswd(mobilephone, md5PassWd, findImei(request));
            if(!resetResponse.isSuccess()){
                if (LOG.isErrorEnabled()) LOG.error("重置密码保存(服务端)校验：response.message="+resetResponse.getMessage());
                //参数不完整
                if(resetResponse.getCode() == CodeTable.INVALID_ARGS.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("重置密码保存(服务端)校验：参数不完整.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                //用户不存在
                if(resetResponse.getCode() == CodeTable.INVALID_CODE.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("重置密码保存(服务端)校验：用户不存在.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
                }
                //更新记录数=0
                if(resetResponse.getCode() == CodeTable.UPDATE_SQL.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("重置密码保存(服务端)校验：更新记录不存在.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            resultMap.put("userId",resetResponse.getData().toString());
            return resultMap;
        }catch (Exception e){
            LOG.error("重置保存密码出错,",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }
}
