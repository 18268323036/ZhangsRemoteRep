package com.cy.driver.domain;

import java.io.Serializable;

/**
 * @author zhangxy 2016/10/19 11:54
 */
public class ConsumeClearingInfoBO implements Serializable {

    private static final long serialVersionUID = 3473676981454001317L;

    private String time;//时间

    private String tradeMoney;//清算金额

    private String tradeType;//结算类型   0收入  1支出

    private String timeName;//用于展示的时间

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
