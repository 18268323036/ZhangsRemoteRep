/**
 * Project Name:ads3
 * File Name:WithdrawCashAction.java
 * Package Name:com.cy.driver.action
 * Date:2015年9月11日下午2:25:25
 * Copyright (c) 2015, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.saasService.SaasPayBankService;
import com.cy.driver.saasService.SaasPayMybankTradeService;
import com.cy.driver.saasService.SaasPayUserService;
import com.cy.driver.saasService.SaasTradeService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.WithdrawService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.platformpay.service.dto.base.CodeTable;
import com.cy.platformpay.service.dto.base.Response;
import com.cy.saas.business.model.dto.TradeWithdrawalApplyDTO;
import com.cy.saas.business.model.po.TradeWithdrawal;
import com.cy.saas.pay.model.dto.AccountBalanceDTO;
import com.cy.saas.pay.model.dto.TradePasswordDTO;
import com.cy.saas.pay.model.dto.WithdrawalDTO;
import com.cy.saas.pay.model.enums.PayAccountTypeEnum;
import com.cy.saas.pay.model.enums.PayUserTypeEnum;
import com.cy.saas.pay.model.po.PayUserBank;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:WithdrawCashAction <br/>
 * Function: 提现. <br/>
 * Date:     2015年9月11日 下午2:25:25 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */

@Scope("prototype")
@RestController("withdrawCashAction")
@RequestMapping(value = "/safeSSL")
public class WithdrawCashAction extends BaseAction {

	 private static Logger logger = LoggerFactory.getLogger(WithdrawCashAction.class);
	 
	 @Resource
	 private WithdrawService withdrawService;

	 @Resource
	 private DriverUserHandlerService driverUserHandlerService;

	@Resource
	private SaasPayUserService saasPayUserService;

	@Resource
	private SaasPayBankService saasPayBankService;
	@Resource
	private SaasPayMybankTradeService saasPayMybankTradeService;
	@Resource
	private SaasTradeService saasTradeService;

	/**
	 * 提现
	 * @param request
	 * @param response
	 * @param withdrawAmount 提现金额
	 * @param withdrawPassword 提现密码
	 * @param bankId 银行卡ID
	 * @return
	 */
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.WITHDRAW)
	@Log(type = LogEnum.WITHDRAW)
	public Object withdraw(HttpServletRequest request, HttpServletResponse response,
						   String withdrawAmount, String withdrawPassword, String bankId) {
		try {
			Map<String ,String > resultMap = new HashMap<String , String>();
 			if (StringUtils.isEmpty(withdrawPassword)) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20097);
			}
			if (StringUtils.isEmpty(withdrawAmount)) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20097);
			}
			if (StringUtils.isEmpty(bankId)) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30103);
			}

			if (logger.isWarnEnabled())
				logger.warn("用户ID:{},提现金额：{}，bankId:{}",findUserId(request), withdrawAmount, bankId);


			com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> response1 = driverUserHandlerService.getDriverUserInfo(findUserId(request));
			if(!response1.isSuccess()){
				if (logger.isErrorEnabled())
					logger.error("用户:{},未认证。", findUserId(request));
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20217);
			}

			Response<Long> resultData = 
					withdrawService.withdrawCash(findUserId(request), findcapitalAccountId(request),new Long(bankId),new BigDecimal(withdrawAmount),"提现",withdrawPassword.toUpperCase());
			
			if (resultData.isSuccess()) {
				updRespHeadSuccess(response);
				resultMap.put("withdrawResult", "0");//成功
				return resultMap;
			}else{
				if(resultData.getCode() == CodeTable.ERROR_ACCOUNT_FEE_LESS.getCode()){//余额不足
					if (logger.isErrorEnabled())
						logger.error("余额不足");
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20214);
				}
				if(resultData.getCode() == CodeTable.ERROR_PASSWORD.getCode()){//提现密码不正确
					if (logger.isErrorEnabled())
						logger.error("提现密码不正确");
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20213);
				}
				if(resultData.getCode() == CodeTable.ERROR_ACCOUNT_CASH_LOCK.getCode()){//提现密码已锁定
					if (logger.isErrorEnabled())
						logger.error("提现密码已锁定");
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20219);
				}
				if(resultData.getCode() == CodeTable.ERROR_CARD_NOTIN_USER.getCode()){//银行卡信息不存在
					if (logger.isErrorEnabled())
						logger.error("银行卡信息不存在");
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20215);
				}
				if (resultData.getCode() == CodeTable.ERROR_CASH_RMB_SINGLE_LIMIT.getCode()) {//单笔提现超过限制
					if (logger.isErrorEnabled())
						logger.error("单笔提现超过限制");
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20216);
				}
				if (resultData.getCode() == CodeTable.ERROR_CASH_RMB_DAY_LIMIT.getCode()) {//当日提现金额超过上限
					if (logger.isErrorEnabled())
						logger.error("当日提现金额超过上限");
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20229);
				}
				if (resultData.getCode() == CodeTable.RESULT_FAILURE.getCode()) {
					if (logger.isErrorEnabled())
						logger.error("错误信息(服务端)==>操作失败，登录帐号：{}", findcapitalAccountId(request));
					updRespHeadError(response);
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20210);
				}
				//这个错误信息是直接有支付网关抛给客户端的
				if (resultData.getCode() == CodeTable.RESULT_WGPAY_FAIL.getCode()) {
					if (logger.isErrorEnabled())
						logger.error("提现网关业务操作失败,资金账户ID=:{}，错误信息:{}", findcapitalAccountId(request), resultData.getMessage());
					updRespHeadError(response);
					return new JSonResponse(ApiResultCodeEnum.SER_20222.getCode(), resultData.getMessage());
				}
				if(resultData.getCode() == CodeTable.RESULT_USER_WAIT.getCode()){//提现中
					updRespHeadSuccess(response);
					resultMap.put("withdrawResult", "1");//提现中
					return resultMap;
				}

				if (logger.isWarnEnabled())
					logger.warn("提现未知错误，用户ID为：{}", findUserId(request));

			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("提现错误出错", e);
			}
		}
		return findException(response);
	}





	/**
	 * UTMS用户提现
	 * @param bankId 银行卡ID
	 * @param amount 提现金额
	 * @param password 提现密码
	 * @return
	 */
	@RequestMapping(value = "/saasWithdrawal", method = RequestMethod.POST)
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_WITHDRAWAL)
	@ResponseBody
	@Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
	public Object saasWithdrawal(String bankId,BigDecimal amount,String password) {
		/**
		 * 获取用户信息
		 * @param dirverId 司机ID
		 * @return
		 */
		com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> response1 = driverUserHandlerService.getDriverUserInfo(findUserId());
		if(!response1.isSuccess()) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20050);
		}
		DriverUserInfoDTO driverUserInfoDTO = response1.getData();
		/**
		 * 银行卡信息
		 */
		PayUserBank payUserBank = saasPayBankService.getPayUserBank(findUserId().toString(),bankId);
		if(payUserBank == null) {//银行卡不存在
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30501);
		}
		/**
		 * 获取手续费
		 */
		BigDecimal fee = saasPayMybankTradeService.getWithdrawalProcFee(bankId,amount);
		if(fee == null) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30502);
		}
		/**
		 * 获取UTMS账号信息
		 */
		AccountBalanceDTO accountBalanceDTO = saasPayUserService.listAccountBalance(PayAccountTypeEnum.BASIC.getCode(),findUserId().toString());
		if(accountBalanceDTO == null) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30503);
		}
		/**
		 * 可用余额
		 */
		BigDecimal utmsPayAmount = accountBalanceDTO.getAvailableBalance();
		/**
		 * 判断提现金额+手续费是否大于余额
		 */
		if(amount.add(fee).compareTo(utmsPayAmount)==1) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30504);
		}
		/**
		 * 提现业务申请
		 */
		TradeWithdrawalApplyDTO applyDTO = new TradeWithdrawalApplyDTO();
		/** 提现金额,not null */
		applyDTO.setAmount(amount);
		/** 提现手续费,not null */
		applyDTO.setFee(fee);
		/** 银行卡id,not null */
		applyDTO.setBankId(payUserBank.getBankId());
		/** 银行编号,not null */
		applyDTO.setBankCode(payUserBank.getBankCode());
		/** 银行全称,not null */
		applyDTO.setBankName(payUserBank.getBankName());
		/** 银行卡号,not null */
		applyDTO.setBankAccountNo(payUserBank.getBankAccountNo());
		/** 开户名,not null */
		applyDTO.setAccountName(payUserBank.getAccountName());
		/** 支行号（联行号） */
		applyDTO.setBranchNo(payUserBank.getBranchNo());
		/** 分支行名称 */
		applyDTO.setBankBranch(payUserBank.getBankBranch());
		/** 卡类型（DC-借记、CC-贷记（信用卡）、PB-存折、OC-其他） */
		applyDTO.setCardType(payUserBank.getCardType());
		/** 卡属性（C-对私、B对公） */
		applyDTO.setCardAttribute(payUserBank.getCardAttribute());
		/** 卡支付属性 */
		applyDTO.setPayAttribute(payUserBank.getPayAttribute());
		/** 创建用户id,not null */
		applyDTO.setCreateUserId(findUserId());
		/** 创建用户类型（1-快到网司机 3-SAAS用户）,not null */
		applyDTO.setCreateUserType(new Byte("1"));
		/** 创建用户名称 */
		applyDTO.setCreateUserName(driverUserInfoDTO.getName());
		/** 创建用户主账户id,not null */
		applyDTO.setCreateParentUserId(findUserId());
		/** 创建用户主账户名称 */
		applyDTO.setCreateParentUserName(driverUserInfoDTO.getName());
		TradeWithdrawal tradeWithdrawal = saasTradeService.applyWithdrawal(applyDTO);
		if(tradeWithdrawal == null) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30505);
		}
		WithdrawalDTO withdrawalDTO = new WithdrawalDTO();
		/** 订单号，必填 */
		withdrawalDTO.setOrderNo(tradeWithdrawal.getBusinessTradeNo());
		/** 银行卡id，必填 */
		withdrawalDTO.setBusinessId(bankId);
		/** 提现金额，必填 */
		withdrawalDTO.setTotalAmount(amount);
		/** 订单标题，必填 */
		withdrawalDTO.setSubject(tradeWithdrawal.getSubject());
		/** 订单描述 */
		withdrawalDTO.setBody(tradeWithdrawal.getBody());
		/** 备注 */
		withdrawalDTO.setMemo(tradeWithdrawal.getMemo());
		/** 提现手续费，必填 */
		withdrawalDTO.setBuyerFee(fee);
		/** 提现用户id，必填 */
		withdrawalDTO.setBuyerUserId(findUserId().toString());
		/** 提现用户类型（Driver-司机、Saas-saas用户），必填 {@link PayUserTypeEnum#code} */
		withdrawalDTO.setBuyerUserType(PayUserTypeEnum.DRIVER.getCode());
		/** 提现用户名称 */
		withdrawalDTO.setBuyerUserName(driverUserInfoDTO.getName());
		TradePasswordDTO tradePasswordDTO = new TradePasswordDTO();
		tradePasswordDTO.setTradePassword(password);
		tradePasswordDTO.setPasswordIsEncrypt(true);
		com.cy.top56.common.Response<String> response2 = saasPayMybankTradeService.withdrawal(withdrawalDTO,tradePasswordDTO);
		if(response2.isSuccess()) {
			updRespHeadSuccess(response);
			return null;
		}
		/**
		 * 提现
		 * @param withdrawalDTO
		 * @param tradePasswordDTO 交易密码，必填
		 * @return {@link com.cy.top56.common.Response.CodeTable#SUCCESS} 成功<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#ERROR} 参数错误（失败）<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#EXCEPTION} 程序出错<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#DATA_NONE_EXIST} 会员信息不存在（失败）<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#LOCK_FAIL} 锁定失败（失败）<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#LOCK_WAIT} 锁定等待<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#FAIL1} 失败（失败）<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#FAIL2} 提现记录已存在<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#PASSWORD_FAIL} 密码错误（失败）<br/>
		 *         {@link com.cy.top56.common.Response.CodeTable#FREEZE1} 帐户冻结（失败）<br/>
		 */
		if(response2.getCode() == com.cy.top56.common.Response.CodeTable.ERROR.getCode()) {//（失败）
			saasTradeService.modifyTradeStateFailed(tradeWithdrawal.getBusinessTradeNo());
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30511,"提现失败,参数异常!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.EXCEPTION.getCode()) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30509,"提现失败,系统异常!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.DATA_NONE_EXIST.getCode()) {//（失败）
			saasTradeService.modifyTradeStateFailed(tradeWithdrawal.getBusinessTradeNo());
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30506,"提现失败,锁定失败!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.LOCK_FAIL.getCode()) {//（失败）
			saasTradeService.modifyTradeStateFailed(tradeWithdrawal.getBusinessTradeNo());
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30506,"提现失败,锁定失败!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.LOCK_WAIT.getCode()) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30506,"提现失败,锁定等待!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.GATEWAY_FAIL.getCode()) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30510,"提现失败,网关调用失败!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.FAIL1.getCode()) {//（失败）
			saasTradeService.modifyTradeStateFailed(tradeWithdrawal.getBusinessTradeNo());
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30506,"提现失败!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.FAIL2.getCode()) {
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30506,"提现失败,提现记录已存在!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.PASSWORD_FAIL.getCode()) {//（失败）
			saasTradeService.modifyTradeStateFailed(tradeWithdrawal.getBusinessTradeNo());
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30507,"提现失败,密码错误!");
		}else if(response2.getCode() == com.cy.top56.common.Response.CodeTable.FREEZE1.getCode()) {//（失败）
			saasTradeService.modifyTradeStateFailed(tradeWithdrawal.getBusinessTradeNo());
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30508,"提现失败,帐户冻结!");
		}
		return null;
	}
}

