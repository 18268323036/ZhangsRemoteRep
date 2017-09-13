package com.cy.driver.domain;

import java.io.Serializable;

/**
 * @author yanst 2016/4/25 20:14
 */
public class PactLineBO implements Serializable {
    private static final long serialVersionUID = -816555849933784894L;


    private Long pactLineId;

    /** 开始线路 */
    private String startAddress;

    /** 结束线路 */
    private String endAddress;

    public Long getPactLineId() {
        return pactLineId;
    }

    public void setPactLineId(Long pactLineId) {
        this.pactLineId = pactLineId;
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
}
