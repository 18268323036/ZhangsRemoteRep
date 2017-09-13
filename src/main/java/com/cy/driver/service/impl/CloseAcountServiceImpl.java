package com.cy.driver.service.impl;

import com.cy.driver.service.CloseAcountService;
import com.cy.pass.service.LogOffApplyService;
import com.cy.pass.service.dto.LogOffApplyDTO;
import com.cy.pass.service.dto.base.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yanst on 2015/12/14.
 * 注销账户
 *
 */
@Service("closeAcountService")
public class CloseAcountServiceImpl implements CloseAcountService {

    @Resource
    private LogOffApplyService logOffApplyService;

    public Response<Long> saveApply(LogOffApplyDTO logOffApplyDTO){
        return logOffApplyService.saveApply(logOffApplyDTO);
    }

}
