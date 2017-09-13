package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/27.
 * 订单详情
 */
public class OrderInfoDetailBO implements Serializable {

    private static final long serialVersionUID = 2167880000805728277L;

    private Long orderId;//订单id
    private String  orderNo;//订单号
    private Byte  companyAuthStatus;//企业认证状态
    private String companyName;//公司名称
    private String consignorName;//发货人姓名
    private String consignorMobile;//发货人手机号码
    private Integer  orderStatusCode;//订单状态编号(-3货主提出取消订单、-2司机提出取消订单、-1订单已取消、1货主已订车、3等待装货、4等待卸货、5已卸货、6已完成)
    private String  orderStatusName;//订单状态名称
    private String  startAddress;//起始地
    private String endAddress;//目的地
    private String requestStartTime;//要求装货时间 (yyyy-mm-dd hh:mm)
    private String requestEndTime;//要求卸货时间 (yyyy-mm-dd hh:mm)
    private String  cargoName;//货物名称
    private String  weight;//重量(单位：吨)
    private String volume;//体积(单位：方)
    private String  carLength;//要求的车长(单位：米)
    private String vehicleTypeName;//车辆类型名称
    private String carriageTypeName	;//车厢类型名称
    private String bidPrice;//成交价格(单位：元)
    private String cargoRemark;//货源备注
    private String pubTime;//发布时间 (yyyy-mm-dd hh:mm:ss)
    private Integer invoiceNum;//发货单上传数量(从0开始)
    private Integer receiptNum;//回单上传数量(从0开始)
    private Byte driverAssessIdent;//评价标识(0未评价、1已评价)
    private Byte orderLock;//订单锁定状态(0正常、1司机发起订单锁定、2货主发起订单锁定)
    private String actualStartTime;//实际装货时间  (yyyy-mm-dd hh:mm:ss)

    private String totalFare;//运费
    private String prepayFare;//预付
    private String payeeAmount;//收款金额
    private String isSignProtocol;//是否签署运输协议 1签署 0 不签署
    private String isShowProceeds;//是否显示确认收款按钮 1显示 0不显示


    /** 确定承运是否需要签署协议（0否、1是） */
    private Integer haveCarrierSign;

    /** 取消原因 */
    private String cancelCause;

    private String oilCard;//油卡费用

    private String needReceiceOilCard;//未收的油卡费用

    private String cash;//现金费用

    private String needReceiveCash;//未收的现金费用

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

    public String getPayeeAmount() {
        return payeeAmount;
    }

    public void setPayeeAmount(String payeeAmount) {
        this.payeeAmount = payeeAmount;
    }

    public String getIsSignProtocol() {
        return isSignProtocol;
    }

    public void setIsSignProtocol(String isSignProtocol) {
        this.isSignProtocol = isSignProtocol;
    }

    public String getIsShowProceeds() {
        return isShowProceeds;
    }

    public void setIsShowProceeds(String isShowProceeds) {
        this.isShowProceeds = isShowProceeds;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Byte getCompanyAuthStatus() {
        return companyAuthStatus;
    }

    public void setCompanyAuthStatus(Byte companyAuthStatus) {
        this.companyAuthStatus = companyAuthStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getConsignorMobile() {
        return consignorMobile;
    }

    public void setConsignorMobile(String consignorMobile) {
        this.consignorMobile = consignorMobile;
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

    public String getCargoRemark() {
        return cargoRemark;
    }

    public void setCargoRemark(String cargoRemark) {
        this.cargoRemark = cargoRemark;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
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

    public Byte getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(Byte orderLock) {
        this.orderLock = orderLock;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Integer getHaveCarrierSign() {
        return haveCarrierSign;
    }

    public void setHaveCarrierSign(Integer haveCarrierSign) {
        this.haveCarrierSign = haveCarrierSign;
    }

    public String getCancelCause() {
        return cancelCause;
    }

    public void setCancelCause(String cancelCause) {
        this.cancelCause = cancelCause;
    }

    public String getNeedReceiceOilCard() {
        return needReceiceOilCard;
    }

    public void setNeedReceiceOilCard(String needReceiceOilCard) {
        this.needReceiceOilCard = needReceiceOilCard;
    }

    public String getOilCard() {
        return oilCard;
    }

    public void setOilCard(String oilCard) {
        this.oilCard = oilCard;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getNeedReceiveCash() {
        return needReceiveCash;
    }

    public void setNeedReceiveCash(String needReceiveCash) {
        this.needReceiveCash = needReceiveCash;
    }
}
