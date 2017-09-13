package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * 货源详情货物明细
 * @author yanst 2016/5/26 16:27
 */
public class CargoDetailList implements Serializable{
    private static final long serialVersionUID = 8778857884371654127L;

    /** 货物名称 */
    private String cargoName;

    /** 货物类型 0 重货 1拋货 */
    private String cargoType;

    /** 数量合计 */
    private String totalNumber;

    /** 重量合计 */
    private String weight;

    /** 体积合计 */
    private String volume;

    /** 包装 */
    private String pack;

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
