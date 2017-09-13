package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.constants.PointEventCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.ConfigData;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.service.*;
import com.cy.pass.service.dto.AppChannelDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.Enum.AuthCodeType;
import com.cy.pass.service.dto.OpsActivityInfoDTO;
import com.cy.pass.service.dto.ShareUserDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mr on 2015/6/23.
 */
@Scope("prototype")
@Controller("registAction")
public class RegistAction extends BaseAction {
    private Logger LOG = LoggerFactory.getLogger(RegistAction.class);
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Resource
    private QueryOrderService queryOrderService;

    @Resource
    private RedPacketsService redPacketsService;

    @Resource
    private AppChannelHandleService appChannelHandleService;

    @Resource
    private ConfigData configData;

    @Resource
    private PointFService pointFService;
    @Resource
    private AppShareService appShareService;

    private String activityName;

    /**
     * @param request
     * @param response
     * @param mobilephone
     * @param password
     * @param verificationCode
     * @param regFrom
     * @param regFromName      注册来源名称（3.3优化版本增加）
     * @return
     */
    @RequestMapping(value = "/quickRegistration", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_REGIST_DRIVER_USERINFO)
//    @Log(type = LogEnum.REGISTER)
    public Object quickRegistration(HttpServletRequest request, HttpServletResponse response, String mobilephone,
                                    String password, String verificationCode, String regFrom, String regFromName) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if (StringUtils.isEmpty(mobilephone) || !ValidateUtil.validateTelePhone(mobilephone)) {
            if (LOG.isErrorEnabled()) LOG.error("快速注册校验：登录账户格式不正确");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20032);
        }

        if (StringUtils.isBlank(password)) {
            if (LOG.isErrorEnabled()) LOG.error("快速注册校验：密码格式不正确");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20105);
        }
        if (StringUtils.isBlank(verificationCode)) {
            if (LOG.isErrorEnabled()) LOG.error("快速注册校验：验证码不能为空");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20106);
        }
        Integer from = 0;
        Integer source = 0;

        String sourceStr = findSource(request);
        if (sourceStr.equals(Constants.OS_ANDROID)) {
            source = Constants.OS_ANDROID_CODE_INT;
        } else if (sourceStr.equals(Constants.OS_IOS)) {
            source = Constants.OS_IOS_CODE_INT;
        } else if (sourceStr.equals(Constants.OS_WXIN)) {
            source = Constants.OS_WXIN_CODE_INT;
        } else {
            source = Constants.OS_OTHER_CODE_INT;
        }

        //如果为 说明是老版本用户
        if (StringUtils.isEmpty(regFromName)) {
            //老版本获取注册来源
            if (StringUtils.isNotBlank(regFrom)) {
                from = Integer.valueOf(regFrom);
            }
        } else {
            Response<AppChannelDTO> response1 = appChannelHandleService.find(regFromName, source);
            if (response1.isSuccess() && response1.getData() != null) {
                from = Integer.valueOf(response1.getData().getChannelNum());
            }
        }
        if (from.intValue() == 3) {
            /** regFrom=3的分享来源全部转变成99 */
            from = 99;
        }

        try {
            //验证码验证
            Response<Boolean> result = driverUserHandlerService.validAuthCode(mobilephone, verificationCode, AuthCodeType.REGIST.getValue());
            if (!result.isSuccess()) {
                if (LOG.isErrorEnabled()) LOG.error("快速注册验证码(服务端)校验：response.message=" + result.getMessage());
                updRespHeadError(response);
                //参数不完整
                if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册验证码(服务端)校验：参数不完整.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                //验证码失效
                if (result.getCode() == CodeTable.LI_VALID_TIMES.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册验证码(服务端)校验：验证码错误或者已失效.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
                }
                //号码已注册(注册的时候不会有该提示)
                if (result.getCode() == CodeTable.VALID_CODE.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册验证码(服务端)校验：号码已注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_20009);
                }
                //货主已经注册
                if (result.getCode() == CodeTable.AREADLYREG.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：货主已经注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20096);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

            //注册
            String phone = getImsi(request);
            String imei = getImei(request);
            String appVersion = findApkVersion(request);
            String sysOsVersion = findSource(request);

            DriverUserInfoDTO userInfo = new DriverUserInfoDTO();
            userInfo.setCode(mobilephone);
            userInfo.setTelephone(phone);
            userInfo.setPassword(password);
            userInfo.setNoImei(imei);
            userInfo.setRegFrom(from);
            userInfo.setAppVersion(appVersion);
            userInfo.setOperatingSystemVersionNumber(sysOsVersion);
            userInfo.setRegisterSource(source.byteValue());

            Response<Long> registResponse = driverUserHandlerService.quickRegist(userInfo, verificationCode);
            if (!registResponse.isSuccess()) {
                if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：response.message=" + registResponse.getMessage());
                updRespHeadError(response);
                //参数不完整
                if (registResponse.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：参数不完整.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                //验证码失效
                if (registResponse.getCode() == CodeTable.LI_VALID_TIMES.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：验证码错误或者已失效.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
                }
                //号码已注册
                if (registResponse.getCode() == CodeTable.VALID_CODE.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：号码已注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_20009, mobilephone);
                }
                //货主已经注册
                if (result.getCode() == CodeTable.AREADLYREG.getCode()) {
                    if (LOG.isErrorEnabled()) LOG.error("快速注册保存(服务端)校验：货主已经注册.");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20096);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }

            /** 同步司机的订单信息*/
            queryOrderService.driverReg(mobilephone,registResponse.getData(),null);

            /** 排除鲍宗彩的手机号码 */
            if (!mobilephone.equals(configData.getRegisterMobilephone())) {
                /** 开通支付账户业务 */
                Long accountId = driverUserHandlerService.openCapitalAccount(registResponse.getData());
                Response<OpsActivityInfoDTO> response1 = redPacketsService.getRedPackets(activityName);
                if (response1.isSuccess()) {//如果该用户有红包金额则调用存放用户的红包接口
                    if (response1.getData() != null) {
                        if (response1.getData().getRewardDriverUnitprice() != null) {
                            com.cy.platformpay.service.dto.base.Response<Boolean> response2 = redPacketsService.bonusLuckFee(accountId, response1.getData().getRewardDriverUnitprice());
                        }
                    }
                }
            }
            queryOrderService.updateDriverIdForMobile(registResponse.getData(), mobilephone);
            updRespHeadSuccess(response);
            resultMap.put("userId", registResponse.getData().toString());

            //注册积分账户-3.4版本
            Long driverId = registResponse.getData();
            String accountCode = pointFService.saveAccount(driverId);
            ShareUserDTO shareUserDTO = appShareService.regUpdateStateByDriver(mobilephone);
            if (StringUtils.isBlank(accountCode)) {
                LOG.error("积分账户开通失败.param={}", driverId);
            } else {
                pointFService.pointReward(Constants.AWARD_DRIVER, driverId, Constants.CHECK_MODE_BY_EVENT, LogEnum.REGISTER.getEventCode(),null,null,convert2InSource(),null);
                if (shareUserDTO != null) {
                    Byte awardUserType = null;
                    if (shareUserDTO.getUserType().intValue() == ShareUserDTO.DRIVER) {
                        awardUserType = Constants.AWARD_DRIVER;
                    } else if (shareUserDTO.getUserType().intValue() == ShareUserDTO.OWNER) {
                        awardUserType = Constants.AWARD_OWNER;
                    }
                    if (awardUserType != null) {
                        String alertMsg = "您成功邀请了"+ SystemsUtil.hideMobile(mobilephone)+"成为快到网的用户";
                        pointFService.pointReward(awardUserType, shareUserDTO.getUserId(), Constants.CHECK_MODE_BY_EVENT, PointEventCode.CODE_SHARE_REG,null,null,convert2InSource(),alertMsg);
                    }
                }
            }
            return resultMap;
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) LOG.error("快速注册出错,", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    @Value("#{propertiesReader['newUser.redPackets.activityName']}")
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
