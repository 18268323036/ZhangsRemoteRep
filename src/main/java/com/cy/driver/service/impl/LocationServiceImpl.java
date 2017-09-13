package com.cy.driver.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.domain.LocationBo;
import com.cy.driver.service.LocationService;
import com.cy.location.service.LocationQuery2Service;
import com.cy.location.service.LocationQueryService;
import com.cy.location.service.LocationSaveService;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.location.service.dto.LocationDTO;
import com.cy.location.service.dto.OrderCaseDTO;
import com.cy.location.service.dto.OrderCaseQueryDTO;
import com.cy.location.service.dto.base.Response;
import com.cy.pass.service.SystemCodeService;
import com.cy.pass.service.dto.SystemCodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wyh on 2015/7/28.
 */
@Service("locationService")
public class LocationServiceImpl implements LocationService {
    private static Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Resource
    private LocationSaveService locationSaveService;
    @Resource
    private SystemCodeService systemCodeService;
    @Resource
    private LocationQueryService locationQueryService;
    @Resource
    private LocationQuery2Service locationQuery2Service;

    @Override
    public Response<Boolean> saveLocation(LocationBo locationBo, long driverId, Byte authState) {
        LocationDTO saveDto = new LocationDTO();
        saveDto.setDriverId(driverId);
        saveDto.setLongitude(locationBo.getLongitude());
        saveDto.setLatitude(locationBo.getLatitude());
        saveDto.setProvince(locationBo.getProvince());
        saveDto.setCity(locationBo.getCity());
        saveDto.setCounty(locationBo.getCounty());
        saveDto.setTown(locationBo.getTown());
        saveDto.setLocation(locationBo.getAddress());
        saveDto.setType(locationBo.getType());
        saveDto.setAuthState(authState);
        saveDto.setBusinessId(locationBo.getBusinessId());
        if(locationBo.getBusinessId() == null) {
            saveDto.setBusinessSource(null);
        }else if(locationBo.getBusinessId() != null && StringUtils.isEmpty(locationBo.getSiteSource())){
            saveDto.setBusinessSource(1);
        }else {
            saveDto.setBusinessSource(Integer.parseInt(locationBo.getSiteSource()));
        }
        return locationSaveService.saveLocation(saveDto);
    }

    @Override
    public String findInitTimeValue() {
        try {
            List<SystemCodeDTO> list = systemCodeService.queryBusinessCode(Constants.APP_LOC_MINUTE_TIME);
            if (list == null || list.size() == 0) {
                return "";
            }
            return list.get(0).getValue();
        }catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("获得初始化数据出错", e);
            }
        }
        return "";
    }

    /**
     * 查询最新的位置服务
     */
    @Override
    public LastLocationDTO queryLastLocation(Long driverId) {
        Response<LastLocationDTO> response = locationQueryService.queryLastLocation(driverId);
        if (!response.isSuccess()) {
            logger.error("调用location服务查询司机最新位置失败,driverId={},返回信息={}", driverId, response.getMessage());
            return null;
        }
        if (response.getData() == null) {
            logger.error("调用location服务查询司机最新位置失败,driverId={},返回最新位置为空", driverId);
            return null;
        }
        return response.getData();
    }

    @Override
    public OrderCaseDTO isExistHistTracking(OrderCaseQueryDTO orderCaseQueryDTO){
        /**
         * 参数1=司机ID、参数2=开始时间、参数3=结束时间、参数4=起始地、参数5=目的地
         */
        com.cy.location.service.dto.base.Response<OrderCaseDTO> response = locationQuery2Service.isExistHistTracking(orderCaseQueryDTO);
        if (response == null) {
            logger.error("修改订单是否存在定位信息异常失败，调用location服务isExistHistTracking出错，orderId={}", orderCaseQueryDTO.getBusinessId());
            return null;
        }
        if (!response.isSuccess()) {
            logger.error("修改订单是否存在定位信息异常失败，调用location服务isExistHistTracking失败，orderId={},返回信息={}", orderCaseQueryDTO.getBusinessId(), response.getMessage());
            return null;
        }
        return response.getData();
    }
}
