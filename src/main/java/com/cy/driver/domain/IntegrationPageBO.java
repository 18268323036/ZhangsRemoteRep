package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 积分明细分页列表信息
 * Created by wyh on 2016/4/25.
 */
public class IntegrationPageBO implements Serializable {
    private static final long serialVersionUID = 465065909103678826L;

    /** 积分明细id */
    private Long integrationId;

    /** 明细名称 */
    private String name;

    /** 出入类型:1 支出 2 收入 */
    private Byte fundFlow;

    /** 积分 */
    private Integer amount;

    /** 积分创建时间（yyyy-MM-dd hh:mm:ss） */
    private String createTime;

    public Long getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(Long integrationId) {
        this.integrationId = integrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getFundFlow() {
        return fundFlow;
    }

    public void setFundFlow(Byte fundFlow) {
        this.fundFlow = fundFlow;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
