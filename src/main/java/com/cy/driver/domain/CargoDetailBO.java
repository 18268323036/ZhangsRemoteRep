package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/22.
 * 货物详情列表
 */
public class CargoDetailBO implements Serializable {
    private static final long serialVersionUID = -6693980799858998682L;

    private Long cargoId;//货源id

    private String companyName;//企业名称

    private Byte companyAuthStatus;//企业认证状态

    private String cargoName;//货物名称

    private String sAddress;//装货地

    private String eAddress;//卸货地

    private String sTime;//要求装货时间(yyyy-mm-dd hh:mm)

    private String eTime;//要求卸货时间(yyyy-mm-dd hh:mm)

    private String weight;//重量(单位：吨)

    private String volume;//体积(单位：方)

    private String carLength;//要求的车长(单位：米)

    private String vehicleTypeName;//车辆类型名称

    private String carriageTypeName;//车厢类型名称

    private String cargoRemark;//货物备注

    private String pubTime;//发布时间(yyyy-mm-dd hh:mm)

    private String IsShowCommentType;//点评是否显示(0否、1是)

    private String commentDriverMobile;//最新点评司机手机号码（号码中间四位****）

    private String commentDriverStatus;//点评司机认证状态

    private Byte commentType;//点评类型(0货已走，1货还在)

    private String commentRemark;//点评备注

    private String commentTime;//点评时间(yyyy-mm-dd hh:mm:ss)

    private Long companyId;//发布企业id

    private Long deployUserId;//发布用户id

    private String consignorName;//发货人姓名(字数小于等于3位的取第一个字拼接"先生"，否者取前2个字拼接"先生")

    private String consignorMobile;//发货人手机号码

    private String IsShowQuote;//报价是否显示(0否、1是)

    private String myQuote;//我的报价(带单位：元/车、元/吨、元/方)

    private String quoteTime;//报价时间(yyyy-mm-dd hh:mm)

    private Byte allowQuote;//是否允许报价 0否 1是

    /** (3.1 新增)运费总价 */
    private String totalFare;

    /** (3.1 新增)预付运费 */
    private String prepayFare;

    /** 网络电话状态（0 可以拨打 1不可以拨打） */
    private String networkTelephoneState;

    /** 营业执照 */
    private String businessLicense;

    /** 信誉好 */
    private Integer good;

    /** 信誉中 */
    private Integer middle;

    /** 信誉差 */
    private Integer bad;

    /** 累计交易数 */
    private Integer transactionNumber;

    private String saddressInfoPCC;//装货地地址信息(省市区)

    private String saddressInfoDetail;//装货地地址信息(详细地址)

    private String eaddressInfoPCC;//卸货地地址信息(省市区)

    private String eaddressInfoDetail;//卸货地地址信息(详细地址)

    private String cargoStateReal;//货源状态(通过数据比对得出)

    private String ownerHeadPhone;//货主头像

    private String cargoSource;//货物来源

    private String oilCard;//油卡费用

    private String cash;//现金费用

    private String oilCardId;//油卡卡号

    public Integer getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(Integer transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getMiddle() {
        return middle;
    }

    public void setMiddle(Integer middle) {
        this.middle = middle;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getNetworkTelephoneState() {
        return networkTelephoneState;
    }

    public void setNetworkTelephoneState(String networkTelephoneState) {
        this.networkTelephoneState = networkTelephoneState;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getPrepayFare() {
        return prepayFare;
    }

    public void setPrepayFare(String prepayFare) {
        this.prepayFare = prepayFare;
    }

    public Byte getCommentType() {
        return commentType;
    }

    public void setCommentType(Byte commentType) {
        this.commentType = commentType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getDeployUserId() {
        return deployUserId;
    }

    public void setDeployUserId(Long deployUserId) {
        this.deployUserId = deployUserId;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
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

    public String getCargoRemark() {
        return cargoRemark;
    }

    public void setCargoRemark(String cargoRemark) {
        this.cargoRemark = cargoRemark;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getIsShowCommentType() {
        return IsShowCommentType;
    }

    public void setIsShowCommentType(String isShowCommentType) {
        IsShowCommentType = isShowCommentType;
    }

    public String getCommentDriverMobile() {
        return commentDriverMobile;
    }

    public void setCommentDriverMobile(String commentDriverMobile) {
        this.commentDriverMobile = commentDriverMobile;
    }

    public String getCommentDriverStatus() {
        return commentDriverStatus;
    }

    public void setCommentDriverStatus(String commentDriverStatus) {
        this.commentDriverStatus = commentDriverStatus;
    }

    public String getCommentRemark() {
        return commentRemark;
    }

    public void setCommentRemark(String commentRemark) {
        this.commentRemark = commentRemark;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }


    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getConsignorMobile() {
        return consignorMobile;
    }

    public void setConsignorMobile(String consignorMobile) {
        this.consignorMobile = consignorMobile;
    }

    public String getIsShowQuote() {
        return IsShowQuote;
    }

    public void setIsShowQuote(String IsShowQuote) {
        this.IsShowQuote = IsShowQuote;
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

    public Byte getCompanyAuthStatus() {
        return companyAuthStatus;
    }

    public void setCompanyAuthStatus(Byte companyAuthStatus) {
        this.companyAuthStatus = companyAuthStatus;
    }

    public Byte getAllowQuote() {
        return allowQuote;
    }

    public void setAllowQuote(Byte allowQuote) {
        this.allowQuote = allowQuote;
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

    public String getOwnerHeadPhone() {
        return ownerHeadPhone;
    }

    public void setOwnerHeadPhone(String ownerHeadPhone) {
        this.ownerHeadPhone = ownerHeadPhone;
    }

    public String getCargoSource() {
        return cargoSource;
    }

    public void setCargoSource(String cargoSource) {
        this.cargoSource = cargoSource;
    }

    public String getOilCard() {
        return oilCard;
    }

    public void setOilCard(String oilCard) {
        this.oilCard = oilCard;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getOilCardId() {
        return oilCardId;
    }

    public void setOilCardId(String oilCardId) {
        this.oilCardId = oilCardId;
    }
}
