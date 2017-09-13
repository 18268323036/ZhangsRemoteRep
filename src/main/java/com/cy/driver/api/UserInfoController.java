package com.cy.driver.api;

import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.action.BaseAction;
import com.cy.driver.api.convert.OwnerInfoConvert;
import com.cy.driver.api.domain.res.DriverImgDetail;
import com.cy.driver.api.domain.res.OwnerInfoDetail;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.service.*;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.rdcservice.service.dto.AuthedInfoDTO;
import com.cy.rdcservice.service.dto.UserItemStatDTO;
import com.cy.rdcservice.service.dto.UserLoginInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanst 2016/5/26 15:48
 */
@Scope("prototype")
@Controller
public class UserInfoController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private OwnerItemService ownerItemService;
    @Resource
    private CargoHandlerService cargoHandlerService;
    @Resource
    private CloudUserService cloudUserService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private DriverImgService driverImgService;


    /**
     * 货主详情
     *
     * @param cargoId     货源Id
     * @param cargoSource 货源类型
     * @return
     */
    @RequestMapping(value = "/cloudOwnerDetail", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.OWNER_DETAIL)
    @ResponseBody
    @Log(type = LogEnum.OWNER_DETAIL)
    public Object onwerDetail(Long cargoId, Integer cargoSource, Long companyId, Long webUserId) {
        try {
            if (companyId == null || cargoId == null || cargoId.longValue() == 0 || cargoSource == null || cargoSource.longValue() == 0) {
                if (LOG.isErrorEnabled()) LOG.error("查询发货人详情信息校验:参数有误。");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            OwnerInfoDetail ownerInfoDetail = new OwnerInfoDetail();
            // 快到网货主详情
            if (cargoSource == 1) {
                com.cy.pass.service.dto.base.Response<CompanyInfoDTO> comResponse = webUserHandleService.getCompanyDetail(companyId);
                if (!comResponse.isSuccess()) {
                    updRespHeadError(response);
                    if (comResponse.getCode() == CodeTable.ERROR.getCode()) {
                        if (LOG.isErrorEnabled()) LOG.error("查询发货人企业详情(pass服务端)校验信息：参数不完整");
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                    }
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                if (comResponse.getData() != null) {
                    CompanyInfoDTO infoDTO = comResponse.getData();
                    if (companyId.intValue() != 1) {
                        ownerInfoDetail.setCompanyName(infoDTO.getCompanyName());
                        ownerInfoDetail.setCompanyAddress(infoDTO.getCompanyAddress());
                    } else {
                        ownerInfoDetail.setCompanyAddress("");
                    }
                }

                com.cy.pass.service.dto.base.Response<WebUserInfoDTO> userResponse = webUserHandleService.getWebUserByCompanyId(companyId);
                if (!userResponse.isSuccess()) {
                    updRespHeadError(response);
                    if (userResponse.getCode() == CodeTable.ERROR.getCode()) {
                        if (LOG.isErrorEnabled()) LOG.error("查询发货人详情(pass服务端)校验信息：参数不完整");
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                    }
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                if (userResponse != null) {
                    WebUserInfoDTO infoDTO = userResponse.getData();
                    ownerInfoDetail.setAuthStatus(infoDTO.getSubmitType() == null ? null : infoDTO.getSubmitType().toString());
                    if (infoDTO.getSubmitType() != null && infoDTO.getSubmitType().intValue() == 3) {
                        ownerInfoDetail.setAuthTime(DateUtil.dateFormat(infoDTO.getAuditTime(), DateUtil.F_DATE));
                    }
                }

                Response<CargoInfoDTO> cargoResponse = cargoHandlerService.getCargoInfo(cargoId);
                if (!cargoResponse.isSuccess()) {
                    updRespHeadError(response);
                    if (comResponse.getCode() == CodeTable.ERROR.getCode()) {
                        if (LOG.isErrorEnabled()) LOG.error("查询发货人详情(cargo服务端)校验信息：参数不完整");
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                    }
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                if (cargoResponse != null) {
                    CargoInfoDTO infoDTO = cargoResponse.getData();
                    if (infoDTO != null) {
                        ownerInfoDetail.setTelephone(infoDTO.getContactTelephone());
                        ownerInfoDetail.setContactName(infoDTO.getContactName());
                        ownerInfoDetail.setMobile(infoDTO.getContactMobilephone());

                        if (companyId.intValue() == 1) {
                            //爬虫爬的货的企业名称=货源信息中的企业名称
                            ownerInfoDetail.setCompanyName(infoDTO.getCompanyName());
                            ownerInfoDetail.setAuthStatus("0");
                            ownerInfoDetail.setAuthTime("");
                        }
                    }
                }
                //信用
                OwnerItemStatDTO ownerItemStatDTO = ownerItemService.getByOwnerId(webUserId);
                if (ownerItemStatDTO != null) {
                    ownerInfoDetail.setCredit(ownerItemStatDTO.getCreditGrade());
                }
            } else { // 区域配送货主详情
                UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(webUserId);
                if (userLoginInfoDTO == null) {
                    LOG.debug("调用rdc服务userService查询货主信息失败");
                    return null;
                }
                AuthedInfoDTO authedInfoDTO = cloudUserService.getCompanyAuthedInfo(companyId);
                if (authedInfoDTO == null) {
                    LOG.debug("调用rdc服务userAuthService查询货主信息失败");
                    return null;
                }
                UserItemStatDTO userItemStatDTO = cloudUserService.getItemInfo(webUserId);
                if (userItemStatDTO == null) {
                    LOG.debug("调用rdc服务userItemStatService查询货主信息失败");
                    return null;
                }
                updRespHeadSuccess(response);
                return OwnerInfoConvert.qypsOwnerInfoConvert(userLoginInfoDTO, authedInfoDTO, userItemStatDTO);
            }
        } catch (Exception e) {
            LOG.error("获取货主详情出错", e);
        }
        return findException(response);
    }


    /**
     * 根据用户id查询
     */
    @RequestMapping(value = "/cloudUserDetail", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_USER_CLOUD)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_USER_CLOUD)
    public Object cloudUserInfo(String userId, String type) {
        try {
            if (userId == null || type == null) {
                if (LOG.isErrorEnabled()) LOG.error("查询司机详情信息校验:参数有误。");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (!type.equals("1") && !type.equals("2") && !type.equals("3") && !type.equals("4")) {
                if (LOG.isErrorEnabled()) LOG.error("查询司机详情信息校验:用户类型入参有误。");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Map<String, Object> map = new HashMap<>();
            //用户是快到司机
            if (type.equals("1")) {
                //司机是快到网司机
                com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(Long.valueOf(userId));
                if (result == null || !result.isSuccess()) {
                    updRespHeadError(response);
                    return null;
                }
                DriverUserInfoDTO driverUserInfoDTO = result.getData();
                map.put("authStatus", driverUserInfoDTO.getSubmitType());
                map.put("authTime", DateUtil.dateFormat(driverUserInfoDTO.getSubmitTime(), DateUtil.F_DATETIME));
                map.put("contactName", driverUserInfoDTO.getName());
                map.put("carNumber", driverUserInfoDTO.getCarNumber());
                map.put("mobile", driverUserInfoDTO.getCode());
                map.put("telephone", driverUserInfoDTO.getTelephone());
                DriverItemStatDTO driverItemStatDTO = driverUserHandlerService.findBusinessInfo(Long.valueOf(userId));
                if (driverItemStatDTO != null) {
                    map.put("credit", driverItemStatDTO.getCreditGrade());
                }
                updRespHeadSuccess(response);
                return map;
                //用户是快到货主
            } else if (type.equals("2")) {
                WebUserInfoDTO webUserInfoDTO = webUserHandleService.getWebUserInfo(Long.valueOf(userId));
                if (webUserInfoDTO.getAccountType() == 0) {
                    map.put("authStatus", webUserInfoDTO.getSubmitType());
                    map.put("authTime", webUserInfoDTO.getAuditTime());
                    map.put("companyName", webUserInfoDTO.getName());
                    map.put("mobile", webUserInfoDTO.getContactMobiphone());
                    map.put("companyAddress", webUserInfoDTO.getContactAddress());
                    map.put("contactName", webUserInfoDTO.getContracter());
                } else {
                    map.put("authStatus", webUserInfoDTO.getSubmitType());
                    map.put("authTime", webUserInfoDTO.getAuditTime());
                    map.put("mobile", webUserInfoDTO.getMobilephone());
                    map.put("contactName", webUserInfoDTO.getName());
                    if (webUserInfoDTO.getCompanyId() != null) {
                        com.cy.pass.service.dto.base.Response<CompanyInfoDTO> seeComResult = webUserHandleService.getCompanyDetail(webUserInfoDTO.getCompanyId());
                        if (seeComResult != null && seeComResult.isSuccess()) {
                            map.put("companyName", seeComResult.getData().getCompanyName());
                            map.put("companyAddress", SystemsUtil.buildAddress(seeComResult.getData().getCompanyProvince(), seeComResult.getData().getCompanyCity(), seeComResult.getData().getCompanyCounty()));
                        }
                    }
                    OwnerItemStatDTO ownerItemStatDTO = ownerItemService.getByOwnerId(Long.valueOf(userId));
                    if (ownerItemStatDTO != null) {
                        map.put("credit", ownerItemStatDTO.getCreditGrade());
                    }
                }
                updRespHeadSuccess(response);
                return map;
                //用户是区域配送用户
            } else {
                UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(Long.valueOf(userId));
                if (userLoginInfoDTO != null) {
                    map.put("authStatus", userLoginInfoDTO.getSubmitType());
                    map.put("contactName", userLoginInfoDTO.getName());
                    map.put("mobile", userLoginInfoDTO.getMobilephone());
                } else {
                    updRespHeadError(response);
                    return null;
                }
                if (userLoginInfoDTO.getParentId() == 0) {
                    AuthedInfoDTO authedInfoDTO = cloudUserService.getCompanyAuthedInfoByUserId(userLoginInfoDTO.getId());
                    if (authedInfoDTO != null) {
                        map.put("authTime", authedInfoDTO.getCreateTime());
                        map.put("companyName", authedInfoDTO.getAuthName());
                        map.put("companyAddress", SystemsUtil.buildAddress(authedInfoDTO.getProvinceName(), authedInfoDTO.getCityName(), authedInfoDTO.getCountyName()));
                    }
                }
                if (userLoginInfoDTO.getParentId() != 0) {
                    AuthedInfoDTO authedInfoDTO = cloudUserService.getCompanyAuthedInfo(userLoginInfoDTO.getParentId());
                    if (authedInfoDTO != null) {
                        map.put("authTime", authedInfoDTO.getCreateTime());
                        map.put("companyName", authedInfoDTO.getAuthName());
                        map.put("companyAddress", SystemsUtil.buildAddress(authedInfoDTO.getProvinceName(), authedInfoDTO.getCityName(), authedInfoDTO.getCountyName()));
                    }
                }
                UserItemStatDTO userItemStatDTO = cloudUserService.getItemInfo(Long.valueOf(userId));
                if (userItemStatDTO != null) {
                    map.put("credit", userItemStatDTO.getCreditGrade());
                }
                updRespHeadSuccess(response);
                return map;
            }
        } catch (Exception e) {
            LOG.error("获取货主详情出错", e);
        }
        return findException(response);
    }


    /**
     * 根据号码查司机Id
     *
     * @param siteCode    平台编码
     * @param mobilePhone 手机号码
     * @return
     */
    @RequestMapping(value = "/cloudDriverInfo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_DRIVER_INFO)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_DRIVER_INFO)
    public Object cloudDriverInfo(String siteCode, String mobilePhone) {
        if (siteCode == null || mobilePhone == null) {
            if (LOG.isErrorEnabled()) LOG.error("查询司机信息校验:参数有误。");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        Map<String, Object> map = new HashMap<>();
        /**
         * 查询云配送手机
         */
        UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getUserInfoByPhone(siteCode, mobilePhone);
        if (userLoginInfoDTO != null) {
            map.put("isExist", "1");
            map.put("username", userLoginInfoDTO.getName());
            map.put("mobilePhone", userLoginInfoDTO.getMobilephone());
            map.put("waybillNature", "1");
            map.put("transportUserId", String.valueOf(userLoginInfoDTO.getId()));
            map.put("userType","3");
            updRespHeadSuccess(response);
            return map;
        } else {
            /**
             * 查询快到网司机
             */
            DriverUserInfoDTO driverUserInfoDTO = cloudUserService.getDriverInfoByCode(mobilePhone);
            if (driverUserInfoDTO != null) {
                map.put("isExist", "1");
                map.put("username", driverUserInfoDTO.getName());
                map.put("mobilePhone", driverUserInfoDTO.getCode());
                map.put("carNumber", driverUserInfoDTO.getCarNumber());
                map.put("waybillNature", "2");
                map.put("transportUserId", String.valueOf(driverUserInfoDTO.getId()));
                map.put("userType","1");
                updRespHeadSuccess(response);
                return map;
            }
        }
        map.put("isExist", "0");
        map.put("waybillNature", "1");
        map.put("transportUserId", "");
        map.put("userType","0");
        updRespHeadSuccess(response);
        return map;
    }

    /**
     * 获取认证信息
     */
    @RequestMapping(value = "/cloudGetAuthenticationInfo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_USER_AUTHEDINFO)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_USER_AUTHEDINFO)
    public Object cloudGetAuthenticationInfo() {
        try {
            Long driverId = findUserId();
            DriverUserInfoDTO userInfoDTO = driverUserHandlerService.getDriverInfo(driverId);
            //用户不存在
            if (userInfoDTO == null) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }
            //查询图片认证信息
            List<Byte> list = new ArrayList<>();
            list.add((byte) 1);
            list.add((byte) 2);
            list.add((byte) 3);
            list.add((byte) 4);
            List<DriverImgDTO> driverImgDTOs = driverImgService.queryImgInfo(driverId, list);
            if (driverImgDTOs == null || driverImgDTOs.size() == 0) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("authenticationInfos", imgConvert(driverImgDTOs));
            resultMap.put("identityLicenseNum", userInfoDTO.getIdentityLicenseNum());
            resultMap.put("name", userInfoDTO.getName());
            resultMap.put("carNumber", userInfoDTO.getCarNumber());
            resultMap.put("driverAuthStatus", userInfoDTO.getSubmitType());
            updRespHeadSuccess(response);
            return resultMap;
        } catch (Exception e) {
            LOG.error("获取司机认证信息出错", e);
        }
        return findException(response);
    }

    public static List<DriverImgDetail> imgConvert(List<DriverImgDTO> driverImgDTOs) {
        List<DriverImgDetail> driverImgDetails = new ArrayList<>();
        for (DriverImgDTO driverImgDTO : driverImgDTOs) {
            DriverImgDetail driverImgDetail = new DriverImgDetail();
            driverImgDetail.setImgPath(driverImgDTO.getImgPath());
            driverImgDetail.setImgType(String.valueOf(driverImgDTO.getImgType()));
            driverImgDetail.setOrderNum(String.valueOf(driverImgDTO.getOrderNum()));
            driverImgDetail.setAuthStatus(String.valueOf(driverImgDTO.getSubmitType()));
            driverImgDetail.setUploadTime(DateUtil.dateFormat(driverImgDTO.getUploadTime(), DateUtil.F_DATE));
            if (driverImgDTO.getEffectiveStartDate() != null && driverImgDTO.getEffectiveDate() != null) {
                driverImgDetail.setEffectiveDate(DateUtil.dateFormat(driverImgDTO.getEffectiveStartDate(), DateUtil.F_DATE) + " 至 " + DateUtil.dateFormat(driverImgDTO.getEffectiveDate(), DateUtil.F_DATE));
            }
            if (driverImgDTO.getEffectiveDate() == null && driverImgDTO.getEffectiveDate() != null) {
                driverImgDetail.setEffectiveDate(DateUtil.dateFormat(driverImgDTO.getEffectiveStartDate(), DateUtil.F_DATE));
            }
            if (driverImgDTO.getEffectiveDate() != null && driverImgDTO.getEffectiveStartDate() == null ) {
                driverImgDetail.setEffectiveDate(DateUtil.dateFormat(driverImgDTO.getEffectiveDate(), DateUtil.F_DATE));
            }
            if (driverImgDTO.getEffectiveDate() == null && driverImgDTO.getEffectiveStartDate() == null ) {
                driverImgDetail.setEffectiveDate("");
            }
            driverImgDetails.add(driverImgDetail);
        }
        return driverImgDetails;
    }


}
