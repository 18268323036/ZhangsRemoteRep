package com.cy.driver.service.impl;

import com.cy.driver.service.AppChannelHandleService;
import com.cy.pass.service.AppChannelService;
import com.cy.pass.service.dto.AppChannelDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yanst on 2016/3/8.
 */
@Service("appChannelHandleServiceImpl")
public class AppChannelHandleServiceImpl implements AppChannelHandleService {

    private static Logger LOGGER = LoggerFactory.getLogger(AppChannelHandleServiceImpl.class);

    @Resource
    private AppChannelService appChannelService;

    /**
     * 根据渠道名称查询渠道包信息
     * @param channelName
     * @param clientType
     * @return
     */
    public Response<AppChannelDTO> find(String channelName, Integer clientType){
        return appChannelService.find(channelName, clientType);
    }

}
