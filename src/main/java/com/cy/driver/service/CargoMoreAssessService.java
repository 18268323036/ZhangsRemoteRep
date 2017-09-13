package com.cy.driver.service;

import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.domain.CargoMoreAssessListBO;

/**
 * Created by Administrator on 2015/7/12.
 */
public interface CargoMoreAssessService {
    /**
     * 更多点评
     * @param driverId
     * @param page
     * @return
     */
    public Response<CargoMoreAssessListBO> getCargoMoreAssess(long driverId, int page, long cargoId);
}
