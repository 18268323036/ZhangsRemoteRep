package com.cy.driver.cloudService.impl;

import com.cy.driver.cloudService.CloudOrderService;
import com.cy.search.service.SearchOrderService;
import com.cy.search.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangxy 2016/7/22 16:56
 */
@Service("cloudOrderService")
public class CloudOrderServiceImpl implements CloudOrderService {

    @Resource
    private SearchOrderService searchOrderService2;

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Long> countOrderNum(Long driverId , List<Integer> queryOrderStateList ) {
        Response<List<Long>> response = searchOrderService2.countCommonByDriver(driverId,queryOrderStateList);
        if(response==null){
            LOG.error("获取订单数量失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取订单数量异常，返回结果失败");
            return null;
        }
        return response.getData();
    }
}
