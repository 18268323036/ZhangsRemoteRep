package com.cy.driver.domain;

import java.math.BigDecimal;

/**
 * 订单详情（司机3.4版本）
 * Created by wyh on 2016/4/23.
 */
public class OrderDetailsBO extends OrderPageInfoBO {
    private static final long serialVersionUID = -3440564670262818890L;

    /** 订单编号 */
    private String orderNo;

    /** 订单创建时间 */
    private String orderCreateTime;

    /** 货源的发布时间 */
    private String cargoCreateTime;

    /** 货源备注 */
    private String cargoRemark;

    /** 发货单上传数量 */
    private Integer invoiceNum;

    /** 回单上传数量 */
    private Integer receiptNum;

    /** 评价标识(0未评价、1已评价) */
    private Byte driverAssessIdent;

    /** 是否签署运输协议 1签署 0 不签署 */
    private String isSignProtocol;

    /** 收款金额（数字，不带单位） */
    private BigDecimal payeeAmount;

    /** 取消原因 */
    private String cancelCause;

    /** 联系人 */
    private String contactName;

    /** 交易数量 */
    private Integer transactionNum;

    /** 货主头像 */
    private String ownerHeadImg;

    /** 支付时间 */
    private String payTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getCargoCreateTime() {
        return cargoCreateTime;
    }

    public void setCargoCreateTime(String cargoCreateTime) {
        this.cargoCreateTime = cargoCreateTime;
    }

    public String getCargoRemark() {
        return cargoRemark;
    }

    public void setCargoRemark(String cargoRemark) {
        this.cargoRemark = cargoRemark;
    }

    public Integer getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(Integer invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Integer getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(Integer receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Byte getDriverAssessIdent() {
        return driverAssessIdent;
    }

    public void setDriverAssessIdent(Byte driverAssessIdent) {
        this.driverAssessIdent = driverAssessIdent;
    }

    public String getIsSignProtocol() {
        return isSignProtocol;
    }

    public void setIsSignProtocol(String isSignProtocol) {
        this.isSignProtocol = isSignProtocol;
    }

    public BigDecimal getPayeeAmount() {
        return payeeAmount;
    }

    public void setPayeeAmount(BigDecimal payeeAmount) {
        this.payeeAmount = payeeAmount;
    }

    public String getCancelCause() {
        return cancelCause;
    }

    public void setCancelCause(String cancelCause) {
        this.cancelCause = cancelCause;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
