package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.JsonUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.*;
import com.cy.driver.service.DriverLineTrackService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.EmptyCarReportService;
import com.cy.driver.service.PointFService;
import com.cy.location.service.DriverLinePointService;
import com.cy.location.service.dto.LineSaveDTO;
import com.cy.location.service.dto.LocationLineDTO;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.Enum.company.EnumSubmitType;
import com.cy.pass.service.dto.base.Response;
import com.google.gson.internal.LinkedTreeMap;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 空车上报
 * Created by wyh on 2015/7/4.
 */
@Scope("prototype")
@RestController("emptyCarReportAction")
public class EmptyCarReportAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(EmptyCarReportAction.class);
    @Resource
    private EmptyCarReportService emptyCarReportService;

    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private PointFService pointFService;
    @Resource
    private DriverLineTrackService driverLineTrackService;
    @Resource
    private DriverLinePointService driverLinePointService;


    /**
     * 空车上报提交
     * @param request
     * @param response
     * @param sTime
     * @param eTime
     * @param sCityCode
     * @param sProCode
     * @param eCityCode
     * @param eProCode
     * @return
     */
    @RequestMapping(value = "/addEmptyCar", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_EMPTY_CAR)
    @Log(type = LogEnum.ADD_EMPTY_CAR)
    public Object addEmptyCar(HttpServletRequest request, HttpServletResponse response, String sTime, String eTime,
                              String sCityCode, String sProCode, String eCityCode, String eProCode){
        Long driverId = findUserId(request);
        //起始时间不为空，并且大于等于当前日期(yyyy-mm-dd)
        if(StringUtils.isEmpty(sTime)){
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
        }

        if (!DateUtil.comparisonDate(DateUtil.parseDate(sTime, DateUtil.F_DATE), DateUtil.parseDate(DateUtil.getCurDate(), DateUtil.F_DATE)))
        {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20067);
        }
        //截至时间允许为空，如果不为空必须大于等于当前日期(yyyy-mm-dd)
        if(!StringUtils.isEmpty(eTime))
        {
            if(!DateUtil.comparisonDate1(DateUtil.parseDate(eTime, DateUtil.F_DATE), DateUtil.parseDate(DateUtil.getCurDate(), DateUtil.F_DATE))){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20068);
            }

            //开始时间和结束日期 之间最大相差7天
           long days = DateUtil.getIntervalDays(DateUtil.parseDate(sTime+" 00:00:00", DateUtil.F_DATETIME), DateUtil.parseDate(eTime+" 00:00:00", DateUtil.F_DATETIME));
            if(days>7){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }
        //出发省编号不为空
        if(StringUtils.isEmpty(sProCode)) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20069);
        }
        //目的省编号不为空
        if(StringUtils.isEmpty(eProCode)) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20073);
        }


         try {
             AddEmptyCarDTO addEmptyCarDTO = new AddEmptyCarDTO();
             addEmptyCarDTO.setStartTime(sTime);
             addEmptyCarDTO.setEndTime(eTime);
             addEmptyCarDTO.setDriverId(findUserId(request));
             addEmptyCarDTO.setStartProCode(sProCode);
             addEmptyCarDTO.setEndProCode(eProCode);
             if(!StringUtils.isEmpty(sCityCode)) {
                 addEmptyCarDTO.setStartCityCode(sCityCode);
             }

             if(!StringUtils.isEmpty(eCityCode)) {
                 addEmptyCarDTO.setEndCityCode(eCityCode);
             }
            Response<Boolean> result = emptyCarReportService.addEmptyCar(addEmptyCarDTO);

             if(result.isSuccess())
             {
                 //上报成功后，保存司机上报的线路信息
                 List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
                 LocationLineDTO locationLineDTO = new LocationLineDTO();
                 locationLineDTO.setType(Constants.ADD_EMPTY_CAR);
                 locationLineDTO.setDriverId(driverId);
                 locationLineDTO.setStartCity(null==sCityCode?"": SystemData.getCityInfoByCode(sCityCode).getAreaName());
                 locationLineDTO.setStartProvince(SystemData.getProvinceInfoByCode(sProCode).getAreaName());
                 locationLineDTO.setEndCity(null==eCityCode?"": SystemData.getCityInfoByCode(eCityCode).getAreaName());
                 locationLineDTO.setEndProvince(SystemData.getProvinceInfoByCode(eProCode).getAreaName());
                 locationLineDTOs.add(locationLineDTO);
                 com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(locationLineDTOs);
                 if(!saveLineRes.isSuccess()){
                     logger.debug("locationLineService服务调用失败");
                 }

                 if(sTime.equals(DateUtil.dateFormat(new Date(), DateUtil.F_DATE))){
                     //改为接单状态
                     Response<Boolean> serResponse = driverUserHandlerService.updateWorkState(findUserId(request), Constants.WORK_STATUS_Orders);
                 }
                 //首次空车上报积分奖励
                 pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                         LogEnum.ADD_EMPTY_CAR.getEventCode(), null, null, convert2InSource(),null);
                 updRespHeadSuccess(response);
                 return null;
             }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("空车上报提交", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


    /**
     * 空车上报提交 （支持最多三个）
     * @param response
     * @param emptyCarSubmitParam
     * @return
     */
    @RequestMapping(value = "/addEmptyCarReport", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_EMPTYCAR_REPORT)
    @Log(type = LogEnum.ADD_EMPTY_CAR)
    public Object addEmptyCar(HttpServletResponse response, EmptyCarSubmitParam emptyCarSubmitParam, CarInfoParamBO carInfoParamBO){
        Long driverId = findUserId();
        //起始时间不为空，并且大于等于当前日期(yyyy-mm-dd)
        if(emptyCarSubmitParam == null || StringUtils.isEmpty(emptyCarSubmitParam.getsTime())){
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
        }

        if (!DateUtil.comparisonDate(DateUtil.parseDate(emptyCarSubmitParam.getsTime(), DateUtil.F_DATE), DateUtil.parseDate(DateUtil.getCurDate(), DateUtil.F_DATE)))
        {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20067);
        }
        //截至时间允许为空，如果不为空必须大于等于当前日期(yyyy-mm-dd)
        if(!StringUtils.isEmpty(emptyCarSubmitParam.geteTime()))
        {
            if(!DateUtil.comparisonDate1(DateUtil.parseDate(emptyCarSubmitParam.geteTime(), DateUtil.F_DATE), DateUtil.parseDate(DateUtil.getCurDate(), DateUtil.F_DATE))){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20068);
            }

            //开始时间和结束日期 之间最大相差7天
            long days = DateUtil.getIntervalDays(DateUtil.parseDate(emptyCarSubmitParam.getsTime(), DateUtil.F_DATE), DateUtil.parseDate(emptyCarSubmitParam.geteTime(), DateUtil.F_DATE));
            if(days>7){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }
        //出发省市不为空
        if(StringUtils.isEmpty(emptyCarSubmitParam.getsProValue()) || StringUtils.isEmpty(emptyCarSubmitParam.getsProCode())
                ||StringUtils.isEmpty(emptyCarSubmitParam.getsCityCode()) || StringUtils.isEmpty(emptyCarSubmitParam.getsCityValue())) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20070);
        }
        //目的省市不为空
        if(StringUtils.isEmpty(emptyCarSubmitParam.getEndCityJsonStr())) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20073);
        }

        try {
            String jsonStr = emptyCarSubmitParam.getEndCityJsonStr();
            List<LinkedTreeMap> endCity = (List<LinkedTreeMap>) JsonUtil.jsonToList(jsonStr);
            emptyCarSubmitParam.setEndCityList(endCity);
            Boolean result = emptyCarReportService.addEmptyCarReport(emptyCarSubmitParam, findUserId());
            if (result) {
                //上报成功后，保存司机上报的线路信息
                List<LocationLineDTO> locationLineDTOs = new ArrayList<>();
                for(LinkedTreeMap destination : emptyCarSubmitParam.getEndCityList()){
                    LocationLineDTO locationLineDTO = new LocationLineDTO();
                    locationLineDTO.setType(Constants.ADD_EMPTY_CAR);
                    locationLineDTO.setDriverId(driverId);
                    locationLineDTO.setStartCounty(null==emptyCarSubmitParam.getsCountyValue()?"": emptyCarSubmitParam.getsCountyValue());
                    locationLineDTO.setStartCity(null==emptyCarSubmitParam.getsCityValue()?"": emptyCarSubmitParam.getsCityValue());
                    locationLineDTO.setStartProvince(emptyCarSubmitParam.getsProValue());
                    locationLineDTO.setEndCity((String)destination.get("eCityValue"));
                    locationLineDTO.setEndProvince((String)destination.get("eProValue"));
                    locationLineDTO.setEndCounty((String)destination.get("eCountyValue"));
                    if(!StringUtils.isEmpty((String)destination.get("eProValue"))) {
                        locationLineDTOs.add(locationLineDTO);
                    }
                }
                com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(locationLineDTOs);
                if(!saveLineRes.isSuccess()){
                    logger.debug("locationLineService服务调用失败");
                }

                if(emptyCarSubmitParam.getsTime().equals(DateUtil.dateFormat(new Date(), DateUtil.F_DATE))){
                    //改为接单状态
                    driverUserHandlerService.updateWorkState(findUserId(request), Constants.WORK_STATUS_Orders);
                }
                List<LineSaveDTO> lineSaveDTOs = new ArrayList<>();
                for(LinkedTreeMap linkedTreeMap : endCity) {
                    LineSaveDTO lineSaveDTO = new LineSaveDTO();
                    lineSaveDTO.setStartTime(DateUtil.parseDate(emptyCarSubmitParam.getsTime(), DateUtil.F_DATE));
                    if(emptyCarSubmitParam.geteTime()!=null&&!emptyCarSubmitParam.geteTime().equals("")) {
                        lineSaveDTO.setEndTime(DateUtil.parseDate(emptyCarSubmitParam.geteTime(), DateUtil.F_DATE));
                    }else{
                        lineSaveDTO.setEndTime(DateUtil.parseDate(emptyCarSubmitParam.getsTime(), DateUtil.F_DATE));
                    }
                    lineSaveDTO.setStartProvince(emptyCarSubmitParam.getsProValue());
                    lineSaveDTO.setStartCity(emptyCarSubmitParam.getsCityValue());
                    lineSaveDTO.setEndProvince((String)linkedTreeMap.get("eProValue"));
                    lineSaveDTO.setEndCity((String)linkedTreeMap.get("eCityValue"));
                    lineSaveDTOs.add(lineSaveDTO);
                }
                driverLinePointService.saveLine(driverId,lineSaveDTOs);


                //首次空车上报积分奖励-3.4版本
                pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT, LogEnum.ADD_EMPTY_CAR.getEventCode(),null,null,convert2InSource(),null);
                if(carInfoParamBO.getCarNumber()!=null) {
                    //修改车辆信息
                    String carNumber = carInfoParamBO.getCarNumber().toUpperCase();
                    if (StringUtils.isEmpty(carNumber) || !ValidateUtil.validateCarNumber(carNumber)) {
                        if (logger.isInfoEnabled()) logger.info("车牌号格式不正确");
                        updRespHeadError(response);
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20054, "车牌号:" + carNumber);
                    }
                    DriverModifyDTO driverModifyDTO = new DriverModifyDTO();
                    if (EnumSubmitType.AUTH_SUCESS.getCode() != findAuthState(request)) {//认证通过不能修改车牌
                        driverModifyDTO.setCarNumber(carNumber);
                    }
                    driverModifyDTO.setDriverId(findUserId());
                    driverModifyDTO.setCarTypesNew(carInfoParamBO.getCarTypeCode());
                    driverModifyDTO.setCarriageType(carInfoParamBO.getCarriageTypeCode());
                    driverModifyDTO.setCarLengthNewest(new BigDecimal(carInfoParamBO.getCarLengthCode()));
                    boolean isSuccess = driverUserHandlerService.modifyDriverInfo(driverModifyDTO);
                    if (isSuccess) {
                        logger.isDebugEnabled();
                        logger.debug("修改车辆信息成功");
                    }
                }
                updRespHeadSuccess(response);
                 return null;
            }
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("空车上报提交出错", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


    /**
     * 空车上报列表
     * @author wyh
     */
    @RequestMapping(value = "/emptyCarReportList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.EMPTY_CAR_REPORT_LIST)
    @Log(type = LogEnum.EMPTY_CAR_REPORT_LIST)
    public Object emptyCarReportList(HttpServletRequest request, HttpServletResponse response){
        try {
            Response<DriverEmptyReportInfoDTO> result = emptyCarReportService.queryDriverEmptyReportInfo(findUserId(request));
            if(result.isSuccess() || result.getData() != null){
                DriverEmptyReportInfoDTO driverEmptyReportInfoDTO = result.getData();

                EmptyReportInfoBO emptyReportInfoBO = new EmptyReportInfoBO();
                emptyReportInfoBO.setFutureEmptyCarNum(driverEmptyReportInfoDTO.getFutureEmptyCarNum());
                emptyReportInfoBO.setTodayEmptyCarNum(driverEmptyReportInfoDTO.getTodayEmptyCarNum());

                //当日
                List<DriverTodayEmptyReportDTO> todayEmptyReportDTOList = driverEmptyReportInfoDTO.getTodayEmptyCarList();
                List<TodayEmptyReportBO> todayEmptyReportBOList = new ArrayList<TodayEmptyReportBO>();
                if(todayEmptyReportDTOList != null && todayEmptyReportDTOList.size() > 0){
                    for(DriverTodayEmptyReportDTO obj : todayEmptyReportDTOList){
                        TodayEmptyReportBO todayEmptyReportBO = new TodayEmptyReportBO();
                        todayEmptyReportBO.setDeleteType(obj.getDeleteType());
                        todayEmptyReportBO.setEmptyCarId(obj.getEmptyCarId());
                        todayEmptyReportBO.setEndAddress(obj.getEndAddress());
                        if (obj.getStartCityCode()==null ||"".equals(obj.getStartCityCode())) {
                            todayEmptyReportBO.setStartCityCode(obj.getStartProvinceCode());
                        }
                        else
                        {
                            todayEmptyReportBO.setStartCityCode(obj.getStartCityCode());
                        }
                        if (obj.getEndCityCode()==null ||"".equals(obj.getEndCityCode())) {
                            todayEmptyReportBO.setEndCityCode(obj.getEndProvinceCode());
                        }
                        else
                        {
                            todayEmptyReportBO.setEndCityCode(obj.getEndCityCode());
                        }
                        todayEmptyReportBO.setStartAddress(obj.getStartAddress());
                        todayEmptyReportBO.setStartTime(DateUtil.dateFormat(obj.getStartTime(), DateUtil.F_DATE));
                        todayEmptyReportBOList.add(todayEmptyReportBO);
                    }
                }
                emptyReportInfoBO.setTodayEmptyCarList(todayEmptyReportBOList);

                List<DriverBusinessLineDTO> businessLineDTOList = driverEmptyReportInfoDTO.getFutureEmptyCarList();
                List<BusinessLineBO> businessLineBOList = new ArrayList<BusinessLineBO>();
                if(businessLineDTOList != null && businessLineDTOList.size() > 0){
                    for(DriverBusinessLineDTO obj : businessLineDTOList){
                        BusinessLineBO businessLineBO = new BusinessLineBO();
                        businessLineBO.setEmptyCarId(obj.getEmptyCarId());
                        businessLineBO.setDeleteType(obj.getDeleteType());
                        businessLineBO.setStartTime(DateUtil.dateFormat(obj.getStartTime(), DateUtil.F_DATE));
                        businessLineBO.setEndTime(DateUtil.dateFormat(obj.getEndTime(), DateUtil.F_DATE));
                        businessLineBO.setStartCityCode(obj.getStartCityCode());
                        businessLineBO.setStartAddress(obj.getStartAddress());
                        businessLineBO.setEndCityCode(obj.getEndCityCode());
                        businessLineBO.setEndAddress(obj.getEndAddress());
                        if (obj.getStartCityCode()==null ||"".equals(obj.getStartCityCode())) {
                            businessLineBO.setStartCityCode(obj.getStartProvinceCode());
                        }
                        else
                        {
                            businessLineBO.setStartCityCode(obj.getStartCityCode());
                        }
                        if (obj.getEndCityCode()==null ||"".equals(obj.getEndCityCode())) {
                            businessLineBO.setEndCityCode(obj.getEndProvinceCode());
                        }
                        else
                        {
                            businessLineBO.setEndCityCode(obj.getEndCityCode());
                        }
                        businessLineBOList.add(businessLineBO);
                    }
                }
                emptyReportInfoBO.setFutureEmptyCarList(businessLineBOList);
                updRespHeadSuccess(response);
                return emptyReportInfoBO;
            }

        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("空车上报列表出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    /**
     * 空车上报删除
     * @author wyh
     */
    @RequestMapping(value = "/deleteEmptyCarReport", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.DELETE_EMPTY_CAR_REPORT)
    @Log(type = LogEnum.DELETE_EMPTY_CAR_REPORT)
    public Object deleteEmptyCarReport(HttpServletRequest request, HttpServletResponse response,
                                       String emptyCarId, String deleteType){
        try {
            if(StringUtils.isEmpty(emptyCarId) || StringUtils.isEmpty(deleteType)){
                return findErrorParam(response);
            }
            Long id = Long.parseLong(emptyCarId);
            Integer deleteTypeInt = Integer.parseInt(deleteType);
            String token = findToken(request);
            Response<Boolean> result = emptyCarReportService.deleteDriverEmptyReport(id, deleteTypeInt, token);
            return findResponseBoolean(result, response);
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("空车上报删除出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }
}
