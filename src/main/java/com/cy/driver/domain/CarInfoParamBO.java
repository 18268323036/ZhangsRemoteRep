package com.cy.driver.domain;

import java.io.Serializable;

/**
 * @author yanst 2016/4/18 14:28
 */
public class CarInfoParamBO implements Serializable {
    private static final long serialVersionUID = -8069926214862123020L;

    /** 车牌号 */
    private String carNumber;

    /** 载重 */
    private String carLoad;

    /** 体积 */
    private String carVolume;

    /** 车长code */
    private String carLengthCode;

    /** 车辆类型code  */
    private String carTypeCode;

    /** 车厢类型Code  */
    private String carriageTypeCode;

    /** 车辆照片 */
    private String carPhoto;

    /** 常跑城市，字符串，以”,”分隔 */
    private String oftenRunCitys;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
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

    public String getCarLengthCode() {
        return carLengthCode;
    }

    public void setCarLengthCode(String carLengthCode) {
        this.carLengthCode = carLengthCode;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getCarriageTypeCode() {
        return carriageTypeCode;
    }

    public void setCarriageTypeCode(String carriageTypeCode) {
        this.carriageTypeCode = carriageTypeCode;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getOftenRunCitys() {
        return oftenRunCitys;
    }

    public void setOftenRunCitys(String oftenRunCitys) {
        this.oftenRunCitys = oftenRunCitys;
    }
}
