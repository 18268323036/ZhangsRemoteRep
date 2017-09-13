package com.cy.driver.action;

import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.cargo.service.dto.DriverCargoAssessInfoDTO;
import com.cy.cargo.service.dto.Enum.CargoAssessType;
import com.cy.cargo.service.dto.Enum.QuoteType;
import com.cy.cargo.service.dto.QuoteInfoDTO;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.CargoOwnerBO;
import com.cy.driver.domain.push.PushSdsjbj;
import com.cy.driver.service.*;
import com.cy.pass.service.dto.CompanyInfoDTO;
import com.cy.pass.service.dto.OwnerItemStatDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.pass.service.dto.base.CodeTable;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 司机与货源处理类
 * Created by mr on 2015/7/10.
 */
@Scope("prototype")
@Controller("driverCargoAction")
public class DriverCargoAction extends BaseAction{

    private Logger LOG = LoggerFactory.getLogger(DriverCargoAction.class);
    @Resource
    private CargoHandlerService cargoHandleService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private PushSendService pushSendService;
    @Resource
    private OwnerItemService ownerItemService;
    @Resource
    private DriverLineTrackService driverLineTrackService;



    /**
     * 保存报价
     * @param request
     * @param response
     * @param cargoId
     * @param quoteType
     * @param quoteAmount
     * @return
     */
    @RequestMapping(value = "/onlineQuote", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_ONLNE_QUOTE)
    @Log(type = LogEnum.ONLINE_QUOTE)
    public Object onlineQuote(HttpServletRequest request, HttpServletResponse response, Long cargoId, Byte quoteType, BigDecimal quoteAmount) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        //校验
        if(cargoId == null || quoteType == null || quoteAmount == null || quoteAmount.compareTo(BigDecimal.ZERO)==0
                ||StringUtils.isBlank(QuoteType.contain(quoteType, QuoteType.values()))){
            if (LOG.isErrorEnabled()) LOG.error("保存司机报价信息校验:开关数据为空或者值不正确.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        try{
            Long driverId = findUserId(request);

            QuoteInfoDTO infoDTO = new QuoteInfoDTO();
            infoDTO.setCargoId(cargoId);
            infoDTO.setDriverId(driverId);
            infoDTO.setQuoteType(quoteType);
            infoDTO.setQuoteFair(quoteAmount);
            Response<Boolean> serResponse = cargoHandleService.addQuoteInfo(infoDTO);
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == com.cy.cargo.service.dto.base.CodeTable.ERROR.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("保存司机报价(服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                //报价失败，货源已产生订单
                if(serResponse.getCode() == com.cy.cargo.service.dto.base.CodeTable.NOT_EXISTS_CARGO.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("保存司机报价(服务端)校验信息："+serResponse.getCode()+","+serResponse.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20066);
                }
                //报价失败，货主已确定你的车，请前往未承运订单确定应承
                if(serResponse.getCode() == com.cy.cargo.service.dto.base.CodeTable.ERROR_SAVE_QUOTE_DRIVER_CONFIRMING.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("保存司机报价(服务端)校验信息："+serResponse.getCode()+","+serResponse.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20081);
                }
                if(serResponse.getCode() == com.cy.cargo.service.dto.base.CodeTable.ERROR_SAVE_QUOTE_HAS_ORDER.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("保存司机报价(服务端)校验信息："+serResponse.getCode()+","+serResponse.getMessage());
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20080);
                }
                if(serResponse.getCode() == com.cy.cargo.service.dto.base.CodeTable.LOCK_WAIT.getCode()){}
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20003);
            }

            //查询货源详情，单独保存货源详情信息中的线路信息
            Response<CargoInfoDTO> getCargoInfoRes = cargoHandleService.getCargoInfo(cargoId);
            if(getCargoInfoRes.isSuccess()){
                CargoInfoDTO cargoInfoDTO = getCargoInfoRes.getData();
                //保存司机报价的货源的路线
                com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId,cargoInfoDTO, Constants.QUOTE_CARGO);
                if(!saveLineRes.isSuccess()){
                    LOG.debug("locationLineService服务调用失败");
                }
                //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
                String requestTime = DateUtil.dateToStr(cargoInfoDTO.getRequestStartTime());
                String today = DateUtil.dateToStr(new Date());
                if(requestTime.equals(today)){
                    com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId,cargoInfoDTO, Constants.QUOTE_CARGO);
                    if(!saveDayLineRes.isSuccess()){
                        LOG.debug("driverDayLineService服务调用失败");
                    }
                }
            }else{
                LOG.debug("查询货源详情失败");
            }

            if(serResponse.getData()){
                /** 保存报价成功的推送 */
                onlineQuotePush(cargoId, quoteType, quoteAmount);
                updRespHeadSuccess(response);
                return resultMap;
            }else{
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
            }
        }catch (Exception e){
            if(LOG.isErrorEnabled())LOG.error("保存司机报价出现异常.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 保存报价成功的推送
     * @param cargoId 货源id
     * @param quoteType 报价类型
     * @param quoteAmount 报价金额
     * @return
     * @author wyh
     */
    private boolean onlineQuotePush(Long cargoId, Byte quoteType,BigDecimal quoteAmount){
        try {
            Response<CargoInfoDTO> result = cargoHandleService.getCargoInfo(cargoId);
            if (result == null) {
                LOG.error("保存报价成功的推送失败，调用cargo服务查询货源信息出错");
                return false;
            }
            if (!result.isSuccess()) {
                LOG.error("保存报价成功的推送失败，调用cargo服务查询货源信息失败，返回信息={}", result.getMessage());
                return false;
            }
            if (result.getData() == null) {
                LOG.error("保存报价成功的推送失败，调用cargo服务查询货源信息失败，返回的货源信息为空,cargoId={}", cargoId);
                return false;
            }
            CargoInfoDTO cargoDTO = result.getData();
            PushSdsjbj info = new PushSdsjbj();
            info.setUserId(cargoDTO.getDeployUserid());
            info.setBusinessId(cargoDTO.getId());
            info.setCargoName(cargoDTO.getCargoName());
            info.setStartCity(cargoDTO.getStartCity());
            info.setEndCity(cargoDTO.getEndCity());
            info.setQuoteMoney(quoteAmount);
            return pushSendService.sdsjbjPushOwn(info);
        } catch (Exception e) {
            LOG.error("保存报价成功的推送出现异常", e);
        }
        return false;
    }

    /**
     *货物点评
     * @param request
     * @param response
     * @param cargoId
     * @param commentType
     * @param commentRemark
     * @return
     */
    @RequestMapping(value = "/cargoComment", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_CARGO_COMMENT)
    public Object cargoComment(HttpServletRequest request, HttpServletResponse response, Long cargoId, Byte commentType, String commentRemark) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        //校验
        if(cargoId == null ||commentType == null || StringUtils.isBlank(CargoAssessType.contain(commentType, CargoAssessType.values()))){
            if (LOG.isErrorEnabled()) LOG.error("保存货物点评信息校验:开关数据为空或者值不正确.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        try{
            Long driverId = findUserId(request);

            DriverCargoAssessInfoDTO infoDTO = new DriverCargoAssessInfoDTO();
            infoDTO.setCargoId(cargoId);
            infoDTO.setDriverId(driverId);
            infoDTO.setType(commentType);
            infoDTO.setAssessInfo(commentRemark);
            Response<Boolean> serResponse = cargoHandleService.addCargoAssess(infoDTO);
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("保存货物点评(服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return resultMap;
        }catch (Exception e){
            if(LOG.isErrorEnabled())LOG.error("保存货物点评出现异常.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 查询发货人详情
     * @param request
     * @param response
     * @param companyId
     * @param webUserId
     * @param cargoId
     * @return
     */
    @RequestMapping(value = "/lookConsignorDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_GET_WEB_USER_INFO)
    @Log(type = LogEnum.LOOK_CONSIGNOR_DETAILS)
    public Object lookConsignorDetails(HttpServletRequest request, HttpServletResponse response, Long companyId, Long webUserId, Long cargoId) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if(companyId == null || cargoId == null){
            if (LOG.isErrorEnabled()) LOG.error("查询发货人详情信息校验:必填参数为空。");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        CargoOwnerBO cargoOwnerBO = new CargoOwnerBO();
        try{
            com.cy.pass.service.dto.base.Response<CompanyInfoDTO> comResponse = webUserHandleService.getCompanyDetail(companyId);
            if(!comResponse.isSuccess()){
                updRespHeadError(response);
                if(comResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("查询发货人企业详情(pass服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if(comResponse.getData() != null){
                CompanyInfoDTO infoDTO = comResponse.getData();
                if(companyId.intValue() != 1){
                    cargoOwnerBO.setCompanyName(infoDTO.getCompanyName());
                    cargoOwnerBO.setCompanyAddress(infoDTO.getCompanyAddress());
                }else{
                    cargoOwnerBO.setCompanyAddress("");
                }
            }

            com.cy.pass.service.dto.base.Response<WebUserInfoDTO> userResponse = webUserHandleService.getWebUserByCompanyId(companyId);
            if(!userResponse.isSuccess()){
                updRespHeadError(response);
                if(userResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("查询发货人详情(pass服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if(userResponse != null){
                WebUserInfoDTO infoDTO = userResponse.getData();
                cargoOwnerBO.setAuthStatus(infoDTO.getSubmitType() == null ? null : infoDTO.getSubmitType().toString());
                if(infoDTO.getSubmitType() != null && infoDTO.getSubmitType().intValue() == 3){
                    cargoOwnerBO.setAuthTime(DateUtil.dateFormat(infoDTO.getAuditTime(), DateUtil.F_DATE));
                }
            }

            Response<CargoInfoDTO> cargoResponse = cargoHandleService.getCargoInfo(cargoId);
            if(!cargoResponse.isSuccess()){
                updRespHeadError(response);
                if(comResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (LOG.isErrorEnabled()) LOG.error("查询发货人详情(cargo服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if(cargoResponse != null){
                CargoInfoDTO infoDTO = cargoResponse.getData();
                if(infoDTO!=null){
                    cargoOwnerBO.setTelephone(infoDTO.getContactTelephone());
                    cargoOwnerBO.setContactName(infoDTO.getContactName());
                    cargoOwnerBO.setMobile(infoDTO.getContactMobilephone());

                    if(companyId.intValue() == 1){
                        //爬虫爬的货的企业名称=货源信息中的企业名称
                        cargoOwnerBO.setCompanyName(infoDTO.getCompanyName());
                        cargoOwnerBO.setAuthStatus("0");
                        cargoOwnerBO.setAuthTime("");
                    }
                }
            }

            //信用
            OwnerItemStatDTO ownerItemStatDTO = ownerItemService.getByOwnerId(webUserId);
            if(ownerItemStatDTO != null){
                cargoOwnerBO.setCredit(ownerItemStatDTO.getCreditGrade());
            }
            resultMap.put("cargoOwner",cargoOwnerBO);

            updRespHeadSuccess(response);
            return resultMap;
        }catch (Exception e){
            if(LOG.isErrorEnabled())LOG.error("查询发货人详情出现异常.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }
}
