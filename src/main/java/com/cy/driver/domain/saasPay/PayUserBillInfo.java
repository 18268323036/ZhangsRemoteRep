package com.cy.driver.domain.saasPay;

import java.math.BigDecimal;

/**
 * utms账单记录信息
 * Created by nixianjing on 17/8/18.
 */
public class PayUserBillInfo {

    /**
     * 业务事件中文
     */
    private String businessEventValue;

    /** 出入金类型（1-支出、2-收入）,not null */
    private Byte fundFlow;

    /** 金额,not null */
    private BigDecimal amount;

    /** 订单标题,not null */
    private String subject;

    /** 实际发生时间,not null */
    private String realityTime;

    /** 实际发生时间月 */
    private String realityTimeYm;


    public String getBusinessEventValue() {
        return businessEventValue;
    }

    public void setBusinessEventValue(String businessEventValue) {
        this.businessEventValue = businessEventValue;
    }

    public Byte getFundFlow() {
        return fundFlow;
    }

    public void setFundFlow(Byte fundFlow) {
        this.fundFlow = fundFlow;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRealityTime() {
        return realityTime;
    }

    public void setRealityTime(String realityTime) {
        this.realityTime = realityTime;
    }

    public String getRealityTimeYm() {
        return realityTimeYm;
    }

    public void setRealityTimeYm(String realityTimeYm) {
        this.realityTimeYm = realityTimeYm;
    }
}
