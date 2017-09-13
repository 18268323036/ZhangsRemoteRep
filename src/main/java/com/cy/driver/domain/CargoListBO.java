package com.cy.driver.domain;

import com.cy.driver.common.util.SystemsUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/16.
 * 货源列表
 */
public class CargoListBO implements Serializable {
    private static final long serialVersionUID = -5543396034687535060L;
    /** 货物ID */
    private Long cargoId;

    /** 货物ID */
    private String cargoIdStr;

    /** 企业名称 */
    private String companyName;

    /**企业认证状态*/
    private Byte companyAuthStatus;

    /** 装货地 */
    private String startAddress;

    /** 卸货地 */
    private String endAddress;

    /** 货物名称 */
    private String cargoName;

    /** 货物重量 */
    private String weight;

    /** 货物体积 */
    private String volume;

    /** 要求的车长 */
    private String carLength;

    /** 车辆类型名称 */
    private String vehicleTypeName;

    /** 车厢类型名称  */
    private String carriageTypeName;

    /** 装货时间 */
    private String startTime;

    /** 卸货时间 */
    private String endTime;

    /** 我的报价(带单位：元/车、元/吨、元/方) */
    private  String myQuote;

    /** 发布时间(例如：几小时前) */
    private String pubTime;

    /** (3.1 新增)运费总价 */
    private String totalFare;

    /** (3.1 新增)预付运费 */
    private String prepayFare;

    /** 货主联系方式 */
    private String ownerPhone ;

    /** 拨打网络电话状态（0 可以拨打 1不可以拨打） */
    private String networkTelephoneState;



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

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = SystemsUtil.municipalitiesTorepeat(startAddress);
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = SystemsUtil.municipalitiesTorepeat(endAddress);
    }

    public Byte getCompanyAuthStatus() {
        return companyAuthStatus;
    }

    public void setCompanyAuthStatus(Byte companyAuthStatus) {
        this.companyAuthStatus = companyAuthStatus;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getNetworkTelephoneState() {
        return networkTelephoneState;
    }

    public void setNetworkTelephoneState(String networkTelephoneState) {
        this.networkTelephoneState = networkTelephoneState;
    }

    public String getCargoIdStr() {
        return cargoIdStr;
    }

    public void setCargoIdStr(String cargoIdStr) {
        this.cargoIdStr = cargoIdStr;
    }
}
