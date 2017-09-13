package com.cy.driver.common.initdata;

import java.io.Serializable;

/**
 * Created by wyh on 2015/7/23.
 */
public class SystemTreeCode implements Serializable {
    private static final long serialVersionUID = -8056936370707453096L;
    private static final String maxParentCode = "0";

    private Long id;//
    private String code;//编号
    private String name;//编号名称
    private String parentCode;//父节点编号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * 是否还存在父节点
     * @return false无父节点，true有父节点
     */
    public boolean haveParent(){
        if(maxParentCode.equals(this.parentCode)){
            return false;
        }else{
            return true;
        }
    }
}
