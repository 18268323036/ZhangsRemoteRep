package com.cy.driver.api.domain.req;

import java.io.Serializable;

/**
 * @author zhangxy 2016/8/3 16:07
 */
public class WaybillDetailVO implements Serializable{
    private static final long serialVersionUID = -1488308455968950792L;
    private String orderId;//运单ID
    private String siteCode;//平台编码
    private String transportMobile;//承运方手机号
    private String transportName;//承运人姓名
    private String carNumber;//车牌号
    private String startAddress;//装货地详细地址
    private String startProvinceCode;
    private String startProvinceValue;//装货省
    private String startCityCode;
    private String startCityValue;//装货市
    private String startCountyCode;
    private String startCountyValue;//装获区
    private String startTime;//装货时间
    private String endAddress;//卸货详细地址
    private String endProvinceCode;
    private String endProvinceValue;//卸货省
    private String endCityCode;
    private String endCityValue;//卸货市
    private String endCountyCode;
    private String endCountyValue;//卸货区
    private String endTime;//卸货时间
    private String consigneeName;//装货地联系人
    private String consigneeMobilePhone;//装货地联系人电话
    private String orderQuotedAmount;//货主报价
    private String orderQuotedPrepay;//货主报价预付款
    private String takeWay;//取货方式（0自提 1送货上门）
    private String remark;//备注
    private String waybillNature;//运单性质（1业务派单运单、2运输派单运单）
    private String transportUserId;//承运方Id


    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getTransportMobile() {
        return transportMobile;
    }

    public void setTransportMobile(String transportMobile) {
        this.transportMobile = transportMobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartProvinceCode() {
        return startProvinceCode;
    }

    public void setStartProvinceCode(String startProvinceCode) {
        this.startProvinceCode = startProvinceCode;
    }

    public String getStartProvinceValue() {
        return startProvinceValue;
    }

    public void setStartProvinceValue(String startProvinceValue) {
        this.startProvinceValue = startProvinceValue;
    }

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getStartCityValue() {
        return startCityValue;
    }

    public void setStartCityValue(String startCityValue) {
        this.startCityValue = startCityValue;
    }

    public String getStartCountyCode() {
        return startCountyCode;
    }

    public void setStartCountyCode(String startCountyCode) {
        this.startCountyCode = startCountyCode;
    }

    public String getStartCountyValue() {
        return startCountyValue;
    }

    public void setStartCountyValue(String startCountyValue) {
        this.startCountyValue = startCountyValue;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndProvinceCode() {
        return endProvinceCode;
    }

    public void setEndProvinceCode(String endProvinceCode) {
        this.endProvinceCode = endProvinceCode;
    }

    public String getEndProvinceValue() {
        return endProvinceValue;
    }

    public void setEndProvinceValue(String endProvinceValue) {
        this.endProvinceValue = endProvinceValue;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public String getEndCityValue() {
        return endCityValue;
    }

    public void setEndCityValue(String endCityValue) {
        this.endCityValue = endCityValue;
    }

    public String getEndCountyCode() {
        return endCountyCode;
    }

    public void setEndCountyCode(String endCountyCode) {
        this.endCountyCode = endCountyCode;
    }

    public String getEndCountyValue() {
        return endCountyValue;
    }

    public void setEndCountyValue(String endCountyValue) {
        this.endCountyValue = endCountyValue;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeMobilePhone() {
        return consigneeMobilePhone;
    }

    public void setConsigneeMobilePhone(String consigneeMobilePhone) {
        this.consigneeMobilePhone = consigneeMobilePhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrderQuotedAmount() {
        return orderQuotedAmount;
    }

    public void setOrderQuotedAmount(String orderQuotedAmount) {
        this.orderQuotedAmount = orderQuotedAmount;
    }

    public String getOrderQuotedPrepay() {
        return orderQuotedPrepay;
    }

    public void setOrderQuotedPrepay(String orderQuotedPrepay) {
        this.orderQuotedPrepay = orderQuotedPrepay;
    }

    public String getTakeWay() {
        return takeWay;
    }

    public void setTakeWay(String takeWay) {
        this.takeWay = takeWay;
    }

    public String getWaybillNature() {
        return waybillNature;
    }

    public void setWaybillNature(String waybillNature) {
        this.waybillNature = waybillNature;
    }

    public String getTransportUserId() {
        return transportUserId;
    }

    public void setTransportUserId(String transportUserId) {
        this.transportUserId = transportUserId;
    }
}
