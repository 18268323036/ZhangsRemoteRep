package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.ConfigData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.InitDataUtil;
import com.cy.driver.domain.BankInfoBO;
import com.cy.driver.service.InitDataService;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.init.Tree;
import com.cy.platformpay.service.dto.BankCardbinDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller("initDataAction")
public class InitDataAction extends BaseAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private InitDataService initDataService;
	@Resource
	private ConfigData configData;

	/**
	 * 初始化数据:支付业务行为
	 * @return
	 * zdy 20151127
	 */
	@RequestMapping(value = "/getBusinessKindType", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_BUSINESS_KIND_TYPE)
	@Log(type = LogEnum.GET_INITIALIZATION_DATA)
	public Object getBusinessKindType(HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("businessKindTypes", InitDataUtil.businessKindTypeData());
		updRespHeadSuccess(response);
		return resultMap;
	}

	/**
	 * 初始化数据
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getInitializationData", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_INITIALIZATION_DATA)
	@Log(type = LogEnum.GET_INITIALIZATION_DATA)
	public Object getInitializationData(HttpServletResponse response, String areaTableFlag) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();

			if(StringUtils.isNotBlank(areaTableFlag)){
				if(configData.getAreaTable().equals(areaTableFlag)){
					resultMap.put("areaTableFlag", areaTableFlag);
					resultMap.put("carTypes", null);
					resultMap.put("carrriageTypes", null);
					resultMap.put("areaCode", null);
					//存入车长
					resultMap.put("carLength", null);
					return resultMap;
				}
			}

			Response<Tree> resulttree = initDataService.getInitializationDataTree();
			Response<Map<String, Object>> resultCarInitData = initDataService.getInitializationDataCarBasicData();
			if (resulttree.isSuccess() && resultCarInitData.isSuccess()) {
				resultMap.put("carTypes", resultCarInitData.getData().get("carTypes"));
				resultMap.put("carrriageTypes",resultCarInitData.getData().get("carrriageTypes"));
				resultMap.put("areaCode", resulttree.getData());
				//存入车长
				resultMap.put("carLength", InitDataUtil.carLengthData());
				resultMap.put("areaTableFlag", configData.getAreaTable());
				updRespHeadSuccess(response);
				return resultMap;
			}
			// 参数不完整
			if (resulttree.getCode() == CodeTable.EXCEPTION.getCode() || resultCarInitData.getCode() == CodeTable.EXCEPTION.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("	初始化数据(服务端)==>异常");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}

		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("	初始化数据出错 - " + e.getMessage());
			}
			e.printStackTrace();
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	/**
	 * 初始主要银行码表数据接口
	 * @param bankTableFlag
	 * @return
	 */
	@RequestMapping(value = "/getBankTable", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_BANK_TABLE)
	@Log(type = LogEnum.GET_INITIALIZATION_DATA)
	public Object getBankTable(HttpServletResponse response, String bankTableFlag) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String bankTableVersion = initDataService.getBankTableVersion();
		if(StringUtils.isNotBlank(bankTableFlag)){
			if(bankTableVersion.equals(bankTableFlag)){
				resultMap.put("bankList", null);
				resultMap.put("bankTableFlag", bankTableFlag);
				return resultMap;
			}
		}

		com.cy.platformpay.service.dto.base.Response<List<BankCardbinDTO>> resultData = initDataService.queryCardbinList();
		if(resultData.isSuccess()){
			List<BankInfoBO> bankBOList = convertBank(resultData.getData());
			resultMap.put("bankList", bankBOList);
			resultMap.put("bankTableFlag", bankTableVersion);
			updRespHeadSuccess(response);
			return resultMap;
		}
		if (resultData == null || !resultData.isSuccess()) {
			if (logger.isErrorEnabled()) {
				logger.error("初始化地区失败,调用pass服务[getInitializationDataTree]返回消息" + (resultData != null ? resultData.getMessage():""));
			}
			updRespHeadError(response);
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20226);
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	public List<BankInfoBO> convertBank(List<BankCardbinDTO> list){
		List<BankInfoBO> bankBOList = new ArrayList<BankInfoBO>();
		if(list != null && list.size() > 0) {
			for (BankCardbinDTO bankDTO : list) {
				BankInfoBO bankBO = new BankInfoBO();
				bankBO.setBankCode(bankDTO.getWyhlBankCode());
				bankBO.setBankName(bankDTO.getBankName());
				bankBO.setCardnumPrefix(bankDTO.getCardnumPrefix());
				bankBO.setImg(bankDTO.getIconImgPath());
				bankBOList.add(bankBO);
			}
		}
		return bankBOList;
	}

}
