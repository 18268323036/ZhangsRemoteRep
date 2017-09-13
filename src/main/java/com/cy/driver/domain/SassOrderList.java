package com.cy.driver.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询分页对象
 * Created by nixianjing on 17/5/15.
 */
public class SassOrderList {

    /** 主键id */
    private Long orderId;

    /** 订单号 */
    private String orderNum;

    /**
     * 货物名称
     */
    public String carrierName;

        /** 合计件数 */
    private Integer totalQuantity;

    /** 合计重量 */
    private Double totalWeight;

    /** 合计体积 */
    private Double totalCubage;

    /** 合计件数单位名称 */
    private String totalQuantityValue;

    /** 合计重量单位名称 */
    private String totalWeightValue;

    /** 合计体积单位名称 */
    private String totalCubageValue;

    /** 订单类型 {@link com.cy.saas.business.model.enums.OrderType} */
    private Byte orderType;

    /** 抢单/竞价状态 {@link com.cy.saas.business.model.enums.OrderExecuteState} */
    private Byte executeState;

    /** 订单参与方id（t_order_part.id，无填写0） */
    private Long orderPartId;

    /** 有效期截至时间 */
    private Date validityEnd;

    /** 有效期截至时间 */
    private String validityEndValue;

    /** 报价状态（0-无新报价、1-有新报价） */
    private Byte quoteState;

    /** 运费总价 */
    private BigDecimal totalFare;

    /** 要求装货时间 */
    private Date needStartTime;

    /** 要求装货时间 */
    private String needStartTimeValue;

    /** 装货地-省名称 */
    private String departureProvinceValue;

    /** 装货地-市名称 */
    private String departureCityValue;

    /** 装货地-县区名称 */
    private String departureCountyValue;

    /** 装货地-详细地址 */
    private String departureAddress;

    /** 卸货地-省名称 */
    private String receiveProvinceValue;

    /** 卸货地-市名称 */
    private String receiveCityValue;

    /** 卸货地-县区名称 */
    private String receiveCountyValue;

    /** 卸货地-详细地址 */
    private String receiveAddress;

    /** 创建主账户名称 */
    private String createParentUserName;

    /** 创建主帐户id */
    private Long createParentUserId;

    /**
     * 认证状态 {@link com.cy.saas.basic.model.enums.SubmitType}
     */
    private String submitType;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 我的报价状态 0等待报价
     */
    private Byte myQuoteState;

    /** 我的当前报价 */
    private BigDecimal myQuote;

    /**
     * 最低报价
     */
    private BigDecimal minPrice;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalCubage() {
        return totalCubage;
    }

    public void setTotalCubage(Double totalCubage) {
        this.totalCubage = totalCubage;
    }

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    public Byte getExecuteState() {
        return executeState;
    }

    public void setExecuteState(Byte executeState) {
        this.executeState = executeState;
    }

    public Date getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }

    public Byte getQuoteState() {
        return quoteState;
    }

    public void setQuoteState(Byte quoteState) {
        this.quoteState = quoteState;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public Date getNeedStartTime() {
        return needStartTime;
    }

    public void setNeedStartTime(Date needStartTime) {
        this.needStartTime = needStartTime;
    }

    public String getDepartureProvinceValue() {
        return departureProvinceValue;
    }

    public void setDepartureProvinceValue(String departureProvinceValue) {
        this.departureProvinceValue = departureProvinceValue;
    }

    public String getDepartureCityValue() {
        return departureCityValue;
    }

    public void setDepartureCityValue(String departureCityValue) {
        this.departureCityValue = departureCityValue;
    }

    public String getDepartureCountyValue() {
        return departureCountyValue;
    }

    public void setDepartureCountyValue(String departureCountyValue) {
        this.departureCountyValue = departureCountyValue;
    }

    public String getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(String departureAddress) {
        this.departureAddress = departureAddress;
    }

    public String getReceiveProvinceValue() {
        return receiveProvinceValue;
    }

    public void setReceiveProvinceValue(String receiveProvinceValue) {
        this.receiveProvinceValue = receiveProvinceValue;
    }

    public String getReceiveCityValue() {
        return receiveCityValue;
    }

    public void setReceiveCityValue(String receiveCityValue) {
        this.receiveCityValue = receiveCityValue;
    }

    public String getReceiveCountyValue() {
        return receiveCountyValue;
    }

    public void setReceiveCountyValue(String receiveCountyValue) {
        this.receiveCountyValue = receiveCountyValue;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getCreateParentUserName() {
        return createParentUserName;
    }

    public void setCreateParentUserName(String createParentUserName) {
        this.createParentUserName = createParentUserName;
    }

    public Long getCreateParentUserId() {
        return createParentUserId;
    }

    public void setCreateParentUserId(Long createParentUserId) {
        this.createParentUserId = createParentUserId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public String getValidityEndValue() {
        return validityEndValue;
    }

    public void setValidityEndValue(String validityEndValue) {
        this.validityEndValue = validityEndValue;
    }

    public String getSubmitType() {
        return submitType;
    }

    public void setSubmitType(String submitType) {
        this.submitType = submitType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Byte getMyQuoteState() {
        return myQuoteState;
    }

    public void setMyQuoteState(Byte myQuoteState) {
        this.myQuoteState = myQuoteState;
    }

    public BigDecimal getMyQuote() {
        return myQuote;
    }

    public void setMyQuote(BigDecimal myQuote) {
        this.myQuote = myQuote;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Long getOrderPartId() {
        return orderPartId;
    }

    public void setOrderPartId(Long orderPartId) {
        this.orderPartId = orderPartId;
    }

    public String getNeedStartTimeValue() {
        return needStartTimeValue;
    }

    public void setNeedStartTimeValue(String needStartTimeValue) {
        this.needStartTimeValue = needStartTimeValue;
    }

    public String getTotalQuantityValue() {
        return totalQuantityValue;
    }

    public void setTotalQuantityValue(String totalQuantityValue) {
        this.totalQuantityValue = totalQuantityValue;
    }

    public String getTotalWeightValue() {
        return totalWeightValue;
    }

    public void setTotalWeightValue(String totalWeightValue) {
        this.totalWeightValue = totalWeightValue;
    }

    public String getTotalCubageValue() {
        return totalCubageValue;
    }

    public void setTotalCubageValue(String totalCubageValue) {
        this.totalCubageValue = totalCubageValue;
    }
}
