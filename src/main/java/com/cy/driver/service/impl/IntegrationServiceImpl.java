package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.award.service.AccountService;
import com.cy.award.service.dto.AwardAccountDTO;
import com.cy.award.service.dto.base.Response;
import com.cy.driver.service.IntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yanst 2016/4/23 15:06
 */
@Service("integrationService")
public class IntegrationServiceImpl implements IntegrationService {

    private Logger LOG = LoggerFactory.getLogger(IntegrationServiceImpl.class);

    @Resource
    private AccountService awardAccountService;

    @Override
    public AwardAccountDTO countByUserId(Byte userType, Long userId) {

        Response<AwardAccountDTO> response = awardAccountService.findByUser(userType, userId);
        if(response == null){
            if(LOG.isErrorEnabled())LOG.error("获取账户积分失败，返回对象为空，入参用户：userId={}", userId);
            return null;
        }
        if(!response.isSuccess() || response.getData() == null){
            if(LOG.isErrorEnabled())LOG.error("获取账户积分失败，返回错误信息，入参用户：userId={}, 错误信息：response={}", userId, JSON.toJSONString(response));
        }
        return response.getData();
    }
}
