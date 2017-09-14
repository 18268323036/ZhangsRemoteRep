package com.cy.driver.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.IPUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.Enum.AuthCodeType;
import com.cy.pass.service.dto.LoginResultInfoDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import com.cy.syslog.service.DriverLogService;
import com.cy.syslog.service.dto.LogDTO;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 类描述： 作者 yanst 创建时间： 2015-6-19 下午2:59:10
 */
@Scope("prototype")
@RestController("loginAction")
public class LoginAction extends BaseAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private DriverUserHandlerService driverUserHandlerService;
	@Resource
	private DriverLogService driverLogService;
	
	@RequestMapping(value = "/loginDriverUserInfo", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_LOGIN_DRIVER_USERINFO)
//	@Log(type = LogEnum.LOGIN_DRIVER_USER_INFO)
	public Object loginDriverUserInfo(HttpServletRequest request, HttpServletResponse response, String mobilephone, String password, String mobileBrand,
									  String mobilePhoneModel, String lotuseedSid) {
		String apkVersion = findApkVersion(request);
		if(mobileBrand == null){
			mobileBrand = "";
		}
		if(mobilePhoneModel == null){
			mobilePhoneModel = "";
		}
		try {
			if (StringUtils.isEmpty(mobilephone) || !ValidateUtil.validateTelePhone(mobilephone)) {
				if (logger.isInfoEnabled())logger.info("登录账户格式不正确");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20010);
			}
            if(StringUtils.isEmpty(password)){
                if (logger.isInfoEnabled())logger.info("密码格式不正确");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20046);
            }

			String sourceStr = findSource(request);
			Byte source=0;
			if(sourceStr.equals(Constants.OS_ANDROID)){
				source = Constants.OS_ANDROID_CODE;
			}else if(sourceStr.equals(Constants.OS_IOS)){
				source = Constants.OS_IOS_CODE;
			}else if(sourceStr.equals(Constants.OS_WXIN)){
				source = Constants.OS_WXIN_CODE;
			}
			else{
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20028);
			}

			Response<LoginResultInfoDTO> resultInfo = driverUserHandlerService.loginDriverUserInfo(mobilephone, apkVersion, password, mobileBrand, mobilePhoneModel, getImei(request),source,lotuseedSid);
			if(logger.isDebugEnabled()){
				logger.debug("登录pass 返回的信息:{}", JSONArray.toJSON(resultInfo));
			}
			if(resultInfo.isSuccess())
			{
				if (logger.isDebugEnabled()) logger.debug("帐号：{}，登录成功.",mobilephone);

				saveLoginLog(Long.valueOf(resultInfo.getData().getUserId()));
				updRespHeadSuccess(response);
				return resultInfo.getData();
			}
			//参数不完整
			if(resultInfo.getCode() == CodeTable.INVALID_ARGS.getCode()){
				if (logger.isErrorEnabled()) logger.error("登录（服务端）校验：参数不完整.");
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			if(resultInfo.getCode()==CodeTable.DRIVERUSER_NOT_REGISTERED.getCode())//未注册
			{
				if(logger.isErrorEnabled())logger.error("错误信息(服务端)==>登录帐号：{}未注册",mobilephone);
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30001,mobilephone);
			}
			if(resultInfo.getCode()==CodeTable.AREADLYREG.getCode()){
				if(logger.isErrorEnabled())logger.error("错误信息(服务端)==>登录帐号：{}已经注册货主系统",mobilephone);
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20096,mobilephone);
			}
			if(resultInfo.getCode()==CodeTable.DRIVERUSER_FROZEN.getCode())//已冻结
			{
				if(logger.isErrorEnabled())logger.error("错误信息(服务端)==>登录帐号：{}已冻结",mobilephone);
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30008,mobilephone);
			}
			if(resultInfo.getCode()==CodeTable.PASSWORD_ERROR.getCode())//密码输入错误 1-3
			{
				if(logger.isErrorEnabled())logger.error("错误信息(服务端)==>登录帐号：{} 密码输入错误 1-3次",mobilephone);
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30009);
			}
			if(resultInfo.getCode()==CodeTable.FIND_PASSWORD1.getCode())//密码输入错误4-10
			{
				if(logger.isErrorEnabled())logger.error("错误信息(服务端)==>登录帐号：{} 密码输入错误4-10次",mobilephone);
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30002);
			}
			if(resultInfo.getCode()==CodeTable.FIND_PASSWORD2.getCode())//密码输入错误 10 以上
			{
				if(logger.isErrorEnabled())logger.error("错误信息(服务端)==>登录帐号：{} 密码输入错误 10 次以上", mobilephone);
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30003);
			}
			
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("登录出错 - " + e.getMessage());
			}
			e.printStackTrace();
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	public void saveLoginLog(Long userId){
		LogDTO logDTO = new LogDTO();
		logDTO.setUserDriverId(userId);
		logDTO.setCreateTime(new Date());
		logDTO.setOperationType(LogEnum.LOGIN_DRIVER_USER_INFO.getOperationType());
		logDTO.setRemark("司机app接口:"+ LogEnum.LOGIN_DRIVER_USER_INFO.getRemark());
		logDTO.setOperationName("/loginDriverUserInfo");
		logDTO.setRequestFromIp(IPUtil.findRequestIp(request));
		if(logger.isDebugEnabled()) {
			logger.debug("插入日志信息：logDTO={}", JSON.toJSONString(logDTO));
		}
		driverLogService.saveLog(logDTO);
	}

	/**
	 * 登录返回验证
	 * @param resultInfo
	 * @param mobilephone
	 * @return
	 */
	public Object loginResult (HttpServletResponse response, Response<LoginResultInfoDTO> resultInfo, String mobilephone){

		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	/**
	 * 验证 验证码
	 * @param request
	 * @param response
	 * @param mobilephone
	 * @param verificationCode
	 * @param verificationCodeType
	 * @return
	 */
	  	@RequestMapping(value = "/verificationForLogin", method = RequestMethod.POST)
	    @ResponseBody
	    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.VERIFICATION_FOR_LOGIN)
//	  	@Log(type = LogEnum.VERIFICATION_FOR_LOGIN)
	    public Object verificationForLogin(HttpServletRequest request, HttpServletResponse response, String mobilephone, String verificationCode, String verificationCodeType) {
	        Map<Object, Object> resultMap = new HashMap<Object, Object>();
			//TODO ios审核使用

	        if (StringUtils.isEmpty(mobilephone) || !ValidateUtil.validateTelePhone(mobilephone)) {
	            if (logger.isErrorEnabled()) logger.error("验证 验证码:账户格式不正确");
	            updRespHeadError(response);
	            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20032);
	        }
	        if(StringUtils.isBlank(verificationCode)){
	            if (logger.isErrorEnabled()) logger.error("验证 验证码:验证码不能为空");
	            updRespHeadError(response);
	            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20106);
	        }
			try {
				if(findUserId(request)!=103759){
					Response<Boolean> result = null;
					if (verificationCodeType != null) {
						result = driverUserHandlerService.validAuthCode(mobilephone, verificationCode, AuthCodeType.UPDATE_PHONE.getValue());
					} else {
						result = driverUserHandlerService.validAuthCode(mobilephone, verificationCode, AuthCodeType.VALID_OVER_IMEI.getValue());
					}

					if (!result.isSuccess()) {
						updRespHeadError(response);
						//参数不完整
						if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
							if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：参数不完整.");
							return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
						}
					 	//验证码失效
						if (result.getCode() == CodeTable.LI_VALID_TIMES.getCode()) {
							if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：验证码失效.");
							return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
						}
						//号码未注册
						if (result.getCode() == CodeTable.INVALID_CODE.getCode()) {
							if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：账户未注册.");
							return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20107);
						}
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
					}
					Response<Boolean> resultCode = driverUserHandlerService.updateImei(findToken(request), getImei(request));

					//参数不完整
					if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
						if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：参数不完整.");
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
					}
					if (!resultCode.isSuccess()) {
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20053);
					}
				}
	            //成功
	            updRespHeadSuccess(response);
	            return resultMap;
	        }catch(Exception e){
	        	if (logger.isErrorEnabled()) 
	        	logger.error("验证 验证码出错,",e);
	            updRespHeadError(response);
	            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	        }
	    }
}
