package com.cy.driver.api.domain.res;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 货源详情
 * @author yanst 2016/5/26 15:54
 */
public class CargoDetail implements Serializable {

    private static final long serialVersionUID = 8677276319373404314L;

    /** 货源id */
    private String cargoIdStr;

    /** 企业名称 */
    private String companyName;

    /** 企业认证状态 */
    private Byte companyAuthStatus;

    /** 企业联系人 */
    private String companyContactName;

    /** 发货人姓名(字数小于等于3位的取第一个字拼接"先生"，否者取前2个字拼接"先生") */
    private String consignorName;

    /** 企业手机号码 */
    private String companyMobile;

    /** 累计交易数 */
    private Integer transactionNumber;

    /** 货主头像 */
    private String photosAddress;

    /** 货物名称 */
    private String cargoName;

    /** 数量合计 */
    private int totalNumber;

    /** 重量合计 */
    private BigDecimal weight;

    /** 体积合计 */
    private BigDecimal volume;

    /** 货物清单 */
    private List<CargoDetailList> cargoList;

    /** 发布企业id */
    private String companyId;

    /** 发布用户id */
    private String ownerUserId;

    /** 发货人联系电话 */
    private String consignorMobilePhone;

    /** 发货人固话 */
//    private String consignorTelePhone;

    /** 发货人地址 */
    private String consignorAddress;

    /** 收货人姓名 */
    private String consigneeName;

    /** 收货人联系电话 */
    private String consigneeMobilePhone;

    /** 收货人固话 */
    private String consigneeTelePhone;

    /** 收货人地址 */
    private String consigneeAddress;

    /** 送货方式 （0自取 1送货上门）*/
    private Integer  shippingMethod;

    /** 代收货款 */
    private BigDecimal collectionDelivery;

    /** 运费总价 */
    private BigDecimal  freightPrice;

    /** 预付费 */
    private BigDecimal prepayment;

//    /** 声明价值 */
//    private BigDecimal declaredValue;
//
//    /** 报价金额 */
//    private BigDecimal quoteMoney;
//
//    /** 保费支付 */
//    private Integer premiumpay ;

    /** 备注 */
    private String cargoRemark;

    /** 点评是否显示(0否、1是) */
    private String hasComment;

    /** 评论人手机号码 **** 隐藏中间四位 */
    private String commentMobilePhone;

    /** 评论人认证状态 */
    private String commentAuthState;

    /** 货源状态 1 货还在 2 已运走 */
    private String cargoState;

    /** 评论人时间 */
    private String commentTime;

    /** 评论人内容 */
    private String commentContent;

    /** 报价是否显示(0否、1是) */
    private String isShowQuote;

    /** 我的报价(带单位：元/车、元/吨、元/方) */
    private String myQuote;

    /** 报价时间(yyyy-mm-dd hh:mm) */
    private String quoteTime;

    /** 是否允许报价 0否 1是 */
    private Byte allowQuote;

    private String networkTelephoneState;

    /************************ 快到网货源信息 ****************/
    private String sAddress;//装货地

    private String eAddress;//卸货地

    private String sTime;//要求装货时间(yyyy-mm-dd hh:mm)

    private String eTime;//要求卸货时间(yyyy-mm-dd hh:mm)

    private String carLength;//要求的车长(单位：米)

    private String vehicleTypeName;//车辆类型名称

    private String carriageTypeName;//车厢类型名称

    private String pubTime;//发布时间(yyyy-mm-dd hh:mm)

    private String createUserType;//创建人类型

    private String saddressInfoPCC;//装货地地址信息(省市区)

    private String saddressInfoDetail;//装货地地址信息(详细地址)

    private String eaddressInfoPCC;//卸货地地址信息(省市区)

    private String eaddressInfoDetail;//卸货地地址信息(详细地址)

    private String cargoStateReal;//货源状态(通过数据比对得出)

    private String cargoSource;//货物来源

    public String getCreateUserType() {
        return createUserType;
    }

    public void setCreateUserType(String createUserType) {
        this.createUserType = createUserType;
    }

    public String getNetworkTelephoneState() {
        return networkTelephoneState;
    }

    public void setNetworkTelephoneState(String networkTelephoneState) {
        this.networkTelephoneState = networkTelephoneState;
    }

    public String getHasComment() {
        return hasComment;
    }

    public void setHasComment(String hasComment) {
        this.hasComment = hasComment;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String geteAddress() {
        return eAddress;
    }

    public void seteAddress(String eAddress) {
        this.eAddress = eAddress;
    }

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

    public String getCargoIdStr() {
        return cargoIdStr;
    }

    public void setCargoIdStr(String cargoIdStr) {
        this.cargoIdStr = cargoIdStr;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Byte getCompanyAuthStatus() {
        return companyAuthStatus;
    }

    public void setCompanyAuthStatus(Byte companyAuthStatus) {
        this.companyAuthStatus = companyAuthStatus;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getCompanyMobile() {
        return companyMobile;
    }

    public void setCompanyMobile(String companyMobile) {
        this.companyMobile = companyMobile;
    }

    public Integer getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(Integer transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getPhotosAddress() {
        return photosAddress;
    }

    public void setPhotosAddress(String photosAddress) {
        this.photosAddress = photosAddress;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public List<CargoDetailList> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<CargoDetailList> cargoList) {
        this.cargoList = cargoList;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getConsignorMobilePhone() {
        return consignorMobilePhone;
    }

    public void setConsignorMobilePhone(String consignorMobilePhone) {
        this.consignorMobilePhone = consignorMobilePhone;
    }

    public String getConsignorAddress() {
        return consignorAddress;
    }

    public void setConsignorAddress(String consignorAddress) {
        this.consignorAddress = consignorAddress;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeMobilePhone() {
        return consigneeMobilePhone;
    }

    public void setConsigneeMobilePhone(String consigneeMobilePhone) {
        this.consigneeMobilePhone = consigneeMobilePhone;
    }

    public String getConsigneeTelePhone() {
        return consigneeTelePhone;
    }

    public void setConsigneeTelePhone(String consigneeTelePhone) {
        this.consigneeTelePhone = consigneeTelePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public Integer getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(Integer shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public BigDecimal getCollectionDelivery() {
        return collectionDelivery;
    }

    public void setCollectionDelivery(BigDecimal collectionDelivery) {
        this.collectionDelivery = collectionDelivery;
    }

    public BigDecimal getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    public BigDecimal getPrepayment() {
        return prepayment;
    }

    public void setPrepayment(BigDecimal prepayment) {
        this.prepayment = prepayment;
    }

    public String getCargoRemark() {
        return cargoRemark;
    }

    public void setCargoRemark(String cargoRemark) {
        this.cargoRemark = cargoRemark;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getCarriageTypeName() {
        return carriageTypeName;
    }

    public void setCarriageTypeName(String carriageTypeName) {
        this.carriageTypeName = carriageTypeName;
    }

    public String getCommentMobilePhone() {
        return commentMobilePhone;
    }

    public void setCommentMobilePhone(String commentMobilePhone) {
        this.commentMobilePhone = commentMobilePhone;
    }

    public String getCommentAuthState() {
        return commentAuthState;
    }

    public void setCommentAuthState(String commentAuthState) {
        this.commentAuthState = commentAuthState;
    }

    public String getCargoState() {
        return cargoState;
    }

    public void setCargoState(String cargoState) {
        this.cargoState = cargoState;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getIsShowQuote() {
        return isShowQuote;
    }

    public void setIsShowQuote(String isShowQuote) {
        this.isShowQuote = isShowQuote;
    }

    public String getMyQuote() {
        return myQuote;
    }

    public void setMyQuote(String myQuote) {
        this.myQuote = myQuote;
    }

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public Byte getAllowQuote() {
        return allowQuote;
    }

    public void setAllowQuote(Byte allowQuote) {
        this.allowQuote = allowQuote;
    }

    public String getCompanyContactName() {
        return companyContactName;
    }

    public void setCompanyContactName(String companyContactName) {
        this.companyContactName = companyContactName;
    }

    public String getEaddressInfoDetail() {
        return eaddressInfoDetail;
    }

    public void setEaddressInfoDetail(String eaddressInfoDetail) {
        this.eaddressInfoDetail = eaddressInfoDetail;
    }

    public String getEaddressInfoPCC() {
        return eaddressInfoPCC;
    }

    public void setEaddressInfoPCC(String eaddressInfoPCC) {
        this.eaddressInfoPCC = eaddressInfoPCC;
    }

    public String getSaddressInfoDetail() {
        return saddressInfoDetail;
    }

    public void setSaddressInfoDetail(String saddressInfoDetail) {
        this.saddressInfoDetail = saddressInfoDetail;
    }

    public String getSaddressInfoPCC() {
        return saddressInfoPCC;
    }

    public void setSaddressInfoPCC(String saddressInfoPCC) {
        this.saddressInfoPCC = saddressInfoPCC;
    }

    public String getCargoStateReal() {
        return cargoStateReal;
    }

    public void setCargoStateReal(String cargoStateReal) {
        this.cargoStateReal = cargoStateReal;
    }

    public String getCargoSource() {
        return cargoSource;
    }

    public void setCargoSource(String cargoSource) {
        this.cargoSource = cargoSource;
    }
}
