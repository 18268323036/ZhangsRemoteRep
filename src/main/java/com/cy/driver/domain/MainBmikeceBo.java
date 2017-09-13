/**
 * com.cy.driver.domain.MainBmikece.java
 * Copyright: Copyright 2013 56top.cn.Inc. ALL rights reserved 
 * @author zhaox 2015年9月10日 下午3:40:04 v1.0
 */
package com.cy.driver.domain;

/**
 * @ClassName: MainBmikece
 * @Description: TODO
 * @author zhaox  2015年9月10日 下午3:40:04
 */
public class MainBmikeceBo {
	
	/**
	 * 账单名称种类（提现和收款）
	 */
	private Integer billTypeName;
	
	/**
	 * 交易时间
	 */
	private String tradingHours;
	
	/**
	 * 收入（0） 支出(1)
	 */
	private Byte trading;
	
	/**
	 * 交易金额
	 */
	private String transaction;


	/**
	 * @return the tradingHours
	 */
	public String getTradingHours() {
		return tradingHours;
	}

	/**
	 * @param tradingHours the tradingHours to set
	 */
	public void setTradingHours(String tradingHours) {
		this.tradingHours = tradingHours;
	}


	/**
	 * @return the billTypeName
	 */
	public Integer getBillTypeName() {
		return billTypeName;
	}

	/**
	 * @param billTypeName the billTypeName to set
	 */
	public void setBillTypeName(Integer billTypeName) {
		this.billTypeName = billTypeName;
	}

	/**
	 * @return the trading
	 */
	public Byte getTrading() {
		return trading;
	}

	/**
	 * @param trading the trading to set
	 */
	public void setTrading(Byte trading) {
		this.trading = trading;
	}

	/**
	 * @return the transaction
	 */
	public String getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	
	
	
}
