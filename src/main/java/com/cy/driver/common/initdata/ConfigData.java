package com.cy.driver.common.initdata;

import org.springframework.beans.factory.annotation.Value;

/**
 * 配置文件信息
 * Created by wyh on 2015/11/28.
 */
public class ConfigData {

    /** 货主推送接口地址 */
    private String sendOwnPushSDKURL;

    /** 司机推送接口地址 */
    private String sendDriverPushSDKURL;

    /** 分包订单协议 */
    private String distributeProtocolUrl;

    /** 注册成功后不创建资金帐户的手机号码 */
    private String registerMobilephone;

    /** 银行码表标识 */
    private String bankTable;

    /** 区域码表标识 */
    private String areaTable;

    private Double mvalue;

    public String getSendOwnPushSDKURL() {
        return sendOwnPushSDKURL;
    }

    @Value("${inside.push.api.sendOwnPushSDKURL}")
    public void setSendOwnPushSDKURL(String sendOwnPushSDKURL) {
        this.sendOwnPushSDKURL = sendOwnPushSDKURL;
    }

    public String getSendDriverPushSDKURL() {
        return sendDriverPushSDKURL;
    }

    @Value("${inside.push.api.sendPushSDKURL}")
    public void setSendDriverPushSDKURL(String sendDriverPushSDKURL) {
        this.sendDriverPushSDKURL = sendDriverPushSDKURL;
    }

    public String getDistributeProtocolUrl() {
        return distributeProtocolUrl;
    }

    @Value("${distribute.protocol.url}")
    public void setDistributeProtocolUrl(String distributeProtocolUrl) {
        this.distributeProtocolUrl = distributeProtocolUrl;
    }

    public String getRegisterMobilephone() {
        return registerMobilephone;
    }

    @Value("${exclude.register.mobilephone}")
    public void setRegisterMobilephone(String registerMobilephone) {
        this.registerMobilephone = registerMobilephone;
    }

    public String getBankTable() {
        return bankTable;
    }

    @Value("${initdata.bankTable.version}")
    public void setBankTable(String bankTable) {
        this.bankTable = bankTable;
    }

    public String getAreaTable() {
        return areaTable;
    }

    @Value("${initdata.areaTable.version}")
    public void setAreaTable(String areaTable) {
        this.areaTable = areaTable;
    }

    public Double getMvalue() {
        return mvalue;
    }

    @Value("${award_point_money_m_value}")
    public void setMvalue(Double mvalue) {
        this.mvalue = mvalue;
    }
}
