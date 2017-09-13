package com.cy.driver.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单评价详情（司机3.4版本）
 * Created by wyh on 2016/4/23.
 */
public class OrderEvalDetailsBO implements Serializable {
    private static final long serialVersionUID = -3856476358399788954L;

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

    /** 联系人 */
    private String contactName;

    /** 交易数量 */
    private Integer transactionNum;

    /** 货主头像 */
    private String ownerHeadImg;

    /** 起始地(省-市-县) */
    private String startAddress;

    /** 目的地(省-市-县) */
    private String endAddress;

    /** 要求装货时间(yyyy-mm-dd) */
    private String requestStartTime;

    /** 要求卸货时间(yyyy-mm-dd) */
    private String requestEndTime;

    /** 运费（数字，不带单位） */
    private BigDecimal totalFare;

    /** 预付款（数字，不带单位） */
    private BigDecimal prepayFare;

    /** 评价标识(0未评价、1已评价) */
    private Byte driverAssessIdent;

    /** 评价分数 3 好评、6 中评、9差评 */
    private Integer eval;

    /** 评价内容 */
    private String evalContent;

    /**油卡费用*/
    private String oilCard;

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(Integer transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getOwnerHeadImg() {
        return ownerHeadImg;
    }

    public void setOwnerHeadImg(String ownerHeadImg) {
        this.ownerHeadImg = ownerHeadImg;
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

    public Byte getDriverAssessIdent() {
        return driverAssessIdent;
    }

    public void setDriverAssessIdent(Byte driverAssessIdent) {
        this.driverAssessIdent = driverAssessIdent;
    }

    public Integer getEval() {
        return eval;
    }

    public void setEval(Integer eval) {
        this.eval = eval;
    }

    public String getEvalContent() {
        return evalContent;
    }

    public void setEvalContent(String evalContent) {
        this.evalContent = evalContent;
    }

    public String getOilCard() {
        return oilCard;
    }

    public void setOilCard(String oilCard) {
        this.oilCard = oilCard;
    }
}
