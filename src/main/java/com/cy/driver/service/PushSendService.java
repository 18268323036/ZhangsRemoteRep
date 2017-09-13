package com.cy.driver.service;

import com.cy.driver.domain.push.*;

/**
 * 推送
 * Created by wyh on 2016/1/4.
 */
public interface PushSendService {

    /**
     * 承运通知（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean cytzPushSubCon(PushCYTZ info);

    /**
     * 拒绝承运通知（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean jjcytzPushSubCon(PushCYTZ info);

    /**
     * 承运通知（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean cytzPushOwn(PushCYTZ info);

    /**
     * 拒绝承运通知（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean jjcytzPushOwn(PushCYTZ info);

    /**
     * 装货通知（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean zhtzCommonPushOwn(PushZHTZ info);

    /**
     * 卸货通知（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean xhtzCommonPushOwn(PushZHTZ info);

    /**
     * 装货通知（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean zhtzSubConPushOwn(PushZHTZ info);

    /**
     * 卸货通知（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean xhtzSubConPushOwn(PushZHTZ info);

    /**
     * 收到司机报价
     * @param info
     * @return
     * @author wyh
     */
    boolean sdsjbjPushOwn(PushSdsjbj info);

    /**
     * 司机同意成为合同车
     * @param info
     * @return
     * @author wyh
     */
    boolean sjtycwhtcPushOwn(PushSjhtc info);

    /**
     * 司机拒绝成为合同车
     * @param info
     * @return
     * @author wyh
     */
    boolean sjjjcwhtcPushOwn(PushSjhtc info);

    /**
     * 司机同意合同线路
     * @param info
     * @return
     * @author wyh
     */
    boolean sjtyhtxlPushOwn(PushSjhtxl info);

    /**
     * 司机拒绝合同线路
     * @param info
     * @return
     * @author wyh
     */
    boolean sjjjhtxlPushOwn(PushSjhtxl info);

    /**
     * 货主取消订单，分包商同意司机不同意（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjbtyPushOwn(PushDriver info);

    /**
     * 货主取消订单，分包商和司机都同意（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjtyPushOwn(PushCargo info);

    /**
     * 分包商取消订单，司机不同意（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjbtyPushSubCon(PushDriver info);

    /**
     * 货主取消订单，司机不同意（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjbtyPushOwn2(PushDriver info);

    /**
     * 货主取消订单，司机同意（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjtyPushOwn2(PushCargo info);

    /**
     * 司机申请取消订单（普通订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjqxddPushOwn(PushDriver info);

    /**
     * 司机申请取消订单（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjqxddPushSubCon(PushDriver info);

    /**
     * 承运后分包商取消订单，司机同意（分包订单）
     * @param info
     * @return
     * @author wyh
     */
    boolean sjtyPushOwn(PushSubCon info);

    /**
     * 转单运单提醒承运方接单
     * @param pushBase
     * @return
     * @author nxj
     */
    boolean waybillPushOwn(PushBase pushBase, String driverName);
}
