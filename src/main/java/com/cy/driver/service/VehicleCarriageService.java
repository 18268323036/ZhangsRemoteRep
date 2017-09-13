package com.cy.driver.service;

import com.cy.driver.common.initdata.SystemTreeCode;

import java.util.List;

/**
 * 车辆类型和车厢类型
 * Created by wyh on 2015/7/23.
 */
public interface VehicleCarriageService {

    /**
     * 查询车辆类型集合
     * @return
     * @author wyh
     */
    List<SystemTreeCode> queryVehicleList();

    /**
     * 查询车厢类型集合
     * @return
     * @author wyh
     */
    List<SystemTreeCode> queryCarriageList();
}
