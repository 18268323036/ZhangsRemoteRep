package com.cy.driver.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 司机和分包商签署的协议
 * Created by wyh on 2016/1/14.
 */
public class ScdProtocolBO implements Serializable {
    private static final long serialVersionUID = 1749996210233405340L;

    /** 订单号 */
    private String orderNumber;

    /** 发货时间 */
    private String deliveryTime;

    /** 甲方 */
    private String firstName;

    /** 身份证 */
    private String cardNumber;

    /** 乙方 */
    private String secondName;

    /** 装货地 */
    private String startAddress;

    /** 卸货地 */
    private String endAddress;

    /** 货物名称 */
    private String cargoName;

    /** 重量 */
    private String cargoWeight;

    /** 体积 */
    private String cargoCubage;

    /** 租车费用 */
    private BigDecimal totalFare;

    /** 预付款 */
    private BigDecimal prepayFare;

    /** 司机身份证号 */
    private String driverCardNumber;

    /** 司机手机 */
    private String driverMobilePhone;

    /** 车牌号 */
    private String carNumber;

    /** 指定收款账户 */
    private String paymentAccount;

    /** 代开发票委托人 */
    private String entrustPerson;

    /** 总车辆费用分润模式：1 全部支付给分包商 2 按信息费和订单中的车辆分别支付给分包商和司机 */
    private Integer totalFareAllotModel;

    /** 文档路径 */
    private String docUrl;

    /** 订单id */
    private Long orderId;

    /** 给司机的油卡费用*/
    private String oilCard;

    /** 给司机的现金费用*/
    private String cash;

    public String getOrderNumber() {
        if (orderNumber == null) {
            return "";
        }
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDeliveryTime() {
        if (deliveryTime == null) {
            return "";
        }
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getFirstName() {
        if (firstName == null) {
            return "";
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCardNumber() {
        if (cardNumber == null) {
            return "";
        }
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecondName() {
        if (secondName == null) {
            return "";
        }
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getStartAddress() {
        if (startAddress == null) {
            return "";
        }
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        if (endAddress == null) {
            return "";
        }
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getCargoName() {
        if (cargoName == null) {
            return "";
        }
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoCubage() {
        return cargoCubage;
    }

    public void setCargoCubage(String cargoCubage) {
        this.cargoCubage = cargoCubage;
    }

    public String getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(String cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public BigDecimal getTotalFare() {
        if (totalFare == null) {
            totalFare = BigDecimal.ZERO;
        }
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public BigDecimal getPrepayFare() {
        if (prepayFare == null) {
            prepayFare = BigDecimal.ZERO;
        }
        return prepayFare;
    }

    public void setPrepayFare(BigDecimal prepayFare) {
        this.prepayFare = prepayFare;
    }

    public String getDriverCardNumber() {
        if (driverCardNumber == null) {
            return "";
        }
        return driverCardNumber;
    }

    public void setDriverCardNumber(String driverCardNumber) {
        this.driverCardNumber = driverCardNumber;
    }

    public String getDriverMobilePhone() {
        if (driverMobilePhone == null) {
            return "";
        }
        return driverMobilePhone;
    }

    public void setDriverMobilePhone(String driverMobilePhone) {
        this.driverMobilePhone = driverMobilePhone;
    }

    public String getCarNumber() {
        if (carNumber == null) {
            return "";
        }
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPaymentAccount() {
        if (paymentAccount == null) {
            return "";
        }
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getEntrustPerson() {
        if (entrustPerson == null) {
            return "";
        }
        return entrustPerson;
    }

    public void setEntrustPerson(String entrustPerson) {
        this.entrustPerson = entrustPerson;
    }

    public Integer getTotalFareAllotModel() {
        return totalFareAllotModel;
    }

    public void setTotalFareAllotModel(Integer totalFareAllotModel) {
        this.totalFareAllotModel = totalFareAllotModel;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
