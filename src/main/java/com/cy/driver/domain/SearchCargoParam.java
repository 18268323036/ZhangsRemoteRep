package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 搜索货源
 * @author yanst 2016/4/19 11:43
 */
public class SearchCargoParam implements Serializable {
    private static final long serialVersionUID = -2047822261512285536L;

    //页码
    private int page;

    //装货时间
    private String sTime;

    //装货地省code
    private String sProCode;

    //装货地省value
    private String sProValue;

    //装货地市code
    private String sCityCode;

    //装货地市value
    private String sCityValue;

    //装货地区code
    private String sCountyCode;

    //装货地区value
    private String sCountyValue;

    //卸货地省code
    private String eProCode;

    //卸货地省value
    private String eProValue;

    //卸货地市code
    private String eCityCode;

    //卸货地市value
    private String eCityValue;

    //卸货地区code
    private String eCountyCode;

    //卸货地区value
    private String eCountyValue;

    //车辆类型一级编号
    private String vehicleType;

    //车辆类型二级编号
    private String vehicleTypeTwo;

    //车厢类型一级编号
    private String carriageType;

    //车厢类型二级编号
    private String carriageTypeTwo;

    //车长
    private String carLength;

    //载重
    private String weight;

    //体积
    private String volume;

    //排序(0默认排序、1认证排序、2车长升序、3车长降序、4重量升序、5重量降序)
    private Integer sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsProCode() {
        return sProCode;
    }

    public void setsProCode(String sProCode) {
        this.sProCode = sProCode;
    }

    public String getsProValue() {
        return sProValue;
    }

    public void setsProValue(String sProValue) {
        this.sProValue = sProValue;
    }

    public String getsCityCode() {
        return sCityCode;
    }

    public void setsCityCode(String sCityCode) {
        this.sCityCode = sCityCode;
    }

    public String getsCityValue() {
        return sCityValue;
    }

    public void setsCityValue(String sCityValue) {
        this.sCityValue = sCityValue;
    }

    public String getsCountyCode() {
        return sCountyCode;
    }

    public void setsCountyCode(String sCountyCode) {
        this.sCountyCode = sCountyCode;
    }

    public String getsCountyValue() {
        return sCountyValue;
    }

    public void setsCountyValue(String sCountyValue) {
        this.sCountyValue = sCountyValue;
    }

    public String geteProCode() {
        return eProCode;
    }

    public void seteProCode(String eProCode) {
        this.eProCode = eProCode;
    }

    public String geteProValue() {
        return eProValue;
    }

    public void seteProValue(String eProValue) {
        this.eProValue = eProValue;
    }

    public String geteCityCode() {
        return eCityCode;
    }

    public void seteCityCode(String eCityCode) {
        this.eCityCode = eCityCode;
    }

    public String geteCityValue() {
        return eCityValue;
    }

    public void seteCityValue(String eCityValue) {
        this.eCityValue = eCityValue;
    }

    public String geteCountyCode() {
        return eCountyCode;
    }

    public void seteCountyCode(String eCountyCode) {
        this.eCountyCode = eCountyCode;
    }

    public String geteCountyValue() {
        return eCountyValue;
    }

    public void seteCountyValue(String eCountyValue) {
        this.eCountyValue = eCountyValue;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleTypeTwo() {
        return vehicleTypeTwo;
    }

    public void setVehicleTypeTwo(String vehicleTypeTwo) {
        this.vehicleTypeTwo = vehicleTypeTwo;
    }

    public String getCarriageType() {
        return carriageType;
    }

    public void setCarriageType(String carriageType) {
        this.carriageType = carriageType;
    }

    public String getCarriageTypeTwo() {
        return carriageTypeTwo;
    }

    public void setCarriageTypeTwo(String carriageTypeTwo) {
        this.carriageTypeTwo = carriageTypeTwo;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
