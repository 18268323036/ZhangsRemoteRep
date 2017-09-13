package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 3.4版本 我的主页信息
 * @author yanst 2016/4/18 13:45
 */
public class MyHomeInfoNewBO extends MyhomeInfo implements Serializable {
    private static final long serialVersionUID = 290911361303805945L;

    /** 积分 */
    private Integer integral;

    /** 浏览记录数 */
    private Long browseNums;

    /** 是否完善过车辆信息 0 已完善 1 未完善 */
    private String showType;

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Long getBrowseNums() {
        return browseNums;
    }

    public void setBrowseNums(Long browseNums) {
        this.browseNums = browseNums;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }
}
