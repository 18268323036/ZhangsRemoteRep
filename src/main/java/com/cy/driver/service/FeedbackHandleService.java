package com.cy.driver.service;

import com.cy.pass.service.dto.DriverFeedbackDTO;
import com.cy.pass.service.dto.base.Response;

/**
 * Created by mr on 2015/7/7.
 */
public interface FeedbackHandleService {

    /**
     * 保存司机反馈信息
     * @param driverFeedback
     * @return
     */
    public Response<Boolean> save(DriverFeedbackDTO driverFeedback);
}
