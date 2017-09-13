package com.cy.driver.domain;

/**
 * 分页查询
 * Created by wyh on 2016/3/25.
 */
public class PageComm {
    private static final int DEFAULT_PAGE_SIZE = 10;

    /** 页码（从1开始） */
    private int page;

    /** 分页大小（默认10） */
    private int pageSize = DEFAULT_PAGE_SIZE;

    public PageComm(int page) {
        this.page = page;
    }

    public PageComm(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
