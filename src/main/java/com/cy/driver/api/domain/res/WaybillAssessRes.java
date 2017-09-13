package com.cy.driver.api.domain.res;

/**
 * 转单运单评价
 * Created by nixianjing on 16/8/1.
 */
public class WaybillAssessRes {


    /**
     * 评价者身份（1司机，2承运方）
     */
    private String assessType;

    /**
     * 评价者姓名
     */
    private String assessUserName;

    /**
     * 头像
     */
    private String userHeadImg;

    /**
     * 联系电话
     */
    private String aimUserPhone;

    /**
     * 评价分数
     */
    private String assessScore;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价时间
     */
    private String assessTime;


    public String getAssessType() {
        return assessType;
    }

    public void setAssessType(String assessType) {
        this.assessType = assessType;
    }

    public String getAssessUserName() {
        return assessUserName;
    }

    public void setAssessUserName(String assessUserName) {
        this.assessUserName = assessUserName;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getAimUserPhone() {
        return aimUserPhone;
    }

    public void setAimUserPhone(String aimUserPhone) {
        this.aimUserPhone = aimUserPhone;
    }

    public String getAssessScore() {
        return assessScore;
    }

    public void setAssessScore(String assessScore) {
        this.assessScore = assessScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAssessTime() {
        return assessTime;
    }

    public void setAssessTime(String assessTime) {
        this.assessTime = assessTime;
    }
}
