package com.cy.driver.service;

import com.cy.order.service.dto.base.Response;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 *
 * 问题上报
 */
public interface ProblemService {

    /**
     * 保存订单问题上报信息
     * @param orderId
     * @param content
     * @param imgUrlList
     * @return
     */
    public Response<Boolean> saveProblem(Long driverId, Long orderId, String content, List<String> imgUrlList);

}
