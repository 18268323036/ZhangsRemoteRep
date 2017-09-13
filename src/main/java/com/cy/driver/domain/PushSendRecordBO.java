package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by yanst on 2015/10/12.
 * 消息中心对象
 */
public class PushSendRecordBO implements Serializable {

    private static final long serialVersionUID = 322500131767004622L;

    private Long msgId;//消息id(主键ID)
    private Byte type;//	推送性质：1 通知 2 交易
    private String typeVal;//	推送性质：1 通知 2 交易
    private String  title;//	消息头
    private String messageTime;//	消息产生时间
    private Long logId;//信息ID（pushId）
    private String content;//	消息
    private String url;//url地址
    private Long targetId;//目标ID
    private String jumpType;//跳转类型 （ 1 url地址、100 app首页、1001 app合同司机列表页面、1002 app合同司机线路详情页面、1101 app提交认证页面、1102 app认证未通过页面、1103 app认证通过页面、2000 app附件货源列表页面、2001 app货源详情页面、3000 app订单列表页面、3001 app订单详情页面、3002 app待承运订单列表、3003 app承运中订单列表、4001 确定收款列表、4002 确定收款详情页面）
    private String isClicked;//是否点击过
    private String siteSource;//消息来源 1快到网 2云配送

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public String getIsClicked() {
        return isClicked;
    }

    public void setIsClicked(String isClicked) {
        this.isClicked = isClicked;
    }

    public String getSiteSource() {
        return siteSource;
    }

    public void setSiteSource(String siteSource) {
        this.siteSource = siteSource;
    }
}
