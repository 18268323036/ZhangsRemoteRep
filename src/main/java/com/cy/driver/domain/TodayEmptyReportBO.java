package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/24.
 * 当日空车上报
 */
public class TodayEmptyReportBO implements Serializable {

    private static final long serialVersionUID = 7266351359727534296L;
    private Long emptyCarId;
    private int deleteType = 1;
    private String startTime;
    private String startCityCode;
    private String startAddress;
    private String endCityCode;
    private String endAddress;

    public Long getEmptyCarId() {
        return emptyCarId;
    }

    public void setEmptyCarId(Long emptyCarId) {
        this.emptyCarId = emptyCarId;
    }

    public int getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(int deleteType) {
        this.deleteType = deleteType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
}
