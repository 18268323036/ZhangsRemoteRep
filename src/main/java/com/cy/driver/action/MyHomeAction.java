package com.cy.driver.action;

import com.cy.award.service.dto.AwardAccountDTO;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.HomeOtherCountsBO;
import com.cy.driver.domain.MyHomeInfoNewBO;
import com.cy.driver.service.*;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.Enum.DriverImgType;
import com.cy.pass.service.dto.init.MsgCenterCountDTO;
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
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanst 2016/4/18 13:39
 */
@Scope("prototype")
@RestController("myHomeAction")
public class MyHomeAction extends BaseAction {
    private Logger logger = LoggerFactory.getLogger(MyHomeAction.class);

    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Resource
    private DriverImgService driverImgService;
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private CargoBrowseRecordService cargoBrowseRecordService;
    @Resource
    private MsgCenterService msgCenterService;
    @Resource
    private IntegrationService integrationService;
    @Resource
    private PointFService pointFService;
    @Resource
    private MyQuoteInfoHandleService myQuoteInfoHandleService;

    /**
     * 我的首页信息（3.4以上版本）
     *
     * @return
     */
    @RequestMapping(value = "/myHomeInfoNew", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MYHOME_INFO_NEW)
    @Log(type = LogEnum.QUERY_INDEX_INFO)
    public Object myHomeInfo(HttpServletResponse response) {
        try {
            Long driverId = findUserId();
            String token = findToken();
            Byte authState = findAuthState();
            MyHomeInfoNewBO homeInfoBO = homeInfo(driverId, token, authState);
            if (homeInfoBO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20095);
            } else {
                updRespHeadSuccess(response);
                return homeInfoBO;
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("首页信息出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    private MyHomeInfoNewBO homeInfo(Long driverId, String token, Byte authState) {
        MyHomeInfoNewBO homeInfoBO = new MyHomeInfoNewBO();
        DriverUserInfoDTO driverDTO = driverUserHandlerService.getDriverInfo(driverId);
        if (driverDTO == null) {
            return null;
        }
        //认证状态
        if (driverDTO.getSubmitType() != null) {
            homeInfoBO.setAuthStatus(driverDTO.getSubmitType().toString());
        } else {
            homeInfoBO.setAuthStatus("0");
        }
        if (StringUtils.isBlank(driverDTO.getCarNumber())) {
            homeInfoBO.setShowType("1");//未完善
        } else {
            homeInfoBO.setShowType("0");//已完善
            homeInfoBO.setCarCard(driverDTO.getCarNumber());//车牌号
        }
        homeInfoBO.setUserName(driverDTO.getName()); //用户姓名
        //用户头像
        String imgAddr = driverImgService.findByType(driverId, DriverImgType.PERSON_PHOTO.getValue());
        homeInfoBO.setPhotosAddress(imgAddr);
        homeInfoBO.setRegisterTime(DateUtil.dateFormat(driverDTO.getCreateTime()));
        //积分
        AwardAccountDTO awardAccountDTO = integrationService.countByUserId(Constants.AWARD_DRIVER,findUserId());
        if(awardAccountDTO != null){
            homeInfoBO.setIntegral(awardAccountDTO.getPointAmount());
        }
        //浏览记录数
        homeInfoBO.setBrowseNums(cargoBrowseRecordService.count(driverId));
        /** 刷新司机缓存 */
        driverUserHandlerService.refreshDriverToken(driverId, token, authState, homeInfoBO.getAuthStatus());
        return homeInfoBO;
    }
	
    /**
     * 首页数量（3.4以上版本）
     * @param response
     * @return
     */
    @RequestMapping(value = "/countHomePage", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.COUNT_HOME_PAGE)
    @Log(type = LogEnum.COUNT_PAGE_NUM)
    public Object countHomePage(HttpServletResponse response) {
        try {
            Long driverId = findUserId(request);
            /**
             * 未读消息数量
             */
            int freightNums = 0;
            MsgCenterCountDTO msgCenterCountDTO = msgCenterService.notReadMessageCount(driverId);
            if (msgCenterCountDTO != null) {
                freightNums  = msgCenterCountDTO.getNotReadTotalNum();
            }
            /**
             * 我的报价数量
             * 待承运订单数量
             */
            //
            Integer quoteNums = myQuoteInfoHandleService.myQuoteNum(driverId);
            if(quoteNums == null){
                quoteNums = 0;
            }
            Integer waitOrderNums = queryOrderService.countOrderNumByState(driverId, Constants.WAIT_CARRIER);
            if(waitOrderNums == null){
                waitOrderNums = 0;
            }

            HomeOtherCountsBO result = new HomeOtherCountsBO();
            result.setQuoteNums(quoteNums);
            result.setWaitOrderNums(waitOrderNums);
            result.setFreightNums(freightNums);
            updRespHeadSuccess(response);
            //每日登陆积分奖励-3.4版本
            pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT, LogEnum.COUNT_PAGE_NUM.getEventCode(),null,null,convert2InSource(),null);
            return result;
        } catch (Exception e) {
                logger.error("首页数量（3.4以上版本）出错。", e);
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

}