package com.cy.driver.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 订单列表
 * Created by wyh on 2015/7/25.
 */
public class OrderInfoListBO implements Serializable {
    private static final long serialVersionUID = 2795248428279527681L;

    private long allNums;//总数量
    private long allPage;//总页数
    private List<OrderInfoBO> orderList;//订单列表集合(节点二)

    public long getAllNums() {
        return allNums;
    }

    public void setAllNums(long allNums) {
        this.allNums = allNums;
    }

    public long getAllPage() {
        return allPage;
    }

    public void setAllPage(long allPage) {
        this.allPage = allPage;
    }

    public List<OrderInfoBO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfoBO> orderList) {
        this.orderList = orderList;
    }
}
