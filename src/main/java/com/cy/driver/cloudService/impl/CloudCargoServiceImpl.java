package com.cy.driver.cloudService.impl;

import com.cy.driver.api.domain.req.FindCargo;
import com.cy.driver.cloudService.CloudCargoService;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.initdata.SysAreaData;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.service.EmptyCarReportService;
import com.cy.driver.service.LocationService;
import com.cy.driver.service.OftenCityHandleService;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.pass.service.dto.DriverBusinessLineDTO;
import com.cy.pass.service.dto.DriverEmptyReportInfoDTO;
import com.cy.pass.service.dto.DriverOftenCityDTO;
import com.cy.pass.service.dto.DriverTodayEmptyReportDTO;
import com.cy.rdcservice.service.CarrierService;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.search.service.SearchCargo2Service;
import com.cy.search.service.SearchCargo3Service;
import com.cy.search.service.dto.base.PageInfo;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.base.Response;
import com.cy.search.service.dto.request.*;
import com.cy.search.service.dto.response.Cargo2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangxy 2016/7/20 17:51
 */
@Service("cloudCargoService")
public class CloudCargoServiceImpl implements CloudCargoService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SearchCargo2Service searchCargo2Service;
    @Resource
    private LocationService locationService;
    @Resource
    private SystemData systemData;
    @Resource
    private OftenCityHandleService oftenCityHandleService;
    @Resource
    private EmptyCarReportService emptyCarReportService;
    @Resource
    private SearchCargo3Service searchCargo3Service;
    @Resource
    private CarrierService carrierService;


    @Override
    public PageResult<Cargo2DTO> findCargo(com.cy.driver.domain.FindCargo findCargo, Long driverId) {
        SearchCargoDTO searchCargoDTO = new SearchCargoDTO();
        searchCargoDTO.setUserId(driverId);
        searchCargoDTO.setEndCountyValue(findCargo.geteCountyValue());
        searchCargoDTO.setEndCountyCode(findCargo.geteCountyCode());
        if(findCargo.geteCityValue()!=null && findCargo.geteCityValue().equals("全部")){
            searchCargoDTO.setEndCityValue("");
            searchCargoDTO.setEndCityCode("");
        }else {
            searchCargoDTO.setEndCityValue(findCargo.geteCityValue());
            searchCargoDTO.setEndCityCode(findCargo.geteCityCode());
        }
        searchCargoDTO.setEndProvinceValue(findCargo.geteProValue());
        searchCargoDTO.setEndProvinceCode(findCargo.geteProCode());
        searchCargoDTO.setStartCountyValue(findCargo.getsCountyValue());
        searchCargoDTO.setStartCountyCode(findCargo.getsCountyCode());
        searchCargoDTO.setStartCityValue(findCargo.getsCityValue());
        searchCargoDTO.setStartCityCode(findCargo.getsCityCode());
        searchCargoDTO.setStartProvinceValue(findCargo.getsProValue());
        searchCargoDTO.setStartProvinceCode(findCargo.getsProCode());
        searchCargoDTO.setUserType(Constants.DRIVER);
        if(!StringUtils.isEmpty(findCargo.getCarLength())) {
            searchCargoDTO.setCarLengthEnd(Double.valueOf(findCargo.getCarLength()));
        }
        if(!StringUtils.isEmpty(findCargo.getWeight())) {
            searchCargoDTO.setCargoWeightEnd(Double.valueOf(findCargo.getWeight()));
        }
        if(!StringUtils.isEmpty(findCargo.getVolume())) {
            searchCargoDTO.setCargoCubageEnd(Double.valueOf(findCargo.getVolume()));
        }
        String vehicleType = findCargo.getVehicleTypeTwo();
        if (org.apache.commons.lang.StringUtils.isBlank(vehicleType)) {
            vehicleType = findCargo.getVehicleType();
        }
        List<String> vehicleTypes = SystemData.findQueryVehicleTypes(vehicleType);
        searchCargoDTO.setVehicleTypes(vehicleTypes);
        String carriageType = findCargo.getCarriageTypeTwo();
        if (org.apache.commons.lang.StringUtils.isBlank(carriageType)) {
            carriageType = findCargo.getCarriageType();
        }
        List<String> carriageTypes = SystemData.findQueryCarriageTypes(carriageType);
        searchCargoDTO.setCarriageTypes(carriageTypes);
        /**
         * 是否允许司机检索到该货源
         * 0 不允许 1 允许
         */
        List<Integer> allowDriverFindeds = new ArrayList<Integer>();
        allowDriverFindeds.add(1);
        searchCargoDTO.setAllowDriverFindeds(allowDriverFindeds);
        /**
         * 货源状态数组
         * 0 待交易 1 已交易 3 待承运 11待派单(区域配送)
         */
        List<Integer> cargoFlags = new ArrayList<Integer>();
        cargoFlags.add(0);
        cargoFlags.add(11);
        searchCargoDTO.setCargoFlags(cargoFlags);
        /**
         * 排序
         * 0默认排序、1认证排序、2车长升序、3车长降序、4重量升序、5重量降序
         */
        if (findCargo.getSort() != null) {
            searchCargoDTO.setSort(findCargo.getSort());
        } else {
            searchCargoDTO.setSort(0);
        }
        searchCargoDTO.setStartTime(DateUtil.parseDate(findCargo.getsTime(), DateUtil.F_DATE));
        searchCargoDTO.setEndTime(DateUtil.parseDate(findCargo.getsTime()+" 23:59:59", DateUtil.F_DATETIME));
        PageInfo<SearchCargoDTO> pageInfo = new PageInfo<>(findCargo.getPage());
        pageInfo.setData(searchCargoDTO);
        PageResult<Cargo2DTO> pageResult = searchCargo2Service.searchCargo(pageInfo);
        return pageResult;
    }

    @Override
    public Cargo2DTO cargoDetails2(IdSourceDTO idSourceDTO) {
        Response<Cargo2DTO> response = searchCargo2Service.cargoDetailsById(idSourceDTO);
        if (!response.isSuccess()) {
            LOG.debug("调用cy.search搜索货源详情出错");
            return null;
        }
        return response.getData();
    }

    /**
     * 附近货源2（采用搜索引擎） 支持区域配送
     */
    @Override
    public PageResult<Cargo2DTO> queryNearCargo2(Long driverId, int page, int sort) {
        SearchNearCargoDTO searchNearCargoDTO = convertQueryNearCargo(driverId, true);
        searchNearCargoDTO.setSort(sort);
        PageInfo pageInfo = new PageInfo(page);
        pageInfo.setData(searchNearCargoDTO);
        return searchCargo2Service.searchNearCargo(pageInfo);
    }

    @Override
    public PageResult<Cargo2DTO> queryNearCargo3(Long driverId,int page,int sort,LastLocationDTO lastLoc,Double carLength,Double carWeight,Double carCubage,String vehicleType,String carriageType) {
        SearchNearCargo3DTO searchNearCargo3DTO = new SearchNearCargo3DTO();
        searchNearCargo3DTO.setSort(sort);
        DriverInfo3DTO driverInfo3DTO = new DriverInfo3DTO();
        driverInfo3DTO.setDriverId(driverId);
        driverInfo3DTO.setCarriageType(carriageType);
        driverInfo3DTO.setVehicleType(vehicleType);
        driverInfo3DTO.setCarWeight(carWeight);
        driverInfo3DTO.setCarLength(carLength);
        driverInfo3DTO.setCarCubage(carCubage);
        Location3DTO location3DTO = new Location3DTO();
        location3DTO.setLongitude(lastLoc.getLongitude());
        location3DTO.setLatitude(lastLoc.getLatitude());
        location3DTO.setProvince(lastLoc.getProvince());
        location3DTO.setCity(lastLoc.getCity());
        searchNearCargo3DTO.setDriverInfo(driverInfo3DTO);
        searchNearCargo3DTO.setCurrentLoc(location3DTO);
        PageInfo<SearchNearCargo3DTO> pageInfo = new PageInfo(page);
        pageInfo.setData(searchNearCargo3DTO);
        return searchCargo3Service.searchNearCargo(pageInfo);
    }

    /**
     * 附近货源数量2（采用搜索引擎） 支持区域配送
     * @param driverId
     * @return
     */
    @Override
    public long countNearCargo2(Long driverId) {
        SearchNearCargoDTO searchNearCargoDTO = convertQueryNearCargo(driverId, true);
        Response<Long> response = searchCargo2Service.countNearCargo(searchNearCargoDTO);
        if(response == null){
            LOG.error("获取附近货源数量失败，返回对象为空");
            return 0L;
        }
        if(!response.isSuccess()){
            LOG.error("获取附近货源数量异常，返回信息：{}", response.getMessage());
            return 0L;
        }
        return response.getData();
    }

    @Override
    public long countNearCargo3(Long driverId,LastLocationDTO lastLoc) {
        Location3DTO location3DTO = new Location3DTO();
        location3DTO.setLongitude(lastLoc.getLongitude());
        location3DTO.setLatitude(lastLoc.getLatitude());
        location3DTO.setCity(lastLoc.getCity());
        location3DTO.setProvince(lastLoc.getProvince());
        Response<Long> response = searchCargo3Service.countNearCargo(driverId,location3DTO);
        if(response == null){
            LOG.error("获取附近货源数量失败，返回对象为空");
            return 0L;
        }
        if(!response.isSuccess()){
            LOG.error("获取附近货源数量异常，返回信息：{}", response.getMessage());
            return 0L;
        }
        return response.getData();
    }


    /**
     * 货源详情
     * @param cargoId
     * @return
     */
    @Override
    public CarrierAssemDTO cargoDetailsRdc(Long cargoId) {
        com.cy.top56.common.Response<CarrierAssemDTO> response = carrierService.findAssem(cargoId);
        if (response == null) {
            LOG.error("调用底层区域配送数据中心rdcService服务的根据货源id查询货源详情失败，响应数据Response为空");
            return null;
        }
        if (!response.isSuccess()) {
            LOG.error("调用底层区域配送数据中心rdcService服务的根据货源id查询货源详情出错，出错信息code={},message={}", response.getCode(), response.getMessage());
            return null;
        }
        return response.getData();
    }



    /**
     * 附近货源的查询对象转换
     */
    private SearchNearCargoDTO convertQueryNearCargo(Long driverId,boolean isLocationInfo){
        LocationDTO locationDTO = new LocationDTO();
        if(isLocationInfo){
            if (driverId == 103759) {
                /** TODO ios审核使用 */
                locationDTO.setProvinceValue(Constants.DEFAULT_PROVINCE);
                locationDTO.setProvinceCode(Constants.DEFAULT_PROVINCE_CODE);
                locationDTO.setCityValue(Constants.DEFAULT_CITY);
                locationDTO.setCityCode(Constants.DEFAULT_CITY_CODE);
            } else {
                /** 获得司机的最新位置 */
                LastLocationDTO lastLoc = locationService.queryLastLocation(driverId);
                if (lastLoc != null) {
                    SysAreaData proObj = systemData.getProvinceInfoByVal(lastLoc.getProvince());
                    if(proObj != null){
                        locationDTO.setProvinceValue(proObj.getAreaName());
                        locationDTO.setProvinceCode(proObj.getAreaCode());
                    }

                    SysAreaData cityObj = systemData.getCityInfoByVal(lastLoc.getCity());
                    if(cityObj != null){
                        locationDTO.setCityValue(cityObj.getAreaName());
                        locationDTO.setCityCode(cityObj.getAreaCode());
                    }
                } else {
                    locationDTO.setProvinceValue(Constants.DEFAULT_PROVINCE);
                    locationDTO.setProvinceCode(Constants.DEFAULT_PROVINCE_CODE);
                    locationDTO.setCityValue(Constants.DEFAULT_CITY);
                    locationDTO.setCityCode(Constants.DEFAULT_CITY_CODE);
                }
            }
        }

        //常跑城市
        List<DriverOftenCityDTO> oftenCityList = oftenCityHandleService.queryList(driverId);
        List<LocationDTO> oftenCitys = new ArrayList<LocationDTO>();
        if (oftenCityList != null && oftenCityList.size() > 0) {
            for (DriverOftenCityDTO driverOftenCityDTO : oftenCityList) {
                LocationDTO dto = new LocationDTO();
                dto.setProvinceValue(driverOftenCityDTO.getProvinceValue());
                dto.setProvinceCode(driverOftenCityDTO.getProvinceCode());
                dto.setCityValue(driverOftenCityDTO.getCityValue());
                dto.setCityCode(driverOftenCityDTO.getCityCode());
                oftenCitys.add(dto);
            }
        }

        //空车上报
        DriverEmptyReportInfoDTO emptyDTO = emptyCarReportService.queryEmptyList(driverId);
        List<CityLineDTO> emptyList = convertEmptyList(emptyDTO);

        SearchNearCargoDTO searchNearCargoDTO = new SearchNearCargoDTO();
        searchNearCargoDTO.setCurrentPosition(locationDTO);
        searchNearCargoDTO.setDriverId(driverId);
        searchNearCargoDTO.setOftenCityList(oftenCitys);
        searchNearCargoDTO.setEmptyCityList(emptyList);
        return searchNearCargoDTO;
    }

    private List<CityLineDTO> convertEmptyList(DriverEmptyReportInfoDTO emptyDTO){
        List<CityLineDTO> list = new ArrayList<CityLineDTO>();
        if (emptyDTO.getTodayEmptyCarList() != null
                && emptyDTO.getTodayEmptyCarList().size() > 0) {
            for (DriverTodayEmptyReportDTO item : emptyDTO.getTodayEmptyCarList()) {
                CityLineDTO cityLineDTO = new CityLineDTO();
                cityLineDTO.setStartProvinceValue(item.getStartProvinceValue());
                cityLineDTO.setStartProvinceCode(item.getStartProvinceCode());
                cityLineDTO.setStartCityValue(item.getStartCityValue());
                cityLineDTO.setStartCityCode(item.getStartCityCode());

                cityLineDTO.setEndProvinceValue(item.getEndProvinceValue());
                cityLineDTO.setEndProvinceCode(item.getEndProvinceCode());
                cityLineDTO.setEndCityValue(item.getEndCityValue());
                cityLineDTO.setEndCityCode(item.getEndCityCode());
                cityLineDTO.setStartDate(item.getStartTime());
                cityLineDTO.setEndDate(item.getStartTime());
                if (item.getStartTime() != null)
                    cityLineDTO.setModifyTimes(item.getStartTime().getTime());
                list.add(cityLineDTO);
            }
        }
        if (emptyDTO.getFutureEmptyCarList() != null
                && emptyDTO.getFutureEmptyCarList().size() > 0) {
            for (DriverBusinessLineDTO item : emptyDTO.getFutureEmptyCarList()) {
                CityLineDTO cityLineDTO = new CityLineDTO();
                cityLineDTO.setStartProvinceValue(item.getStartProvinceValue());
                cityLineDTO.setStartProvinceCode(item.getStartProvinceCode());
                cityLineDTO.setStartCityValue(item.getStartCityValue());
                cityLineDTO.setStartCityCode(item.getStartCityCode());

                cityLineDTO.setEndProvinceValue(item.getEndProvinceValue());
                cityLineDTO.setEndProvinceCode(item.getEndProvinceCode());
                cityLineDTO.setEndCityValue(item.getEndCityValue());
                cityLineDTO.setEndCityCode(item.getEndCityCode());
                cityLineDTO.setStartDate(item.getStartTime());
                cityLineDTO.setEndDate(item.getEndTime());
                Date date = item.getModifyTime();
                if(date == null){
                    date = item.getStartTime();
                }
                cityLineDTO.setModifyTimes(date.getTime());
                list.add(cityLineDTO);
            }
        }
        return list;
    }
}
