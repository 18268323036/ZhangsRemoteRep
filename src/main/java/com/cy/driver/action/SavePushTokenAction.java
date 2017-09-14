package com.cy.driver.action;

import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.SavePushTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 保存推送的token
 * Created by wyh on 2015/7/24.
 */
@Scope("prototype")
@RestController("savePushTokenAction")
public class SavePushTokenAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(SavePushTokenAction.class);
    @Resource
    private SavePushTokenService savePushTokenService;

    //保存IOS推送的token
    @RequestMapping(value = "/saveIosPushToken", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAVE_IOS_PUSH_TOKEN)
    @Log(type = LogEnum.SAVE_IOS_PUSH_TOKEN)
    public Object saveIosPushToken(HttpServletRequest request, HttpServletResponse response,
                                   String pushToken){
        try {
            if (StringUtils.isEmpty(pushToken)) {
                logger.error("保存IOS推送的token失败，pushToken={}", pushToken);
                return findErrorParam(response);
            }
            String source = findSource(request);
            if(!Constants.OS_IOS.equalsIgnoreCase(source)){
                logger.error("保存IOS推送的token失败，source={}", source);
                return findErrorParam(response);
            }
            long driverId = findUserId(request);
            boolean flag = savePushTokenService.savePushToken(driverId, pushToken);
            if (flag) {
                updRespHeadSuccess(response);
                return null;
            } else {
                return findHandleFail(response);
            }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("保存推送的token出错", e);
            }
        }
        return findException(response);
    }
}
