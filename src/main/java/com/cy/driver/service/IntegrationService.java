package com.cy.driver.service;

import com.cy.award.service.dto.AwardAccountDTO;

/**
 * 积分服务
 *
 * @author yanst 2016/4/23 15:03
 */
public interface IntegrationService {


    /**
     * 根据userId 获取积分总数
     * @param userType
     * @param userId
     * @return
     */
    AwardAccountDTO countByUserId(Byte userType, Long userId);
}
