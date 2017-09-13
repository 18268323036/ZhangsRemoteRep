package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/10.
 */
public class BankBO implements Serializable {
    private static final long serialVersionUID = -7823125955887129994L;

    /** bankId */
    private Long bankId;

    /** 行名 */
    private String bankName;

    /** 行号 */
    private String bankCode;

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
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
