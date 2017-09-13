package com.cy.driver.common.syslog;

/**
 * Created by haoy on 2014/9/23.
 */
public enum LogEnum {

    REGISTER(1, "quickRegistration", "注册","driver_regist"),
    LOGIN_DRIVER_USER_INFO(2, "logindriveruserinfo", "登陆"),
//    CHK_APP_VERSION(3, "systemVersionCheckAndDown", "app系统版本检查"),
//    APP_DOWN(4, "appDown", "APP下载"),
    UPDATE_LOGIN_NAME(6, "updateLoginName", "修改手机号码"),
    COUNT_NEAR_CARGO(12, "countNearCargo", "附近货源量"),
    QUERY_NEAR_CARGO_LIST(14, "queryNearCargoList", "附近货源列表"),
    MYHOME_INFO(17, "myHomeInfo", "我的主页信息查询"),
//    GET_VERIFICATION_CODE(18, "getVerificationCode", "获取验证码"),
    QUERY_CARGO_LIST(26, "queryCargoList", "搜索货源"),
    LOOK_CARGO_DETAILS(29, "lookCargoDetails", "货源详情"),
    TRANSACTION_ORDER_LIST(33, "transactionOrderList", "司机订单列表"),
    LOOK_ORDER_DETAILS(34, "lookOrderDetails", "订单详情"),
    ONLINE_QUOTE(39, "onlineQuote", "在线报价"),
    SUBMIT_UPLOAD_LOCATION(41, "submitUploadLocation", "位置信息上传"),
    BE_SUBMIT_UPLOAD_LOCATION(42, "beSubmitUploadLocation", "被动位置信息上传"),
    QUERY_MY_QUOTE_LIST(52, "queryMyQuoteList", "我的报价列表"),
    CANCEL_ORDER(55, "cancelOrder", "司机取消订单"),
    REALNAME_AUTHENTICATION(58, "realNameAuthentication", "实名认证"),
    UPLOAD_RECEIPT(66, "receiptUpload", "上传回单","driver_upload_receipt"),
    UPLOAD_INVOICE(67, "receiptVerify", "上传发货单","driver_upload_invoice"),
//    DELETE_RECEIPT_INVOICE(68, "deleteReceiptInvoice", "删除发货单/回单图片"),
    LOOK_RECEIPT(70, "lookReceipt", "查看回单"),
    UPDATE_PACT_STATE(75, "updatePactState", "合同客户确认接口（同意拒绝）","driver_aggree_pact_customer"),
    QUERY_PACT_DRIVER_DETAILS(76, "queryPactDriverDetails", "查询合同客户线路详情"),
    UPDATE_VIP_DRIVER_LINE_STATE(77, "updateVipDriverLineState", "合同客户线路确认（同意/拒绝）"),
    GET_INITIALIZATION_DATA(1000, "getInitializationData", "初始化数据接口"),
    SAVE_IOS_PUSH_TOKEN(1001, "saveIosPushToken", "保存IOS推送的token"),
    NEXT_FOR_GET_PASSWORD(2000, "nextForgetPassword", "下一步（忘记密码）"),
    UPDATE_USER_NAME(2001, "updateUserName", "用户姓名修改"),
    UPDATE_PERSONNEL_PHOTOS(2002, "updatePersonnelPhotos", "个人头像修改","driver_upload_head_img"),
    ADD_CAR_INFO(2003, "addCarInfo", "添加车辆数据接口","driver_update_car_img"),
    GET_CAR_INFO(2004, "getCarInfo", "查看车辆信息接口"),
    ADD_OFTEN_RUN_CITYS(2005, "addOftenRunCitys", "增加常跑城市","driver_add_ofen_run_city"),
    OFTEN_RUN_CITYS_LIST(2006, "oftenRunCitysList", "常跑城市列表"),
    EMPTY_CAR_REPORT_LIST(2007, "emptyCarReportList", "空车上报列表"),
    DELETE_EMPTY_CAR_REPORT(2008, "deleteEmptyCarReport", "空车上报删除"),
    CAR_AUTHENTICATION(2009, "carAuthentication", "车辆认证-认证"),
    DIVING_LCENSE_AUTHENTICATION(2010, "divingLcenseAuthentication", "驾驶证认证-认证"),
    GET_AUTHENTICATION_INFO(2011, "getAuthenticationInfo", "查询认证-认证"),
    QUERY_PACT_INFO_LIST(2012, "queryPactInfoList", "合同客户查询接口"),
    UPDATE_DRIVER_PASSWD(2013, "updateDriverPassWd", "修改密码"),
    CONFIRM_CUSTOMER_INQUIRY(2014, "confirmCustomerInquiry", "意见反馈"),
    SIGN_OUT(2015, "signOut", "退出"),
    MESSAGE_CENTER_LIST(2016, "messageCenterList", "消息中心列表"),
    MARK_CLICKED(2017,"markClicked","消息标记为点击"),
    CLOSE_ACCOUNT(2018,"closeAccount","账户注销"),
    MY_WALLET_INFO(2019,"myWalletInfo","我的钱包信息"),
    ADD_BROWSE_RECORD(2020,"addBrowseRecord","添加货源浏览记录信息"),
    PAGE_FOR_CARGO_BROWSERECORD(2021,"pageForCargoBrowseRecord","货源浏览记录"),
    DELETES_CARGO_BROWSE_RECORD(2022,"addBrowseRecord","批量删除货源浏览记录信息"),
    DELETES_MSG(2023,"deletesMsg","批量删除消息"),
    LIST_INTERRATION_BOOTPAGE(2024,"listInterrationBootPage","积分引导页图片列表"),
    ALL_INFO_AUTHENTICATION(2025, "allInfoAuthentication", "身份证驾驶证行驶证认证"),


    QUERY_CAROUSEL_PICTURE(3000, "queryCarouselPicture", "轮播图片"),
    QUERY_ADVERTISEMENT(3101, "queryAdvertisement", "广告弹屏"),
    QUERY_INDEX_INFO(3001, "queryIndexInfo", "首页信息"),
    ANSWER_LIST_SWITCH(3002, "answerListSwitch", "接单开关"),
    LOOK_MORE_COMMENT(3003, "lookMoreComment", "查看更多点评"),
    BUILD_CARGO_SHARE_CONTENT(3004, "buildCargoShareContent", "获得货源分享内容", "driver_share_cargo"),
    LOOK_CONSIGNOR_DETAILS(3005, "lookConsignorDetails", "查看发货人详情"),
    ADD_EMPTY_CAR(3006, "addEmptyCar", "空车上报提交","driver_add_empty_car"),
    QUERY_QUOTE_HISTORY(3007, "queryQuoteHistory", "报价历史"),
    CLICK_INDEX_SUBMIT(3008, "clickIndexSubmit", "点击首页页面的提交(我的报价、待承运订单、承运订单页面)"),
    DIAL_NETWORK_PHONE(78,"dialNetworkPhone","货源拨打电话"),
    ORDER_PHONE(81,"orderPhone","订单拨打电话"),
    GET_ONWER_ACCESS_LIST(3009, "getOnwerAccessList", "查询货主评论列表"),
    ACCURATE_CARGO(3010, "accurateCargo", "精准货源"),


    UPDATE_ORDER_STATUS(4001, "updateOrderStatus", "订单状态变更"),
    SUBMIT_ORDER_PROBLEM(4002, "submitOrderProblem", "问题上报"),
    SUBMIT_ASSESS(4003, "submitAssess", "提交评价"),
    LOOK_INVOICE(4004, "lookInvoice", "查看发货单"),
    LOOK_ASSESS(4005, "lookAssess", "查看评价"),
    AUDIT_CANCEL_ORDER(4006, "auditCancelOrder", "是否同意取消订单"),
    INIT_TIME_LOC_PARAM(4007, "initTimeLocParam", "初始化定时位置上传参数"),
    QUERY_ORDER_CONSIGNOR_DETAILS(4008, "queryOrderConsignorDetails", "查看发货人详情（订单）"),
    CONFIRM_COLLECTION(4009,"confirmCollection","确认收款","driver_confirm_receipt_fee"),
    COLLECT_FREIGHT_LIST(4010,"collectFreightList","待收运费列表"),
    PAGE_ORDER_WAIT_CARRIER(4011, "pageOrderWaitCarrier", "待承运订单分页列表"),
    COUNT_ORDER_NUM(4012,"countOrderNum","统计订单数量"),
    COUNT_PAGE_NUM(4017,"countNearCargoNew","首页数量","driver_day_login"),
    PAGE_ORDER_WAIT_LOAD(4013,"pageOrderWaitLoading","待装货订单分页列表"),
    PAGE_ORDER_WAIT_UNLOAD(4014,"pageOrderWaitUnload","待卸货订单分页列表"),
    PAGE_ORDER_WAIT_EVAL(4015,"pageOrderWaitEval","待评价订单分页列表"),
    PAGE_ORDER_WAIT_OTHER(4016,"pageOrderWaitOther","其他订单分页列表"),
    ORDER_ALL_DETAILS(4017,"orderAllDetails","订单详情（司机3.4版本）"),
    ORDER_EVAL_DETAILS(4018,"orderEvalDetails","订单评价详情（司机3.4版本）"),
    SAVE_ORDER_EVAL(4003,"saveOrderEval","保存订单评价（司机3.4版本）","driver_order_assess"),
    PAGE_SEARCH_ORDER(4019,"pageSearchOrder","搜索订单分页列表（司机3.4版本）"),

    SET_WITHDRAW_PASSWORD(6000,"setWithdrawPassword","设置提现密码"),
    UPDATE_WITHDRAW_PASSWORD(6001,"updateWithdrawPassword","查询账户余额"),
    FIND_PASSWORD_IDENTITY(6002,"findPasswordIdentity","找回提现密码(身份证验证)"),
    FIND_PASSWORD_MOBILE(6003,"findPasswordMobile","找回提现密码(手机验证)"),
    FIND_WITHDRAW_PASSWORD(6004,"findWithdrawPassword","找回提现密码"),
    FIND_MYBANK_CARDINFO(6005,"findMyBankCardInfo","查看我的银行卡信息"),
    ADD_MYBANK_CARDINFO(6006,"addMyBankCardInfo","新增我的银行卡"),
    DELETE_MYBANK_CARDINFO(6007,"deleteMyBankCardInfo","删除提现银行卡"),
    MAIN_BMIKECE(6008,"mainBmikece","账单明细"),
    WITHDRAW(6009,"withdraw","提现"),
    THIS_MONTH_BILL(6010,"thisMonthBill","本月账单明细"),
    OILCARD_BILL(6011,"oilcardBill","油卡账单明细"),
    OILCARD_CLEARING_BILL(6012,"oilCardClearingBill","油卡清算列表"),

    BANK_CODE_LIST(0010,"bankCodeList","银行码表"),

    CONVERT_CODE(6101,"convertCode","兑换兑换码"),
    PAGE_INTEGRATION(6102,"积分明细分页列表","兑换兑换码"),
    ADD_INTEGRATION(6103,"addIntegration","添加积分"),

    CLOUD_CARRIER_DETAIL(7101,"cloudCarrierDetail","托单详情"),
    CARRIER_SIGN_IN(7102,"carrierSignIn","托单签收"),
    COMMENT_LIST(7103,"commentList","评论列表"),
    ADD_COMMENT(7104,"addComment","添加评论"),
    HOME_PAGE_NUM(7105,"homePageNum","首页数量"),
    QUERY_MESSAGE_INFO(7106,"queryMessageInfo","消息列表"),
    MODIFY_MESSAGE_STATE(7107,"modifyMessageState","更新消息状态"),
    NEAR_CARGO(7108,"nearCargo","附近货源"),
    FIND_CARGO(7109,"findCargo","搜索货源"),
    CARGO_DETAIL(7110,"cargoDetail","货源详情"),
    MESSAGE_TO_BUSINESSDETAIL(7111,"messageToBusinessDetail","推送跳转至货源详情"),
    ADD_QUOTE_INFO(7112,"addQuoteInfo","报价"),
    OWNER_DETAIL(7113,"ownerDetail","货主详情"),
    CLOUD_USER_CLOUD(7114,"cloudUserCloud","云配货主详情"),
    CLOUD_DRIVER_INFO(7115,"cloudDriverInfo","司机详情"),
    CLOUD_USER_AUTHEDINFO(7116,"cloudUserAuthedinfo","云配用户认证情况"),
    COMMON_WAY_BILL_NUMBER(7117,"commonWaybillNumber","普通运单数量"),
    WAYBILL_LIST(7118,"waybillList","运单列表"),
    WAYBILL_DETAIL(7119,"waybillDetail","运单详情"),
    CLOUD_UPDATE_ORDER_STATUS(7120,"cloudUpdateOrderStatus","改变运单状态"),
    CLOUD_ORDER_QUOTE(7121,"cloudOrderQuote","运单报价"),
    CLOUD_TURN_WAYBILL(7122,"cloudTurnWaybill","运单转单"),
    QUERY_WAYBILL_LIST(7123,"queryWaybillList","搜索运单列表"),
    CONVERT_ORDER_WAY_BILL_NUMBER(7124,"convertOrderWaybillNumber","转单运单数量"),
    FIND_WAYBILL_PAGE(7125,"findWaybillPage","转单运单列表"),
    FIND_BAYILL_BY_ID(7126,"findWaybillById","查询转单运单详情"),
    UPDATE_WAYBILL_FARE(7127,"updateWaybillFare","修改转单运单运费"),
    UPDATE_WAYBILL_FARE_STATE(7128,"updateWaybillFareState","确认报价"),
    PUSH_WAYBILL_BY_ID(7129,"pushWaybillById","转单运单提醒接单"),
    ADD_WAYBILL_ASSESS(7130,"addWaybillAssess","保存转单运单评价"),
    FIND_WAYBILL_ASSESS_BY_ID(7131,"findWaybillAssessById","查询转单运单评价"),
    FIND_WAYBILL_PATH_BY_ID(7132,"findWaybillPathById","转单运单轨迹描点"),
    FIND_WAYBILL_PATH_BY_ID_LIST(7133,"findWaybillPathByIdList","转单运单轨迹列表"),
    FIND_SAAS_ORDER_PAGE(7134,"saasOrderPageList","抢单/竞价/我的报价列表"),
    FIND_SAAS_ORDER_INFO(7135,"querySaasOrderById","抢单/竞价/我的报价详情"),
    UPDATE_SAAS_ORDER_ROB(7136,"saasOrderRob","抢单"),
    UPDATE_SAAS_ORDER_BID(7137,"saasOrderBid","报价-重新报价"),
    FIND_SAAS_ORDER_PARTID_INFO(7138,"querySaasOrderByPartId","抢单/竞价/我的报价详情"),






    ;


    private int operationType;
    private String operationName;
    private String remark;
    private String eventCode;

    public static final String DRIVER_ORDER_CHENGYUN = "driver_confirm_chengyun";
    public static final String DRIVER_ORDER_ZHUANGHUO = "driver_confirm_zhuanghuo";
    public static final String DRIVER_ORDER_XIEHUO = "driver_confirm_xiehuo";

    LogEnum(int operationType, String operationName, String remark) {
        this.operationType = operationType;
        this.operationName = operationName;
        this.remark = remark;
    }

    LogEnum(int operationType, String operationName, String remark,String eventCode) {
        this.operationType = operationType;
        this.operationName = operationName;
        this.remark = remark;
        this.eventCode = eventCode;
    }

    public int getOperationType() {
        return operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getRemark() {
        return remark;
    }

    public String getEventCode() {
        return eventCode;
    }
}
