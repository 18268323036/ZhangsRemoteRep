package com.cy.driver.api.domain.req;

import java.io.Serializable;

/**
 * @author yanst 2016/5/26 14:41
 */
public class Page implements Serializable {
    private static final long serialVersionUID = -77812975772289870L;

    private Long page;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }
}
