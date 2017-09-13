package com.cy.driver.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanst on 2015/12/1.
 * 推送模版
 * 数组格式为： index0 ==>标题  index1 ==>内容  index2 ==>跳转类型 index3==》交易类型 （1 通知 2 交易）
 */
public class PushSendTemplate {
    /**
     * 跳转方式
     *
     * -- 推送给货主
     * 1 url地址跳转
     * 100 跳转app首页
     * 1000 找车列表
     * 1001 普通车辆详情(tarId=driverId)
     * 1100 认证页面(选择认证的页面)
     * 1101 认证详情页面(tarId=accountType{0货主、1个人、2分包商})
     * 1102 线上业务认证页面(业务开通页面)
     * 1103 线上业务详情页面
     * 1200 我的页面
     * 2000 货源列表(tarId=cargoState{-1已过期、0全部、1待交易、2待承运、3已交易})
     * 2001 货源详情(tarId=cargoId)
     * 2002 发货页面
     * 2100 合同车辆列表
     * 2101 合同车辆详情页面(tarId=pactId)
     * 2200 常用车辆列表
     * 2201 常用车辆详情页面(tarId=userDriverId)
     * 3100 普通订单详情(tarId=orderId)
     * 3101 分包订单详情1(tarId=distributeId)
     * 3102 分包订单详情2(tarId=distributeId,spareOne=orderId)
     * 4001 财务支持——待支付详情(tarId=applyPayId)
     * 4002 账单明细页
     * 5001 分包管理页面（分包商）
     * 5002 我的分包商页面（货主）
     *
     *
     * -- 推送给司机
     * 3001 运单详情详情
     * 3100 转单详情
     */

    /** 模版内容 */
    private static final Map<String, String[]> templateMap = new HashMap<String, String[]>();
    static {
        /** 承运通知（分包订单）
         * ${1}=装货时间（yyyy-MM-dd）
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR100+"", new String[]{"承运通知", "亲，要求装货时间${1}，${2}运往${3}的${4}，司机已确认承运！", "3102", "2"});
        /** 拒绝承运通知（分包订单）
         * ${1}=装货时间（yyyy-MM-dd）
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR101+"", new String[]{"拒绝承运通知", "亲，要求装货时间${1}，${2}运往${3}的${4}，司机拒绝承运！", "3102", "2"});
        /** 承运通知（普通订单）
         * ${1}=装货时间（yyyy-MM-dd）
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR102+"", new String[]{"承运通知", "亲，要求装货时间${1}，${2}运往${3}的${4}，司机已确认承运！", "3100", "2"});
        /** 拒绝承运通知（普通订单）
         * ${1}=装货时间（yyyy-MM-dd）
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR103+"", new String[]{"拒绝承运通知", "亲，要求装货时间${1}，${2}运往${3}的${4}，司机拒绝承运！", "3100", "2"});
        /** 装货通知（普通订单）
         * ${1}=装货地市
         * ${2}=卸货地市
         * ${3}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR104+"", new String[]{"装货通知", "亲，${1}运往${2}的${3}，司机已经确认装货！", "3100", "2"});
        /** 卸货通知（普通订单）
         * ${1}=装货地市
         * ${2}=卸货地市
         * ${3}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR105+"", new String[]{"卸货通知", "亲，${1}运往${2}的${3}，司机已经确认卸货！", "3100", "2"});
        /** 装货通知（分包订单）
         * ${1}=装货地市
         * ${2}=卸货地市
         * ${3}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR106+"", new String[]{"装货通知", "亲，${1}运往${2}的${3}，司机已经确认装货！", "3102", "2"});
        /** 卸货通知（分包订单）
         * ${1}=装货地市
         * ${2}=卸货地市
         * ${3}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR107+"", new String[]{"卸货通知", "亲，${1}运往${2}的${3}，司机已经确认卸货！", "3102", "2"});
        /** 收到司机报价
         * ${1}=货物名称
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=报价
         */
        templateMap.put(Constants.PUSH_USE_FOR108+"", new String[]{"收到司机报价", "您发布的货源：${1}${2}运往${3}，有新的报价${4}元。", "2001", "1"});
        /** 司机同意成为合同车
         * ${1}=司机名称
         * ${2}=车牌号码
         */
        templateMap.put(Constants.PUSH_USE_FOR109+"", new String[]{"司机同意成为合同车", "${1}${2}已经成为您的合同车辆。", "2101", "1"});
        /** 司机拒绝成为合同车
         * ${1}=司机名称
         * ${2}=车牌号码
         */
        templateMap.put(Constants.PUSH_USE_FOR110+"", new String[]{"司机拒绝成为合同车", "${1}${2}拒绝成为您的合同车辆。", "2101", "1"});
        /** 司机同意合同线路
         * ${1}=司机名称
         * ${2}=车牌号码
         * ${3}=起点城市
         * ${4}=目的地城市
         */
        templateMap.put(Constants.PUSH_USE_FOR111+"", new String[]{"司机同意合同线路", "${1}${2}同意了合同线路${3}到${4}。", "2101", "1"});
        /** 司机拒绝合同线路
         * ${1}=司机名称
         * ${2}=车牌号码
         * ${3}=起点城市
         * ${4}=目的地城市
         */
        templateMap.put(Constants.PUSH_USE_FOR112+"", new String[]{"司机拒绝合同线路", "${1}${2}拒绝了合同线路${3}到${4}。", "2101", "1"});
        /** 货主取消订单，分包商同意司机不同意（分包订单）
         * ${1}=装货时间
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         * ${5}=司机名称
         */
        templateMap.put(Constants.PUSH_USE_FOR113+"", new String[]{"取消通知", "司机${5}拒绝取消订单：${1}，${2}运往${3}的${4}。", "3102", "2"});
        /** 货主取消订单，分包商和司机都同意（分包订单）
         * ${1}=装货时间
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR114+"", new String[]{"取消通知", "订单已取消：${1}，${2}到${3}的${4}。", "3102", "2"});
        /** 分包商取消订单，司机不同意（分包订单）
         * ${1}=装货时间
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         * ${5}=司机名称
         */
        templateMap.put(Constants.PUSH_USE_FOR115+"", new String[]{"取消通知", "司机${5}拒绝取消订单：${1}，${2}到${3}的${4}。", "3102", "2"});
        /** 货主取消订单，司机不同意（普通订单）
         * ${1}=装货时间
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         * ${5}=司机名称
         */
        templateMap.put(Constants.PUSH_USE_FOR116+"", new String[]{"取消通知", "司机${5}拒绝取消订单：${1}，${2}到${3}的${4}。", "3100", "2"});
        /** 货主取消订单，司机同意（普通订单）
         * ${1}=装货时间
         * ${2}=装货地市
         * ${3}=卸货地市
         * ${4}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR117+"", new String[]{"取消通知", "订单已取消：${1}，${2}到${3}的${4}。", "3100", "2"});
        /** 司机申请取消订单（普通订单）
         * ${1}=司机名称
         * ${2}=装货时间
         * ${3}=装货地市
         * ${4}=卸货地市
         * ${5}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR118+"", new String[]{"取消通知", "司机${1}向您提出取消订单：${2}，${3}到${4}的${5}。", "3100", "2"});
        /** 司机申请取消订单（分包订单）
         * ${1}=司机名称
         * ${2}=装货时间
         * ${3}=装货地市
         * ${4}=卸货地市
         * ${5}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR119+"", new String[]{"取消通知", "司机${1}向您提出取消订单：${2}，${3}到${4}的${5}。", "3102", "2"});
        /** 承运后分包商取消订单，司机同意（分包订单）
         * ${1}=分包商名称
         * ${2}=装货时间
         * ${3}=装货地市
         * ${4}=卸货地市
         * ${5}=货物名称
         */
        templateMap.put(Constants.PUSH_USE_FOR120+"", new String[]{"取消通知", "分包商${1}向您提出取消订单：${2}，${3}运往${4}的${5}。", "3102", "2"});

        /** 转单运单提醒司机接单（转单运单）
         * ${1}=司机姓名
         */
        templateMap.put(Constants.PUSH_USE_FOR121+"", new String[]{"提醒接单", "${1}提醒您!有一笔待承接运单赶紧去确认吧。", "3001", "2"});

        /** 普通运单云配送订单接单提醒（普通订单）
         * ${1}=司机姓名
         */
        templateMap.put(Constants.PUSH_USE_FOR122+"", new String[]{"承运通知", "亲，要求装货时间${1}，${2}运往${3}的${4}，司机已确认承运！", "3102", "2"});
    }

    /**
     * 生成推送内容
     * @param useFor 用途
     * @param msg 替换内容
     * @return
     * @author wyh
     */
    public static PushDetails build(Integer useFor, String[] msg){
        String[] template = templateMap.get(useFor.toString());
        PushDetails pushDetails = new PushDetails();
        pushDetails.setPushTitle(template[0]);
        pushDetails.setPushContent(buildContent(template[1], msg));
        pushDetails.setJumpType(Integer.parseInt(template[2]));
        pushDetails.setPushKind(Byte.parseByte(template[3]));
        pushDetails.setUseFor(useFor);
        return pushDetails;
    }

    private static String buildContent(String content, String[] msg){
        if (msg != null && msg.length > 0) {
            int index = 0;
            for (String str: msg) {
                index++;
                if (str == null) {
                    content = content.replace("${"+index+"}", "");
                } else {
                    content = content.replace("${"+index+"}", str);
                }
            }
        }
        return content;
    }



}
