package com.cy.driver.service;

import com.cy.driver.domain.EmptyCarSubmitParam;
import com.cy.pass.service.dto.AddEmptyCarDTO;
import com.cy.pass.service.dto.DriverEmptyReportInfoDTO;
import com.cy.pass.service.dto.base.Response;

/**
 * Created by wyh on 2015/7/4.
 */
public interface EmptyCarReportService {

    /**
     * 添加空车上报
     * @param addEmptyCarDTO
     * @return
     */
    public Response<Boolean> addEmptyCar(AddEmptyCarDTO addEmptyCarDTO);

    /**
     * 添加空车上报
     * @param emptyCarSubmitParam
     * @return
     */
    public Boolean addEmptyCarReport(EmptyCarSubmitParam emptyCarSubmitParam, Long driverId);

    /**
     * 获得空车上报信息
     * @param driverId
     * @return
     * @author wyh
     */
    Response<DriverEmptyReportInfoDTO> queryDriverEmptyReportInfo(Long driverId);

    /**
     * 删除空车上报信息
     * @param id
     * @param deleteType 删除类型
     * @param token
     * @return
     * @author wyh
     */
    Response<Boolean> deleteDriverEmptyReport(Long id, int deleteType, String token);

    /**
     * 查询空车上报列表
     * @param driverId
     * @return
     * @author wyh
     */
    DriverEmptyReportInfoDTO queryEmptyList(Long driverId);
}
