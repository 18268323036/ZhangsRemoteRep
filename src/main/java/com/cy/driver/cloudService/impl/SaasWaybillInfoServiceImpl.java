package com.cy.driver.cloudService.impl;

import com.alibaba.fastjson.JSONObject;
import com.cy.driver.cloudService.SaasWaybillInfoService;
import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import com.cy.saas.basic.model.dto.SystemProtocolDTO;
import com.cy.saas.basic.service.AccountUserService;
import com.cy.saas.basic.service.SystemProtocolService;
import com.cy.saas.business.model.dto.*;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.saas.business.service.*;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by nixianjing on 17/5/31.
 */
@Service("saasWaybillInfoService")
public class SaasWaybillInfoServiceImpl implements SaasWaybillInfoService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private WaybillInfoService saWaybillInfoService;
    @Resource
    private CarrierService saCarrierService;
    @Resource
    private AccountUserService accountUserService;
    @Resource
    private UserItemStatService saUserItemStatService;

    @Resource
    private WaybillOperService saWaybillOperService;
    @Resource
    private CarrierTrackingService saCarrierTrackingService;
    @Resource
    private SystemProtocolService saSystemProtocolService;




    @Override
    public WaybillDetailsDTO getWaybillDetails(Long waybillId) {
        Response<WaybillDetailsDTO> response = saWaybillInfoService.getWaybillDetails(waybillId);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用saasService服务waybillInfoService查询运单和运单生成的托单详情失败");
            return null;
        }
        return response.getData();
    }

    @Override
    public WaybillInfo getWaybillByParentWaybillId(Long parentWaybillId) {
        Response<WaybillInfo> response = saWaybillInfoService.getWaybillByParentWaybillId(parentWaybillId);
        if(response==null){
            if(LOG.isDebugEnabled()) LOG.debug("调用saas服务getWaybillByParentWaybillId查询子运单");
            return null;
        }
        if(!response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用saas服务getWaybillByParentWaybillId查询子运单失败,失败原因{}",response.getMessage());
            return null;
        }
        return response.getData();
    }

    @Override
    public List<Integer> countWaitSignBySourceWaybillIds(List<Long> sourceWaybillIds) {
        Response<List<Integer>> response = saCarrierService.countWaitSignBySourceWaybillIds(sourceWaybillIds);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用saas服务countWaitSignBySourceWaybillIds查询签收信息失败");
            return null;
        }
        return response.getData();
    }

    @Override
    public AccountUserDetails2DTO getAccountUserDetails2(Long userId) {
        Response<AccountUserDetails2DTO> response =  accountUserService.getAccountUserDetails2(userId);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务carrierService查询签收信息失败");
            return null;
        }
        return response.getData();
    }


    @Override
    public UserItemStatDTO getByUserId(Long userId) {
        Response<UserItemStatDTO> response = saUserItemStatService.getByUserId(userId);
        if(response == null){
            LOG.error("获取用户业务信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取用户业务信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }

    @Override
    public boolean modifyWaybillStateNew(WaybillStateModifyDTO waybillStateModifyDTO, Protocol2DTO protocol2DTO) {
        Response<Boolean> response = saWaybillOperService.modifyWaybillStateNew( waybillStateModifyDTO,  protocol2DTO);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillOperService改变运单状态失败");
            return false;
        }
        if(response.isSuccess() && response.getData()!=null && response.getData()){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveTrackingByYPS(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = saCarrierTrackingService.saveWaybillTrackingByDriver(waybillId ,trackingSaveDTO,  modifyUserDTO);
        if(response == null){
            LOG.debug("调用carrierTrackingService.saveTrackingByYPS记录运单跟踪信息失败，失败原因response为空,入参trackingSave2DTO={}，modifyUserDTO={}", JSONObject.toJSONString(trackingSaveDTO),JSONObject.toJSONString(modifyUserDTO));
            return false;
        }
        if(!response.isSuccess()){
            LOG.error("调用carrierTrackingService.saveTrackingByYPS记录运单跟踪信息失败，失败原因response={},入参trackingSave2DTO={}，modifyUserDTO={}", response.getMessage(),JSONObject.toJSONString(trackingSaveDTO),JSONObject.toJSONString(modifyUserDTO));
            return false;
        }
        return response.getData();
    }

    @Override
    public boolean saveWaybillTrackingByDriver(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = saCarrierTrackingService.saveWaybillTrackingByDriver(waybillId, trackingSaveDTO, modifyUserDTO);
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
    public boolean saveCarrierTrackingByDriver(Long carrierId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = saCarrierTrackingService.saveCarrierTrackingByDriver( carrierId,  trackingSaveDTO,  modifyUserDTO);
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

    @Override
    public SystemProtocolDTO getSiteProtocol(String siteCode) {
        Response<SystemProtocolDTO> response = saSystemProtocolService.getSiteProtocol(siteCode);
        if(response==null){
            LOG.debug("调用systemProtocolService.getSiteProtocol查询平台信息异常，异常原因response为null");
            return null;
        }
        if(!response.isSuccess()){
            LOG.debug("调用systemProtocolService.getSiteProtocol查询平台信息异常，异常原因={}",response.getMessage());
            return null;
        }
        return response.getData();
    }

    @Override
    public CarrierAssemDTO findAssem(Long carrierId) {
        Response<CarrierAssemDTO> response = saCarrierService.findAssem(carrierId);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务carrierService查询托单详情失败");
            return null;
        }
        return response.getData();
    }

    @Override
    public Response<Boolean> carrierSign(Long carrierId, String signNumber, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = saCarrierService.carrierSign(carrierId,signNumber,modifyUserDTO);
        if(response==null){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务carrierSign签收托单失败");
            return null;
        }
        return response;
    }


}
