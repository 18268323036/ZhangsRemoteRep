package com.cy.driver.domain.saasPay;

/**
 * Created by nixianjing on 17/8/18.
 */
public class PayUserBankInfo {

    /** 银行图标 */
    private String iconImgPath;

    /** 银行卡id,not null */
    private String bankId;

    /** 银行编号,not null */
    private String bankCode;

    /** 银行全称,not null */
    private String bankName;

    /** 银行卡号,not null */
    private String bankAccountNo;

    /** 卡类型（DC-借记、CC-贷记（信用卡）、PB-存折、OC-其他） */
    private String cardType;

    public String getIconImgPath() {

        return iconImgPath;
    }

    public void setIconImgPath(String iconImgPath) {
        this.iconImgPath = iconImgPath;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
