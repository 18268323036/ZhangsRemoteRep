package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * 订单/运单列表
 * @author zhangxy 2016/6/1 16:52
 */
public class WayBillList implements Serializable {

    private static final long serialVersionUID = -5384112101483439620L;

    private String orderId;//运单id
    private Integer orderLock;//订单锁定状态(0正常、1订单已锁定)
    private Long companyId;//公司id
    private String companyName;//公司名称
    private Long createOwnUserId;//创建人主企业账号
    private Long createUserId;//创建人账号
    private String createUserName;//创建人姓名
    private Integer createUserType;//创建人类型
    /**
     * 快到网订单状态(1待司机确认、11待货主发货、12待司机装货、3运输跟踪(2.0)/待司机卸货(3.0)、
     * 7司机已卸货(2.0)/待货主收货(3.0)、5订单完成、6交易取消、9司机未安装App、20订单关闭(预留))
     *
     * 云配送运单状态（1待接单、-1拒绝接单、12已接单、6交易取消、3已装货、7已卸货、9司机未安装App）
     */
    private Integer orderStatusCode;
    private String cargoTotalQuantity;//总件数
    private String cargoWeight;//总重量(单位：吨)
    private String cargoCubage;//总体积(单位：方)
    private Integer orderSource;//订单来源（1来源快到网、2来源云配送,3SAAS运单）
    private String startAddress;//起始地
    private String endAddress;//目的地
    private String requestStartTime;//要求装货时间(yyyy-mm-dd hh:mm)
    private String requestEndTime;//要求卸货时间(yyyy-mm-dd hh:mm)
    private Integer orderQuoteType;//报价类型(1整车报价、2重量报价、3体积报价)
    private Double orderQuotedAmount;//货主报价(价格为0表示价格面议)
    private Double orderQuotedPrepay;//货主报价预付款
    private Integer transportFareState;//司机报价的状态(-2货主不采纳承运方的报价、0承运方未报价、1承运方已报价、2货主已采纳承运方的报价)
    private Double transportQuote;//司机报价
    private Double transportQuotePrepay;//司机报价预付款
    private Double finalPayment;//确认后的最终运费价格
    private Double finalAdvancePayment;//确认后的最终预付款
    private Integer waitSignIn;//待签收数
    private String contactPhone;//货主联系电话
    private String haveCarrierSign;//确定承运是否需要签署协议（0否、1是）
    private Integer cancleSource;//订单取消来源（0正常、1快到网司机、2快到网货主、4系统取消、6区域配送用户、7重新派单）
    private String cargoName;//货物名称
    private String carrierName;//货物名称
    private String carLength;//车长
    private String stateName;//状态名
    private Integer authState;//公司认证状态
    private String platformCode;//平台编码
    private Double totalCollectionPayment;//司机代收货款
    private String isComment;//承运方是否评价
    private String orderSubAssessIdent;//分包商是否评价
    private String carrierSignType;//签订的协议类型


    public String getCarrierSignType() {
        return carrierSignType;
    }

    public void setCarrierSignType(String carrierSignType) {
        this.carrierSignType = carrierSignType;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getOrderSubAssessIdent() {
        return orderSubAssessIdent;
    }

    public void setOrderSubAssessIdent(String orderSubAssessIdent) {
        this.orderSubAssessIdent = orderSubAssessIdent;
    }

    public Double getTotalCollectionPayment() {
        return totalCollectionPayment;
    }

    public void setTotalCollectionPayment(Double totalCollectionPayment) {
        this.totalCollectionPayment = totalCollectionPayment;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateOwnUserId() {
        return createOwnUserId;
    }

    public void setCreateOwnUserId(Long createOwnUserId) {
        this.createOwnUserId = createOwnUserId;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getCreateUserType() {
        return createUserType;
    }

    public void setCreateUserType(Integer createUserType) {
        this.createUserType = createUserType;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public Integer getAuthState() {
        return authState;
    }

    public void setAuthState(Integer authState) {
        this.authState = authState;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public Integer getCancleSource() {
        return cancleSource;
    }

    public void setCancleSource(Integer cancleSource) {
        this.cancleSource = cancleSource;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getHaveCarrierSign() {
        return haveCarrierSign;
    }

    public void setHaveCarrierSign(String haveCarrierSign) {
        this.haveCarrierSign = haveCarrierSign;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(Integer orderLock) {
        this.orderLock = orderLock;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(Integer orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getCargoTotalQuantity() {
        return cargoTotalQuantity;
    }

    public void setCargoTotalQuantity(String cargoTotalQuantity) {
        this.cargoTotalQuantity = cargoTotalQuantity;
    }

    public String getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(String cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public String getCargoCubage() {
        return cargoCubage;
    }

    public void setCargoCubage(String cargoCubage) {
        this.cargoCubage = cargoCubage;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
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

    public String getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(String requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public String getRequestStartTime() {
        return requestStartTime;
    }

    public void setRequestStartTime(String requestStartTime) {
        this.requestStartTime = requestStartTime;
    }

    public Integer getOrderQuoteType() {
        return orderQuoteType;
    }

    public void setOrderQuoteType(Integer orderQuoteType) {
        this.orderQuoteType = orderQuoteType;
    }

    public Double getOrderQuotedAmount() {
        return orderQuotedAmount;
    }

    public void setOrderQuotedAmount(Double orderQuotedAmount) {
        this.orderQuotedAmount = orderQuotedAmount;
    }

    public Double getOrderQuotedPrepay() {
        return orderQuotedPrepay;
    }

    public void setOrderQuotedPrepay(Double orderQuotedPrepay) {
        this.orderQuotedPrepay = orderQuotedPrepay;
    }

    public Integer getTransportFareState() {
        return transportFareState;
    }

    public void setTransportFareState(Integer transportFareState) {
        this.transportFareState = transportFareState;
    }

    public Double getTransportQuote() {
        return transportQuote;
    }

    public void setTransportQuote(Double transportQuote) {
        this.transportQuote = transportQuote;
    }

    public Double getTransportQuotePrepay() {
        return transportQuotePrepay;
    }

    public void setTransportQuotePrepay(Double transportQuotePrepay) {
        this.transportQuotePrepay = transportQuotePrepay;
    }

    public Double getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(Double finalPayment) {
        this.finalPayment = finalPayment;
    }

    public Double getFinalAdvancePayment() {
        return finalAdvancePayment;
    }

    public void setFinalAdvancePayment(Double finalAdvancePayment) {
        this.finalAdvancePayment = finalAdvancePayment;
    }

    public Integer getWaitSignIn() {
        return waitSignIn;
    }

    public void setWaitSignIn(Integer waitSignIn) {
        this.waitSignIn = waitSignIn;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}
