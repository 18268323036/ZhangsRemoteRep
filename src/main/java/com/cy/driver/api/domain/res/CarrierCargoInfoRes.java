package com.cy.driver.api.domain.res;

/**
 * 托单货物
 * Created by nixianjing on 16/8/1.
 */
public class CarrierCargoInfoRes {

    /**
     * 主键id
     */
    private String cargoId;

    /**
     * 货品名称
     */
    private String cargoName;

    /**
     * 货物类型（1重货 2泡货）
     */
    private String cargoType;

    /**
     * 重量
     */
    private String weight;

    /**
     * 体积
      */
    private String cubage;

    /**
     * 件数
      */
    private String quantity;

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCubage() {
        return cubage;
    }

    public void setCubage(String cubage) {
        this.cubage = cubage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
