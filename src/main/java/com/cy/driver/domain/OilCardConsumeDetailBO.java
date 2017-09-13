package com.cy.driver.domain;

import java.io.Serializable;

/**
 * @author zhangxy 2016/10/19 11:36
 */
public class OilCardConsumeDetailBO implements Serializable {
    private static final long serialVersionUID = 8488797240523523811L;

    private String businessType;//业务种类（1001油卡充值、1002运费收入、2001支付运费）
    private String oilCardId;//油卡卡号
    private String tradeType;//出入类型（1支出、2收入）
    private String tradeMoney;//出入金额
    private String tradeTime;//交易时间
    private String businessTypeName;//业务种类名

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOilCardId() {
        return oilCardId;
    }

    public void setOilCardId(String oilCardId) {
        this.oilCardId = oilCardId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
