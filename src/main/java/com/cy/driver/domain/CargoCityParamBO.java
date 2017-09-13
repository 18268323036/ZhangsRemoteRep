package com.cy.driver.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/24.
 */
public class CargoCityParamBO implements Serializable {
    private static final long serialVersionUID = 3654688767722447154L;

    private String startProvince;
    private String startProviceCode;
    private String startCity;
    private String startCityCode;
    private String endProvince;
    private String endProviceCode;
    private String endCity;
    private String endCityCode;
    private Date startDate;
    private Date endDate;
    private Long sortCode;

    public String getStartProvince() {
        return startProvince;
    }

    public void setStartProvince(String startProvince) {
        this.startProvince = startProvince;
    }

    public String getStartProviceCode() {
        return startProviceCode;
    }

    public void setStartProviceCode(String startProviceCode) {
        this.startProviceCode = startProviceCode;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getEndProvince() {
        return endProvince;
    }

    public void setEndProvince(String endProvince) {
        this.endProvince = endProvince;
    }

    public String getEndProviceCode() {
        return endProviceCode;
    }

    public void setEndProviceCode(String endProviceCode) {
        this.endProviceCode = endProviceCode;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getSortCode() {
        return sortCode;
    }

    public void setSortCode(Long sortCode) {
        this.sortCode = sortCode;
    }
}
