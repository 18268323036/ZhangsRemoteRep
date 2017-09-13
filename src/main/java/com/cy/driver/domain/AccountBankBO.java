package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/10.
 * 我的银行卡信息
 */
public class AccountBankBO implements Serializable {

    private static final long serialVersionUID = 3923587312290023860L;
    /** bankId */
    private Long bankId;

    /** 银行卡号 */
    private String bankCard;

    /** 姓名 */
    private String username;

    /** 开户支行名称 */
    private String accountName;

    /** 银行卡类型(储蓄卡、信用卡) */
    private String bankCardType;

    /** 银行卡图标 */
    private String img;

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
