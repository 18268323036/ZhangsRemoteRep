package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/5.
 * 发货人详情（订单）
 *
 */
public class ConsignorDetailBO implements Serializable {
    private static final long serialVersionUID = 7199732837343908802L;

    private String companyName;//公司名称
    private String companyAddress;//公司地址
    private Byte  authStatus;//企业认证状态
    private String  authTime;//企业认证时间
    private String contactName;//发货人姓名
    private String mobile;//发货人手机号码
    private String telephone;//发货人座机

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Byte getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Byte authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
