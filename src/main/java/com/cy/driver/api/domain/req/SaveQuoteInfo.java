package com.cy.driver.api.domain.req;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 保存报价信息
 * Created by 管理员 on 2016/6/16.
 */
public class SaveQuoteInfo implements Serializable {
    private static final long serialVersionUID = 7564460305889985141L;

    /** 货源id */
    private Long cargoId;

    /** 报价类型 */
    private Byte quoteType;

    /** 报价金额 */
    private BigDecimal quoteAmount;

    /** 备注 */
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Byte getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(Byte quoteType) {
        this.quoteType = quoteType;
    }

    public BigDecimal getQuoteAmount() {
        return quoteAmount;
    }

    public void setQuoteAmount(BigDecimal quoteAmount) {
        this.quoteAmount = quoteAmount;
    }
}
