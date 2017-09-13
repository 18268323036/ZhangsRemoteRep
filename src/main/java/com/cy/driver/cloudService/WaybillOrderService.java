package com.cy.driver.cloudService;

import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.rdcservice.service.dto.*;
import com.cy.search.service.dto.request.DriverIdAndKeywordDTO;

import java.util.List;

/**
 * 司机转单Service
 * Created by nixianjing on 16/7/27.
 */
public interface WaybillOrderService {

    /**
     * 司机转单的数量
     * @param driverId 司机ID
     * @param statcList 查询转单状态list
     * @return
     */
    public Object countTurnOrderByDriverId(Long driverId, List<Integer> statcList);


    /**
     * 查询转单列表
     * @param page 查询第几页
     * @param turnOrderQueryDTO (司机ID\转单订单状态)
     * @return
     */
    public Object pageTurnOrderByDriverList(Integer page, TurnOrderQueryDTO turnOrderQueryDTO);


    /**
     * 查询转单详情
     * @param orderId
     * @return
     */
    public Object findTurnOrderByOrderId(String orderId);


    /**
     * 修改转单运单运费
     * @param waybillModifyFareDTO
     * @return
     */
    public Object updateFare(WaybillModifyFareDTO waybillModifyFareDTO);


    /**
     * waybillId - 运单id，必填
     state - 状态（2采用承运方报价、-2不采用承运方报价），必填
     modifyUserDTO - 修改用户信息
     * @return
     */
    public Object useTransportQuote(String orderId, String state, ModifyUserDTO modifyUserDTO);


    /**
     * 保存转单运单评价
     * @param waybillAssessSaveDTO
     * @return
     */
    public Object saveAssess(WaybillAssessSaveDTO waybillAssessSaveDTO);


    /**
     * 根据运单Id查看运单评价信息
     * @param waybillId
     * @return
     */
    public Object findAssessByWaybillId(Long waybillId);


    /**
     * 获取转单运单承运方位置信息
     * @param waybillId
     * @return
     */
    public Object findWaybillPathById(String waybillId);


    /**
     * 查询转单运单历史轨迹列表
     * @param waybuillId
     * @param page
     * @return
     */
    public Object findWaybillPathByIdList(String waybuillId, String page);

    /**
     * 运单查询列表
     * @param pageInfo
     * @return
     */
    public Object queryWaybillList(com.cy.search.service.dto.base.PageInfo<DriverIdAndKeywordDTO> pageInfo);


    /**
     * 获取司机信息
     * @param driverId
     * @return
     */
    public DriverUserInfoDTO getDriverUserInfo(Long driverId);

    /**
     * 保存运单跟踪记录
     * @param waybillId
     * @param trackingSaveDTO
     * @param modifyUserDTO
     * @return
     */
    boolean saveWaybillTrackingByDriver(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO);


    /**
     * 保存货运轨迹
     * @param trackingSave2DTO
     * @param modifyUserDTO
     * @return
     */
    Boolean saveTrackingByYPS(TrackingSave2DTO trackingSave2DTO, ModifyUserDTO modifyUserDTO);
}
