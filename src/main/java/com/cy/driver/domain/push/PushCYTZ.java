package com.cy.driver.domain.push;

import java.util.Date;

/**
 * 承运通知
 * Created by wyh on 2016/1/4.
 */
public class PushCYTZ extends PushBase {
    /** 装货时间 */
    private Date startTime;

    /** 装货地市 */
    private String startAddrCity;

    /** 卸货地市 */
    private String endAddrCity;

    /** 货物名称 */
    private String cargoName;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStartAddrCity() {
        return startAddrCity;
    }

    public void setStartAddrCity(String startAddrCity) {
        this.startAddrCity = startAddrCity;
    }

    public String getEndAddrCity() {
        return endAddrCity;
    }

    public void setEndAddrCity(String endAddrCity) {
        this.endAddrCity = endAddrCity;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }
}
