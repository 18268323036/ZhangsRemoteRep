package com.cy.driver.common.constants;

/**
 * 服务端常量类
 * @author Administrator
 *
 */
public class Constants {

	/**系统短时间格式:  yyyy-MM-dd*/
	public static final String DATE_FORMATE_DAY = "yyyy-MM-dd";
	/**系统短时间格式:  yyyy-MM-dd HH*/
	public static final String DATE_FORMATE_HOUR = "yyyy-MM-dd HH";
	/**系统时间格式:  yyyy-MM-dd HH:mm:ss*/
	public static final String DATE_FORMATE_LONG = "yyyy-MM-dd HH:mm:ss";
	/**系统时间格式:  yyyyMMdd*/
	public static final String DATE_FORMATE_TIME = "yyyyMMdd";
	/**系统时间格式:  Y年m月d日 H时m分s秒*/
	public static final String DATE_FORMATE_CN_LONG = "yyyy年MM月dd日HH时mm分ss秒";
	
	/**    手机系统      **/
	public static final String OS_ANDROID = "android";
	public static final String OS_IOS = "ios";
    public static final String OS_WP = "wp";
    public static final String OS_WXIN = "wxin";
    public static final Byte OS_ANDROID_CODE = 1;
    public static final Byte OS_IOS_CODE = 2;
    public static final Byte OS_WP_CODE = 3;
    public static final Byte OS_WXIN_CODE = 5;
    public static final Integer OS_ANDROID_CODE_INT = 1;
    public static final Integer OS_IOS_CODE_INT = 2;
    public static final Integer OS_WP_CODE_INT = 3;
    public static final Integer OS_OTHER_CODE_INT = 4;
    public static final Integer OS_WXIN_CODE_INT = 5;

    /**货源分享信息前缀*/
    public static final String CARGO_SHARE_PREFIX_CONTENT = "我正在使用快到网手机配货，给你分享一条货源信息：";

    public static final int PAGE_SIZE = 10;//分页大小

	public static final int CARGO_PAGE_SIZE = 10;//货源分页大小

	public static final int NEAR_CARGO_PAGE_SIZE = 10;//附近货源查询

	public static final int MY_QUOTE_LIST = 10;//我的报价列表

    public static final int ACCOUNT_SIZE = 10;//账户余额大小

    public static final int MSG_CENTER_SIZE = 10;//消息中心分页大小

    /** 系统树形码表 码表类型(1车辆类型CL、2车厢类型CX) */
    public static final int SYS_TREE_CODE_VEHICLE = 1;//1车辆类型
    public static final int SYS_TREE_CODE_CARRIAGE = 2;//2车辆类型


	/** 司机工作状态*/
	public static final int WORK_STATUS_Reject= 0;//司机工作状态 拒接
	public static final int WORK_STATUS_Orders = 1;//司机工作状态 接单


    //请求头的key
    //协议类型 1-json,2-tlv
    public static final String REQ_HEAD_PROTOCAL_TYPE = "h1";
    //协议版本，如：3.0
    public static final String REQ_HEAD_PROTOCAL_VERSION = "h2";
    //信息来源，分android，ios，wp,other
    public static final String REQ_HEAD_SOURCE = "h3";
    //设备操作系统版本
    public static final String REQ_HEAD_OS_VERSION = "h4";
    //设备编号
    public static final String REQ_HEAD_IMEI = "h5";
    //SIM卡编号
    public static final String REQ_HEAD_IMSI = "h6";
    //加密算法，如：DES,MD5
    public static final String REQ_HEAD_COMPRESS = "h7";
    //时间戳,格式：yyyyMMddHHmmss如：20150409160033
    public static final String REQ_HEAD_TIME_STAMP = "h8";
    //渠道id
    public static final String REQ_HEAD_CHANNEL_ID = "h9";
    //请求消息流水号,格式：时间戳+六位的随机数，如：20150409160033000001
    public static final String REQ_HEAD_MESSENGER_ID = "h10";
    //版本号,如：2.2.2
    public static final String REQ_HEAD_APK_VERSION = "h11";
    //请求码
    public static final String REQ_HEAD_REQ_CODE = "h12";
    //令牌，登录之后产生token
    public static final String REQ_HEAD_TOKEN = "h13";
    //签名(md5(h3 + ^195Aop! + h10 + @#54Yd + h12)，生成md5转大写)
    public static final String REQ_HEAD_SIGNATURE = "h14";

    /** 系统参数编号 app的位置上传定时的时间(分钟) */
    public static final String APP_LOC_MINUTE_TIME = "APP_LOC_MINUTE_TIME";

    /*系统位置默认坐标值*/
    public static  final String DEFAULT_PROVINCE = "浙江省";
    public static  final String DEFAULT_PROVINCE_CODE = "330000";
    public static final String DEFAULT_CITY = "杭州市";
    public static final String DEFAULT_CITY_CODE = "330100";


    /** 网络电话次数限制 */
    public static final String NETWORKPHONE_NUM_LIMIT = "3";

    /** 甲方名称 */
    public static final String FIRST_PARTY = "建德市快到网物流电商基地有限公司";

    /** 订单性质：1 司机直运订单 2 分包商转包订单 (分包商业务新增) */
    public static final byte TRANSACTION_KIND_COMMON = 1;
    public static final byte TRANSACTION_KIND_SUBCONTRACTOR = 2;

    /** 总车辆费用分润模式：1 全部支付给分包商 2 分包商收现金司机收现金+油卡 3 司机收现金分包商收现金+油卡*/
    public static final byte DISTRIBUTE_MODEL_ALL = 1;
    public static final byte DISTRIBUTE_MODEL_SUBONLYCASH = 2;
    public static final byte DISTRIBUTE_MODEL_DRIVERONLYCASH = 3;


    /** 合同类别(0 货主合同  1 司机合同 2 分包商合同) */
    public static final byte PROTOCOL_DRIVER = 1;
    public static final String PROTOCOL_DRIVER_STR = "SJ";

    /**
     * 0不区分子系统 1 营销平台 2 快到网网站 3 司机app服务端
     * 4 经管系统 5调度系统 6货主app服务端
     */
    public static final int EVENT_FROM3 = 3;

    /** 承运通知（分包订单） */
    public static final int PUSH_USE_FOR100 = 100;

    /** 拒绝承运通知（分包订单） */
    public static final int PUSH_USE_FOR101 = 101;

    /** 承运通知（普通订单） */
    public static final int PUSH_USE_FOR102 = 102;

    /** 拒绝承运通知（普通订单） */
    public static final int PUSH_USE_FOR103 = 103;

    /** 装货通知（普通订单） */
    public static final int PUSH_USE_FOR104 = 104;

    /** 卸货通知（普通订单） */
    public static final int PUSH_USE_FOR105 = 105;

    /** 装货通知（分包订单） */
    public static final int PUSH_USE_FOR106 = 106;

    /** 卸货通知（分包订单） */
    public static final int PUSH_USE_FOR107 = 107;

    /** 收到司机报价 */
    public static final int PUSH_USE_FOR108 = 108;

    /** 司机同意成为合同车 */
    public static final int PUSH_USE_FOR109 = 109;

    /** 司机拒绝成为合同车 */
    public static final int PUSH_USE_FOR110 = 110;

    /** 司机同意合同线路 */
    public static final int PUSH_USE_FOR111 = 111;

    /** 司机拒绝合同线路 */
    public static final int PUSH_USE_FOR112 = 112;

    /** 货主取消订单，分包商同意司机不同意（分包订单） */
    public static final int PUSH_USE_FOR113 = 113;

    /** 货主取消订单，分包商和司机都同意（分包订单） */
    public static final int PUSH_USE_FOR114 = 114;

    /** 分包商取消订单，司机不同意（分包订单） */
    public static final int PUSH_USE_FOR115 = 115;

    /** 货主取消订单，司机不同意（普通订单） */
    public static final int PUSH_USE_FOR116 = 116;

    /** 货主取消订单，司机同意（普通订单） */
    public static final int PUSH_USE_FOR117 = 117;

    /** 司机申请取消订单（普通订单） */
    public static final int PUSH_USE_FOR118 = 118;

    /** 司机申请取消订单（分包订单） */
    public static final int PUSH_USE_FOR119 = 119;

    /** 承运后分包商取消订单，司机同意（分包订单） */
    public static final int PUSH_USE_FOR120 = 120;

    /** 转单运单提醒司机接单（转单运单） */
    public static final int PUSH_USE_FOR121 = 121;

    /** 转单运单提醒司机接单（转单运单） */
    public static final int PUSH_USE_FOR122 = 122;

    /** t_push_send_record.user_type 用户类型（0司机 1企业） */
    public static final byte PUSH_USER_TYPE_DRIVER = 0;
    public static final byte PUSH_USER_TYPE_COMPANY = 1;

    /** APP种类：1 司机版APP 2 货主版APP */
    public static final int APP_KIND_OWNER = 2;

    /** 分享类型：1 APP常规分享 2 2016新春开门红活动 */
    public static final int SHARE_TYPE_APP = 1;

    /** 评价分数 3 好评、6 中评、9差评 */
    public static final int ASSESS_SCORE_GOOD = 3;
    public static final int ASSESS_SCORE_MIDDLE = 6;
    public static final int ASSESS_SCORE_BAD = 9;

    /** 用户类型（0企业 1司机） */
    public static final int OWNER = 0;
    public static final int DRIVER = 1;

    /** 待承运订单查询状态 */
    public static final int WAIT_CARRIER = 1;
    /** 待装货*/
    public static final int WAIT_LOAD = 3;
    /** 待卸货*/
    public static final int WAIT_UNLOAD = 4;
    /** 待评价*/
    public static final int WAIT_EVALUATION = 5;
    /** 其它*/
    public static final int OTHER = 6;

    /** 用户类型（0:司机 1:企业 2:分包商）积分服务 */
    public static final byte AWARD_DRIVER = 0;
    public static final byte AWARD_OWNER = 1;
    public static final byte AWARD_SUB = 2;

    /**积分计算方式*/
    public static final int CHECK_MODE_BY_EVENT = 1;
    public static final int CHECK_MODE_BY_OUTTER = 2;


    /** 认证状态（0未认证 3已认证） */
    public static final byte AUTH_NO = 0;
    public static final byte AUTH_YES = 3;

    /**
     * 埋点位置标识(1查看货源详情、2承运订单、3货源报价、4货源电话联系货主、5搜索货源条件、6空车上报)
     */
    public static final int CARGO_DETAIL = 1;
    public static final int ASSEPT_FOR_CARRIAGE = 2;
    public static final int QUOTE_CARGO = 3;
    public static final int CALL_OWNER = 4;
    public static final int QUERY_CARGO = 5;
    public static final int ADD_EMPTY_CAR = 6;

    /** 用户类型 1快到网司机 2快到网货主 3 区域配送用户 */
    public static final int USER_TYPE_KDWSJ= 1;
    public static final int USER_TYPE_QUPSYH= 3;

    /**图片类型（-1 手机号码图片 1 身份证正面,2 身份证反面,3 驾驶证,4 行驶证,5 营运证,6 头像,7车辆图片）*/
     public static final int MOBILE_IMG= -1;
    public static final int IDCARD_FRONT= 1;
    public static final int IDCARD_BACK= 2;
    public static final int DRIVER_CARD= 3;
    public static final int DRIVER_LICENCE= 4;
    public static final int OPERATION_IMG= 5;
    public static final int HEAD_PORTRAIT_IMG= 6;
    public static final int CAR_IMG= 7;

    /**订单状态 1 待接单,2 待装货,3 待卸货,4 已卸货,5 其他 */
    public static final int ORDER_WAIT= 1;
    public static final int ORDER_WAIT_LOAD= 2;
    public static final int ORDER_WAIT_UNLOAD= 3;
    public static final int ORDER_HAS_UNLOAD= 4;
    public static final int ORDER_OTHER= 5;


    /**订单来源（1来源快到网、2来源云配送、3来源saas系统）*/
    public static final int ORDER_FROM_KUAIDAO = 1;
    public static final int ORDER_FROM_QYPS = 2;
    public static final int ORDER_FROM_SAAS = 3;

    /**转单运单状态 1 待承运方接单,2 承运方已接单 */
    public static final int WAYBILL_ORDER_WAIT= 1;
    public static final int WAYBILL_ORDER_WAIT_LOAD= 2;

}
