package com.cy.driver.cloudService.impl;

import com.cy.driver.cloudService.CloudCarrierService;
import com.cy.rdcservice.service.CarrierService;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.ModifyUserDTO;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangxy 2016/8/2 19:04
 */
@Service("cloudCarrierService")
public class CloudCarrierServiceImpl implements CloudCarrierService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CarrierService carrierService;

    @Override
    public CarrierAssemDTO queryCarrierDetail(Long carrierId) {
        Response<CarrierAssemDTO> response = carrierService.findAssem(carrierId);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务carrierService查询托单详情失败");
            return null;
        }
        return response.getData();
    }


    @Override
    public Response<Boolean> carrierSignIn(Long carrierId, String signNum, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = carrierService.carrierSign(carrierId,signNum,modifyUserDTO);
        if(response==null){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务carrierSign签收托单失败");
            return null;
        }
        return response;

    }


}
