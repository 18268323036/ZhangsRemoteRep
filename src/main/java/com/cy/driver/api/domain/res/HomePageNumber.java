package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * 首页数量
 * @author yanst 2016/6/7 10:31
 */
public class HomePageNumber implements Serializable {

    private static final long serialVersionUID = -5249032057828711834L;

    /** 附近货源数量 */
    private Long nearCargoNum;

    /** 货源报价数量 */
    private Long cargoQuoteNum;

    /** 待承运数量 */
    private Long waitCarriageNum;

    /** 消息数量(zxy加)  */
    private Long freightNums;

    public Long getFreightNums() {
        return freightNums;
    }

    public void setFreightNums(Long freightNums) {
        this.freightNums = freightNums;
    }

    public Long getNearCargoNum() {
        return nearCargoNum;
    }

    public void setNearCargoNum(Long nearCargoNum) {
        this.nearCargoNum = nearCargoNum;
    }

    public Long getCargoQuoteNum() {
        return cargoQuoteNum;
    }

    public void setCargoQuoteNum(Long cargoQuoteNum) {
        this.cargoQuoteNum = cargoQuoteNum;
    }

    public Long getWaitCarriageNum() {
        return waitCarriageNum;
    }

    public void setWaitCarriageNum(Long waitCarriageNum) {
        this.waitCarriageNum = waitCarriageNum;
    }
}
