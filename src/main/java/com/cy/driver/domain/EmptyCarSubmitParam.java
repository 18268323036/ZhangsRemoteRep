package com.cy.driver.domain;


import com.google.gson.internal.LinkedTreeMap;
import java.io.Serializable;
import java.util.List;

/**
 * 添加空车上报
 * @author yanst 2016/4/21 14:30
 */
public class EmptyCarSubmitParam implements Serializable{
    private static final long serialVersionUID = -3912236435978554854L;

    /** 起始时间 */
    private String sTime;

    /** 截止时间 */
    private String eTime;

    /** 起始省code */
    private String sProCode;

    /** 起始省 */
    private String sProValue;

    /** 起始市code */
    private String sCityCode;

    /** 起始市 */
    private String sCityValue;

    /** 起始区code */
    private String sCountyCode;

    /** 起始区 */
    private String sCountyValue;

    /** 截止城市Json字符串 */
    private String endCityJsonStr;

    private List<LinkedTreeMap> endCityList;

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String getsProCode() {
        return sProCode;
    }

    public void setsProCode(String sProCode) {
        this.sProCode = sProCode;
    }

    public String getsProValue() {
        return sProValue;
    }

    public void setsProValue(String sProValue) {
        this.sProValue = sProValue;
    }

    public String getsCityCode() {
        return sCityCode;
    }

    public void setsCityCode(String sCityCode) {
        this.sCityCode = sCityCode;
    }

    public String getsCityValue() {
        return sCityValue;
    }

    public void setsCityValue(String sCityValue) {
        this.sCityValue = sCityValue;
    }

    public String getsCountyCode() {
        return sCountyCode;
    }

    public void setsCountyCode(String sCountyCode) {
        this.sCountyCode = sCountyCode;
    }

    public String getsCountyValue() {
        return sCountyValue;
    }

    public void setsCountyValue(String sCountyValue) {
        this.sCountyValue = sCountyValue;
    }

    public String getEndCityJsonStr() {
        return endCityJsonStr;
    }

    public void setEndCityJsonStr(String endCityJsonStr) {
        this.endCityJsonStr = endCityJsonStr;
    }

    public List<LinkedTreeMap> getEndCityList() {
        return endCityList;
    }

    public void setEndCityList(List<LinkedTreeMap> endCityList) {
        this.endCityList = endCityList;
    }


}
