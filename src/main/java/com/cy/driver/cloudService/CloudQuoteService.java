package com.cy.driver.cloudService;

import com.cy.rdcservice.service.dto.CarrierQuoteDTO;


/**
 * @author zhangxy 2016/7/21
 */
public interface CloudQuoteService {

    boolean saveQuoteInfo(CarrierQuoteDTO carrierQuoteDTO);

    /**
     * 货源报价数量
     * @param driverId
     * @param userType
     * @return
     */
    long countByUser(Long driverId, Integer userType);
}
