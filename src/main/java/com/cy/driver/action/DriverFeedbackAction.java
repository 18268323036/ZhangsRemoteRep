package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.FeedbackHandleService;
import com.cy.pass.service.dto.DriverFeedbackDTO;
import com.cy.pass.service.dto.Enum.FeedBackType;
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
 * 意见反馈处理类
 * Created by mr on 2015/7/7.
 */
@Scope("prototype")
@Controller("driverFeedbackAction")
public class DriverFeedbackAction extends BaseAction {
    private Logger LOG = LoggerFactory.getLogger(DriverFeedbackAction.class);
    @Resource
    private FeedbackHandleService feedbackHandleService;

    /**
     * 保存反馈
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveDriverFeedback", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_FEEDBACK_ADD)
    @Log(type = LogEnum.CONFIRM_CUSTOMER_INQUIRY)
    public Object saveDriverFeedback(HttpServletRequest request, HttpServletResponse response, Integer opinionClassification,
                                     String problemDesc, String pictureAddress){
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if(StringUtils.isBlank(FeedBackType.contain(opinionClassification, FeedBackType.values()))
             ){
            if (LOG.isErrorEnabled()) LOG.error("保存反馈校验信息：参数不完整");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        Long userId = findUserId(request);

        DriverFeedbackDTO feedbackDTO = new DriverFeedbackDTO();
        feedbackDTO.setType(opinionClassification);
        feedbackDTO.setImgUrl(pictureAddress);
        feedbackDTO.setDriverId(userId);
        feedbackDTO.setContent(problemDesc);
        String source = findSource(request);
        if (source.equalsIgnoreCase(Constants.OS_ANDROID)) {
            feedbackDTO.setSource(Constants.OS_ANDROID_CODE_INT);
        } else if (source.equalsIgnoreCase(Constants.OS_IOS)) {
            feedbackDTO.setSource(Constants.OS_IOS_CODE_INT);
        }else if (source.equalsIgnoreCase(Constants.OS_WXIN)) {
            feedbackDTO.setSource(Constants.OS_WXIN_CODE_INT);
        }else {
            feedbackDTO.setSource(Constants.OS_OTHER_CODE_INT);
        }

        Response<Boolean> serResponse = feedbackHandleService.save(feedbackDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("保存反馈(服务端)校验信息：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("保存反馈(服务端)校验信息：服务端错误.",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }

        updRespHeadSuccess(response);
        return resultMap;
    }
}
