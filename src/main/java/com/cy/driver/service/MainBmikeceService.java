/**
 * com.cy.driver.service.MainBmikeceService.java
 * Copyright: Copyright 2013 56top.cn.Inc. ALL rights reserved 
 * @author zhaox 2015年9月10日 下午3:43:19 v1.0
 */
package com.cy.driver.service;

import com.cy.driver.domain.ConsumeClearingInfoBO;
import com.cy.driver.domain.PageBase;
import com.cy.order.service.dto.ConsumeMonthSumDTO;
import com.cy.order.service.dto.OilCardConsumeDetailDTO;
import com.cy.platformpay.service.dto.AccountBillDTO;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import com.cy.platformpay.service.dto.base.PageResult;

import java.util.List;

/**
 * @ClassName: MainBmikeceService
 * @Description:
 * @author zhaox  2015年9月10日 下午3:43:19
 */
public interface MainBmikeceService {


	/**
	 * 本月账单
	 * @param page 页码
	 * @param    capitalAccountId 资金账户ID
	 * @return
	 */
	PageBase<AccountBillDTO> thisMonthBill(Integer page, Long capitalAccountId, Integer businessKind);

	/**
	 * <p>Title: queryMainBmikeceList</p>
	 * <p>Description: </p>
	 * @param page 页码
	 * @param    capitalAccountId 资金账户ID
	 * @return
	 * @author zhaox 2015年9月10日 下午3:50:37
	 */
	public PageResult<AccountBillDTO> queryMainBmikeceList(Integer page, Long capitalAccountId, Integer businessKind, String queryStartTime, String queryEndTime);

	/**
	 * 
	 * @Title: queryAccountInfo
	 * @Description:查询帐户信息 
	 * @param accountId
	 * @return
	 * @author zhaox 2015年9月10日下午4:28:21
	 */
	public com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO> queryAccountInfo(Long accountId);


	/**
	 * 油卡账单明细
	 * @param page
	 * @param queryStartDate
	 * @param queryEndDate
	 * @param oilCardId
     * @param userId
     * @return
     */
	com.cy.order.service.dto.base.PageResult<OilCardConsumeDetailDTO> queryOilCardAccountDetailByDriver(Long page, String queryStartDate, String queryEndDate, String oilCardId, Long userId);


	/**
	 * 油卡账单清算列表
	 * @param userId
	 * @return
     */
	List<ConsumeMonthSumDTO> consumeMonthSum(Long userId);

	/**
	 * 账单清算列表
	 * @param capitalAccountId
	 * @return
	 */
	List<ConsumeClearingInfoBO> accountClearingList(Long capitalAccountId);


	/**
	 * 根据资金账号ID获取账号密码
	 * @param capitalAccountId
	 * @return
     */
	String getPayPwd(Long capitalAccountId);
}
