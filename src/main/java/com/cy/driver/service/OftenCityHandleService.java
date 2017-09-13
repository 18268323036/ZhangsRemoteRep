package com.cy.driver.service;

import com.cy.pass.service.dto.DriverOftenCityDTO;
import com.cy.pass.service.dto.base.Response;

import java.util.List;

/**
 * Created by mr on 2015/7/6.
 */
public interface OftenCityHandleService {
    /**
     * 维护司机常跑城市信息
     * @param cityCodeList 城市Code列表
     * @param driverId
     * @return
     */
    public Response<String> saveOrUpdate(List<String> cityCodeList, String driverId);

    /**
     * 根据司机编号查询常跑地区列表
     * @param driverId
     * @return
     */
    public Response<List<DriverOftenCityDTO>> list(String driverId);

    /**
     * 查询司机的常跑城市列表
     * @param driverId
     * @return
     * @author wyh
     */
    List<DriverOftenCityDTO> queryList(Long driverId);
}
