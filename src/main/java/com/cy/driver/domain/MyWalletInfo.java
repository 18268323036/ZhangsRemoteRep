package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 我的钱包信息
 * @author yanst 2016/4/20 9:42
 */
public class MyWalletInfo implements Serializable {

    private static final long serialVersionUID = 5493715955092031367L;

    private String amount;//总金额

    private Integer capitalFreightNum;//待收运费

    private Integer bankCardNum; //银行卡张数

    private Integer couponsNum; //优惠卷

    private String idSetUpTradersPassword;//是否设置过交易密码（0 已设置 1 未设置）

    private Integer integral;//积分

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getCapitalFreightNum() {
        return capitalFreightNum;
    }

    public void setCapitalFreightNum(Integer capitalFreightNum) {
        this.capitalFreightNum = capitalFreightNum;
    }

    public Integer getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(Integer bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public Integer getCouponsNum() {
        return couponsNum;
    }

    public void setCouponsNum(Integer couponsNum) {
        this.couponsNum = couponsNum;
    }

    public String getIdSetUpTradersPassword() {
        return idSetUpTradersPassword;
    }

    public void setIdSetUpTradersPassword(String idSetUpTradersPassword) {
        this.idSetUpTradersPassword = idSetUpTradersPassword;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
