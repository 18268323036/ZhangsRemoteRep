package com.cy.driver.service.impl;

import com.cy.driver.service.OwnerItemService;
import com.cy.pass.service.OwnerItemStatService;
import com.cy.pass.service.dto.OwnerItemStatDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wyh on 2016/4/23.
 */
@Service("ownerItemService")
public class OwnerItemServiceImpl implements OwnerItemService {
    private static final Logger LOG = LoggerFactory.getLogger(OwnerItemServiceImpl.class);

    @Resource
    private OwnerItemStatService ownerItemStatService;

    /**
     * 根据货主id查询相关统计信息
     */
    @Override
    public OwnerItemStatDTO getByOwnerId(Long ownerId) {
        Response<OwnerItemStatDTO> response = ownerItemStatService.findByUserId(ownerId);
        if (!response.isSuccess()) {
            LOG.error("调用pass服务根据web用户id查询货主的统计信息失败，失败信息={}", response.getMessage());
        }
        return response.getData();
    }
}
