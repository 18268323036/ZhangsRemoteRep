package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * 普通运单数量
 * @author yanst 2016/6/1 14:30
 */
public class WaybillNumber implements Serializable {
    private static final long serialVersionUID = 8073635083551817616L;

    /** 待接单 */
    private int waitOrder;

    /** 待装货 */
    private int waitLoading;

    /** 待卸货 */
    private int waitUnload;

    /** 已卸货 */
    private int aleadyUnload;

    /** 其他 */
    private int other;

    public int getWaitOrder() {
        return waitOrder;
    }

    public void setWaitOrder(int waitOrder) {
        this.waitOrder = waitOrder;
    }

    public int getWaitLoading() {
        return waitLoading;
    }

    public void setWaitLoading(int waitLoading) {
        this.waitLoading = waitLoading;
    }

    public int getWaitUnload() {
        return waitUnload;
    }

    public void setWaitUnload(int waitUnload) {
        this.waitUnload = waitUnload;
    }

    public int getAleadyUnload() {
        return aleadyUnload;
    }

    public void setAleadyUnload(int aleadyUnload) {
        this.aleadyUnload = aleadyUnload;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }
}
