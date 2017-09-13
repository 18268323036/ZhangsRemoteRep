package com.cy.driver.service;

import com.cy.driver.domain.PageBase;
import com.cy.order.service.dto.CollectFreightDTO;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.OrderStateRespDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.base.PageResult;
import com.cy.order.service.dto.base.Response;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.order.service.dto.distribute.DistributeOrderDTO;

/**
 * 查询订单列表
 * Created by wyh on 2015/7/25.
 */
public interface QueryOrderService {


    /**
     * 获取订单数量
     * @param driverId 司机ID
     */
    public  int selectByOrderComplete(Long driverId);

    /**
     * 验证订单
     * @param driverId 司机ID
     */
    public  Response<Boolean> validateDriverOrder(Long driverId);

    /**
     * 待收运费数量
     * @param driverId 司机ID
     * @return  Integer 待收运费数量
     */
    public  Integer collectFreightNums(Long driverId);

    /**
     *
     * @param page 页码
     * @param driverId 司机ID
     * @return
     */
    public  PageResult<CollectFreightDTO> collectFreightList(Integer page, Long driverId);

    /**
     *是否同意取消订单
     * @param driverId
     * @param orderId
     * @param auditState
     * @return
     */
    Response<OrderStateRespDTO> auditCancelOrder(Long driverId, Long orderId, Integer auditState);

    /**
     * 取消订单
     * @param driverId 司机ID
     * @param orderId  订单ID
     * @param auditState
     * @param cancelReason
     * @return
     */
    Response<OrderStateRespDTO> cancelOrder(Long driverId, Long orderId, Integer auditState, String cancelReason);

    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    OrderAndCargoDTO getOrderDetails(Long orderId);

    /**
     * 查看订单详情包含取消的订单
     * @param orderId
     * @return
     */
    OrderAndCargoDTO getOrderDetails2(Long orderId);

    /**
     *订单状态变更
     * @param driverId  司机id
     * @param orderStatus  列表状态(0全部订单、1待承运、2承运中)
     * @param orderId  订单ID
     * @return
     */
    Response<OrderStateRespDTO> updateOrderStatus(Long driverId, Integer orderStatus, Long orderId, String driverName);

    /**
     * 获得订单列表
     * @param driverId 司机id
     * @param orderStatus 列表状态(0全部订单、1待承运、2承运中)
     * @param page 页码(从1开始)
     * @return
     * @author wyh
     */
    PageBase<DistributeOrderDTO> queryOrderList(long driverId, Integer orderStatus, Integer page);

    /**
     * 根据登录帐号（司机手机号码） 补driverId
     * @param driverId 司机ID
     * @param driverMobiphone 手机号码
     * @return
     */
    Response<Boolean> updateDriverIdForMobile(Long driverId, String driverMobiphone);

    /**
     * 获得派单信息
     * @param distributeId 派单id
     * @return
     * @author wyh
     */
    DistributeInfoDTO getDistributeInfo(Long distributeId);

    /**
     * 获得订单信息
     * @param orderId 订单id
     * @return
     * @author wyh
     */
    TransactionInfoDTO getTransactionInfo(Long orderId);

    /**
     * 统计订单数量
     * @param driverId
     * @param state 状态 0：全部；1：待承运；2：承运中；3：带装货；4：待卸货；5：待评价；6：其他
     * @return
     */
    Integer countOrderNumByState(Long driverId, Integer state);

    /**
     * 搜索订单
     * @param driverId 司机id
     * @param searchContent 搜索内容
     * @param page 页码
     * @return
     * @author wyh
     */
    PageBase<DistributeOrderDTO> searchOrderList(long driverId, String searchContent, Integer page);

    /**
     * 司机注册后同步运单
     */
    boolean driverReg(String mobilePhone, Long userId, String userName);
}

