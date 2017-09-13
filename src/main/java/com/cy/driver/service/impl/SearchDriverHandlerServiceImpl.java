package com.cy.driver.service.impl;

import com.cy.driver.common.util.ObjConverter;
import com.cy.driver.domain.DriverUserBO;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.SearchDriverHandlerService;
import com.cy.search.service.SearchDriverService;
import com.cy.search.service.dto.base.Response;
import com.cy.search.service.dto.response.DriverImgDTO;
import com.cy.search.service.dto.response.DriverUserInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wyh on 2016/3/11.
 */
@Service("searchDriverHandlerService")
public class SearchDriverHandlerServiceImpl implements SearchDriverHandlerService {
    private static Logger LOG = LoggerFactory.getLogger(SearchDriverHandlerServiceImpl.class);

    @Resource
    private SearchDriverService searchDriverService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Override
    public List<DriverImgDTO> findDriverImgs(List<Long> driverIds, Integer imgType) {
        if (imgType == null) {
            LOG.error("根据司机id和图片类型查询司机图片信息失败，imgType为空");
            return null;
        }
        try {
            Response<List<DriverImgDTO>>  response = searchDriverService.searchDriverImg(imgType, driverIds);
            if (!response.isSuccess()) {
                LOG.error("调用search服务根据图片类型和司机id查询司机图片信息失败，返回信息={}", response.getMessage());
            }
            return response.getData();
        } catch (Exception e) {
            LOG.error("调用search服务根据图片类型和司机id查询司机图片信息出现异常", e);
            return null;
        }
    }

    @Override
    public List<DriverUserInfoDTO> findDriverInfo(List<Long> driverIds) {
        try {
            Response<List<DriverUserInfoDTO>> response = searchDriverService.searchDriver(driverIds);
            if (!response.isSuccess()) {
                LOG.error("调用search服务根据司机id查询司机信息失败，返回信息={}", response.getMessage());
            }
            return response.getData();
        } catch (Exception e) {
            LOG.error("调用search服务根据司机id查询司机信息出现异常", e);
            return null;
        }
    }

    @Override
    public DriverUserInfoDTO findDriverInfo(Long driverId) {
        try {
            Response<DriverUserInfoDTO> response = searchDriverService.searchDriverUserInfo(driverId);
            if (!response.isSuccess()) {
                LOG.error("调用search服务根据司机id查询司机信息失败，返回信息={}", response.getMessage());
            }
            if (response.getData() != null) {
                return response.getData();
            }
            LOG.error("调用search服务根据司机id查询司机信息失败，返回的司机信息为空");
        } catch (Exception e) {
            LOG.error("调用search服务根据司机id查询司机信息出现异常", e);
        }
        com.cy.pass.service.dto.DriverUserInfoDTO driverDTO = driverUserHandlerService.getDriverInfo(driverId);
        if (driverDTO == null) {
            LOG.error("根据司机id查询司机信息失败，返回的司机信息为空");
            return null;
        } else {
            DriverUserBO driver = ObjConverter.convert(driverDTO, DriverUserBO.class);
            DriverUserInfoDTO dto = ObjConverter.convert(driver, DriverUserInfoDTO.class);
            if (driverDTO.getCarLengthNewest() != null) {
                dto.setCarLengthNewest(driverDTO.getCarLengthNewest().doubleValue());
            }
            if (driverDTO.getCarWeightNewest() != null) {
                dto.setCarWeightNewest(driverDTO.getCarWeightNewest().doubleValue());
            }
            if (driverDTO.getCarCubageNewest() != null) {
                dto.setCarCubageNewest(driverDTO.getCarCubageNewest().doubleValue());
            }
            return dto;
        }
    }

    @Override
    public DriverImgDTO findDriverImg(Long driverId, Integer imgType) {
        if (imgType == null) {
            LOG.error("根据司机id和图片类型查询司机图片信息失败，imgType为空");
            return null;
        }
        try {
            Response<DriverImgDTO> response = searchDriverService.searchDriverImg(imgType, driverId);
            if (!response.isSuccess()) {
                LOG.error("调用search服务根据图片类型和司机id查询司机图片信息失败，返回信息={}", response.getMessage());
            }
            return response.getData();
        } catch (Exception e) {
            LOG.error("调用search服务根据图片类型和司机id查询司机图片信息出现异常", e);
            return null;
        }
    }


}
