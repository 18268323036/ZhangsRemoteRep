package com.cy.driver.common.enumer;


import com.cy.driver.common.util.SystemsUtil;

/**
 * Created by wyh on 2016/4/21.
 */
public enum AppOrderState {
    /**
     * 客户端订单状态
     * -3货主提出取消订单（普通订单）/货主提出取消分包商同意（分包订单）/分包商提出取消（分包订单）
     * -2司机提出取消订单（普通订单）/货主提出取消待分包商确认（分包订单）/分包商提出取消司机同意（分包订单）
     * -1订单已取消
     * 1货主已订车、3等待装货、2待发货、4等待卸货、5已卸货、6已完成、7待评价
     */
    HZTCQXDD(-3, "货主提出取消订单", "货主提出取消订单", "冻结", "冻结"),
    SJTCQXDD(-2, "司机提出取消订单", "司机提出取消订单", "冻结", "冻结"),
    QX(-1, "订单已取消", "订单已取消", "订单已取消", "订单已取消"),
    YDC(1, "货主已订车", "货主已订车", "待承运", "待承运"),
    DDZH(3, "等待装货", "等待装货", "待装货", "待装货"),
    DFH(2, "待发货", "待发货", "待发货", "待发货"),
    DDXH(4, "等待卸货", "等待卸货", "待卸货", "待卸货"),
    YXH(5, "已卸货", "已卸货", "待收货", "待收货"),
    YWC(6, "已完成", "已完成", "已完成", "已完成"),
    DPJ(7, "待评价", "待评价", "待评价", "待评价"),
    ;

    private int code;

    private String orderValue1;

    private String disValue1;

    private String orderValue2;

    private String disValue2;

    private AppOrderState(int code, String orderValue1, String disValue1, String orderValue2, String disValue2) {
        this.code = code;
        this.orderValue1 = orderValue1;
        this.disValue1 = disValue1;
        this.orderValue2 = orderValue2;
        this.disValue2 = disValue2;
    }

    public int getCode() {
        return code;
    }

    public String getOrderValue1() {
        return orderValue1;
    }

    public String getDisValue1() {
        return disValue1;
    }

    public String getOrderValue2() {
        return orderValue2;
    }

    public String getDisValue2() {
        return disValue2;
    }


    /**
     * 获得客户端订单状态，司机3.4版本
     * @param serviceOrderCode  服务端订单状态编号
     * @param orderLock 锁定状态
     * @param tradeCancelOrigin 取消来源
     * @param driverAssessIdent 司机是否已评价
     * @return
     * @author wyh
     */
    public static AppOrderState convertAppOrderState(int serviceOrderCode, int orderLock, int tradeCancelOrigin, int driverAssessIdent) {
        if (orderLock == 0) {
            switch (serviceOrderCode) {
                case SystemsUtil.SERVICE_ORDER_DSJQR:
                    /** 待承运 */
                    return YDC;
                case SystemsUtil.SERVICE_ORDER_DSJZH:
                    /** 待司机装货 */
                    return DDZH;
                case SystemsUtil.SERVICE_ORDER_DHZFH:
                    /** 待货主发货 */
                    return DDXH;
                case SystemsUtil.SERVICE_ORDER_DSJXH:
                    /** 待司机卸货 */
                    return DDXH;
                case SystemsUtil.SERVICE_ORDER_DHZSH:
                    /** 待货主收货 */
                    if (driverAssessIdent == 1) {
                        return YWC;
                    } else {
                        return DPJ;
                    }
                case SystemsUtil.SERVICE_ORDER_DDWC:
                    /** 订单完成 */
                    if (driverAssessIdent == 1) {
                        return YWC;
                    } else {
                        return DPJ;
                    }
                case SystemsUtil.SERVICE_ORDER_JYQX:
                    return QX;
                default:
                    return QX;
            }
        } else {
            /** 0 正常订单，1 司机取消，2货主取消，3司机取消后,货主取消 ，4 系统取消、5分包商取消 */
            if (tradeCancelOrigin == 1) {
                return SJTCQXDD;
            } else {
                return HZTCQXDD;
            }
        }
    }
}
