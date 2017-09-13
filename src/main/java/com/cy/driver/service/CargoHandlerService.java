package com.cy.driver.service;

import com.cy.cargo.service.dto.*;
import com.cy.cargo.service.dto.base.Response;

import java.util.List;

/**
 * Created by Administrator on 2015/7/14.
 * 获取处理服务
 */
public interface CargoHandlerService {

    /**
     * 省列表
     * @return
     * @throws Exception
     */
    public Response<List<TasteCargoDTO>> listCountByProvince() throws Exception;

    /**
     * 获取货源详情
     * @param cargoId
     * @param driverId
     * @return
     */
    public Response<CargoDetailDTO> getCargoDetail(long cargoId, long driverId);

    /**
     *获取货物详情
     * @param cargoId
     * @return
     */
    public Response<CargoInfoDTO> getCargoInfo(Long cargoId);

    /**
     * 保存货物点评信息
     * @param cargoAssessDTO
     * @return
     */
    public Response<Boolean> addCargoAssess(DriverCargoAssessInfoDTO cargoAssessDTO);

    /**
     * 保存报价信息
     * @param quoteDTO
     * @return
     */
    public Response<Boolean> addQuoteInfo(QuoteInfoDTO quoteDTO);
}
