//package com.cy.driver.service.impl;
//
//import com.alibaba.dubbo.common.utils.StringUtils;
//import com.cy.cargo.service.CargoQueryService;
//import com.cy.cargo.service.dto.CargoCustomParamDTO;
//import com.cy.cargo.service.dto.CargoInfoDTO;
//import com.cy.driver.common.constants.Constants;
//import com.cy.driver.common.initdata.SysAreaData;
//import com.cy.driver.common.initdata.SystemData;
//import com.cy.driver.domain.SearchCargoBO;
//import com.cy.driver.service.EmptyCarReportService;
//import com.cy.driver.service.LocationService;
//import com.cy.driver.service.OftenCityHandleService;
//import com.cy.driver.service.SearchCargoHandlerService;
//import com.cy.location.service.dto.LastLocationDTO;
//import com.cy.pass.service.InitializationDataService;
//import com.cy.pass.service.dto.*;
//import com.cy.pass.service.dto.Enum.AreaLevel;
//import com.cy.search.service.SearchCargo3Service;
//import com.cy.search.service.SearchCargoService;
//import com.cy.search.service.dto.base.PageInfo;
//import com.cy.search.service.dto.base.PageResult;
//import com.cy.search.service.dto.base.Response;
//import com.cy.search.service.dto.request.*;
//import com.cy.search.service.dto.response.Cargo2DTO;
//import com.cy.search.service.dto.response.CargoDTO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by wyh on 2016/3/8.
// */
//@Service("searchCargoHandlerService")
//public class SearchCargoHandlerServiceImpl implements SearchCargoHandlerService {
//    private static final Logger LOG = LoggerFactory.getLogger(SearchCargoHandlerServiceImpl.class);
//
//    @Resource
//    private SearchCargoService searchCargoService;
//    @Resource
//    private LocationService locationService;
//    @Resource
//    private SystemData systemData;
//    @Resource
//    private OftenCityHandleService oftenCityHandleService;
//    @Resource
//    private EmptyCarReportService emptyCarReportService;
//    @Resource
//    private InitializationDataService initializationDataService;
//    @Resource
//    private SearchCargo3Service searchCargo3Service;
//    @Resource
//    private CargoQueryService cargoQueryService;
//
//    /**
//     * 精准货源（采用搜索引擎）
//     */
//    @Override
//    public PageResult<CargoDTO> accurateCargo(Long driverId, int page) {
//        SearchNearCargoDTO searchNearCargoDTO = convertQueryNearCargo(driverId, false);
//        PageInfo<SearchAccurateCargoDTO> pageInfo = new PageInfo(page);
//        SearchAccurateCargoDTO searchAccurateCargoDTO = new SearchAccurateCargoDTO();
//        searchAccurateCargoDTO.setDriverId(searchNearCargoDTO.getDriverId());
//        searchAccurateCargoDTO.setEmptyCityList(searchNearCargoDTO.getEmptyCityList());
//        searchAccurateCargoDTO.setOftenCityList(searchNearCargoDTO.getOftenCityList());
//        pageInfo.setData(searchAccurateCargoDTO);
//        return searchCargoService.searchAccurateCargo(pageInfo);
//    }
//
//    @Override
//    public PageResult<Cargo2DTO> accurateCargo3(Long driverId, int page,LastLocationDTO lastLoc,Double carLength,Double carWeight,Double carCubage,String vehicleType,String carriageType) {
//        PageInfo<SearchAccurateCargo3DTO> pageInfo = new PageInfo(page);
//        SearchAccurateCargo3DTO searchAccurateCargo3DTO = new SearchAccurateCargo3DTO();
//        DriverInfo3DTO driverInfo3DTO = new DriverInfo3DTO();
//        driverInfo3DTO.setDriverId(driverId);
//        driverInfo3DTO.setCarriageType(carriageType);
//        driverInfo3DTO.setCarCubage(carCubage);
//        driverInfo3DTO.setCarLength(carLength);
//        driverInfo3DTO.setCarWeight(carWeight);
//        driverInfo3DTO.setVehicleType(vehicleType);
//        Location3DTO location3DTO = new Location3DTO();
//        location3DTO.setProvince(lastLoc.getProvince());
//        location3DTO.setCity(lastLoc.getCity());
//        location3DTO.setLatitude(lastLoc.getLatitude());
//        location3DTO.setLongitude(lastLoc.getLongitude());
//        searchAccurateCargo3DTO.setCurrentLoc(location3DTO);
//        searchAccurateCargo3DTO.setDriverInfo(driverInfo3DTO);
//        pageInfo.setData(searchAccurateCargo3DTO);
//        return searchCargo3Service.searchAccurateCargo(pageInfo);
//    }
//
//    /**
//     * 附近货源数量（采用搜索引擎）
//     */
//    @Override
//    public long countNearCargo(Long driverId,String longitude, String latitude) {
////        SearchNearCargoDTO searchNearCargoDTO = convertQueryNearCargo(driverId, true);
//        Location3DTO location3DTO = new Location3DTO();
//        location3DTO.setLongitude(longitude);
//        location3DTO.setLatitude(latitude);
//        Response<Long> result = searchCargo3Service.countNearCargo(driverId,location3DTO);
//        if (!result.isSuccess()) {
//            LOG.error("调用search服务查询附近货源数量失败，失败信息={}", result.getMessage());
//            return 0;
//        }
//        return result.getData();
//    }
//
//    /**
//     * 附近货源（采用搜索引擎）
//     */
//    @Override
//    public PageResult<Cargo2DTO> queryNearCargo(Long driverId, int page, int sort, String longitude, String latitude) {
////        SearchNearCargoDTO searchNearCargoDTO = convertQueryNearCargo(driverId, true);
//        SearchNearCargo3DTO searchNearCargo3DTO = new SearchNearCargo3DTO();
//        searchNearCargo3DTO.setSort(sort);
//        DriverInfo3DTO driverInfo3DTO = new DriverInfo3DTO();
//        driverInfo3DTO.setDriverId(driverId);
//        Location3DTO location3DTO = new Location3DTO();
//        location3DTO.setLongitude(longitude);
//        location3DTO.setLatitude(latitude);
//        PageInfo pageInfo = new PageInfo(page);
//        pageInfo.setData(searchNearCargo3DTO);
//        return searchCargo3Service.searchNearCargo(pageInfo);
//    }
//
//    /**
//     * 搜索货源（采用搜索引擎）
//     */
//    @Override
//    public PageResult<CargoDTO> queryCargoList(SearchCargoBO searchBO) {
//        PageInfo<SearchCargoDTO> pageInfo = new PageInfo(searchBO.getPage());
//        SearchCargoDTO searchDTO = new SearchCargoDTO();
//        searchDTO.setStartTime(searchBO.getStartTime());
//        searchDTO.setEndTime(searchBO.getStartTime());
//        if (!StringUtils.isEmpty(searchBO.getStartProvinceCode())) {
//            AreaTableDTO sProObj = getAreaTableObj(searchBO.getStartProvinceCode(), AreaLevel.PROVICE.getValue());
//            if (sProObj != null) {
//                searchDTO.setStartProvinceCode(sProObj.getCode());
//                searchDTO.setStartProvinceValue(sProObj.getValue());
//            }
//        }
//        if (!StringUtils.isEmpty(searchBO.getStartCityCode())) {
//            AreaTableDTO sProObj = getAreaTableObj(searchBO.getStartCityCode(), AreaLevel.CITY.getValue());
//            if (sProObj != null) {
//                searchDTO.setStartCityCode(sProObj.getCode());
//                searchDTO.setStartCityValue(sProObj.getValue());
//            }
//        }
//        if (!StringUtils.isEmpty(searchBO.getEndProvinceCode())) {
//            AreaTableDTO sProObj = getAreaTableObj(searchBO.getEndProvinceCode(), AreaLevel.PROVICE.getValue());
//            if (sProObj != null) {
//                searchDTO.setEndProvinceCode(sProObj.getCode());
//                searchDTO.setEndProvinceValue(sProObj.getValue());
//            }
//        }
//        if (!StringUtils.isEmpty(searchBO.getEndCityCode())) {
//            AreaTableDTO sProObj = getAreaTableObj(searchBO.getEndCityCode(), AreaLevel.CITY.getValue());
//            if (sProObj != null) {
//                searchDTO.setEndCityCode(sProObj.getCode());
//                searchDTO.setEndCityValue(sProObj.getValue());
//            }
//        }
//        searchDTO.setCarLengthEnd(searchBO.getCarLength());
//        searchDTO.setCargoWeightEnd(searchBO.getCargoWeight());
//        searchDTO.setCargoCubageEnd(searchBO.getCargoCubage());
//
//        List<Integer> allowDriverFindeds = new ArrayList<Integer>();
//        allowDriverFindeds.add(1);
//        searchDTO.setAllowDriverFindeds(allowDriverFindeds);
//
//        List<String> vehicleTypes = systemData.findQueryVehicleTypes(searchBO.getVehicleType());
//        searchDTO.setVehicleTypes(vehicleTypes);
//
//        List<String> carriageTypes = systemData.findQueryCarriageTypes(searchBO.getCarriageType());
//        searchDTO.setCarriageTypes(carriageTypes);
//
//        List<Integer> cargoFlags = new ArrayList<Integer>();
//        cargoFlags.add(0);
//        searchDTO.setCargoFlags(cargoFlags);
//
//        searchDTO.setSort(searchBO.getSort());
//        searchDTO.setUserId(searchBO.getDriverId());
//        searchDTO.setUserType(1);
//
//        pageInfo.setData(searchDTO);
//        return searchCargoService.searchCargo(pageInfo);
//    }
//
//    /**
//     * 货源详情
//     */
//    @Override
//    public CargoDTO cargoDetails(Long cargoId) {
//        Response<CargoDTO> response = searchCargoService.cargoDetailsById(cargoId);
//        if (!response.isSuccess()) {
//            LOG.error("调用search服务根据id查询货源详情失败，失败信息={}", response.getMessage());
//            return null;
//        }
//        if (response.getData() == null) {
//            LOG.error("调用search服务根据id查询货源详情失败，返回货源数据为空");
//        }
//        return response.getData();
//    }
//
//    /**
//     * 省市编号转换名称
//     */
//    private AreaTableDTO getAreaTableObj(String code, int level) {
//        try {
//            return initializationDataService.getAreaTableObj(code, level);
//        } catch (Exception e) {
//            LOG.error("省市编号转换名称出现异常", e);
//            return null;
//        }
//    }
//
//    /**
//     * 附近货源的查询对象转换
//     */
//    private SearchNearCargoDTO convertQueryNearCargo(Long driverId, boolean isLocationInfo) {
//        LocationDTO locationDTO = new LocationDTO();
//        if (isLocationInfo) {
//            if (driverId == 103759) {
//                /** TODO ios审核使用 */
//                locationDTO.setProvinceValue(Constants.DEFAULT_PROVINCE);
//                locationDTO.setProvinceCode(Constants.DEFAULT_PROVINCE_CODE);
//                locationDTO.setCityValue(Constants.DEFAULT_CITY);
//                locationDTO.setCityCode(Constants.DEFAULT_CITY_CODE);
//            } else {
//                /** 获得司机的最新位置 */
//                LastLocationDTO lastLoc = locationService.queryLastLocation(driverId);
//                if (lastLoc != null) {
//                    SysAreaData proObj = systemData.getProvinceInfoByVal(lastLoc.getProvince());
//                    if (proObj != null) {
//                        locationDTO.setProvinceValue(proObj.getAreaName());
//                        locationDTO.setProvinceCode(proObj.getAreaCode());
//                    }
//
//                    SysAreaData cityObj = systemData.getCityInfoByVal(lastLoc.getCity());
//                    if (cityObj != null) {
//                        locationDTO.setCityValue(cityObj.getAreaName());
//                        locationDTO.setCityCode(cityObj.getAreaCode());
//                    }
//                } else {
//                    locationDTO.setProvinceValue(Constants.DEFAULT_PROVINCE);
//                    locationDTO.setProvinceCode(Constants.DEFAULT_PROVINCE_CODE);
//                    locationDTO.setCityValue(Constants.DEFAULT_CITY);
//                    locationDTO.setCityCode(Constants.DEFAULT_CITY_CODE);
//                }
//            }
//        }
//
//        //常跑城市
//        List<DriverOftenCityDTO> oftenCityList = oftenCityHandleService.queryList(driverId);
//        List<LocationDTO> oftenCitys = new ArrayList<LocationDTO>();
//        if (oftenCityList != null && oftenCityList.size() > 0) {
//            for (DriverOftenCityDTO driverOftenCityDTO : oftenCityList) {
//                LocationDTO dto = new LocationDTO();
//                dto.setProvinceValue(driverOftenCityDTO.getProvinceValue());
//                dto.setProvinceCode(driverOftenCityDTO.getProvinceCode());
//                dto.setCityValue(driverOftenCityDTO.getCityValue());
//                dto.setCityCode(driverOftenCityDTO.getCityCode());
//                oftenCitys.add(dto);
//            }
//        }
//
//        //空车上报
//        DriverEmptyReportInfoDTO emptyDTO = emptyCarReportService.queryEmptyList(driverId);
//        List<CityLineDTO> emptyList = convertEmptyList(emptyDTO);
//
//        SearchNearCargoDTO searchNearCargoDTO = new SearchNearCargoDTO();
//        searchNearCargoDTO.setCurrentPosition(locationDTO);
//        searchNearCargoDTO.setDriverId(driverId);
//        searchNearCargoDTO.setOftenCityList(oftenCitys);
//        searchNearCargoDTO.setEmptyCityList(emptyList);
//        return searchNearCargoDTO;
//    }
//
//    private List<CityLineDTO> convertEmptyList(DriverEmptyReportInfoDTO emptyDTO) {
//        List<CityLineDTO> list = new ArrayList<CityLineDTO>();
//        if (emptyDTO.getTodayEmptyCarList() != null
//                && emptyDTO.getTodayEmptyCarList().size() > 0) {
//            for (DriverTodayEmptyReportDTO item : emptyDTO.getTodayEmptyCarList()) {
//                CityLineDTO cityLineDTO = new CityLineDTO();
//                cityLineDTO.setStartProvinceValue(item.getStartProvinceValue());
//                cityLineDTO.setStartProvinceCode(item.getStartProvinceCode());
//                cityLineDTO.setStartCityValue(item.getStartCityValue());
//                cityLineDTO.setStartCityCode(item.getStartCityCode());
//
//                cityLineDTO.setEndProvinceValue(item.getEndProvinceValue());
//                cityLineDTO.setEndProvinceCode(item.getEndProvinceCode());
//                cityLineDTO.setEndCityValue(item.getEndCityValue());
//                cityLineDTO.setEndCityCode(item.getEndCityCode());
//                cityLineDTO.setStartDate(item.getStartTime());
//                cityLineDTO.setEndDate(item.getStartTime());
//                if (item.getStartTime() != null)
//                    cityLineDTO.setModifyTimes(item.getStartTime().getTime());
//                list.add(cityLineDTO);
//            }
//        }
//        if (emptyDTO.getFutureEmptyCarList() != null
//                && emptyDTO.getFutureEmptyCarList().size() > 0) {
//            for (DriverBusinessLineDTO item : emptyDTO.getFutureEmptyCarList()) {
//                CityLineDTO cityLineDTO = new CityLineDTO();
//                cityLineDTO.setStartProvinceValue(item.getStartProvinceValue());
//                cityLineDTO.setStartProvinceCode(item.getStartProvinceCode());
//                cityLineDTO.setStartCityValue(item.getStartCityValue());
//                cityLineDTO.setStartCityCode(item.getStartCityCode());
//
//                cityLineDTO.setEndProvinceValue(item.getEndProvinceValue());
//                cityLineDTO.setEndProvinceCode(item.getEndProvinceCode());
//                cityLineDTO.setEndCityValue(item.getEndCityValue());
//                cityLineDTO.setEndCityCode(item.getEndCityCode());
//                cityLineDTO.setStartDate(item.getStartTime());
//                cityLineDTO.setEndDate(item.getEndTime());
//                Date date = item.getModifyTime();
//                if (date == null) {
//                    date = item.getStartTime();
//                }
//                cityLineDTO.setModifyTimes(date.getTime());
//                list.add(cityLineDTO);
//            }
//        }
//        return list;
//    }
//
//    @Override
//    public com.cy.cargo.service.dto.base.PageResult<CargoInfoDTO> pageByCustom(Long driverId, String city, int page) {
//        com.cy.cargo.service.dto.base.PageInfo<CargoCustomParamDTO> pageInfo = new com.cy.cargo.service.dto.base.PageInfo<CargoCustomParamDTO>(page);
//        CargoCustomParamDTO cargoCustomParamDTO = new CargoCustomParamDTO();
//        cargoCustomParamDTO.setUserId(driverId);
//        cargoCustomParamDTO.setStartProvince("浙江省");
//        cargoCustomParamDTO.setStartCity(city);
//        cargoCustomParamDTO.setCargoCubage(new Double(30));
//        cargoCustomParamDTO.setCargoWeight(new Double(5));
//        cargoCustomParamDTO.setUserType((byte)1);
//        cargoCustomParamDTO.setSort(1);
//        pageInfo.setData(cargoCustomParamDTO);
//        com.cy.cargo.service.dto.base.PageResult<CargoInfoDTO> pageResult = cargoQueryService.pageByCustom(pageInfo);
//        return pageResult;
//    }
//}
