package com.cy.driver.common.util;


import com.cy.pass.service.dto.Enum.CarriageType;
import com.cy.pass.service.dto.Enum.PushMsgType;
import com.cy.pass.service.dto.Enum.VehicleType;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/17.
 * 系统util类
 */
public class SystemsUtil {

    public static final int ZERO_MIN = 0;//0 分钟
    public static final int TEN_MIN = 10;//10 分钟
    public static final int HALF_HOUR = 30;//30 分钟
    public static final int ONE_HOUR = 60;// 60 分钟
    public static final int ONE_DAY = 60 * 24;//60*24 分钟

    public static String getTimeStr(Date date) {
        double min = DateUtil.getIntervalMinutes(date, new Date());
        String str = "";
        if (min > ZERO_MIN && min <= TEN_MIN) {
            str = "刚刚 发布";
        } else if (min > TEN_MIN && min <= HALF_HOUR) {
            str = "半小时前 发布";
        } else if (min > HALF_HOUR && min <= ONE_HOUR) {
            str = "1小时前 发布";
        } else if (min > ONE_HOUR && min <= ONE_HOUR * 2) {
            str = "2小时前 发布";
        } else if (min > ONE_HOUR * 2 && min <= ONE_HOUR * 3) {
            str = "3小时前 发布";
        } else if (min > ONE_HOUR * 3 && min <= ONE_HOUR * 4) {
            str = "4小时前 发布";
        } else if (min > ONE_HOUR * 4 && min <= ONE_HOUR * 5) {
            str = "5小时前 发布";
        } else if (min > ONE_HOUR * 5 && min <= ONE_HOUR * 6) {
            str = "6小时前 发布";
        } else if (min > ONE_HOUR * 6 && min <= ONE_HOUR * 7) {
            str = "7小时前 发布";
        } else if (min > ONE_HOUR * 7 && min <= ONE_HOUR * 8) {
            str = "8小时前 发布";
        } else if (min > ONE_HOUR * 8 && min <= ONE_HOUR * 9) {
            str = "9小时前 发布";
        } else if (min > ONE_HOUR * 9 && min <= ONE_HOUR * 10) {
            str = "10小时前 发布";
        } else if (min > ONE_HOUR * 10 && min <= ONE_HOUR * 11) {
            str = "11小时前 发布";
        } else if (min > ONE_HOUR * 11 && min <= ONE_HOUR * 12) {
            str = "12小时前 发布";
        } else if (min > ONE_HOUR * 12 && min <= ONE_HOUR * 13) {
            str = "13小时前 发布";
        } else if (min > ONE_HOUR * 13 && min <= ONE_HOUR * 14) {
            str = "14小时前 发布";
        } else if (min > ONE_HOUR * 14 && min <= ONE_HOUR * 15) {
            str = "15小时前 发布";
        } else if (min > ONE_HOUR * 15 && min <= ONE_HOUR * 16) {
            str = "16小时前 发布";
        } else if (min > ONE_HOUR * 16 && min <= ONE_HOUR * 17) {
            str = "17小时前 发布";
        } else if (min > ONE_HOUR * 17 && min <= ONE_HOUR * 18) {
            str = "18小时前 发布";
        } else if (min > ONE_HOUR * 18 && min <= ONE_HOUR * 19) {
            str = "19小时前 发布";
        } else if (min > ONE_HOUR * 19 && min <= ONE_HOUR * 20) {
            str = "20小时前 发布";
        } else if (min > ONE_HOUR * 20 && min <= ONE_HOUR * 21) {
            str = "21小时前 发布";
        } else if (min > ONE_HOUR * 21 && min <= ONE_HOUR * 22) {
            str = "22小时前 发布";
        } else if (min > ONE_HOUR * 22 && min <= ONE_HOUR * 23) {
            str = "23小时前 发布";
        }else if (min > ONE_HOUR * 23 && min <= ONE_HOUR * 24) {
            str = "24小时前 发布";
        }
        else if (min > ONE_DAY && min <= ONE_DAY * 2) {
            str = "1天前 发布";
        } else if (min > ONE_DAY * 2 && min <= ONE_DAY * 3) {
            str = "2天前 发布";
        } else if (min > ONE_DAY * 3 && min <= ONE_DAY * 4) {
            str = "3天前 发布";
        } else if (min > ONE_DAY * 4 && min <= ONE_DAY * 5) {
            str = "4天前 发布";
        } else if (min > ONE_DAY * 5 && min <= ONE_DAY * 6) {
            str = "5天前 发布";
        } else if (min > ONE_DAY * 6 && min <= ONE_DAY * 7) {
            str = "6天前 发布";
        } else if (min > ONE_DAY * 7) {
            str = "7天前 发布";
        }
        return str;
    }

    public static String quoteUnitConver(Byte s) {
        if (s == null) {
            return "";
        }
        if (s == 1) {
            return "元/车";
        } else if (s == 2) {
            return "元/吨";
        } else if (s == 3) {
            return "元/方";
        }
        return "";
    }

    /**
     * 客户端订单状态
     * -3货主提出取消订单（普通订单）/货主提出取消分包商同意（分包订单）/分包商提出取消（分包订单）
     * -2司机提出取消订单（普通订单）/货主提出取消待分包商确认（分包订单）/分包商提出取消司机同意（分包订单）
     * -1订单已取消
     * 1货主已订车、3等待装货、2待发货、4等待卸货、5已卸货、6已完成、7待评价 ,8已冻结 ,9已转单
     */
    public static final int APP_ORDER_HZTCQXDD = -3;
    public static final int APP_ORDER_SJTCQXDD = -2;
    public static final int APP_ORDER_QX = -1;
    public static final int APP_ORDER_YDC = 1;
    public static final int APP_ORDER_DDZH = 3;
    public static final int APP_ORDER_DFH = 2;
    public static final int APP_ORDER_DDXH = 4;
    public static final int APP_ORDER_YXH = 5;
    public static final int APP_ORDER_YWC = 6;
    public static final int APP_ORDER_DPJ = 7;
    public static final int APP_ORDER_YDJ = 8;
    public static final int APP_ORDER_YZD = 9;

    public static final String APP_ORDER_HZTCQXDD_VALUE = "货主提出取消订单";
    public static final String APP_ORDER_SJTCQXDD_VALUE = "司机提出取消订单";
    public static final String APP_ORDER_QX_VALUE = "订单已取消";
    public static final String APP_ORDER_YDC_VALUE = "货主已订车";
    public static final String APP_ORDER_DDZH_VALUE = "等待装货";
    public static final String APP_ORDER_DFH_VALUE = "待发货";
    public static final String APP_ORDER_DDXH_VALUE = "等待卸货";
    public static final String APP_ORDER_YXH_VALUE = "已卸货";
    public static final String APP_ORDER_YWC_VALUE = "已完成";
    public static final String APP_ORDER_DPJ_VALUE = "待评价";
    public static final String APP_ORDER_YDJ_VALUE = "已冻结";
    public static final String APP_ORDER_YZD_VALUE = "已转单";

    public static final String APP_ORDER_HZTCQXDDFBSYTT_VALUE = "货主提出取消分包商已同意";
    public static final String APP_ORDER_FBSTCQXDDSJYTT_VALUE = "分包商提出取消司机已同意";
    public static final String APP_ORDER_FBSTCQXDD_VALUE = "分包商提出取消订单";

    /**
     *
     * -- 快到网
     * 服务端订单状态：6交易取消、1待司机确认、12待司机装货、11待货主发货、3运输跟踪/待司机卸货、
     * 7司机已卸货/待货主收货、5订单完成
     *
     *
     * -- 云配送
     * 1待司机接单 2待司机装货 3待卸货 4已卸货 5交易取消 6已转单 7已完成 8已冻结
     *
     *
     **/
    public static final int SERVICE_ORDER_JYQX = 6;
    public static final int SERVICE_ORDER_DSJQR = 1;
    public static final int SERVICE_ORDER_DHZFH = 11;
    public static final int SERVICE_ORDER_DSJZH = 12;
    public static final int SERVICE_ORDER_DSJXH = 3;
    public static final int SERVICE_ORDER_DHZSH = 7;
    public static final int SERVICE_ORDER_DDWC = 5;





    /**
     * 获得客户端订单状态编号
     *
     * @param serviceOrderCode  服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @param orderSource 订单来源 1快到网 2云配送
     * @param turnedState 转单状态 0未转单 1已转单
     * @return
     * @author wyh
     */
    public static int findWayBillAppOrderCode(int serviceOrderCode, int orderLock, int tradeCancelOrigin,int orderSource,int turnedState) {
        if(orderSource == 1) {
            return findAppOrderCode(serviceOrderCode,orderLock,tradeCancelOrigin);
        }else {
            if(turnedState == 1) {
                return APP_ORDER_YZD;
            }else {
                if (orderLock == 0) {
                    switch (serviceOrderCode) {
                        case SERVICE_ORDER_JYQX:
                            return APP_ORDER_QX;
                        case SERVICE_ORDER_DSJQR:
                            return APP_ORDER_YDC;
                        case SERVICE_ORDER_DSJZH:
                            return APP_ORDER_DDZH;
                        case SERVICE_ORDER_DSJXH:
                            return APP_ORDER_DDXH;
                        case SERVICE_ORDER_DHZSH:
                            return APP_ORDER_YXH;
                        case SERVICE_ORDER_DDWC:
                            return APP_ORDER_YWC;
                        case SERVICE_ORDER_DHZFH:
                            return APP_ORDER_DFH;
                        default:
                            return APP_ORDER_QX;
                    }
                } else {
                    return APP_ORDER_YDJ;
                }
            }
        }

    }


    /**
     * 获得客户端订单状态编号名称
     *
     * @param serviceOrderCode 服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @return
     * @author wyh
     */
    public static String findWaybillAppOrderValue(int serviceOrderCode, int orderLock, int tradeCancelOrigin,int orderSource,int turnedState) {
        if(orderSource == 1) {
            return findAppOrderValue(serviceOrderCode,orderLock,tradeCancelOrigin);
        }else {
            if(turnedState == 1) {
                return APP_ORDER_YZD_VALUE;
            }else {
                if (orderLock == 0) {
                    switch (serviceOrderCode) {
                        case SERVICE_ORDER_JYQX:
                            return APP_ORDER_QX_VALUE;
                        case SERVICE_ORDER_DSJQR:
                            return APP_ORDER_YDC_VALUE;
                        case SERVICE_ORDER_DSJZH:
                            return APP_ORDER_DDZH_VALUE;
                        case SERVICE_ORDER_DSJXH:
                            return APP_ORDER_DDXH_VALUE;
                        case SERVICE_ORDER_DHZSH:
                            return APP_ORDER_YXH_VALUE;
                        case SERVICE_ORDER_DDWC:
                            return APP_ORDER_YWC_VALUE;
                        case SERVICE_ORDER_DHZFH:
                            return APP_ORDER_DFH_VALUE;
                        default:
                            return APP_ORDER_QX_VALUE;
                    }
                } else {
                    return APP_ORDER_YDJ_VALUE;
                }
            }
        }
    }





    /**
     * 获得客户端订单状态编号
     *
     * @param serviceOrderCode  服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @return
     * @author wyh
     */
    public static int findAppOrderCode(int serviceOrderCode, int orderLock, int tradeCancelOrigin) {
        if (orderLock == 0) {
            switch (serviceOrderCode) {
                case SERVICE_ORDER_JYQX:
                    return APP_ORDER_QX;
                case SERVICE_ORDER_DSJQR:
                    return APP_ORDER_YDC;
                case SERVICE_ORDER_DSJZH:
                    return APP_ORDER_DDZH;
                case SERVICE_ORDER_DSJXH:
                    return APP_ORDER_DDXH;
                case SERVICE_ORDER_DHZSH:
                    return APP_ORDER_YXH;
                case SERVICE_ORDER_DDWC:
                    return APP_ORDER_YWC;
                case SERVICE_ORDER_DHZFH:
                    return APP_ORDER_DDXH;
                default:
                    return APP_ORDER_QX;
            }
        } else {
            if (tradeCancelOrigin == 2 || tradeCancelOrigin == 5) {
                return APP_ORDER_HZTCQXDD;
            } else {
                return APP_ORDER_SJTCQXDD;
            }
        }
    }

    /**
     * 获得客户端订单状态编号名称
     *
     * @param serviceOrderCode 服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @return
     * @author wyh
     */
    public static String findAppOrderValue(int serviceOrderCode, int orderLock, int tradeCancelOrigin) {
        if (orderLock == 0) {
            switch (serviceOrderCode) {
                case SERVICE_ORDER_JYQX:
                    return APP_ORDER_QX_VALUE;
                case SERVICE_ORDER_DSJQR:
                    return APP_ORDER_YDC_VALUE;
                case SERVICE_ORDER_DSJZH:
                    return APP_ORDER_DDZH_VALUE;
                case SERVICE_ORDER_DSJXH:
                    return APP_ORDER_DDXH_VALUE;
                case SERVICE_ORDER_DHZSH:
                    return APP_ORDER_YXH_VALUE;
                case SERVICE_ORDER_DDWC:
                    return APP_ORDER_YWC_VALUE;
                case SERVICE_ORDER_DHZFH:
                    return APP_ORDER_DFH_VALUE;
                default:
                    return APP_ORDER_QX_VALUE;
            }
        } else {
            if (tradeCancelOrigin == 2) {
                return APP_ORDER_HZTCQXDD_VALUE;
            } else {
                return APP_ORDER_SJTCQXDD_VALUE;
            }
        }
    }

    /**
     * 获得客户端分包订单状态编号
     *
     * @param serviceOrderCode  服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @param cancelStep 取消步骤（0无、1到司机审核、2到企业审核、5到分包商审核）
     * @return
     * @author wyh
     */
    public static int findAppDistributeOrderCode(int serviceOrderCode, int orderLock, int tradeCancelOrigin, int cancelStep) {
        if (orderLock == 0) {
            switch (serviceOrderCode) {
                case SERVICE_ORDER_JYQX:
                    return APP_ORDER_QX;
                case SERVICE_ORDER_DSJQR:
                    return APP_ORDER_YDC;
                case SERVICE_ORDER_DSJZH:
                    return APP_ORDER_DDZH;
                case SERVICE_ORDER_DSJXH:
                    return APP_ORDER_DDXH;
                case SERVICE_ORDER_DHZSH:
                    return APP_ORDER_YXH;
                case SERVICE_ORDER_DDWC:
                    return APP_ORDER_YWC;
                case SERVICE_ORDER_DHZFH:
                    return APP_ORDER_DFH;
                default:
                    return APP_ORDER_QX;
            }
        } else {
            /** tradeCancelOrigin 1 司机取消，2货主取消，5分包商取消 */
            /** cancelStep 1到司机审核、2到企业审核、5到分包商审核 */
            if (tradeCancelOrigin == 1) {
                return APP_ORDER_SJTCQXDD;
            } else {
                if (cancelStep == 1) {
                    return APP_ORDER_HZTCQXDD;
                } else {
                    return APP_ORDER_SJTCQXDD;
                }
            }
        }
    }



    /**
     * 获得客户端分包订单状态编号名称
     * @param serviceOrderCode 服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @param cancelStep 取消步骤（0无、1到司机审核、2到企业审核、5到分包商审核）
     * @return
     * @author wyh
     */
    public static String findAppDistributeOrderValue(int serviceOrderCode, int orderLock, int tradeCancelOrigin, int cancelStep) {
        if (orderLock == 0) {
            switch (serviceOrderCode) {
                case SERVICE_ORDER_JYQX:
                    return APP_ORDER_QX_VALUE;
                case SERVICE_ORDER_DSJQR:
                    return APP_ORDER_YDC_VALUE;
                case SERVICE_ORDER_DSJZH:
                    return APP_ORDER_DDZH_VALUE;
                case SERVICE_ORDER_DSJXH:
                    return APP_ORDER_DDXH_VALUE;
                case SERVICE_ORDER_DHZSH:
                    return APP_ORDER_YXH_VALUE;
                case SERVICE_ORDER_DDWC:
                    return APP_ORDER_YWC_VALUE;
                case SERVICE_ORDER_DHZFH:
                    return APP_ORDER_DFH_VALUE;
                default:
                    return APP_ORDER_QX_VALUE;
            }
        } else {
            /** tradeCancelOrigin 1 司机取消，2货主取消，5分包商取消 */
            /** cancelStep 1到司机审核、2到企业审核、5到分包商审核 */
            if (tradeCancelOrigin == 2) {
                switch (serviceOrderCode) {
                    case SERVICE_ORDER_DSJZH:
                        if (cancelStep == 1) {
                            return APP_ORDER_HZTCQXDDFBSYTT_VALUE;
                        } else {
                            return APP_ORDER_HZTCQXDD_VALUE;
                        }
                    default:
                        return APP_ORDER_HZTCQXDD_VALUE;
                }
            } else if (tradeCancelOrigin == 5) {
                switch (serviceOrderCode) {
                    case SERVICE_ORDER_DSJZH:
                        if (cancelStep == 1) {
                            return APP_ORDER_FBSTCQXDD_VALUE;
                        } else {
                            return APP_ORDER_FBSTCQXDDSJYTT_VALUE;
                        }
                    default:
                        return APP_ORDER_FBSTCQXDD_VALUE;
                }
            } else {
                return APP_ORDER_SJTCQXDD_VALUE;
            }
        }
    }

    /**
     * 获得省市县  如果详细地址不为空，则获取后面
     *@param detailAddress 详细地址
     * @param province 省名
     * @param city     市名
     * @param county   县名
     * @return 获得地址
     */
    public static String buildAddress(String detailAddress, String province, String city, String county) {
        if(detailAddress==null||"".equals(detailAddress)){
            String address = "";
            if (StringUtils.isNotBlank(province)) {
                address += province;
            }
            if (StringUtils.isNotBlank(city)) {
                address += city;
            }
            if (StringUtils.isNotBlank(county)) {
                address += county;
            }
            return municipalitiesTorepeat(address);
        }
        return detailAddress;
    }


    /**
     * 拼接省市区详细地址
     * @param detailAddr
     * @param province
     * @param city
     * @param county
     * @param split
     * @return
     */
    public static String buildAllAddress(String detailAddr, String province, String city, String county, String split){
        String address = "";
        if (split == null)
            split = "";
        if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(province)) {
            address += province + split;
        }
        if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(city)) {
            address += city + split;
        }
        if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(county)) {
            address += county + split;
        }
        if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(detailAddr)) {
            address += detailAddr + split;
        }
        if (!"".equals(split) && !"".equals(address)) {
            address = address.substring(0, address.length()-1);
        }
        return cityRepeat(address, split);
    }


    public static String doubleConStr(Double num){
        if(num==null){
            return "0";
        }
        return String.valueOf(num);
    }

    /**
     * 直辖市去重
     * @param address
     * @return
     */
    public static String cityRepeat(String address, String split) {
        if(address==null){
            return "";
        }
        int length = split.length();
        if(address.indexOf("北京市"+split+"北京市")!=-1){
            return address.substring(3+length, address.length());
        }
        else if(address.indexOf("天津市"+split+"天津市")!=-1){
            return address.substring(3+length,address.length());
        }
        else if(address.indexOf("上海市"+split+"上海市")!=-1){
            return address.substring(3+length,address.length());
        }
        else if(address.indexOf("重庆市"+split+"重庆市")!=-1){
            return address.substring(3+length,address.length());
        }
        return address;
    }

    /**
     * 获得省市县
     *
     * @param province 省名
     * @param city     市名
     * @param county   县名
     * @return 获得省市县
     */
    public static String buildAddress(String province, String city, String county) {
        String address = "";
        if (StringUtils.isNotBlank(province)) {
            address += province;
        }
        if (StringUtils.isNotBlank(city)) {
            address += city;
        }
        if (StringUtils.isNotBlank(county)) {
            address += county;
        }
        return municipalitiesTorepeat(address);
    }

    /**
     * 转换成带单位的金额
     *
     * @param amount 金额
     * @return
     */
    public static String buildAmountUnit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 1) {
            return "面议";
        } else {
            return amount.toString() + "元";
        }
    }

    /**
     * 转换成带单位的重量
     *
     * @param weight 重量
     * @return
     */
    public static String buildWeightUnit(BigDecimal weight) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 1) {
            return "";
        } else {
            return subZeroAndDot(weight) + "吨";
        }
    }


    /**
     * 转换成带单位的件
     *
     * @param cargoTotalQuantity 件
     * @return
     */
    public static String buildQuantity(Integer cargoTotalQuantity) {
        if (cargoTotalQuantity == null || cargoTotalQuantity.intValue() < 0) {
            return "";
        } else {
            return subZeroAndDot(cargoTotalQuantity) + "件";
        }
    }


    /**
     * 转换成带单位的重量
     *
     * @param weight 重量
     * @return
     */
    public static String buildWeightUnit(Double weight) {
        if (weight == null || BigDecimal.valueOf(weight).compareTo(BigDecimal.ZERO) < 1) {
            return "";
        } else {
            return subZeroAndDot(weight) + "吨";
        }
    }

    /**
     * 千克转换为吨
     */
    public static Double kgToTonne(Double weight){
        if(weight==null){
            return (double)0;
        }else{
            return weight/1000;
        }
    }

    /**
     * 转换成带单位的体积
     *
     * @param volume 体积
     * @return
     */
    public static String buildVolumeUnit(BigDecimal volume) {
        if (volume == null || volume.compareTo(BigDecimal.ZERO) < 1) {
            return "";
        } else {
            return subZeroAndDot(volume) + "方";
        }
    }

    /**
     * 转换成带单位的体积
     *
     * @param volume 体积
     * @return
     */
    public static String buildVolumeUnit(Double volume) {
        if (volume == null || BigDecimal.valueOf(volume).compareTo(BigDecimal.ZERO) < 1) {
            return "";
        } else {
            return subZeroAndDot(volume) + "方";
        }
    }

    /**
     * 转换成带单位的长度
     *
     * @param len 长度
     * @return
     */
    public static String buildCarLenUnit(BigDecimal len) {
        if (len == null || len.compareTo(BigDecimal.ZERO) < 1) {
            return "";
        } else {
            return subZeroAndDot(len) + "米";
        }
    }

    /**
     * 转换成带单位的长度
     *
     * @param len 长度
     * @return
     */
    public static String buildCarLenUnit(Double len) {
        if (len == null || BigDecimal.valueOf(len).compareTo(BigDecimal.ZERO) < 1) {
            return "";
        } else {
            return subZeroAndDot(len) + "米";
        }
    }

    /**
     * 获得带单位的报价
     *
     * @param quote 报价金额
     * @return
     */
    public static String buildQuoteUnit(BigDecimal quote, Byte quoteType) {
        String quoteName = "";
        if (!(quote == null || quote.compareTo(BigDecimal.ZERO) < 1)) {
            String quoteUnit = "";
            if (quoteType != null) {
                //1整车报价、2重量报价、3体积报价(元/车、元/吨、元/方)
                switch (quoteType.intValue()) {
                    case 1:
                        quoteUnit = "元/车";
                        break;
                    case 2:
                        quoteUnit = "元/吨";
                        break;
                    case 3:
                        quoteUnit = "元/方";
                        break;
                }
            }
            quoteName = quote.toString() + quoteUnit;
        }
        return quoteName;
    }

    /**
     * 直辖市去重
     *
     * @param municipalities
     * @return
     */
    public static String municipalitiesTorepeat(String municipalities) {
        if(municipalities==null){
            return "";
        }
        if(municipalities.indexOf("北京市北京市")!=-1){
            return municipalities.substring(3,municipalities.length());
        }
        else if(municipalities.indexOf("天津市天津市")!=-1){
            return municipalities.substring(3,municipalities.length());
        }
        else if(municipalities.indexOf("上海市上海市")!=-1){
            return municipalities.substring(3,municipalities.length());
        }
        else if(municipalities.indexOf("重庆市重庆市")!=-1){
            return municipalities.substring(3,municipalities.length());
        }
        return municipalities;
    }

    public static String subZeroAndDot(Object obj){
        if (obj == null) {
            return "";
        }
        String s = obj.toString();
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 总运费如果没有转为面议
     * @param bigDecimal
     * @return
     */
    public static String getTotalFare(BigDecimal bigDecimal){
        if(bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) < 1 )
        {
            return "面议";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(bigDecimal)+"元";
    }

    /**
     * double转换成没有单位的string
     */
    public static String getDoubleToString(Double number){
        if(number==null || number.intValue()<0){
            return "0";
        }
        return String.valueOf(number);
    }


    /**
     * 现金支付、油卡支付费用如果没有返回0.00
     * @param fare
     */
    public static String getFare(BigDecimal fare){
        if(fare==null){
            return "0";
        }else{
            return String.valueOf(fare);
        }
    }

    /**
     * 货源重量转换(带单位)
     * @param cargoWeight 货源重量
     * @return
     * @author wyh
     */
    public static String buildCargoWeightIncludeUnit(BigDecimal cargoWeight){
        if (cargoWeight == null || cargoWeight.compareTo(BigDecimal.ZERO) <= 0)
            return "";
        return cargoWeight.toString()+" 吨";
    }

    /**
     * 货源体积转换(带单位)
     * @param cargoCubage 货源体积
     * @return
     * @author wyh
     */
    public static String buildCargoCubageIncludeUnit(BigDecimal cargoCubage){
        if (cargoCubage == null || cargoCubage.compareTo(BigDecimal.ZERO) <= 0)
            return "";
        return cargoCubage.toString()+" 方";
    }

    /**
     * 获取没有负号的小数
     * @param fare
     * @return
     */
    public static String getPlusNumber(BigDecimal fare){
        if(fare!=null) {
            if (fare.intValue() >= 0) {
                return String.valueOf(fare);
            } else {
                String strFare = String.valueOf(fare);
                return strFare.replace("-","");
            }
        }else{
            return "0.00";
        }
    }

    public static void main(String[] args) {
        System.out.println(getTotalFare(new BigDecimal("10.0")));
    }


    /**
     * 获取网络电话状态
     * @param str
     * @return
     */
    public static String getnetworkPhoneSta(String str){
        if(StringUtils.isEmpty(str)|| "0".equals(str))
        {
            return "0";
        }
        if(Long.valueOf(str)>3){
            return "1";
        }
        return "0";
    }

    /**
     * 运输协议是否现实  1签署 0 不签署
     * @param bigDecimal
     * @return
     */
    public static String isSignProtocol(BigDecimal bigDecimal){
        if(bigDecimal == null || "".equals(bigDecimal.toString())|| bigDecimal.toString().equals("0.00")|| bigDecimal.toString().equals("0"))
        {
            return "1";
        }
        return "0";
    }

    /**
     * 确定收款按钮是否显示 1显示 0不显示
     * @param bigDecimal
     * @return
     */
    public static String isShowProceeds(BigDecimal bigDecimal){
        if(bigDecimal == null || "".equals(bigDecimal.toString())|| bigDecimal.toString().equals("0.00")|| bigDecimal.toString().equals("0"))
        {
            return "0";
        }
        return "1";
    }

    /**
     * 显示到app上金额转换
     * @param bigDecimal
     * @return
     */
    public static String showAppMoney(BigDecimal bigDecimal){
        if(bigDecimal == null || "".equals(bigDecimal.toString())|| bigDecimal.toString().equals("0.00")|| bigDecimal.toString().equals("0"))
        {
            return "面议";
        }
        return bigDecimal+"元";
    }



    /**
     * 隐藏银行卡
     * @param bankCard 银行卡号
     * @return
     */
    public static String hideBankCard(String bankCard){
        if(bankCard == null || "".equals(bankCard)){
            return "尾号";
        }
        String bankCardstr =  bankCard.replace(" ", "");
        return "尾号"+bankCardstr.substring(bankCardstr.length()-4,bankCardstr.length());
    }


    /**
     * 支付业务行为
     * @param bt 交易行为
     * @return
     */
    public static String payBusinessKind(Integer bt){
        if(bt ==null || bt.intValue() == 0)
        {
            return "";
        }
        if(1000 == bt.intValue())
        {
            return "提现失败资金退回";
        }
        else if(2000 == bt.intValue())
        {
            return "充值";
        }
        else if(2001 == bt.intValue()) {
            return "收到运费";
        }
        else if(6000 == bt.intValue())
        {
            return "提现";
        }
        else if(6001 == bt.intValue())
        {
            return "支付运费";
        }
        else if(6002 == bt.intValue())
        {
            return "支付服务费";
        }
        return "";
    }

    /**
     * 帐户明细的收入还是支出
     * @param bt 交易行为
     * @return
     */
    public static String trading(Byte bt){
        if(bt == null)
        {
            return "";
        }
        if(bt.intValue() == 1){
            return "1";//支出
        }else if(bt.intValue() == 2){
            return "0";//支入
        }
        return "";
    }

    /**
     * 发货时间 方法
     * 1.之前格式为:yyyy-MM-dd HH:hh  改为：yyyy-MM-dd (胡老板说的)
     * @param time
     * @return
     */
    public static String cargoOrderStartTime(Date time){
        if(time == null){
            return "";
        }
        return DateUtil.dateFormat(time, DateUtil.F_DATE);
    }

    /**
     * 卸货时间 方法
     * 1.之前格式为:yyyy-MM-dd HH:hh  改为：yyyy-MM-dd (胡老板说的)
     * @param time
     * @return
     */
    public static String cargoOrderEndTime(Date time){
        return cargoOrderStartTime(time);
    }


    /**
     * 消息类型文本 方法
     *  @param pushKind 推送类型
     * @return
     */
    public static String msgTypeVal(Byte pushKind){
        if(pushKind == null ){
            return "";
        }
        String str = PushMsgType.contain((byte)pushKind ,PushMsgType.values());
        if(StringUtils.isEmpty(str))
        {
            return "";
        }
        return str;
    }

    /**
     * 是否同意取消订单
     *  @param auditState -1不同意取消(对应底层服务5)、1同意取消(对应底层服务6)
     * @return
     */
    public static Integer buildAuditCancelOrder(Integer auditState){
        if(auditState.intValue() == -1){
            return 5;
        }
        else if(auditState.intValue() == 1){
            return 6;
        }
        return null;
    }


    /**
     * BigDecimal 相加 b1+b2
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal bigAdd(BigDecimal b1, BigDecimal b2){
        if(b1==null && b2==null){
            return new BigDecimal("0.00");
        }
        if(b1 == null){
            return new BigDecimal("0.00").add(b2);
        }
        if( b2 == null){
            return b1.add(new BigDecimal("0.00"));
        }
        return b1.add(b2);
    }


    /**
     * BigDecimal 相加(b1+b2)之后 转为str
     * @param b1
     * @param b2
     * @return
     */
    public static String bigAddToStr(BigDecimal b1, BigDecimal b2){
        if(b1==null && b2==null){
            return "0.00";
        }
        if(b1 == null){
            return String.valueOf(new BigDecimal("0").add(b2));
        }
        if( b2 == null){
            return String.valueOf(b1.add(new BigDecimal("0")));
        }
        return String.valueOf(b1.add(b2));
    }

    /**
     * BigDecimal 相减(b1-b2)之后  转为str
     * @param b1
     * @param b2
     * @return
     */
    public static String bigSubtractToStr(BigDecimal b1, BigDecimal b2){
        if(b1==null && b2==null){
            return "0";
        }
        if(b1 == null){
            return String.valueOf(new BigDecimal("0").subtract(b2));
        }
        if(b2 == null){ //b2 为 null  直接返回b1
            return String.valueOf(b1);
        }
        return String.valueOf(b1.subtract(b2));
    }

    /**
     * BigDecimal 相减 b1-b2
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal bigSubtract(BigDecimal b1, BigDecimal b2){
        if(b1==null && b2==null){
            return new BigDecimal(0);
        }
        if(b1 == null){
            return new BigDecimal("0").subtract(b2);
        }
        if(b2 == null){ //b2 为 null  直接返回b1
            return b1;
        }
        return b1.subtract(b2);
    }

    public static BigDecimal getFareBigDecimal(String b1){
        if(b1 == null || b1.equals("")){
            return new BigDecimal("0");
        }else{
            return new BigDecimal(b1);
        }
    }

    public static BigDecimal getFareBigDecimal(BigDecimal b1){
        if(b1 == null){
            return new BigDecimal("0");
        }else{
            return b1;
        }
    }

    /**
     * 车辆类型编号转换成名称
     * @param vehicleType 车辆类型编号
     * @return
     * @author wyh
     */
    public static String buildVehicleTypeValue(String vehicleType){
        if (com.alibaba.dubbo.common.utils.StringUtils.isBlank(vehicleType))
            return "";
        if (VehicleType.COMMON.getCode().equals(vehicleType))
            return VehicleType.COMMON.getName();
        if (VehicleType.SEMI_TRAILER.getCode().equals(vehicleType))
            return VehicleType.SEMI_TRAILER.getName();
        if (VehicleType.FULL_TRAILER.getCode().equals(vehicleType))
            return VehicleType.FULL_TRAILER.getName();
        if (VehicleType.SPECIAL.getCode().equals(vehicleType))
            return VehicleType.SPECIAL.getName();
        if (VehicleType.SPECIAL_DANGEROUS_GOODS.getCode().equals(vehicleType))
            return VehicleType.SPECIAL_DANGEROUS_GOODS.getName();
        if (VehicleType.SPECIAL_COLD_STORAGE.getCode().equals(vehicleType))
            return VehicleType.SPECIAL_COLD_STORAGE.getName();
        if (VehicleType.SPECIAL_PROJECT.getCode().equals(vehicleType))
            return VehicleType.SPECIAL_PROJECT.getName();
        return "";
    }

    /**
     * 车厢类型编号转换成名称
     * @param carriageType 车厢类型编号
     * @return
     * @author wyh
     */
    public static String buildCarriageTypeValue(String carriageType){
        if (com.alibaba.dubbo.common.utils.StringUtils.isBlank(carriageType))
            return "";
        if (CarriageType.TAILGATE.getCode().equals(carriageType))
            return CarriageType.TAILGATE.getName();
        if (CarriageType.TAILGATE_FLAT.getCode().equals(carriageType))
            return CarriageType.TAILGATE_FLAT.getName();
        if (CarriageType.TAILGATE_HIGH_LOW_PLATE.getCode().equals(carriageType))
            return CarriageType.TAILGATE_HIGH_LOW_PLATE.getName();
        if (CarriageType.TAILGATE_LOW_HURDLES.getCode().equals(carriageType))
            return CarriageType.TAILGATE_LOW_HURDLES.getName();
        if (CarriageType.TAILGATE_WAREHOUSE_GATE.getCode().equals(carriageType))
            return CarriageType.TAILGATE_WAREHOUSE_GATE.getName();
        if (CarriageType.VAN.getCode().equals(carriageType))
            return CarriageType.VAN.getName();
        if (CarriageType.VAN_COMMON.getCode().equals(carriageType))
            return CarriageType.VAN_COMMON.getName();
        if (CarriageType.VAN_CONTAINER.getCode().equals(carriageType))
            return CarriageType.VAN_CONTAINER.getName();
        if (CarriageType.SPECIAL.getCode().equals(carriageType))
            return CarriageType.SPECIAL.getName();
        if (CarriageType.SPECIAL_FREE_BOX.getCode().equals(carriageType))
            return CarriageType.SPECIAL_FREE_BOX.getName();
        if (CarriageType.SPECIAL_DEFORMED_CAR.getCode().equals(carriageType))
            return CarriageType.SPECIAL_DEFORMED_CAR.getName();
        if (CarriageType.SPECIAL_DUMP_TRUCK.getCode().equals(carriageType))
            return CarriageType.SPECIAL_DUMP_TRUCK.getName();
        return "";
    }

    /**
     * 查询资金明细的类型转换
     * @param businessKind 明细类型（0全部、1提现/余额转出、2充值、
     *                     3支付运费、4支付信息费、5支付服务费、
     *                     6余额转出失败资金回退/提现失败资金回退、
     *                     7分包商收到信息费、8分包商收到运费）
     * @return 1000 提现失败资金退回 1001 运营活动奖励 2000 充值
     * 2001 司机收到运费 2002 分包商收到信息费 2003 分包商收到运费
     * 6000 提现 6001 支付运费 6002 支付服务费 6003 支付信息费
     * @author wyh
     */
    public static Integer buildQueryAccountDetail(Integer businessKind){
        if (businessKind == null) {
            return null;
        }
        if (businessKind.intValue() == 0) {
            return 1000;
        } else if (businessKind.intValue() == 1) {
            return 1001;
        } else if (businessKind.intValue() == 2) {
            return 2000;
        } else if (businessKind.intValue() == 3) {
            return 2001;
        } else if (businessKind.intValue() == 4) {
            return 2002;
        } else if (businessKind.intValue() == 5) {
            return 2003;
        } else if (businessKind.intValue() == 6) {
            return 5999;
        } else if (businessKind.intValue() == 7) {
            return 6000;
        } else if (businessKind.intValue() == 8) {
            return 6001;
        }else if (businessKind.intValue() == 9) {
            return 6002;
        }else if (businessKind.intValue() == 10) {
            return 6003;
        }else if (businessKind.intValue() == 11) {
            return 6004;
        }else if (businessKind.intValue() == 12) {
            return 6005;
        }else if (businessKind.intValue() == 13) {
            return 6006;
        }else if (businessKind.intValue() == 14) {
            return 6007;
        }
        return null;
    }

    /**
     * 隐藏手机号码
     * @return false:不是; true:是
     * */
    public static String hideMobile(String mobile){
        if(StringUtils.isEmpty(mobile))
        {
            return "";
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

//    public static void main(String[] args) {
//
////             System.out.println(hideBankCard("34789 207930 890433 421432"));
//        Date date = DateUtil.parseDate("2015-09-20 10:32:53",DateUtil.F_DATETIME);
//        System.out.println(getTimeStr(date));
//
//    }
}
