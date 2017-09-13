package com.cy.driver.api.domain.res;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangxy 2016/7/22 17:35
 */
public class PageBase<T> implements Serializable {
    private static final long serialVersionUID = 6720067261576426415L;

    //总条数
    private int totalNum;

    //总页数
    private int totalPage;

    private List<T> listData;

    public PageBase(){}

    public PageBase(int totalNum,int totalPage,List<T> dataList){
        this.totalNum = totalNum;
        this.totalPage = totalPage;
        this.listData = dataList;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }
}
