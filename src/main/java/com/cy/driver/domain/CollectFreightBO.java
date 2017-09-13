package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/9.
 */
public class CollectFreightBO implements Serializable {
    private static final long serialVersionUID = -8711287735693577966L;
    private String orderId;//订单ID
    private String companyName;//发货公司名称
    private String freightPrice;//	运费总价
    private String needReceiveFair;//待收运费
    private String loadTime;//	装货时间
    private String payTime;//	货主付款时间（按照格式显示）
    private String cargoName;//货物名称
    private String needReceiveOilCard;//待收油卡费用

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

    public String getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(String freightPrice) {
        this.freightPrice = freightPrice;
    }

    public String getNeedReceiveFair() {
        return needReceiveFair;
    }

    public void setNeedReceiveFair(String needReceiveFair) {
        this.needReceiveFair = needReceiveFair;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getNeedReceiveOilCard() {
        return needReceiveOilCard;
    }

    public void setNeedReceiveOilCard(String needReceiveOilCard) {
        this.needReceiveOilCard = needReceiveOilCard;
    }
}
