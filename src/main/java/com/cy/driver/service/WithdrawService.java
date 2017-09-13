/**
 * Project Name:ads3
 * File Name:WithdrawService.java
 * Package Name:com.cy.driver.service
 * Date:2015年9月11日下午3:18:17
 * Copyright (c) 2015, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.cy.driver.service;

import com.cy.platformpay.service.dto.base.Response;

import java.math.BigDecimal;

/**
 * ClassName:WithdrawService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年9月11日 下午3:18:17 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface WithdrawService {

	/**
	 *@param  driverId 司机ID
	 * @param accountId  资金账户ID
	 * @param bindBankId  提现银行卡
	 * @param fee 金额
	 * @param remark   备注
	 * @param cashPwd 提现密码
	 * @return
	 */
	public Response<Long> withdrawCash(Long driverId, Long accountId, Long bindBankId, BigDecimal fee, String remark, String cashPwd);

}

