package com.cy.driver.service;

import com.cy.pass.service.dto.LogOffApplyDTO;
import com.cy.pass.service.dto.base.Response;

/**
 * Created by Administrator on 2015/12/14.
 */
public interface CloseAcountService {

    /**
     * 保存注销申请
     * @param logOffApplyDTO
     * @return
     */
    public Response<Long> saveApply(LogOffApplyDTO logOffApplyDTO);

}
