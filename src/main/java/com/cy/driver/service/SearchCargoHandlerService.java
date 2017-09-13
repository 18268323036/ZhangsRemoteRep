package com.cy.driver.service;

import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.domain.SearchCargoBO;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.response.Cargo2DTO;
import com.cy.search.service.dto.response.CargoDTO;

/**
 * Created by wyh on 2016/3/8.
 */
public interface SearchCargoHandlerService {

    /**
     * 附近货源数量（采用搜索引擎）
     * @param driverId
     * @return
     * @author wyh
     */
    long countNearCargo(Long driverId, String longitude, String latitude);


    /**
     * 精准货源（采用搜索引擎）
     */
    PageResult<CargoDTO> accurateCargo(Long driverId, int page);

    /**
     * 精准货源3.5.1
     */
    PageResult<Cargo2DTO> accurateCargo3(Long driverId, int page, LastLocationDTO lastLoc, Double carLength, Double carWeight, Double carCubage, String vehicleType, String carriageType);

    /**
     * 附近货源（采用搜索引擎）
     * @param driverId
     * @param page
     * @param sort
     * @return
     * @author wyh
     */
    PageResult<Cargo2DTO> queryNearCargo(Long driverId, int page, int sort, String longitude, String latitude);

    /**
     * 搜索货源（采用搜索引擎）
     * @param searchBO
     * @return
     * @author wyh
     */
    PageResult<CargoDTO> queryCargoList(SearchCargoBO searchBO);

    /**
     * 货源详情
     * @param cargoId
     * @return
     * @author wyh
     */
    CargoDTO cargoDetails(Long cargoId);

    /**
     * 德邦查询货源接口
     * @param driverId
     * @param city
     * @param page
     * @return
     */
    com.cy.cargo.service.dto.base.PageResult<CargoInfoDTO> pageByCustom(Long driverId, String city, int page);
}
