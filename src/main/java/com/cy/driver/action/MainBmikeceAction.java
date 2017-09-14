/**
 * com.cy.driver.action.MainBmikeceAction.java
 * Copyright: Copyright 2013 56top.cn.Inc. ALL rights reserved 
 * @author zhaox 2015年9月10日 下午3:24:23 v1.0
 */
package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.*;
import com.cy.driver.service.MainBmikeceService;
import com.cy.order.service.dto.ConsumeMonthSumDTO;
import com.cy.order.service.dto.OilCardConsumeDetailDTO;
import com.cy.platformpay.service.dto.AccountBillDTO;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import com.cy.platformpay.service.dto.base.PageResult;
import com.cy.platformpay.service.dto.base.Response;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MainBmikeceAction
 * @author zhaox 2015年9月10日 下午3:24:23
 */
@Scope("prototype")
@RestController
public class MainBmikeceAction extends BaseAction{

	private Logger LOG = LoggerFactory.getLogger(MainBmikeceAction.class);

	@Resource
	private MainBmikeceService mainBmikeceService;


	/**
	 * 本月账单明细
	 * @param response
	 * @param page 页码
	 * @param businessKind 允许为空，空当作查询全部
	 * @return
	 */
	@RequestMapping(value = "/thisMonthBill", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.THIS_MONTH_BILL)
	@Log(type = LogEnum.THIS_MONTH_BILL)
	public Object thisMonthBill(HttpServletResponse response, Integer page, Integer businessKind) {
		try{
			//不合法
			if (page.intValue() <= 0) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
			}
			PageBase<AccountBillDTO> resultData = mainBmikeceService.thisMonthBill(page, findcapitalAccountId(), businessKind);
			updRespHeadSuccess(response);
			return convertAccountBill2(resultData);
		}
		catch (Exception e){
			if (LOG.isDebugEnabled())
				LOG.error("账户余额列表出错。", e);
		}
		return findException(response);
	}

	public PageBase<AccountBillBO> convertAccountBill2(PageBase<AccountBillDTO> resultData){
		PageBase<AccountBillBO> pageBase = new PageBase<AccountBillBO>();
		pageBase.setTotalNum(resultData.getTotalNum());
		pageBase.setTotalPage(resultData.getTotalPage());
		List<AccountBillBO> accountBillBOList = new ArrayList<AccountBillBO>();
		if(resultData.getListData() != null){
			for (AccountBillDTO accountBillDTO : resultData.getListData())
			{
				AccountBillBO accountBillBO = new AccountBillBO();
				accountBillBO.setBillTypeName(SystemsUtil.payBusinessKind(accountBillDTO.getBusinessKind()));
				accountBillBO.setTradingHours(DateUtil.dateFormat(accountBillDTO.getCreateTime(), DateUtil.F_DATETOMIN));
				accountBillBO.setTrading(SystemsUtil.trading(accountBillDTO.getFundFlow()));
				accountBillBO.setTransaction(SystemsUtil.showAppMoney(accountBillDTO.getMoney()));
				accountBillBOList.add(accountBillBO);
			}
			pageBase.setListData(accountBillBOList);
		}
		return pageBase;
	}

	/**
	 * 账单明细
	 * @param response
	 * @param page 页码
	 * @param businessKind 允许为空，空当作查询全部
	 * @return
	 */
	@RequestMapping(value = "/bill", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.BILL)
	@Log(type = LogEnum.MAIN_BMIKECE)
	public Object bill(HttpServletResponse response, Integer page, Integer businessKind, String queryStartDate, String queryEndDate) {
		try{
			//不合法
			if (page.intValue() <= 0) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
			}
			PageResult<AccountBillDTO> resultData = mainBmikeceService.queryMainBmikeceList(page, findcapitalAccountId(), businessKind,queryStartDate,queryEndDate);
			updRespHeadSuccess(response);
			return convertAccountBill2(resultData);
		}
		catch (Exception e){
			if (LOG.isDebugEnabled())
				LOG.error("账户余额列表出错。", e);
		}
		return findException(response);
	}


	/**
	 * 油卡账单明细
	 * @param page 页码
	 * @param businessKind 允许为空，空当作查询全部
	 * @return
	 */
	@RequestMapping(value = "/oilCardAccountList", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.OILCARD_BILL)
	@Log(type = LogEnum.OILCARD_BILL)
	public Object oilCardAccountList(Integer page, Integer businessKind, String queryStartDate,String queryEndDate, String oilCardId) {
		LOG.isDebugEnabled(); LOG.debug("油卡账单明细入参,page={},businessKind={},queryStartDate={},queryEndDate{},oilCardId{}.businessType{}",page,businessKind,queryStartDate,queryEndDate,oilCardId);
		if(page==null){
			updRespHeadError(response);
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
		}
		com.cy.order.service.dto.base.PageResult<OilCardConsumeDetailDTO> pageResult = mainBmikeceService.queryOilCardAccountDetailByDriver(Long.valueOf(page), queryStartDate, queryEndDate, oilCardId,findUserId());
		PageBase<OilCardConsumeDetailBO> pageBase = oilCardAccountDetailConvert(pageResult);
		updRespHeadSuccess(response);
		return pageBase;
	}


	@RequestMapping(value = "/accountListByMonth", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.OILCARD_CLEARING_BILL)
	@Log(type = LogEnum.OILCARD_CLEARING_BILL)
	public Object accountListByMonth(Integer accountType){
		if(LOG.isDebugEnabled()) LOG.debug("油卡账单清算请求参数,accountType={}",accountType);
		if(accountType==null || accountType<=0){
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
		}
		Map<String, Object> map = new HashMap<>();
		if(accountType==1){
			List<ConsumeClearingInfoBO> consumeClearingInfoBOs = mainBmikeceService.accountClearingList(findcapitalAccountId());
			map.put("clearingByMonth", consumeClearingInfoBOs);
		}else {
			List<ConsumeMonthSumDTO> consumeMonthSumDTOs = mainBmikeceService.consumeMonthSum(findUserId());
			map.put("clearingByMonth", clearingInfoListConvert(consumeMonthSumDTOs));
		}
		updRespHeadSuccess(response);
		return map;
	}


	private List<ConsumeClearingInfoBO> clearingInfoListConvert(List<ConsumeMonthSumDTO> consumeMonthSumDTOs){
		List<ConsumeClearingInfoBO> consumeClearingInfos = new ArrayList<>();
		for(ConsumeMonthSumDTO consumeMonthSumDTO : consumeMonthSumDTOs){
			ConsumeClearingInfoBO consumeClearingInfoBO = new ConsumeClearingInfoBO();
			consumeClearingInfoBO.setTime(consumeMonthSumDTO.getYearMothStr());
			consumeClearingInfoBO.setTradeMoney(SystemsUtil.getPlusNumber(consumeMonthSumDTO.getSumMoney()));
			consumeClearingInfoBO.setTimeName(consumeMonthSumDTO.getYearMothStr());
			if(consumeMonthSumDTO.getSumMoney()!=null) {
				if (consumeMonthSumDTO.getSumMoney().intValue() >= 0) {
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

	public PageBase<AccountBillBO> convertAccountBill2(PageResult<AccountBillDTO> resultData){
		PageBase<AccountBillBO> pageBase = new PageBase<AccountBillBO>();
		pageBase.setTotalNum(resultData.getTotalRecord());
		pageBase.setTotalPage(resultData.getTotalPage());
		List<AccountBillBO> accountBillBOList = new ArrayList<AccountBillBO>();
		if(resultData.getDataList()!=null){
			for (AccountBillDTO accountBillDTO : resultData.getDataList())
			{
				AccountBillBO accountBillBO = new AccountBillBO();
				accountBillBO.setBillTypeName(SystemsUtil.payBusinessKind(accountBillDTO.getBusinessKind()));
				accountBillBO.setTradingHours(DateUtil.dateFormat(accountBillDTO.getCreateTime(), DateUtil.F_DATETOMIN));
				accountBillBO.setTrading(SystemsUtil.trading(accountBillDTO.getFundFlow()));
				accountBillBO.setTransaction(SystemsUtil.showAppMoney(accountBillDTO.getMoney()));
				accountBillBOList.add(accountBillBO);
			}
			pageBase.setListData(accountBillBOList);
		}
		return pageBase;
	}



	/**
	 * 账户余额
	 * @param request
	 * @param response
	 * @param page 页码
	 * @param businessKind     * 允许为空，空当作查询全部
	 * 业务行为:
	 * 1000---5000 收入（2000 以下属于系统结算行为）
	 * 5000---10000 支出（6000 以下属于系统结算行为）
	 * 1000 提现失败资金退回
	 * 2000 充值
	 * 2001 收到运费
	 * 6000 提现
	 * 6001 支付运费
	 * 6002 支付服务费
	 * @return
	 */
	@RequestMapping(value = "/mainBmikece", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_BMIKECE)
	@Log(type = LogEnum.MAIN_BMIKECE)
	public Object mainBmikece(HttpServletRequest request, HttpServletResponse response, Integer page, Integer businessKind) {
		try{
			//不合法
			if (page.intValue() <= 0) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
			}
			PageResult<AccountBillDTO> resultData = mainBmikeceService.queryMainBmikeceList(page, findcapitalAccountId(request), businessKind,null,null);

			Response<CapitalAccountDTO> resultTotalMoney = mainBmikeceService.queryAccountInfo(findcapitalAccountId(request));
			String totalMoney1 = "0.00";
			BigDecimal luckyMoney = new BigDecimal("0.00");
			BigDecimal totalMoney = new BigDecimal("0.00");
			if(resultTotalMoney.isSuccess())
			{
				if(resultTotalMoney.getData() != null){

					luckyMoney = resultTotalMoney.getData().getLuckyMoney() == null ?luckyMoney:resultTotalMoney.getData().getLuckyMoney();
					totalMoney = resultTotalMoney.getData().getTotalMoney() == null ?totalMoney:resultTotalMoney.getData().getTotalMoney();
					totalMoney1 = totalMoney.add(luckyMoney)+"";
				}
			}
			updRespHeadSuccess(response);
			return convertAccountBill(resultData, totalMoney1);
		}
		catch (Exception e){
			if (LOG.isDebugEnabled())
			LOG.error("账户余额列表出错。", e);
		}
		return findException(response);
	}


	public AccountBillListBO convertAccountBill(PageResult<AccountBillDTO> resultData, String totalMoney ){
		AccountBillListBO accountBillListBO = new AccountBillListBO();
		accountBillListBO.setAllNUms(resultData.getTotalRecord());
		accountBillListBO.setAllPage(resultData.getTotalPage());
		List<AccountBillBO> accountBillBOList = new ArrayList<AccountBillBO>();
		if(resultData.getDataList()!=null){
		    for (AccountBillDTO accountBillDTO : resultData.getDataList())
			{
				AccountBillBO accountBillBO = new AccountBillBO();
				accountBillBO.setBillTypeName(SystemsUtil.payBusinessKind(accountBillDTO.getBusinessKind()));
				accountBillBO.setTradingHours(DateUtil.dateFormat(accountBillDTO.getCreateTime(), DateUtil.F_DATETOMIN));
				accountBillBO.setTrading(SystemsUtil.trading(accountBillDTO.getFundFlow()));
				accountBillBO.setTransaction(SystemsUtil.showAppMoney(accountBillDTO.getMoney()));
				accountBillBOList.add(accountBillBO);
			}
			accountBillListBO.setAccountBillBOList(accountBillBOList);
		}
		accountBillListBO.setMainBmikece(totalMoney);
		return accountBillListBO;
	}

	private PageBase<OilCardConsumeDetailBO> oilCardAccountDetailConvert(com.cy.order.service.dto.base.PageResult<OilCardConsumeDetailDTO> pageResult){
		PageBase<OilCardConsumeDetailBO> pageBase = new PageBase<>();
		pageBase.setTotalNum(pageResult.getTotalRecord());
		pageBase.setTotalPage(pageResult.getTotalPage());
		List<OilCardConsumeDetailBO> oilCardConsumeDetailBOs = new ArrayList<>();
		if(pageResult.getDataList()!=null) {
			for (OilCardConsumeDetailDTO oilCardConsumeDetailDTO : pageResult.getDataList()) {
				OilCardConsumeDetailBO oilCardConsumeDetailBO = new OilCardConsumeDetailBO();
				oilCardConsumeDetailBO.setBusinessType(String.valueOf(oilCardConsumeDetailDTO.getBusinessKind()));
				oilCardConsumeDetailBO.setBusinessTypeName(oilCardBusinessTypeNameConvert(oilCardConsumeDetailDTO.getBusinessKind()));
				oilCardConsumeDetailBO.setOilCardId(oilCardConsumeDetailDTO.getOilChildCode());
				oilCardConsumeDetailBO.setTradeMoney(SystemsUtil.getPlusNumber(oilCardConsumeDetailDTO.getMoney()));
				oilCardConsumeDetailBO.setTradeType(getTradeType(oilCardConsumeDetailDTO.getFundFlow()));
				oilCardConsumeDetailBO.setTradeTime(DateUtil.dateFormat(oilCardConsumeDetailDTO.getCreateTime(), DateUtil.F_DATETOMIN));
				oilCardConsumeDetailBOs.add(oilCardConsumeDetailBO);
			}
			pageBase.setListData(oilCardConsumeDetailBOs);
			return pageBase;
		}
		return pageBase;
	}



	private String oilCardBusinessTypeNameConvert(Integer businessKind){
		if(businessKind==1001){
			return "油卡充值";
		}else if(businessKind==1002){
			return "运费收入";
		}else if(businessKind==2001){
			return "支付运费";
		}
		return null;
	}

	private String getTradeType(byte tradeType){
		if(tradeType==2){
			return "0";
		}else if(tradeType==1){
			return "1";
		}
		return null;
	}

}
