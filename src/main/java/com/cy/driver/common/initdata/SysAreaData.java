package com.cy.driver.common.initdata;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/24.
 * 区域系统数据
 */
public class SysAreaData implements Serializable{
    private static final long serialVersionUID = 7233206404763912271L;

    /**  */
    private Integer id;

    /** 0 大区 1 省 2 市 3 县/区 */
    private Integer areaLevel;

    /** 区域名称 */
    private String areaName;

    /** 区域代码 */
    private String areaCode;

    /** 父级区域代码，大区级的该值为00000000 */
    private String parentCode;

    /** 城市名称的全拼 */
    private String namePinYin;

    /** 城市名称的首字母 */
    private String nameInitial;

    /** 是否热门城市（1：是，0否） */
    private Boolean hostCity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getNamePinYin() {
        return namePinYin;
    }

    public void setNamePinYin(String namePinYin) {
        this.namePinYin = namePinYin;
    }

    public String getNameInitial() {
        return nameInitial;
    }

    public void setNameInitial(String nameInitial) {
        this.nameInitial = nameInitial;
    }

    public Boolean getHostCity() {
        return hostCity;
    }

    public void setHostCity(Boolean hostCity) {
        this.hostCity = hostCity;
    }
}
