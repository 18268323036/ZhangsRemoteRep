package com.cy.driver.cloudService.impl;

import com.cy.driver.cloudService.CloudSaveTrackingService;
import com.cy.rdcservice.service.CarrierTrackingService;
import com.cy.rdcservice.service.dto.ModifyUserDTO;
import com.cy.rdcservice.service.dto.TrackingSaveDTO;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author yanst 2016/8/9 9:26
 */
@Service("cloudSaveTrackingService")
public class CloudSaveTrackingServiceImpl implements CloudSaveTrackingService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CarrierTrackingService carrierTrackingService;

    @Override
    public boolean saveWaybillTrackingByDrvier(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = carrierTrackingService.saveWaybillTrackingByDriver(waybillId,trackingSaveDTO,modifyUserDTO);
        if(response==null){
            LOG.isDebugEnabled(); LOG.debug("调用carrierTrackingService.saveWaybillTrackingByDriver保存运单轨迹失败");
            return false;
        }
        if(!response.isSuccess() || !response.getData()){
            LOG.isDebugEnabled(); LOG.debug("调用carrierTrackingService.saveWaybillTrackingByDriver保存运单轨迹失败,失败原因{}",response.getMessage());
            return false;
        }
        return response.getData();
    }

    @Override
    public boolean saveCarrierTrackingByDrvier(Long carrierId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = carrierTrackingService.saveCarrierTrackingByDriver(carrierId,trackingSaveDTO,modifyUserDTO);
        if(response==null){
            LOG.isDebugEnabled(); LOG.debug("调用carrierTrackingService.saveCarrierTrackingByDriver保存运单轨迹失败");
            return false;
        }
        if(!response.isSuccess() || !response.getData()){
            LOG.isDebugEnabled(); LOG.debug("调用carrierTrackingService.saveCarrierTrackingByDriver保存运单轨迹失败,失败原因{}",response.getMessage());
            return false;
        }
        return response.getData();
    }


}
