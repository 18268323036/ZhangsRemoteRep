package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.PushSendRecordBO;
import com.cy.driver.domain.PushSendRecordListBO;
import com.cy.driver.service.MsgCenterService;
import com.cy.pass.service.dto.PushSendRecordDTO;
import com.cy.pass.service.dto.base.PageResult;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: MainBmikeceAction
 * @author yanst 2015年9月10日 下午3:24:23
 */
@Scope("prototype")
@Controller
public class MsgCenterAction extends BaseAction {
    private Logger LOG = LoggerFactory.getLogger(MainBmikeceAction.class);

    @Resource
    private MsgCenterService msgCenterService;

    /**
     * 消息中心列表
     *
     * @param request
     * @param response
     * @param page 页数 从1开始
     * @param type 推送性质：1 通知 2 交易
     * @return
     */
    @RequestMapping(value = "/messageCenterList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MESSAGE_CENTER_LIST)
    @Log(type = LogEnum.MESSAGE_CENTER_LIST)
    public Object dialNetworkPhone(HttpServletRequest request, HttpServletResponse response, Long page, String type) {
        try {
            if(page.longValue() == 0){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            PageResult<PushSendRecordDTO> pageResults = msgCenterService.messageCenterList(page, findUserId(request), type);
            updRespHeadSuccess(response);
            return  convertPsrData(pageResults);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("消息中心列表出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


    /**
     * 消息标记为点击
     *
     * @param request
     * @param response
     * @param messageId 消息ID
     * @return
     */
    @RequestMapping(value = "/markClicked", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MARK_CLICKED)
    @Log(type = LogEnum.MARK_CLICKED)
    public Object dialNetworkPhone(HttpServletRequest request, HttpServletResponse response, Long messageId) {
        try {
            if(messageId.longValue() == 0){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Response<Boolean> response1 = msgCenterService.markClicked(messageId,findUserId(),new Byte("0"));
            if(response1.isSuccess()){
                updRespHeadSuccess(response);
                return  new HashMap<>();
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("消息中心列表出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    private PushSendRecordListBO convertPsrData(PageResult<PushSendRecordDTO> pageResults){
        PushSendRecordListBO pushSendRecordListBO = new PushSendRecordListBO();
        pushSendRecordListBO.setTotalNum(pageResults.getTotalRecord());
        if(pageResults.getTotalRecord()==0){
            pushSendRecordListBO.setTotalPage(0);
        }else
        {
            pushSendRecordListBO.setTotalPage(pageResults.getTotalPage());
        }
        List<PushSendRecordBO> list = new ArrayList<PushSendRecordBO>();
        if(pageResults.getDataList() != null){
            for(PushSendRecordDTO pushSendRecordDTO : pageResults.getDataList()){
                PushSendRecordBO pushSendRecordBO = new PushSendRecordBO();
                pushSendRecordBO.setMsgId(pushSendRecordDTO.getId());
                pushSendRecordBO.setType(pushSendRecordDTO.getPushKind());
                pushSendRecordBO.setTypeVal(SystemsUtil.msgTypeVal(pushSendRecordDTO.getPushKind()));
                pushSendRecordBO.setTitle(pushSendRecordDTO.getPushTitle());
                pushSendRecordBO.setMessageTime(DateUtil.dateFormat(pushSendRecordDTO.getCreateTime(), DateUtil.F_DATETIME));
                pushSendRecordBO.setLogId(pushSendRecordDTO.getPushId());
                pushSendRecordBO.setContent(pushSendRecordDTO.getPushContent());
                pushSendRecordBO.setUrl(pushSendRecordDTO.getUrl());
                pushSendRecordBO.setTargetId(pushSendRecordDTO.getTargetId());
                pushSendRecordBO.setJumpType(pushSendRecordDTO.getJumpType());
                if(pushSendRecordDTO.getSiteSource() != null) {
                    pushSendRecordBO.setSiteSource(String.valueOf(pushSendRecordDTO.getSiteSource()));
                }
                if(pushSendRecordDTO.getIsClicked()==null || pushSendRecordDTO.getIsClicked().intValue() == 0){
                    pushSendRecordBO.setIsClicked("0");
                }else {
                    pushSendRecordBO.setIsClicked("1");
                }
                list.add(pushSendRecordBO);
            }
        }
        pushSendRecordListBO.setBoList(list);
        return pushSendRecordListBO;
    }


    /**
     * 批量删除消息
     * @param response
     * @param msgId 消息ID
     * @return
     */
    @RequestMapping(value = "/deletesMsg", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.DELETES_MSG)
    @Log(type = LogEnum.DELETES_MSG)
    public Object deletes(HttpServletResponse response, String msgId) {
        try {
            if(StringUtils.isBlank(msgId)){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }

            if(msgId.endsWith(",")){
                msgId = msgId.substring(0,msgId.length()-1);
            }

            String [] msgIdsStr = msgId.split(",");
            Long[] msgIds = new Long[msgIdsStr.length];
            int i = 0;
            for (String str : msgIdsStr) {
                msgIds[i] = Long.valueOf(str);
                i++;
            }
            Boolean result = msgCenterService.deletes(msgIds);
            if(result){
                updRespHeadSuccess(response);
                return  null;
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("批量删除消息出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


}
