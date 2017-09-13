package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 货主评论信息列表
 * @author yanst 2016/4/19 15:02
 */
public class OnwerAssessListBO implements Serializable {
    private static final long serialVersionUID = -8188712707054563175L;

    private Long assessId;

    /** 评论用户id */
    private Long assessUserId;

    /** 评论人头像 */
    private String onwerHeadImg;

    /** 认证状态 （3已认证 其它未认证）*/
    private String authState;

    /** 手机号码 (隐藏中间四位)*/
    private String mobilePhone;

    /** 评论时间 */
    private String accessTime;

    /** 评论类型 1:差评 2:中评 3:好评 */
    private Integer accessType;

    /** 评论内容 */
    private String accessContent;

    public Long getAssessId() {
        return assessId;
    }

    public void setAssessId(Long assessId) {
        this.assessId = assessId;
    }

    public Long getAssessUserId() {
        return assessUserId;
    }

    public void setAssessUserId(Long assessUserId) {
        this.assessUserId = assessUserId;
    }

    public String getOnwerHeadImg() {
        return onwerHeadImg;
    }

    public void setOnwerHeadImg(String onwerHeadImg) {
        this.onwerHeadImg = onwerHeadImg;
    }

    public String getAuthState() {
        return authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public Integer getAccessType() {
        return accessType;
    }

    public void setAccessType(Integer accessType) {
        this.accessType = accessType;
    }

    public String getAccessContent() {
        return accessContent;
    }

    public void setAccessContent(String accessContent) {
        this.accessContent = accessContent;
    }
}
