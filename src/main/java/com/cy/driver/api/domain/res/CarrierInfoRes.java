package com.cy.driver.api.domain.res;

import java.util.List;

/**
 * 托单对象
 * Created by nixianjing on 16/8/1.
 */
public class CarrierInfoRes {

    /**
     * 托单ID
     */
    private String carrierId;

    /**
     * 托单号
     */
    private String carrierNum;

    /**
     * 托单起始地
     */
    private String carrierStartAddress;

    /**
     * 托单目的地
     */
    private String carrierEndAddress;

    /**
     * 合计体积(与合计重量二选一，无填写0)
     */
    private String totalCubage;

    /**
     * 合计件数
      */
    private String	totalQuantity;

    /**
     * 合计重量(与合计体积二选一，无填写0)
     */
    private String totalWeight;


    /**
     * 托单明细List
     */
    private List<CarrierCargoInfoRes> cargoListData;

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierNum() {
        return carrierNum;
    }

    public void setCarrierNum(String carrierNum) {
        this.carrierNum = carrierNum;
    }

    public String getCarrierStartAddress() {
        return carrierStartAddress;
    }

    public void setCarrierStartAddress(String carrierStartAddress) {
        this.carrierStartAddress = carrierStartAddress;
    }

    public String getCarrierEndAddress() {
        return carrierEndAddress;
    }

    public void setCarrierEndAddress(String carrierEndAddress) {
        this.carrierEndAddress = carrierEndAddress;
    }

    public String getTotalCubage() {
        return totalCubage;
    }

    public void setTotalCubage(String totalCubage) {
        this.totalCubage = totalCubage;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<CarrierCargoInfoRes> getCargoListData() {
        return cargoListData;
    }

    public void setCargoListData(List<CarrierCargoInfoRes> cargoListData) {
        this.cargoListData = cargoListData;
    }
}
