package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/24.
 * 预约空车上报
 */
public class BusinessLineBO implements Serializable {
    private static final long serialVersionUID = -506585814641791073L;

    private Long emptyCarId;//emptyCarId
    private int deleteType = 2;//类型：删除未来预约信息
    private String startTime;//起始时间
    private String endTime;//截至日期
    private String startCityCode;//出发地城市编号
    private String startAddress;//出发地
    private String endCityCode;//目的地城市编号
    private String endAddress;//目的地

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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
