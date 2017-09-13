package com.cy.driver.domain.push;

import java.io.Serializable;

/**
 * Created by yanst on 2015/12/1.
 * 推送记录
 */
public class PushInfo implements Serializable {
    private static final long serialVersionUID = 7979852235029506191L;

    /** 用户id */
    private Long userId;

    /** 用户类型（0司机 1企业） */
    private Byte userType;

    /**
     * 用途
     * 21订车成功、22订单提醒司机确认、23货主取消订车(司机未承运)、
     * 24货主申请取消订单(司机已经承运)、25司机申请取消货主同意、
     * 26司机申请取消货主不同意、27收货确认、28发货确认、
     * 29付款提醒、30邀请成为合同司机、31合同路线提醒、
     * 32添加常用车辆提醒、33常用车辆定位提醒、34合同请求定位提醒
     */
    private Integer useFor;

    /**
     * 系统来源
     * 0不区分子系统 1营销平台 2快到网货主网站 3司机app服务端
     * 4经管系统 5调度系统 6货主app服务端
     */
    private Integer eventFrom;

    /** 跳转对象ID(信息主键id：如货源id，订单id。。。) */
    private Long targetId;

    /** 跳转对象ID(信息主键id：如货源id，订单id。。。) */
    private Long targetId2;

    /** 模版内容填充信息，按顺序放入 */
    private String[] messages;

    /** 来源 1快到网 2云配送 */
    private Integer sourceType;

    /** 提醒类别：（1.货源 2.订单 3.积分 4.系统） **/
    private Byte remindType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Integer getUseFor() {
        return useFor;
    }

    public void setUseFor(Integer useFor) {
        this.useFor = useFor;
    }

    public Integer getEventFrom() {
        return eventFrom;
    }

    public void setEventFrom(Integer eventFrom) {
        this.eventFrom = eventFrom;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetId2() {
        return targetId2;
    }

    public void setTargetId2(Long targetId2) {
        this.targetId2 = targetId2;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Byte getRemindType() {
        return remindType;
    }

    public void setRemindType(Byte remindType) {
        this.remindType = remindType;
    }
}
