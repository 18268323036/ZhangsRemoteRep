package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 货主详情BO
 * Created by mr on 2015/7/14.
 */
public class CargoOwnerBO implements Serializable{

    private static final long serialVersionUID = 2091809649118449985L;
    /*公司名称*/
    private String companyName;
    /*公司地址*/
    private String companyAddress;
    /*认证状态 0未提交1已提交2审核未通过3审核以通过*/
    private String authStatus;
    /*认证时间*/
    private String authTime;
    /*联系人*/
    private String contactName;
    /*手机*/
    private String mobile;
    /*座机*/
    private String telephone;
    /*信用*/
    private Double credit;

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

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
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

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
