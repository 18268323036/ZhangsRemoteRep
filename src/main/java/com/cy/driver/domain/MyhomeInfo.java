package com.cy.driver.domain;

import java.io.Serializable;


public class MyhomeInfo implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -5029255496095686881L;

	
	private String photosAddress;//个人头像md5
	
	private String authStatus;//认证状态
	
	private String carCard;//车牌号
	
	private String userName;//用户姓名
	
	private String registerTime;//注册时间

	private String emptyCarReportNumber;//空车上报
	
	private String oftenRunCityNums;//常跑城市

	public String getPhotosAddress() {
		return photosAddress;
	}

	public void setPhotosAddress(String photosAddress) {
		this.photosAddress = photosAddress;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getCarCard() {
		return carCard;
	}

	public void setCarCard(String carCard) {
		this.carCard = carCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}


	public String getEmptyCarReportNumber() {
		return emptyCarReportNumber;
	}

	public void setEmptyCarReportNumber(String emptyCarReportNumber) {
		this.emptyCarReportNumber = emptyCarReportNumber;
	}

	public String getOftenRunCityNums() {
		return oftenRunCityNums;
	}

	public void setOftenRunCityNums(String oftenRunCityNums) {
		this.oftenRunCityNums = oftenRunCityNums;
	}
	
}
