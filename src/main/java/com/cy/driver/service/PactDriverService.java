package com.cy.driver.service;

import com.cy.driver.domain.PageBase;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.base.Response;

import java.util.List;

/**
 * 合同司机
 * Created by wyh on 2015/7/6.
 */
public interface PactDriverService {

    /**
     * 根据合同id集合查询合同路线
     * @param pactLineIds
     * @return
     */
    List<VipDriverLineInfoDTO> listByPactLineIds(List<Long> pactLineIds);


    /**
     * 合同客户列表 根据状态查询
     * @param driverId
     * @param page
     * @param state
     * @return
     */
    PageBase<PactDriverInfo3DTO> queryPactInfoList(long driverId, long page, Byte state);

    /**
     * 查询合同客户列表
     * @param driverId 司机id
     * @param page 查询的页数
     * @return
     * @author wyh
     */
    Response<PactDriverListDTO> queryPactInfoList(long driverId, long page);

    /**
     * 合同客户确认
     * @param driverId 司机id
     * @param pactDriverId 合同司机id
     * @param pactStart 状态
     * @return
     * @author wyh
     */
    Response<Boolean> updatePactState(long driverId, long pactDriverId, int pactStart);

    /**
     * 查询合同客户线路详情
     * @param driverId 司机id
     * @param pactDriverId 合同司机id
     * @return
     * @author wyh
     */
    Response<PactDriverDetailsDTO> queryPactDriverDetails(long driverId, long pactDriverId);

    /**
     * 合同客户线路确认
     * @param driverId 司机id
     * @param vipDriverLineId 合同线路id
     * @param start 状态
     * @return
     * @author wyh
     */
    Response<Boolean> updateVipDriverLineState(long driverId, long vipDriverLineId, int start);

    /**
     * 合同车辆详情（不包括线路）
     * @param pactDriverId 合同司机id
     * @return
     * @author wyh
     */
    PactDriverInfo2DTO getPactDriverInfo(Long pactDriverId);

    /**
     * 合同车辆路线详情
     * @param lineId 路线id
     * @return
     * @author wyh
     */
    VipDriverLineInfoDTO getLineInfo(Long lineId);
}
