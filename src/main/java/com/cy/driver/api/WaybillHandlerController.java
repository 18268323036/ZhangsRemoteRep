package com.cy.driver.api;

import com.cy.basic.service.dto.SystemConfigDTO;
import com.cy.basic.service.dto.SystemProtocolDTO;
import com.cy.driver.action.BaseAction;
import com.cy.driver.api.convert.WayBillConvert;
import com.cy.driver.api.domain.req.WaybillDetailVO;
import com.cy.driver.api.domain.res.WayBillDetail;
import com.cy.driver.api.domain.res.WayBillList;
import com.cy.driver.cloudService.*;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.PageBase;
import com.cy.driver.saasService.SaasPayUserService;
import com.cy.driver.service.*;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.pass.service.dto.DriverInfoDTO;
import com.cy.pass.service.dto.DriverItemStatDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.rdcservice.service.UserAuthService;
import com.cy.rdcservice.service.dto.*;
import com.cy.rdcservice.service.dto.enums.BusinessType;
import com.cy.rdcservice.service.dto.enums.TrackingOperateType;
import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import com.cy.saas.basic.model.po.SettingBusiness;
import com.cy.saas.business.model.dto.Protocol2DTO;
import com.cy.saas.business.model.dto.WaybillDetailsDTO;
import com.cy.saas.business.model.po.CarrierInfo;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.saas.pay.model.enums.PayUserNatureEnum;
import com.cy.search.service.dto.response.OrderInfoDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhangxy 2016/6/1 14:21
 */
@Scope("prototype")
@Controller
public class WaybillHandlerController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(WaybillHandlerController.class);

    @Resource
    private CloudCommonWaybillService cloudCommonWaybillService;
    @Resource
    private PointFService pointFService;
    @Resource
    private TrackPointService trackPointService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private IdSourceDTOService idSourceDTOService;
    @Resource
    private DriverImgService driverImgService;
    @Resource
    private WaybillOrderService waybillOrderService;
    @Resource
    private CloudUserService cloudUserService;
    @Resource
    private SaasWaybillInfoService saasWaybillInfoService;
    @Resource
    private SaasOrderInfoService saasOrderInfoService;
    @Resource
    private LocationService locationService;
    @Resource
    private SaasPayUserService saasPayUserService;
    @Resource
    private MainBmikeceService mainBmikeceService;

    /**
     * 普通运单数量
     *
     * @return
     */
    @RequestMapping(value = "/commonWayBillNumber", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.COMMON_WAY_BILL_NUMBER)
    @ResponseBody
    @Log(type = LogEnum.COMMON_WAY_BILL_NUMBER)
    public Object commonWayBillNumber() {
        try {
            Map<String, Object> map = new HashMap<>();
            Long driverId = findUserId();
            List<Integer> list = new ArrayList<>();
            list.add(Constants.ORDER_WAIT);
            list.add(Constants.ORDER_WAIT_LOAD);
            list.add(Constants.ORDER_WAIT_UNLOAD);
            list.add(Constants.ORDER_HAS_UNLOAD);
            list.add(Constants.ORDER_OTHER);
            List<Long> waybillNum = cloudCommonWaybillService.queryWaybillNum(driverId, list);
            if (waybillNum == null) {
                if (LOG.isDebugEnabled()) LOG.debug("调用search服务SearchOrderService查询订单数量失败");
                return null;
            }
            map.put("WaitAcceptNum", waybillNum.get(0));
            map.put("WaitLoadNum", waybillNum.get(1));
            map.put("WaitUnloadNum", waybillNum.get(2));
            map.put("UnloadedNum", waybillNum.get(3));
            map.put("OtherNum", waybillNum.get(4));
            updRespHeadSuccess(response);
            return map;
        } catch (Exception e) {
            LOG.error("获取普通运单数量异常", e);
        }
        return findException(response);
    }


    /**
     * 运单列表
     *
     * @param queryOrderState 运单类型
     * @param filterState     其他中的运单类型
     * @param page            查看的页数
     * @return
     */
    @RequestMapping(value = "/wayBillList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.WAY_BILL_LIST)
    @ResponseBody
    @Log(type = LogEnum.WAYBILL_LIST)
    public Object wayBillList(Integer queryOrderState, Integer filterState, Integer page) {
        Long driverId = findUserId();
        if (page == null || page.intValue() <= 0)
            page = 1;
        PageBase<WayBillList> pageBase = new PageBase<>();
        Map<String, Object> map = cloudCommonWaybillService.queryWaybillList(driverId, page, filterState, queryOrderState);
        List<OrderInfoDTO> orderInfoDTOs = (List<OrderInfoDTO>) map.get("dataList");
        if (orderInfoDTOs == null || orderInfoDTOs.size() == 0) {
            updRespHeadSuccess(response);
            return null;
        }
        List<WayBillList> wayBillLists = WayBillConvert.wayBillListConvert(orderInfoDTOs);
        List<Long> yWayBillNums = new ArrayList<>();
        List<Long> sWayBillNums = new ArrayList<>();
        if (queryOrderState == Constants.ORDER_HAS_UNLOAD) {
            for (OrderInfoDTO orderInfoDTO : orderInfoDTOs) {
                if (orderInfoDTO.getOrderSource().intValue() == 2) {
                    yWayBillNums.add(orderInfoDTO.getOrderId());
                }
                if (orderInfoDTO.getOrderSource().intValue() == 3) {
                    sWayBillNums.add(orderInfoDTO.getOrderId());
                }
            }
            if (yWayBillNums != null && yWayBillNums.size() != 0) {
                List<Integer> signInInfo = cloudCommonWaybillService.querySignInInfo(yWayBillNums);
                if (signInInfo != null && signInInfo.size() != 0) {
                    int count = 0;
                    for (WayBillList wayBillList : wayBillLists) {
                        if (wayBillList.getOrderSource().intValue() == 2) {
                            wayBillList.setWaitSignIn(signInInfo.get(count));
                            count++;
                        }
                    }
                } else {
                    if (LOG.isDebugEnabled()) LOG.debug("已卸货运单签收信息查询失败");
                }
            }
            if (sWayBillNums != null && sWayBillNums.size() != 0) {
                List<Integer> signInInfo = saasWaybillInfoService.countWaitSignBySourceWaybillIds(sWayBillNums);
                if (signInInfo != null && signInInfo.size() != 0) {
                    int count = 0;
                    for (WayBillList wayBillList : wayBillLists) {
                        if (wayBillList.getOrderSource().intValue() == 3) {
                            wayBillList.setWaitSignIn(signInInfo.get(count));
                            count++;
                        }
                    }
                } else {
                    if (LOG.isDebugEnabled()) LOG.debug("已卸货运单签收信息查询失败");
                }
            }
        }
        for (WayBillList wayBillList : wayBillLists) {
            if (wayBillList.getOrderSource().intValue() == 1) {
                WebUserInfoDTO webUserInfoDTO = webUserHandleService.getWebUserInfo(wayBillList.getCreateOwnUserId());
                if (webUserInfoDTO != null && webUserInfoDTO.getSubmitType()!=null) {
                    wayBillList.setAuthState(Integer.valueOf(webUserInfoDTO.getSubmitType()));
                }else{
                    wayBillList.setAuthState(0);
                }
            } else if(wayBillList.getOrderSource() == 2) {
                UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(wayBillList.getCreateOwnUserId());
                if (userLoginInfoDTO != null && userLoginInfoDTO.getSubmitType()!=null) {
                    wayBillList.setAuthState(Integer.valueOf(userLoginInfoDTO.getSubmitType()));
                }else{
                    wayBillList.setAuthState(0);
                }
            }else if(wayBillList.getOrderSource() == 3) {
                AccountUserDetails2DTO accountUserDetails2DTO = saasWaybillInfoService.getAccountUserDetails2(wayBillList.getCreateOwnUserId());
                if (accountUserDetails2DTO != null) {
                    wayBillList.setAuthState(Integer.valueOf(accountUserDetails2DTO.getAccountUser().getSubmitType()));
                }else{
                    wayBillList.setAuthState(0);
                }
            }else{
                UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(wayBillList.getCreateOwnUserId());
                if (userLoginInfoDTO != null && userLoginInfoDTO.getSubmitType()!=null) {
                    wayBillList.setAuthState(Integer.valueOf(userLoginInfoDTO.getSubmitType()));
                }else{
                    wayBillList.setAuthState(0);
                }
            }
        }
        pageBase.setListData(wayBillLists);
        pageBase.setTotalPage(Integer.parseInt(map.get("totalPage").toString()));
        pageBase.setTotalNum(Integer.parseInt(map.get("totalRecord").toString()));
        updRespHeadSuccess(response);
        return pageBase;
    }


    /**
     * 运单详情
     *
     * @param orderId 订单ID
     * @param orderSource 来源
     * @return
     */
    @RequestMapping(value = "/wayBillDetail", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.WAY_BILL_DETAIL)
    @ResponseBody
    @Log(type = LogEnum.WAYBILL_DETAIL)
    public Object wayBillDetail(Long orderId,Integer orderSource) {
        try {
            if (orderId == null || orderSource == null) {
                if (LOG.isErrorEnabled()) LOG.error("运单详情查询失败:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            WayBillDetail wayBillDetail = null;
            if(null == orderSource  || Constants.ORDER_FROM_QYPS == orderSource.intValue()) {//云配
                WaybillDetailDTO waybillDetailDTO = cloudCommonWaybillService.queryWaybillDetail(orderId);
                if (waybillDetailDTO == null) {
                    if (LOG.isErrorEnabled()) LOG.error("调用queryWaybillDetail方法查询运单详情失败");
                    updRespHeadError(response);
                    return null;
                }
                if (waybillDetailDTO.getWaybillInfoDTO().getCreateUserType().intValue() == 1) {
                    Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(waybillDetailDTO.getWaybillInfoDTO().getCreateUserId());
                    if (result == null || !result.isSuccess()) {
                        if (LOG.isErrorEnabled())
                            LOG.error("查询快到网司机信息失败,失败用户id{}", waybillDetailDTO.getWaybillInfoDTO().getCreateUserId());
                    }
                    DriverUserInfoDTO driverUserInfoDTO = result.getData();
                    DriverItemStatDTO driverItemStatDTO = driverUserHandlerService.findBusinessInfo(waybillDetailDTO.getWaybillInfoDTO().getCreateUserId());
                    if (driverItemStatDTO == null) {
                        if (LOG.isErrorEnabled())
                            LOG.error("查询快到网司机业务信息失败,失败用户id{}", waybillDetailDTO.getWaybillInfoDTO().getCreateUserId());
                    }
                    //头像信息传6
                    String imgAddres = driverImgService.findByType(waybillDetailDTO.getWaybillInfoDTO().getCreateUserId(), (byte) 6);
                    wayBillDetail = WayBillConvert.wayBillDetailConvert(waybillDetailDTO, null, null, driverUserInfoDTO, driverItemStatDTO, imgAddres);
                } else {
                    UserItemStatDTO userItemStatDTO = cloudUserService.getItemInfo(waybillDetailDTO.getWaybillInfoDTO().getCreateUserId());
                    UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(waybillDetailDTO.getWaybillInfoDTO().getCreateUserId());
                    if (userLoginInfoDTO == null) {
                        if (LOG.isErrorEnabled()) LOG.error("调用userService.getUser方法查询用户认证信息失败");
                    }
                    wayBillDetail = WayBillConvert.wayBillDetailConvert(waybillDetailDTO, userLoginInfoDTO, userItemStatDTO, null, null, null);
                }
                //是否转单，转单还需要转单信息
                if (waybillDetailDTO.getWaybillInfoDTO().getTurnedState() == 1) {
                    WaybillInfoDTO waybillInfoDTO = cloudCommonWaybillService.getByParentOrderId(orderId);
                    wayBillDetail.setTurnedWaybillNum(waybillInfoDTO.getWaybillNum());
                    wayBillDetail.setTurnedWaybillTime(DateUtil.dateFormat(waybillInfoDTO.getCreateTime(), DateUtil.F_DATETIME));
                }
                //是否已卸货，已卸货还需要给到签收信息
                if (waybillDetailDTO.getWaybillInfoDTO().getState() == 7) {
                    List<Long> list = new ArrayList<>();
                    list.add(orderId);
                    List<Integer> signInInfo = cloudCommonWaybillService.querySignInInfo(list);
                    if (signInInfo != null && signInInfo.size() != 0) {
                        wayBillDetail.setWaitSignIn(SystemsUtil.buildQuantity(signInInfo.get(0)));
                    }
                }
                wayBillDetail.setOrderSource(String.valueOf(Constants.ORDER_FROM_QYPS));
            }else if(Constants.ORDER_FROM_SAAS == orderSource.intValue()) {//saas
                WaybillDetailsDTO waybillDetailsDTO = saasWaybillInfoService.getWaybillDetails(orderId);
                if (waybillDetailsDTO == null) {
                    if (LOG.isErrorEnabled()) LOG.error("调用saas-getWaybillDetails方法查询运单详情失败");
                    updRespHeadError(response);
                    return null;
                }
                if (waybillDetailsDTO.getWaybillInfo().getCreateUserType() == 1) {
                    Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(waybillDetailsDTO.getWaybillInfo().getCreateUserId());
                    if (result == null || !result.isSuccess()) {
                        if (LOG.isErrorEnabled())
                            LOG.error("查询快到网司机信息失败,失败用户id{}", waybillDetailsDTO.getWaybillInfo().getCreateUserId());
                    }
                    DriverUserInfoDTO driverUserInfoDTO = result.getData();
                    DriverItemStatDTO driverItemStatDTO = driverUserHandlerService.findBusinessInfo(waybillDetailsDTO.getWaybillInfo().getCreateUserId());
                    if (driverItemStatDTO == null) {
                        if (LOG.isErrorEnabled())
                            LOG.error("查询快到网司机业务信息失败,失败用户id{}", waybillDetailsDTO.getWaybillInfo().getCreateUserId());
                    }
                    //头像信息传6
                    String imgAddres = driverImgService.findByType(waybillDetailsDTO.getWaybillInfo().getCreateUserId(), (byte) 6);
                    wayBillDetail = WayBillConvert.wayBillDetailsConvert(waybillDetailsDTO, null,null, driverUserInfoDTO, driverItemStatDTO, imgAddres);
                } else {
                    com.cy.saas.business.model.dto.UserItemStatDTO userItemStatDTO = saasWaybillInfoService.getByUserId(waybillDetailsDTO.getWaybillInfo().getCreateUserId());
                    AccountUserDetails2DTO accountUserDetails2DTO = saasWaybillInfoService.getAccountUserDetails2(waybillDetailsDTO.getWaybillInfo().getCreateUserId());
                    if (accountUserDetails2DTO == null) {
                        if (LOG.isErrorEnabled()) LOG.error("调用userService.getUser方法查询用户认证信息失败");
                    }
                    wayBillDetail = WayBillConvert.wayBillDetailsConvert(waybillDetailsDTO, accountUserDetails2DTO,userItemStatDTO, null, null, null);
                }
                //是否转单，转单还需要转单信息
                if (waybillDetailsDTO.getWaybillInfo().getTurnedState() == 1) {
                    WaybillInfo waybillInfo = saasWaybillInfoService.getWaybillByParentWaybillId(orderId);
                    wayBillDetail.setTurnedWaybillNum(waybillInfo.getWaybillNum());
                    wayBillDetail.setTurnedWaybillTime(DateUtil.dateFormat(waybillInfo.getCreateTime(), DateUtil.F_DATETIME));
                }
                //是否已卸货，已卸货还需要给到签收信息
                if (waybillDetailsDTO.getWaybillInfo().getState() == 7) {
                    List<Long> list = new ArrayList<>();
                    list.add(orderId);
                    List<Integer> signInInfo = saasWaybillInfoService.countWaitSignBySourceWaybillIds(list);
                    if (signInInfo != null && signInInfo.size() != 0) {
                        wayBillDetail.setWaitSignIn(SystemsUtil.buildQuantity(signInInfo.get(0)));
                    }
                }
                wayBillDetail.setOrderSource(String.valueOf(Constants.ORDER_FROM_SAAS));
            }
            updRespHeadSuccess(response);
            return wayBillDetail;
        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }

    /**
     * 改变运单状态
     *
     * @param orderId     运单编号
     * @param orderStatus 运单状态
     */
    @RequestMapping(value = "/cloudupdateOrderStatus", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_UPDATE_ORDER_STATUS)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_UPDATE_ORDER_STATUS)
    public Object asseptForCarriage(String orderId, String orderStatus,String province,String city,String county,String address,String longitude,String latitude,Integer orderSource) {
        try {
            if (orderId == null || orderStatus == null || orderSource == null) {
                if (LOG.isErrorEnabled()) LOG.error("改变运单状态失败:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (Integer.valueOf(orderStatus).intValue() == 22) {
                Byte authedInfo = findAuthState();
                if (authedInfo != 3) {
                    LOG.error("确定承运失败，司机信息未认证");
                    return findJSonResponse(response, ApiResultCodeEnum.SER_20217);
                }
            }
            boolean modifyStatus = false;
            Map<String, Object> map = new HashMap<>();
            if(orderStatus == null || orderSource.intValue() == 2) {//云配
                WaybillDetailDTO waybillDetailDTO = cloudCommonWaybillService.queryWaybillDetail(Long.valueOf(orderId));
                WaybillInfoDTO waybillInfoDTO = waybillDetailDTO.getWaybillInfoDTO();
                CarrierBasicInfoDTO carrierBasicInfoDTO = waybillDetailDTO.getCarrierAssemDTOList().get(0).getInfoDTO();
                ProtocolDTO protocolDTO = null;
                if(Integer.valueOf(orderStatus).intValue() == 22) {
                    /** 司机承运签订分包协议 */
                    protocolDTO = new ProtocolDTO();
                    protocolDTO.setSendCargoTime(DateUtil.dateFormat(waybillInfoDTO.getNeedStartTime(), DateUtil.F_DATETIME));//发货时间
                    UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(waybillInfoDTO.getCreateUserId());
                    if(userLoginInfoDTO.getAccountType()==null || userLoginInfoDTO.getAccountType()==1){
                        protocolDTO.setPartA(StringUtils.isEmpty(userLoginInfoDTO.getName())?userLoginInfoDTO.getCode():userLoginInfoDTO.getName());//甲方（托运方）
                    }else{
                        SystemConfigDTO systemConfigDTO = cloudUserService.getBySiteCode(userLoginInfoDTO.getSiteCode());
                        protocolDTO.setPartA(systemConfigDTO.getSite());//甲方（托运方）
                    }
                    protocolDTO.setPartB(waybillInfoDTO.getTransportUserName());//乙方（承运方）
                    protocolDTO.setCode(waybillInfoDTO.getCreateUserContact());//手机号码
                    protocolDTO.setCarNumber(waybillInfoDTO.getTransportCarnumber());//车牌号
                    protocolDTO.setDepartureAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getDepartureAddress(),waybillInfoDTO.getDepartureProvinceValue(),waybillInfoDTO.getDepartureCityValue(),waybillInfoDTO.getDepartureCountyValue(),""));//装货地
                    protocolDTO.setReceiveAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getReceiveAddress(),waybillInfoDTO.getReceiveProvinceValue(),waybillInfoDTO.getReceiveCityValue(),waybillInfoDTO.getReceiveCountyValue(),""));//卸货地
                    protocolDTO.setCargoName(carrierBasicInfoDTO.getCargoName());//货物名称
                    protocolDTO.setWeightAndCubage(SystemsUtil.getDoubleToString(waybillInfoDTO.getTotalWeight())+"吨/"+ SystemsUtil.getDoubleToString(waybillInfoDTO.getTotalCubage())+"方");//重量/体积
                    protocolDTO.setTotalFare(SystemsUtil.getTotalFare(waybillInfoDTO.getTotalFare()));//运费
                    protocolDTO.setPrepayFare(SystemsUtil.getFare(waybillInfoDTO.getPrepayFare())+"元");//预付款
                    protocolDTO.setOrderNum(waybillInfoDTO.getWaybillNum());//订单号
                    SystemConfigDTO systemConfigDTO1 = cloudUserService.getBySiteCode(waybillInfoDTO.getSiteCode());//查询平台签章
                    protocolDTO.setSignet(systemConfigDTO1.getProtocolSign());//签章
                    SystemProtocolDTO systemProtocolDTO = cloudUserService.getProtocolDTOBySiteCode(waybillInfoDTO.getSiteCode());
                    protocolDTO.setProtocolText(systemProtocolDTO.getDriverOrderProtocolText());//协议内容
                    protocolDTO.setTransportType("普通货运公路运输");
                }
                WaybillStateModifyDTO waybillStateModifyDTO = new WaybillStateModifyDTO();
                waybillStateModifyDTO.setModifyState(Integer.valueOf(orderStatus));//运单改变操作类型
                waybillStateModifyDTO.setWaybillId(Long.valueOf(orderId));//运单id
                waybillStateModifyDTO.setModifyUserType((byte) 1);//修改用户类型（1快到网司机 2快到网货主 3区域配送用户）
                waybillStateModifyDTO.setModifyUserId(findUserId());
                waybillStateModifyDTO.setModifyOwnUserId(findUserId());
                Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(findUserId());
                if (result != null || result.isSuccess()) {
                    DriverUserInfoDTO driverUserInfoDTO = result.getData();
                    waybillStateModifyDTO.setModifyUserName(driverUserInfoDTO.getName());
                }
                modifyStatus = cloudCommonWaybillService.modifyWaybillStatus(waybillStateModifyDTO,protocolDTO);
                if (Integer.valueOf(orderStatus) == 3 || Integer.valueOf(orderStatus) == 22) {
                    if (waybillDetailDTO != null) {
                        trackPointService.saveLine(findUserId(), waybillDetailDTO.getWaybillInfoDTO());
                    }
                }
                if (modifyStatus) {
                    TrackingSave2DTO trackingSave2DTO = new TrackingSave2DTO();
                    if(longitude==null){
                        LastLocationDTO lastLocationDTO = locationService.queryLastLocation(findUserId());
                        if(lastLocationDTO!=null) {
                            trackingSave2DTO.setLongitude(lastLocationDTO.getLongitude());
                            trackingSave2DTO.setLatitude(lastLocationDTO.getLatitude());
                            trackingSave2DTO.setProvince(lastLocationDTO.getProvince());
                            trackingSave2DTO.setCity(lastLocationDTO.getCity());
                            trackingSave2DTO.setCounty(lastLocationDTO.getCounty());
                            trackingSave2DTO.setAddress(lastLocationDTO.getTown());
                        }else{
                            trackingSave2DTO.setLongitude("120.107455");
                            trackingSave2DTO.setLatitude("30.293039");
                            trackingSave2DTO.setProvince("浙江省");
                            trackingSave2DTO.setCity("杭州市");
                            trackingSave2DTO.setCounty("拱墅区");
                            trackingSave2DTO.setAddress("矩阵国际");
                        }
                    }else{
                        trackingSave2DTO.setLongitude(longitude);
                        trackingSave2DTO.setLatitude(latitude);
                        trackingSave2DTO.setProvince(province);
                        trackingSave2DTO.setCity(city);
                        trackingSave2DTO.setCounty(county);
                        trackingSave2DTO.setAddress(address);
                    }
                    trackingSave2DTO.setBusinessId(Long.valueOf(orderId));
                    trackingSave2DTO.setBusinessType(BusinessType.WAYBILL.getCode());
                    //记录运单跟踪轨迹
                    ModifyUserDTO modifyUserDTO = new ModifyUserDTO();
                    modifyUserDTO.setModifyUserId(findUserId());
                    modifyUserDTO.setModifyUserName(result.getData().getName());
                    modifyUserDTO.setModifyUserType((byte)1);
                    if(orderStatus.equals("22")){
                        trackingSave2DTO.setOperateType(TrackingOperateType.CONFIRM_ORDER.getCode());
                    }else if(orderStatus.equals("23")){
                        trackingSave2DTO.setOperateType(TrackingOperateType.LOAD_CARGO.getCode());
                    }else if(orderStatus.equals("24")){
                        trackingSave2DTO.setOperateType(TrackingOperateType.UNLOAD_CARGO.getCode());
                    }
                    waybillOrderService.saveTrackingByYPS(trackingSave2DTO,modifyUserDTO);
                    //承运、装货、卸货积分奖励-3.4版本
                    if (Integer.valueOf(orderStatus) == 22 || Integer.valueOf(orderStatus) == 23 || Integer.valueOf(orderStatus) == 24) {
                        String eventCode = "";
                        if (Integer.valueOf(orderStatus) == 22) {
                            eventCode = LogEnum.DRIVER_ORDER_CHENGYUN;
                        } else if (Integer.valueOf(orderStatus) == 23) {
                            eventCode = LogEnum.DRIVER_ORDER_ZHUANGHUO;
                        } else if (Integer.valueOf(orderStatus) == 24) {
                            eventCode = LogEnum.DRIVER_ORDER_XIEHUO;
                        }
                        pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(), Constants.CHECK_MODE_BY_EVENT, eventCode, null, null, convert2InSource(), null);
                    }
                    boolean updateIndex = idSourceDTOService.updateIndex(orderId, Constants.ORDER_FROM_QYPS);
                    if (!updateIndex) {
                        LOG.debug("调用同步运单状态服务失败");
                    }
                    //// TODO: 2016/7/29 推送
                    updRespHeadSuccess(response);
                    return map;
                } else {
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }
            }else if(orderSource.intValue() == 3) {//saas
                Response<DriverUserInfoDTO> driverUserInfoDTOResponse = driverUserHandlerService.getDriverUserInfo(findUserId());
                DriverUserInfoDTO driverUserInfoDTO1 = new DriverUserInfoDTO();
                if (!driverUserInfoDTOResponse.isSuccess()) {
                    LOG.error("获取司机詳細信息服务调用错误");
                }else{
                    driverUserInfoDTO1 = driverUserInfoDTOResponse.getData();
                }
                if(!saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                    String kdwPassWord = mainBmikeceService.getPayPwd(findcapitalAccountId());
                    if(!saasPayUserService.saveForOpenAccount(PayUserNatureEnum.PERSON.getCode(),driverUserInfoDTO1.getName(),driverUserInfoDTO1.getCode(),findUserId().toString(),kdwPassWord)) {
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                    }
                }
                WaybillDetailsDTO waybillDetailsDTO = saasWaybillInfoService.getWaybillDetails(Long.valueOf(orderId));
                WaybillInfo waybillInfo = waybillDetailsDTO.getWaybillInfo();
                CarrierInfo carrierInfo = waybillDetailsDTO.getCarrierTradeDTOList().get(0).getCarrierInfo();
                AccountUserDetails2DTO accountUserDetails2DTO = saasWaybillInfoService.getAccountUserDetails2(waybillInfo.getCreateUserId());
                Protocol2DTO protocol2DTO = null;
                if(Integer.valueOf(orderStatus).intValue() == 22) {
                    protocol2DTO = new Protocol2DTO();
                    /** 甲方（托运方），必填 */
                    protocol2DTO.setPartA(accountUserDetails2DTO.getAccountUser().getName());
                    /** 乙方（承运方），必填 */
                    protocol2DTO.setPartB(driverUserInfoDTO1.getName());
                    /** 保价 */
                    protocol2DTO.setInsured(null);
                    /** 保费 */
                    protocol2DTO.setPremium(null);
                    /** 司机手机号码，司机协议必填 */
                    protocol2DTO.setDriverMobile(driverUserInfoDTO1.getCode().toString());
                    /** 司机身份证号，司机协议必传 */
                    protocol2DTO.setDriverIDCard(driverUserInfoDTO1.getIdentityLicenseNum());
                    /** 车牌号，司机协议必传 */
                    protocol2DTO.setCarNumber(driverUserInfoDTO1.getCarNumber());
                    /** 合同生效日期 */
                    protocol2DTO.setEffectiveBegin(new Date());
                    /** 合同有效期至 */
                    protocol2DTO.setEffectiveEnd(null);
                    SettingBusiness settingBusiness = saasOrderInfoService.getByUserId(accountUserDetails2DTO.getAccountUser().getUserId());
                    /** 协议内容，必填 */
                    protocol2DTO.setProtocolText(settingBusiness.getDriverOrderProtocolText());
                    /** 协议备注 */
                    protocol2DTO.setProtocolRemark(null);
                    /** 甲方签章 */
                    protocol2DTO.setPartyASignet(settingBusiness.getProtocolSign());
                    /** 甲方用户id */
                    protocol2DTO.setPartyAUserId(accountUserDetails2DTO.getAccountUser().getUserId());
                    /** 乙方签章 */
                    protocol2DTO.setPartyBSignet(null);
                    /** 乙方用户id */
                    protocol2DTO.setPartyBUserId(null);
                }
                com.cy.saas.business.model.dto.WaybillStateModifyDTO waybillStateModifyDTO = new com.cy.saas.business.model.dto.WaybillStateModifyDTO();
                waybillStateModifyDTO.setModifyState(Integer.valueOf(orderStatus));//运单改变操作类型
                waybillStateModifyDTO.setWaybillId(Long.valueOf(orderId));//运单id
                waybillStateModifyDTO.setModifyUserType((byte) 1);//修改用户类型（1快到网司机 2快到网货主 3区域配送用户）
                waybillStateModifyDTO.setModifyUserId(findUserId());
                waybillStateModifyDTO.setModifyOwnUserId(findUserId());
                Response<DriverUserInfoDTO> result = driverUserHandlerService.getDriverUserInfo(findUserId());
                if (result != null || result.isSuccess()) {
                    DriverUserInfoDTO driverUserInfoDTO = result.getData();
                    waybillStateModifyDTO.setModifyUserName(driverUserInfoDTO.getName());
                }
                modifyStatus = saasWaybillInfoService.modifyWaybillStateNew(waybillStateModifyDTO,protocol2DTO);
                if (Integer.valueOf(orderStatus) == 3 || Integer.valueOf(orderStatus) == 22) {
                    if (waybillDetailsDTO != null) {
                        trackPointService.saSaveLine(findUserId(), waybillDetailsDTO.getWaybillInfo());
                    }
                }
                if (modifyStatus) {
                    //记录运单跟踪轨迹
                    com.cy.saas.business.model.dto.ModifyUserDTO modifyUserDTO = new com.cy.saas.business.model.dto.ModifyUserDTO();
                    modifyUserDTO.setModifyUserId(findUserId());
                    modifyUserDTO.setModifyOwnUserId(findUserId());
                    modifyUserDTO.setModifyUserName(result.getData().getName());
                    modifyUserDTO.setModifyOwnUserName(result.getData().getName());
                    modifyUserDTO.setModifyUserType((byte)1);
                    com.cy.saas.business.model.dto.TrackingSaveDTO trackingSaveDTO = new com.cy.saas.business.model.dto.TrackingSaveDTO();
                    trackingSaveDTO.setLongitude(longitude);
                    trackingSaveDTO.setLatitude(latitude);
                    trackingSaveDTO.setProvince(province);
                    trackingSaveDTO.setCity(city);
                    trackingSaveDTO.setCounty(county);
                    trackingSaveDTO.setAddress(address);
                    if(orderStatus.equals("22")){
                        trackingSaveDTO.setOperateType(TrackingOperateType.CONFIRM_ORDER.getCode());
                    }else if(orderStatus.equals("23")){
                        trackingSaveDTO.setOperateType(TrackingOperateType.LOAD_CARGO.getCode());
                    }else if(orderStatus.equals("24")){
                        trackingSaveDTO.setOperateType(TrackingOperateType.UNLOAD_CARGO.getCode());
                    }
                    saasWaybillInfoService.saveTrackingByYPS(Long.valueOf(orderId),trackingSaveDTO,modifyUserDTO);
                    //承运、装货、卸货积分奖励-3.4版本
                    if (Integer.valueOf(orderStatus) == 22 || Integer.valueOf(orderStatus) == 23 || Integer.valueOf(orderStatus) == 24) {
                        String eventCode = "";
                        if (Integer.valueOf(orderStatus) == 22) {
                            eventCode = LogEnum.DRIVER_ORDER_CHENGYUN;
                        } else if (Integer.valueOf(orderStatus) == 23) {
                            eventCode = LogEnum.DRIVER_ORDER_ZHUANGHUO;
                        } else if (Integer.valueOf(orderStatus) == 24) {
                            eventCode = LogEnum.DRIVER_ORDER_XIEHUO;
                        }
                        pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(), Constants.CHECK_MODE_BY_EVENT, eventCode, null, null, convert2InSource(), null);
                    }
                    updRespHeadSuccess(response);
                    return map;
                } else {
                    updRespHeadError(response);
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
                }
            }
        } catch (Exception e) {
            LOG.error("修改运单状态异常", e);
        }
        return findException(response);
    }

    /**
     * 运单报价
     */
    @RequestMapping(value = "/cloudOrderQuote", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_ORDER_QUOTE)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_ORDER_QUOTE)
    public Object cloudOrderQuote(String orderId, BigDecimal quoteAmount, BigDecimal advancePayment) {
        try {
            //校验
            if (orderId == null || quoteAmount == null || advancePayment == null || BigDecimal.ZERO == quoteAmount) {
                if (LOG.isErrorEnabled()) LOG.error("保存司机报价信息校验:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            boolean quotedResult = cloudCommonWaybillService.driverQuoteWaybill(Long.valueOf(orderId), quoteAmount, advancePayment);
            if (quotedResult) {
                // TODO: 2016/7/29 推送
                boolean updateIndex = idSourceDTOService.updateIndex(orderId, Constants.ORDER_FROM_QYPS);
                if (!updateIndex) {
                    LOG.debug("调用同步运单状态服务失败");
                }
                updRespHeadSuccess(response);
                return null;
            } else {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
            }

        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }

    /**
     * 运单转单
     */
    @RequestMapping(value = "/cloudTurnWaybill", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLOUD_TURN_WAYBILL)
    @ResponseBody
    @Log(type = LogEnum.CLOUD_TURN_WAYBILL)
    public Object cloudTurnWaybill(WaybillDetailVO waybillDetailVO) {
        try {
            //校验
            if (waybillDetailVO == null || waybillDetailVO.getOrderId() == null ||
                    StringUtils.isEmpty(waybillDetailVO.getSiteCode()) ||
                    StringUtils.isEmpty(waybillDetailVO.getStartProvinceCode()) ||
                    StringUtils.isEmpty(waybillDetailVO.getTransportMobile()) ||
                    StringUtils.isEmpty(waybillDetailVO.getStartProvinceValue()) ||
                    StringUtils.isEmpty(waybillDetailVO.getStartCityValue()) ||
                    StringUtils.isEmpty(waybillDetailVO.getStartCityCode()) ||
                    StringUtils.isEmpty(waybillDetailVO.getTransportName()) ||
                    waybillDetailVO.getStartTime() == null ||
                    waybillDetailVO.getEndTime() == null ||
                    StringUtils.isEmpty(waybillDetailVO.getConsigneeName()) ||
                    StringUtils.isEmpty(waybillDetailVO.getConsigneeMobilePhone()) ||
                    StringUtils.isEmpty(waybillDetailVO.getTakeWay())) {
                if (LOG.isErrorEnabled()) LOG.error("运单转单校验:开关数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (StringUtils.isEmpty(waybillDetailVO.getOrderQuotedAmount())  || Double.valueOf(waybillDetailVO.getOrderQuotedAmount()) <= 0) {
                if (LOG.isErrorEnabled()) LOG.error("运单转单校验:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if(StringUtils.isEmpty(waybillDetailVO.getOrderQuotedPrepay()) || Double.valueOf(waybillDetailVO.getOrderQuotedPrepay()) <= 0){
                waybillDetailVO.setOrderQuotedPrepay("0");
            }
            if (Integer.valueOf(waybillDetailVO.getTakeWay()) != 0 && Integer.valueOf(waybillDetailVO.getTakeWay()) != 1) {
                if (LOG.isErrorEnabled()) LOG.error("取件参数校验:数据为空或者值不正确.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            WaybillTurnOrderDTO waybillTurnOrderDTO = new WaybillTurnOrderDTO();

            Long driverId = findUserId();

            waybillTurnOrderDTO.setParentWaybillId(Long.valueOf(waybillDetailVO.getOrderId()));// 父级运单id（t_waybill_info.id），必填
            waybillTurnOrderDTO.setOwnUserId(driverId);// 所属主帐号id(如果是主帐号是创建人，则与create_user_id相同)，必填
            waybillTurnOrderDTO.setCreateUserId(driverId);// 创建用户编码，必填

            /**
             * 获取司机信息
             */
            Response<DriverUserInfoDTO> responseDriver = driverUserHandlerService.getDriverUserInfo(driverId);
            if (responseDriver != null && responseDriver.isSuccess()) {
                waybillTurnOrderDTO.setCreateUserName(responseDriver.getData().getName());// 创建用户名称
            }
            /**
             * 获取司机信息头像,电话号码
             */
            DriverInfoDTO driverInfoDTO = driverUserHandlerService.getDriverInfoIncludeImg(driverId);
            waybillTurnOrderDTO.setCreateHeadImgUrl(driverInfoDTO.getPhotosAddress());// 创建用户头像
            waybillTurnOrderDTO.setCreateUserContact(driverInfoDTO.getMobilePhone());// 创建人（我接到的运单中的发货人）联系方式
            waybillTurnOrderDTO.setCreateUserType(new Byte("1"));// 1快到网司机 3区域配送用户，必填
            waybillTurnOrderDTO.setDepartureContact(waybillDetailVO.getConsigneeName()); //装货地联系人，必填
            waybillTurnOrderDTO.setDepartureMobile(waybillDetailVO.getConsigneeMobilePhone());// 装货地联系手机
            waybillTurnOrderDTO.setDepartureTelephone(waybillDetailVO.getConsigneeMobilePhone());// 装货地联系电话
            waybillTurnOrderDTO.setDepartureAddress(waybillDetailVO.getStartAddress());// 出发地详细地址
            waybillTurnOrderDTO.setDepartureProvinceCode(waybillDetailVO.getStartProvinceCode());//出发地省编号，必填
            waybillTurnOrderDTO.setDepartureProvinceValue(waybillDetailVO.getStartProvinceValue());//出发地省名称，必填
            waybillTurnOrderDTO.setDepartureCityCode(waybillDetailVO.getStartCityCode());// 出发地市编号，必填
            waybillTurnOrderDTO.setDepartureCityValue(waybillDetailVO.getStartCityValue());// 出发地市名称，必填
            waybillTurnOrderDTO.setDepartureCountyCode(waybillDetailVO.getStartCountyCode());// 出发地县区编号
            waybillTurnOrderDTO.setDepartureCountyValue(waybillDetailVO.getStartCountyValue());// 出发地县区名称
            waybillTurnOrderDTO.setNeedStartTime(DateUtil.strToDate(waybillDetailVO.getStartTime()));// 要求装货时间，必填
            waybillTurnOrderDTO.setNeedEndTime(DateUtil.strToDate(waybillDetailVO.getEndTime()));
            waybillTurnOrderDTO.setPrepayFare(new BigDecimal(waybillDetailVO.getOrderQuotedPrepay()));// 预付款，必填
            waybillTurnOrderDTO.setRemark(waybillDetailVO.getRemark()); //备注
            waybillTurnOrderDTO.setTotalFare(new BigDecimal(waybillDetailVO.getOrderQuotedAmount()));// 运费总价，必填
            waybillTurnOrderDTO.setWaybillNature(new Byte(waybillDetailVO.getWaybillNature()));//运单性质（1业务派单运单、2运输派单运单、4运输揽件运单），必填

            /**
             * 承运方Id为空
             */
            if (StringUtils.isEmpty(waybillDetailVO.getTransportUserId())) {
                waybillTurnOrderDTO.setTransportUserName(waybillDetailVO.getTransportName());// 承运方用户名称
                waybillTurnOrderDTO.setTransportCarnumber(waybillDetailVO.getCarNumber()); //承运方车牌号码
                waybillTurnOrderDTO.setTransportMobile(waybillDetailVO.getTransportMobile());// 承运方手机号码，必填
                waybillTurnOrderDTO.setTransportType(null);// 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
            } else {
                /**
                 * 快到网
                 */
                if ("2".equals(waybillDetailVO.getWaybillNature())) {
                    waybillTurnOrderDTO.setTransportType(new Byte("1"));// 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
                    waybillTurnOrderDTO.setTransportUserName(waybillDetailVO.getTransportName());// 承运方用户名称
                    waybillTurnOrderDTO.setTransportCarnumber(waybillDetailVO.getCarNumber()); //承运方车牌号码
                    waybillTurnOrderDTO.setTransportMobile(waybillDetailVO.getTransportMobile());// 承运方手机号码，必填
                    /**
                     * 获取司机信息
                     */
                    Response<DriverUserInfoDTO> responseTDriver = driverUserHandlerService.getDriverUserInfo(Long.valueOf(waybillDetailVO.getTransportUserId()));

                    /**
                     * 获取司机信息头像,电话号码
                     */
                    DriverInfoDTO driverInfoDTOT = driverUserHandlerService.getDriverInfoIncludeImg(Long.valueOf(waybillDetailVO.getTransportUserId()));
                    waybillTurnOrderDTO.setTransportOwnUserId(responseTDriver.getData().getId()); //承运方所属主帐号id
                    waybillTurnOrderDTO.setTransportUserId(responseTDriver.getData().getId()); //承运方用户编码
                    waybillTurnOrderDTO.setTransportCompanyName(waybillDetailVO.getTransportName()); //承运方企业名称
                    waybillTurnOrderDTO.setTransportHeadImgUrl(driverInfoDTOT.getPhotosAddress());// 承运方头像
                    waybillTurnOrderDTO.setTransportTelephone(driverInfoDTOT.getMobilePhone());// 承运方固定电话
                } else {
                    waybillTurnOrderDTO.setTransportUserName(waybillDetailVO.getTransportName());// 承运方用户名称
                    waybillTurnOrderDTO.setTransportCarnumber(waybillDetailVO.getCarNumber()); //承运方车牌号码
                    waybillTurnOrderDTO.setTransportMobile(waybillDetailVO.getTransportMobile());// 承运方手机号码，必填
                    waybillTurnOrderDTO.setTransportType(new Byte("3"));// 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
                    /**
                     * 查询云配送用户信息
                     */
                    UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(Long.valueOf(waybillDetailVO.getTransportUserId()));
                    if (userLoginInfoDTO.getParentId() == null || userLoginInfoDTO.getParentId().longValue() == 0L) {
                        waybillTurnOrderDTO.setTransportOwnUserId(userLoginInfoDTO.getId()); //承运方所属主帐号id
                    } else {
                        waybillTurnOrderDTO.setTransportOwnUserId(userLoginInfoDTO.getParentId()); //承运方所属主帐号id
                    }
                    /**
                     * 根据主账号查询主账号信息
                     */
                    com.cy.top56.common.Response<AuthedInfoDTO> response = userAuthService.getByUserId(userLoginInfoDTO.getParentId());
                    if (response.getCode() != 0) {

                    }
                    if (response.getData() == null) {
                        waybillTurnOrderDTO.setTransportCompanyName(""); //承运方企业名称
                    } else {
                        waybillTurnOrderDTO.setTransportCompanyName(response.getData().getAuthName()); //承运方企业名称
                    }
                    waybillTurnOrderDTO.setTransportUserId(userLoginInfoDTO.getId()); //承运方用户编码
                    waybillTurnOrderDTO.setTransportHeadImgUrl(userLoginInfoDTO.getHeadImg());// 承运方头像
                    waybillTurnOrderDTO.setTransportTelephone(userLoginInfoDTO.getMobilephone());// 承运方固定电话
                }
            }
            Map<String, Object> map = new HashMap<>();
            Integer takeWay = Integer.valueOf(waybillDetailVO.getTakeWay());
            Long turnorderId = cloudCommonWaybillService.turnWaybill(waybillTurnOrderDTO, takeWay);
            if (turnorderId != null) {
                boolean updateIndex = idSourceDTOService.updateIndex(String.valueOf(turnorderId), Constants.ORDER_FROM_QYPS);
                if (!updateIndex) {
                    LOG.debug("调用同步运单状态服务失败");
                }
                updRespHeadSuccess(response);
                map.put("turnorderId", turnorderId);
                return map;
            }
            updRespHeadError(response);
            return null;
        } catch (Exception e) {
            LOG.error("获取运单详情异常", e);
        }
        return findException(response);
    }


}