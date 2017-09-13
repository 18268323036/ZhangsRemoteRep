package com.cy.driver.common.constants;

/**
 * 请求头和响应码的编号
 * Created by wyh on 2015/5/7.
 */
public class HeadCodeConstants {
	
	/**初始化数据 */
    public static final String GET_INITIALIZATION_DATA = "1001";
	/** 系统版本检查及下载(系统接口) */
    public static final String REQ_CODE_CHECK_VERSION_DOWN = "1003";
    /**  引导页图片列表 */
    public static final String GET_BOOT_PAGE_LIST = "1005";
    /** 登录 */
    public static final String REQ_CODE_LOGIN_DRIVER_USERINFO = "1107";
    /** 登录验证 验证码 */
    public static final String VERIFICATION_FOR_LOGIN = "1109";
    /*注册-request*/
    public static final String REQ_CODE_REGIST_DRIVER_USERINFO = "1111";
    /*忘记密码下一步-request*/
    public static final String REQ_CODE_FORGET_NEXT_DRIVER_USERINFO = "1113";
    /*重置密码*/
    public static final String REQ_CODE_RESET_PASSWD_DRIVER_USERINFO = "1115";
    /*获取验证码*/
    public static final String REQ_CODE_GET_AUTH_CODE_DRIVER_USERINFO = "1117";
    /** apk 下载 */
    public static final String REQ_CODE_APP_DOWN = "1119";
    /** 引导页图片下载 */
    public static final String BOOT_PAGE_PICTURE_DOWN = "1121";
    /** 我的主页信息查询 */
    public static final String MY_HOME_INFO = "1123";

    /** 登录名修改 */
    public static final String UPDATE_LOGIN_NAME = "1125";
    /** 用户姓名修改 */
    public static final String UPDATE_USER_NAME = "1127";
    /** 个人头像修改 */
    public static final String UPDATE_PERSONNEL_PHOTOS = "1129";
    /**添加车辆*/
    public static final String REQ_CODE_CAR_ADD ="1131";
    /**更新车辆*/
    public static final String REQ_CODE_CAR_SELECT ="1133";
    /**司机添加常跑城市*/
    public static final String REQ_CODE_OFTEN_CITY_ADD ="1135";
    /**司机常跑城市 列表*/
    public static final String REQ_CODE_OFTEN_CITY_LIST ="1137";
    /** 空车上报列表 */
    public static final String EMPTY_CAR_REPORT_LIST = "1139";
    /** 删除空车上报 */
    public static final String DELETE_EMPTY_CAR_REPORT = "1141";
    /** 获得分享app的内容 */
    public static final String BUILD_APP_SHARE_CONTENT = "1143";
    /** 车辆认证-认证*/
    public static final String CAR_AUTHENTICATION = "1145";
    /** 驾驶证认证-认证*/
    public static final String AUTHENTICATION_DRIVING_LICENSE = "1147";
    /** 实名认证认证*/
    public static final String AUTHENTICATION_REAL_NAME = "1149";
    /** 获取认证*/
    public static final String GET_AUTHENTICATION_INFO = "1151";
    /** 查询合同客户列表 */
    public static final String QUERY_PACT_INFO_LIST = "1153";
    /** 合同客户确认 */
    public static final String UPDATE_PACT_STATE = "1155";
    /** 修改密码*/
    public static final String REQ_CODE_UPDATE_PASSWD = "1157";
    /**意见反馈 保存*/
    public static final String REQ_CODE_FEEDBACK_ADD = "1159";
    /**上传单个文件 */
    public static final String REQ_SINGLE_FILE_UPLOAD = "1161";
    /**上传多个文件 */
    public static final String REQ_MUILTI_FILE_UPLOAD = "1163";
    /** 合同客户线路确认 */
    public static final String UPDATE_VIP_DRIVER_LINE_STATE = "1165";
    /** 查询合同客户线路详情 */
    public static final String QUERY_PACT_DRIVER_DETAILS = "1167";
    /** 用户退出*/
    public static final String REQ_CODE_USER_QUIT = "1169";

    /** 保存点击的推送 */
    public static final String UPLOAD_REGISTRATION_ID = "1174";

    /** 消息中心列表 */
    public static final String MESSAGE_CENTER_LIST = "1170";

    /** 消息标记为点击 */
    public static final String MARK_CLICKED = "1172";

    /** 账户注销 */
    public static final String CLOSE_ACCOUNT = "1177";
    /** 我的首页信息（3.4以上版本） */
    public static final String MYHOME_INFO_NEW = "1179";
    /** 首页完善车辆信息接口（3.4以上版本） */
    public static final String PERFECT_INFORMATION = "1181";

    /** 编辑车辆图片信息（3.4以上版本） */
    public static final String EDIT_CAR_PHOTO = "1183";
    /** 编辑车牌信息（3.4以上版本） */
    public static final String EDIT_CAR_NUMBER = "1185";
    /** 编辑车辆类型信息（3.4以上版本） */
    public static final String EDIT_CAR_TYPE = "1187";
    /** 编辑车辆载重信息（3.4以上版本） */
    public static final String EDIT_CAR_LOAD = "1189";
    /** 编辑车辆体积信息（3.4以上版本） */
    public static final String EDIT_CAR_VOLUME = "1191";
    /** 我的钱包信息（3.4以上版本） */
    public static final String MY_WALLET_INFO = "1193";
    /** 添加浏览记录 （3.4以上版本）*/
    public static final String ADD_BROWSE_RECORD = "1195";
    /** 货源浏览记录列表 （3.4以上版本）*/
    public static final String PAGE_FOR_CARGO_BROWSERECORD = "1197";
    /** 删除货源浏览记录 （3.4以上版本）*/
    public static final String DELETES_CARGO_BROWSE_RECORD = "1199";
    /** 批量删除消息 （3.4以上版本）*/
    public static final String DELETES_MSG = "1201";
    /** 初始主要银行码表数据接口 （3.4以上版本）*/
    public static final String GET_BANK_TABLE = "1203";
    /** 初始化数据:支付业务行为 （3.4以上版本）*/
    public static final String GET_BUSINESS_KIND_TYPE = "1205";
    /** 合同客户列表（3.4以上版本）*/
    public static final String PAQE_PACT_INFO = "1207";
    /** 兑换积分引导页图片（3.4以上版本）*/
    public static final String LIST_INTERRATION_BOOTPAGE = "1209";


    /** 货源首页轮播图片*/
    public static final String SEQ_CODE_CARGO_LOOP_PHOTO = "2001";
    /** 首页信息*/
    public static final String QUERY_INDEX_INFO = "2003";

    /** 首页信息(3.3以上版本）*/
    public static final String QUERY_INDEX_INFO_NEW = "2999";

    /** 首页数量(3.3以上版本）*/
    public static final String HOME_COUNT_CARGO_NEW = "2997";
    /** 首页数量(3.4以上版本）*/
    public static final String COUNT_HOME_PAGE = "2993";

    /** 附近货源数量(3.3以上版本）*/
    public static final String NEAR_CARGO_COUNT_NEW = "2995";

    /** 查看更多评价*/
    public static final String LOOK_MORE_COMMENT = "2015";


    /** 司机接单开关*/
    public static final String REQ_CODE_ACCESS_ORDER_SWITCH = "2007";
    /** 在线报价*/
    public static final String REQ_CODE_ONLNE_QUOTE = "2017";
    /** 货物点评*/
    public static final String REQ_CODE_CARGO_COMMENT = "2019";
    /*获取货源分享内容*/
    public static final String REQ_CODE_GET_CARGO_SHARE_CONTENT = "2021";
    /*查看发货人详情*/
    public static final String REQ_CODE_GET_WEB_USER_INFO = "2023";

    /**附近货源数量*/
    public static final String COUNT_NEAR_CARGO = "2005";


    /*附近货源列表*/
    public static final String REQ_CODE_QUERY_NEAR_CARGO = "2009";
    /**搜索货源*/
    public static final String QUERY_CARGO_LIST = "2011";
    /**获取货源详情*/
    public static final String LOOK_CARGO_DETAILS = "2013";

    /**空车上报提交*/
    public static final String ADD_EMPTY_CAR = "2025";

    /**我的报价列表*/
    public static final String QUERY_MY_QUOTE_LIST = "2027";

    /**首页其它数量*/
    public static final String QUERY_INDEX_NUMS = "2031";

    /**报价历史*/
    public static final String QUERY_QUOTE_HISTORY = "2029";

    /**点击首页页面的提交*/
    public static final String CLICK_INDEX_SUBMIT = "2033";

    /**查询货主评论列表 （3.4版本）*/
    public static final String GET_ONWER_ACCESS_LIST = "2037";
    
    /**空车上报（批量） （3.4版本）*/
    public static final String ADD_EMPTYCAR_REPORT = "2039";

    /**精准货源 （3.4版本）*/
    public static final String ACCURATE_CARGO = "2041";




    /** 保存IOS推送的token */
    public static final String SAVE_IOS_PUSH_TOKEN = "1201";

    /** 订单列表 */
    public static final String TRANSACTION_ORDER_LIST = "3001";

    /** 订单状态变更 */
    public static final String UPDATE_ORDER_STATUS = "3003";

    /** 查看订单详情 */
    public static final String LOOK_ORDER_DETAILS="3007";

    /** 查看发货人详情(订单) */
    public static final String QUERY_ORDER_CONSIGNOR_DETAILS="3027";

    /** 提交评价 */
    public static final String SAVE_COMMENT="3011";

    /** 查看评价 */
    public static final String GET_COMMENT="3017";

    /** 取消订单 */
    public static final String CANCEL_ORDER="3019";

    /**	是否同意取消订单 */
    public static final String AUDIT_CANCEL_ORDER="3021";

    /** 位置上传 */
    public static final String SUBMIT_UPLOAD_LOCATION = "4001";

    /** 上传回单 */
    public static final String UPLOAD_RECEIPT="3023";

    /** 初始化定时位置上传参数 */
    public static final String INIT_TIME_LOC_PARAM = "4003";

    /**	上传发货单 */
    public static final String UPLOAD_INVOICE="3005";

    /** 查看回单 */
    public static final String LOOK_RECEIPT="3015";

    /**	查看发货单 */
    public static final String LOOK_INVOICE="3013";

    /**	问题上报 */
    public static final String SUBMIT_ORDER_PROBLEM="3009";

    /** 保存点击的推送 */
    public static final String SAVE_CLICKED_PUSH = "5001";


    /** 拨打网络电话 */
    public static final String DIAL_NETWORK_PHONE = "2035";


    /**待收运费列表 */
    public static final String COLLECT_FREIGHT_LIST = "3031";

    /**设置提现密码 */
    public static final String SET_WITHDRAW_PASSWORD = "6001";

    /**修改提现密码 */
    public static final String UPDATE_WITHDRAW_PASSWORD = "6003";

    /**找回提现密码（身份验证） */
    public static final String FIND_PASSWORD_IDENTITY = "6005";

    /**找回提现密码（手机号码验证） */
    public static final String FIND_PASSWORD_MOBILE = "6007";

    /**查看我的银行卡信息 */
    public static final String FIND_WITHDRAW_PASSWORD = "6009";
    
    /**账户余额*/
    public static final String FIND_BMIKECE = "6017";
    /***/
    
    /**提现*/
    public static final String WITHDRAW = "6019";
    
    /**银行码表 */
    public static final String BANK_CODE_LIST = "6021";

    /**查看我的银行卡信息 */
    public static final String FIND_MY_BANKCARD_INFO = "6011";

    /**新增我的银行卡 */
    public static final String ADD_MYBANKCARD_INFO = "6013";

    /** 删除提现银行卡 */
    public static final String DELETE_MYBANKCARD_INFO = "6015";

    /** 账单明细 （3.4版本）*/
    public static final String BILL = "6017";

    /** 本月账单明细 （3.4版本）*/
    public static final String THIS_MONTH_BILL = "6019";

    /**确认收款 */
    public static final String CONFIRM_COLLECTION = "3029";

    /**3.3.17	查看运输协议 */
    public static final String GET_TRANSPROT_PRO = "3033";

    /** 订单拨打电话 */
    public static final String ORDER_PHONE = "3035";

    /** 统计订单数量（司机3.4版本） */
    public static final String COUNT_ORDER_NUM = "3037";



    /** 待承运订单分页列表（司机3.4版本） */
    public static final String PAGE_ORDER_WAIT_CARRIER = "3039";

    /** 待装货订单分页列表（司机3.4版本） */
    public static final String PAGE_ORDER_WAIT_LOAD = "3041";

    /** 待卸货订单分页列表（司机3.4版本） */
    public static final String PAGE_ORDER_WAIT_UNLOAD = "3043";

    /** 待评价订单分页列表（司机3.4版本） */
    public static final String PAGE_ORDER_WAIT_EVAL = "3045";

    /** 其他订单分页列表（司机3.4版本） */
    public static final String PAGE_ORDER_WAIT_OTHER = "3047";

    /** 订单详情（司机3.4版本） */
    public static final String ORDER_ALL_DETAILS = "3049";
    /** 订单评价详情（司机3.4版本） */
    public static final String ORDER_EVAL_DETAILS = "3051";

    /** 保存订单评价（司机3.4版本） */
    public static final String SAVE_ORDER_EVAL = "3053";

    /** 搜索订单分页列表（司机3.4版本） */
    public static final String PAGE_SEARCH_ORDER = "3055";

    /** 身份证、驾驶证、行驶证认证*/
    public static final String AUTHENTICATE_ALL_INFO = "3057";

    /** 兑换兑换码 */
    public static final String CONVERT_CODE = "6101";

    /** 积分明细分页列表 */
    public static final String PAGE_INTEGRATION = "6103";

    /** 添加积分 */
    public static final String ADD_INTEGRATION = "6105";


    /** 货源首页轮播图片*/
    public static final String SEQ_ADVERTISEMENT_LOOP_PHOTO = "7001";

}