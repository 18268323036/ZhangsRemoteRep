package com.cy.driver.cloudService.impl;

import com.cy.driver.cloudService.CloudCommonWaybillService;
import com.cy.rdcservice.service.CarrierService;
import com.cy.rdcservice.service.WaybillInfoService;
import com.cy.rdcservice.service.WaybillOperService;
import com.cy.rdcservice.service.dto.*;
import com.cy.search.service.SearchOrderService;
import com.cy.search.service.dto.base.PageInfo;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.base.Response;
import com.cy.search.service.dto.request.DriverIdAndStateDTO;
import com.cy.search.service.dto.response.OrderInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxy 2016/7/28 10:27
 */
@Service("cloudCommonWaybillService")
public class CloudCommonWaybillServiceImpl implements CloudCommonWaybillService {


    Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SearchOrderService searchOrderService2;
    @Resource
    private WaybillInfoService waybillInfoService;
    @Resource
    private WaybillOperService waybillOperService;
    @Resource
    private CarrierService carrierService;


    @Override
    public List<Long> queryWaybillNum(Long driverId, List<Integer> queryOrderStateList) {
        Response<List<Long>> response = searchOrderService2.countCommonByDriver(driverId,queryOrderStateList);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询订单首页数量信息失败");
            return null;
        }
        return response.getData();
    }

    @Override
    public Map<String,Object> queryWaybillList(Long driverId, Integer page, Integer filterState, Integer 	queryOrderState) {
        PageInfo<DriverIdAndStateDTO> pageInfo = new PageInfo<>(page);
        DriverIdAndStateDTO driverIdAndStateDTO = new DriverIdAndStateDTO();
        driverIdAndStateDTO.setDriverId(driverId);
        driverIdAndStateDTO.setFilterState(filterState);
        driverIdAndStateDTO.setQueryOrderState(queryOrderState);
        pageInfo.setData(driverIdAndStateDTO);
        PageResult<OrderInfoDTO> pageResult = searchOrderService2.pageCommonByDriver(pageInfo);
        if(pageResult==null || !pageResult.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询订单列表信息失败");
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalRecord",String.valueOf(pageResult.getTotalRecord()));
        map.put("totalPage",String.valueOf(pageResult.getTotalPage()));
        map.put("dataList",pageResult.getDataList());
        return map;
    }

    @Override
    public WaybillDetailDTO queryWaybillDetail(Long waybillNum) {
        com.cy.top56.common.Response<WaybillDetailDTO> response = waybillInfoService.queryWaybillDetail(waybillNum);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillInfoService查询运单详情失败");
            return null;
        }
        return response.getData();
    }

    @Override
    public List<Integer> querySignInInfo(List<Long> waybillNum) {
        com.cy.top56.common.Response<List<Integer>> response = carrierService.countWaitSignBySourceWaybillIds(waybillNum);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务carrierService查询签收信息失败");
            return null;
        }
        return response.getData();
    }

    @Override
    public boolean modifyWaybillStatus(WaybillStateModifyDTO waybillStateModifyDTO, ProtocolDTO protocolDTO) {
        com.cy.top56.common.Response<Boolean> response = waybillOperService.modifyWaybillStateNew(waybillStateModifyDTO,protocolDTO);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillOperService改变运单状态失败");
            return false;
        }
        if(response.isSuccess() && response.getData()!=null && response.getData()){
            return true;
        }
        return false;
    }


    @Override
    public boolean driverQuoteWaybill(Long orderId, BigDecimal quoteAmount, BigDecimal advancePayment) {
        WaybillTransportQuoteDTO waybillTransportQuoteDTO = new WaybillTransportQuoteDTO();
        waybillTransportQuoteDTO.setWaybillId(orderId);
        waybillTransportQuoteDTO.setTransportPrepayFare(advancePayment);
        waybillTransportQuoteDTO.setTransportTotalFare(quoteAmount);

        WaybillDetailDTO waybillDetailDTO = queryWaybillDetail(orderId);



        waybillTransportQuoteDTO.setModifyUserId(waybillDetailDTO.getWaybillInfoDTO().getTransportUserId());
        waybillTransportQuoteDTO.setModifyUserName(waybillDetailDTO.getWaybillInfoDTO().getTransportUserName());
        waybillTransportQuoteDTO.setModifyUserType(new Byte("1"));

        com.cy.top56.common.Response<Boolean> response = waybillOperService.transportQuote(waybillTransportQuoteDTO);
        if(response==null || !response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillOperService报价运单失败");
            return false;
        }
        if(response.isSuccess() && response.getData()){
            return true;
        }
        return false;
    }

    @Override
    public Long turnWaybill(WaybillTurnOrderDTO waybillTurnOrderDTO, Integer takeWay) {
        com.cy.top56.common.Response<Long> response = waybillOperService.turnOrderByDriver(waybillTurnOrderDTO,takeWay);
        if(response==null){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillOperService转单失败");
            return null;
        }
        if(!response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillOperService转单失败,失败原因{}",response.getMessage());
            return null;
        }
        return response.getData();
    }

    @Override
    public WaybillInfoDTO getByParentOrderId(Long waybillId) {
        com.cy.top56.common.Response<WaybillInfoDTO> response = waybillInfoService.getByParentWaybillId(waybillId);
        if(response==null){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillInfoService查询子运单");
            return null;
        }
        if(!response.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用rdcService服务waybillInfoService查询子运单失败,失败原因{}",response.getMessage());
            return null;
        }
        return response.getData();
    }


}
