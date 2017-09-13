package com.cy.driver.domain;

import java.util.List;

/**
 * Created by nixianjing on 17/5/15.
 */
public class SassOrderPageRes {

    /**
     * 返回总条数
     */
    private String totalNum;

    /**
     * 返回总页数
     */
    private String totalPage;


    /**
     * 抢单-竞价list
     */
    private List<SassOrderList> listData;


    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public List<SassOrderList> getListData() {
        return listData;
    }

    public void setListData(List<SassOrderList> listData) {
        this.listData = listData;
    }
}
