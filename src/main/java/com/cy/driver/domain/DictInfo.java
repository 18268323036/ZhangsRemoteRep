package com.cy.driver.domain;

import java.io.Serializable;

/**
 * 数据字典对象
 */
public class DictInfo implements Serializable {

    private static final long serialVersionUID = 3312190358905755378L;

    private String code;
    private String value;
    private Integer type; //类型  1 货主 2 分包商
    private String parentCode;

    public DictInfo() {
        super();
    }

    public DictInfo(String code, String value) {
        super();
        this.code = code;
        this.value = value;
    }

    public DictInfo(String code, String value, Integer type) {
        this.code = code;
        this.value = value;
        this.type = type;
    }

    public DictInfo(String code, String value, String parentCode) {
        super();
        this.code = code;
        this.value = value;
        this.parentCode = parentCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentCode() {
        return parentCode;
    }


    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

}
