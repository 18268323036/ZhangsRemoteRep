package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * Created by nixianjing on 16/8/2.
 */
public class WaybillOrderCountRes implements Serializable {

    /**
     * 订单类型 (1转单订单待接单 2转单订单已接单)
     */
    private Integer state;

    /**
     * 订单数量
     */
    private Integer count;


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
