package com.cy.driver.service;

import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.domain.SearchCargoParam;
import com.cy.location.service.dto.LocationLineDTO;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.WaybillInfoDTO;
import com.cy.saas.business.model.po.WaybillInfo;

import java.util.List;

/**
 * @author zhangxy 2016/7/12
 */
public interface DriverLineTrackService {

    com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, CargoDetailDTO cargoDetailDTO, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, CargoDetailDTO cargoDetailDTO, Integer trackType);

    com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, OrderAndCargoDTO orderAndCargoDTO, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, OrderAndCargoDTO orderAndCargoDTO, Integer trackType);

    com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, CargoInfoDTO cargoInfoDTO, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, CargoInfoDTO cargoInfoDTO, Integer trackType);

    com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, SearchCargoParam searchCargoParam, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, SearchCargoParam searchCargoParam, Integer trackType);

    com.cy.location.service.dto.base.Response<Boolean> saveLine(List<LocationLineDTO> locationLineDTOs);

    com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, CarrierAssemDTO carrierAssemDTO, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, CarrierAssemDTO carrierAssemDTO, Integer trackType);

    com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, WaybillInfoDTO waybillInfoDTO, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, WaybillInfoDTO waybillInfoDTO, Integer trackType);

    com.cy.location.service.dto.base.Response<Boolean> saSaveLine(Long driverId, WaybillInfo waybillInfo, Integer trackType);

    com.cy.pass.service.dto.base.Response<Boolean> saSaveDayLine(Long driverId, WaybillInfo waybillInfo, Integer trackType);


}
