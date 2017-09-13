package com.cy.driver.service;

import com.cy.pass.service.dto.OwnerItemStatDTO;

/**
 * 货主统计信息（比如：信用、评价、订单数量）
 * Created by wyh on 2016/4/23.
 */
public interface OwnerItemService {

    /**
     * 根据货主id查询相关统计信息
     * @param ownerId
     * @return 货主id
     * @author wyh
     */
    OwnerItemStatDTO getByOwnerId(Long ownerId);
}
