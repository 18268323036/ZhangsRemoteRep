package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.service.MsgCenterService;
import com.cy.pass.service.MessageCenterService;
import com.cy.pass.service.dto.MsgCenterParamDTO;
import com.cy.pass.service.dto.PushSendRecordDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.PageInfo;
import com.cy.pass.service.dto.base.PageResult;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.init.MsgCenterCountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 信息中心处理类
 * Created by yanst on 2015/10/12.
 */
@Service("msgCenterService")
public class MsgCenterServiceImpl implements MsgCenterService {
    private Logger LOG = LoggerFactory.getLogger(MsgCenterServiceImpl.class);

    @Resource
    private MessageCenterService messageCenterService;
    private boolean StringUti;

    /**
     *获取消息中心列表
     * @param page
     * @param driverId
     * @param pushKind 推送性质：1 提醒 2 系统 3 业务
     * @return
     */
    public PageResult<PushSendRecordDTO> messageCenterList(Long page , Long driverId ,String pushKind){
        if(driverId.longValue() <= 0 ){
            return new PageResult<PushSendRecordDTO>(CodeTable.ERROR);
        }
        PageInfo pageInfo = new PageInfo(page, Constants.MSG_CENTER_SIZE);
        MsgCenterParamDTO msgCenterParamDTO = new MsgCenterParamDTO();
        msgCenterParamDTO.setDriverId(driverId);
        msgCenterParamDTO.setPushKind(pushKind);
        return  messageCenterService.messageCenterList(pageInfo, msgCenterParamDTO);
    }

    /**
     *标记信息为已经查看
     * @param logId 日志ID
     * @return
     */
    public Response<Boolean> markClicked(Long logId, Long userId,Byte userType){
        if(logId.longValue() <= 0 ){
            return new Response<Boolean>(CodeTable.ERROR);
        }
        return  messageCenterService.markClicked(logId,userId,userType);
    }

    @Override
    public MsgCenterCountDTO notReadMessageCount(Long driverId) {
        MsgCenterParamDTO msgCenterParamDTO = new MsgCenterParamDTO();
        msgCenterParamDTO.setDriverId(driverId);
        Response<MsgCenterCountDTO> response = messageCenterService.notReadMessageCount(msgCenterParamDTO);
        if (!response.isSuccess()) {
            LOG.error("调用pass查询消息数量失败，失败信息={}", response.getMessage());
        }
        return response.getData();
    }

    @Override
    public Boolean deletes(Long[] msgIds) {
        Response<Boolean> response = messageCenterService.delete(msgIds);
        if(response == null){
            if(LOG.isDebugEnabled())LOG.debug("调用批量删除消息接口失败，底层返回空对象。");
            return Boolean.FALSE;
        }
        if(!response.isSuccess()){
            if(LOG.isDebugEnabled())LOG.debug("调用批量删除消息接口失败，失败信息reponse={}", JSON.toJSONString(response));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
