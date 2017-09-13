package com.cy.driver.service.impl;

import com.cy.driver.common.util.DateUtil;
import com.cy.driver.service.LocationService;
import com.cy.driver.service.TransactionService;
import com.cy.location.service.dto.OrderCaseDTO;
import com.cy.location.service.dto.OrderCaseQueryDTO;
import com.cy.order.service.OrderService;
import com.cy.order.service.dto.TransactionCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by wyh on 2015/12/23.
 */
@Service("transactionCopyService")
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Resource
    private OrderService orderService;
    @Resource
    private LocationService locationService;

    /**
     * 修改订单是否存在定位信息异常
     */
    @Override
    public boolean updateOrderWarn(TransactionInfoDTO orderDTO, TransactionCargoDTO cargoDTO, int orderState){
        try {
            Date startTime = DateUtil.dateTimeToDateStart(orderDTO.getDriverVerifyTime());
            StringBuffer start = new StringBuffer();
            start.append(cargoDTO.getStartProvince());
            start.append(cargoDTO.getStartCity());
            if (cargoDTO.getStartCounty() != null) {
                start.append(cargoDTO.getStartCounty());
            }
            Date endTime = DateUtil.dateTimeToDateEnd(new Date());
            StringBuffer end = new StringBuffer();
            end.append(cargoDTO.getEndProvince());
            end.append(cargoDTO.getEndCity());
            if (cargoDTO.getEndCounty() != null) {
                end.append(cargoDTO.getEndCounty());
            }
            OrderCaseQueryDTO orderCaseQueryDTO = new OrderCaseQueryDTO();
            orderCaseQueryDTO.setBusinessSource(1);
            orderCaseQueryDTO.setBusinessId(orderDTO.getId());
            orderCaseQueryDTO.setDriverId(orderDTO.getDriverId());
            orderCaseQueryDTO.setStartCounty(cargoDTO.getStartCounty());
            orderCaseQueryDTO.setStartCity(cargoDTO.getStartCity());
            orderCaseQueryDTO.setStartProvince(cargoDTO.getStartProvince());
            orderCaseQueryDTO.setStartTime(startTime);
            orderCaseQueryDTO.setEndCounty(cargoDTO.getEndCounty());
            orderCaseQueryDTO.setEndCity(cargoDTO.getEndCity());
            orderCaseQueryDTO.setEndProvince(cargoDTO.getEndProvince());
            orderCaseQueryDTO.setEndTime(endTime);
            OrderCaseDTO orderCaseDTO = locationService.isExistHistTracking(orderCaseQueryDTO);
            if (orderCaseDTO == null) {
                LOG.error("修改订单是否存在定位信息异常失败，调用判断订单是否异常失败，orderId={},返回的map对象为空或则出错", orderDTO.getId());
                return false;
            }
            if (orderCaseDTO.getWarnCode()==null) {
                LOG.error("修改订单是否存在定位信息异常失败，调用判断订单是否异常失败，orderId={},返回的map.warnCode为空", orderDTO.getId());
                return false;
            }
            /** 是否存在定位信息异常：0 无异常 1 装货地定位缺失 2 卸货地定位缺丢 3 装卸货地定位均缺丢 */
            LOG.isDebugEnabled(); LOG.debug("查询是否存在定位信息异常成功，code{},message{}",orderCaseDTO.getWarnCode(),orderCaseDTO.getWarnMessage());
            Byte locationInfoWarn = Byte.parseByte(orderCaseDTO.getWarnCode().toString());
            byte resultWarn = locationInfoWarn.byteValue();
            if (locationInfoWarn.intValue() == 0) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("无需修改订单是否存在定位信息异常，订单状态无异常");
                }
                resultWarn = 0;
            } else {
                if (orderState == 1) {
                    /** 装货操作 */
                    if(locationInfoWarn == 2) {
                        resultWarn = 0;
                    } else {
                        resultWarn = 1;
                    }
                }
            }
            Response<Boolean> result = orderService.updateWarn(orderDTO.getId(), resultWarn);
            if (result == null) {
                LOG.error("修改订单是否存在定位信息异常失败，调用order服务修改订单中的定位信息异常出错，orderId={}", orderDTO.getId());
                return false;
            }
            if (!result.isSuccess()) {
                LOG.error("修改订单是否存在定位信息异常失败，调用order服务修改订单中的定位信息异常失败，orderId={}，返回信息={}", orderDTO.getId(), result.getMessage());
                return false;
            }
            if (result.getData() == null || !result.getData().booleanValue()) {
                LOG.error("修改订单是否存在定位信息异常失败，调用order服务修改订单中的定位信息异常失败，orderId={}，返回修改结果={}", orderDTO.getId(), result.getData());
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.error("修改订单是否存在定位信息异常出现异常", e);
            return false;
        }
    }
}
