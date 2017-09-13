package com.cy.driver.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单分页列表信息（司机3.4版本）
 * Created by wyh on 2016/4/20.
 */
public class OrderPageInfoBO implements Serializable {
    private static final long serialVersionUID = 753499203351146350L;

    /** 订单id */
    private Long orderId;

    /** 订单性质（1普通订单、2分包订单） */
    private Byte transactionKind;

    /** 公司名称 */
    private String companyName;

    /** 认证状态（0未认证 3已认证） */
    private Byte authState;

    /** 联系电话（手机号码或者是固定电话） */
    private String contactPhone;

    /**订单创建人id */
    private String createUserId;

    /** 起始地(省-市-县) */
    private String startAddress;

    /** 目的地(省-市-县) */
    private String endAddress;

    /** 要求装货时间(yyyy-mm-dd) */
    private String requestStartTime;

    /** 要求卸货时间(yyyy-mm-dd) */
    private String requestEndTime;

    /** 货物名称 */
    private String cargoName;

    /** 重量(单位：吨) */
    private String weight;

    /** 体积(单位：方) */
    private String volume;

    /** 要求的车长(单位：米) */
    private String carLength;

    /** 车辆类型名称 */
    private String vehicleTypeName;

    /** 车厢类型名称 */
    private String carriageTypeName;

    /** 我的报价(带单位：元/车、元/吨、元/方) */
    private String myQuote;

    /** 运费（数字，不带单位） */
    private BigDecimal totalFare;

    /** 预付款（数字，不带单位） */
    private BigDecimal prepayFare;

    /**
     * 订单状态编号
     * -3冻结、-2冻结、-1订单已取消
     * 1待承运、3待装货、4待卸货、6已完成、7待评价
     */
    private Integer orderStatusCode;

    /** 订单状态名称 */
    private String orderStatusName;

    /** 是否需要司机同意或者拒绝（0不需要、1需要） */
    private Integer haveAudit;

    /** 确定承运是否需要签署协议（0否、1是） */
    private Integer haveCarrierSign;

    /** 提示名称（按钮旁边的红色字体） */
    private String promptName;

    /** 是否显示确认收款按钮 1显示 0不显示 */
    private String isShowProceeds;

    /**油卡费用*/
    private BigDecimal oilCard;

    /**现金支付*/
    private BigDecimal cash;

    /**未收的油卡费用*/
    private BigDecimal needReceiveOilCard;

    /**未收的现金费用*/
    private BigDecimal needReceiveCash;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Byte getTransactionKind() {
        return transactionKind;
    }

    public void setTransactionKind(Byte transactionKind) {
        this.transactionKind = transactionKind;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Byte getAuthState() {
        return authState;
    }

    public void setAuthState(Byte authState) {
        this.authState = authState;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getCarriageTypeName() {
        return carriageTypeName;
    }

    public void setCarriageTypeName(String carriageTypeName) {
        this.carriageTypeName = carriageTypeName;
    }

    public String getMyQuote() {
        return myQuote;
    }

    public void setMyQuote(String myQuote) {
        this.myQuote = myQuote;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public BigDecimal getPrepayFare() {
        return prepayFare;
    }

    public void setPrepayFare(BigDecimal prepayFare) {
        this.prepayFare = prepayFare;
    }

    public Integer getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(Integer orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Integer getHaveAudit() {
        return haveAudit;
    }

    public void setHaveAudit(Integer haveAudit) {
        this.haveAudit = haveAudit;
    }

    public Integer getHaveCarrierSign() {
        return haveCarrierSign;
    }

    public void setHaveCarrierSign(Integer haveCarrierSign) {
        this.haveCarrierSign = haveCarrierSign;
    }

    public String getPromptName() {
        return promptName;
    }

    public void setPromptName(String promptName) {
        this.promptName = promptName;
    }

    public String getIsShowProceeds() {
        return isShowProceeds;
    }

    public void setIsShowProceeds(String isShowProceeds) {
        this.isShowProceeds = isShowProceeds;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public BigDecimal getOilCard() {
        return oilCard;
    }

    public void setOilCard(BigDecimal oilCard) {
        this.oilCard = oilCard;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getNeedReceiveCash() {
        return needReceiveCash;
    }

    public void setNeedReceiveCash(BigDecimal needReceiveCash) {
        this.needReceiveCash = needReceiveCash;
    }

    public BigDecimal getNeedReceiveOilCard() {
        return needReceiveOilCard;
    }

    public void setNeedReceiveOilCard(BigDecimal needReceiveOilCard) {
        this.needReceiveOilCard = needReceiveOilCard;
    }
}
