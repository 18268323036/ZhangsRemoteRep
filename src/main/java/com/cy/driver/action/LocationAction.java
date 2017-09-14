package com.cy.driver.action;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.LocationBo;
import com.cy.driver.service.LocationService;
import com.cy.driver.service.LogService;
import com.cy.location.service.dto.base.CodeTable;
import com.cy.location.service.dto.base.Response;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 位置
 * Created by wyh on 2015/7/28.
 */
@Scope("prototype")
@RestController("locationAction")
public class LocationAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(LocationAction.class);
    @Resource
    private LocationService locationService;
    @Resource
    private SystemData systemData;
    @Resource
    private LogService logService;

    /**
     * 位置上传
     * @param request
     * @param response
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/submitUploadLocation", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SUBMIT_UPLOAD_LOCATION)
    @Log(type = LogEnum.SUBMIT_UPLOAD_LOCATION)
    public Object submitUploadLocation(HttpServletRequest request, HttpServletResponse response){
        try {
            LocationBo locationBo = buildParam(request, LocationBo.class);
            if (logger.isDebugEnabled())
                logger.debug("位置上传的请求参数：locationBo={}", JSON.toJSONString(locationBo));
            if(StringUtils.isBlank(locationBo.getLongitude())
                    || StringUtils.isBlank(locationBo.getLatitude())
                    || locationBo.getType() == null){
                return findErrorParam(response);
            }

            Long driverId = findUserId(request);
            Byte authState = findAuthState(request);
            Response<Boolean> result = locationService.saveLocation(locationBo, driverId, authState);
            if(!result.isSuccess()){
                logger.error("调用location位置上传失败，失败信息={}", result.getMessage());
                if(result.getCode() == CodeTable.INVALID_ARGS.getCode()){
                    return findErrorParam(response);
                }
                if(result.getCode() == CodeTable.ADDRESS_CHANGE_FAIL.getCode()){
                    if(logger.isErrorEnabled()){
                        logger.error("位置上传经纬度转化地址为空");
                    }
                    return findHandleFail(response);
                }
                return findException(response);
            }
            if(locationBo.getType() == null || locationBo.getType() == 0){
                logService.saveLogInfo(driverId,request, LogEnum.BE_SUBMIT_UPLOAD_LOCATION);
            }else{
                logService.saveLogInfo(driverId,request, LogEnum.SUBMIT_UPLOAD_LOCATION);
            }
            updRespHeadSuccess(response);
            return null;
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("位置上传出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 初始化定时位置上传参数
     * @param request
     * @param response
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/initTimeLocParam", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.INIT_TIME_LOC_PARAM)
    @Log(type = LogEnum.INIT_TIME_LOC_PARAM)
    public Object initTimeLocParam(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("minuteTime", systemData.getAppLocMinutes());
            //TODO  后续业务待完善
            map.put("distance","1");//千米
            updRespHeadSuccess(response);
            return map;
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("位置上传出错", e);
            }
        }
        return findException(response);
    }
}
