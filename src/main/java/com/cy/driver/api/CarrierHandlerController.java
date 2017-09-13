package com.cy.driver.api;

import com.alibaba.fastjson.JSON;
import com.cy.driver.action.BaseAction;
import com.cy.driver.api.convert.WayBillConvert;
import com.cy.driver.api.domain.res.CarrierDetail;
import com.cy.driver.cloudService.CloudCarrierService;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.driver.cloudService.SaasOrderInfoService;
import com.cy.driver.cloudService.SaasWaybillInfoService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.LocationService;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.rdcservice.service.CarrierTrackingService;
import com.cy.rdcservice.service.dto.*;
import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxy 2016/8/2 18:54
 */
@Scope("prototype")
@Controller
public class CarrierHandlerController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(WaybillHandlerController.class);

    @Resource
    private CloudUserService cloudUserService;
    @Resource
    private CloudCarrierService cloudCarrierService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private CarrierTrackingService carrierTrackingService;
    @Resource
    private LocationService locationService;
    @Resource
    private SaasWaybillInfoService saasWaybillInfoService;
    @Resource
    private SaasOrderInfoService saasOrderInfoService;

    /**
     * 托单详情
     */
    @RequestMapping(value = "/cloudCarrierDetail", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_CARRIER_DETAIL)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_CARRIER_DETAIL)
    public Object cloudCarrierDetail(String carrierId,Integer orderSource) {
        try {
            if (carrierId == null) {
                if (LOG.isErrorEnabled()) LOG.error("查询托单详情信息校验:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            CarrierDetail carrierDetail = null;
            if(orderSource == null || orderSource.intValue() == 2) {
                CarrierAssemDTO carrierAssemDTO = cloudCarrierService.queryCarrierDetail(Long.valueOf(carrierId));
                if (carrierAssemDTO == null) {
                    if (LOG.isDebugEnabled()) LOG.debug("调用carrierService.findAssem服务查询托单详情失败");
                    updRespHeadError(response);
                    return null;
                }
                //把托单信息先转进CarrierDetail中
                carrierDetail = WayBillConvert.carrierDetailConvert2(carrierAssemDTO);
                //查询托单创建人信息
                UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(carrierAssemDTO.getInfoDTO().getCreateUserId());
                if (userLoginInfoDTO == null) {
                    if (LOG.isDebugEnabled()) LOG.debug("调用userService.getUser服务用户详情失败");
                } else {
                    carrierDetail.setPhotosAddress(userLoginInfoDTO.getHeadImg());
                    carrierDetail.setOwnerPhone(userLoginInfoDTO.getMobilephone());
                    //根据是否是主账号来寻找公司认证信息
                    if (userLoginInfoDTO.getParentId() == 0) {
                        carrierDetail.setAuthedInfo(Integer.valueOf(userLoginInfoDTO.getSubmitType()));
                    } else {
                        UserLoginInfoDTO parentUserInfo = cloudUserService.getOwnerInfo(userLoginInfoDTO.getParentId());
                        carrierDetail.setAuthedInfo(Integer.valueOf(parentUserInfo.getSubmitType()));
                    }
                }
                UserItemStatDTO userItemStatDTO = cloudUserService.getItemInfo(carrierAssemDTO.getInfoDTO().getCreateUserId());
                if (userItemStatDTO == null) {
                    if (LOG.isDebugEnabled()) LOG.debug("调用userItemStatService.getByUserId服务用户详情失败");
                } else {
                    carrierDetail.setTransactionNumber(String.valueOf(userItemStatDTO.getFinishOrderNum()));
                }
            }else if(orderSource.intValue() == 3) {
                com.cy.saas.business.model.dto.CarrierAssemDTO saCarrierAssemDTO = saasWaybillInfoService.findAssem(Long.valueOf(carrierId));
                if (saCarrierAssemDTO == null) {
                    if (LOG.isDebugEnabled()) LOG.debug("调用carrierService.findAssem服务查询托单详情失败");
                    updRespHeadError(response);
                    return null;
                }
                //把托单信息先转进CarrierDetail中
                carrierDetail = WayBillConvert.saCarrierDetailConvert2(saCarrierAssemDTO);
                //查询托单创建人信息
                AccountUserDetails2DTO accountUserDetails2DTO = saasWaybillInfoService.getAccountUserDetails2(saCarrierAssemDTO.getInfoDTO().getCreateUserId());
                if (accountUserDetails2DTO == null) {
                    if (LOG.isDebugEnabled()) LOG.debug("调用userService.getUser服务用户详情失败");
                } else {
                    carrierDetail.setPhotosAddress(accountUserDetails2DTO.getAccountUser().getHeadImg());
                    carrierDetail.setOwnerPhone(accountUserDetails2DTO.getAccountUser().getLoginCode());
                    carrierDetail.setAuthedInfo(Integer.valueOf(accountUserDetails2DTO.getAccountUser().getSubmitType()));
                }
                com.cy.saas.business.model.dto.UserItemStatDTO userItemStatDTO = saasWaybillInfoService.getByUserId(saCarrierAssemDTO.getInfoDTO().getCreateUserId());
                if (userItemStatDTO == null) {
                    if (LOG.isDebugEnabled()) LOG.debug("调用userItemStatService.getByUserId服务用户详情失败");
                } else {
                    carrierDetail.setTransactionNumber(String.valueOf(userItemStatDTO.getFinishOrderNum()));
                }
            }
            updRespHeadSuccess(response);
            return carrierDetail;
        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }

    /**
     * 托单签收
     */
    @RequestMapping(value = "/cloudCarrierSign", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_CARRIER_SIGN)
    @ResponseBody
    @Log(type = LogEnum.CARRIER_SIGN_IN)
    public Object cloudCarrierSign(Long carrierId, String signNum,String province,String city,String county,String address,String longitude,String latitude,Integer orderSource) {
        try {
            if (carrierId == null || signNum == null) {
                if (LOG.isErrorEnabled()) LOG.error("签收托单校验:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Long driverId = findUserId();
            Map<String, Object> map = new HashMap<>();
            Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(driverId);
            com.cy.top56.common.Response<Boolean> isSignSuccess = null;
            //判断数据来源orderSource
            if(orderSource == null || orderSource.intValue() == 2) {//云配
                ModifyUserDTO modifyUserDTO = new ModifyUserDTO();
                if (result.isSuccess() && result.getData() != null) {
                    modifyUserDTO.setModifyUserType((byte) 1);
                    modifyUserDTO.setModifyUserName(result.getData().getName());
                    modifyUserDTO.setModifyUserId(driverId);
                    modifyUserDTO.setModifyUserAccountType(null);
                }else{
                    updRespHeadError(response);
                    return null;
                }
                isSignSuccess = cloudCarrierService.carrierSignIn(carrierId, signNum, modifyUserDTO);
                if (isSignSuccess==null) {
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }else if(isSignSuccess.isSuccess() && isSignSuccess.getData()){
                    TrackingSaveDTO trackingSaveDTO = new TrackingSaveDTO();
                    trackingSaveDTO.setOperateType(6);
                    if(longitude==null){
                        LastLocationDTO lastLocationDTO = locationService.queryLastLocation(driverId);
                        if(lastLocationDTO!=null) {
                            trackingSaveDTO.setLongitude(lastLocationDTO.getLongitude());
                            trackingSaveDTO.setLatitude(lastLocationDTO.getLatitude());
                            trackingSaveDTO.setProvince(lastLocationDTO.getProvince());
                            trackingSaveDTO.setCity(lastLocationDTO.getCity());
                            trackingSaveDTO.setCounty(lastLocationDTO.getCounty());
                            trackingSaveDTO.setAddress(lastLocationDTO.getTown());
                            trackingSaveDTO.setOperateType(6);
                        }else{
                            trackingSaveDTO.setLongitude("120.107455");
                            trackingSaveDTO.setLatitude("30.293039");
                            trackingSaveDTO.setProvince("浙江省");
                            trackingSaveDTO.setCity("杭州市");
                            trackingSaveDTO.setCounty("拱墅区");
                            trackingSaveDTO.setAddress("矩阵国际");
                            trackingSaveDTO.setOperateType(6);
                        }
                    }else{
                        trackingSaveDTO.setLongitude(longitude);
                        trackingSaveDTO.setLatitude(latitude);
                        trackingSaveDTO.setProvince(province);
                        trackingSaveDTO.setCity(city);
                        trackingSaveDTO.setCounty(county);
                        trackingSaveDTO.setAddress(address);
                        trackingSaveDTO.setOperateType(6);
                    }
                    com.cy.top56.common.Response<Boolean> response1 = carrierTrackingService.saveCarrierTrackingByDriver(carrierId,trackingSaveDTO,modifyUserDTO);
                    if(response1==null || !response1.isSuccess()){
                        LOG.debug("司机保存签收轨迹失败,carrierTrackingService.saveCarrierTrackingByDriver,modifyUserDTO={},trackingSaveDTO={}", JSON.toJSONString(modifyUserDTO),JSON.toJSONString(trackingSaveDTO));
                    }
                    updRespHeadSuccess(response);
                    return null;
                }
            } else if(orderSource.intValue() == 3) {//saas
                com.cy.saas.business.model.dto.ModifyUserDTO modifyUserDTO = new com.cy.saas.business.model.dto.ModifyUserDTO();
                if (result.isSuccess() && result.getData() != null) {
                    modifyUserDTO.setModifyUserType((byte) 1);
                    modifyUserDTO.setModifyUserName(result.getData().getName());
                    modifyUserDTO.setModifyUserId(driverId);
                    modifyUserDTO.setModifyUserAccountType(null);
                }else{
                    updRespHeadError(response);
                    return null;
                }
                isSignSuccess =  saasWaybillInfoService.carrierSign(carrierId,signNum,modifyUserDTO);
                if (isSignSuccess == null) {
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }else if(isSignSuccess.isSuccess() && isSignSuccess.getData()){
                    com.cy.saas.business.model.dto.TrackingSaveDTO trackingSaveDTO = new com.cy.saas.business.model.dto.TrackingSaveDTO();
                    trackingSaveDTO.setOperateType(6);
                    if(longitude==null){
                        LastLocationDTO lastLocationDTO = locationService.queryLastLocation(driverId);
                        if(lastLocationDTO!=null) {
                            trackingSaveDTO.setLongitude(lastLocationDTO.getLongitude());
                            trackingSaveDTO.setLatitude(lastLocationDTO.getLatitude());
                            trackingSaveDTO.setProvince(lastLocationDTO.getProvince());
                            trackingSaveDTO.setCity(lastLocationDTO.getCity());
                            trackingSaveDTO.setCounty(lastLocationDTO.getCounty());
                            trackingSaveDTO.setAddress(lastLocationDTO.getTown());
                            trackingSaveDTO.setOperateType(6);
                        }else{
                            trackingSaveDTO.setLongitude("120.107455");
                            trackingSaveDTO.setLatitude("30.293039");
                            trackingSaveDTO.setProvince("浙江省");
                            trackingSaveDTO.setCity("杭州市");
                            trackingSaveDTO.setCounty("拱墅区");
                            trackingSaveDTO.setAddress("矩阵国际");
                            trackingSaveDTO.setOperateType(6);
                        }
                    }else{
                        trackingSaveDTO.setLongitude(longitude);
                        trackingSaveDTO.setLatitude(latitude);
                        trackingSaveDTO.setProvince(province);
                        trackingSaveDTO.setCity(city);
                        trackingSaveDTO.setCounty(county);
                        trackingSaveDTO.setAddress(address);
                        trackingSaveDTO.setOperateType(6);
                    }
                    boolean response1 = saasWaybillInfoService.saveCarrierTrackingByDriver(carrierId,trackingSaveDTO,modifyUserDTO);
                    if(!response1){
                        LOG.debug("司机保存签收轨迹失败,carrierTrackingService.saveCarrierTrackingByDriver,modifyUserDTO={},trackingSaveDTO={}", JSON.toJSONString(modifyUserDTO),JSON.toJSONString(trackingSaveDTO));
                    }
                    updRespHeadSuccess(response);
                    return null;
                }
            }
            if(isSignSuccess.getCode() == com.cy.top56.common.Response.CodeTable.ERROR.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20230);
            }else if(isSignSuccess.getCode() == com.cy.top56.common.Response.CodeTable.DATA_NONE_EXIST.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20231);
            }else if(isSignSuccess.getCode() == com.cy.top56.common.Response.CodeTable.NONE_PERMISSION.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20232);
            }else if(isSignSuccess.getCode() == com.cy.top56.common.Response.CodeTable.VALID_ERROR.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20233);
            }else if(isSignSuccess.getCode() == com.cy.top56.common.Response.CodeTable.EXCEPTION.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
            }
        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }
}
