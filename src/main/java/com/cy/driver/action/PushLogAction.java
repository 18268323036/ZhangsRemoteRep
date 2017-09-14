package com.cy.driver.action;

import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.MessageCenterService;
import com.cy.syslog.service.PushLogService;
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
 * 推送
 * Created by wyh on 2015/8/3.
 */
@Scope("prototype")
@RestController("pushLogAction")
public class PushLogAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(PushLogAction.class);
    @Resource
    private PushLogService pushLogService;

    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Resource
    private MessageCenterService messageCenterService;


    /**
     * 保存点击的推送
     * @return
     */
    @RequestMapping(value = "/saveClickedPush", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAVE_CLICKED_PUSH)
    public Object saveClickedPush(HttpServletRequest request, HttpServletResponse response,
                                  Long logId){
        try {
            if(logId == null){
                return findErrorParam(response);
            }
            com.cy.pass.service.dto.base.Response<Boolean> result =  messageCenterService.markClicked(logId,findUserId(),(byte)0);
            if(result.isSuccess()){
                if(result.getData()){
                    updRespHeadSuccess(response);
                    return null;
                }
                return findHandleFail(response);
            }
            return findException(response);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("保存点击的推送出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 上传极光推送绑定id
     * @param registrationId 极光推送ID
     * @return
     */
    @RequestMapping(value = "/uploadRegistrationId", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPLOAD_REGISTRATION_ID)
    public Object uploadRegistrationId(HttpServletRequest request, HttpServletResponse response,
                                       String registrationId){
        try {
            if(registrationId == null){
                return findErrorParam(response);
            }
            com.cy.pass.service.dto.base.Response<Boolean> result = driverUserHandlerService.updateRegistrationId(findUserIdStr(request), registrationId);
            if(result.isSuccess()){
                if(result.getData()){
                    updRespHeadSuccess(response);
                    return null;
                }
                return findHandleFail(response);
            }
            return findException(response);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("上传极光推送绑定id出错", e);
            }
        }
        return findException(response);
    }
}
