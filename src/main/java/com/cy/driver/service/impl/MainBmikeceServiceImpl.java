/**
 * com.cy.driver.service.impl.MainBmikeceServiceImpl.java
 * Copyright: Copyright 2013 56top.cn.Inc. ALL rights reserved 
 * @author zhaox 2015年9月10日 下午3:50:37 v1.0
 */
package com.cy.driver.service.impl;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.ConsumeClearingInfoBO;
import com.cy.driver.domain.PageBase;
import com.cy.driver.service.MainBmikeceService;
import com.cy.order.service.OilCardService;
import com.cy.order.service.dto.ConsumeMonthSumDTO;
import com.cy.order.service.dto.OilCardConsumeDetailDTO;
import com.cy.order.service.dto.OilCardConsumeDetailManageParamDTO;
import com.cy.order.service.dto.base.Response;
import com.cy.platformpay.service.AccountBillService;
import com.cy.platformpay.service.CapitalAccountService;
import com.cy.platformpay.service.dto.AccountBillDTO;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import com.cy.platformpay.service.dto.QueryBillDTO;
import com.cy.platformpay.service.dto.base.PageInfo;
import com.cy.platformpay.service.dto.base.PageResult;
import com.cy.platformpay.service.dto.bill.request.QueryFlowCountDTO;
import com.cy.platformpay.service.dto.bill.response.StatisticDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MainBmikeceServiceImpl
 * @Description: TODO
 * @author zhaox  2015年9月10日 下午3:50:37
 */
@Service("mainBmikeceService")
public class MainBmikeceServiceImpl implements MainBmikeceService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private AccountBillService accountBillService;
	
	@Resource
	private CapitalAccountService capitalAccountService;

	@Resource
	private OilCardService oilCardService;

	/**
	 * 本月账单
	 * @param page 页码
	 * @param    capitalAccountId 资金账户ID
	 * @return
	 */
	@Override
	public PageBase<AccountBillDTO> thisMonthBill(Integer page, Long capitalAccountId, Integer businessKind) {
		PageInfo<QueryBillDTO> pageInfo = new PageInfo<QueryBillDTO>(page, Constants.ACCOUNT_SIZE);
		QueryBillDTO queryBillDTO = new QueryBillDTO();
		queryBillDTO.setCapitalAccountId(capitalAccountId);
		queryBillDTO.setBusinessKind(SystemsUtil.buildQueryAccountDetail(businessKind));
		queryBillDTO.setQueryStartDate(DateUtil.getThisMonth());
		pageInfo.setData(queryBillDTO);
		PageResult<AccountBillDTO> result = accountBillService.pageBill(pageInfo);
		if(result == null){
			if(log.isErrorEnabled()){
				log.error("调用底层接口获取账单明细失败，对象信息为空。");
			}
			return null;
		}

		if(!result.isSuccess()){
			if(log.isErrorEnabled()){
				log.error("调用底层接口获取账单明细出错，失败信息={}", result.getMessage());
			}
			return null;
		}

		PageBase<AccountBillDTO> pageBase = new PageBase<>();
		pageBase.setTotalNum(result.getTotalRecord());
		pageBase.setTotalPage(result.getTotalPage());
		pageBase.setListData(result.getDataList());
		return pageBase;
	}


	/**
	 * <p>Title: queryMainBmikeceList</p> 
	 * <p>Description: </p>
	 * @param page 页码
	 * @param    capitalAccountId 资金账户ID
	 * @return
	 * @author zhaox 2015年9月10日 下午3:50:37
	 */
	@Override
	public PageResult<AccountBillDTO> queryMainBmikeceList(Integer page, Long capitalAccountId, Integer businessKind,String queryStartTime,String queryEndTime) {
		PageInfo<QueryBillDTO> pageInfo = new PageInfo<QueryBillDTO>(page, Constants.ACCOUNT_SIZE);
		QueryBillDTO queryBillDTO = new QueryBillDTO();
		if(queryStartTime!=null) {
			queryBillDTO.setQueryStartDate(DateUtil.parseDate(queryStartTime, DateUtil.F_DATE));
		}
		if(queryEndTime!=null) {
			queryBillDTO.setQueryEndDate(DateUtil.parseDate(queryEndTime, DateUtil.F_DATE));
		}
		queryBillDTO.setCapitalAccountId(capitalAccountId);
		queryBillDTO.setBusinessKind(SystemsUtil.buildQueryAccountDetail(businessKind));
		pageInfo.setData(queryBillDTO);
		return accountBillService.pageBill(pageInfo);
	}

	/**
	 * <p>Title: queryAccountInfo</p> 
	 * <p>Description: 获取账户信息</p>
	 * @param accountId
	 * @return
	 * @see com.cy.driver.service.MainBmikeceService#queryAccountInfo(Long)
	 * @author zhaox 2015年9月10日 下午4:28:34
	*/
	@Override
	public com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO> queryAccountInfo(Long accountId) {
		try {
			com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO> resp =  capitalAccountService.getAccount(accountId);
			return resp;
		} catch (Exception e) {
			log.error("查询账户总余额异常,accountId:"+accountId,e);
			return new com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO>();
		}
	}

	@Override
	public com.cy.order.service.dto.base.PageResult<OilCardConsumeDetailDTO> queryOilCardAccountDetailByDriver(Long page, String queryStartDate, String queryEndDate, String oilCardId,Long userId) {
		com.cy.order.service.dto.base.PageInfo<OilCardConsumeDetailManageParamDTO> pageInfo = new com.cy.order.service.dto.base.PageInfo<>(page);
		OilCardConsumeDetailManageParamDTO oilCardConsumeDetailManageParamDTO = new OilCardConsumeDetailManageParamDTO();
		if(queryStartDate!=null) {
			oilCardConsumeDetailManageParamDTO.setBeginDate(DateUtil.parseDate(queryStartDate, DateUtil.F_DATE));
		}
		if(queryEndDate!=null) {
			oilCardConsumeDetailManageParamDTO.setEndDate(DateUtil.parseDate(queryEndDate, DateUtil.F_DATE));
		}
		oilCardConsumeDetailManageParamDTO.setUserId(userId);
		oilCardConsumeDetailManageParamDTO.setOilChildCode(oilCardId);
		oilCardConsumeDetailManageParamDTO.setUserType((byte)1);
		pageInfo.setData(oilCardConsumeDetailManageParamDTO);
		com.cy.order.service.dto.base.PageResult<OilCardConsumeDetailDTO> pageResult = oilCardService.pageOilCardConsumeDetailManage(pageInfo);
		if(pageResult==null){
			log.debug("主账号查询或分包商油卡账单明细失败");
		}
		if(!pageResult.isSuccess()){
			log.debug("主账号查询或分包商油卡账单明细失败,失败原因{}",pageResult.getMessage());
		}
		return pageResult;
	}


	@Override
	public List<ConsumeMonthSumDTO> consumeMonthSum(Long userId) {
		List<String> list = DateUtil.getTwelveMonth();
		List<ConsumeMonthSumDTO> consumeMonthSumDTOs = new ArrayList<>();
		for(int i=0;i<12;i++) {
			ConsumeMonthSumDTO consumeMonthSumDTO = new ConsumeMonthSumDTO();
			consumeMonthSumDTO.setUserId(userId);
			consumeMonthSumDTO.setUserType((byte)1);
			consumeMonthSumDTO.setYearMothStr(list.get(i));
			consumeMonthSumDTOs.add(consumeMonthSumDTO);
		}
		Response<List<ConsumeMonthSumDTO>> response = oilCardService.consumeMonthSum(consumeMonthSumDTOs);
		if(response==null){
			log.debug("查询油卡账单汇总失败");
			return null;
		}
		if(!response.isSuccess()){
			log.debug("查询油卡账单汇总失败,失败原因{}",response.getMessage());
			return null;
		}
		return response.getData();
	}

	@Override
	public List<ConsumeClearingInfoBO> accountClearingList(Long capitalAccountId) {
		List<String> list = DateUtil.getTwelveMonth();
		List<ConsumeClearingInfoBO> consumeClearingInfos = new ArrayList<>();
		for (String dateStr : list) {
			QueryFlowCountDTO queryFlowCountDTO = new QueryFlowCountDTO();
			ConsumeClearingInfoBO consumeClearingInfoBO = new ConsumeClearingInfoBO();
			queryFlowCountDTO.setCapitalAccountId(capitalAccountId);
			queryFlowCountDTO.setQueryStartDate(DateUtil.getFirstOfMonth(dateStr));
			queryFlowCountDTO.setQueryEndDate(DateUtil.getSecondMonthFirstDay(dateStr));
			com.cy.platformpay.service.dto.base.Response<StatisticDTO> response = accountBillService.getBillStatistics(queryFlowCountDTO);
			consumeClearingInfoBO.setTimeName(dateStr);
			consumeClearingInfoBO.setTime(dateStr);
			consumeClearingInfoBO.setTradeMoney(SystemsUtil.getPlusNumber(response.getData().getFlowCount()));
			if (response != null && response.getData() != null) {
				if (response.getData().getFlowCount().intValue() >= 0) {
					consumeClearingInfoBO.setTradeType("0");
				} else {
					consumeClearingInfoBO.setTradeType("1");
				}
			}
			consumeClearingInfos.add(consumeClearingInfoBO);
		}
		consumeClearingInfos.get(0).setTimeName(consumeClearingInfos.get(0).getTime()+"(本月)");
		return consumeClearingInfos;
	}

	@Override
	public String getPayPwd(Long capitalAccountId) {
		com.cy.platformpay.service.dto.base.Response<String> response = capitalAccountService.getPayPwd(capitalAccountId);
		if(response.isSuccess()) {
			return response.getData();
		}
		return null;
	}

	//	public MainBmikeceBo convert(AccountBillDTO dto){
//		MainBmikeceBo bmikece = new MainBmikeceBo();
//		bmikece.setBillTypeName(dto.getBusinessKind());
//		bmikece.setTradingHours(DateUtil.dateFormat(dto.getCreateTime(), DateUtil.F_DATETIME));
//		bmikece.setTrading(dto.getFundFlow());
//		bmikece.setTransaction(dto.getMoney().toString());
//		return bmikece;
//
//	}
}
