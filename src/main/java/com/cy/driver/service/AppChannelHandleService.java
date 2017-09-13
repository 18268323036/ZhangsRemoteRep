package com.cy.driver.service;

import com.cy.pass.service.dto.AppChannelDTO;
import com.cy.pass.service.dto.base.Response;

/**
 * app 渠道包服务
 * Created by yanst on 2016/3/8.
 */
public interface AppChannelHandleService {

    /**
     * 根据渠道名称查询渠道包信息
     * @param channelName
     * @param clientType
     * @return
     */
    Response<AppChannelDTO> find(String channelName, Integer clientType);
}
