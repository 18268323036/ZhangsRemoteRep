package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SysAreaData;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.CarInfoParamBO;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.OftenCityHandleService;
import com.cy.driver.service.PointFService;
import com.cy.location.service.DriverLinePointService;
import com.cy.location.service.dto.OftenCitySaveDTO;
import com.cy.pass.service.dto.CarDTO;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanst 2016/4/18 14:24
 */
@Scope("prototype")
@Controller("perfectInformationAction")
public class PerfectInformationAction extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(PerfectInformationAction.class);

    @Resource
    private SystemData systemData;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Resource
    private OftenCityHandleService oftenCityHandleService;
    @Resource
    private PointFService pointFService;
    @Resource
    private DriverLinePointService driverLinePointService;

    /**
     * 完善车辆信息（3.4版本）
     * @param carInfoParamBO
     * @return
     */
    @RequestMapping(value = "/perfectInformation", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PERFECT_INFORMATION)
    @Log(type = LogEnum.ADD_CAR_INFO)
    public Object perfectInformation(HttpServletResponse response, CarInfoParamBO carInfoParamBO) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        //必传参数
        if(carInfoParamBO == null || StringUtils.isBlank(carInfoParamBO.getCarNumber()) || StringUtils.isBlank(carInfoParamBO.getCarLengthCode())
                || StringUtils.isBlank(carInfoParamBO.getCarTypeCode()) || StringUtils.isBlank(carInfoParamBO.getCarriageTypeCode())
                || (StringUtils.isBlank(carInfoParamBO.getCarLoad()) && StringUtils.isBlank(carInfoParamBO.getCarVolume()))
                || StringUtils.isBlank(carInfoParamBO.getOftenRunCitys())) {

            if (LOG.isErrorEnabled()) LOG.error("添加车辆信息：参数不完整");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        CarDTO carDTO = new CarDTO();
        //已经认证则不能修改车牌号  车牌校验
        if(EnumSubmitType.AUTH_SUCESS.getCode() != findAuthState(request)){
            String carNumber = carInfoParamBO.getCarNumber().toUpperCase();//转成大写
            if (StringUtils.isEmpty(carNumber) || !ValidateUtil.validateCarNumber(carNumber)) {
                if (LOG.isInfoEnabled())LOG.info("车牌号格式不正确");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20054,"车牌号:"+carNumber);
            }
            carDTO.setCarNumber(carNumber);
        }

        String tocken = findToken(request);
        String userId = findUserIdStr(request);
        carDTO.setDriverId(userId);
        carDTO.setTocken(tocken);
        carDTO.setCarLengthNewest(new BigDecimal(carInfoParamBO.getCarLengthCode()));
        if(StringUtils.isNotEmpty(carInfoParamBO.getCarLoad())){
            carDTO.setCarWeightNewest(new BigDecimal(carInfoParamBO.getCarLoad()));
        }
        if(StringUtils.isNotEmpty(carInfoParamBO.getCarVolume())) {
            carDTO.setCarCubageNewest(new BigDecimal(carInfoParamBO.getCarVolume()));
        }

        //常跑城市  去掉结尾,
        String oftenRunCity = carInfoParamBO.getOftenRunCitys();
        if(carInfoParamBO.getOftenRunCitys().endsWith(",")){
            oftenRunCity = oftenRunCity.substring(0,oftenRunCity.length()-1);
        }
        List<String> newCityCodeList = null;
        List<OftenCitySaveDTO> oftenCitySaveDTOs = new ArrayList<>();
        if (StringUtils.isNotBlank(oftenRunCity) ) {
            // 去重
            String[] cityCodeList = oftenRunCity.split(",");
            newCityCodeList = new ArrayList<String>();
            for (String code : cityCodeList) {
                if (!newCityCodeList.contains(code)) {
                    newCityCodeList.add(code);
                }
            }
            if (cityCodeList.length > 6) {
                if (LOG.isErrorEnabled())
                    LOG.error("添加常跑城市信息校验：长跑城市超过6个。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20065);
            }
            for(String code : newCityCodeList) {
                SysAreaData sysAreaDataCity = SystemData.getCityInfoByCode(code);
                OftenCitySaveDTO oftenCitySaveDTO = new OftenCitySaveDTO();
                oftenCitySaveDTO.setCity(sysAreaDataCity.getAreaName());
                SysAreaData sysAreaDataPro = SystemData.getProvinceInfoByCode(sysAreaDataCity.getParentCode());
                oftenCitySaveDTO.setProvince(sysAreaDataPro.getAreaName());
                oftenCitySaveDTO.setDriverId(Long.valueOf(userId));
                oftenCitySaveDTOs.add(oftenCitySaveDTO);
            }
        }

        carDTO.setCarTypesNew(carInfoParamBO.getCarTypeCode());
        carDTO.setCarriageType(carInfoParamBO.getCarriageTypeCode());
        carDTO.setCarPhoto(carInfoParamBO.getCarPhoto());

        try {
            Response<Boolean> serResponse = driverUserHandlerService.updateCarInfo(carDTO);
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("添加车辆信息(服务端)校验：参数不完整",serResponse.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                if (LOG.isErrorEnabled()) LOG.error("添加车辆信息(服务端)校验：",serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20111);
            }
            com.cy.location.service.dto.base.Response<Boolean> saveTracking = driverLinePointService.saveOftenCity(oftenCitySaveDTOs);
            if(saveTracking!=null && saveTracking.isSuccess() && saveTracking.getData()){
                if (LOG.isDebugEnabled()) LOG.error("保存轨迹(司机常跑城市)成功");
            }

            if (StringUtils.isNotBlank(carInfoParamBO.getCarPhoto())) {
                //完善头像积分奖励-3.4版本
                pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                        LogEnum.ADD_CAR_INFO.getEventCode(),null,null,convert2InSource(),null);
            }
        } catch (Exception e){
            if (LOG.isErrorEnabled()) LOG.error("添加车辆信息失败", e);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20111);
        }

        try {
            Response<String> serResponse1 =  oftenCityHandleService.saveOrUpdate(newCityCodeList, userId);
            if(!serResponse1.isSuccess()){
                updRespHeadError(response);
                if(CodeTable.ERROR.getCode() == serResponse1.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("添加常跑城市信息（服务端）校验：server.message="+serResponse1.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                if (LOG.isErrorEnabled()) LOG.error("保存常跑城市失败（服务端）校验：server.message="+serResponse1.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20112);
            }


            //常跑城市首次填写积分奖励-3.4版本
            pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                    LogEnum.ADD_OFTEN_RUN_CITYS.getEventCode(), null, null, convert2InSource(),null);
        } catch (Exception e){
            if (LOG.isErrorEnabled()) LOG.error("添加车辆信息成功，保存常跑城市失败", e);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20112);
        }
        updRespHeadSuccess(response);
        return resultMap;
    }


}
