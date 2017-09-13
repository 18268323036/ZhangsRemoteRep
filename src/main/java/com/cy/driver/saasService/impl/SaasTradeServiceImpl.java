package com.cy.driver.saasService.impl;

import com.cy.driver.saasService.SaasTradeService;
import com.cy.saas.business.model.dto.TradeWithdrawalApplyDTO;
import com.cy.saas.business.model.po.TradeWithdrawal;
import com.cy.saas.business.service.TradeService;
import com.cy.top56.common.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by nixianjing on 17/8/18.
 */
@Service("saasTradeService")
public class SaasTradeServiceImpl implements SaasTradeService {

    @Resource
    private TradeService utmsTradeService;


    @Override
    public TradeWithdrawal applyWithdrawal(TradeWithdrawalApplyDTO applyDTO) {
//        /** 提现金额,not null */
//        private BigDecimal amount;
//
//        /** 提现手续费,not null */
//        private BigDecimal fee;
//
//        /** 银行卡id,not null */
//        private String bankId;
//
//        /** 银行编号,not null */
//        private String bankCode;
//
//        /** 银行全称,not null */
//        private String bankName;
//
//        /** 银行卡号,not null */
//        private String bankAccountNo;
//
//        /** 开户名,not null */
//        private String accountName;
//
//        /** 支行号（联行号） */
//        private String branchNo;
//
//        /** 分支行名称 */
//        private String bankBranch;
//
//        /** 卡类型（DC-借记、CC-贷记（信用卡）、PB-存折、OC-其他） */
//        private String cardType;
//
//        /** 卡属性（C-对私、B对公） */
//        private String cardAttribute;
//
//        /** 卡支付属性 */
//        private String payAttribute;
//
//        /** 创建用户id,not null */
//        private Long createUserId;
//
//        /** 创建用户类型（1-快到网司机 3-SAAS用户）,not null */
//        private Byte createUserType;
//
//        /** 创建用户名称 */
//        private String createUserName;
//
//        /** 创建用户主账户id,not null */
//        private Long createParentUserId;
//
//        /** 创建用户主账户名称 */
//        private String createParentUserName;



        /**
         * 提现
         *
         * @param applyDTO
         * @return
         * @throws {@link Response.CodeTable#SUCCESS} 成功 <br/>
         *                {@link Response.CodeTable#EXCEPTION} 失败 <br/>
         *                {@link Response.CodeTable#ERROR} 参数错误 <br/>
         */
        Response<TradeWithdrawal> response = utmsTradeService.applyWithdrawal(applyDTO);
        if(response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    @Override
    public TradeWithdrawal getWithdrawalDetail(String businessTradeNo) {
//        /** 主键id,not null */
//        private Long id;
//
//        /** 业务交易号（trade_instruction.business_trade_no）,not null */
//        private String businessTradeNo;
//
//        /** 提现金额,not null */
//        private BigDecimal amount;
//
//        /** 提现手续费,not null */
//        private BigDecimal fee;
//
//        /** 银行卡id,not null */
//        private String bankId;
//
//        /** 银行编号,not null */
//        private String bankCode;
//
//        /** 银行全称,not null */
//        private String bankName;
//
//        /** 银行卡号,not null */
//        private String bankAccountNo;
//
//        /** 开户名,not null */
//        private String accountName;
//
//        /** 支行号（联行号） */
//        private String branchNo;
//
//        /** 分支行名称 */
//        private String bankBranch;
//
//        /** 卡类型（DC-借记、CC-贷记（信用卡）、PB-存折、OC-其他） */
//        private String cardType;
//
//        /** 卡属性（C-对私、B对公） */
//        private String cardAttribute;
//
//        /** 卡支付属性 */
//        private String payAttribute;
//
//        /** 订单标题,not null */
//        private String subject;
//
//        /** 订单描述 */
//        private String body;
//
//        /** 备注 */
//        private String memo;
//
//        /** 创建用户id,not null */
//        private Long createUserId;
//
//        /** 创建用户类型（1-快到网司机 3-SAAS用户）,not null */
//        private Byte createUserType;
//
//        /** 创建用户名称 */
//        private String createUserName;
//
//        /** 创建用户主账户id,not null */
//        private Long createParentUserId;
//
//        /** 创建用户主账户名称 */
//        private String createParentUserName;
//
//        /** 创建时间,not null */
//        private Date createTime;
        /**
         * 获取提现详情
         *
         * @param businessTradeNo
         * @return
         * @throws {@link Response.CodeTable#SUCCESS} 成功 <br/>
         *                {@link Response.CodeTable#EXCEPTION} 失败 <br/>
         *                {@link Response.CodeTable#ERROR} 参数错误 <br/>
         */
        Response<TradeWithdrawal> response = utmsTradeService.getWithdrawalDetail(businessTradeNo);
        if(response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    @Override
    public TradeWithdrawal getWithdrawalDetail(Long tradeWithdrawalId) {
        /**
         * 获取提现详情
         *
         * @param tradeWithdrawalId
         * @return
         * @throws {@link Response.CodeTable#SUCCESS} 成功 <br/>
         *                {@link Response.CodeTable#EXCEPTION} 失败 <br/>
         *                {@link Response.CodeTable#ERROR} 参数错误 <br/>
         */
        Response<TradeWithdrawal> response = utmsTradeService.getWithdrawalDetail(tradeWithdrawalId);
        if(response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    @Override
    public boolean modifyTradeStateFailed(String businessTradeNo) {
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
        Response response = utmsTradeService.modifyTradeStateFailed(businessTradeNo);
        if(response.isSuccess()) {
            return true;
        }
        return false;
    }
}
