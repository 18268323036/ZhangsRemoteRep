package com.cy.driver.saasService;

import com.cy.saas.business.model.dto.TradeWithdrawalApplyDTO;
import com.cy.saas.business.model.po.TradeWithdrawal;
import com.cy.top56.common.Response;

/**
 * 提现业务操作
 * Created by nixianjing on 17/8/18.
 */
public interface SaasTradeService {


    /**
     * 提现(申请提现业务)
     * @param applyDTO
     * @return
     */
    TradeWithdrawal applyWithdrawal(TradeWithdrawalApplyDTO applyDTO);

    /**
     * 根据业务编号获取提现详情
     * @param businessTradeNo
     * @return
     */
    TradeWithdrawal getWithdrawalDetail(String businessTradeNo);

    /**
     * 提现业务ID获取提现详情
     *
     * @param tradeWithdrawalId
     * @return
     * @throws {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *                {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *                {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    TradeWithdrawal getWithdrawalDetail(Long tradeWithdrawalId);


    /**
     * 修改交易状态为失败
     *      目前只有提现支持，支付运费支付失败状态还为0
     *
     * @param businessTradeNo
     * @return
     * @throws {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *                {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *                {@link Response.CodeTable#ERROR} 参数错误 <br/>
     *                {@link Response.CodeTable#FAIL1} 业务交易号不存在 <br/>
     *                {@link Response.CodeTable#FAIL2} 交易状态不正确 <br/>
     *                {@link Response.CodeTable#FAIL3} 更新状态失败 <br/>
     */
    boolean modifyTradeStateFailed(String businessTradeNo);
}
