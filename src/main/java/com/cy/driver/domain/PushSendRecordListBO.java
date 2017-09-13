package com.cy.driver.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 消息中心列表
 * Created by yanst on 2015/10/12.
 */
public class PushSendRecordListBO implements Serializable {
    private static final long serialVersionUID = -2639332014828207004L;

    private int totalNum;//总数量

    private int totalPage;//总页数

    private List<PushSendRecordBO> boList;

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

    public List<PushSendRecordBO> getBoList() {
        return boList;
    }

    public void setBoList(List<PushSendRecordBO> boList) {
        this.boList = boList;
    }
}
