package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * @author zhangxy 2016/8/11 14:09
 */
public class DriverImgDetail implements Serializable{

    private static final long serialVersionUID = 2860584925374939846L;

    private String imgPath;//图片路径
    private String imgType;//司机信息图片类型（-1 手机号码图片 1 身份证正面,2 身份证反面,3 驾驶证,4 行驶证,5 营运证,6 头像,7车辆图片）
    private String authStatus;//认证状态（1 认证中 2 认证失败 3 已认证）
    private String uploadTime;//图片上传时间
    private String effectiveDate;//有效期
    private String orderNum;//排序

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
