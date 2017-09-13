package com.cy.driver.common.enumer;

/**
 *
 *  yanst
 *
 *  api 请求码编号  此请求码基于3.5版本以上创建 位数：5位
 */
public enum ApiReqCodeEnum {

    DEFAULT("",""),
    /**初始化数据 */
    GET_INITIALIZATION_DATA("1001", "初始化数据"),
    /** 系统版本检查及下载(系统接口) */
    REQ_CODE_CHECK_VERSION_DOWN("1003", "系统版本检查及下载(系统接口)"),
    /**  引导页图片列表 */
    GET_BOOT_PAGE_LIST("1005", "引导页图片列表"),
    /** 登录 */
    REQ_CODE_LOGIN_DRIVER_USERINFO("1107", "登录"),
    /** 登录验证 验证码 */
    VERIFICATION_FOR_LOGIN("1109", "登录验证 验证码"),
    /*注册-request*/
    REQ_CODE_REGIST_DRIVER_USERINFO("1111", "注册"),
    /*忘记密码下一步-request*/
    REQ_CODE_FORGET_NEXT_DRIVER_USERINFO("1113", "忘记密码下一步"),
    /*重置密码*/
    REQ_CODE_RESET_PASSWD_DRIVER_USERINFO("1115", "重置密码"),
    /*获取验证码*/
    REQ_CODE_GET_AUTH_CODE_DRIVER_USERINFO("1117", "获取验证码"),
    /** apk 下载 */
    REQ_CODE_APP_DOWN("1119", "apk 下载"),
    /** 引导页图片下载 */
    BOOT_PAGE_PICTURE_DOWN("1121", "引导页图片下载"),
    /** 我的主页信息查询 */
    MY_HOME_INFO("1123", "我的主页信息查询"),

    /** 登录名修改 */
    UPDATE_LOGIN_NAME("1125", "登录名修改"),
    /** 用户姓名修改 */
    UPDATE_USER_NAME("1127", "用户姓名修改"),
    /** 个人头像修改 */
    UPDATE_PERSONNEL_PHOTOS("1129", "个人头像修改"),
    /**添加车辆*/
    REQ_CODE_CAR_ADD ("1131", "添加车辆"),
    /**更新车辆*/
    REQ_CODE_CAR_SELECT ("1133", "更新车辆"),
    /**司机添加常跑城市*/
    REQ_CODE_OFTEN_CITY_ADD ("1135", "司机添加常跑城市"),
    /**司机常跑城市 列表*/
    REQ_CODE_OFTEN_CITY_LIST ("1137", "司机常跑城市 列表"),
    /** 空车上报列表 */
    EMPTY_CAR_REPORT_LIST("1139", "空车上报列表"),
    /** 删除空车上报 */
    DELETE_EMPTY_CAR_REPORT("1141", "删除空车上报"),
    /** 获得分享app的内容 */
    BUILD_APP_SHARE_CONTENT("1143", "获得分享app的内容"),
    /** 车辆认证-认证*/
    CAR_AUTHENTICATION("1145", "车辆认证-认证"),
    /** 驾驶证认证-认证*/
    AUTHENTICATION_DRIVING_LICENSE("1147", "驾驶证认证-认证"),
    /** 实名认证认证*/
    AUTHENTICATION_REAL_NAME("1149", "实名认证认证"),
    /** 获取认证*/
    GET_AUTHENTICATION_INFO("1151", "获取认证"),
    /** 查询合同客户列表 */
    QUERY_PACT_INFO_LIST("1153", "查询合同客户列表"),
    /** 合同客户确认 */
    UPDATE_PACT_STATE("1155", "合同客户确认"),
    /** 修改密码*/
    REQ_CODE_UPDATE_PASSWD("1157", "修改密码"),
    /**意见反馈 保存*/
    REQ_CODE_FEEDBACK_ADD("1159", "意见反馈 保存"),
    /**上传单个文件 */
    REQ_SINGLE_FILE_UPLOAD("1161", "上传单个文件"),
    /**上传多个文件 */
    REQ_MUILTI_FILE_UPLOAD("1163", "上传多个文件"),
    /** 合同客户线路确认 */
    UPDATE_VIP_DRIVER_LINE_STATE("1165", "合同客户线路确认"),
    /** 查询合同客户线路详情 */
    QUERY_PACT_DRIVER_DETAILS("1167", "查询合同客户线路详情"),
    /** 用户退出*/
    REQ_CODE_USER_QUIT("1169", "用户退出"),

    /** 保存点击的推送 */
    UPLOAD_REGISTRATION_ID("1174", "保存点击的推送"),

    /** 消息中心列表 */
    MESSAGE_CENTER_LIST("1170", "消息中心列表"),

    /** 消息标记为点击 */
    MARK_CLICKED("1172", "消息标记为点击"),

    /** 账户注销 */
    CLOSE_ACCOUNT("1177", "账户注销"),
    /** 我的首页信息（3.4以上版本） */
    MYHOME_INFO_NEW("1179", "我的首页信息（3.4以上版本）"),
    /** 首页完善车辆信息接口（3.4以上版本） */
    PERFECT_INFORMATION("1181", "首页完善车辆信息接口（3.4以上版本）"),

    /** 编辑车辆图片信息（3.4以上版本） */
    EDIT_CAR_PHOTO("1183", "编辑车辆图片信息（3.4以上版本）"),
    /** 编辑车牌信息（3.4以上版本） */
    EDIT_CAR_NUMBER("1185", "编辑车牌信息（3.4以上版本）"),
    /** 编辑车辆类型信息（3.4以上版本） */
    EDIT_CAR_TYPE("1187", "编辑车辆类型信息（3.4以上版本）"),
    /** 编辑车辆载重信息（3.4以上版本） */
    EDIT_CAR_LOAD("1189", "编辑车辆载重信息（3.4以上版本）"),
    /** 编辑车辆体积信息（3.4以上版本） */
    EDIT_CAR_VOLUME("1191", "编辑车辆体积信息（3.4以上版本）"),
    /** 我的钱包信息（3.4以上版本） */
    MY_WALLET_INFO("1193", "我的钱包信息（3.4以上版本）"),
    /** 添加浏览记录 （3.4以上版本）*/
    ADD_BROWSE_RECORD("1195", "添加浏览记录 （3.4以上版本）"),
    /** 货源浏览记录列表 （3.4以上版本）*/
    PAGE_FOR_CARGO_BROWSERECORD("1197", "货源浏览记录列表 （3.4以上版本）"),
    /** 删除货源浏览记录 （3.4以上版本）*/
    DELETES_CARGO_BROWSE_RECORD("1199", "删除货源浏览记录 （3.4以上版本）"),
    /** 批量删除消息 （3.4以上版本）*/
    DELETES_MSG("1201", "批量删除消息 （3.4以上版本）"),
    /** 批量删除消息 （3.4以上版本） 1201 3.5版本废除1201 接口*/
    DELETES_MSG2("1211", "批量删除消息2"),
    /** 初始主要银行码表数据接口 （3.4以上版本）*/
    GET_BANK_TABLE("1203", "初始主要银行码表数据接口 （3.4以上版本）"),
    /** 初始化数据:支付业务行为 （3.4以上版本）*/
    GET_BUSINESS_KIND_TYPE("1205", "初始化数据:支付业务行为 （3.4以上版本）"),
    /** 合同客户列表（3.4以上版本）*/
    PAQE_PACT_INFO("1207", "合同客户列表（3.4以上版本）"),
    /** 兑换积分引导页图片（3.4以上版本）*/
    LIST_INTERRATION_BOOTPAGE("1209", "兑换积分引导页图片（3.4以上版本）"),


    /** 货源首页轮播图片*/
    SEQ_CODE_CARGO_LOOP_PHOTO("2001", "货源首页轮播图片"),
    /** 首页信息*/
    QUERY_INDEX_INFO("2003", "首页信息"),

    /** 首页信息(3.3以上版本）*/
    QUERY_INDEX_INFO_NEW("2999", "首页信息(3.3以上版本）"),

    /** 首页数量(3.3以上版本）*/
    HOME_COUNT_CARGO_NEW("2997", "首页数量(3.3以上版本）"),
    /** 首页数量(3.4以上版本）*/
    COUNT_HOME_PAGE("2993", "首页数量(3.4以上版本）"),

    /** 附近货源数量(3.3以上版本）*/
    NEAR_CARGO_COUNT_NEW("2995", " 附近货源数量(3.3以上版本）"),

    /** 查看更多评价*/
    LOOK_MORE_COMMENT("2015", " 查看更多评价"),


    /** 司机接单开关*/
    REQ_CODE_ACCESS_ORDER_SWITCH("2007", "司机接单开关"),
    /** 在线报价*/
    REQ_CODE_ONLNE_QUOTE("2017", "在线报价"),
    /** 货物点评*/
    REQ_CODE_CARGO_COMMENT("2019", "货物点评"),
    /*获取货源分享内容*/
    REQ_CODE_GET_CARGO_SHARE_CONTENT("2021", "获取货源分享内容"),
    /*查看发货人详情*/
    REQ_CODE_GET_WEB_USER_INFO("2023", "查看发货人详情"),

    /**附近货源数量*/
    COUNT_NEAR_CARGO("2005", "附近货源数量"),


    /*附近货源列表*/
    REQ_CODE_QUERY_NEAR_CARGO("2009", "附近货源列表"),
    /**搜索货源*/
    QUERY_CARGO_LIST("2011", "搜索货源"),
    /**获取货源详情*/
    LOOK_CARGO_DETAILS("2013", "获取货源详情"),
    /**空车上报提交*/
    ADD_EMPTY_CAR("2025", "空车上报提交"),


    /**我的报价列表*/
    QUERY_MY_QUOTE_LIST("2027", "我的报价列表"),

    /**首页其它数量*/
    QUERY_INDEX_NUMS("2031", "首页其它数量"),

    /**报价历史*/
    QUERY_QUOTE_HISTORY("2029", "报价历史"),

    /**点击首页页面的提交*/
    CLICK_INDEX_SUBMIT("2033", "点击首页页面的提交"),

    /**查询货主评论列表 （3.4版本）*/
    GET_ONWER_ACCESS_LIST("2037", "查询货主评论列表 （3.4版本）"),

    /**空车上报（批量） （3.4版本）*/
    ADD_EMPTYCAR_REPORT("2039", "空车上报（批量） （3.4版本）"),

    /**精准货源 （3.4版本）*/
    ACCURATE_CARGO("2041", "精准货源 （3.4版本）"),




    /** 保存IOS推送的token */
    SAVE_IOS_PUSH_TOKEN("1201", "保存IOS推送的token"),

    /** 订单列表 */
    TRANSACTION_ORDER_LIST("3001", "订单列表"),

    /** 订单状态变更 */
    UPDATE_ORDER_STATUS("3003", "订单状态变更"),

    /** 查看订单详情 */
    LOOK_ORDER_DETAILS("3007", "查看订单详情"),

    /** 查看发货人详情(订单) */
    QUERY_ORDER_CONSIGNOR_DETAILS("3027", "查看发货人详情(订单)"),

    /** 提交评价 */
    SAVE_COMMENT("3011", "提交评价"),

    /** 查看评价 */
    GET_COMMENT("3017", "查看评价"),

    /** 取消订单 */
    CANCEL_ORDER("3019", "取消订单"),

    /**	是否同意取消订单 */
    AUDIT_CANCEL_ORDER("3021", "是否同意取消订单"),

    /** 位置上传 */
    SUBMIT_UPLOAD_LOCATION("4001", "位置上传"),

    /** 上传回单 */
    UPLOAD_RECEIPT("3023", "上传回单"),

    /** 初始化定时位置上传参数 */
    INIT_TIME_LOC_PARAM("4003", "初始化定时位置上传参数"),

    /**	上传发货单 */
    UPLOAD_INVOICE("3005", "上传发货单"),

    /** 查看回单 */
    LOOK_RECEIPT("3015", "查看回单"),

    /**	查看发货单 */
    LOOK_INVOICE("3013", "查看发货单"),

    /**	问题上报 */
    SUBMIT_ORDER_PROBLEM("3009", "问题上报"),

    /** 保存点击的推送 */
    SAVE_CLICKED_PUSH("5001", "保存点击的推送"),


    /** 拨打网络电话 */
    DIAL_NETWORK_PHONE("2035", "拨打网络电话"),


    /**待收运费列表 */
    COLLECT_FREIGHT_LIST("3031", "待收运费列表"),

    /**设置提现密码 */
    SET_WITHDRAW_PASSWORD("6001", "设置提现密码"),

    /**修改提现密码 */
    UPDATE_WITHDRAW_PASSWORD("6003", "修改提现密码"),

    /**找回提现密码（身份验证） */
    FIND_PASSWORD_IDENTITY("6005", "找回提现密码（身份验证）"),

    /**找回提现密码（手机号码验证） */
    FIND_PASSWORD_MOBILE("6007", "找回提现密码（手机号码验证）"),

    /**查看我的银行卡信息 */
    FIND_WITHDRAW_PASSWORD("6009", "查看我的银行卡信息"),

    /**账户余额*/
    FIND_BMIKECE("6017", "账户余额"),
    /***/

    /**提现*/
    WITHDRAW("6019", "提现"),

    /**银行码表 */
    BANK_CODE_LIST("6021", "银行码表"),

    /**查看我的银行卡信息 */
    FIND_MY_BANKCARD_INFO("6011", "查看我的银行卡信息"),

    /**新增我的银行卡 */
    ADD_MYBANKCARD_INFO("6013", "新增我的银行卡"),

    /** 删除提现银行卡 */
    DELETE_MYBANKCARD_INFO("6015", "删除提现银行卡"),

    /** 账单明细（3.4版本）*/
    BILL("6017", "账单明细（3.4版本）"),

    /** 本月账单明细（3.4版本）*/
    THIS_MONTH_BILL("6019", "本月账单明细（3.4版本）"),

    /** 账单明细（3.4版本）*/
    OILCARD_BILL("6107", "油卡账单明细"),

    /** 账单清算列表（3.4版本）*/
    OILCARD_CLEARING_BILL("6109", "油卡清算列表"),

    /**确认收款 */
    CONFIRM_COLLECTION("3029", "确认收款"),

    /**3.3.17	查看运输协议 */
    GET_TRANSPROT_PRO("3033", "查看运输协议"),

    GET_TURN_WAYBILL_TRANSPORT_PRO("3059","转单查看运输协议"),

    /** 订单拨打电话 */
    ORDER_PHONE("3035", "订单拨打电话"),

    /** 统计订单数量（司机3.4版本） */
    COUNT_ORDER_NUM("3037", "统计订单数量（司机3.4版本）"),

    /** 待承运订单分页列表（司机3.4版本） */
    PAGE_ORDER_WAIT_CARRIER("3039", ""),

    /** 待装货订单分页列表（司机3.4版本） */
    PAGE_ORDER_WAIT_LOAD("3041", "待装货订单分页列表（司机3.4版本）"),

    /** 待卸货订单分页列表（司机3.4版本） */
    PAGE_ORDER_WAIT_UNLOAD("3043", "待卸货订单分页列表（司机3.4版本）"),

    /** 待评价订单分页列表（司机3.4版本） */
    PAGE_ORDER_WAIT_EVAL("3045", "待评价订单分页列表（司机3.4版本）"),

    /** 其他订单分页列表（司机3.4版本） */
    PAGE_ORDER_WAIT_OTHER("3047", "其他订单分页列表（司机3.4版本）"),

    /** 订单详情（司机3.4版本） */
    ORDER_ALL_DETAILS("3049", "订单详情（司机3.4版本）"),
    /** 订单评价详情（司机3.4版本） */
    ORDER_EVAL_DETAILS("3051", "订单评价详情（司机3.4版本）"),

    /** 保存订单评价（司机3.4版本） */
    SAVE_ORDER_EVAL("3053", "保存订单评价（司机3.4版本）"),

    /** 搜索订单分页列表（司机3.4版本） */
    PAGE_SEARCH_ORDER("3055", "搜索订单分页列表（司机3.4版本）"),

    AUTHENTICATE_ALL_INFO("3057","身份证、驾驶证、行驶证认证(司机3.5版本)"),

    /** 兑换兑换码 */
    CONVERT_CODE("6101", "兑换兑换码"),

    /** 积分明细分页列表 */
    PAGE_INTEGRATION("6103", "积分明细分页列表"),

    /** 添加积分 */
    ADD_INTEGRATION("6105", "添加积分"),

    /** 货源首页轮播图片 */
    SEQ_ADVERTISEMENT_LOOP_PHOTO("7001","货源首页轮播图片"),

    /*****************3.5版本********************************/

    /** 货源模块  10000-19999 */

    NEAR_CARGO("10000","附近货源"),
    FIND_CARGO("10002","找货"),
    CARGO_DETAIL("10004","货源详情"),

    COMMENT_LIST("10006","评论列表"),
    ADD_COMMENT("10008","添加评论"),

    OWNER_DETAIL("10010","货主详情"),
    HOME_PAGE_NUM("10012","首页数量"),
    ADD_QUOTE_INFO("10014","保存报价信息"),
    MY_CARGO_QUOTE("10016","我的报价列表"),
    QUOTED_INFO("10018","报价分页列表"),
    QUERY_ACCURATE_CARGO("10020","精准货源"),
    QUERY_MESSAGE_INFO("10022","消息信息查询"),
    MESSAGE_TO_BUSINESSDETAIL("10024","短信转向业务详情"),
    MODIFY_MESSAGE_STATE("10026","改变短信查看状态"),

    /** 订单模块 20000-21000 */
    COMMON_WAY_BILL_NUMBER("20000","普通运单数量"),
    WAY_BILL_LIST("20004","运单列表"),
    WAY_BILL_DETAIL("20006","运单详情"),
    CLOUD_UPDATE_ORDER_STATUS("20008","确认承运"),
    CLOUD_ORDER_QUOTE("20010","运单报价"),
    CLOUD_CARRIER_DETAIL("20012","托单详情"),
    CLOUD_CARRIER_SIGN("20014","托单签收"),
    CLOUD_DRIVER_INFO("20016","司机详情"),
    CLOUD_TURN_WAYBILL("20018","运单转单"),
    CLOUD_USER_CLOUD("20020","用户详情"),
    CLOUD_USER_AUTHEDINFO("20022","用户认证信息"),
    CLOUD_TRACKING_SAVE("20024","保存司机轨迹信息"),


    /** 转单运单模块 21001-22000 NXJ */
    CONVERT_ORDER_WAY_BILL_NUMBER("21001","转单运单数量"),
    FIND_WAYBILL_PAGE("21003","转单运单列表"),
    FIND_BAYILL_BY_ID("21005","查看转单运单详情"),
    UPDATE_WAYBILL_FARE("21007","修改转单运单报价"),
    UPDATE_WAYBILL_FARE_STATE("21009","确认转单运单报价"),
    PUSH_WAYBILL_BY_ID("21011","转单运单提醒接单"),
    ADD_WAYBILL_ASSESS("21013","保存转单运单评价"),
    FIND_WAYBILL_ASSESS_BY_ID("21015","查看转单运单评价"),
    FIND_WAYBILL_PATH_BY_ID("21017","查看转单运单轨迹描点"),
    FIND_WAYBILL_PATH_BY_ID_LIST("21019","查看转单运单历史轨迹"),
    QUERY_WAYBILL_LIST("21021","搜索运单"),

    /** SAAS抢单/竞价 22001-23000 NXJ */
    SAAS_ORDER_PAGE("22001","抢单/竞价/我的报价列表"),
    SAAS_ORDER_INFO("22003","抢单/竞价/我的报价详情ID"),
    SAAS_ORDER_ROB("22005","抢单"),
    SAAS_ORDER_BID("22007","报价-重新报价"),
    SAAS_ORDER_PARTID_INFO("22009","抢单/竞价/我的报价详情PARTID"),



    QUERY_MY_USER_ACCOUNT_INFO("22011","我的首页获取总资产和钱包金额"),
    QUERY_MY_UTMS_ACCOUNT_INFO("22013","UTMS钱包首页"),
    GET_UTMS_BUSINESS_ENEN_ENUM("22015","获取UTMS账单查询类型"),
    PAGE_UTMS_BULL_DETAIL_LIST("22017","UTMS账单分页查询"),
    GET_UTMS_MAIN_BANK_BY_CARD_NO("22019","UTMS根据银行卡号获取总行信息"),
    PAGE_UTMS_BRANCH_BANK_LIST("22021","UTMS分页支行查询"),
    SAVE_UTMS_BIN_BANK("22023","UTMS绑卡"),
    DELETE_UTMS_UN_BIN_BANK("22025","UTMS解绑银行卡"),
    GET_UTMS_QUERY_BANK_LIST("22027","UTMS获取银行卡列表"),
    GET_SAAS_WITHDRAWAL_PROC_FEE("22029","UTMS获取提现手续费"),
    SAAS_WITHDRAWAL("22031","UTMS钱包提现"),
    PAGE_UTMS_MAIN_BANK_LIST("22033","UTMS分页总行查询"),
    ;

    private String code;

    private String desc;

    ApiReqCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
