/**
 * Project Name:ads3
 * File Name:WithdrawServiceImpl.java
 * Package Name:com.cy.driver.service.impl
 * Date:2015年9月11日下午3:25:46
 * Copyright (c) 2015, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.cy.driver.service.impl;

import com.cy.driver.common.redis.PasswordClient;
import com.cy.driver.service.WithdrawService;
import com.cy.pass.service.UserWithdrawalRecordService;
import com.cy.pass.service.dto.UserWithdrawalRecordDTO;
import com.cy.platformpay.service.AccountBankService;
import com.cy.platformpay.service.CapitalAccountService;
import com.cy.platformpay.service.CashTradeService;
import com.cy.platformpay.service.PayTradeService;
import com.cy.platformpay.service.dto.AccountBankDTO;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import com.cy.platformpay.service.dto.CashTradeParamDTO;
import com.cy.platformpay.service.dto.base.CodeTable;
import com.cy.platformpay.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:WithdrawServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2015年9月11日 下午3:25:46 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
@Service("withdrawService")
public class WithdrawServiceImpl implements WithdrawService{
    private static final Logger LOG = LoggerFactory.getLogger(WithdrawServiceImpl.class);
	@Resource
	private CashTradeService cashTradeService;

	@Resource
	private UserWithdrawalRecordService userWithdrawalRecordService;

	@Resource
	private AccountBankService accountBankService;

	@Resource
	private CapitalAccountService capitalAccountService;

	@Resource
	private PasswordClient passwordClient;

	@Resource
	private PayTradeService payTradeService;

	/**
	 *
	 * @param accountId  资金账户ID
	 * @param bindBankId  提现银行卡
	 * @param fee 金额
	 * @param remark   备注
	 * @param cashPwd 提现密码
	 * @return
	 */
	@Override
	public Response<Long> withdrawCash(Long driverId, Long accountId,Long bindBankId,BigDecimal fee,String remark, String cashPwd) {

		Response<CapitalAccountDTO> responseCad = capitalAccountService.getByDriver(driverId);
		if(!responseCad.isSuccess() ||  responseCad.getData() == null){
			return new Response<Long>(CodeTable.RESULT_FAILURE);
		}
		if(responseCad.getData().getId().longValue() != accountId.longValue()){
			return new  Response<Long>(CodeTable.RESULT_FAILURE);
		}

		//获取银行卡信息
		Response<AccountBankDTO> responseBankInfo = accountBankService.getByBindBankId(bindBankId);
		if(!responseBankInfo.isSuccess() || responseBankInfo.getData() == null ){
			return new Response<Long>(CodeTable.ERROR_CARD_NOTIN_USER);//银行卡不存在
		}
		AccountBankDTO accountBankDTO = responseBankInfo.getData();

        /**
         * 修改时间：2016-03-29 11:20
         * 修改人：王远航
         * 修改依据：支付密码和提现密码合成交易密码，数据库保留支付密码。兼容货主app1.1.1
         */

        /** 从原来的判断提现密码是否锁定改成判断支付密码是否锁定 */
		if(responseCad.getData().getPayLocked() == 1){//已锁定
			return new Response<Long>(CodeTable.ERROR_ACCOUNT_CASH_LOCK);
		}

        /** 从原来的验证提现密码是否正确改成验证支付密码是否正确 */
        Response<Boolean> validResponse = payTradeService.validPayPwd(accountId, cashPwd);
        if (validResponse == null || validResponse.getData() == null){
            LOG.error("{}提现失败，提现密码验证失败", driverId);
            return new Response<Long>(CodeTable.RESULT_FAILURE);
        }
        if(!validResponse.getData()){
			LOG.error("{}提现失败，提现密码不正确", driverId);
			//1.没有超过五次，次数加1 2.超过五次锁定提现密码
			boolean flag = passwordClient.havePassLimit(driverId);
			if(flag){
                /** 从原来的锁定提现密码改成锁定支付密码 */
                capitalAccountService.payPwdLock(accountId);
				passwordClient.removeDriverWithdram(driverId);
				return new Response<Long>(CodeTable.ERROR_ACCOUNT_CASH_LOCK);
			}
            return new Response<Long>(CodeTable.ERROR_PASSWORD);
        }

		passwordClient.removeDriverWithdram(driverId);

		//判断余额是否充足
		if(fee.compareTo(responseCad.getData().getTotalMoney()) == 1){//余额不足
			return new Response<Long>(CodeTable.ERROR_ACCOUNT_FEE_LESS);
		}

		//插入记录  t_user_withdrawal_record
		UserWithdrawalRecordDTO uwrd = new UserWithdrawalRecordDTO();
		uwrd.setUserKind((byte)1);//司机
		uwrd.setUserId(driverId);
		uwrd.setMoney(fee);//提现金额
		uwrd.setToBankName(accountBankDTO.getBankName());//提现至银行帐户行名'
		uwrd.setToBankCode(accountBankDTO.getBankCode());//提现至银行帐户行号
		uwrd.setToAccountName(accountBankDTO.getAccountName());//
		uwrd.setToAccountCode(accountBankDTO.getAccountCode());//
		uwrd.setWithdrawalTime(new Date());
		uwrd.setExecuteStatus((byte) 0);//提现中
		uwrd.setExecuteTime(new Date());
		com.cy.pass.service.dto.base.Response<Long> responseId = userWithdrawalRecordService.insert(uwrd);
		if(!responseId.isSuccess() || responseId.getData() == null ){
			return new Response<Long>(CodeTable.RESULT_FAILURE);
		}

		//调用platform-pay  提现
		CashTradeParamDTO cashTradeParamDTO = new CashTradeParamDTO();
		cashTradeParamDTO.setAccountId(accountId);
		cashTradeParamDTO.setBindBankId(bindBankId);
		cashTradeParamDTO.setFee(fee);
		cashTradeParamDTO.setRemark(remark);
		cashTradeParamDTO.setBusinessEventId(responseId.getData());//记录表主键

        if (LOG.isWarnEnabled())
            LOG.warn("{}准备开始提现，提现金额={}", driverId, fee.toString());

		Response<Long> response = cashTradeService.cashRMB(cashTradeParamDTO);

		if(response.getCode() == CodeTable.RESULT_USER_WAIT.getCode()){
			return response;
		}
		//回写   t_user_withdrawal_record
		UserWithdrawalRecordDTO uwrd1 = new UserWithdrawalRecordDTO();
		uwrd1.setId(responseId.getData());
		if(response.isSuccess() && response.getData() != null){//成功
			uwrd1.setExecuteStatus((byte) 1);
		}else//失败
		{
			uwrd1.setExecuteStatus((byte) -1);
		}
		uwrd1.setPlatformCommandId(response.getData());
		uwrd1.setExecuteTime(new Date());
		userWithdrawalRecordService.updateState(uwrd1);
		return response;//操作失败
	}

}

