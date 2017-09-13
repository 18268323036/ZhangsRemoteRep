package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * 评论列表信息
 * @author yanst 2016/5/30 15:01
 */
public class CommentList implements Serializable{
    private static final long serialVersionUID = -8242451574056120519L;

    /** 评论人照片 */
    private String commentPerHeadPhone;

    /** 手机号码 */
    private String mobilePhone;

    /** 评论人认证状态 */
    private String commentAuthState;

    /** 评论时间 */
    private String commentTime;

    /** 货源状态 1 货还在 2 已运走 */
    private String cargoState;

    /** 评论人内容 */
    private String commentContent;

    public String getCommentPerHeadPhone() {
        return commentPerHeadPhone;
    }

    public void setCommentPerHeadPhone(String commentPerHeadPhone) {
        this.commentPerHeadPhone = commentPerHeadPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCommentAuthState() {
        return commentAuthState;
    }

    public void setCommentAuthState(String commentAuthState) {
        this.commentAuthState = commentAuthState;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCargoState() {
        return cargoState;
    }

    public void setCargoState(String cargoState) {
        this.cargoState = cargoState;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
