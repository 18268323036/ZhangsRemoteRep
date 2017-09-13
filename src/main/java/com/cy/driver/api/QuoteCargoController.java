package com.cy.driver.api;

import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.cargo.service.dto.Enum.QuoteType;
import com.cy.cargo.service.dto.QuoteInfoDTO;
import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.driver.action.BaseAction;
import com.cy.driver.api.domain.req.SaveQuoteInfo;
import com.cy.driver.cloudService.CloudCargoService;
import com.cy.driver.cloudService.CloudQuoteService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.push.PushSdsjbj;
import com.cy.driver.service.CargoHandlerService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.PushSendService;
import com.cy.driver.service.TrackPointService;
import com.cy.location.service.DriverLinePointService;
import com.cy.location.service.dto.ViewCargoLineSaveDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.CarrierQuoteDTO;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.Cargo2DTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxy on 2016/6/13.
 */

@Scope("prototype")
@Controller
public class QuoteCargoController extends BaseAction {

    @Resource
    private CloudCargoService cloudCargoService;
    @Resource
    private CargoHandlerService cargoHandleService;
    @Resource
    private PushSendService pushSendService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private CloudQuoteService cloudQuoteService;
    @Resource
    private TrackPointService trackPointService;
    @Resource
    private DriverLinePointService driverLinePointService;


    private Logger LOG = LoggerFactory.getLogger(QuoteCargoController.class);

//    /**
//     * 我的报价列表
//     * @param page
//     * @return
//     */
//    @RequestMapping(value = "/queryMyQuoteList", method = RequestMethod.POST)
//    @ReqRespHeadCode(reqHeadCode1 = ApiReqCodeEnum.MY_CARGO_QUOTE)
//    public Object queryMyQuoteList(Integer page) {
//            Long driverId = findUserId();
//            if (page == null || page.intValue() <= 0L)
//                page = 1;
//
//            PageInfo<QuoteUserDTO> pageInfo = new PageInfo<>(page);
//            QuoteUserDTO quoteUserDTO = new QuoteUserDTO();
//            quoteUserDTO.setUserId(driverId.toString());
//            quoteUserDTO.setUserType(Constants.USER_TYPE_KDWSJ);
//            pageInfo.setData(quoteUserDTO);
//
//        return  myQuoteInfoHandleService.queryMyQuotedList(pageInfo);
//    }


    /**
     * 保存报价信息
     *
     * @param saveQuoteInfo
     * @return
     */
    @RequestMapping(value = "/cloudAddQuoteInfo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_QUOTE_INFO)
    @ResponseBody
    @Log(type = LogEnum.ADD_QUOTE_INFO)
    public Object addQuoteInfo(SaveQuoteInfo saveQuoteInfo, Integer cargoSource, HttpServletResponse response) {
        //校验
        if (saveQuoteInfo == null || saveQuoteInfo.getQuoteType() == null || saveQuoteInfo.getQuoteAmount() == null || saveQuoteInfo.getQuoteAmount().compareTo(BigDecimal.ZERO) == 0
                || StringUtils.isBlank(QuoteType.contain(saveQuoteInfo.getQuoteType().byteValue(), QuoteType.values())) || cargoSource == null) {
            if (LOG.isErrorEnabled()) LOG.error("保存司机报价信息校验:报价数据为空或者值不正确.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        try {
            Long driverId = findUserId();
            Byte authState = findAuthState();
            Map<String,Object> map = new HashMap<>();
            boolean isSuccess = false;
            if (cargoSource == 2) {
                IdSourceDTO idSourceDTO = new IdSourceDTO();
                idSourceDTO.setSource(cargoSource);
                idSourceDTO.setId(saveQuoteInfo.getCargoId());
                Cargo2DTO cargo2DTO = cloudCargoService.cargoDetails2(idSourceDTO);
                if (cargo2DTO == null) {
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }
                CarrierQuoteDTO carrierQuoteDTO = new CarrierQuoteDTO();
                carrierQuoteDTO.setCarrierId(saveQuoteInfo.getCargoId());
                carrierQuoteDTO.setQuoteFair(saveQuoteInfo.getQuoteAmount());
                carrierQuoteDTO.setQuoteType(saveQuoteInfo.getQuoteType());
                carrierQuoteDTO.setUserId(String.valueOf(driverId));
                carrierQuoteDTO.setUserAuthState(authState);
                carrierQuoteDTO.setUserType((byte) Constants.USER_TYPE_QUPSYH);
                carrierQuoteDTO.setRemark(saveQuoteInfo.getRemark());
                carrierQuoteDTO.setUserName(cargo2DTO.getDeployUserName());
                carrierQuoteDTO.setUserMobile(cargo2DTO.getContactMobilephone());
                Response<String> findImg = driverUserHandlerService.getImgInfo(driverId, (byte) Constants.HEAD_PORTRAIT_IMG);
                if (!findImg.isSuccess()) {
                    LOG.error("获取司机头像服务调用错误");
                }
                Response<DriverUserInfoDTO> findDriverInfo = driverUserHandlerService.getDriverUserInfo(driverId);
                if (!findDriverInfo.isSuccess()) {
                    LOG.error("获取司机信息服务调用错误");
                }
                carrierQuoteDTO.setUserHeadImg(findImg.getData());
                carrierQuoteDTO.setUserCarNumber(findDriverInfo.getData().getCarNumber());
                carrierQuoteDTO.setStart((byte) 0);
                CarrierAssemDTO carrierAssemDTO = cloudCargoService.cargoDetailsRdc(saveQuoteInfo.getCargoId());
                //3.5保存司机路线（报价）（旧，暂不删除）
                if(carrierAssemDTO!=null){
                    trackPointService.saveLine(driverId,carrierAssemDTO, Constants.QUOTE_CARGO);
                }
                //3.5保存司机路线（报价）（新）
                if(carrierAssemDTO!=null){
                    ViewCargoLineSaveDTO viewCargoLineSaveDTO = new ViewCargoLineSaveDTO();
                    viewCargoLineSaveDTO.setDriverId(driverId);
                    viewCargoLineSaveDTO.setCargoId(saveQuoteInfo.getCargoId());
                    viewCargoLineSaveDTO.setViewType(2);//保存报价时埋点类型
                    viewCargoLineSaveDTO.setCargoSource(2);//来源云配
                    viewCargoLineSaveDTO.setStartTime(carrierAssemDTO.getInfoDTO().getNeedStartTime());
                    viewCargoLineSaveDTO.setStartProvince(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue());
                    viewCargoLineSaveDTO.setStartCity(carrierAssemDTO.getAddressDTO().getDepartureCityValue());
                    viewCargoLineSaveDTO.setEndProvince(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue());
                    viewCargoLineSaveDTO.setEndCity(carrierAssemDTO.getAddressDTO().getReceiveCityValue());
                    driverLinePointService.saveViewCargoLine(viewCargoLineSaveDTO);
                }

                if(cloudQuoteService.saveQuoteInfo(carrierQuoteDTO)){
                    updRespHeadSuccess(response);
                    return map;
                }else {
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }
            } else {
                QuoteInfoDTO infoDTO = new QuoteInfoDTO();
                infoDTO.setCargoId(saveQuoteInfo.getCargoId());
                infoDTO.setDriverId(driverId);
                infoDTO.setQuoteType(saveQuoteInfo.getQuoteType());
                infoDTO.setQuoteFair(saveQuoteInfo.getQuoteAmount());
                com.cy.cargo.service.dto.base.Response<Boolean> serResponse = cargoHandleService.addQuoteInfo(infoDTO);
                isSuccess = serResponse.isSuccess() && serResponse.getData();
                //查询货源详情，单独保存货源详情信息中的线路信息
                com.cy.cargo.service.dto.base.Response<CargoInfoDTO> getCargoInfoRes = cargoHandleService.getCargoInfo(saveQuoteInfo.getCargoId());
                if (getCargoInfoRes.isSuccess() && getCargoInfoRes.getData() != null) {
                    CargoInfoDTO cargoInfoDTO = getCargoInfoRes.getData();
                    //保存司机报价的货源的路线
                    trackPointService.saveLine(driverId, cargoInfoDTO);

                    //3.5保存司机路线(查看货源详情)(新)
                    ViewCargoLineSaveDTO viewCargoLineSaveDTO = new ViewCargoLineSaveDTO();
                    viewCargoLineSaveDTO.setDriverId(driverId);
                    viewCargoLineSaveDTO.setCargoId(saveQuoteInfo.getCargoId());
                    viewCargoLineSaveDTO.setViewType(2);//保存报价时埋点类型
                    viewCargoLineSaveDTO.setCargoSource(1);//来源快到网
                    viewCargoLineSaveDTO.setStartTime(cargoInfoDTO.getRequestStartTime());
                    viewCargoLineSaveDTO.setStartProvince(cargoInfoDTO.getStartProvince());
                    viewCargoLineSaveDTO.setStartCity(cargoInfoDTO.getStartCity());
                    viewCargoLineSaveDTO.setEndProvince(cargoInfoDTO.getEndProvince());
                    viewCargoLineSaveDTO.setEndCity(cargoInfoDTO.getEndCity());
                    driverLinePointService.saveViewCargoLine(viewCargoLineSaveDTO);

                } else {
                    LOG.debug("查询货源详情失败");
                }
                if(serResponse.getCode()==CodeTable.NOT_EXISTS_CARGO.getCode()){
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20116);
                }else if(serResponse.getCode()==CodeTable.ERROR_SAVE_QUOTE_DRIVER_CONFIRMING.getCode() || serResponse.getCode()==CodeTable.ERROR_SAVE_QUOTE_HAS_ORDER.getCode()){
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20117);
                }
                if (isSuccess) {
                    /** 保存报价成功的推送 */
                    onlineQuotePush(saveQuoteInfo.getCargoId(), saveQuoteInfo.getQuoteType(), saveQuoteInfo.getQuoteAmount());
                    updRespHeadSuccess(response);
                      return map;
                } else {
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) LOG.error("保存司机报价出现异常.", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }


    /**
     * 保存报价成功的推送
     *
     * @param cargoId     货源id
     * @param quoteType   报价类型
     * @param quoteAmount 报价金额
     * @return
     * @author wyh
     */
    private boolean onlineQuotePush(Long cargoId, Byte quoteType, BigDecimal quoteAmount) {
        try {
            com.cy.cargo.service.dto.base.Response<CargoInfoDTO> result = cargoHandleService.getCargoInfo(cargoId);
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


//    /**
//     * 报价分页列表
//     *
//     * @param cargoId
//     * @param page
//     * @return
//     */
//    @RequestMapping(value = "/quotedInfo", method = RequestMethod.POST)
//    @ReqRespHeadCode(reqHeadCode1 = ApiReqCodeEnum.QUOTED_INFO)
//    public Object quotedInfo(String cargoId, Integer page, Integer cargoSource) {
//        if (StringUtils.isBlank(cargoId)) {
//            throw ValidParamException.paramError("货源id不能为空");
//        }
//        if (page == null || page.intValue() < 1) {
//            throw ValidParamException.paramError("分页page不合法");
//        }
//        Long driverId = findUserId();
//        return myQuoteInfoHandleService.queryQuotedInfo(Long.valueOf(cargoId),driverId,cargoSource,page);
//    }


}
