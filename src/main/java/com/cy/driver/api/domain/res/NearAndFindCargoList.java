package com.cy.driver.api.domain.res;

import com.cy.driver.domain.CargoListBO;

import java.io.Serializable;

/**
 * 找货列表
 * @author yanst 2016/5/26 15:02
 */
public class NearAndFindCargoList extends CargoListBO implements Serializable {
    private static final long serialVersionUID = -3360760741317660171L;

    /** 货源id */
    private String cargoIdStr;

    /** 区域平台编号 */
    private String platformCode;

    /** 配送中心名称 */
    private String distributionCenterName;

    /** 货源来源（1快到网货源、2区域配送托单） */
    private Integer cargoSource;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public Integer getCargoSource() {
        return cargoSource;
    }

    public void setCargoSource(Integer cargoSource) {
        this.cargoSource = cargoSource;
    }

    public String getCargoIdStr() {
        return cargoIdStr;
    }

    public void setCargoIdStr(String cargoIdStr) {
        this.cargoIdStr = cargoIdStr;
    }

    public String getDistributionCenterName() {
        return distributionCenterName;
    }

    public void setDistributionCenterName(String distributionCenterName) {
        this.distributionCenterName = distributionCenterName;
    }
}
