package com.cy.driver.common.annotate;

/**
 * Created by wyh on 2015/5/7.
 */
public enum ApiResultCodeEnum {
    SYS_10001("SYS_10001","系统错误"),
    SYS_10002("SYS_10002","系统请求不合法"),
    SYS_10003("SYS_10003","服务器当前忙，请稍候重试"),
    SYS_10004("SYS_10004","请调整手机时间为正确时间"),

    SER_20001("SER_20001","参数错误"),
    SER_20002("SER_20002","处理失败"),
    SER_20003("SER_20003","请稍等重试"),

    SER_20008("SER_20008","用户不存在[未注册]"),
    SER_20009("SER_20009","该手机号%1S已经注册"),
    SER_20010("SER_20010","用户名或密码不正确"),


    SER_20026("SER_20026","请求头%1S不合法"),

    SER_20028("SER_20028","手机系统类型未获取."),

    SER_20030("SER_20030","验证码已失效，请重新获取。"),
    SER_20031("SER_20031","验证码错误或者已失效"),
    SER_20032("SER_20032","手机号码格式不正确。"),
    SER_20033("SER_20033","无运单信息。"),
    SER_20034("SER_20034","无效订单信息。"),
    SER_20035("SER_20035","图片不存在。"),
    SER_20036("SER_20036","身份证号码格式不正确。"),
    SER_20037("SER_20037","认证步骤不存在。"),
    SER_20038("SER_20038","认证已提交不能修改。"),
    SER_20039("SER_20039","认证已通过，不能修改。"),
    SER_20040("SER_20040","图片路径不存在。"),
    SER_20041("SER_20041","订单状态不存在。"),
    SER_20042("SER_20042","获取银行ID失败。"),
    SER_20043("SER_20043","银行支付系统保存支付结果信息失败."),
    SER_20044("SER_20044","类型不存在."),
    SER_20045("SER_20045","调用百度逆地理编码服务失败."),
    SER_20046("SER_20046","密码格式不正确."),
    SER_20047("SER_20047","订单已经签收或拒签，不能重复操作."),
    SER_20048("SER_20048","当前系统版本不存在."),
    SER_20049("SER_20049","token不能为空信息."),
    SER_20050("SER_20050","token无效."),
    SER_20051("SER_20051","密码已锁定."),
    SER_20052("SER_20052","拦截到请求头参数不合法."),
    SER_20053("SER_20053","修改imei失败."),
    SER_20054("SER_20054","%1S格式不正确."),
    SER_20055("SER_20055","车辆类型不合法."),
    SER_20056("SER_20056","二级车辆类型不合法."),
    SER_20057("SER_20057","车厢类型不合法."),
    SER_20058("SER_20058","二级车厢类型不合法."),
    SER_20059("SER_20059","用户姓名不为空."),
    SER_20060("SER_20060","新的登录账号已经注册,请使用其它手机。"),
    SER_20061("SER_20061","用户未登录。"),
    SER_20062("SER_20062","参数值不合法。"),
    SER_20063("SER_20063","行驶证未认证。"),
    SER_20064("SER_20064","驾驶证未认证。"),
    SER_20065("SER_20065","常跑城市超过6个。"),
    SER_20066("SER_20066","货源不存在或者已失效。"),
    SER_20067("SER_20067","开始日期小于当前日期."),
    SER_20068("SER_20068","截至日期必须大于等于当前日期。"),
    SER_20069("SER_20069","出发地省不能为空。"),//出发地省不为空。
    SER_20070("SER_20070","出发地市不能为空。"),//出发地市不为空。
    SER_20071("SER_20071","出发地不能为空。"),//
    SER_20072("SER_20072","出发地不能为空。"),//
    SER_20073("SER_20073","目的地省不能为空。"),//目的地省不为空
    SER_20074("SER_20074","目的地市不能为空。"),//目的地市不为空
    SER_20075("SER_20075","目的地不能为空。"),
    SER_20076("SER_20076","目的地不能为空。"),
    SER_20077("SER_20077","起始时间为空或大于等于当前日期"),
    SER_20078("SER_20078","开始日期不为空."),
    SER_20079("SER_20079","坐标不能为空."),
    SER_20080("SER_20080","报价失败，货源已产生订单"),
    SER_20081("SER_20081","报价失败，货主已确定你的车，请前往未承运订单确定应承"),
    SER_20082("SER_20082","调用百度坐标失败。"),
    SER_20083("SER_20083","订单已存在评价。"),
    SER_20084("SER_20084","发货单保存失败。"),
    SER_20085("SER_20085","订单不存在或者已删除。"),
    SER_20086("SER_20086","订单已删除。"),
    SER_20087("SER_20087","该订单不属于该用户。"),
    SER_20088("SER_20088","订单已取消。"),
    SER_20089("SER_20089","订单已锁定。"),
    SER_20090("SER_20090","订单未锁定。"),
    SER_20091("SER_20091","订单已完成。"),
    SER_20092("SER_20092","订单已关闭。"),
    SER_20093("SER_20093","订单未完成或未卸货，不能评价。"),
    SER_20094("SER_20094","调用百度坐标失败。"),
    SER_20095("SER_20095","数据不存在。"),
    SER_20096("SER_20096","货主平台已经注册。"),
    SER_20097("SER_20097","提现密码不能为空。"),
    SER_20098("SER_20098","身份证号码格式不正确。"),
    SER_20099("SER_20099","资金账户不存在"),
    SER_20100("SER_20100","用户姓名不存在"),
    SER_20101("SER_20101","用户已冻结"),
    SER_20102("SER_20102","原密码错误"),
    SER_20103("SER_20103","修改失败"),
    SER_20104("SER_20104","推送失败"),
    SER_20105("SER_20105","密码格式不正确"),
    SER_20106("SER_20106","验证码为空"),
    SER_20107("SER_20107","手机号码未注册"),
    SER_20108("SER_20108","次数达到上限"),
    SER_20109("SER_20109","原提现密码不正确"),
    SER_20110("SER_20110","请勾选完整的评分"),
    SER_20111("SER_20111","保存车辆信息失败"),
    SER_20112("SER_20112","保存常跑城市失败"),
    SER_20113("SER_20113","查询货源浏览记录信息失败"),
    SER_20114("SER_20114","这笔订单已申请支付，无法取消！"),
    SER_20115("SER_20115","服务编码为空！"),
    SER_20116("SER_20116","货源已过期！"),
    SER_20117("SER_20116","货源已被承运！"),



    SER_20201("SER_20201","新增失败"),
    SER_20202("SER_20202","删除失败"),
    SER_20203("SER_20203","修改失败"),
    SER_20204("SER_20204","企业信息不存在"),
    SER_20205("SER_20205","货主信息不存在"),
    SER_20206("SER_20206","短信服务超时或者限制"),
    SER_20207("SER_20207","身份证号码不匹配。"),
    SER_20208("SER_20208","找回密码失败。"),
    SER_20209("SER_20209","身份证号码不匹配。"),
    SER_20210("SER_20210","提现失败。"),
    SER_20211("SER_20211","收款失败。"),
    SER_20212("SER_20212","提现中"),
    SER_20213("SER_20213","提现密码不正确"),
    SER_20214("SER_20214","余额不足"),
    SER_20215("SER_20215","银行卡不存在"),
    SER_20216("SER_20216","单笔提现超过限制"),
    SER_20217("SER_20217","用户未认证，请先认证"),
    SER_20218("SER_20218","银行卡已经存在"),
    SER_20219("SER_20219","密码连续输入错误，已锁定。请通过找回密码重置!"),
    SER_20220("SER_20220","提现密码不能与登录密码一致。"),
    SER_20221("SER_20221","暂不支持该银行。"),
    SER_20222("SER_20222","提现网关业务操作失败。"),
    SER_20223("SER_20223","您的账户存在余额或者欠款不能注销。"),
    SER_20224("SER_20224","您的账户存在未完成的订单不能注销。"),
    SER_20225("SER_20225","注销失败。"),
    SER_20226("SER_20226","查询失败。"),
    SER_20227("SER_20227","订单数据异常。"),
    SER_20228("SER_20228","分包订单协议签订失败。"),
    SER_20229("SER_20229","当日提现金额超过上限。"),
    SER_20230("SER_20230","签收失败"),
    SER_20231("SER_20231","签收码不存在"),
    SER_20232("SER_20232","已签收或已下架"),
    SER_20233("SER_20233","签收码不正确"),
    SER_20234("SER_20234","无法注销账户"),



    SER_20300("SER_20300","订单查询失败。"),
    SER_20301("SER_20301","账户提现功能已锁定,请联系客服"),
    SER_20302("SER_20302","积分账户信息不存在"),
    SER_20303("SER_20303","货源信息不存在或者失效"),
    SER_20304("SER_20304","您的账号存在异常，请及时联系快到网客服"),


    RULE_20009("RULE_20009","该手机号[%1S]已经注册，是否直接登录？"),
    RULE_30001("RULE_30001","你的手机号码[%1S]尚未注册，是否现在注册？"),
    RULE_30002("RULE_30002","你是否需要找回密码？"),
    RULE_30003("RULE_30003","登录密码已被锁定，建议你找回密码？"),
    RULE_30004("RULE_30004","验证码短信可能有延迟，确定返回并重新登录？"),
    RULE_30005("RULE_30005","获取验证码次数超过业务限制，如有疑问，请联系客服！"),
    RULE_30006("RULE_30006","登录密码已被锁定，建议你找回密码？"),
    RULE_30007("RULE_30007","你的手机号码[%1S]已经注册，是否直接登录？"),
    RULE_30008("RULE_30008","你的手机号码[%1S]已经冻结。"),
    RULE_30009("RULE_30009","密码输入错误，请重新输入."),
    RULE_30101("RULE_30101","未认证司机用户查看货源详情已达到上线"),
    RULE_30102("RULE_30102","修改失败，认证中或者已认证用户不能修改姓名"),
    RULE_30103("RULE_30103","银行账户表ID不能为空"),
    RULE_30104("RULE_30104","未认证司机用户一天之内只能拨打3次电话，请认证"),
    RULE_30105("RULE_30105","订单状态不匹配"),
    RULE_30200("RULE_30200","无效的兑换码"),

    /** nxj转单运单 **/
    RULE_30301("RULE_30301","运费金额不能小于预付款"),
    RULE_30302("RULE_30302","承运方不是司机,不能查看轨迹"),
    RULE_30303("RULE_30303","承运方无当前位置信息"),
    RULE_30304("RULE_30304","修改运费,预付款失败!请稍后重试"),
    RULE_30305("RULE_30305","确认承运方报价失败!请稍后重试"),
    RULE_30306("RULE_30306","您的账号暂无此功能!"),
    /** nxj抢单/竞价 **/
    RULE_30401("RULE_30401","竞价失败!"),
    RULE_30402("RULE_30402","抢单失败!"),

    /**  UTMS钱包 **/
    RULE_30501("RULE_30501","银行卡不存在!"),
    RULE_30502("RULE_30502","获取手续费失败!"),
    RULE_30503("RULE_30503","获取钱包信息失败!"),
    RULE_30504("RULE_30504","提现失败!余额不足."),
    RULE_30505("RULE_30505","提现业务申请失败"),
    RULE_30506("RULE_30506","提现失败!"),
    RULE_30507("RULE_30507","提现失败,密码错误!"),
    RULE_30508("RULE_30508","提现失败,账户冻结!"),
    RULE_30509("RULE_30509","提现失败,系统异常!"),
    RULE_30510("RULE_30510","提现失败,网关异常!"),
    RULE_30511("RULE_30511","提现失败,参数异常!"),
    RULE_30512("RULE_30512","绑定银行卡失败,银行卡姓名与用户注册姓名不符!")
    ;


    private String code;
    private String message;

    ApiResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static String findErrorCodeStr(String msg, String ...messages){
        int count = 0;
        if(messages != null && messages.length > 0){
            for (String str : messages) {
                count++;
                msg = msg.replaceAll("%"+count+"S", str);
            }
        }
        return msg;
    }
}
