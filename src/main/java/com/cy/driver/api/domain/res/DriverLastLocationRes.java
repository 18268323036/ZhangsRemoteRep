package com.cy.driver.api.domain.res;

/**
 * Created by nixianjing on 16/8/3.
 */
public class DriverLastLocationRes {
    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 车牌号
     */
    private String driverCarNumber;

    /**
     * 联系电话
     */
    private String driverMobile;

    /**
     * 司机当前位置
     */
    private String driverAddress;

    /**
     * 司机当前经度
     */
    private String driverLng;

    /**
     * 司机当前纬度
     */
    private String driverLat;

    /**
     * 装货地地址
     */
    private String startAddress;

    /**
     * 装货地经度
     */
    private String startLng;

    /**
     * 装货地纬度
     */
    private String startLat;

    /**
     * 卸货地地址
     */
    private String endAddress;

    /**
     * 卸货地经度
     */
    private String endLng;

    /**
     * 卸货地纬度
     */
    private String endLat;


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverCarNumber() {
        return driverCarNumber;
    }

    public void setDriverCarNumber(String driverCarNumber) {
        this.driverCarNumber = driverCarNumber;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(String driverLng) {
        this.driverLng = driverLng;
    }

    public String getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(String driverLat) {
        this.driverLat = driverLat;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartLng() {
        return startLng;
    }

    public void setStartLng(String startLng) {
        this.startLng = startLng;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndLng() {
        return endLng;
    }

    public void setEndLng(String endLng) {
        this.endLng = endLng;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }
}
