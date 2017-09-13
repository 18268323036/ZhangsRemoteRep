package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.core.rabbitmq.ExtensionBusEnum;
import com.cy.core.rabbitmq.ExtensionBusMQProducer;
import com.cy.core.rabbitmq.ExtensionBusMsg;
import com.cy.core.rabbitmq.PlatformEnum;
import com.cy.driver.domain.PageBase;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.QueryOrderService;
import com.cy.order.service.DistributeService;
import com.cy.order.service.OrderService;
import com.cy.order.service.SearchOrderService;
import com.cy.order.service.dto.*;
import com.cy.order.service.dto.base.PageInfo;
import com.cy.order.service.dto.base.PageResult;
import com.cy.order.service.dto.base.Response;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.order.service.dto.distribute.DistributeOrderDTO;
import com.cy.order.service.dto.order.OrderStateParamDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.rdcservice.service.WaybillOperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询订单列表
 * Created by wyh on 2015/7/25.
 */
@Service("queryOrderService")
public class QueryOrderServiceImpl implements QueryOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(QueryOrderServiceImpl.class);
    @Resource
    private OrderService orderService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private SearchOrderService searchOrderService;
    @Resource
    private DistributeService distributeService;
    @Resource
    private WaybillOperService waybillOperService;
    @Resource
    private ExtensionBusMQProducer extensionBusMQProducer;

    @Override
    public int selectByOrderComplete(Long driverId) {
        try {
            return orderService.selectByOrderComplete(driverId);
        } catch (Exception e) {
            LOG.error("获取已完成的订单数量出现异常", e);
            return 0;
        }
    }

    /**
     * 验证订单
     * @param driverId 司机ID
     */
    public  Response<Boolean> validateDriverOrder(Long driverId){
        return orderService.validateDriverOrder(driverId);
    }

    @Override
    public  Integer collectFreightNums(Long driverId){
        Response<Integer>  resultFreightNums = orderService.collectFreightCounts(driverId);
        int freightNums = 0;
        if(resultFreightNums.isSuccess())
        {
            if(resultFreightNums.getData()!=null){
                freightNums = resultFreightNums.getData();
            }
        }
        return freightNums;
    }


    @Override
    public  PageResult<CollectFreightDTO> collectFreightList(Integer page,Long driverId){
        PageInfo pageInfo = new PageInfo(page);
        return orderService.collectFreightList(pageInfo, driverId);
    }

    @Override
    public Response<OrderStateRespDTO> auditCancelOrder(Long driverId,Long orderId,Integer auditState){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> response = driverUserHandlerService.getDriverUserInfo(driverId);
        String driverName = "";
        if(response.isSuccess() && response.getData() != null){
            driverName = response.getData().getName();
        }
        OrderStateParamDTO paramDTO = new OrderStateParamDTO();
        paramDTO.setUserId(driverId);
        paramDTO.setUserName(driverName);
        paramDTO.setOrderId(orderId);
        paramDTO.setCommandId(auditState);
        paramDTO.setCancelCause("");
        return orderService.updateStartByDriver(paramDTO);
    }

    @Override
    public Response<OrderStateRespDTO> cancelOrder(Long driverId, Long orderId, Integer auditState, String cancelReason){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> response = driverUserHandlerService.getDriverUserInfo(driverId);
        String driverName = "";
        if(response.isSuccess() && response.getData() != null){
            driverName = response.getData().getName();
        }
        OrderStateParamDTO paramDTO = new OrderStateParamDTO();
        paramDTO.setUserId(driverId);
        paramDTO.setUserName(driverName);
        paramDTO.setOrderId(orderId);
        paramDTO.setCommandId(auditState);
        paramDTO.setCancelCause(cancelReason);
        return orderService.updateStartByDriver(paramDTO);
    }

    @Override
    public OrderAndCargoDTO getOrderDetails(Long orderId) {
        Response<OrderAndCargoDTO> result = orderService.getOrderAndCargo(orderId);
        if (result == null) {
            LOG.error("调用order服务查询订单相关信息出错,orderId={}", orderId);
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用order服务查询订单相关信息失败,orderId={},返回信息={}", orderId, result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用order服务查询订单相关信息失败,orderId={},返回订单相关信息为空", orderId);
            return null;
        }
        if (result.getData().getOrderInfo() == null) {
            LOG.error("调用order服务查询订单相关信息失败,orderId={},返回订单信息为空", orderId);
            return null;
        }
        if (result.getData().getCargoInfo() == null)
            result.getData().setCargoInfo(new TransactionCargoDTO());
        if (result.getData().getDriverInfo() == null)
            result.getData().setDriverInfo(new TransactionCarryDriverDTO());
        return result.getData();
    }

    /**
     * 查看订单详情包含取消的订单
     */
    @Override
    public OrderAndCargoDTO getOrderDetails2(Long orderId) {
        Response<OrderAndCargoDTO> result = orderService.getOrderDetails(orderId);
        if (result == null) {
            LOG.error("调用order服务查询订单详情包含取消的订单和货源和司机信息出错,orderId={}", orderId);
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用order服务查询订单详情包含取消的订单和货源和司机信息失败,orderId={},返回信息={}", orderId, result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用order服务查询订单详情包含取消的订单和货源和司机信息失败,orderId={},返回订单相关信息为空", orderId);
            return null;
        }
        if (result.getData().getOrderInfo() == null) {
            LOG.error("调用order服务查询订单详情包含取消的订单和货源和司机信息失败,orderId={},返回订单信息为空", orderId);
            return null;
        }
        if (result.getData().getCargoInfo() == null)
            result.getData().setCargoInfo(new TransactionCargoDTO());
        if (result.getData().getDriverInfo() == null)
            result.getData().setDriverInfo(new TransactionCarryDriverDTO());
        return result.getData();
    }

    @Override
    public Response<OrderStateRespDTO> updateOrderStatus(Long driverId, Integer orderStatus, Long orderId, String driverName) {
        OrderStateParamDTO paramDTO = new OrderStateParamDTO();
        paramDTO.setUserId(driverId);
        paramDTO.setUserName(driverName);
        paramDTO.setOrderId(orderId);
        paramDTO.setCommandId(orderStatus);
        paramDTO.setCancelCause("");
        return orderService.updateStartByDriver(paramDTO);
    }

    @Override
    public PageBase<DistributeOrderDTO> queryOrderList(long driverId, Integer orderStatus, Integer page) {
        PageInfo pageInfo = new PageInfo(page);
        PageResult<DistributeOrderDTO> pageResult = searchOrderService.queryOrderByDriver(pageInfo, driverId, orderStatus);
        if (!pageResult.isSuccess()) {
            LOG.error("调用order服务查询司机订单分页列表失败，失败信息={}", pageResult.getMessage());
            return null;
        }
        PageBase<DistributeOrderDTO> pageBase = new PageBase();
        pageBase.setTotalNum(pageResult.getTotalRecord());
        pageBase.setTotalPage(pageResult.getTotalPage());
        pageBase.setListData(pageResult.getDataList());
        return pageBase;
    }

    @Override
    public Response<Boolean> updateDriverIdForMobile(Long driverId, String mobilePhone) {
        return orderService.updateDriverIdForMobile(driverId, mobilePhone);
    }

    /**
     * 获得派单信息
     */
    @Override
    public DistributeInfoDTO getDistributeInfo(Long distributeId) {
        Response<DistributeInfoDTO> result = distributeService.getDistributeInfo(distributeId);
        if (result == null) {
            LOG.error("调用order服务查询派单信息出错,distributeId={}", distributeId);
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用order服务查询派单信息失败,distributeId={},返回信息={}", distributeId, result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用order服务查询派单信息失败,distributeId={},返回派单信息为空", distributeId);
            return null;
        }
        return result.getData();
    }

    /**
     * 获得订单信息
     */
    @Override
    public TransactionInfoDTO getTransactionInfo(Long orderId) {
        Response<TransactionInfoDTO> response = orderService.getTransactionInfo(orderId);
        if (response == null) {
            LOG.error("调用order服务查询订单信息出错,orderId={}", orderId);
            return null;
        }
        if (!response.isSuccess()) {
            LOG.error("调用order服务查询订单信息失败,orderId={},返回信息={}", orderId, response.getMessage());
            return null;
        }
        if (response.getData() == null) {
            LOG.error("调用order服务查询订单信息失败,orderId={},返回订单信息为空", orderId);
            return null;
        }
        return response.getData();
    }

    /**
     *
     * @param driverId
     * @param state 状态 0：全部；1：待承运；2：承运中；3：带装货；4：待卸货；5：待评价；6：其他
     * @return
     */
    @Override
    public Integer countOrderNumByState(Long driverId, Integer state) {
        Response<Integer> result = orderService.countOrders(driverId, state, null);
        if(result == null){
            if(LOG.isErrorEnabled()){
                LOG.error("调用底层服务，获取待承运订单失败，返回对象为空");
            }
            return null;
        }
        if(!result.isSuccess() || result.getData() == null){
            if(LOG.isErrorEnabled()){
                LOG.error("调用底层服务，获取待承运订单失败，返回数据为空，result={}", JSON.toJSONString(result));
            }
            return null;
        }
        return result.getData();
    }

    /**
     * 搜索订单
     */
    @Override
    public PageBase<DistributeOrderDTO> searchOrderList(long driverId, String searchContent, Integer page) {
        PageInfo pageInfo = new PageInfo(page);
        PageResult<DistributeOrderDTO> pageResult = searchOrderService.searchOrderByDriver(pageInfo, driverId, searchContent);
        if (!pageResult.isSuccess()) {
            LOG.error("调用order服务搜索司机订单分页列表失败，失败信息={}", pageResult.getMessage());
            return null;
        }
        PageBase<DistributeOrderDTO> pageBase = new PageBase();
        pageBase.setTotalNum(pageResult.getTotalRecord());
        pageBase.setTotalPage(pageResult.getTotalPage());
        pageBase.setListData(pageResult.getDataList());
        return pageBase;
    }

    @Override
    public boolean driverReg(String mobilePhone, Long userId, String userName) {
        try {
            com.cy.top56.common.Response<Boolean> response = waybillOperService.driverReg(mobilePhone,userId,userName);
            if(response==null){
                LOG.isDebugEnabled();  LOG.debug("同步司机订单信息失败");
            }
            if(!response.isSuccess()){
                LOG.isDebugEnabled();  LOG.debug("同步司机订单信息失败,失败原因{}",response.getMessage());
            }
            /**
             * 司机注册消息队列
             */
            List<Long> idList = new ArrayList<>();
            idList.add(userId);
            ExtensionBusMsg message = ExtensionBusMsg.newExtensionBusMsg(PlatformEnum.KDW, ExtensionBusEnum.KDW_DRIVER, JSON.toJSONString(idList), null, null);
            extensionBusMQProducer.sendMessage(message);

            return response.getData();
        }catch (Exception e){
            LOG.error("同步订单信息时发生异常，异常信息{}",e);
        }
        return false;
    }


}
