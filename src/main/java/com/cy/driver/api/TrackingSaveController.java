package com.cy.driver.api;

import com.cy.driver.action.BaseAction;
import com.cy.driver.api.convert.TrackingSaveConvert;
import com.cy.driver.cloudService.CloudSaveTrackingService;
import com.cy.driver.cloudService.SaasWaybillInfoService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.rdcservice.service.dto.ModifyUserDTO;
import com.cy.rdcservice.service.dto.TrackingSaveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 运单轨迹保存
 *
 * @author zhangxy 2016/8/16 15:08
 */

@RestController
public class TrackingSaveController extends BaseAction {

    Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private CloudSaveTrackingService cloudSaveTrackingService;
    @Resource
    private SaasWaybillInfoService saasWaybillInfoService;


    @RequestMapping(value = "/cloudTrackingSave", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_TRACKING_SAVE)
    @ResponseBody
    public Object cloudTrackingSave(String longitude, String latitude, String province, String city, String county, String address, String orderId, String orderStatus, String carrierId,Integer orderSource) {
        try {
            if (longitude == null || latitude == null || province == null || city == null) {
                if (LOG.isErrorEnabled()) LOG.error("签收失败:轨迹数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Map<String, Object> map = new HashMap<>();
            Long driverId = findUserId();
            Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(driverId);
            if (result == null || !result.isSuccess()) {
                updRespHeadError(response);
                return null;
            }
            if(orderSource == null || orderSource.intValue() == 2) {//云配
                if (orderId != null && orderStatus != null) {
                    //保存运单轨迹
                    if (orderStatus.equals("22") || orderStatus.equals("23") || orderStatus.equals("24")) {
                        TrackingSaveDTO trackingSaveDTO = TrackingSaveConvert.makeTrackingSaveDTO(orderStatus, province, city, county, address, longitude, latitude);
                        ModifyUserDTO modifyUserDTO = TrackingSaveConvert.makeModifyUserDTO(driverId, result.getData().getName());
                        boolean isSuccess = cloudSaveTrackingService.saveWaybillTrackingByDrvier(Long.valueOf(orderId), trackingSaveDTO, modifyUserDTO);
                        if (!isSuccess) {
                            LOG.isDebugEnabled(); LOG.debug("保存运单轨迹失败：调用服务返回结果异常");
                            updRespHeadError(response);
                            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                        } else {
                            updRespHeadSuccess(response);
                            return map;
                        }
                    }
                }
                //托单签收时保存用户轨迹信息
                if (carrierId != null) {
                    ModifyUserDTO modifyUserDTO = TrackingSaveConvert.makeModifyUserDTO(driverId, result.getData().getName());
                    TrackingSaveDTO trackingSaveDTO = TrackingSaveConvert.makeTrackingSaveDTO("6", province, city, county, address, longitude, latitude);
                    boolean isSuccess = cloudSaveTrackingService.saveCarrierTrackingByDrvier(Long.valueOf(carrierId), trackingSaveDTO, modifyUserDTO);
                    if (!isSuccess) {
                        LOG.isDebugEnabled();
                        LOG.debug("保存运单轨迹失败");
                        updRespHeadError(response);
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                    } else {
                        updRespHeadSuccess(response);
                        return map;
                    }
                }
            }else if(orderSource.intValue() == 3) {//saas
                if (orderId != null && orderStatus != null) {
                    //保存运单轨迹
                    if (orderStatus.equals("22") || orderStatus.equals("23") || orderStatus.equals("24")) {
                        com.cy.saas.business.model.dto.TrackingSaveDTO trackingSaveDTO = TrackingSaveConvert.saasMakeTrackingSaveDTO(orderStatus, province, city, county, address, longitude, latitude);
                        com.cy.saas.business.model.dto.ModifyUserDTO modifyUserDTO = TrackingSaveConvert.saasMakeModifyUserDTO(driverId, result.getData().getName());
                        boolean isSuccess = saasWaybillInfoService.saveWaybillTrackingByDriver(Long.valueOf(orderId), trackingSaveDTO, modifyUserDTO);
                        if (!isSuccess) {
                            LOG.isDebugEnabled(); LOG.debug("保存运单轨迹失败：调用服务返回结果异常");
                            updRespHeadError(response);
                            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                        } else {
                            updRespHeadSuccess(response);
                            return map;
                        }
                    }
                }
                //托单签收时保存用户轨迹信息
                if (carrierId != null) {
                    com.cy.saas.business.model.dto.ModifyUserDTO modifyUserDTO = TrackingSaveConvert.saasMakeModifyUserDTO(driverId, result.getData().getName());
                    com.cy.saas.business.model.dto.TrackingSaveDTO trackingSaveDTO = TrackingSaveConvert.saasMakeTrackingSaveDTO("6", province, city, county, address, longitude, latitude);
                    boolean isSuccess = saasWaybillInfoService.saveCarrierTrackingByDriver(Long.valueOf(carrierId), trackingSaveDTO, modifyUserDTO);
                    if (!isSuccess) {
                        LOG.isDebugEnabled();
                        LOG.debug("保存运单轨迹失败");
                        updRespHeadError(response);
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                    } else {
                        updRespHeadSuccess(response);
                        return map;
                    }
                }
            }
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }

}
