package com.cy.driver.domain.saasPay;

/**
 * Created by nixianjing on 17/8/18.
 */
public class EnumInfo {

    private String key;

    private String value;

    public EnumInfo (String key,String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
