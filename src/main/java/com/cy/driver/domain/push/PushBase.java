package com.cy.driver.domain.push;

/**
 * Created by wyh on 2016/1/4.
 */
public class PushBase {

    /** 接受信息的用户id */
    private Long userId;

    /** 业务id */
    private Long businessId;

    /** 业务id */
    private Long businessId2;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getBusinessId2() {
        return businessId2;
    }

    public void setBusinessId2(Long businessId2) {
        this.businessId2 = businessId2;
    }
}
