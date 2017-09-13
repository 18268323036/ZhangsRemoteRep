package com.cy.driver.service;

import com.cy.pass.service.dto.PushSendRecordDTO;
import com.cy.pass.service.dto.base.PageResult;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.init.MsgCenterCountDTO;

/**
 * Created by Administrator on 2015/10/12.
 */
public interface MsgCenterService {
    /**
     *获取消息中心列表
     * @param page
     * @param driverId
     * @param pushKind 推送性质：1 提醒 2 系统 3 业务
     * @return
     */
    public PageResult<PushSendRecordDTO> messageCenterList(Long page, Long driverId, String pushKind);

    /**
     *标记信息为已经查看
     * @param logId 日志ID
     * @param userId 用户ID
     * @param logId 用户类型0司机 1企业
     * @return
     */
    public Response<Boolean> markClicked(Long logId, Long userId, Byte userType);

    /**
     * 未读信息数量
     * @param driverId  司机ID
     * @return
     */
    MsgCenterCountDTO notReadMessageCount(Long driverId);


    /**
     * 批量删除消息
     * @param msgIds  消息ID
     * @return
     */
    Boolean deletes(Long[] msgIds);

}
