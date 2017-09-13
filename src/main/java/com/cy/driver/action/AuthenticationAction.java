package com.cy.driver.action;


import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.DriverImgTypeBO;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.AuthenticationInfoDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.driverAuth.DriverImgTypeDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Scope("prototype")
@Controller("authenticationAction")
public class AuthenticationAction extends BaseAction {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    /**
     * 获取认证信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getAuthenticationInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_AUTHENTICATION_INFO)
    @Log(type = LogEnum.GET_AUTHENTICATION_INFO)
    public Object getAuthenticationInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long driverId = findUserId(request);
            DriverUserInfoDTO userInfoDTO = driverUserHandlerService.getDriverInfo(driverId);
            //用户不存在
            if (userInfoDTO == null) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }
            Response<List<AuthenticationInfoDTO>> authenticationInfoDTOs = driverUserHandlerService.getAuthenticationInfo(driverId);
            if (authenticationInfoDTOs.isSuccess()) {
                updRespHeadSuccess(response);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("authenticationInfos", authenticationInfoDTOs.getData());
                resultMap.put("identityLicenseNum", userInfoDTO.getIdentityLicenseNum());
                resultMap.put("name", userInfoDTO.getName());
                resultMap.put("carNumber", userInfoDTO.getCarNumber());
                resultMap.put("driverAuthStatus", userInfoDTO.getSubmitType());
                return resultMap;
            }
            // 参数不完整
            if (authenticationInfoDTOs.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("获取认证信息(服务端)==>参数不合法");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

            if (authenticationInfoDTOs.getCode() == CodeTable.EXCEPTION.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("获取认证信息(服务端)==>异常");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("实名认证出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 实名认证
     *
     * @param request
     * @param response
     * @param realName
     * @param card
     * @param positiveCardMd5
     * @param oppositeCardMd5
     * @return
     */
    @RequestMapping(value = "/authenticationRealName", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.AUTHENTICATION_REAL_NAME)
    @Log(type = LogEnum.REALNAME_AUTHENTICATION)
    public Object authenticationRealName(HttpServletRequest request, HttpServletResponse response, String realName, String card, String positiveCardMd5, String oppositeCardMd5) {
        try {

            if (StringUtils.isEmpty(realName) || StringUtils.isEmpty(card) || StringUtils.isEmpty(positiveCardMd5) || StringUtils.isEmpty(oppositeCardMd5)) {
                if (logger.isErrorEnabled())
                    logger.error("实名认证：参数不能为空【不合法】。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            Response<Boolean> resultCarAuthentication = driverUserHandlerService.authenticationRealName(findUserId(request), realName, card.toUpperCase(), positiveCardMd5, oppositeCardMd5);
            if (resultCarAuthentication.isSuccess()) {
                updRespHeadSuccess(response);
                return null;
            }
            // 参数不完整
            if (resultCarAuthentication.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("实名认证(服务端)==>不合法");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

            if (resultCarAuthentication.getCode() == CodeTable.EXCEPTION.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("实名认证(服务端)==>异常");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

            if (resultCarAuthentication.getCode() == CodeTable.NOT_TRAVEL_CARD.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("实名认证(服务端)==>行驶证未认证。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20063);
            }

            if (resultCarAuthentication.getCode() == CodeTable.NOT_DRIVE_CARD.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("实名认证(服务端)==>驾驶证未认证。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20064);
            }

        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("实名认证出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 驾驶证认证
     *
     * @param request
     * @param response
     * @param divingLiensePhoto
     * @return
     */
    @RequestMapping(value = "/authenticationDrivingLicense", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.AUTHENTICATION_DRIVING_LICENSE)
    @Log(type = LogEnum.DIVING_LCENSE_AUTHENTICATION)
    public Object authenticationDrivingLicense(HttpServletRequest request, HttpServletResponse response, String divingLiensePhoto) {
        try {

            if (StringUtils.isEmpty(divingLiensePhoto)) {
                if (logger.isErrorEnabled())
                    logger.error("驾驶证认证：参数不能为空【不合法】。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            Response<Boolean> resultCarAuthentication = driverUserHandlerService.authenticationDrivingLicense(findUserId(request), divingLiensePhoto);
            if (resultCarAuthentication.isSuccess()) {
                updRespHeadSuccess(response);
                return null;
            }
            // 参数不完整
            if (resultCarAuthentication.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("驾驶证认证(服务端)==>不合法");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if (resultCarAuthentication.getCode() == CodeTable.EXCEPTION.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("驾驶证认证(服务端)==>异常");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("驾驶证认证出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 车辆认证  -行驶证认证
     *
     * @param request
     * @param response
     * @param carNumber
     * @param divingLiensePhoto
     * @return
     */
    @RequestMapping(value = "/authenticationCar", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CAR_AUTHENTICATION)
    @Log(type = LogEnum.CAR_AUTHENTICATION)
    public Object authenticationCar(HttpServletRequest request, HttpServletResponse response, String carNumber, String divingLiensePhoto) {
        try {

            //必传参数
            if (StringUtils.isBlank(carNumber)) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            carNumber = carNumber.toUpperCase();//转成大写
            if (StringUtils.isEmpty(carNumber) || !ValidateUtil.validateCarNumber(carNumber)) {
                if (logger.isInfoEnabled()) logger.info("车辆认证：车牌号格式不正确");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20054, "车牌号:" + carNumber);
            }
            if (StringUtils.isEmpty(divingLiensePhoto)) {
                if (logger.isErrorEnabled())
                    logger.error("车辆认证：参数不能为空【不合法】。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            Response<Boolean> resultCarAuthentication = driverUserHandlerService.authenticationCar(findUserId(request), carNumber, divingLiensePhoto);

            if (resultCarAuthentication.isSuccess()) {
                updRespHeadSuccess(response);
                return null;
            }
            // 参数不完整
            if (resultCarAuthentication.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("车辆认证(服务端)==>不合法");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if (resultCarAuthentication.getCode() == CodeTable.EXCEPTION.getCode()) {
                if (logger.isErrorEnabled())
                    logger.error("	车辆认证(服务端)==>异常");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("	车辆认证出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 身份证，行驶证，驾驶证同时认证
     * @param imgsinfo
     * @param carNumber
     * @param card
     * @param realName
     * @return
     */
    @RequestMapping(value = "/authenticationAll", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.AUTHENTICATE_ALL_INFO)
    @Log(type = LogEnum.ALL_INFO_AUTHENTICATION)
    public Object authenticationAllInfo(String carNumber ,String card, String realName,String imgsinfo) {
        try {
            //必传参数
            if (StringUtils.isEmpty(imgsinfo) || StringUtils.isEmpty(carNumber) || StringUtils.isEmpty(card) ||
                    StringUtils.isEmpty(realName)) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Long driverId = findUserId();
            Map<String,Object> map = new HashMap<>();
            carNumber = carNumber.toUpperCase();//转成大写
            if (StringUtils.isEmpty(carNumber) || !ValidateUtil.validateCarNumber(carNumber)) {
                if (logger.isInfoEnabled()) logger.info("车辆认证：车牌号格式不正确");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20054, "车牌号:" + carNumber);
            }
            Gson gson = new Gson();
            List<DriverImgTypeBO> list = gson.fromJson(imgsinfo,new TypeToken<List<DriverImgTypeBO>>(){}.getType());
            List<DriverImgTypeDTO> driverImgTypeDTOs = new ArrayList<>();
            for(DriverImgTypeBO driverImgTypeBO:list){
                DriverImgTypeDTO driverImgTypeDTO = new DriverImgTypeDTO();
                driverImgTypeDTO.setEffectiveDateE(DateUtil.parseDate(driverImgTypeBO.getEffectiveDateE(), DateUtil.F_DATE));
                driverImgTypeDTO.setEffectiveDateS(DateUtil.parseDate(driverImgTypeBO.getEffectiveDateS(), DateUtil.F_DATE));
                driverImgTypeDTO.setFileMd5(driverImgTypeBO.getFileMd5());
                driverImgTypeDTO.setImgType(Byte.valueOf(driverImgTypeBO.getImgType()));
                driverImgTypeDTOs.add(driverImgTypeDTO);
            }
            boolean isSuccess = driverUserHandlerService.authenticationAllInfo(driverId,carNumber,card,driverImgTypeDTOs,realName);
            if(isSuccess){
                updRespHeadSuccess(response);
                return map;
            }else {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
            }

        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("	车辆认证出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


}
