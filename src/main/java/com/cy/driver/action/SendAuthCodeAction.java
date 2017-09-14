package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.Enum.AuthCodeType;
import com.cy.pass.service.dto.Enum.SourceType;
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
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mr on 2015/6/23.
 */
@Scope("prototype")
@RestController("sendAuthAction")
public class SendAuthCodeAction extends BaseAction{
    private Logger LOG = LoggerFactory.getLogger(SendAuthCodeAction.class);
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @RequestMapping(value = "/getVerificationCode", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_GET_AUTH_CODE_DRIVER_USERINFO)
//    @Log(type = LogEnum.GET_VERIFICATION_CODE)
    public Object getVerificationCode(HttpServletRequest request, HttpServletResponse response, String mobilephone, Byte verificationType) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if (StringUtils.isEmpty(mobilephone) || !ValidateUtil.validateTelePhone(mobilephone)) {
            if (LOG.isErrorEnabled()) LOG.error("获取验证码校验：账户格式不正确");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20032);
        }
        if(AuthCodeType.contain(verificationType, AuthCodeType.values()) == null){
            if (LOG.isErrorEnabled()) LOG.error("获取验证码校验：获取验证码参数错误");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        try{
            String requestIp = request.getRemoteAddr();//app端请求ip
            Response<String> result = driverUserHandlerService.getAuthCode(mobilephone, SourceType.APP.getValue(), verificationType, requestIp);
            if(!result.isSuccess()){
                LOG.error("获取验证码(服务端)校验:response.message="+result.getCode()+"-"+result.getMessage());
                updRespHeadError(response);
                //参数不完整(包含短信模板未配置)
                if(result.getCode() == CodeTable.INVALID_ARGS.getCode()){
                    LOG.error("获取验证码(服务端)校验:获取短信内容参数不完整。"+result.getCode()+"-"+result.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                if(result.getCode() == CodeTable.ERROR_MESSAGE_SERVICE.getCode()){
                    LOG.error("获取验证码(服务端)校验:短信服务超时或者限制。"+result.getCode()+"-"+result.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20206);
                }
                //注册，账户修改(新手机),号码已注册
                if(verificationType.intValue() == AuthCodeType.REGIST.getValue().intValue()
                        || verificationType.intValue() == AuthCodeType.UPDATE_NEW_PHONE.getValue().intValue()){
                    if(result.getCode() == CodeTable.VALID_CODE.getCode()){
                        if (LOG.isErrorEnabled()) LOG.error("获取验证码(服务端)校验：注册验证码，号码已注册.");
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_20009,mobilephone);
                    }
                //修改手机号，是否注册
                }else if( verificationType.intValue() == AuthCodeType.UPDATE_PHONE.getValue().intValue()){
                    if (result.getCode() == CodeTable.INVALID_CODE.getCode()) {
                        if (LOG.isErrorEnabled()) LOG.error("获取验证码(服务端)校验：忘记密码验证码，号码未注册.");

                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30001,mobilephone);
                    }
                }//忘记密码
                else if(verificationType.intValue() == AuthCodeType.FORGET_PASSWD.getValue().intValue()){
                    if (result.getCode() == CodeTable.INVALID_CODE.getCode()) {
                        if (LOG.isErrorEnabled()) LOG.error("获取验证码(服务端)校验：忘记密码验证码，号码未注册.");

                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30001,mobilephone);
                    }
                    //货主已经注册
                    if(result.getCode() == CodeTable.AREADLYREG.getCode()){
                        if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：货主已经注册.");
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20096);
                    }
                }

                //验证码发送次数超出上限
                if(result.getCode() == CodeTable.LI_CALL_TIMES.getCode()){
                    LOG.error("获取验证码(服务端)校验:注册验证码发送次数超出上限");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30005, mobilephone);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }else{
                //返回 空
                updRespHeadSuccess(response);
                return resultMap;
            }
        }catch (Exception e){
            LOG.error("获取验证码出错,",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }
}
