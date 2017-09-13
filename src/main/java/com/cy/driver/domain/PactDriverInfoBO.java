package com.cy.driver.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 合同客户信息
 * @author yanst 2016/4/25 19:54
 */
public class PactDriverInfoBO implements Serializable {
    private static final long serialVersionUID = 7024850415216631534L;

    /** 合同客户ID */
    private Long pactDriverId;

    /** 认证状态 */
    private byte authState;

    /** 公司名称 */
    private String companyName;

    /** 企业id */
    private Long companyId;

    /** 合同开始时间 */
    private String pactStartTime;

    /** 状态（0待同意1同意-1不同意） */
    private Integer pactStart;

    private List<PactLineBO> listData;

    public Long getPactDriverId() {
        return pactDriverId;
    }

    public void setPactDriverId(Long pactDriverId) {
        this.pactDriverId = pactDriverId;
    }

    public byte getAuthState() {
        return authState;
    }

    public void setAuthState(byte authState) {
        this.authState = authState;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getPactStartTime() {
        return pactStartTime;
    }

    public void setPactStartTime(String pactStartTime) {
        this.pactStartTime = pactStartTime;
    }

    public Integer getPactStart() {
        return pactStart;
    }

    public void setPactStart(Integer pactStart) {
        this.pactStart = pactStart;
    }

    public List<PactLineBO> getListData() {
        return listData;
    }

    public void setListData(List<PactLineBO> listData) {
        this.listData = listData;
    }
}
