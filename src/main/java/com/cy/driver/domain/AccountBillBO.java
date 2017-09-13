package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/11.
 * 账户余额
 */
public class AccountBillBO implements Serializable {
    private static final long serialVersionUID = -707353779322879307L;

    private String billTypeName;//	账单种类名称（提现和收款）

    private String tradingHours;//	交易时间

    private String trading;//	支入（0）或者支出（1）

    private String transaction;//	交易金额

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

    public String getTradingHours() {
        return tradingHours;
    }

    public void setTradingHours(String tradingHours) {
        this.tradingHours = tradingHours;
    }

    public String getTrading() {
        return trading;
    }

    public void setTrading(String trading) {
        this.trading = trading;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}
