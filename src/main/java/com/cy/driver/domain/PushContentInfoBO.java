package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 3.0推送接受信息
 * *表示为必填
 * Created by wyh on 2015/7/22.
 */
public class PushContentInfoBO implements Serializable {
    private static final long serialVersionUID = -6043576522990417120L;

    private Integer pushChannel;//* 推送服务渠道(1极光-默认)
    private Integer type;//* 推送方式(1全部，2分组，3，4单个)
    private String driverId;//司机id 或者 别名(推送方式为全部可为空)
    private String pushTitle;//* 推送标题
    private String pushContent;//* 推送内容
    private Integer eventFrom;//* 来源(1-营销平台 2-appDriverServer 3-Web网站 4-经管系统 5-调度服务)
    private String tarId;//跳转对象ID(信息主键id：如货源id，订单id。。。)

    /**
     * 跳转方式
     * 1url地址跳转
     * 100跳转app首页
     * 1001跳转app合同司机列表页面
     * 1002跳转app合同司机线路详情页面
     * 1101跳转app提交认证页面
     * 1102跳转app认证未通过页面
     * 1103跳转app认证通过页面
     * 2000跳转app附件货源列表页面
     * 2001跳转app货源详情页面(导入货源配车成功、)
     * 3000跳转app订单列表页面
     * 3001跳转app订单详情页面(订车成功、订单提醒司机确认、订单申请取消、收货确认)
     * 3002跳转app待承运订单列表
     * 3003跳转app承运中订单列表
     * 4001跳转到确定收款列表
     * 4002跳转到确定收款详情页面
     */
    private String jumpType;//*
    private Integer haveCover;//* 是否覆盖同类信息(0不覆盖、1覆盖)
    private String jumpUrl;//跳转url
    private String remark;//备注
    private String spareOne;//备用字段1
    private String spareTwo;//备用字段2
    private String spareTree;//备用字段3

    public Integer getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(Integer pushChannel) {
        this.pushChannel = pushChannel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public Integer getEventFrom() {
        return eventFrom;
    }

    public void setEventFrom(Integer eventFrom) {
        this.eventFrom = eventFrom;
    }

    public String getTarId() {
        if(tarId == null){
            tarId = "";
        }
        return tarId;
    }

    public void setTarId(String tarId) {
        this.tarId = tarId;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public Integer getHaveCover() {
        if(haveCover != null && haveCover.intValue() == 1){
            haveCover = 1;
        }else{
            haveCover = 0;
        }
        return haveCover;
    }

    public void setHaveCover(Integer haveCover) {
        this.haveCover = haveCover;
    }

    public String getJumpUrl() {
        if(jumpUrl == null){
            jumpUrl = "";
        }
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getRemark() {
        if(remark == null){
            remark = "";
        }
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSpareOne() {
        if(spareOne == null){
            spareOne = "";
        }
        return spareOne;
    }

    public void setSpareOne(String spareOne) {
        this.spareOne = spareOne;
    }

    public String getSpareTwo() {
        if(spareTwo == null){
            spareTwo = "";
        }
        return spareTwo;
    }

    public void setSpareTwo(String spareTwo) {
        this.spareTwo = spareTwo;
    }

    public String getSpareTree() {
        if(spareTree == null){
            spareTree = "";
        }
        return spareTree;
    }

    public void setSpareTree(String spareTree) {
        this.spareTree = spareTree;
    }
}
