package com.cy.driver.service.impl;

import com.cy.driver.service.RedPacketsService;
import com.cy.pass.service.OpsActivityInfoService;
import com.cy.pass.service.dto.OpsActivityInfoDTO;
import com.cy.platformpay.service.CapitalAccountService;
import com.cy.platformpay.service.dto.base.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/2.
 */
@Service("redPacketsService")
public class RedPacketsServiceImpl implements RedPacketsService {

    @Resource
    private CapitalAccountService capitalAccountService;

    @Resource
    private OpsActivityInfoService opsActivityInfoService;

    /**
     * 账户红包金额奖励
     * @param accountId 平台资金账户编号，必填
     * @param luckyMoney    奖励金额，必填
     * @return  CodeTable.SUCCESS<True>  红包奖励入库成功<br/>
     * CodeTable.ERROR  参数错误<br/>
     * CodeTable.ERROR_ACCOUNT_NOT_EXIST    账户不存在<br/>
     * CodeTable.RESULT_FAILURE 操作失败<br/>
     */
    public Response<Boolean> bonusLuckFee(Long accountId,BigDecimal luckyMoney){
        return capitalAccountService.bonusLuckFee(accountId, luckyMoney);
    }


    public com.cy.pass.service.dto.base.Response<OpsActivityInfoDTO> getRedPackets(String activityName){
       return opsActivityInfoService.getByActivityName(activityName);
    }

}
