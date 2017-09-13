package com.cy.driver.service;

import com.cy.pass.service.dto.OpsActivityInfoDTO;
import com.cy.platformpay.service.dto.base.Response;

import java.math.BigDecimal;

/**
 * Created by yanst on 2015/11/2.
 * 红包
 */
public interface RedPacketsService {

    /**
     * 账户红包金额奖励
     * @param accountId 平台资金账户编号，必填
     * @param luckyMoney    奖励金额，必填
     * @return  CodeTable.SUCCESS<True>  红包奖励入库成功<br/>
     * CodeTable.ERROR  参数错误<br/>
     * CodeTable.ERROR_ACCOUNT_NOT_EXIST    账户不存在<br/>
     * CodeTable.RESULT_FAILURE 操作失败<br/>
     */
    public Response<Boolean> bonusLuckFee(Long accountId, BigDecimal luckyMoney);

    /**
     * 新用户注册获取红包金额数量
     * @param activityName 活动名称
     * @return
     */
    public com.cy.pass.service.dto.base.Response<OpsActivityInfoDTO> getRedPackets(String activityName);
}
