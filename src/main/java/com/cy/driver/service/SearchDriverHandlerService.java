package com.cy.driver.service;

import com.cy.search.service.dto.response.DriverImgDTO;
import com.cy.search.service.dto.response.DriverUserInfoDTO;

import java.util.List;

/**
 * Created by wyh on 2016/3/11.
 */
public interface SearchDriverHandlerService {

    /**
     * 根据司机id和图片类型查询司机图片信息
     * @param driverIds 司机id集合
     * @param imgType 图片类型
     * @return
     * @author yanst
     */
    List<DriverImgDTO> findDriverImgs(List<Long> driverIds, Integer imgType);

    /**
     * 根据司机id查询司机信息
     *
     * @param driverIds 司机id
     * @return
     * @author yanst
     */
    List<DriverUserInfoDTO> findDriverInfo(List<Long> driverIds);

    /**
     * 根据司机id查询司机信息
     * @param driverId
     * @return
     * @author wyh
     */
    DriverUserInfoDTO findDriverInfo(Long driverId);

    /**
     * 根据司机id和图片类型查询司机图片信息
     * @param driverId
     * @param imgType
     * @return
     * @author wyh
     */
    DriverImgDTO findDriverImg(Long driverId, Integer imgType);
}
