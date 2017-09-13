package com.cy.driver.service;

import com.cy.pass.service.dto.SysPushCounterParamDTO;

/**
 * 推送消息处理服务
 * Created by yanst on 2015/11/3.
 */
public interface PushMsgService {

    /**
     *
     * @param sysPushCounterParamDTO
     */
    public void findPushCounter(SysPushCounterParamDTO sysPushCounterParamDTO);

}
