package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 订单数量
 * Created by gaojw on 2016/4/21.
 */
public class CountOrderNumBO implements Serializable {
    private static final long serialVersionUID = -1945794382392901012L;

    /** 待承运的订单数量 */
    private Integer waitCarrierNum;
    /** 待装货的订单数量 */
    private Integer waitLoadNum;
    /** 待卸货的订单数量 */
    private Integer waitUnloadNum;
    /** 待评价的订单数量 */
    private Integer waitEvalNum;

    public Integer getWaitCarrierNum() {
        return waitCarrierNum;
    }

    public void setWaitCarrierNum(Integer waitCarrierNum) {
        this.waitCarrierNum = waitCarrierNum;
    }

    public Integer getWaitLoadNum() {
        return waitLoadNum;
    }

    public void setWaitLoadNum(Integer waitLoadNum) {
        this.waitLoadNum = waitLoadNum;
    }

    public Integer getWaitUnloadNum() {
        return waitUnloadNum;
    }

    public void setWaitUnloadNum(Integer waitUnloadNum) {
        this.waitUnloadNum = waitUnloadNum;
    }

    public Integer getWaitEvalNum() {
        return waitEvalNum;
    }

    public void setWaitEvalNum(Integer waitEvalNum) {
        this.waitEvalNum = waitEvalNum;
    }
}
