package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.InitDataUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.CarInfoBO;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.EmptyCarReportService;
import com.cy.driver.service.PointFService;
import com.cy.pass.service.dto.CarDTO;
import com.cy.pass.service.dto.DriverEmptyReportInfoDTO;
import com.cy.pass.service.dto.Enum.company.EnumSubmitType;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mr on 2015/7/4.
 */
@Scope("prototype")
@RestController("driverCarAction")
public class DriverCarAction extends BaseAction{
    private Logger LOG = LoggerFactory.getLogger(DriverCarAction.class);
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Resource
    private SystemData systemData;
    @Resource
    private PointFService pointFService;
    @Resource
    private EmptyCarReportService emptyCarReportService;


    /**
     * 编辑车辆图片信息 （3.4以上版本）
     * @param carPhoto
     * @return
     */
    @RequestMapping(value = "/editCarPhoto", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.EDIT_CAR_PHOTO)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object editCarPhoto(HttpServletResponse response, String carPhoto) {
        //必传参数
        if(StringUtils.isBlank(carPhoto) ){
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆图片信息：参数不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        CarDTO carDTO = new CarDTO();
        carDTO.setDriverId(findToken(request));
        carDTO.setTocken(findUserIdStr(request));
        carDTO.setCarPhoto(carPhoto);
        Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("修改车辆图片信息失败(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("修改车辆图片信息失败(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
        //完善头像积分奖励-3.4版本
        pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                        LogEnum.ADD_CAR_INFO.getEventCode(),null,null,convert2InSource(),null);
        updRespHeadSuccess(response);
        return null;
    }

    /**
     * 编辑车牌信息 （3.4以上版本）
     * @param carNumber
     * @return
     */
    @RequestMapping(value = "/editCarNumber", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.EDIT_CAR_NUMBER)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object editCarNumber(HttpServletResponse response, String carNumber) {
        //必传参数
        if(StringUtils.isBlank(carNumber) ){
            if (LOG.isErrorEnabled()) LOG.error("编辑车牌信息：参数不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        CarDTO carDTO = new CarDTO();
        //已经认证则不能修改车牌号  车牌校验
        if(EnumSubmitType.AUTH_SUCESS.getCode() == findAuthState(request)){//认证通过不能修改车牌
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20039);
        }
        carNumber = carNumber.toUpperCase();//转成大写
        if (StringUtils.isEmpty(carNumber) || !ValidateUtil.validateCarNumber(carNumber)) {
            if (LOG.isInfoEnabled())LOG.info("车牌号格式不正确");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20054,"车牌号:"+carNumber);
        }
        carDTO.setDriverId(findToken(request));
        carDTO.setTocken(findUserIdStr(request));
        carDTO.setCarNumber(carNumber);
        Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("编辑车牌信息失败(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("编辑车牌信息失败(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
        updRespHeadSuccess(response);
        return null;
    }


    /**
     * 编辑车辆类型信息 （3.4以上版本）
     * @param carLengthCode
     * @param carTypeCode
     * @param carriageTypeCode
     * @return
     */
    @RequestMapping(value = "/editCarType", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.EDIT_CAR_TYPE)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object editCarType(HttpServletResponse response, String carLengthCode, String carTypeCode, String carriageTypeCode) {
        //必传参数
        if(StringUtils.isBlank(carLengthCode) || StringUtils.isBlank(carTypeCode) || StringUtils.isBlank(carriageTypeCode)){
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆类型信息：参数不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        CarDTO carDTO = new CarDTO();
        carDTO.setDriverId(findToken(request));
        carDTO.setTocken(findUserIdStr(request));
        carDTO.setCarTypesNew(carTypeCode);
        carDTO.setCarriageType(carriageTypeCode);
        carDTO.setCarLengthNewest(new BigDecimal(carLengthCode));
        Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("编辑车辆类型信息失败(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆类型信息失败(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
        updRespHeadSuccess(response);
        return null;
    }

    /**
     * 编辑车辆载重信息 （3.4以上版本）
     * @param carLoad
     * @return
     */
    @RequestMapping(value = "/editCarLoad", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.EDIT_CAR_LOAD)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object editCarLoad(HttpServletResponse response, String carLoad) {
        //必传参数
        if(StringUtils.isBlank(carLoad)){
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆载重信息：参数不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        CarDTO carDTO = new CarDTO();
        carDTO.setDriverId(findToken(request));
        carDTO.setTocken(findUserIdStr(request));
        if(StringUtils.isNotEmpty(carLoad)){
            carDTO.setCarWeightNewest(new BigDecimal(carLoad));
        }
        Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("编辑车辆载重信息失败(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆载重信息失败(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
        updRespHeadSuccess(response);
        return null;
    }

    /**
     * 编辑车辆体积信息 （3.4以上版本）
     * @param carVolume
     * @return
     */
    @RequestMapping(value = "/editCarVolume", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.EDIT_CAR_VOLUME)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object editCarVolume(HttpServletResponse response, String carVolume) {
        //必传参数
        if(StringUtils.isBlank(carVolume)){
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆体积信息：参数不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        CarDTO carDTO = new CarDTO();
        carDTO.setDriverId(findToken(request));
        carDTO.setTocken(findUserIdStr(request));
        if(StringUtils.isNotEmpty(carVolume)){
            carDTO.setCarWeightNewest(new BigDecimal(carVolume));
        }
        Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("编辑车辆体积信息失败(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("编辑车辆体积信息失败(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
        updRespHeadSuccess(response);
        return null;
    }

    /**
     * 添加车辆信息
     * @param carNumber
     * @param carLength
     * @param carLoad
     * @param carVolume
     * @param carTypeCode
     * @param carriageTypeCode
     * @param carPhoto
     * @return
     */
    @RequestMapping(value = "/addCarInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_CAR_ADD)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object addCarInfo(HttpServletResponse response,
                             String carNumber,String carLength,String carLoad,String carVolume,
                             String carTypeCode,String carTypeChildCode,String carriageTypeCode,
                             String carriageTypeChildCode,String carPhoto) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        //必传参数
        if(StringUtils.isBlank(carNumber) || StringUtils.isBlank(carLength) || StringUtils.isBlank(carTypeCode)
                || StringUtils.isBlank(carriageTypeCode) || StringUtils.isBlank(carriageTypeChildCode)){

            if (LOG.isErrorEnabled()) LOG.error("添加车辆信息：参数不完整");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        //车牌校验
        carNumber = carNumber.toUpperCase();//转成大写
        if (StringUtils.isEmpty(carNumber) || !ValidateUtil.validateCarNumber(carNumber)) {
            if (LOG.isInfoEnabled())LOG.info("车牌号格式不正确");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20054,"车牌号:"+carNumber);
        }
        //车长校验
        //载重
        //体积
        //车辆类型
        String carTypesNew = "";//车辆类型
        if(StringUtils.isEmpty(carTypeChildCode)){
            carTypesNew = carTypeCode;
        }else{
            carTypesNew = carTypeChildCode;
        }
        String carriageType = "";//车厢类型
        if(StringUtils.isEmpty(carriageTypeChildCode)){
            carriageType = carriageTypeCode;
        }else{
            carriageType = carriageTypeChildCode;
        }
        if(StringUtils.isEmpty(systemData.containVehicleType(carTypesNew))){
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20055);
        }
        if(StringUtils.isEmpty(systemData.containCarriageType(carriageType))){
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20057);
        }

        String tocken = findToken(request);
        String userId = findUserIdStr(request);

        CarDTO carDTO = new CarDTO();
        carDTO.setDriverId(userId);
        carDTO.setTocken(tocken);
        carDTO.setCarNumber(carNumber);

        carDTO.setCarLengthNewest(new BigDecimal(carLength));
        if(StringUtils.isNotEmpty(carLoad)){
            carDTO.setCarWeightNewest(new BigDecimal(carLoad));
        }
        if(StringUtils.isNotEmpty(carVolume)) {
            carDTO.setCarCubageNewest(new BigDecimal(carVolume));
        }
        carDTO.setCarTypesNew(carTypesNew);
        carDTO.setCarriageType(carriageType);

        carDTO.setCarPhoto(carPhoto);

        Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("添加车辆信息(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("添加车辆信息(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }

        updRespHeadSuccess(response);
        return resultMap;
    }

    /**
     * 获取车辆信息
     * @return
     */
    @RequestMapping(value = "/getCarInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_CAR_SELECT)
    @Log(type = LogEnum.GET_CAR_INFO)
    public Object getCarInfo(HttpServletResponse response) {
        String userId = findUserIdStr(request);
        Response<CarDTO> serResponse = driverUserHandlerService.getCarInfo(userId);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("查询车辆信息(服务端)校验：参数不完整",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("查询车辆信息(服务端)校验：",serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }

        CarDTO carDTO = serResponse.getData();
        updRespHeadSuccess(response);
        if(carDTO != null){
            CarInfoBO carBO = new CarInfoBO();
            BigDecimal carLength = new BigDecimal("0");
            if(carDTO.getCarLengthNewest() != null){
                carLength = carDTO.getCarLengthNewest();
            }else if(carDTO.getCarLengthNewest() == null){
                carLength = null;
            }
            carBO.setCarLength(carLength==null?null:carLength.toString());
            carBO.setCarPhoto(carDTO.getCarPhoto());
            carBO.setCarLoad(carDTO.getCarWeightNewest() == null ? "" : carDTO.getCarWeightNewest().toString());
            carBO.setCarNumber(carDTO.getCarNumber());
            carBO.setCarVolume(carDTO.getCarCubageNewest() == null ? "" : carDTO.getCarCubageNewest().toString());

            List<String> carTypeList = systemData.findVehicleTypeCode(carDTO.getCarTypesNew());
            List<String> carriageTypeList = systemData.findCarriageTypeCode(carDTO.getCarriageType());

            carBO.setCarriageTypeChildCode(carriageTypeList.get(1));
            carBO.setCarriageTypeCode(carriageTypeList.get(0));
            carBO.setCarTypeChildCode(carTypeList.get(1));
            carBO.setCarTypeCode(carTypeList.get(0));

            /** 3.4版本 车长、车辆类型、车厢类型 */
            carBO.setCarLengthValue(InitDataUtil.getCarLengthValue(carLength));
            carBO.setCarLengthCode(carLength);
            carBO.setCarVehicleType(SystemsUtil.buildVehicleTypeValue(carDTO.getCarTypesNew()));
            carBO.setCarVehicleTypeCode(carDTO.getCarTypesNew());
            carBO.setCarCarriageType(SystemsUtil.buildCarriageTypeValue(carDTO.getCarriageType()));
            carBO.setCarCarriageTypeCode(carDTO.getCarriageType());

            Response<DriverEmptyReportInfoDTO> result = emptyCarReportService.queryDriverEmptyReportInfo(findUserId());
            if(result.isSuccess() || result.getData() != null) {
                DriverEmptyReportInfoDTO driverEmptyReportInfoDTO = result.getData();
                carBO.setEmptyCarAmount(driverEmptyReportInfoDTO.getFutureEmptyCarNum()+driverEmptyReportInfoDTO.getTodayEmptyCarNum()+"");
            }
            return carBO;
        }
        return null;
    }
}
