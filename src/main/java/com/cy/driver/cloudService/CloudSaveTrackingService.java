package com.cy.driver.cloudService;

import com.cy.rdcservice.service.dto.ModifyUserDTO;
import com.cy.rdcservice.service.dto.TrackingSaveDTO;

/**
 * @author zhangxy 2016/8/9 9:22
 */
public interface CloudSaveTrackingService {

    boolean saveWaybillTrackingByDrvier(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO);

    boolean saveCarrierTrackingByDrvier(Long carrierId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO);


}
