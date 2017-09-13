package com.cy.driver.domain.push;

import java.util.Date;

/**
 * 司机发起的推送
 * Created by wyh on 2016/2/2.
 */
public class PushDriver extends PushBase {
    /** 装货时间 */
    private Date startTime;

    /** 装货地市 */
    private String startCity;

    /** 卸货地市 */
    private String endCity;

    /** 货物名称 */
    private String cargoName;

    /** 司机名称 */
    private String driverName;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
