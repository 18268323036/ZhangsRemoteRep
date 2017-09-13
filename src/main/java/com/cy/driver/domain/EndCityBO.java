package com.cy.driver.domain;

/**
 * @author yanst 2016/4/25 18:18
 */
public class EndCityBO {

    /** 截止省code */
    private String eProCode;

    /** 截止省 */
    private String eProValue;

    /** 截止市code */
    private String eCityCode;

    /** 截止市 */
    private String eCityValue;

    /** 截止区code */
    private String eCountyCode;

    /** 截止区 */
    private String eCountyValue;

    public String geteProCode() {
        return eProCode;
    }

    public void seteProCode(String eProCode) {
        this.eProCode = eProCode;
    }

    public String geteProValue() {
        return eProValue;
    }

    public void seteProValue(String eProValue) {
        this.eProValue = eProValue;
    }

    public String geteCityCode() {
        return eCityCode;
    }

    public void seteCityCode(String eCityCode) {
        this.eCityCode = eCityCode;
    }

    public String geteCityValue() {
        return eCityValue;
    }

    public void seteCityValue(String eCityValue) {
        this.eCityValue = eCityValue;
    }

    public String geteCountyValue() {
        return eCountyValue;
    }

    public void seteCountyValue(String eCountyValue) {
        this.eCountyValue = eCountyValue;
    }

    public String geteCountyCode() {
        return eCountyCode;
    }

    public void seteCountyCode(String eCountyCode) {
        this.eCountyCode = eCountyCode;
    }
}
