package com.cy.driver.api.domain.res;

import java.io.Serializable;
import java.util.List;

/**
 * 运单/订单详情
 *
 * @author zhangxy 2016/7/29 14:30
 */
public class WayBillDetail implements Serializable {

    private static final long serialVersionUID = 671113522815925455L;
    private String orderId;//运单Id
    private String waybillNum;//运单编号
    private Integer orderLock;//订单是否冻结(0正常 1订单已锁定)
    private Integer cancleSource;//订单取消来源（0正常、1快到网司机、2快到网货主、4系统取消、6区域配送用户、7重新派单）
    private String cancleCause;//订单取消原因
    private Long createUserId;//创建人id
    private Integer createUserType;//创建人类型
    private Integer turnedState;//转单状态（0未转单、1已转单）
    private String turnedWaybillNum;//转单运单id
    private String turnedWaybillTime;//转单时间
    private String createTime;//运单创建时间(yyyy-MM-dd hh-mm)
    private String companyName;//货主所在企业名
    private String ownerName;//货主姓名
    private Integer transactionNumber;//货主累计交易数
    private String photosAddress;//创建人头像
    private String consignorAddress;//装货地(省市区)
    private String startTime;//装货时间
    private String consignorName;//装货地联系人
    private String consignorMobilePhone;//装货地联系人电话
    private String consignorLongitude;//装货地经度
    private String consignorLatitude;//装货地纬度
    private String consigneeAddress;//卸货地(省市区)
    private String endTime;//卸货时间
    private String consigneeName;//卸货地联系人
    private String consigneeMobilePhone;//卸货地联系人电话
    private String consigneeLongitude;//卸货地经度
    private String consigneeLatitude;//卸货地纬度
    private Integer fromCarrierCount;//来自几笔托单
    private String carrierAmountNum;//托单货物总件数
    private String carrierAmountWeight;//托单总重量
    private String carrierAmountVolume;//托单总体积
    private String mark;//备注
    private Integer isComment;//承运人是否已经评价
    private String carLength;// 车长
    private Integer orderStatusCode;//状态编号
    private String orderStatusName;//状态名(1 待接单,2 待装货,3 待卸货,4 已卸货,5 交易已取消,6 已转单，7已完成，8已冻结)
    private String orderPhone;//货主联系电话
    private Double orderQuotedAmount;//货主报价
    private Double orderQuotedPrepay;//货主报价预付款
    private Integer transportFareState;//司机的报价状态(-2货主不采纳承运方的报价、0承运方未报价、1承运方已报价、2货主已采纳承运方的报价)
    private Double transportQuote;//司机报价
    private Double transportQuotePrepay;//司机报价预付款
    private Double finalPayment;//确认后的最终运费价格
    private Double finalAdvancePayment;//确认后的最终预付款
    private Double totalCollectionPayment;//代收货款
    private String waitSignIn;//待签收件数
    private String siteCode;//平台编码
    private String orderSource;//订单来源（1来源快到网、2来源云配送\3saas）
    private List<CarrierDetail> carrierDetail;//托单详情

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhotosAddress() {
        return photosAddress;
    }

    public void setPhotosAddress(String photosAddress) {
        this.photosAddress = photosAddress;
    }

    public String getWaybillNum() {
        return waybillNum;
    }

    public void setWaybillNum(String waybillNum) {
        this.waybillNum = waybillNum;
    }

    public Integer getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(Integer orderLock) {
        this.orderLock = orderLock;
    }

    public Integer getCancleSource() {
        return cancleSource;
    }

    public void setCancleSource(Integer cancleSource) {
        this.cancleSource = cancleSource;
    }

    public Integer getTurnedState() {
        return turnedState;
    }

    public void setTurnedState(Integer turnedState) {
        this.turnedState = turnedState;
    }

    public String getTurnedWaybillNum() {
        return turnedWaybillNum;
    }

    public void setTurnedWaybillNum(String turnedWaybillNum) {
        this.turnedWaybillNum = turnedWaybillNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(Integer transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getConsignorAddress() {
        return consignorAddress;
    }

    public void setConsignorAddress(String consignorAddress) {
        this.consignorAddress = consignorAddress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getConsignorMobilePhone() {
        return consignorMobilePhone;
    }

    public void setConsignorMobilePhone(String consignorMobilePhone) {
        this.consignorMobilePhone = consignorMobilePhone;
    }

    public String getConsignorLongitude() {
        return consignorLongitude;
    }

    public void setConsignorLongitude(String consignorLongitude) {
        this.consignorLongitude = consignorLongitude;
    }

    public String getConsignorLatitude() {
        return consignorLatitude;
    }

    public void setConsignorLatitude(String consignorLatitude) {
        this.consignorLatitude = consignorLatitude;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getConsigneeLongitude() {
        return consigneeLongitude;
    }

    public void setConsigneeLongitude(String consigneeLongitude) {
        this.consigneeLongitude = consigneeLongitude;
    }

    public String getConsigneeLatitude() {
        return consigneeLatitude;
    }

    public void setConsigneeLatitude(String consigneeLatitude) {
        this.consigneeLatitude = consigneeLatitude;
    }

    public Integer getFromCarrierCount() {
        return fromCarrierCount;
    }

    public void setFromCarrierCount(Integer fromCarrierCount) {
        this.fromCarrierCount = fromCarrierCount;
    }

    public String getCarrierAmountNum() {
        return carrierAmountNum;
    }

    public void setCarrierAmountNum(String carrierAmountNum) {
        this.carrierAmountNum = carrierAmountNum;
    }

    public String getWaitSignIn() {
        return waitSignIn;
    }

    public void setWaitSignIn(String waitSignIn) {
        this.waitSignIn = waitSignIn;
    }

    public String getCarrierAmountWeight() {
        return carrierAmountWeight;
    }

    public void setCarrierAmountWeight(String carrierAmountWeight) {
        this.carrierAmountWeight = carrierAmountWeight;
    }

    public String getCarrierAmountVolume() {
        return carrierAmountVolume;
    }

    public void setCarrierAmountVolume(String carrierAmountVolume) {
        this.carrierAmountVolume = carrierAmountVolume;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public List<CarrierDetail> getCarrierDetail() {
        return carrierDetail;
    }

    public void setCarrierDetail(List<CarrierDetail> carrierDetail) {
        this.carrierDetail = carrierDetail;
    }


    public String getTurnedWaybillTime() {
        return turnedWaybillTime;
    }

    public void setTurnedWaybillTime(String turnedWaybillTime) {
        this.turnedWaybillTime = turnedWaybillTime;
    }


    public String getCancleCause() {
        return cancleCause;
    }

    public void setCancleCause(String cancleCause) {
        this.cancleCause = cancleCause;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Integer getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(Integer orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public Double getTotalCollectionPayment() {
        return totalCollectionPayment;
    }

    public void setTotalCollectionPayment(Double totalCollectionPayment) {
        this.totalCollectionPayment = totalCollectionPayment;
    }

    public Double getFinalAdvancePayment() {
        return finalAdvancePayment;
    }

    public void setFinalAdvancePayment(Double finalAdvancePayment) {
        this.finalAdvancePayment = finalAdvancePayment;
    }

    public Double getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(Double finalPayment) {
        this.finalPayment = finalPayment;
    }

    public Double getTransportQuotePrepay() {
        return transportQuotePrepay;
    }

    public void setTransportQuotePrepay(Double transportQuotePrepay) {
        this.transportQuotePrepay = transportQuotePrepay;
    }

    public Double getTransportQuote() {
        return transportQuote;
    }

    public void setTransportQuote(Double transportQuote) {
        this.transportQuote = transportQuote;
    }

    public Integer getTransportFareState() {
        return transportFareState;
    }

    public void setTransportFareState(Integer transportFareState) {
        this.transportFareState = transportFareState;
    }

    public Double getOrderQuotedPrepay() {
        return orderQuotedPrepay;
    }

    public void setOrderQuotedPrepay(Double orderQuotedPrepay) {
        this.orderQuotedPrepay = orderQuotedPrepay;
    }

    public Double getOrderQuotedAmount() {
        return orderQuotedAmount;
    }

    public void setOrderQuotedAmount(Double orderQuotedAmount) {
        this.orderQuotedAmount = orderQuotedAmount;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }
}
