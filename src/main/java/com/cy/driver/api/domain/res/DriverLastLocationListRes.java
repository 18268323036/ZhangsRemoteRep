package com.cy.driver.api.domain.res;

/**
 * Created by nixianjing on 16/8/3.
 */
public class DriverLastLocationListRes {

    /**
     * 上传位置信息
     */
    private String townAddress;

    /**
     * 上传时间(年-月-日 时：分：秒)
     */
    private String townTime;


    public String getTownAddress() {
        return townAddress;
    }

    public void setTownAddress(String townAddress) {
        this.townAddress = townAddress;
    }

    public String getTownTime() {
        return townTime;
    }

    public void setTownTime(String townTime) {
        this.townTime = townTime;
    }
}
