package com.cy.driver.domain.push;

/**
 * 司机合同线路
 * Created by wyh on 2016/1/11.
 */
public class PushSjhtxl extends PushBase {

    /** 司机名称 */
    private String driverName;

    /** 车牌号码 */
    private String carNumber;

    /** 起点城市 */
    private String startCity;

    /** 目的地城市 */
    private String endCity;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }
}
