package com.cy.driver.service.impl;

import com.cy.driver.service.ProblemService;
import com.cy.order.service.OrderProblemService;
import com.cy.order.service.dto.base.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 * 问题上报
 *
 */
@Service("ProblemService")
public class ProblemServiceImpl implements ProblemService {

    @Resource
    private OrderProblemService orderProblemService;

    /**
     * 保存订单问题上报信息
     * @param orderId
     * @param content
     * @param imgUrlList
     * @return
     */
    public Response<Boolean> saveProblem(Long driverId,Long orderId,String content,List<String> imgUrlList){
        return  orderProblemService.saveProblem(driverId, orderId,content,imgUrlList);
    }
}
