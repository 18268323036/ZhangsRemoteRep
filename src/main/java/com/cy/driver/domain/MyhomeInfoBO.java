package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/11.
 */
public class MyhomeInfoBO implements Serializable {
    private static final long serialVersionUID = 8137695861549065006L;

    private String photosAddress;//个人头像md5

    private String authStatus;//认证状态

    private String carCard;//车牌号

    private String userName;//用户姓名

    private String registerTime;//注册时间

    private String emptyCarReportNumber;//空车上报

    private String oftenRunCityNums;//常跑城市

    private String amount;//余额

    private int capitalFreightNum;//	待收运费数量

    private String isSetUpPassword;//	是否设置过提现密码（0 已设置 1 未设置）

    private int bankCardNum;//	银行卡张数

    /** 未读通知消息数量 */
    private int notReadNoticeNum;

    /** 未读交易消息数量 */
    private int notReadTradeNum;


    public String getPhotosAddress() {
        return photosAddress;
    }

    public void setPhotosAddress(String photosAddress) {
        this.photosAddress = photosAddress;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getCarCard() {
        return carCard;
    }

    public void setCarCard(String carCard) {
        this.carCard = carCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getEmptyCarReportNumber() {
        return emptyCarReportNumber;
    }

    public void setEmptyCarReportNumber(String emptyCarReportNumber) {
        this.emptyCarReportNumber = emptyCarReportNumber;
    }

    public String getOftenRunCityNums() {
        return oftenRunCityNums;
    }

    public void setOftenRunCityNums(String oftenRunCityNums) {
        this.oftenRunCityNums = oftenRunCityNums;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIsSetUpPassword() {
        return isSetUpPassword;
    }

    public void setIsSetUpPassword(String isSetUpPassword) {
        this.isSetUpPassword = isSetUpPassword;
    }

    public int getCapitalFreightNum() {
        return capitalFreightNum;
    }

    public void setCapitalFreightNum(int capitalFreightNum) {
        this.capitalFreightNum = capitalFreightNum;
    }

    public int getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(int bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public int getNotReadNoticeNum() {
        return notReadNoticeNum;
    }

    public void setNotReadNoticeNum(int notReadNoticeNum) {
        this.notReadNoticeNum = notReadNoticeNum;
    }

    public int getNotReadTradeNum() {
        return notReadTradeNum;
    }

    public void setNotReadTradeNum(int notReadTradeNum) {
        this.notReadTradeNum = notReadTradeNum;
    }
}
