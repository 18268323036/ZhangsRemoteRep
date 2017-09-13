package com.cy.driver.service;

import com.cy.driver.domain.LocationBo;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.location.service.dto.OrderCaseDTO;
import com.cy.location.service.dto.OrderCaseQueryDTO;
import com.cy.location.service.dto.base.Response;

/**
 * Created by wyh on 2015/7/28.
 */
public interface LocationService {

    /**
     * 保存位置信息
     * @param locationBo
     * @param driverId
     * @param authState 认证状态
     * @return
     * @author wyh
     */
    Response<Boolean> saveLocation(LocationBo locationBo, long driverId, Byte authState);

    /**
     * 获得初始化时间
     * @return
     * @author wyh
     */
    String findInitTimeValue();

    /**
     * 查询最新的位置服务
     * @param driverId
     * @return
     * @author wyh
     */
    LastLocationDTO queryLastLocation(Long driverId);

    /**
     * 判断订单是否异常
     * @param orderCaseQueryDTO
     * @return
     */
    public OrderCaseDTO isExistHistTracking(OrderCaseQueryDTO orderCaseQueryDTO);

}
