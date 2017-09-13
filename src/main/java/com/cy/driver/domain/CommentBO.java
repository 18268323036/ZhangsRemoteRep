package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/27.
 */
public class CommentBO implements Serializable {
    private static final long serialVersionUID = 5012142985556316617L;

    private Long assessId;//评价id
    private Byte describeScore;//描述相符分数(0-5)
    private Byte respSpeedScore;//响应速度(0-5)
    private Byte serviceScore;//服务态度(0-5)
    private String assessContent;//评价内容

    private String userName;//用户姓名

    private String driverName;//司机姓名

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Long getAssessId() {
        return assessId;
    }

    public void setAssessId(Long assessId) {
        this.assessId = assessId;
    }

    public Byte getDescribeScore() {
        return describeScore;
    }

    public void setDescribeScore(Byte describeScore) {
        this.describeScore = describeScore;
    }

    public Byte getRespSpeedScore() {
        return respSpeedScore;
    }

    public void setRespSpeedScore(Byte respSpeedScore) {
        this.respSpeedScore = respSpeedScore;
    }

    public Byte getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Byte serviceScore) {
        this.serviceScore = serviceScore;
    }

    public String getAssessContent() {
        return assessContent;
    }

    public void setAssessContent(String assessContent) {
        this.assessContent = assessContent;
    }
}
