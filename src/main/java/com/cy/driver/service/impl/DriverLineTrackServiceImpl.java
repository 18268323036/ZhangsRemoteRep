package com.cy.driver.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.domain.SearchCargoParam;
import com.cy.driver.service.DriverLineTrackService;
import com.cy.location.service.LocationLineService;
import com.cy.location.service.dto.LocationLineDTO;
import com.cy.location.service.dto.base.Response;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.pass.service.DriverDayLineService;
import com.cy.pass.service.dto.DriverDayLineDTO;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.WaybillInfoDTO;
import com.cy.saas.business.model.po.WaybillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxy 2016/7/12 16:11
 */

@Service("driverLineTrackService")
public class DriverLineTrackServiceImpl implements DriverLineTrackService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private LocationLineService locationLineService;

    @Resource
    private DriverDayLineService driverDayLineService;


    /**
     * 查看货源详情时调用的埋点方法
     * @param driverId
     * @param cargoDetailDTO
     * @param trackType
     * @return
     */
    @Override
    public com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, CargoDetailDTO cargoDetailDTO, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(cargoDetailDTO.getStartCounty());
        locationLineDTO.setStartCity(cargoDetailDTO.getStartCity());
        locationLineDTO.setStartProvince(cargoDetailDTO.getStartProvince());
        locationLineDTO.setEndCounty(cargoDetailDTO.getEndCounty());
        locationLineDTO.setEndCity(cargoDetailDTO.getEndCity());
        locationLineDTO.setEndProvince(cargoDetailDTO.getEndProvince());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    /**
     * 查看货源详情时调用的埋点方法(当货源要求发货日期等于当日)
     * @param driverId
     * @param cargoDetailDTO
     * @param trackType
     * @return
     */
    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, CargoDetailDTO cargoDetailDTO, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(cargoDetailDTO.getStartCounty());
        driverDayLineDTO.setStartCity(cargoDetailDTO.getStartCity());
        driverDayLineDTO.setStartProvince(cargoDetailDTO.getStartProvince());
        driverDayLineDTO.setEndCounty(cargoDetailDTO.getEndCounty());
        driverDayLineDTO.setEndCity(cargoDetailDTO.getEndCity());
        driverDayLineDTO.setEndProvince(cargoDetailDTO.getEndProvince());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }

    /**
     * 司机承运订单时调用的保存路线信息服务
     * @param driverId
     * @param orderAndCargoDTO
     * @param trackType
     * @return
     */
    @Override
    public Response<Boolean> saveLine(Long driverId, OrderAndCargoDTO orderAndCargoDTO, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(orderAndCargoDTO.getCargoInfo().getStartCounty());
        locationLineDTO.setStartCity(orderAndCargoDTO.getCargoInfo().getStartCity());
        locationLineDTO.setStartProvince(orderAndCargoDTO.getCargoInfo().getStartProvince());
        locationLineDTO.setEndCounty(orderAndCargoDTO.getCargoInfo().getEndCounty());
        locationLineDTO.setEndCity(orderAndCargoDTO.getCargoInfo().getEndCity());
        locationLineDTO.setEndProvince(orderAndCargoDTO.getCargoInfo().getEndProvince());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    /**
     * 司机承运订单时调用的保存路线信息服务(当货源要求发货日期等于当日)
     * @param driverId
     * @param orderAndCargoDTO
     * @param trackType
     * @return
     */
    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, OrderAndCargoDTO orderAndCargoDTO, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(orderAndCargoDTO.getCargoInfo().getStartCounty());
        driverDayLineDTO.setStartCity(orderAndCargoDTO.getCargoInfo().getStartCity());
        driverDayLineDTO.setStartProvince(orderAndCargoDTO.getCargoInfo().getStartProvince());
        driverDayLineDTO.setEndCounty(orderAndCargoDTO.getCargoInfo().getEndCounty());
        driverDayLineDTO.setEndCity(orderAndCargoDTO.getCargoInfo().getEndCity());
        driverDayLineDTO.setEndProvince(orderAndCargoDTO.getCargoInfo().getEndProvince());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }

    /**
     * 司机报价货源时保存货源路线调用的接口
     * @param driverId
     * @param cargoInfoDTO
     * @param trackType
     * @return
     */
    @Override
    public Response<Boolean> saveLine(Long driverId, CargoInfoDTO cargoInfoDTO, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(cargoInfoDTO.getStartCounty());
        locationLineDTO.setStartCity(cargoInfoDTO.getStartCity());
        locationLineDTO.setStartProvince(cargoInfoDTO.getStartProvince());
        locationLineDTO.setEndCounty(cargoInfoDTO.getEndCounty());
        locationLineDTO.setEndCity(cargoInfoDTO.getEndCity());
        locationLineDTO.setEndProvince(cargoInfoDTO.getEndProvince());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    /**
     * 司机报价货源时保存货源路线调用的接口(当货源要求发货日期等于当日)
     * @param driverId
     * @param cargoInfoDTO
     * @param trackType
     * @return
     */
    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, CargoInfoDTO cargoInfoDTO, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(cargoInfoDTO.getStartCounty());
        driverDayLineDTO.setStartCity(cargoInfoDTO.getStartCity());
        driverDayLineDTO.setStartProvince(cargoInfoDTO.getStartProvince());
        driverDayLineDTO.setEndCounty(cargoInfoDTO.getEndCounty());
        driverDayLineDTO.setEndCity(cargoInfoDTO.getEndCity());
        driverDayLineDTO.setEndProvince(cargoInfoDTO.getEndProvince());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }

    /**
     * 搜索货源的时候保存货源路线调用的接口
     * @param driverId
     * @param searchCargoParam
     * @param trackType
     * @return
     */
    @Override
    public Response<Boolean> saveLine(Long driverId, SearchCargoParam searchCargoParam, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(null==searchCargoParam.getsCountyValue()?"":searchCargoParam.getsCountyValue());
        if(StringUtils.isNotEmpty(searchCargoParam.getsCityCode())) {
            locationLineDTO.setStartCity(null==searchCargoParam.getsCityValue()?"":searchCargoParam.getsCityValue());
        }else {
            locationLineDTO.setStartCity("");
        }
        locationLineDTO.setStartProvince(searchCargoParam.getsProValue());
        locationLineDTO.setEndCounty(null==searchCargoParam.geteCountyValue()?"":searchCargoParam.geteCountyValue());
        if(StringUtils.isNotEmpty(searchCargoParam.geteCityCode())) {
            locationLineDTO.setEndCity(null==searchCargoParam.geteCityValue()?"":searchCargoParam.geteCityValue());
        }else {
            locationLineDTO.setEndCity("");
        }
        locationLineDTO.setEndProvince(searchCargoParam.geteProValue());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    /**
     * 搜索货源的时候保存货源路线调用的接口(当货源要求发货日期等于当日)
     * @param driverId
     * @param searchCargoParam
     * @param trackType
     * @return
     */
    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, SearchCargoParam searchCargoParam, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(null==searchCargoParam.getsCountyValue()?"":searchCargoParam.getsCountyValue());
        if(StringUtils.isNotEmpty(searchCargoParam.getsCityCode())) {
            driverDayLineDTO.setStartCity(null==searchCargoParam.getsCityValue()?"":searchCargoParam.getsCityValue());
        }else {
            driverDayLineDTO.setStartCity("");
        }
        driverDayLineDTO.setStartProvince(searchCargoParam.getsProValue());
        driverDayLineDTO.setEndCounty(null==searchCargoParam.geteCountyValue()?"":searchCargoParam.geteCountyValue());
        if(StringUtils.isNotEmpty(searchCargoParam.geteCityCode())) {
            driverDayLineDTO.setEndCity(null==searchCargoParam.geteCityValue()?"":searchCargoParam.geteCityValue());
        }else {
            driverDayLineDTO.setEndCity("");
        }
        driverDayLineDTO.setEndProvince(searchCargoParam.geteProValue());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }

    /**
     * 司机空车上报时候调用的保存司机填入路线的接口
     * @param locationLineDTOs
     * @return
     */
    @Override
    public Response<Boolean> saveLine(List<LocationLineDTO> locationLineDTOs) {
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    @Override
    public com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, CarrierAssemDTO carrierAssemDTO, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(carrierAssemDTO.getAddressDTO().getDepartureCountyValue());
        locationLineDTO.setStartCity(carrierAssemDTO.getAddressDTO().getDepartureCityValue());
        locationLineDTO.setStartProvince(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue());
        locationLineDTO.setEndCounty(carrierAssemDTO.getAddressDTO().getReceiveCountyValue());
        locationLineDTO.setEndCity(carrierAssemDTO.getAddressDTO().getReceiveCityValue());
        locationLineDTO.setEndProvince(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, CarrierAssemDTO carrierAssemDTO, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(carrierAssemDTO.getAddressDTO().getDepartureCountyValue());
        driverDayLineDTO.setStartCity(carrierAssemDTO.getAddressDTO().getDepartureCityValue());
        driverDayLineDTO.setStartProvince(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue());
        driverDayLineDTO.setEndCounty(carrierAssemDTO.getAddressDTO().getReceiveCountyValue());
        driverDayLineDTO.setEndCity(carrierAssemDTO.getAddressDTO().getReceiveCityValue());
        driverDayLineDTO.setEndProvince(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }

    @Override
    public com.cy.location.service.dto.base.Response<Boolean> saveLine(Long driverId, WaybillInfoDTO waybillInfoDTO, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(waybillInfoDTO.getDepartureCountyValue());
        locationLineDTO.setStartCity(waybillInfoDTO.getDepartureCityValue());
        locationLineDTO.setStartProvince(waybillInfoDTO.getDepartureProvinceValue());
        locationLineDTO.setEndCounty(waybillInfoDTO.getReceiveCountyValue());
        locationLineDTO.setEndCity(waybillInfoDTO.getReceiveCityValue());
        locationLineDTO.setEndProvince(waybillInfoDTO.getReceiveProvinceValue());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saveDayLine(Long driverId, WaybillInfoDTO waybillInfoDTO, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(waybillInfoDTO.getDepartureCountyValue());
        driverDayLineDTO.setStartCity(waybillInfoDTO.getDepartureCityValue());
        driverDayLineDTO.setStartProvince(waybillInfoDTO.getDepartureProvinceValue());
        driverDayLineDTO.setEndCounty(waybillInfoDTO.getReceiveCountyValue());
        driverDayLineDTO.setEndCity(waybillInfoDTO.getReceiveCityValue());
        driverDayLineDTO.setEndProvince(waybillInfoDTO.getReceiveProvinceValue());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }

    @Override
    public Response<Boolean> saSaveLine(Long driverId, WaybillInfo waybillInfo, Integer trackType) {
        List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
        LocationLineDTO locationLineDTO = new LocationLineDTO();
        locationLineDTO.setType(trackType);
        locationLineDTO.setDriverId(driverId);
        locationLineDTO.setStartCounty(waybillInfo.getDepartureCountyValue());
        locationLineDTO.setStartCity(waybillInfo.getDepartureCityValue());
        locationLineDTO.setStartProvince(waybillInfo.getDepartureProvinceValue());
        locationLineDTO.setEndCounty(waybillInfo.getReceiveCountyValue());
        locationLineDTO.setEndCity(waybillInfo.getReceiveCityValue());
        locationLineDTO.setEndProvince(waybillInfo.getReceiveProvinceValue());
        locationLineDTOs.add(locationLineDTO);
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = locationLineService.saveLine(locationLineDTOs);
        return saveLineRes;
    }

    @Override
    public com.cy.pass.service.dto.base.Response<Boolean> saSaveDayLine(Long driverId, WaybillInfo waybillInfo, Integer trackType) {
        List<DriverDayLineDTO> driverDayLineDTOs = new ArrayList<>();
        DriverDayLineDTO driverDayLineDTO = new DriverDayLineDTO();
        driverDayLineDTO.setType(trackType);
        driverDayLineDTO.setDriverId(driverId);
        driverDayLineDTO.setStartCounty(waybillInfo.getDepartureCountyValue());
        driverDayLineDTO.setStartCity(waybillInfo.getDepartureCityValue());
        driverDayLineDTO.setStartProvince(waybillInfo.getDepartureProvinceValue());
        driverDayLineDTO.setEndCounty(waybillInfo.getReceiveCountyValue());
        driverDayLineDTO.setEndCity(waybillInfo.getReceiveCityValue());
        driverDayLineDTO.setEndProvince(waybillInfo.getReceiveProvinceValue());
        driverDayLineDTOs.add(driverDayLineDTO);
        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverDayLineService.saveDayLine(driverDayLineDTOs);
        return saveDayLineRes;
    }
}
