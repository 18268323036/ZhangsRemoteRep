package com.cy.driver.api;

import com.cy.driver.action.BaseAction;
import com.cy.driver.api.convert.TrackingSaveConvert;
import com.cy.driver.api.convert.WayBillConvert;
import com.cy.driver.api.domain.res.WayBillList;
import com.cy.driver.api.domain.res.WaybillOrderRes;
import com.cy.driver.cloudService.CloudCommonWaybillService;
import com.cy.driver.cloudService.CloudSaveTrackingService;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.driver.cloudService.WaybillOrderService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.push.PushBase;
import com.cy.driver.service.IdSourceDTOService;
import com.cy.driver.service.PushSendService;
import com.cy.driver.service.WebUserHandleService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.rdcservice.service.InstationMessageService;
import com.cy.rdcservice.service.WaybillInfoService;
import com.cy.rdcservice.service.dto.*;
import com.cy.search.service.dto.request.DriverIdAndKeywordDTO;
import com.cy.search.service.dto.response.OrderInfoDTO;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 转单运单业务
 * Created by nixianjing on 16/8/2.
 */
@Scope("prototype")
@Controller
public class WaybillOrderController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(WaybillOrderController.class);


    @Resource
    private WaybillOrderService waybillOrderService;

    @Resource
    private PushSendService pushSendService;

    @Resource
    private WebUserHandleService webUserHandleService;

    @Resource
    private CloudUserService cloudUserService;

    @Resource
    private CloudCommonWaybillService cloudCommonWaybillService;

    @Resource
    private IdSourceDTOService idSourceDTOService;

    @Resource
    private WaybillInfoService waybillInfoService;

    @Resource
    private CloudSaveTrackingService cloudSaveTrackingService;

    @Resource
    private InstationMessageService instationMessageService;

    /**
     * 搜索运单列表
     * @return
     */
    @RequestMapping(value = "/queryWaybillList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_WAYBILL_LIST)
    @ResponseBody
    @Log(type = LogEnum.QUERY_WAYBILL_LIST)
    public Object queryWaybillList(String keyword,String page){
        try{
            if(StringUtils.isEmpty(keyword) || StringUtils.isEmpty(page)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单列表查询失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            PageBase<WayBillList> pageBase = new PageBase<WayBillList>();
            DriverIdAndKeywordDTO driverIdAndKeywordDTO = new DriverIdAndKeywordDTO();
            driverIdAndKeywordDTO.setDriverId(findUserId());
            driverIdAndKeywordDTO.setKeyword(keyword);
            com.cy.search.service.dto.base.PageInfo<DriverIdAndKeywordDTO> pageInfo = new com.cy.search.service.dto.base.PageInfo<DriverIdAndKeywordDTO>(Integer.parseInt(page));
            pageInfo.setData(driverIdAndKeywordDTO);
            Map<String, Object> map = (Map<String, Object>)waybillOrderService.queryWaybillList(pageInfo);
            List<OrderInfoDTO> orderInfoDTOs = (List<OrderInfoDTO>) map.get("dataList");
            List<WayBillList> wayBillLists = null;
            if (orderInfoDTOs == null || orderInfoDTOs.size() == 0) {
                wayBillLists = null;
                updRespHeadSuccess(response);
                return null;
            }
            wayBillLists = WayBillConvert.wayBillListConvert(orderInfoDTOs);
            for (WayBillList wayBillList : wayBillLists) {
                if(wayBillList.getOrderSource().equals(Constants.ORDER_HAS_UNLOAD)) {
                    List<Long> wayBillNums = new ArrayList<>();
                    wayBillNums.add(Long.valueOf(wayBillList.getOrderId()));
                    List<Integer> signInInfo = cloudCommonWaybillService.querySignInInfo(wayBillNums);
                    if (signInInfo != null || signInInfo.size() != 0) {
                        if (wayBillList.getOrderSource() == 2) {
                            wayBillList.setWaitSignIn(signInInfo.get(0));
                        }
                    }
                }
                if (wayBillList.getOrderSource() == 1) {
                    WebUserInfoDTO webUserInfoDTO = webUserHandleService.getWebUserInfo(wayBillList.getCreateOwnUserId());
                    if(webUserInfoDTO!=null){
                        wayBillList.setAuthState(Integer.valueOf(webUserInfoDTO.getSubmitType()));
                    }
                } else {
                    UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(wayBillList.getCreateOwnUserId());
                    if(userLoginInfoDTO!=null){
                        wayBillList.setAuthState(Integer.valueOf(userLoginInfoDTO.getSubmitType()));
                    }
                }
            }
            pageBase.setListData(wayBillLists);
            pageBase.setTotalPage(Integer.parseInt(map.get("totalPage").toString()));
            pageBase.setTotalNum(Integer.parseInt(map.get("totalRecord").toString()));
            updRespHeadSuccess(response);
            return pageBase;
        }catch (Exception e){
            LOG.error("获取搜索运单列表异常", e);
        }
        return findException(response);
    }

    /**
     * 转单运单数量
     * @return
     */
    @RequestMapping(value = "/findWaybillCount", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CONVERT_ORDER_WAY_BILL_NUMBER)
    @ResponseBody
    @Log(type = LogEnum.CONVERT_ORDER_WAY_BILL_NUMBER)
    public Object findWaybillCount(){
        try{
            List<Integer> statcList = new ArrayList<Integer>();
            statcList.add(Constants.WAYBILL_ORDER_WAIT);
            statcList.add(Constants.WAYBILL_ORDER_WAIT_LOAD);
            Object object = waybillOrderService.countTurnOrderByDriverId(findUserId(),statcList);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return object;
        }catch (Exception e){
            LOG.error("获取转单运单数量异常", e);
        }
        return findException(response);
    }


    /**
     * 转单运单列表
     * @return
     */
    @RequestMapping(value = "/findWaybillPage", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_WAYBILL_PAGE)
    @ResponseBody
    @Log(type = LogEnum.FIND_WAYBILL_PAGE)
    public Object findWaybillPage(String state,String page){
        try{
            if(StringUtils.isEmpty(state) || StringUtils.isEmpty(page)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单列表查询失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            TurnOrderQueryDTO turnOrderQueryDTO = new TurnOrderQueryDTO();
            turnOrderQueryDTO.setDriverId(findUserId());
            turnOrderQueryDTO.setState(Integer.parseInt(state));
            Object object = waybillOrderService.pageTurnOrderByDriverList(Integer.parseInt(page),turnOrderQueryDTO);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return object;
        }catch (Exception e){
            LOG.error("获取转单运单列表异常", e);
        }
        return findException(response);
    }

    /**
     * 转单运单详情
     * @return
     */
    @RequestMapping(value = "/findWaybillById", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_BAYILL_BY_ID)
    @ResponseBody
    @Log(type = LogEnum.FIND_BAYILL_BY_ID)
    public Object findWaybillById(String orderId){
        try{
            if(StringUtils.isEmpty(orderId)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单详情查询失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Object object = waybillOrderService.findTurnOrderByOrderId(orderId);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return object;
        }catch (Exception e){
            LOG.error("获取转单运单详情异常", e);
        }
        return findException(response);
    }

    /**
     * 修改转单运单运费
     * @return
     */
    @RequestMapping(value = "/updateWaybillFare", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_WAYBILL_FARE)
    @ResponseBody
    @Log(type = LogEnum.UPDATE_WAYBILL_FARE)
    public Object updateWaybillFare(String orderId,String totalFare,String prepayFare){
        try{
            if(StringUtils.isEmpty(orderId) || StringUtils.isEmpty(totalFare) || StringUtils.isEmpty(prepayFare)) {
                if (LOG.isErrorEnabled()) LOG.error("修改转单运单报价运费失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            DriverUserInfoDTO driverUserInfoDTO = waybillOrderService.getDriverUserInfo(findUserId());
            if(driverUserInfoDTO == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            WaybillModifyFareDTO waybillModifyFareDTO = new WaybillModifyFareDTO();
            waybillModifyFareDTO.setWaybillId(Long.valueOf(orderId));
            waybillModifyFareDTO.setWaybillUserType(1);//订单的所属人类型（1运单发起人、2运单承运方）
            waybillModifyFareDTO.setTotalFare(BigDecimal.valueOf(Double.valueOf(totalFare)));
            waybillModifyFareDTO.setPrepayFare(BigDecimal.valueOf(Double.valueOf(prepayFare)));
            waybillModifyFareDTO.setModifyUserId(findUserId());//修改用户id，必填
            waybillModifyFareDTO.setModifyOwnUserId(findUserId());
            waybillModifyFareDTO.setModifyUserAccountType(null);//修改用户帐户类型（0主帐户、1子账户、null无账户类型）
            waybillModifyFareDTO.setModifyUserName(driverUserInfoDTO.getName());//修改用户名称
            waybillModifyFareDTO.setModifyUserType(new Byte("1"));//修改用类型（1快到网司机 2快到网货主 3区域配送用户），必填
            Object object = waybillOrderService.updateFare(waybillModifyFareDTO);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if(!(boolean)object) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30304);
            }
            updRespHeadSuccess(response);
            idSourceDTOService.updateIndex(orderId, Constants.ORDER_FROM_QYPS);
            return null;
        }catch (Exception e){
            LOG.error("修改转单运单报价异常", e);
        }
        return findException(response);
    }


    /**
     * 确认报价
     * @return
     */
    @RequestMapping(value = "/updateWaybillFareState", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_WAYBILL_FARE_STATE)
    @ResponseBody
    @Log(type = LogEnum.UPDATE_WAYBILL_FARE_STATE)
    public Object updateWaybillFareState(String orderId,String state,String longitude, String latitude, String province, String city, String county, String address){
        try{
            if(StringUtils.isEmpty(orderId) || StringUtils.isEmpty(state)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单确认承运方报价失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            DriverUserInfoDTO driverUserInfoDTO = waybillOrderService.getDriverUserInfo(findUserId());
            if(driverUserInfoDTO == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            ModifyUserDTO modifyUserDTO = new ModifyUserDTO();
            modifyUserDTO.setModifyUserId(findUserId());//修改用户id，必填
            modifyUserDTO.setModifyUserAccountType(null);//修改用户帐户类型（0主帐户、1子账户、null无账户类型）
            modifyUserDTO.setModifyUserName(driverUserInfoDTO.getName());//修改用户名称
            modifyUserDTO.setModifyUserType(new Byte("1"));//修改用类型（1快到网司机 2快到网货主 3区域配送用户），必填
            Object object = waybillOrderService.useTransportQuote(orderId,state,modifyUserDTO);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if(!(boolean)object) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30305);
            }
            //1是确认接单的标识
            TrackingSaveDTO trackingSaveDTO = TrackingSaveConvert.makeTrackingSaveDTO("1", province, city, county, address, longitude, latitude);
            boolean isSuccess = cloudSaveTrackingService.saveWaybillTrackingByDrvier(Long.valueOf(orderId),trackingSaveDTO,modifyUserDTO);
            if(!isSuccess){
                LOG.isDebugEnabled();  LOG.debug("保存运单轨迹失败");
            }
            updRespHeadSuccess(response);
            idSourceDTOService.updateIndex(orderId, Constants.ORDER_FROM_QYPS);
            return null;
        }catch (Exception e){
            LOG.error("修改转单运单承运方报价状态异常", e);

        }
        return findException(response);
    }

    /**
     * 转单运单提醒接单
     * @return
     */
    @RequestMapping(value = "/pushWaybillById", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PUSH_WAYBILL_BY_ID)
    @ResponseBody
    @Log(type = LogEnum.PUSH_WAYBILL_BY_ID)
    public Object pushWaybillById(String orderId){
        try{
            if(StringUtils.isEmpty(orderId)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单提醒接单失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            WaybillOrderRes waybillOrderRes = (WaybillOrderRes)waybillOrderService.findTurnOrderByOrderId(orderId);
            if("1".equals(waybillOrderRes.getTransportType())) {
                PushBase pushBase = new PushBase();
                pushBase.setBusinessId(Long.valueOf(waybillOrderRes.getOrderId()));
                pushBase.setUserId(Long.valueOf(waybillOrderRes.getTransportUserId()));
                pushSendService.waybillPushOwn(pushBase,waybillOrderRes.getTransportUserName());
            }else {
                DriverUserInfoDTO driverUserInfoDTO = waybillOrderService.getDriverUserInfo(findUserId());
                if(driverUserInfoDTO == null) {
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
                }
                MessageBaseDTO messageBaseDTO = new MessageBaseDTO();
                messageBaseDTO.setSiteCode(waybillOrderRes.getSiteCode());
                messageBaseDTO.setUserId(Long.valueOf(waybillOrderRes.getTransportUserId()));
                messageBaseDTO.setUserName(waybillOrderRes.getTransportUserName());
                messageBaseDTO.setUserHeadImg(waybillOrderRes.getTransportHeadImgUrl());
                messageBaseDTO.setOwnUserId(Long.valueOf(waybillOrderRes.getTransportOwnUserId()));
                messageBaseDTO.setRemark("");
                messageBaseDTO.setCreateUserId(findUserId());
                messageBaseDTO.setCreateUserName(driverUserInfoDTO.getName());
                instationMessageService.remindOrderInsert(messageBaseDTO,waybillOrderRes.getTransportUserName(),driverUserInfoDTO.getName());
            }
            idSourceDTOService.updateIndex(orderId, Constants.ORDER_FROM_QYPS);
            updRespHeadSuccess(response);
            return null;
        }catch (Exception e){
            LOG.error("修改转单运单报价异常", e);
        }
        return findException(response);
    }


    /**
     * 保存转单运单评价
     * @return
     */
    @RequestMapping(value = "/addWaybillAssess", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_WAYBILL_ASSESS)
    @ResponseBody
    @Log(type = LogEnum.ADD_WAYBILL_ASSESS)
    public Object addWaybillAssess(String orderId,String assessScore,String content){
        try{
            if(StringUtils.isEmpty(orderId) || StringUtils.isEmpty(assessScore)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单详情查询失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            WaybillAssessSaveDTO waybillAssessSaveDTO = new WaybillAssessSaveDTO();
            waybillAssessSaveDTO.setWaybillId(Long.parseLong(orderId));
            waybillAssessSaveDTO.setAssessUserType(1);//评价用户类型（1运单发起人、2运单承运方）
            waybillAssessSaveDTO.setAssessScore(Integer.parseInt(assessScore));//评价分数
            waybillAssessSaveDTO.setContent(content);//评价内容
            waybillAssessSaveDTO.setCreateTime(new Date());//创建时间
            waybillAssessSaveDTO.setNameHidden(new Byte("0"));//是否匿名（0否 1是）
            Object object = waybillOrderService.saveAssess(waybillAssessSaveDTO);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            idSourceDTOService.updateIndex(orderId, Constants.ORDER_FROM_QYPS);
            return null;
        }catch (Exception e){
            LOG.error("保存转单运单评价异常", e);
        }
        return findException(response);
    }


    /**
     * 转单运单查看评价
     * @return
     */
    @RequestMapping(value = "/findWaybillAssessById", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_WAYBILL_ASSESS_BY_ID)
    @ResponseBody
    @Log(type = LogEnum.FIND_WAYBILL_ASSESS_BY_ID)
    public Object findWaybillAssessById(String orderId){
        try{
            if(StringUtils.isEmpty(orderId)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单查看评价失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Object object = waybillOrderService.findAssessByWaybillId(Long.valueOf(orderId));
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return object;
        }catch (Exception e){
            LOG.error("查询转单运单评价列表异常", e);
        }
        return findException(response);
    }


    /**
     * 转单运单轨迹描点
     * @return
     */
    @RequestMapping(value = "/findWaybillPathById", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_WAYBILL_PATH_BY_ID)
    @ResponseBody
    @Log(type = LogEnum.FIND_WAYBILL_PATH_BY_ID)
    public Object findWaybillPathById(String orderId){
        try{
            if(StringUtils.isEmpty(orderId)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹描点失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            /**
             * 查询运单信息
             */
            Response<WaybillDetailDTO> responseew = waybillInfoService.queryWaybillDetail(Long.valueOf(orderId));
            if(responseew.getCode() != 0 || responseew.getData() == null) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表查询运单信息失败!");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            /**
             * 判断承运方是不是司机
             */
            if(responseew.getData().getWaybillInfoDTO().getTransportType().intValue() != 1) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30302);
            }
            Object object = waybillOrderService.findWaybillPathById(orderId);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return object;
        }catch (Exception e){
            LOG.error("查询转单运单轨迹异常", e);
        }
        return findException(response);
    }

    /**
     * 转单运单轨迹列表
     * @return
     */
    @RequestMapping(value = "/findWaybillPathByIdList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_WAYBILL_PATH_BY_ID_LIST)
    @ResponseBody
    @Log(type = LogEnum.FIND_WAYBILL_PATH_BY_ID_LIST)
    public Object findWaybillPathByIdList(String orderId,String page){
        try{
            if(StringUtils.isEmpty(orderId)) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表失败:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            /**
             * 查询运单信息
             */
            Response<WaybillDetailDTO> responseew = waybillInfoService.queryWaybillDetail(Long.valueOf(orderId));
            if(responseew.getCode() != 0 || responseew.getData() == null) {
                if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表查询运单信息失败!");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            /**
             * 判断承运方是不是司机
             */
            if(responseew.getData().getWaybillInfoDTO().getTransportType().intValue() != 1) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30302);
            }
            Object object = waybillOrderService.findWaybillPathByIdList(orderId,page);
            if(object == null) {
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return object;
        }catch (Exception e){
            LOG.error("查询转单运单轨迹列表异常", e);
        }
        return findException(response);
    }
}
