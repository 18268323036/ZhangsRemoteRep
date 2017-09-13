package com.cy.driver.common.springex;


import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.DriverPermissionDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 权限校验拦截器
 * 拦截： 请求不合法参数、token失效、冻结帐号、密码锁定帐号
 * @author <p>yanst </p>
 * @since 
 */
@Component("permissionValidationInterceptor")
public class PermissionValidationInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(PermissionValidationInterceptor.class);

    @Resource
	DriverUserHandlerService driverUserHandlerService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
    		String protocalType = request.getHeader(Constants.REQ_HEAD_PROTOCAL_TYPE);// 协议类型 1-json,2-tlv
    		String protocalVersion = request.getHeader(Constants.REQ_HEAD_PROTOCAL_VERSION);// 协议版本，如：3.0
    		String source = request.getHeader(Constants.REQ_HEAD_SOURCE);// 信息来源，分android，ios，wp, wxin
    		String osVersion = request.getHeader(Constants.REQ_HEAD_OS_VERSION);// 设备操作系统版本
    		String imei = request.getHeader(Constants.REQ_HEAD_IMEI);// 设备编号
    		String imsi = request.getHeader(Constants.REQ_HEAD_IMSI);// SIM卡编号
    		String timeStamp = request.getHeader(Constants.REQ_HEAD_TIME_STAMP);// 时间戳,格式：yyyyMMddHHmmss如： 20150409160033
    		String channelId = request.getHeader(Constants.REQ_HEAD_CHANNEL_ID);// 渠道id
    		String apkVersion = request.getHeader(Constants.REQ_HEAD_APK_VERSION);// 版本号,如：2.2.2
    		if (protocalType == null || "".equals(protocalType) || protocalVersion == null || "".equals(protocalVersion) || source == null || "".equals(source)
    				|| osVersion == null || "".equals(osVersion)  || timeStamp == null
    				|| "".equals(timeStamp) || channelId == null || "".equals(channelId)|| apkVersion == null || "".equals(apkVersion)) {
    			writeMsg(response, ApiResultCodeEnum.SER_20052);
    			if(logger.isErrorEnabled()){
    				logger.error("未通过拦截器===>请求头参数不合法,URL={}",request.getRequestURL());
    			}
    			return false;
    		}
            String token = request.getHeader(Constants.REQ_HEAD_TOKEN);
            
            if(token == null || "".equals(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)){
				logger.error("权限拦截失败,失败原因,token为空,token={},URL={}",token,request.getRequestURL());
                writeMsg(response, ApiResultCodeEnum.SER_20115);
                return false;
            }
            
        	if(logger.isDebugEnabled()){
    			logger.debug("请求头信息：protocalType=={},protocalVersion=={},source=={},osVersion=={},imei=={},imsi=={}," +
						"timeStamp=={},channelId=={},apkVersion=={},token=={}",
						protocalType,protocalVersion,source,osVersion,imei,imsi,
						timeStamp,channelId,apkVersion,token);
//    			logger.debug("请求头信息：protocalVersion=={}",protocalVersion);
//    			logger.debug("请求头信息：source=={}",source);
//    			logger.debug("请求头信息：osVersion=={}",osVersion);
//    			logger.debug("请求头信息：imei=={}",imei);
//    			logger.debug("请求头信息：imsi=={}",imsi);
//    			logger.debug("请求头信息：timeStamp=={}",timeStamp);
//    			logger.debug("请求头信息：channelId=={}",channelId);
//    			logger.debug("请求头信息：apkVersion=={}",apkVersion);
//    			logger.debug("请求头信息：token=={}", token);
    		}
		String reqCodeSource = request.getHeader(Constants.REQ_HEAD_REQ_CODE);

            Response<DriverPermissionDTO> resultInfo = driverUserHandlerService.permissionValidation(token, new Date());
            if(resultInfo.isSuccess()){
				request.setAttribute("capitalAccountId", resultInfo.getData().getCapitalAccountId());
            	request.setAttribute("userId", resultInfo.getData().getDriverId());
				request.setAttribute("authState", resultInfo.getData().getAuthState());
            	 return super.preHandle(request, response, handler);
            }
            if(resultInfo.getCode()==CodeTable.DRIVERUSER_NOT_LOGIN.getCode())//用户未登录
    		{
				logger.error("权限拦截失败,用户未登录,token={},URL={}",token,request.getRequestURL());
            	writeMsg(response, ApiResultCodeEnum.SER_20050);//用户未登录
                return false;
    		}
            if(resultInfo.getCode()==CodeTable.FIND_PASSWORD2.getCode())
    		{
				logger.error("权限拦截失败,密码已经锁定,token={},URL={}",token,request.getRequestURL());
            	writeMsg(response, ApiResultCodeEnum.SER_20051);//密码已经锁定
                return false;
    		}
            if(resultInfo.getCode()==CodeTable.DRIVERUSER_FROZEN.getCode())//已冻结
    		{
				logger.error("权限拦截失败,帐号已经冻结,token={},URL={}",token,request.getRequestURL());
            	writeMsg(response, ApiResultCodeEnum.SER_20101);//帐号已经冻结
                return false;
    		}
            if(resultInfo.getCode()==CodeTable.TOKEN_EXPIRE.getCode())//token 过期
    		{
				logger.error("权限拦截失败,token无效,token={},URL{}",token,request.getRequestURL());
            	writeMsg(response, ApiResultCodeEnum.SER_20050);//token无效
                return false;
    		}
			if(resultInfo.getCode()==CodeTable.EXCEPTION.getCode()){
				logger.error("权限拦截失败,底层异常,.={},URL{}",token,request.getRequestURL());
				writeMsg(response, ApiResultCodeEnum.SYS_10001);
				return false;
			}
            return super.preHandle(request, response, handler);
    }

	private void writeMsg(HttpServletResponse response, ApiResultCodeEnum apiResultCodeEnum, String... content) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("resultCode", "0");
		JsonGenerator jsonGenerator = null;
		try {
			jsonGenerator = new ObjectMapper().getJsonFactory().createJsonGenerator(response.getOutputStream(), JsonEncoding.UTF8);
			JSonResponse errorMsg = JSonResponse.makeHasContentJSonRespone(apiResultCodeEnum, content);
			jsonGenerator.writeObject(errorMsg);
		} catch (JsonProcessingException e) {
			if (logger.isErrorEnabled()) {
				logger.error("权限拦截器异常 - " + e.getMessage());
			}
			JSonResponse errorMsg = JSonResponse.makeHasContentJSonRespone(apiResultCodeEnum);
			jsonGenerator.writeObject(errorMsg);
		}
	}

}
