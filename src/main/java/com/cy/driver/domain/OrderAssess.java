package com.cy.driver.domain;

/**
 * 保存评价
 * Created by wyh on 2016/4/1.
 */
public class OrderAssess {

    /** 订单id */
    private Long orderId;

    /** 源用户编号 */
    private Long orgUserId;

    /** 源用户名称 */
    private String orgUserName;

    /** 评价分数（3 好评、6 中评、9差评） */
    private Integer assessScore;

    /** 评价内容 */
    private String content;

    /** 是否匿名 0 否  1 是 */
    private Byte nameHidden;

    /** 扩展字段保存 */
    private String col1;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(Long orgUserId) {
        this.orgUserId = orgUserId;
    }

    public String getOrgUserName() {
        return orgUserName;
    }

    public void setOrgUserName(String orgUserName) {
        this.orgUserName = orgUserName;
    }

    public Integer getAssessScore() {
        return assessScore;
    }

    public void setAssessScore(Integer assessScore) {
        this.assessScore = assessScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getNameHidden() {
        return nameHidden;
    }

    public void setNameHidden(Byte nameHidden) {
        this.nameHidden = nameHidden;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }
}
