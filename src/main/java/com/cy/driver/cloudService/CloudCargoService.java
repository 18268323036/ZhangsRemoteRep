package com.cy.driver.cloudService;

import com.cy.driver.domain.FindCargo;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.Cargo2DTO;

/**
 * @author zhangxy 2016/7/20
 */
public interface CloudCargoService {

    PageResult<Cargo2DTO> findCargo(FindCargo findCargo, Long driverId);

    Cargo2DTO cargoDetails2(IdSourceDTO idSourceDTO);

    /**
     * 附近货源2（采用搜索引擎）支持区域配送
     * @param driverId
     * @param page
     * @param sort
     * @return
     * @author yanst
     */
    PageResult<Cargo2DTO> queryNearCargo2(Long driverId, int page, int sort);

    PageResult<Cargo2DTO> queryNearCargo3(Long driverId, int page, int sort, LastLocationDTO lastLoc, Double carLength, Double carWeight, Double carCubage, String vehicleType, String carriageType);

    /**
     * 附近货源数量2（采用搜索引擎） 支持区域配送
     * @param driverId
     * @return
     */
    long countNearCargo2(Long driverId);

    /**
     * 附近货源数量3（采用搜索引擎） 支持区域配送
     * @param driverId
     * @return
     */
    long countNearCargo3(Long driverId, LastLocationDTO lastLoc);

    /**
     * 货源明细(区域配送)货源详情正在使用
     */
    CarrierAssemDTO cargoDetailsRdc(Long cargoId);

}
