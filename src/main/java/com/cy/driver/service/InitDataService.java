package com.cy.driver.service;

import com.cy.driver.common.initdata.SysAreaData;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.init.Tree;
import com.cy.platformpay.service.dto.BankCardbinDTO;

import java.util.List;
import java.util.Map;


public interface InitDataService {

	/**
	 * 查询银行总行列表
	 * @return
	 */
	public com.cy.platformpay.service.dto.base.Response<List<BankCardbinDTO>> queryCardbinList();

	/**
	 * 根据区域等级获取 对应等级的所有信息
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public List<SysAreaData> getAreaTableDataLevel(int level) throws Exception;

	/**
	 * 获取初始化地区码表
	 * @return
	 */
	public Response<Tree> getInitializationDataTree();
	
	/**
	 * 获取初始化车辆信息
	 * @return
	 */
	public Response<Map<String, Object>> getInitializationDataCarBasicData();

	/**
	 * 获取车厢类型
	 * @param code
	 * @param codeTwo
	 * @return
	 */
	public String getCarrriageTypes(int code, int codeTwo);


	/**
	 * 获取银行卡BIN版本信息
	 */
	String getBankTableVersion();
}
