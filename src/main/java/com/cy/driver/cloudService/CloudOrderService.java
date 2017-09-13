package com.cy.driver.cloudService;


import java.util.List;

/**
 * @author zhangxy 2016/7/22
 */
public interface CloudOrderService {

    List<Long> countOrderNum(Long driverId, List<Integer> queryOrderStateList);

}
