package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/22.
 * 我的报价列表
 */
public class QuoteInfoBO implements Serializable{
    private static final long serialVersionUID = -5741086925648833820L;

    private Long cargoId;//货源id
    private String  companyName;//货主名称
    private String startTime;//	装货时间(yyyy-mm-dd hh:mm)
    private String startAddress;//出发地
    private String endAddress;//目的地
    private String cargoName;//货物名称
    private String weight;//重量(带单位：吨)
    private String volume;//体积(带单位：方)
    private String carLength;//车长(带单位：米)
    private String vehicleTypeName;//车辆类型名称
    private String carriageTypeName;//车厢类型名称
    private String  myQuote;//我的报价(带单位：元/车、元/吨、元/方)
    private String quoteTime;//报价时间(yyyy-mm-dd hh:mm)
    private String statusCode;//状态编号(-1货源已撤销、1未承运、2已承运)
    private String statusName;//状态名称

    /** (3.1 新增)运费总价 */
    private String totalFare;

    /** (3.1 新增)预付运费 */
    private String prepayFare;

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

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getMyQuote() {
        return myQuote;
    }

    public void setMyQuote(String myQuote) {
        this.myQuote = myQuote;
    }

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
