package com.cy.driver.domain;

/**
 * Created by wyh on 2016/4/6.
 */
public class AssessScore {
    /** 描述相符分 */
    private Integer describeScore;

    /** 响应速度分 */
    private Integer speedScore;

    /** 服务态度分 */
    private Integer serviceScore;

    public Integer getDescribeScore() {
        return describeScore;
    }

    public void setDescribeScore(Integer describeScore) {
        this.describeScore = describeScore;
    }

    public Integer getSpeedScore() {
        return speedScore;
    }

    public void setSpeedScore(Integer speedScore) {
        this.speedScore = speedScore;
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }
}
