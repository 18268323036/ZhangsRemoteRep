package com.cy.driver.api.domain.res;

import java.io.Serializable;

/**
 * 托单下的货源明细
 * @author yanst 2016/6/2 14:06
 */
public class WayBillDetailCargoList implements Serializable{

    private static final long serialVersionUID = 35546581345891294L;

    private String  cargoName;//货物名称

    private String number;//件数

    private String  weight;//重量(单位：吨)

    private String volume;//体积(单位：方)

    private Integer cargoType;//货物类型(1重货 2泡货)

    private Integer packing;//包装（1塑料 2纸 3木（纤维） 4金属 5编织袋 6无 7其他）

    private String cargoTypeStr;

    private String packingStr;

    public String getCargoTypeStr() {
        return cargoTypeStr;
    }

    public void setCargoTypeStr(String cargoTypeStr) {
        this.cargoTypeStr = cargoTypeStr;
    }

    public String getPackingStr() {
        return packingStr;
    }

    public void setPackingStr(String packingStr) {
        this.packingStr = packingStr;
    }

    public Integer getPacking() {
        return packing;
    }

    public void setPacking(Integer packing) {
        this.packing = packing;
    }

    public Integer getCargoType() {
        return cargoType;
    }

    public void setCargoType(Integer cargoType) {
        this.cargoType = cargoType;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
