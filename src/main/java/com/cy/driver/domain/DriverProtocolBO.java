package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 司机签订协议参数
 * Created by Administrator on 2015/9/13.
 */
public class DriverProtocolBO implements Serializable {
    private static final long serialVersionUID = 7202097991330641024L;

    private String orderNo;//订单号
    private String deliveryTime;//发货时间
    private String secondName;//乙方
    private String startAdd;//装货地w
    private String endAdd;//卸货地
    private String cargoName;//货物名称
    private String weight;//重量
    private String volume;//体积
    private String carCost;//租车费用
    private String advanceCost;//预付款
    private String driverCard;//司机身份证号码
    private String mobilePhone;//手机
    private String carNumber;//车牌号码
    private String receivedAccount;//指定收款帐户
    private String deputePerson;//代开发票委托人
    private String cash;//代开发票委托人
    private String oilCard;//油卡费用

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getStartAdd() {
        return startAdd;
    }

    public void setStartAdd(String startAdd) {
        this.startAdd = startAdd;
    }

    public String getEndAdd() {
        return endAdd;
    }

    public void setEndAdd(String endAdd) {
        this.endAdd = endAdd;
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

    public String getCarCost() {
        return carCost;
    }

    public void setCarCost(String carCost) {
        this.carCost = carCost;
    }

    public String getAdvanceCost() {
        return advanceCost;
    }

    public void setAdvanceCost(String advanceCost) {
        this.advanceCost = advanceCost;
    }

    public String getDriverCard() {
        return driverCard;
    }

    public void setDriverCard(String driverCard) {
        this.driverCard = driverCard;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getReceivedAccount() {
        return receivedAccount;
    }

    public void setReceivedAccount(String receivedAccount) {
        this.receivedAccount = receivedAccount;
    }

    public String getDeputePerson() {
        return deputePerson;
    }

    public void setDeputePerson(String deputePerson) {
        this.deputePerson = deputePerson;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getOilCard() {
        return oilCard;
    }

    public void setOilCard(String oilCard) {
        this.oilCard = oilCard;
    }
}
