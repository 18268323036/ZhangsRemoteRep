package com.cy.driver.api.domain.res;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nixianjing on 16/8/2.
 */
public class WaybillOrderPageRes implements Serializable {

    /**
     * 返回总条数
     */
    private String totalNum;

    /**
     * 返回总页数
     */
    private String totalPage;

    /**
     * 返回转单订单列表
     */
    private List<WaybillOrderListRes> listData;


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

    public List<WaybillOrderListRes> getListData() {
        return listData;
    }

    public void setListData(List<WaybillOrderListRes> listData) {
        this.listData = listData;
    }
}
