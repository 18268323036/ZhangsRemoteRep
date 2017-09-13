package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 订单信息
 * Created by wyh on 2015/7/25.
 */
public class OrderInfoBO implements Serializable {
    private static final long serialVersionUID = 6286037962773812530L;

    private String orderId;//订单id
    private String companyName;//公司名称
    private Integer orderStatusCode;//订单状态编号(-1订单已取消、1货主已订车、3等待装货、4等待卸货、5已卸货、6已完成)
    private String orderStatusName;//订单状态名称
    private String startAddress;//起始地
    private String endAddress;//目的地
    private String requestStartTime;//要求装货时间(yyyy-mm-dd hh:mm)
    private String requestEndTime;//要求卸货时间(yyyy-mm-dd hh:mm)
    private String cargoName;//货物名称
    private String weight;//重量(单位：吨)
    private String volume;//体积(单位：方)
    private String carLength;//要求的车长(单位：米)
    private String vehicleTypeName;//车辆类型名称
    private String carriageTypeName;//车厢类型名称
    private String bidPrice;//成交价格(单位：元)
    private String myQuote;//我的报价(带单位：元/车、元/吨、元/方)
    private Integer invoiceNum;//发货单上传数量(从0开始)
    private Integer receiptNum;//回单上传数量(从0开始)
    private Integer driverAssessIdent;//评价标识(0未评价、1已评价)
    private Integer orderLock;//订单锁定状态(0正常、1司机发起订单锁定、2货主发起订单锁定)
    private String totalFare;//运费
    private String prepayFare;//预付款

    /** 确定承运是否需要签署协议（0否、1是） */
    private Integer haveCarrierSign;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
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

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getMyQuote() {
        return myQuote;
    }

    public void setMyQuote(String myQuote) {
        this.myQuote = myQuote;
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

    public Integer getDriverAssessIdent() {
        return driverAssessIdent;
    }

    public void setDriverAssessIdent(Integer driverAssessIdent) {
        this.driverAssessIdent = driverAssessIdent;
    }

    public Integer getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(Integer orderLock) {
        this.orderLock = orderLock;
    }

    public Integer getHaveCarrierSign() {
        return haveCarrierSign;
    }

    public void setHaveCarrierSign(Integer haveCarrierSign) {
        this.haveCarrierSign = haveCarrierSign;
    }
}
