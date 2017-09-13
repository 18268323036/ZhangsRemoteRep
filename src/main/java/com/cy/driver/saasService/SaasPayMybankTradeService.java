package com.cy.driver.saasService;

import com.cy.saas.pay.model.dto.TradePasswordDTO;
import com.cy.saas.pay.model.dto.WithdrawalDTO;
import com.cy.top56.common.Response;

import java.math.BigDecimal;

/**
 * 提现操作
 * Created by nixianjing on 17/8/18.
 */
public interface SaasPayMybankTradeService {

    /**
     * 提现
     * @param withdrawalDTO
     * @param tradePasswordDTO 交易密码，必填
     * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
     *         {@link Response.CodeTable#ERROR} 参数错误（失败）<br/>
     *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
     *         {@link Response.CodeTable#DATA_NONE_EXIST} 会员信息不存在（失败）<br/>
     *         {@link Response.CodeTable#LOCK_FAIL} 锁定失败<br/>
     *         {@link Response.CodeTable#LOCK_WAIT} 锁定等待<br/>
     *         {@link Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
     *         {@link Response.CodeTable#FAIL1} 失败（失败）<br/>
     *         {@link Response.CodeTable#FAIL2} 提现记录已存在<br/>
     *         {@link Response.CodeTable#PASSWORD_FAIL} 密码错误（失败）<br/>
     */
    Response<String> withdrawal(WithdrawalDTO withdrawalDTO, TradePasswordDTO tradePasswordDTO);

    /**
     * 获取提现手续费
     * @param bankId 银行卡id，必填
     * @param amount 提现金额，必填
     * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
     *         {@link Response.CodeTable#ERROR} 参数错误（失败）<br/>
     *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
     */
    BigDecimal getWithdrawalProcFee(String bankId, BigDecimal amount);
}
