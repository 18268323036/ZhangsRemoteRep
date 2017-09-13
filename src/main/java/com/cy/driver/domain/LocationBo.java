package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by wyh on 2015/7/28.
 */
public class LocationBo implements Serializable {
    private static final long serialVersionUID = 7901754472261435019L;

    private String longitude;//经度
    private String latitude;//纬度
    private String province;//省
    private String city;//市
    private String county;//县
    private String town;//镇
    private String address;//详细地址
    private String siteSource;//来源 1快到网 2云配送

    /**
     * 类型
     * (0默认、1登录时、2登录状态下,客户端启动数据初始化时、3搜索货源时/附近货源、
     * 4空车上报时、11确认装货、12确认卸货、13取消订单、14问题上报请求提交时、15确定承运)
     */
    private Integer type;

    /** 业务id（例如：订单id） */
    private Long businessId;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getSiteSource() {
        return siteSource;
    }

    public void setSiteSource(String siteSource) {
        this.siteSource = siteSource;
    }
}
