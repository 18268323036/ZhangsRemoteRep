package com.cy.driver.api;

import com.cy.driver.action.BaseAction;
import com.cy.driver.api.domain.res.HomePageNumber;
import com.cy.driver.cloudService.CloudCargoService;
import com.cy.driver.cloudService.CloudOrderService;
import com.cy.driver.cloudService.CloudQuoteService;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.*;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.pass.service.dto.MessageQueryDTO;
import com.cy.pass.service.dto.MessageRemindDTO;
import com.cy.pass.service.dto.init.MsgCenterCountDTO;
import org.apache.commons.codec.binary.Base64;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 * @author yanst 2016/6/7 10:26
 */
@Scope("prototype")
@RestController
public class HomePageController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(HomePageController.class);

    @Resource
    private CloudCargoService cloudCargoService;
    @Resource
    private CloudQuoteService cloudQuoteService;
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private MsgCenterService msgCenterService;
    @Resource
    private CloudOrderService cloudOrderService;
    @Resource
    private PointFService pointFService;
    @Resource
    private LocationService locationService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;



    /**
     *
     * 首页数量
     *
     * @return
     */
    @RequestMapping(value = "/cloudHomePageNum", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.HOME_PAGE_NUM)
    @ResponseBody
    @Log(type = LogEnum.HOME_PAGE_NUM)
    public Object homePageNum(HttpServletResponse response){
        try{
            
            Long driverId = findUserId();
            HomePageNumber homePageNumber = new HomePageNumber();
            LastLocationDTO lastLoc = locationService.queryLastLocation(driverId);
            Long nearCargoNum = 0l;
            if(lastLoc!=null) {
                nearCargoNum = cloudCargoService.countNearCargo3(findUserId(), lastLoc);
            }
            Long cargoQuoteNum = cloudQuoteService.countByUser(findUserId(), Constants.USER_TYPE_KDWSJ);
            int freightNums = queryOrderService.collectFreightNums(driverId);
            MsgCenterCountDTO msgCenterCountDTO = msgCenterService.notReadMessageCount(driverId);
            if (msgCenterCountDTO != null) {
                freightNums = freightNums + msgCenterCountDTO.getNotReadTotalNum();
            }
            homePageNumber.setCargoQuoteNum(cargoQuoteNum);
            homePageNumber.setNearCargoNum(nearCargoNum);
            homePageNumber.setFreightNums((long)freightNums);
            //查询待承运订单
            List<Integer> queryOrderStateList = new ArrayList<>();
            queryOrderStateList.add(Constants.ORDER_WAIT);
            List<Long> orderNum = cloudOrderService.countOrderNum(driverId,queryOrderStateList);
            if(orderNum==null){
                if(LOG.isDebugEnabled()) LOG.debug("调用Service服务SearchOrderService查询订单数量失败");
            }else {
                homePageNumber.setWaitCarriageNum(orderNum.get(0));
            }
            //每日登陆积分奖励-3.4版本
            pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(), Constants.CHECK_MODE_BY_EVENT, LogEnum.COUNT_PAGE_NUM.getEventCode(),null,null,convert2InSource(),null);
            updRespHeadSuccess(response);
            return homePageNumber;
        }catch (Exception e){
            LOG.error("获取首页数量信息出错{}", e);
        }
        return findException(response);
    }

    /**
     * 打开app的时候调用，检查短信跳转的业务信息是否已经被查看，如果没有则改变状态并且提供查看业务的相关信息
     * @return
     */
    @RequestMapping(value = "/getMessageInfo" , method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_MESSAGE_INFO)
    @ResponseBody
    @Log(type = LogEnum.QUERY_MESSAGE_INFO)
    public Object getMessageInfo(){
        Map<String,Object> map = new HashMap<>();
        MessageQueryDTO messageQueryDTO = new MessageQueryDTO();
        messageQueryDTO.setUserId(findUserId());
        messageQueryDTO.setUserType((byte) Constants.DRIVER);
        MessageRemindDTO messageRemindDTO = driverUserHandlerService.getLastMessage(messageQueryDTO);
        if(messageRemindDTO==null || messageRemindDTO.getViewState()==1){
            updRespHeadError(response);
            return null;
        }
        boolean isModifySuccess = driverUserHandlerService.modifyMessageState(messageRemindDTO.getId(),(byte)1);
        if(isModifySuccess){
            LOG.debug("修改消息状态成功");
        }else {
            LOG.debug("修改消息状态失败");
        }
        map.put("source",messageRemindDTO.getBusinessSource());
        map.put("businessType",messageRemindDTO.getBusinessType());
        map.put("businessId",messageRemindDTO.getBusinessId());
        return map;
    }

    /**
     * 通过短信第一次点击进入货源详情页面时要改变短信查看状态从未激活(-1)到等待查看(0)
     * @return
     */
    @RequestMapping(value = "/modifyMessageState")
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MODIFY_MESSAGE_STATE)
    @ResponseBody
    @Log(type = LogEnum.MODIFY_MESSAGE_STATE)
    public Object modifyMessageState(String key){
        byte[] bytes = Base64.decodeBase64(key);
        String keyStr = new String(bytes);
        boolean isModifySuccess = driverUserHandlerService.modifyMessageState(Long.valueOf(keyStr),(byte)0);
        if(isModifySuccess){
            LOG.debug("修改消息状态成功");
            return null;
        }else {
            LOG.debug("修改消息状态失败");
            return null;
        }
    }



}
