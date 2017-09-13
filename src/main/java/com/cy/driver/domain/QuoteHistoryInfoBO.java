package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/23.
 *
 * 报价历史
 */
public class QuoteHistoryInfoBO implements Serializable {
    private static final long serialVersionUID = -3752455285706595295L;

    private Long quoteId;//报价id
    private String myQuote;//	我的报价(带单位：元/车、元/吨、元/方)
    private String quoteTime;//	报价时间(yyyy-mm-dd hh:mm:ss)

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getMyQuote() {
        return myQuote;
    }

    public void setMyQuote(String myQuote) {
        this.myQuote = myQuote;
    }

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }
}
