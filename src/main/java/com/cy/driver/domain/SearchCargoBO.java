package com.cy.driver.domain;

import java.util.Date;

/**
 * Created by wyh on 2016/3/8.
 */
public class SearchCargoBO {
    /** 司机id */
    private Long driverId;

    /** 装货地-省编号 */
    private String startProvinceCode;

    /** 装货地-省名称 */
    private String startProvinceValue;

    /** 装货地-市编号 */
    private String startCityCode;

    /** 装货地-市名称 */
    private String startCityValue;

    /** 装货地-区编号 */
    private String startCountyCode;

    /** 装货地-区名称 */
    private String startCountyValue;

    /** 卸货地-省编号 */
    private String endProvinceCode;

    /** 卸货地-省名称 */
    private String endProvinceValue;

    /** 卸货地-市编号 */
    private String endCityCode;

    /** 卸货地-市名称 */
    private String endCityValue;

    /** 卸货地-区编号 */
    private String endCountyCode;

    /** 卸货地-区名称 */
    private String endCountyValue;


    /** 车辆类型 */
    private String vehicleType;

    /** 车厢类型 */
    private String carriageType;

    /** 车长 */
    private Double carLength;

    /** 重量 */
    private Double cargoWeight;

    /** 体积 */
    private Double cargoCubage;

    /** 开始时间 */
    private Date startTime;

    /**
     * 排序
     * 0默认排序、1认证排序、2车长升序、3车长降序、4重量升序、5重量降序
     */
    private int sort;

    /** 页码 */
    private int page;

    public String getStartProvinceValue() {
        return startProvinceValue;
    }

    public void setStartProvinceValue(String startProvinceValue) {
        this.startProvinceValue = startProvinceValue;
    }

    public String getStartCityValue() {
        return startCityValue;
    }

    public void setStartCityValue(String startCityValue) {
        this.startCityValue = startCityValue;
    }

    public String getStartCountyCode() {
        return startCountyCode;
    }

    public void setStartCountyCode(String startCountyCode) {
        this.startCountyCode = startCountyCode;
    }

    public String getStartCountyValue() {
        return startCountyValue;
    }

    public void setStartCountyValue(String startCountyValue) {
        this.startCountyValue = startCountyValue;
    }

    public String getEndProvinceValue() {
        return endProvinceValue;
    }

    public void setEndProvinceValue(String endProvinceValue) {
        this.endProvinceValue = endProvinceValue;
    }

    public String getEndCityValue() {
        return endCityValue;
    }

    public void setEndCityValue(String endCityValue) {
        this.endCityValue = endCityValue;
    }

    public String getEndCountyCode() {
        return endCountyCode;
    }

    public void setEndCountyCode(String endCountyCode) {
        this.endCountyCode = endCountyCode;
    }

    public String getEndCountyValue() {
        return endCountyValue;
    }

    public void setEndCountyValue(String endCountyValue) {
        this.endCountyValue = endCountyValue;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getStartProvinceCode() {
        return startProvinceCode;
    }

    public void setStartProvinceCode(String startProvinceCode) {
        this.startProvinceCode = startProvinceCode;
    }

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getEndProvinceCode() {
        return endProvinceCode;
    }

    public void setEndProvinceCode(String endProvinceCode) {
        this.endProvinceCode = endProvinceCode;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCarriageType() {
        return carriageType;
    }

    public void setCarriageType(String carriageType) {
        this.carriageType = carriageType;
    }

    public Double getCarLength() {
        return carLength;
    }

    public void setCarLength(Double carLength) {
        this.carLength = carLength;
    }

    public Double getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(Double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public Double getCargoCubage() {
        return cargoCubage;
    }

    public void setCargoCubage(Double cargoCubage) {
        this.cargoCubage = cargoCubage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
