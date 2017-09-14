package com.cy.driver.api;

import com.cy.driver.action.BaseAction;
import com.cy.driver.api.domain.res.SassOrderInfo;
import com.cy.driver.api.domain.res.SassOrderList;
import com.cy.driver.api.domain.res.SassOrderPageRes;
import com.cy.driver.cloudService.SaasOrderInfoService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.PageBase;
import com.cy.driver.saasService.SaasPayUserService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.MainBmikeceService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.saas.basic.model.enums.ServiceSource;
import com.cy.saas.basic.model.enums.SiteCode;
import com.cy.saas.basic.model.enums.WorkPatternEnum;
import com.cy.saas.basic.model.po.SettingBusiness;
import com.cy.saas.business.model.dto.*;
import com.cy.saas.business.model.enums.UserType;
import com.cy.saas.business.model.po.OrderPart;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.saas.pay.model.enums.PayUserNatureEnum;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 抢单-竞价api
 * Created by nixianjing on 17/5/15.
 */
@Scope("prototype")
@RestController
public class SaasOrderController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(SaasOrderController.class);

    @Resource
    private SaasOrderInfoService saasOrderInfoService;

    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private SaasPayUserService saasPayUserService;
    @Resource
    private MainBmikeceService mainBmikeceService;


    /**
     * 抢单-竞价-我的报价分页列表
     * @param page 查看页数
     * @param dataType 数据类型: 1抢单 2竞价 3我的报价
     * @return
     */
    @RequestMapping(value = "/querySaasOrderPage", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_ORDER_PAGE)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object querySaasOrderPage(String page,String dataType) {
        if (page == null || Integer.parseInt(page) <= 0){
            page = "1";
        }
        SassOrderPageRes sassOrderRes = new SassOrderPageRes();
        if(Integer.parseInt(dataType) == 1) {//抢单
            OrderInfoPageParamDTO paramDTO = new OrderInfoPageParamDTO();
            paramDTO.setUserType(new Byte(String.valueOf(UserType.OWNERDRIVER.getCode())));
            paramDTO.setOwnUserId(findUserId());
            paramDTO.setSiteCode(SiteCode.SAAS.getCode());
            paramDTO.setServiceSource(ServiceSource.App.getCode());
            sassOrderRes = saasOrderInfoService.pageRobForReceiving(Integer.parseInt(page),paramDTO);
        }else if(Integer.parseInt(dataType) == 2){//竞价
            OrderInfoPageParamDTO paramDTO = new OrderInfoPageParamDTO();
            paramDTO.setShowMyBidded(false);
            paramDTO.setUserType(new Byte(String.valueOf(UserType.OWNERDRIVER.getCode())));
            paramDTO.setOwnUserId(findUserId());
            paramDTO.setSiteCode(SiteCode.SAAS.getCode());
            paramDTO.setServiceSource(ServiceSource.App.getCode());
            sassOrderRes = saasOrderInfoService.pageBidForReceiving(Integer.parseInt(page),paramDTO);
        }else if(Integer.parseInt(dataType) == 3){//我的报价
            PagePartForMyQuoteDTO paramDTO = new PagePartForMyQuoteDTO();
            paramDTO.setUserType(new Byte(String.valueOf(UserType.OWNERDRIVER.getCode())));
            paramDTO.setOwnUserId(findUserId());
            paramDTO.setSiteCode(SiteCode.SAAS.getCode());
            paramDTO.setServiceSource(ServiceSource.App.getCode());
            sassOrderRes = saasOrderInfoService.pagePartForMyQuote(Integer.parseInt(page),paramDTO);
        }else {
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        PageBase<SassOrderList> pageBase = new PageBase<>();
        pageBase.setListData(sassOrderRes.getListData());
        pageBase.setTotalPage(Integer.parseInt(sassOrderRes.getTotalPage()));
        pageBase.setTotalNum(Integer.parseInt(sassOrderRes.getTotalNum()));
        updRespHeadSuccess(response);
        return pageBase;
    }


    /**
     * 抢单-竞价-我的报价详情ID
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/querySaasOrderById", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_ORDER_INFO)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_INFO)
    public Object querySaasOrderById(Long orderId) {
        if(orderId == null) {
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        SassOrderInfo sassOrderInfo = saasOrderInfoService.getDetailForRobBidByInfoId(orderId);
        sassOrderInfo.setTotalQuantityValue("件");
        sassOrderInfo.setTotalWeightValue("公斤");
        sassOrderInfo.setTotalCubageValue("方");
        updRespHeadSuccess(response);
        return sassOrderInfo;
    }

    /**
     * 抢单-竞价-我的报价详情partId
     * @param partId
     * @return
     */
    @RequestMapping(value = "/querySaasOrderByPartId", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_ORDER_PARTID_INFO)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PARTID_INFO)
    public Object querySaasOrderByPartId(Long partId) {
        if(partId == null) {
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        SassOrderInfo sassOrderInfo = saasOrderInfoService.getDetailForRobBidByPartId(partId);
        sassOrderInfo.setTotalQuantityValue("件");
        sassOrderInfo.setTotalWeightValue("公斤");
        sassOrderInfo.setTotalCubageValue("方");
        updRespHeadSuccess(response);
        return sassOrderInfo;
    }

    /**
     * 抢单
     *
     * @param orderPartId     运单编号
     * @param totalFare 运费总价
     */
    @RequestMapping(value = "/saasOrderRob", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_ORDER_ROB)
    @ResponseBody
    @Log(type = LogEnum.UPDATE_SAAS_ORDER_ROB)
    public Object saasOrderRob(String orderPartId, String totalFare) {
        try {
            if (orderPartId == null || totalFare == null) {
                if (LOG.isErrorEnabled()) LOG.error("改变运单状态失败:开关数据为空或者值不正确.");
               return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUserInfoDTOResponse = driverUserHandlerService.getDriverUserInfo(findUserId());
            DriverUserInfoDTO driverUserInfoDTO = new DriverUserInfoDTO();
            if (!driverUserInfoDTOResponse.isSuccess()) {
                LOG.error("获取司机詳細信息服务调用错误");
            }else{
                driverUserInfoDTO = driverUserInfoDTOResponse.getData();
            }
            if(driverUserInfoDTO.getSubmitType().intValue() != 3) {
                findException(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30402,"抢单失败!司机未认证");
            }
            //开通账户
            if(!saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                String kdwPassWord = mainBmikeceService.getPayPwd(findcapitalAccountId());
                if(!saasPayUserService.saveForOpenAccount(PayUserNatureEnum.PERSON.getCode(),driverUserInfoDTO.getName(),driverUserInfoDTO.getCode(),findUserId().toString(),kdwPassWord)) {
                    findException(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30402);
                }
            }
            OrderPart orderPart = saasOrderInfoService.getOrderPart(new Long(orderPartId));
            SaveGrabSingleDTO saveGrabSingleDTO = new SaveGrabSingleDTO();
            saveGrabSingleDTO.setSiteCode(SiteCode.SAAS.getCode());
            saveGrabSingleDTO.setServiceSource(ServiceSource.App.getCode());
            saveGrabSingleDTO.setTotalFare(new BigDecimal(totalFare));
            OrderPartUserDTO orderPartUserDTO = new OrderPartUserDTO();
            orderPartUserDTO.setOrderPartId(orderPart.getId());
            orderPartUserDTO.setUserType(orderPart.getUserType());
            orderPartUserDTO.setUserId(orderPart.getUserId());
            orderPartUserDTO.setUserName(orderPart.getUserName());
            orderPartUserDTO.setUserMobile(orderPart.getUserMobile());
            orderPartUserDTO.setParentUserId(orderPart.getUserId());
            orderPartUserDTO.setParentUserName(orderPart.getUserName());
            com.cy.pass.service.dto.base.Response<String> findImg = driverUserHandlerService.getImgInfo(findUserId(), (byte) Constants.HEAD_PORTRAIT_IMG);
            if (!findImg.isSuccess()) {
                LOG.error("获取司机头像服务调用错误");
                orderPartUserDTO.setUserHeadImg("");
            }else{
                orderPartUserDTO.setUserHeadImg(findImg.getData());
            }
            orderPartUserDTO.setCarNumber(driverUserInfoDTO.getCarNumber());
            saveGrabSingleDTO.setOrderPartUserDTO(orderPartUserDTO);
            SassOrderInfo sassOrderInfo = saasOrderInfoService.getDetailForRobBidByPartId(new Long(orderPartId));
            Protocol2DTO protocol2DTO = new Protocol2DTO();
            /** 甲方（托运方） {@link WorkPatternEnum#ACCEPT_AUTO} 该类型情况必填 */
            protocol2DTO.setPartA(sassOrderInfo.getCompanyName());
            /** 乙方（承运方） {@link WorkPatternEnum#ACCEPT_AUTO} 该类型情况必填 */
            protocol2DTO.setPartB(driverUserInfoDTO.getName());
            /** 保价 */
            protocol2DTO.setInsured(null);
            /** 保费 */
            protocol2DTO.setPremium(null);
            /** 司机手机号码 {@link WorkPatternEnum#ACCEPT_AUTO} 该类型情况下的司机协议必填 */
            protocol2DTO.setDriverMobile(driverUserInfoDTO.getCode().toString());
            /** 司机身份证号 {@link WorkPatternEnum#ACCEPT_AUTO} 该类型情况下的司机协议必填 */
            protocol2DTO.setDriverIDCard(driverUserInfoDTO.getIdentityLicenseNum());
            /** 车牌号 {@link WorkPatternEnum#ACCEPT_AUTO} 该类型情况下的司机协议必填 */
            protocol2DTO.setCarNumber(driverUserInfoDTO.getCarNumber());
            /** 合同生效日期 */
            protocol2DTO.setEffectiveBegin(new Date());
            /** 合同有效期至 */
            protocol2DTO.setEffectiveEnd(null);
            SettingBusiness settingBusiness = saasOrderInfoService.getByUserId(sassOrderInfo.getCreateParentUserId());
            /** 协议内容，必填 */
            protocol2DTO.setProtocolText(settingBusiness.getDriverOrderProtocolText());
            /** 协议备注 */
            protocol2DTO.setProtocolRemark(null);
            /** 甲方签章 */
            protocol2DTO.setPartyASignet(settingBusiness.getProtocolSign());
            /** 甲方用户id */
            protocol2DTO.setPartyAUserId(sassOrderInfo.getCreateParentUserId());
            /** 乙方签章 */
            protocol2DTO.setPartyBSignet(null);
            /** 乙方用户id */
            protocol2DTO.setPartyBUserId(null);
            saveGrabSingleDTO.setProtocol2DTO(protocol2DTO);
            Response<WaybillInfo> responses = saasOrderInfoService.saveByGrabSingle(saveGrabSingleDTO);
            if(!responses.isSuccess()) {
                findException(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30402,responses.getMessage());
            }
            updRespHeadSuccess(response);
            return null;
        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }

    /**
     * 竞价
     *
     * @param orderPartId 参与方ID
     * @param quote 报价金额
     */
    @RequestMapping(value = "/saasOrderBid", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_ORDER_BID)
    @ResponseBody
    @Log(type = LogEnum.UPDATE_SAAS_ORDER_BID)
    public Object saasOrderBid(String orderPartId, String quote) {
        try {
            if (orderPartId == null || quote == null) {
                if (LOG.isErrorEnabled()) LOG.error("改变运单状态失败:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUserInfoDTOResponse = driverUserHandlerService.getDriverUserInfo(findUserId());
            DriverUserInfoDTO driverUserInfoDTO = new DriverUserInfoDTO();
            if (!driverUserInfoDTOResponse.isSuccess()) {
                LOG.error("获取司机詳細信息服务调用错误");
            }else{
                driverUserInfoDTO = driverUserInfoDTOResponse.getData();
            }
            if(driverUserInfoDTO.getSubmitType().intValue() != 3) {
                findException(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30402,"竞价报价失败!司机未认证");
            }
            //开通账户
            if(!saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                String kdwPassWord = mainBmikeceService.getPayPwd(findcapitalAccountId());
                if(!saasPayUserService.saveForOpenAccount(PayUserNatureEnum.PERSON.getCode(),driverUserInfoDTO.getName(),driverUserInfoDTO.getCode(),findUserId().toString(),kdwPassWord)) {
                    findException(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30401);
                }
            }
            ModifyOrderQuoteDTO modifyOrderQuoteDTO = new ModifyOrderQuoteDTO();
            modifyOrderQuoteDTO.setSiteCode(SiteCode.SAAS.getCode());
            modifyOrderQuoteDTO.setServiceSource(ServiceSource.App.getCode());
            modifyOrderQuoteDTO.setParentUserId(findUserId());
            modifyOrderQuoteDTO.setOrderPartId(new Long(orderPartId));
            modifyOrderQuoteDTO.setQuote(new BigDecimal(quote));
            Response responses = saasOrderInfoService.modifyOrderQuote(modifyOrderQuoteDTO);
            if(!responses.isSuccess()) {
                findException(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30401);
            }
            updRespHeadSuccess(response);
           return null;
        } catch (Exception e) {
            LOG.error("竞价异常", e);
        }
        return findException(response);
    }



}
