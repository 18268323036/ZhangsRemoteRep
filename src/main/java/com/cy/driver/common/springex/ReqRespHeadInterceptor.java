package com.cy.driver.common.springex;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.driver.action.BaseAction;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.MD5Util;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 请求响应头拦截
 * Created by wyh on 2015/5/7.
 */
@Component("reqRespHeadInterceptor")
public class ReqRespHeadInterceptor extends HandlerInterceptorAdapter {
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(ReqRespHeadInterceptor.class);

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("各请求开始时间");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        long beginTime = System.currentTimeMillis();
        startTimeThreadLocal.set(beginTime);
        //验证签名
        if (!validSignature(request)) {
            writeMsg(response, ApiResultCodeEnum.SYS_10002);
            return false;
        }

        String reqCodeSource = request.getHeader(Constants.REQ_HEAD_REQ_CODE);
        String reqCode = findReqHeadCode(handler, response);
        int reqCodeInt = 0;
        if (!"".equals(reqCode)) {
            reqCodeInt = Integer.parseInt(reqCode) + 1;
        }
        response.setHeader("respCode", reqCodeInt + "");
        if (reqCodeSource == null || "".equals(reqCodeSource)) {
            writeMsg(response, ApiResultCodeEnum.SER_20026, reqCodeSource);
            return false;
        }
        if (!reqCodeSource.equals(reqCode)) {
            writeMsg(response, ApiResultCodeEnum.SER_20026, reqCodeSource);
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 验证签名 false验证失败，true验证成功
     */
    private boolean validSignature(HttpServletRequest request) {
        String source = request.getHeader(Constants.REQ_HEAD_SOURCE);
        if (StringUtils.isBlank(source)) {
            return false;
        }

        String signature = request.getHeader(Constants.REQ_HEAD_SIGNATURE);
        if (StringUtils.isEmpty(signature)) {
            return false;
        }
        String messengerId = request.getHeader(Constants.REQ_HEAD_MESSENGER_ID);
        String reqCode = request.getHeader(Constants.REQ_HEAD_REQ_CODE);
        String[] strs = new String[]{source, messengerId, reqCode};

        String version = request.getHeader(Constants.REQ_HEAD_APK_VERSION);//获取版本
        if (StringUtils.isBlank(version)) {
            return false;
        }
        String serviceSignature;
        if ((Constants.OS_IOS.equals(source) && version.startsWith("1.0")) || (Constants.OS_ANDROID.equals(source) && version.startsWith("3.0"))) {
            //服务端生成签名
            serviceSignature = MD5Util.md5Signature(strs);
        } else if (Constants.OS_WXIN.equals(source)) {
            //微信服务端生成签名
            serviceSignature = MD5Util.md5Signature1ByWxin(strs);
        } else {
            //服务端生成签名
            serviceSignature = MD5Util.md5Signature1(strs);
        }
        if (signature.equalsIgnoreCase(serviceSignature)) {
            return true;
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();//2、结束时间
        long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime;//3、消耗的时间
        logger.warn(new StringBuilder("本次请求:").append(request.getRequestURL()).append("总耗时为：").append(consumeTime).append("毫秒").toString());
    }

    private void writeMsg(HttpServletResponse response, ApiResultCodeEnum apiResultCodeEnum, String... content)throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("resultCode", "0");
        JsonGenerator jsonGenerator = new ObjectMapper().getJsonFactory().createJsonGenerator(response.getOutputStream(), JsonEncoding.UTF8);
        JSonResponse errorMsg = JSonResponse.makeHasContentJSonRespone(apiResultCodeEnum, content);
        jsonGenerator.writeObject(errorMsg);
    }

    private String findReqHeadCode(Object handler, HttpServletResponse response) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            BaseAction baseAction = (BaseAction) handlerMethod.getBean();
            baseAction.setResponse(response);

            ReqRespHeadCode reqHeadCode = method.getAnnotation(ReqRespHeadCode.class);
            if (reqHeadCode == null || reqHeadCode.reqHeadCode() == null) {
                return "";
            }
            return reqHeadCode.reqHeadCode().getCode();
        }
        return "";
    }
}

