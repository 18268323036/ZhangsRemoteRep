package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * Created by nixianjing on 16/8/2.
 */
public class WaybillOrderListRes implements Serializable {


    /**
     * 转单订单ID
     */
    private String orderId;

    /**
     * 平台编码
     */
    private String siteCode;

    /**
     * 转单运单父级运单id（t_waybill_info.id）
     */
    private String parentWaybillId;

    /**
     * 运单性质（1业务派单运单、2运输派单运单、4运输揽件运单）
     */
    private String	waybillNature;

    /**
     * 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
     */
    private String	transportType;

    /**
     * 承运方用户名称
     */
    private String	transportUserName;

    /**
     * 承运方用户编码
     */
    private String	transportUserId;

    /**
     * 承运方企业名称
     */
    private String transportCompanyName;

    /**
     * 承运方所属主帐号id
     */
    private String	transportOwnUserId;

    /**
     * 承运方固定电话
     */
    private String	transportTelephone;

    /**
     * 承运方手机号码
     */
    private String	transportMobile;

    /**
     * 承运方车牌号码
     */
    private String	transportCarnumber;

    /**
     * 起始地(省-市-县)
     */
    private String startAddress;

    /**
     * 目的地(省-市-县)
     */
    private String endAddress;

    /**
     * 要求装货时间(yyyy-mm-dd)
     */
    private String requestStartTime;

    /**
     * 要求卸货时间(yyyy-mm-dd)
     */
    private String requestEndTime;

    /**
     * 总体积
     */
    private String totalCubage;

    /**
     * 总件数
     */
    private String	totalQuantity;

    /**
     * 总重量
     */
    private String	totalWeight;

    /**
     * 我的报价运费
     */
    private String	totalFare;

    /**
     * 我的报价预付款
     */
    private String	prepayFare;

    /**
     * 承运方报价状态(-2货主不采纳承运方的报价、0承运方未报价、1承运方已报价、2货主已采纳承运方的报价)
     */
    private String	transportFareState;

    /**
     * 承运方的报价运费
     */
    private String	transportTotalFare;

    /**
     * 承运方的报价预付款
     */
    private String	transportPrepayFare;

    /**
     * 运单锁（0正常、1运单已锁定）
     */
    private String	waybillLock;

    /**
     * 订单
     */
    private String stateName;

    /**
     * 运单状态:1待接单、-1拒绝接单、12已接单、6交易取消、3已装货、7已卸货、9司机未安装App
     */
    private String	state;

    /**
     * 转单状态（0未转单、1已转单）
     */
    private String turnedState;

    /**
     * 是否转单而来（0否 1是）
     */
    private String isTurned;

    /**
     * 运单号
     */
    private String waybillNum;

    /**
     * 运单名称
     */
    private String waybillName;

    /**
     * 承运方认证状态
     */
    private String authState;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getParentWaybillId() {
        return parentWaybillId;
    }

    public void setParentWaybillId(String parentWaybillId) {
        this.parentWaybillId = parentWaybillId;
    }

    public String getWaybillNature() {
        return waybillNature;
    }

    public void setWaybillNature(String waybillNature) {
        this.waybillNature = waybillNature;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportUserName() {
        return transportUserName;
    }

    public void setTransportUserName(String transportUserName) {
        this.transportUserName = transportUserName;
    }

    public String getTransportUserId() {
        return transportUserId;
    }

    public void setTransportUserId(String transportUserId) {
        this.transportUserId = transportUserId;
    }

    public String getTransportCompanyName() {
        return transportCompanyName;
    }

    public void setTransportCompanyName(String transportCompanyName) {
        this.transportCompanyName = transportCompanyName;
    }

    public String getTransportOwnUserId() {
        return transportOwnUserId;
    }

    public void setTransportOwnUserId(String transportOwnUserId) {
        this.transportOwnUserId = transportOwnUserId;
    }

    public String getTransportTelephone() {
        return transportTelephone;
    }

    public void setTransportTelephone(String transportTelephone) {
        this.transportTelephone = transportTelephone;
    }

    public String getTransportMobile() {
        return transportMobile;
    }

    public void setTransportMobile(String transportMobile) {
        this.transportMobile = transportMobile;
    }

    public String getTransportCarnumber() {
        return transportCarnumber;
    }

    public void setTransportCarnumber(String transportCarnumber) {
        this.transportCarnumber = transportCarnumber;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getRequestStartTime() {
        return requestStartTime;
    }

    public void setRequestStartTime(String requestStartTime) {
        this.requestStartTime = requestStartTime;
    }

    public String getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(String requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public String getTotalCubage() {
        return totalCubage;
    }

    public void setTotalCubage(String totalCubage) {
        this.totalCubage = totalCubage;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getPrepayFare() {
        return prepayFare;
    }

    public void setPrepayFare(String prepayFare) {
        this.prepayFare = prepayFare;
    }

    public String getTransportFareState() {
        return transportFareState;
    }

    public void setTransportFareState(String transportFareState) {
        this.transportFareState = transportFareState;
    }

    public String getTransportTotalFare() {
        return transportTotalFare;
    }

    public void setTransportTotalFare(String transportTotalFare) {
        this.transportTotalFare = transportTotalFare;
    }

    public String getTransportPrepayFare() {
        return transportPrepayFare;
    }

    public void setTransportPrepayFare(String transportPrepayFare) {
        this.transportPrepayFare = transportPrepayFare;
    }

    public String getWaybillLock() {
        return waybillLock;
    }

    public void setWaybillLock(String waybillLock) {
        this.waybillLock = waybillLock;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTurnedState() {
        return turnedState;
    }

    public void setTurnedState(String turnedState) {
        this.turnedState = turnedState;
    }

    public String getIsTurned() {
        return isTurned;
    }

    public void setIsTurned(String isTurned) {
        this.isTurned = isTurned;
    }

    public String getWaybillNum() {
        return waybillNum;
    }

    public void setWaybillNum(String waybillNum) {
        this.waybillNum = waybillNum;
    }

    public String getWaybillName() {
        return waybillName;
    }

    public void setWaybillName(String waybillName) {
        this.waybillName = waybillName;
    }

    public String getAuthState() {
        return authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
