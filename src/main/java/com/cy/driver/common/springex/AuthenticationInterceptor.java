package com.cy.driver.common.springex;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.service.DriverUserHandlerService;
import org.apache.commons.lang.StringUtils;
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

/**
 * Created by Administrator on 2015/9/12.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {


    private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Resource
    DriverUserHandlerService driverUserHandlerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        Object userIdStr = request.getAttribute("userId");
        if(userIdStr == null)
        {
            writeMsg(response, ApiResultCodeEnum.SER_20095);//为空说明用户不存在 token无效
            return false;
        }
        if(StringUtils.isEmpty(userIdStr.toString())){
            writeMsg(response, ApiResultCodeEnum.SER_20095);//为空说明用户不存在 token无效
            return false;
        }

        /** 王远航修改，修改时间，2016-03-10 19:36 */
        String authStateStr = (String) request.getAttribute("authState");
        if (StringUtils.isBlank(authStateStr)) {
            logger.error("认证拦截出错，request.getAttribute(authState)为空");
            writeMsg(response, ApiResultCodeEnum.SER_20217);//用户未认证
            return false;
        } else {
            Byte authState = Byte.valueOf(authStateStr);
            if (authState.intValue() != 3) {
                writeMsg(response, ApiResultCodeEnum.SER_20217);//用户未认证
                return false;
            }
            return super.preHandle(request, response, handler);
        }

//        Response<DriverUserInfoDTO> response1 = driverUserHandlerService.getDriverUserInfo(Long.valueOf(userIdStr.toString()));
//
//        if(!response1.isSuccess() || response1.getData() == null){
//            writeMsg(response, "SYS_10001");//为空说明用户不存在 token无效
//            return false;
//        }
//
//        if(!response1.getData().getSubmitType().toString().equals("3")){
//            writeMsg(response, "SER_20217");//用户未认证
//            return false;
//        }

//        return super.preHandle(request, response, handler);
    }

    private void writeMsg(HttpServletResponse response, ApiResultCodeEnum apiResultCodeEnum, String... content) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("resultCode", "0");
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new ObjectMapper().getJsonFactory().createJsonGenerator(response.getOutputStream(), JsonEncoding.UTF8);
            JSonResponse errorMsg = JSonResponse.makeHasContentJSonRespone(apiResultCodeEnum, content);
            jsonGenerator.writeObject(errorMsg);
        } catch (JsonProcessingException e) {
            if (logger.isErrorEnabled()) {
                logger.error("认证拦截器异常 - " + e.getMessage());
            }
            JSonResponse errorMsg = JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            jsonGenerator.writeObject(errorMsg);
        }
    }
}
