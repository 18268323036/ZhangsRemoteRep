package com.cy.driver.service;

import com.cy.driver.domain.PageBase;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.CargoDTO;

import java.util.List;

/**
 * @author yanst 2016/4/20
 */
public interface CargoBrowseRecordService {

    /**
     * 添加货源浏览记录
     * @param driverId
     * @param cargoId
     * @param userType
     * @return
     */
    public Boolean add(Long driverId, Long cargoId, int userType, Integer cargoSource);

    /**
     * 浏览记录列表
     * @param driverId
     * @param page
     * @return
     */
    public PageBase<CargoDTO> pageList(Long driverId, Integer page);

    /**
     * 批量删除
     *
     * @param cargoIds
     * @param driverId
     * @return
     */
    public Boolean deletes(List<IdSourceDTO> cargoIds, Long driverId);


    /**
     * 浏览记录数
     * @param driverId
     * @return
     */
    public Long count(Long driverId);
}
