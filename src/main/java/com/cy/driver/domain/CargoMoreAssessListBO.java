package com.cy.driver.domain;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/7/12.
 */
public class CargoMoreAssessListBO implements Serializable{
    private static final long serialVersionUID = 143492555020130946L;

    private long cargoAssessCount;//总数量
    private long cargoAssessTotalPages;//总页数
    private List<CargoMoreAssessBO> cargoMoreAssessBOList;//列表

    public long getCargoAssessCount() {
        return cargoAssessCount;
    }

    public void setCargoAssessCount(long cargoAssessCount) {
        this.cargoAssessCount = cargoAssessCount;
    }

    public long getCargoAssessTotalPages() {
        return cargoAssessTotalPages;
    }

    public void setCargoAssessTotalPages(long cargoAssessTotalPages) {
        this.cargoAssessTotalPages = cargoAssessTotalPages;
    }

    public List<CargoMoreAssessBO> getCargoMoreAssessBOList() {
        return cargoMoreAssessBOList;
    }

    public void setCargoMoreAssessBOList(List<CargoMoreAssessBO> cargoMoreAssessBOList) {
        this.cargoMoreAssessBOList = cargoMoreAssessBOList;
    }
}
