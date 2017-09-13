package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.domain.EmptyCarSubmitParam;
import com.cy.driver.service.EmptyCarReportService;
import com.cy.pass.service.DriverEmptyReportService;
import com.cy.pass.service.dto.AddEmptyCarDTO;
import com.cy.pass.service.dto.DriverEmptyReportInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wyh on 2015/7/4.
 */
@Service("emptyCarReportServiceImpl")
public class EmptyCarReportServiceImpl implements EmptyCarReportService {
    private static final Logger LOG = LoggerFactory.getLogger(EmptyCarReportServiceImpl.class);

    @Resource
    private DriverEmptyReportService driverEmptyReportService;

    /**
     * 添加空车上报
     * @param addEmptyCarDTO
     * @return
     */
    @Override
    public Response<Boolean> addEmptyCar(AddEmptyCarDTO addEmptyCarDTO) {
        return driverEmptyReportService.addEmptyCar(addEmptyCarDTO);
    }

    /**
     * 添加空车上报
     * @param emptyCarSubmitParam
     * @return
     */
    @Override
    public Boolean addEmptyCarReport(EmptyCarSubmitParam emptyCarSubmitParam, Long driverId) {
        if(emptyCarSubmitParam == null || emptyCarSubmitParam.getEndCityList() == null || emptyCarSubmitParam.getEndCityList().size() == 0){
            LOG.debug("调用空车上参数不能为空");
            return Boolean.FALSE;
        }
        //3.4增加 省value、市value、县（code和value） 赋值
        for (LinkedTreeMap endCity  : emptyCarSubmitParam.getEndCityList()){
            AddEmptyCarDTO addEmptyCarDTO = new AddEmptyCarDTO();
            if(StringUtils.isEmpty((String)endCity.get("eProValue")))
                continue;
            addEmptyCarDTO.setStartTime(emptyCarSubmitParam.getsTime());
            addEmptyCarDTO.setEndTime(emptyCarSubmitParam.geteTime());
            addEmptyCarDTO.setDriverId(driverId);
            addEmptyCarDTO.setStartProCode(emptyCarSubmitParam.getsProCode());
            addEmptyCarDTO.setStartProValue(emptyCarSubmitParam.getsProValue());
            addEmptyCarDTO.setStartCityCode(emptyCarSubmitParam.getsCityCode());
            addEmptyCarDTO.setStartCityValue(emptyCarSubmitParam.getsCityValue());
            addEmptyCarDTO.setStartCountyCode(emptyCarSubmitParam.getsCountyCode());
            addEmptyCarDTO.setStartCountyValue(emptyCarSubmitParam.getsCountyValue());
            addEmptyCarDTO.setEndProCode((String)endCity.get("eProCode"));
            addEmptyCarDTO.setEndProValue((String)endCity.get("eProValue"));
            addEmptyCarDTO.setEndCityCode((String)endCity.get("eCityCode"));
            addEmptyCarDTO.setEndCityValue((String)endCity.get("eCityValue"));
            addEmptyCarDTO.setEndCountyCode((String)endCity.get("eCountyCode"));
            addEmptyCarDTO.setEndCountyValue((String)endCity.get("eCountyValue"));

            Response<Boolean> response = driverEmptyReportService.addEmptyCar(addEmptyCarDTO);
            if(response == null){
                LOG.debug("调用空车上报底层返回失败，返回对象为null");
                return Boolean.FALSE;
            }
            if(!response.isSuccess()){
                LOG.debug("调用空车上报底层返回失败，失败信息。reponse = {}", JSON.toJSONString(response));
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;

    }

    @Override
    public Response<DriverEmptyReportInfoDTO> queryDriverEmptyReportInfo(Long driverId) {
        return driverEmptyReportService.queryDriverEmptyReport(driverId);
    }

    @Override
    public Response<Boolean> deleteDriverEmptyReport(Long id, int deleteType, String token) {
        return driverEmptyReportService.deleteDriverEmptyReport(id, deleteType, token);
    }

    /**
     * 查询空车上报列表
     */
    @Override
    public DriverEmptyReportInfoDTO queryEmptyList(Long driverId) {
        Response<DriverEmptyReportInfoDTO> response = driverEmptyReportService.queryDriverEmptyReport(driverId);
        if (!response.isSuccess()) {
            LOG.error("调用pass服务查询司机的空车上报列表失败,driverId={},返回信息={}", driverId, response.getMessage());
            return null;
        }
        if (response.getData() == null) {
            LOG.error("调用pass服务查询司机的空车上报列表失败,driverId={},返回空车列表信息为空", driverId);
            return null;
        }
        return response.getData();
    }
}
