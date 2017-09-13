package com.cy.driver.service;

import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.domain.FindCargo;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.WaybillInfoDTO;
import com.cy.saas.business.model.po.WaybillInfo;

/**
 * @author zhangxy 2016/7/20
 */
public interface TrackPointService {
    void saveLine(Long driverId, FindCargo findCargo);

    void saveLine(Long driverId, CargoDetailDTO cargoDetailDTO);

    void saveLine(Long driverId, CarrierAssemDTO carrierAssemDTO, int type);

    void saveLine(Long driverId, CargoInfoDTO cargoInfoDTO);

    void saveLine(Long driverId, WaybillInfoDTO waybillInfoDTO);

    void saSaveLine(Long driverId, WaybillInfo waybillInfo);
}
