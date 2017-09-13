package com.cy.driver.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 车辆信息类
 * Created by mr on 2015/7/4.
 */
public class CarInfoBO implements Serializable{
    private static final long serialVersionUID = -2665242128351797252L;
    /*车牌号*/
    private String carNumber;
    /** 车长 */
    private String carLength;
    /** 运力-吨位 */
    private String carLoad;
    /** 运力-体积 */
    private String carVolume;
    /** 车型（3.0新增） */
    private String carTypeCode;
    /** 车辆类型二级(3.0新增) */
    private String carTypeChildCode;
    /** 车厢类型(3.0新增) */
    private String carriageTypeCode;
    /** 车厢类型二级(3.0新增) */
    private String carriageTypeChildCode;
    /** 车辆图片*/
    private String carPhoto;

    /** 车长（3.4新增） */
    private String carLengthValue;

    /** 车长code（3.4新增） */
    private BigDecimal carLengthCode;

    /** 车型（3.4新增） */
    private String carVehicleType;

    /** 车型编号（3.4新增） */
    private String carVehicleTypeCode;

    /** 车厢（3.4新增） */
    private String carCarriageType;

    /** 车（3.4新增） */
    private String CarCarriageTypeCode;

    /**添加的空车上报数量(3.5.1新增)*/
    private String emptyCarAmount;

    public String getCarLengthValue() {
        return carLengthValue;
    }

    public void setCarLengthValue(String carLengthValue) {
        this.carLengthValue = carLengthValue;
    }

    public BigDecimal getCarLengthCode() {
        return carLengthCode;
    }

    public void setCarLengthCode(BigDecimal carLengthCode) {
        this.carLengthCode = carLengthCode;
    }

    public String getCarVehicleType() {
        return carVehicleType;
    }

    public void setCarVehicleType(String carVehicleType) {
        this.carVehicleType = carVehicleType;
    }

    public String getCarVehicleTypeCode() {
        return carVehicleTypeCode;
    }

    public void setCarVehicleTypeCode(String carVehicleTypeCode) {
        this.carVehicleTypeCode = carVehicleTypeCode;
    }

    public String getCarCarriageType() {
        return carCarriageType;
    }

    public void setCarCarriageType(String carCarriageType) {
        this.carCarriageType = carCarriageType;
    }

    public String getCarCarriageTypeCode() {
        return CarCarriageTypeCode;
    }

    public void setCarCarriageTypeCode(String carCarriageTypeCode) {
        CarCarriageTypeCode = carCarriageTypeCode;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getCarLoad() {
        return carLoad;
    }

    public void setCarLoad(String carLoad) {
        this.carLoad = carLoad;
    }

    public String getCarVolume() {
        return carVolume;
    }

    public void setCarVolume(String carVolume) {
        this.carVolume = carVolume;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getCarTypeChildCode() {
        return carTypeChildCode;
    }

    public void setCarTypeChildCode(String carTypeChildCode) {
        this.carTypeChildCode = carTypeChildCode;
    }

    public String getCarriageTypeCode() {
        return carriageTypeCode;
    }

    public void setCarriageTypeCode(String carriageTypeCode) {
        this.carriageTypeCode = carriageTypeCode;
    }

    public String getCarriageTypeChildCode() {
        return carriageTypeChildCode;
    }

    public void setCarriageTypeChildCode(String carriageTypeChildCode) {
        this.carriageTypeChildCode = carriageTypeChildCode;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getEmptyCarAmount() {
        return emptyCarAmount;
    }

    public void setEmptyCarAmount(String emptyCarAmount) {
        this.emptyCarAmount = emptyCarAmount;
    }
}
