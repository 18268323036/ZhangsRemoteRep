package com.cy.driver.domain.push;

/**
 * 司机合同车
 * Created by wyh on 2016/1/11.
 */
public class PushSjhtc extends PushBase {

    /** 司机名称 */
    private String driverName;

    /** 车牌号码 */
    private String carNumber;

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
}
