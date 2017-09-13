package com.cy.driver.api.domain.res;

import java.io.Serializable;
import java.util.List;

/**
 * 托单详情(运单下包着托单)
 * @author zhangxy 2016/6/2 14:05
 */
public class CarrierDetail implements Serializable {
    private static final long serialVersionUID = -840510734136285859L;

    private String carrierId;//托单号
    private String carrierNum;//托单编号
    private String consignorAddress;//托单装货地(省市区)
    private String consignorLongitude;//托单装货地经度
    private String consignorLatitude;//托单装货地纬度
    private String consigneeAddress;//托单卸货地(省市区)
    private String consigneeLongitude;//托单卸货地经度
    private String consigneeLatitude;//托单卸货地纬度
    private String amountNum;//托单总件数
    private String amountWeight;//托单总重量
    private String amountVolume;//托单总体积
    /**
     * 状态(10运单产生、11待派单、12待接单、13待发货、
     * 14已发货/运输中、15已收货/待签收、16已签收/已完成、17已下架/已完成)
     */
    private Integer state;

    private List<WayBillDetailCargoList> cargoDetail;//托单货物列表

    private String companyName;//创建人所在公司名
    private String ownerName;//公司联系人姓名
    private String ownerPhone;//公司联系人电话
    private Integer authedInfo;//公司认证信息
    private String transactionNumber;//累计交易数
    private String cargoName;//货物名称
    private String photosAddress;//创建人头像
    private String consignorName;//发货人姓名
    private String consignorPhone;//发货人联系电话
    private String consigneeName;//收货人姓名
    private String consigneePhone;//收货人联系电话
    private String consigneeTelephone;//收货人固话
    private Integer takeWay;//取件方式（0自提 1送货上门）
    private String collectionPayment;//代收货款
    private String totalFare;//运费总价
    private String prepayFare;//预付款
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrepayFare() {
        return prepayFare;
    }

    public void setPrepayFare(String prepayFare) {
        this.prepayFare = prepayFare;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getCollectionPayment() {
        return collectionPayment;
    }

    public void setCollectionPayment(String collectionPayment) {
        this.collectionPayment = collectionPayment;
    }

    public Integer getTakeWay() {
        return takeWay;
    }

    public void setTakeWay(Integer takeWay) {
        this.takeWay = takeWay;
    }

    public String getConsigneeTelephone() {
        return consigneeTelephone;
    }

    public void setConsigneeTelephone(String consigneeTelephone) {
        this.consigneeTelephone = consigneeTelephone;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsignorPhone() {
        return consignorPhone;
    }

    public void setConsignorPhone(String consignorPhone) {
        this.consignorPhone = consignorPhone;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getPhotosAddress() {
        return photosAddress;
    }

    public void setPhotosAddress(String photosAddress) {
        this.photosAddress = photosAddress;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Integer getAuthedInfo() {
        return authedInfo;
    }

    public void setAuthedInfo(Integer authedInfo) {
        this.authedInfo = authedInfo;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getConsignorAddress() {
        return consignorAddress;
    }

    public void setConsignorAddress(String consignorAddress) {
        this.consignorAddress = consignorAddress;
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

    public String getAmountNum() {
        return amountNum;
    }

    public void setAmountNum(String amountNum) {
        this.amountNum = amountNum;
    }

    public String getAmountWeight() {
        return amountWeight;
    }

    public void setAmountWeight(String amountWeight) {
        this.amountWeight = amountWeight;
    }

    public String getAmountVolume() {
        return amountVolume;
    }

    public void setAmountVolume(String amountVolume) {
        this.amountVolume = amountVolume;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<WayBillDetailCargoList> getCargoDetail() {
        return cargoDetail;
    }

    public void setCargoDetail(List<WayBillDetailCargoList> cargoDetail) {
        this.cargoDetail = cargoDetail;
    }

    public String getCarrierNum() {
        return carrierNum;
    }

    public void setCarrierNum(String carrierNum) {
        this.carrierNum = carrierNum;
    }
}
