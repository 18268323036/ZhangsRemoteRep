package com.cy.driver.api.domain.res;

import java.util.List;

/**
 * Created by nixianjing on 16/8/2.
 */
public class WaybillOrderRes extends WaybillOrderListRes {


    /**
     * 装货地联系人
     */
    private String departureContact;


    /**
     * 发货地联系电话
     */
    private String	departureTelephone;

    /**
     * 装货地联系手机
     */
    private String	departureMobile;


    /**
     * 卸货地联系人
     */
    private String	receiveContact;


    /**
     * 收货地联系电话
     */
    private String	receiveTelephone;

    /**
     * 收货地联系手机
     */
    private String	receiveMobile;

    /**
     * 创建时间
     */
    private String oderCreateTime;

    /**
     * 修改时间
     */
    private String orderModifyTime;

    /**
     * 备注
     */
    private String	remark;

    /**
     * 运单总待收货款
     */
    private String	totalCollectionPayment;

    /**
     * 来自托单总数量（笔）
     */
    private String totalCarrierCount;

    /**
     * 转自运单父级运单编号
     */
    private String parentWaybillNum;

    /**
     * 承运方累计交易数
     */
    private String transportTotalCount;

    /**
     * 是否评价(0未评价、1已评价)
     */
    private String assessIdent;

    /**
     * 承运方头像
     */
    private String transportHeadImgUrl;


    /**
     * 托单列表
     * @return
     */
    private List<CarrierInfoRes> listData;



    public String getDepartureContact() {
        return departureContact;
    }

    public void setDepartureContact(String departureContact) {
        this.departureContact = departureContact;
    }

    public String getDepartureTelephone() {
        return departureTelephone;
    }

    public void setDepartureTelephone(String departureTelephone) {
        this.departureTelephone = departureTelephone;
    }

    public String getDepartureMobile() {
        return departureMobile;
    }

    public void setDepartureMobile(String departureMobile) {
        this.departureMobile = departureMobile;
    }

    public String getReceiveContact() {
        return receiveContact;
    }

    public void setReceiveContact(String receiveContact) {
        this.receiveContact = receiveContact;
    }

    public String getReceiveTelephone() {
        return receiveTelephone;
    }

    public void setReceiveTelephone(String receiveTelephone) {
        this.receiveTelephone = receiveTelephone;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public String getOderCreateTime() {
        return oderCreateTime;
    }

    public void setOderCreateTime(String oderCreateTime) {
        this.oderCreateTime = oderCreateTime;
    }

    public String getOrderModifyTime() {
        return orderModifyTime;
    }

    public void setOrderModifyTime(String orderModifyTime) {
        this.orderModifyTime = orderModifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalCollectionPayment() {
        return totalCollectionPayment;
    }

    public void setTotalCollectionPayment(String totalCollectionPayment) {
        this.totalCollectionPayment = totalCollectionPayment;
    }

    public String getTotalCarrierCount() {
        return totalCarrierCount;
    }

    public void setTotalCarrierCount(String totalCarrierCount) {
        this.totalCarrierCount = totalCarrierCount;
    }

    public String getParentWaybillNum() {
        return parentWaybillNum;
    }

    public void setParentWaybillNum(String parentWaybillNum) {
        this.parentWaybillNum = parentWaybillNum;
    }

    public String getTransportTotalCount() {
        return transportTotalCount;
    }

    public void setTransportTotalCount(String transportTotalCount) {
        this.transportTotalCount = transportTotalCount;
    }

    public String getAssessIdent() {
        return assessIdent;
    }

    public void setAssessIdent(String assessIdent) {
        this.assessIdent = assessIdent;
    }

    public String getTransportHeadImgUrl() {
        return transportHeadImgUrl;
    }

    public void setTransportHeadImgUrl(String transportHeadImgUrl) {
        this.transportHeadImgUrl = transportHeadImgUrl;
    }

    public List<CarrierInfoRes> getListData() {
        return listData;
    }

    public void setListData(List<CarrierInfoRes> listData) {
        this.listData = listData;
    }
}
