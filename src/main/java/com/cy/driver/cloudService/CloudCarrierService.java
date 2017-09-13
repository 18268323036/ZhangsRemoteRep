package com.cy.driver.cloudService;

import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.ModifyUserDTO;
import com.cy.top56.common.Response;

/**
 * @author zhangxy 2016/8/2
 */
public interface CloudCarrierService {

    /**
     * 查询托单详情
     */
    CarrierAssemDTO queryCarrierDetail(Long carrierId);

    Response<Boolean> carrierSignIn(Long carrierId, String signNum, ModifyUserDTO modifyUserDTO);

}
