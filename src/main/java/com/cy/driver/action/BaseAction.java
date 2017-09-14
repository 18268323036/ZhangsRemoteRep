package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.StrUtil;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * 所有基类action
 *
 * @author hayden
 */
@RestController
public class BaseAction {
    @Autowired
    protected HttpServletRequest request;

    protected HttpServletResponse response;

    Logger LOG = LoggerFactory.getLogger(BaseAction.class);

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**获取资金帐户ID @return Long */
    public Long findcapitalAccountId(HttpServletRequest req){
        Long capitalAccountId = (Long)req.getAttribute("capitalAccountId");
        return capitalAccountId;
    }

    /**获取资金帐户ID @return Long */
    public Long findcapitalAccountId(){
        Long capitalAccountId = (Long)request.getAttribute("capitalAccountId");
        return capitalAccountId;
    }

    /**获取token  @return String */
    public String findToken(HttpServletRequest req){
        String token = req.getHeader(Constants.REQ_HEAD_TOKEN);
        return token;
    }

    /**获取token  @return String */
    public String findToken(){
        String token = request.getHeader(Constants.REQ_HEAD_TOKEN);
        return token;
    }

    /**获取imei  @return String */
    public String findImei(HttpServletRequest req){
        String imei = req.getHeader(Constants.REQ_HEAD_IMEI);
        return imei;
    }


    /**获取司机ID @return Long */
    public Long findUserId(HttpServletRequest req){
        Long userId = (Long)req.getAttribute("userId");
    	return userId;
    }

    /**获取司机ID @return Long */
    public Long findUserId(){
        Long userId = (Long)request.getAttribute("userId");
        return userId;
    }

    /**获取司机认证状态 @return Byte */
    public Byte findAuthState(HttpServletRequest req){
        return Byte.valueOf((String)req.getAttribute("authState"));
    }

    /**获取司机认证状态 @return Byte */
    public Byte findAuthState(){
        return Byte.valueOf((String)request.getAttribute("authState"));
    }


    /**获取司机ID @return String */
    public String findUserIdStr(HttpServletRequest req){
    	return req.getAttribute("userId") == null ? "" : req.getAttribute("userId").toString();
    }

    /**获取来源 @return String */
    public String findSource(HttpServletRequest req){
    	return req.getHeader(Constants.REQ_HEAD_SOURCE);
    }
    
    /** 获取司机当前apk version  @return String */
    public String findApkVersion(HttpServletRequest req){
    	return req.getHeader(Constants.REQ_HEAD_APK_VERSION);
    }
    
    public void updRespHeadError(HttpServletResponse resp){
        resp.setHeader("resultCode", "0");
    }

    public void updRespHeadSuccess(HttpServletResponse resp){
        resp.setHeader("resultCode", "1");
    }

    public String getImsi(HttpServletRequest resp){return resp.getHeader(Constants.REQ_HEAD_IMSI);}

    public String getImei(HttpServletRequest resp){return resp.getHeader(Constants.REQ_HEAD_IMEI);}

    /**
     * 获得带数据的ResponseData
     * @author wyh
     */
    public Object findResponseData(Response result, HttpServletResponse resp){
        if (result.isSuccess()) {
            updRespHeadSuccess(resp);
            return result.getData();
        } else {
            if (result.getCode() == CodeTable.EXCEPTION.getCode()) {
                return findException(resp);
            } else {
                return findErrorParam(resp);
            }
        }
    }

    /**
     * 获得boolean的Response
     * @author wyh
     */
    public Object findResponseBoolean(Response<Boolean> result, HttpServletResponse resp){
        if (result.isSuccess()) {
            updRespHeadSuccess(resp);
            return null;
        } else {
            if (result.getCode() == CodeTable.EXCEPTION.getCode()) {
                return findException(resp);
            } else {
                return findErrorParam(resp);
            }
        }
    }

    /**
     * 获得报错的Response
     * @author wyh
     */
    public Object findException(HttpServletResponse resp){
        updRespHeadError(resp);
        return JSonResponse.makeException();
    }

    /**
     * 获得参数错误的Response
     * @param resp
     * @return
     */
    public Object findErrorParam(HttpServletResponse resp){
        updRespHeadError(resp);
        return JSonResponse.makeErrorParam();
    }

    /**
     * 获得处理失败的Response
     * @param resp
     * @return
     */
    protected Object findHandleFail(HttpServletResponse resp){
        updRespHeadError(resp);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
    }

    protected Object findJSonResponse(HttpServletResponse resp, ApiResultCodeEnum apiResultCodeEnum){
        updRespHeadError(resp);
        return JSonResponse.makeHasContentJSonRespone(apiResultCodeEnum);
    }


    /**
     * 生成参数
     * @param req
     * @param c
     * @param <T>
     * @return
     * @throws Exception
     * @author wyh
     */
    protected <T> T buildParam(HttpServletRequest req, Class<T> c){
        T t = null;
        try {
            Enumeration<String> names = req.getParameterNames();
            if (names != null) {
                t = c.newInstance();
                while (names.hasMoreElements()) {
                    try {
                        String key = names.nextElement();
                        Field field = c.getDeclaredField(key);
                        Class paramType = field.getType();
                        Class paraClass = null;
                        Object value = null;
                        String reqVal = req.getParameter(key);
                        if (paramType.equals(String.class)) {
                            paraClass = String.class;
                            value = reqVal;
                        } else if (paramType.equals(Integer.class)) {
                            paraClass = Integer.class;
                            value = StrUtil.buildInt(reqVal);
                        } else if (paramType.equals(Long.class)){
                            paraClass = Long.class;
                            value = StrUtil.buildLong(reqVal);
                        }
                        if (paraClass == null) {
                            continue;
                        }
                        Method method = c.getMethod("set" + StrUtil.toIndexUpperCase(field.getName()), paraClass);
                        method.invoke(t, value);
                    } catch (Exception e) {}
                }
            }
            return t;
        } catch (Exception e){}
        return null;
    }


    public Integer convert2InSource(){
        Integer source=0;
        String sourceStr = findSource(request);
        if(sourceStr.equals(Constants.OS_ANDROID)){
            source = Constants.OS_ANDROID_CODE_INT;
        }else if(sourceStr.equals(Constants.OS_IOS)){
            source = Constants.OS_IOS_CODE_INT;
        }else if(sourceStr.equals(Constants.OS_WXIN)){
            source = Constants.OS_WXIN_CODE_INT;
        }else {
            source = Constants.OS_OTHER_CODE_INT;
        }
        return source;
    }

    /**
     * 积分提醒
     */
    public void promptPointMSG(HttpServletResponse resp, String eventName, Integer amount) {
        resp.setHeader("promptCode", "1");
        resp.setHeader("promptMsg", eventName+" 获得"+amount+"积分");
    }
}
