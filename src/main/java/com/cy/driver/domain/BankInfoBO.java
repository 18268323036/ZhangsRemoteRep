package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by yanst on 2015/12/22
 * 银行码表
 */
public class BankInfoBO implements Serializable {
    private static final long serialVersionUID = -7823125955887129994L;

    /** 行名 */
    private String bankName;

    /** 行号 */
    private String bankCode;

    /** 银行图片 */
    private String img;

    /** 卡号前缀 */
    private String cardnumPrefix;

    public String getCardnumPrefix() {
        return cardnumPrefix;
    }

    public void setCardnumPrefix(String cardnumPrefix) {
        this.cardnumPrefix = cardnumPrefix;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
