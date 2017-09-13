package com.cy.driver.service.impl;

import com.cy.driver.service.FeedbackHandleService;
import com.cy.pass.service.UserFeedBackService;
import com.cy.pass.service.dto.DriverFeedbackDTO;
import com.cy.pass.service.dto.base.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by mr on 2015/7/7.
 */
@Service
public class FeedbackHandleServiceImpl implements FeedbackHandleService{
    @Resource
    private UserFeedBackService userFeedBackService;
    @Override
    public Response<Boolean> save(DriverFeedbackDTO driverFeedback) {
        return userFeedBackService.save(driverFeedback);
    }
}
