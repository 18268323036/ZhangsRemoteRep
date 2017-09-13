package com.cy.driver.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.constants.PushDetails;
import com.cy.driver.common.constants.PushSendTemplate;
import com.cy.driver.common.initdata.ConfigData;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.HttpUtils;
import com.cy.driver.domain.push.*;
import com.cy.driver.service.PushSendService;
import com.cy.pass.service.MessageCenterService;
import com.cy.pass.service.dto.PushSendRecordDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by wyh on 2016/1/4.
 */
@Service("pushSendService")
public class PushSendServiceImpl implements PushSendService {
    private static final Logger LOG = LoggerFactory.getLogger(PushSendServiceImpl.class);
    @Resource
    private MessageCenterService messageCenterService;
    @Resource
    private ConfigData configData;

    /**
     * 承运通知（分包订单）
     */
    @Override
    public boolean cytzPushSubCon(PushCYTZ info) {
        String[] messages = new String[4];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartAddrCity();
        messages[2] = info.getEndAddrCity();
        messages[3] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR100);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 拒绝承运通知（分包订单）
     */
    @Override
    public boolean jjcytzPushSubCon(PushCYTZ info) {
        String[] messages = new String[4];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartAddrCity();
        messages[2] = info.getEndAddrCity();
        messages[3] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR101);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }



    /**
     * 承运通知（普通订单）
     */
    @Override
    public boolean cytzPushOwn(PushCYTZ info) {
        String[] messages = new String[4];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartAddrCity();
        messages[2] = info.getEndAddrCity();
        messages[3] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR102);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 拒绝承运通知（普通订单）
     */
    @Override
    public boolean jjcytzPushOwn(PushCYTZ info) {
        String[] messages = new String[4];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartAddrCity();
        messages[2] = info.getEndAddrCity();
        messages[3] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR103);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 装货通知（普通订单）
     */
    @Override
    public boolean zhtzCommonPushOwn(PushZHTZ info) {
        String[] messages = new String[3];
        messages[0] = info.getStartAddrCity();
        messages[1] = info.getEndAddrCity();
        messages[2] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR104);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 卸货通知（普通订单）
     */
    @Override
    public boolean xhtzCommonPushOwn(PushZHTZ info) {
        String[] messages = new String[3];
        messages[0] = info.getStartAddrCity();
        messages[1] = info.getEndAddrCity();
        messages[2] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR105);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 装货通知（分包订单）
     */
    @Override
    public boolean zhtzSubConPushOwn(PushZHTZ info) {
        String[] messages = new String[3];
        messages[0] = info.getStartAddrCity();
        messages[1] = info.getEndAddrCity();
        messages[2] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR106);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 卸货通知（分包订单）
     */
    @Override
    public boolean xhtzSubConPushOwn(PushZHTZ info) {
        String[] messages = new String[3];
        messages[0] = info.getStartAddrCity();
        messages[1] = info.getEndAddrCity();
        messages[2] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR107);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 收到司机报价
     */
    @Override
    public boolean sdsjbjPushOwn(PushSdsjbj info) {
        String[] messages = new String[4];
        messages[0] = info.getCargoName();
        messages[1] = info.getStartCity();
        messages[2] = info.getEndCity();
        if (info.getQuoteMoney() != null)
            messages[3] = info.getQuoteMoney().toString();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR108);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)4);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 司机同意成为合同车
     */
    @Override
    public boolean sjtycwhtcPushOwn(PushSjhtc info) {
        String[] messages = new String[2];
        messages[0] = info.getDriverName();
        messages[1] = info.getCarNumber();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR109);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)4);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 司机拒绝成为合同车
     */
    @Override
    public boolean sjjjcwhtcPushOwn(PushSjhtc info) {
        String[] messages = new String[2];
        messages[0] = info.getDriverName();
        messages[1] = info.getCarNumber();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR110);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)4);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 司机同意合同线路
     */
    @Override
    public boolean sjtyhtxlPushOwn(PushSjhtxl info) {
        String[] messages = new String[4];
        messages[0] = info.getDriverName();
        messages[1] = info.getCarNumber();
        messages[2] = info.getStartCity();
        messages[3] = info.getEndCity();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR111);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)4);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 司机拒绝合同线路
     */
    @Override
    public boolean sjjjhtxlPushOwn(PushSjhtxl info) {
        String[] messages = new String[4];
        messages[0] = info.getDriverName();
        messages[1] = info.getCarNumber();
        messages[2] = info.getStartCity();
        messages[3] = info.getEndCity();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR112);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)4);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 货主取消订单，分包商同意司机不同意（分包订单）
     */
    @Override
    public boolean sjbtyPushOwn(PushDriver info) {
        String[] messages = new String[5];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartCity();
        messages[2] = info.getEndCity();
        messages[3] = info.getCargoName();
        messages[4] = info.getDriverName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR113);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 货主取消订单，分包商和司机都同意（分包订单）
     */
    @Override
    public boolean sjtyPushOwn(PushCargo info) {
        String[] messages = new String[4];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartCity();
        messages[2] = info.getEndCity();
        messages[3] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR114);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 分包商取消订单，司机不同意（分包订单）
     */
    @Override
    public boolean sjbtyPushSubCon(PushDriver info) {
        String[] messages = new String[5];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartCity();
        messages[2] = info.getEndCity();
        messages[3] = info.getCargoName();
        messages[4] = info.getDriverName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR115);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 货主取消订单，司机不同意（普通订单）
     */
    @Override
    public boolean sjbtyPushOwn2(PushDriver info) {
        String[] messages = new String[5];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartCity();
        messages[2] = info.getEndCity();
        messages[3] = info.getCargoName();
        messages[4] = info.getDriverName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR116);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 货主取消订单，司机同意（普通订单）
     */
    @Override
    public boolean sjtyPushOwn2(PushCargo info) {
        String[] messages = new String[4];
        messages[0] = DateUtil.dateToStr(info.getStartTime());
        messages[1] = info.getStartCity();
        messages[2] = info.getEndCity();
        messages[3] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR117);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 司机申请取消订单（普通订单）
     */
    @Override
    public boolean sjqxddPushOwn(PushDriver info) {
        String[] messages = new String[5];
        messages[0] = info.getDriverName();
        messages[1] = DateUtil.dateToStr(info.getStartTime());
        messages[2] = info.getStartCity();
        messages[3] = info.getEndCity();
        messages[4] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR118);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 司机申请取消订单（分包订单）
     */
    @Override
    public boolean sjqxddPushSubCon(PushDriver info) {
        String[] messages = new String[5];
        messages[0] = info.getDriverName();
        messages[1] = DateUtil.dateToStr(info.getStartTime());
        messages[2] = info.getStartCity();
        messages[3] = info.getEndCity();
        messages[4] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR119);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 承运后分包商取消订单，司机同意（分包订单）
     */
    @Override
    public boolean sjtyPushOwn(PushSubCon info) {
        String[] messages = new String[5];
        messages[0] = info.getSubConName();
        messages[1] = DateUtil.dateToStr(info.getStartTime());
        messages[2] = info.getStartCity();
        messages[3] = info.getEndCity();
        messages[4] = info.getCargoName();

        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(info.getUserId());
        pushInfo.setTargetId(info.getBusinessId());
        pushInfo.setTargetId2(info.getBusinessId2());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR120);
        pushInfo.setSourceType(1);
        pushInfo.setRemindType((byte)2);
        return pushSingleOwn(pushInfo);
    }

    /**
     * 转单运单提醒承运方接单
     * @param pushBase
     * @return
     */
    @Override
    public boolean waybillPushOwn(PushBase pushBase, String driverName) {
        String[] messages = new String[1];
        messages[0] = driverName;
        PushInfo pushInfo = new PushInfo();
        pushInfo.setUserId(pushBase.getUserId());
        pushInfo.setTargetId(pushBase.getBusinessId());
        pushInfo.setMessages(messages);
        pushInfo.setUseFor(Constants.PUSH_USE_FOR121);
        pushInfo.setSourceType(2);
        pushInfo.setRemindType((byte)2);
        return pushSingleDriver(pushInfo);
    }

    /**
     * 推送单条信息给货主
     * @param pushInfo
     * @return
     * @author wyh
     */
    private boolean pushSingleOwn(PushInfo pushInfo) {
        if (pushInfo.getUserId() == null) {
            LOG.error("推送单条信息给货主失败，用户id必填");
            return false;
        }
        pushInfo.setEventFrom(Constants.EVENT_FROM3);
        pushInfo.setUserType(Constants.PUSH_USER_TYPE_COMPANY);
        boolean flag = pushSingle(pushInfo);
        if (flag) {
            if (LOG.isDebugEnabled())
                LOG.debug("推送单条信息给货主成功，userId={}", pushInfo.getUserId());
        } else {
            LOG.error("推送单条信息给货主失败，userId={}", pushInfo.getUserId());
        }
        return flag;
    }



    /**
     * 推送单条信息给司机
     * @param pushInfo
     * @return
     * @author wyh
     */
    private boolean pushSingleDriver(PushInfo pushInfo){
        if (pushInfo.getUserId() == null) {
            LOG.error("推送单条信息给司机失败，司机id必填");
            return false;
        }
        pushInfo.setEventFrom(Constants.EVENT_FROM3);
        pushInfo.setUserType(Constants.PUSH_USER_TYPE_DRIVER);
        boolean flag = pushSingle(pushInfo);
        if (flag) {
            if (LOG.isDebugEnabled())
                LOG.debug("推送单条信息给司机成功，driverId={}", pushInfo.getUserId());
        } else {
            LOG.error("推送单条信息给司机失败，driverId={}", pushInfo.getUserId());
        }
        return flag;
    }

    /**
     * 推送单条信息
     * @param pushInfo 推送信息
     * @return
     * @author wyh
     */
    private boolean pushSingle(PushInfo pushInfo) {
        try {
            /** 推送内容 */
            PushDetails pushDetails = PushSendTemplate.build(pushInfo.getUseFor(), pushInfo.getMessages());
            Map<String, Object> result = push(pushInfo, pushDetails);
            Byte code = (Byte) result.get("code");
            /** 新增推送记录业务信息 */
            insertRecord(pushInfo.getUserId(), pushInfo.getUserType(), (Long) result.get("logId"),
                    pushDetails.getPushKind(), pushDetails.getPushTitle(),
                    pushDetails.getPushContent(), pushDetails.getUrl(),
                    pushInfo.getTargetId(), code,
                    pushDetails.getJumpType().toString(), pushInfo.getUseFor(), pushInfo.getEventFrom().byteValue(),pushInfo.getSourceType(),pushInfo.getRemindType());
            if (code.intValue() == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.error("推送单条信息出现异常", e);
        }
        return false;
    }

    /**
     * 接口推送
     * @param pushInfo
     * @param pushDetails
     * @return
     * @author wyh
     */
    private Map<String, Object> push(PushInfo pushInfo, PushDetails pushDetails) {
        Map<String, Object> result = new HashMap<String, Object>();
        /** 参数 */
        Map<String, String> paramMap = new HashMap<String, String>();
        /** 推送服务渠道(1极光-默认) */
        paramMap.put("pushChannel", "1");
        /** 推送方式(1全部，2分组，3，4单个) */
        paramMap.put("type", "4");
        /** 推送地址 */
        String pushUrl = "";
        if (pushInfo.getUserType().byteValue() == Constants.PUSH_USER_TYPE_COMPANY) {//货主推送
            /** 用户id 或者 别名(推送方式为全部可为空) */
            paramMap.put("userId", pushInfo.getUserId().toString());
            pushUrl = configData.getSendOwnPushSDKURL();
        } else {//司机推送
            /** 司机id 或者 别名(推送方式为全部可为空) */
            paramMap.put("driverId", pushInfo.getUserId().toString());
            pushUrl = configData.getSendDriverPushSDKURL();
        }
        /** 推送标题 */
        paramMap.put("pushTitle", pushDetails.getPushTitle());
        /** 推送内容 */
        paramMap.put("pushContent", pushDetails.getPushContent());
        /** 来源(1营销平台 2司机app服务 3货主Web网站 4经管系统 5调度服务 6货主app服务) */
        paramMap.put("eventFrom", pushInfo.getEventFrom().toString());
        /** 跳转方式 */
        paramMap.put("jumpType", pushDetails.getJumpType().toString());
        /** 跳转对象ID(信息主键id：如货源id，订单id。。。) */
        if (pushInfo.getTargetId() != null) {
            paramMap.put("tarId", pushInfo.getTargetId().toString());
        }
        if (pushInfo.getTargetId2() != null) {
            paramMap.put("spareOne", pushInfo.getTargetId2().toString());
        }
        /** 来源  */
        if(pushInfo.getSourceType() != null) {
            paramMap.put("spareTwo", pushInfo.getSourceType().toString());
        }
        /** 跳转url */
        if (!StringUtils.isBlank(pushDetails.getUrl()))
            paramMap.put("jumpUrl", pushDetails.getUrl());
        /** 调用推送服务 */
        String pushResultStr = HttpUtils.doPostRequest(pushUrl, paramMap);
        /** 返回状态 0成功 1失败 */
        Byte code = 1;
        Long logId = 0L;
        try {
            JSONObject jsonObject = JSONObject.parseObject(pushResultStr);
            if (jsonObject != null) {
                //推送接口发送返回状态 0失败，1成功
                String objErrorCode = jsonObject.getString("errorCode");
                if (objErrorCode != null && "1".equals(objErrorCode.toString())) {
                    code = 0;
                }
                Long objId = jsonObject.getLong("object");
                if (objId != null)
                    logId = objId;
            } else {
                LOG.error("调用推送接口之后解析json失败，jsonObject为空");
            }
        } catch (Exception e) {
            LOG.error("调用推送接口之后解析json出错", e);
        }
        result.put("code", code);
        result.put("logId", logId);
        return result;
    }

    /**
     * 添加推送记录信息
     * @param userId 用户id
     * @param userType 用户类型
     * @param logId 推送日志id
     * @param pushKind 推送性质（1通知 2交易）
     * @param pushTitle 推送标题
     * @param pushContent 推送内容
     * @param url 调整url
     * @param targetId 跳转对象ID(信息主键id：如货源id，订单id。。。)
     * @param returnStatus 推送接口发送返回状态（0成功 1失败）
     * @param jumpType 跳转类型
     * @param useFor 用途
     * @param siteSource 1快到网 2云配送
     * @param remindType '提醒类别：（1.货源 2.订单 3.积分 4.系统）
     * @return
     * @author wyh
     */
    private boolean insertRecord(Long userId, Byte userType, Long logId, Byte pushKind, String pushTitle,
                                 String pushContent, String url, Long targetId,
                                 Byte returnStatus, String jumpType, Integer useFor, Byte eventFrom,Integer siteSource,byte remindType){
        PushSendRecordDTO pushSendRecordDTO = new PushSendRecordDTO();
        pushSendRecordDTO.setEventFrom(eventFrom);
        pushSendRecordDTO.setDriverId(userId);
        pushSendRecordDTO.setUserType(userType);
        pushSendRecordDTO.setPushId(logId);
        pushSendRecordDTO.setPushKind(pushKind);
        pushSendRecordDTO.setPushTitle(pushTitle);
        pushSendRecordDTO.setPushContent(pushContent);
        pushSendRecordDTO.setUrl(url);
        if (!StringUtils.isBlank(jumpType)) {
            pushSendRecordDTO.setTargetType(Long.parseLong(jumpType));
        } else {
            pushSendRecordDTO.setTargetType(0L);
        }
        pushSendRecordDTO.setTargetId(targetId);
        pushSendRecordDTO.setReturnStatus(returnStatus);
        pushSendRecordDTO.setRemark("");
        pushSendRecordDTO.setJumpType(jumpType);
        pushSendRecordDTO.setUseFor(useFor);
        pushSendRecordDTO.setSiteSource(siteSource);
        pushSendRecordDTO.setRemindType(remindType);
        Response<Boolean> response = messageCenterService.insert(pushSendRecordDTO);
        return response.getData();
    }
}
