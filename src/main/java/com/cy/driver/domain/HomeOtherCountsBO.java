package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/23.
 */
public class HomeOtherCountsBO implements Serializable {

    private static final long serialVersionUID = -8960732654607618541L;
    private int quoteNums;//我的报价数量
    private int waitOrderNums;//订单待承运数量
    private int orderNums;//订单已承运数量
    private long cargoNums;//附近货源数量
    private int freightNums;//未读消息数量

    public int getQuoteNums() {
        return quoteNums;
    }

    public void setQuoteNums(int quoteNums) {
        this.quoteNums = quoteNums;
    }

    public int getWaitOrderNums() {
        return waitOrderNums;
    }

    public void setWaitOrderNums(int waitOrderNums) {
        this.waitOrderNums = waitOrderNums;
    }

    public int getOrderNums() {
        return orderNums;
    }

    public void setOrderNums(int orderNums) {
        this.orderNums = orderNums;
    }

    public long getCargoNums() {
        return cargoNums;
    }

    public void setCargoNums(long cargoNums) {
        this.cargoNums = cargoNums;
    }

    public int getFreightNums() {
        return freightNums;
    }

    public void setFreightNums(int freightNums) {
        this.freightNums = freightNums;
    }
}

