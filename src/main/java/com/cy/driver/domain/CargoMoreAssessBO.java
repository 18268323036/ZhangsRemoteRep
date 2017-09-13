package com.cy.driver.domain;

import com.cy.driver.common.util.ValidateUtil;

import java.io.Serializable;


public class CargoMoreAssessBO implements Serializable{
	
	  private static final long serialVersionUID = -392193140066428845L;
	  
	    /** 主键（自增、初始值1） */
	    private Long assessId;
	    
	    /** 0：货已走，1：货还在 */
	    private Byte type;

	    /** 点评内容（备注） */
	    private String assessInfo;

	    /** 点评时间 */
	    private String createTime;

		/**  手机号码  */
		private String mobilePhone;

		/** 认证状态  */
		private String authStatus;

	public Long getAssessId() {
		return assessId;
	}

	public void setAssessId(Long assessId) {
		this.assessId = assessId;
	}

	public Byte getType() {
			return type;
		}

		public void setType(Byte type) {
			this.type = type;
		}

		public String getAssessInfo() {
			return assessInfo;
		}

		public void setAssessInfo(String assessInfo) {
			this.assessInfo = assessInfo;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = ValidateUtil.hideMobile(mobilePhone);
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
}
