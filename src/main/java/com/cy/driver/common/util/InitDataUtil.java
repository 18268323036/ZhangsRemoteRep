package com.cy.driver.common.util;

import com.cy.driver.domain.DictInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanst 2016/4/19 9:36
 */
public class InitDataUtil {

    /**
     * 支付业务行为
     * @return
     */
    public static List<DictInfo> businessKindTypeData() {
        List<DictInfo> list = new ArrayList<DictInfo>();
//        list.add(new DictInfo("", "请选择"));
//        list.add(new DictInfo("1000", "提现失败资金退回"));
//        list.add(new DictInfo("2000", "充值"));
//        list.add(new DictInfo("2001", "收到运费"));
//        list.add(new DictInfo("6000", "提现"));
//        list.add(new DictInfo("6001", "支付运费"));
//        list.add(new DictInfo("6002", "支付服务费"));

        list.add(new DictInfo("", "全部", 3));
        list.add(new DictInfo("0", "提现失败资金退回", 3));
        list.add(new DictInfo("1", "系统红包奖励", 3));
        list.add(new DictInfo("2", "充值", 3));
        list.add(new DictInfo("3", "司机收到运费", 3));
        list.add(new DictInfo("7", "提现", 3));
//        list.add(new DictInfo("11", "身份验证费", 3));
//        list.add(new DictInfo("12", "保险费", 3));
        return list;
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
            return null;
        } else if (businessKind.intValue() == 1) {
            return 6000;
        } else if (businessKind.intValue() == 2) {
            return 2000;
        } else if (businessKind.intValue() == 3) {
            return 6001;
        } else if (businessKind.intValue() == 4) {
            return 6003;
        } else if (businessKind.intValue() == 5) {
            return 6002;
        } else if (businessKind.intValue() == 6) {
            return 1000;
        } else if (businessKind.intValue() == 7) {
            return 2002;
        } else if (businessKind.intValue() == 8) {
            return 2003;
        }else if (businessKind.intValue() == 9) {
            return 2001;
        }else if (businessKind.intValue() == 10) {
            return 1001;
        }
        return null;
    }

        /**
         * 数据字典-车辆-车长
         *
         * @author zdy 20151119
         */
    public static List<DictInfo> carLengthData() {
        List<DictInfo> list = new ArrayList<DictInfo>();
//        list.add(new DictInfo("0", "车长"));
        list.add(new DictInfo("3.9", "4M以下"));
        list.add(new DictInfo("4.2", "4.2M"));
        list.add(new DictInfo("4.3", "4.3M"));
        list.add(new DictInfo("4.5", "4.5M"));
        list.add(new DictInfo("4.8", "4.8M"));
        list.add(new DictInfo("5", "5M"));
        list.add(new DictInfo("5.2", "5.2M"));
        list.add(new DictInfo("5.8", "5.8M"));
        list.add(new DictInfo("6", "6M"));
        list.add(new DictInfo("6.2", "6.2M"));
        list.add(new DictInfo("6.3", "6.3M"));
        list.add(new DictInfo("6.5", "6.5M"));
        list.add(new DictInfo("6.8", "6.8M"));
        list.add(new DictInfo("7", "7M"));
        list.add(new DictInfo("7.2", "7.2M"));
        list.add(new DictInfo("7.4", "7.4M"));
        list.add(new DictInfo("7.5", "7.5M"));
        list.add(new DictInfo("7.7", "7.7M"));
        list.add(new DictInfo("7.8", "7.8M"));
        list.add(new DictInfo("8", "8M"));
        list.add(new DictInfo("8.6", "8.6M"));
        list.add(new DictInfo("8.7", "8.7M"));
        list.add(new DictInfo("8.8", "8.8M"));
        list.add(new DictInfo("9", "9M"));
        list.add(new DictInfo("9.6", "9.6M"));
        list.add(new DictInfo("9.8", "9.8M"));
        list.add(new DictInfo("10", "10M"));
        list.add(new DictInfo("10.5", "10.5M"));
        list.add(new DictInfo("12", "12M"));
        list.add(new DictInfo("12.5", "12.5M"));
        list.add(new DictInfo("13", "13M"));
        list.add(new DictInfo("13.5", "13.5M"));
        list.add(new DictInfo("14", "14M"));
        list.add(new DictInfo("15", "15M"));
        list.add(new DictInfo("16", "16M"));
        list.add(new DictInfo("16.5", "16.5M"));
        list.add(new DictInfo("17", "17M"));
        list.add(new DictInfo("17.5", "17.5M"));
        list.add(new DictInfo("18", "18M"));
        list.add(new DictInfo("19", "19M"));
        list.add(new DictInfo("20", "20M"));
        list.add(new DictInfo("21.1", "20M以上"));
        return list;
    }

    public static String getCarLengthValue(BigDecimal carLength){
        if(carLength==null){
            return "";
        }
        if(carLength.compareTo(new BigDecimal("3.9")) <= 0){
            return "4M以下";
        }else if(carLength.compareTo(new BigDecimal("21.1")) >= 0){
            return "20M以上";
        }else{
            return carLength+"M";
        }
    }

}
