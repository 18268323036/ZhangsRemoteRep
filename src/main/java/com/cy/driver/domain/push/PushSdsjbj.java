package com.cy.driver.domain.push;

import java.math.BigDecimal;

/**
 * 收到司机报价
 * Created by wyh on 2016/1/11.
 */
public class PushSdsjbj extends PushBase {

    /** 货物名称 */
    private String cargoName;

    /** 装货地市 */
    private String startCity;

    /** 卸货地市 */
    private String endCity;

    /** 报价 */
    private BigDecimal quoteMoney;

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public BigDecimal getQuoteMoney() {
        return quoteMoney;
    }

    public void setQuoteMoney(BigDecimal quoteMoney) {
        this.quoteMoney = quoteMoney;
    }
}
