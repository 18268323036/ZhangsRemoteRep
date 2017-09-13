package com.cy.driver.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 *
 * 待收运费列表
 */
public class CollectFreightListBO implements Serializable {
    private static final long serialVersionUID = 345744548914184845L;

    private int collectFreightNums;//待收运费总数量
    private int collectFreightPage;//待收运费总页数
    private List<CollectFreightBO> collectFreightLists;//待收运费列表（细见：待收运费列表信息）

    public int getCollectFreightNums() {
        return collectFreightNums;
    }

    public void setCollectFreightNums(int collectFreightNums) {
        this.collectFreightNums = collectFreightNums;
    }

    public int getCollectFreightPage() {
        return collectFreightPage;
    }

    public void setCollectFreightPage(int collectFreightPage) {
        this.collectFreightPage = collectFreightPage;
    }

    public List<CollectFreightBO> getCollectFreightLists() {
        return collectFreightLists;
    }

    public void setCollectFreightLists(List<CollectFreightBO> collectFreightLists) {
        this.collectFreightLists = collectFreightLists;
    }
}
