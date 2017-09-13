package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/12.
 */
public class HomeInfoBO implements Serializable {
    private static final long serialVersionUID = 7558493430607060450L;

    private String showType;//是否显示添加车辆(0显示、1不显示)

    private String carCard;//车牌号码

    private String authStatus;//认证状态

    private String photosAddress;//头像地址

    private String workStatus;//接单/拒接状态(0 拒接、1接单)

    private String orderCounts;//已完成订单数量

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getCarCard() {
        return carCard;
    }

    public void setCarCard(String carCard) {
        this.carCard = carCard;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getPhotosAddress() {
        return photosAddress;
    }

    public void setPhotosAddress(String photosAddress) {
        this.photosAddress = photosAddress;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getOrderCounts() {
        return orderCounts;
    }

    public void setOrderCounts(String orderCounts) {
        this.orderCounts = orderCounts;
    }
}
