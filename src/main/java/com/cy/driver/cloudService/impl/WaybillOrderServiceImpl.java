package com.cy.driver.cloudService.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cy.driver.api.convert.WayBillConvert;
import com.cy.driver.api.domain.res.*;
import com.cy.driver.cloudService.WaybillOrderService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.location.service.LocationQueryService;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.location.service.dto.LocationDTO;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.rdcservice.service.*;
import com.cy.rdcservice.service.dto.*;
import com.cy.search.service.SearchOrderService;
import com.cy.search.service.dto.request.DriverIdAndKeywordDTO;
import com.cy.search.service.dto.response.OrderInfoDTO;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

/**
 *
 * Created by nixianjing on 16/7/27.
 */
@Service("waybillOrderService")
public class WaybillOrderServiceImpl implements WaybillOrderService {

    private Logger LOG = LoggerFactory.getLogger(WaybillOrderServiceImpl.class);

    public static final String KEY_1 = "7d9fbeb43e975cd1e9477a7e5d5e192a";

    @Resource
    private WaybillInfoService waybillInfoService;

    @Resource
    private UserService userService;

    @Resource
    private UserItemStatService userItemStatService;

    @Resource
    private WaybillOperService waybillOperService;

    @Resource
    private WaybillAssessService waybillAssessService;

    @Resource
    private DriverUserInfoService driverUserInfoService;

    @Resource
    private SearchOrderService searchOrderService2;

    @Resource
    private LocationQueryService locationQueryService;

    @Resource
    private CarrierTrackingService carrierTrackingService;


    /**
     * 返回转单订单数量
     * @param driverId 司机ID
     * @param statcList 查询转单状态list
     * @return
     */
    @Override
    public Object countTurnOrderByDriverId(Long driverId, List<Integer> statcList) {
        List<WaybillOrderCountRes> countList = new ArrayList<WaybillOrderCountRes>();
        Response<List<Integer>> response = waybillInfoService.countTurnOrderByDriver(driverId,statcList);
        if(response.getCode() != 0 || response.getData() == null) {
            if (LOG.isErrorEnabled()) LOG.error("查询转单运单状态数量失败!message信息:"+response.getMessage());
            return null;
        }else {
            List<Integer> list = response.getData();
            for(int i = 0;i < list.size();i++) {
                WaybillOrderCountRes waybillOrderCountRes = new WaybillOrderCountRes();
                waybillOrderCountRes.setState(statcList.get(i));
                waybillOrderCountRes.setCount(list.get(i));
                countList.add(waybillOrderCountRes);
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listData",countList);
        return map;
    }


    /**
     * 转单订单分页
     * @param page 查询第几页
     * @param turnOrderQueryDTO (司机ID\转单订单状态)
     * @return
     */
    @Override
    public Object pageTurnOrderByDriverList(Integer page,TurnOrderQueryDTO turnOrderQueryDTO) {
        PageInfo<TurnOrderQueryDTO> pageInfo = new PageInfo(page,10);
        pageInfo.setData(turnOrderQueryDTO);
        PageResult<WaybillInfoDTO> pageResult = waybillInfoService.pageTurnOrderByDriver(pageInfo);
        if(pageResult.getCode() != 0) {
            if (LOG.isErrorEnabled()) LOG.error("查询转单运单列表失败!message信息:"+pageResult.getMessage());
            return null;
        }
        WaybillOrderPageRes waybillOrderPageRes = new WaybillOrderPageRes();
        waybillOrderPageRes.setTotalNum(String.valueOf(pageResult.getTotalRecord()));
        waybillOrderPageRes.setTotalPage(String.valueOf(pageResult.getTotalPage()));
        waybillOrderPageRes.setListData(getOrderList(pageResult.getDataList()));
        return waybillOrderPageRes;
    }


    /**
     * 查询转单详情
     * @param orderId
     * @return
     */
    @Override
    public Object findTurnOrderByOrderId(String orderId) {
        Response<WaybillDetailDTO> response = waybillInfoService.queryWaybillDetail(Long.valueOf(orderId));
        if(response.getCode() != 0 || response.getData() == null) {
            if (LOG.isErrorEnabled()) LOG.error("查询转单运单详情失败!message信息:"+response.getMessage());
            return null;
        }
        return getOrderInfo(response.getData());
    }


    /**
     * 修改运单运费
     * @param waybillModifyFareDTO
     * @return
     */
    @Override
    public Object updateFare(WaybillModifyFareDTO waybillModifyFareDTO) {
        if(waybillModifyFareDTO.getTotalFare().compareTo(waybillModifyFareDTO.getPrepayFare()) == -1) {
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30301);
        }
        Response<Boolean> response = waybillOperService.modifyFare(waybillModifyFareDTO);
        if(response.getCode() != 0) {
            if (LOG.isErrorEnabled()) LOG.error("修改转单运单运费金额失败!message信息:"+response.getMessage());
            return null;
        }
        return response.getData();
    }


    /**
     *
     * @param orderId 运单ID
     * @param state 状态（2采用承运方报价、-2不采用承运方报价），必填
     * @param modifyUserDTO
     * @return
     */
    @Override
    public Object useTransportQuote(String orderId, String state, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = waybillOperService.useTransportQuote(Long.valueOf(orderId),new Byte(state),modifyUserDTO);
        if(response.getCode() != 0) {
            if (LOG.isErrorEnabled()) LOG.error("修改转单运单运单承运方报价状态失败!message信息:"+response.getMessage());
            return null;
        }
        return response.getData();
    }


    /**
     * 保存评价
     * @param waybillAssessSaveDTO
     * @return
     */
    @Override
    public Object saveAssess(WaybillAssessSaveDTO waybillAssessSaveDTO) {
        /**
         * 查询转单运单信息
         */
        Response<WaybillDetailDTO> response = waybillInfoService.queryWaybillDetail(waybillAssessSaveDTO.getWaybillId());
        if(response.getCode() != 0 || response.getData()  == null) {
            if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表查询运单信息失败!");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }

        /**
         * 认证信息
         */
        if(getSumitType(String.valueOf(response.getData().getWaybillInfoDTO().getTransportType()),response.getData().getWaybillInfoDTO().getTransportUserId()).equals("3")) {
            waybillAssessSaveDTO.setAimAuthState((byte)1);
        }else {
            waybillAssessSaveDTO.setAimAuthState((byte)0);
        }
        waybillAssessSaveDTO.setAimUserHeadImg(response.getData().getWaybillInfoDTO().getTransportHeadImgUrl());//目标用户头像
        waybillAssessSaveDTO.setAimUserId(response.getData().getWaybillInfoDTO().getTransportUserId());//目标用户编码
        waybillAssessSaveDTO.setAimUserName(response.getData().getWaybillInfoDTO().getTransportUserName());//目标用户名称
        waybillAssessSaveDTO.setAimUserPhone(response.getData().getWaybillInfoDTO().getTransportMobile());//目标用户手机号码
        waybillAssessSaveDTO.setAimUserType(response.getData().getWaybillInfoDTO().getTransportType());//目标用户类型 1快到网司机 2快到网货主 3区域配送用户

        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> dirver = driverUserInfoService.getDriverUserInfo(response.getData().getWaybillInfoDTO().getCreateUserId());
        waybillAssessSaveDTO.setOrgUserType(response.getData().getWaybillInfoDTO().getCreateUserType());//源用户类型 1快到网司机 2快到网货主 3区域配送用户
        waybillAssessSaveDTO.setOrgUserId(response.getData().getWaybillInfoDTO().getCreateUserId());//源用户编码
        if(dirver.getData().getSubmitType()==3){
            waybillAssessSaveDTO.setOrgAuthState((byte)1);//源用户认证状态 0 未认证 1 已认证
        }else{
            waybillAssessSaveDTO.setOrgAuthState((byte)0);//源用户认证状态 0 未认证 1 已认证
        }

        waybillAssessSaveDTO.setOrgUserHeadImg(response.getData().getWaybillInfoDTO().getCreateHeadImgUrl());//源用户头像
        waybillAssessSaveDTO.setOrgUserName(response.getData().getWaybillInfoDTO().getCreateUserName());//源用户名称
        waybillAssessSaveDTO.setOrgUserPhone(dirver.getData().getCode());//源用户手机号码
        Response<Long> responseWay = waybillAssessService.save(waybillAssessSaveDTO);
        if(responseWay.getCode() != 0 || responseWay.getData() == null) {
            if (LOG.isErrorEnabled()) LOG.error("转单运单运单评价失败!message信息:"+response.getMessage());
            return null;
        }
        return responseWay.getData();
    }


    /**
     * 查询评价
     * @param waybillId
     * @return
     */
    @Override
    public Object findAssessByWaybillId(Long waybillId) {
        Response<List<WaybillAssessDTO>> response = waybillAssessService.listByWaybillId(waybillId);
        if(response.getCode() != 0) {
            if (LOG.isErrorEnabled()) LOG.error("查询转单运单运单评价失败!message信息:"+response.getMessage());
            return null;
        }
        List<WaybillAssessRes> waybillAssessResList = new ArrayList<WaybillAssessRes>();
        List<WaybillAssessDTO> list = response.getData();
        for(int i = 0;i<list.size();i++) {
            WaybillAssessRes waybillAssessRes = new WaybillAssessRes();
            //源用户类型 1快到网司机 2快到网货主 3区域配送用户
            if(list.get(i).getOrgUserType() == 1) {
                waybillAssessRes.setAssessType("1");
            }else {
                waybillAssessRes.setAssessType("2");
            }
            /**
             * 评价者姓名
             */
            waybillAssessRes.setAssessUserName(list.get(i).getOrgUserName());

            /**
             * 头像
             */
            waybillAssessRes.setUserHeadImg(list.get(i).getOrgUserHeadImg());

            /**
             * 联系电话
             */
            if(StringUtils.isNotEmpty(list.get(i).getOrgUserPhone())) {
                waybillAssessRes.setAimUserPhone(list.get(i).getOrgUserPhone());
            }else {
                waybillAssessRes.setAimUserPhone("");
            }

            /**
             * 评价分数
             */
            waybillAssessRes.setAssessScore(String.valueOf(list.get(i).getAssessScore()));

            /**
             * 评价内容
             */
            waybillAssessRes.setContent(list.get(i).getContent());

            /**
             * 评价时间
             */
            waybillAssessRes.setAssessTime(DateUtil.dateFormat(list.get(i).getCreateTime()));
            waybillAssessResList.add(waybillAssessRes);
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("listData",waybillAssessResList);
        return map;
    }


    /**
     * 查询转单运单轨迹
     * @param waybillId
     * @return
     */
    @Override
    public Object findWaybillPathById(String waybillId) {
        DriverLastLocationRes driverLastLocationRes = new DriverLastLocationRes();
        /**
         * 查询运单信息
         */
        Response<WaybillDetailDTO> response = waybillInfoService.queryWaybillDetail(Long.valueOf(waybillId));
        if(response.getCode() != 0 || response.getData() == null) {
            if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表查询运单信息失败!");
            return null;
        }
        com.cy.location.service.dto.base.Response<LastLocationDTO> lastResponse =  locationQueryService.queryLastLocation(response.getData().getWaybillInfoDTO().getTransportUserId());
        if(lastResponse.getCode() != 0) {
            if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表查询司机当前位置信息失败!");
            return null;
        }
        if(lastResponse.getData() == null) {
            return null;
        }
        driverLastLocationRes.setDriverName(response.getData().getWaybillInfoDTO().getTransportUserName());
        driverLastLocationRes.setDriverMobile(response.getData().getWaybillInfoDTO().getTransportMobile());
        driverLastLocationRes.setDriverCarNumber(response.getData().getWaybillInfoDTO().getTransportCarnumber());
        driverLastLocationRes.setDriverAddress(lastResponse.getData().getLocation());
        driverLastLocationRes.setDriverLat(lastResponse.getData().getLatitude());
        driverLastLocationRes.setDriverLng(lastResponse.getData().getLongitude());
        driverLastLocationRes.setStartAddress(SystemsUtil.buildAddress(response.getData().getWaybillInfoDTO().getDepartureProvinceValue(),response.getData().getWaybillInfoDTO().getDepartureCityValue(),response.getData().getWaybillInfoDTO().getDepartureCountyValue()));
        Map<String,String> startMap = getGeocoderLatitude(driverLastLocationRes.getStartAddress());
        driverLastLocationRes.setStartLat(startMap.get("lat"));
        driverLastLocationRes.setStartLng(startMap.get("lng"));
        driverLastLocationRes.setEndAddress(SystemsUtil.buildAddress(response.getData().getWaybillInfoDTO().getReceiveProvinceValue(),response.getData().getWaybillInfoDTO().getReceiveCityValue(),response.getData().getWaybillInfoDTO().getReceiveCountyValue()));
        Map<String,String> endMap = getGeocoderLatitude(driverLastLocationRes.getEndAddress());
        driverLastLocationRes.setEndLat(endMap.get("lat"));
        driverLastLocationRes.setEndLng(endMap.get("lng"));
        return driverLastLocationRes;
    }


    @Override
    public Object findWaybillPathByIdList(String waybuillId, String page) {
        Map<String ,Object> map = new HashMap<String ,Object>();
        /**
         * 查询运单信息
         */
        Response<WaybillDetailDTO> response = waybillInfoService.queryWaybillDetail(Long.valueOf(waybuillId));
        if(response.getCode() != 0 || response.getData() == null) {
            if (LOG.isErrorEnabled()) LOG.error("转单运单轨迹列表查询运单信息失败!");
            return null;
        }
        com.cy.location.service.dto.base.PageResult<LocationDTO> pageResult =  locationQueryService.queryDriverHistTrackingByPage(response.getData().getWaybillInfoDTO().getTransportUserId(),
                response.getData().getWaybillInfoDTO().getCreateTime(),
                response.getData().getWaybillInfoDTO().getModifyTime(),
                null,
                Integer.parseInt(page),
                10);
        if(pageResult.getCode() != 0) {
            map.put("listData",null);
        }else {
            map.put("listData",getLastLocation(pageResult.getDataList()));
        }
        map.put("totalNum",pageResult.getTotalRecord());
        map.put("totalPage",pageResult.getTotalPage());
        return map;
    }

    /**
     * 运单查询
     * @param pageInfo
     * @return
     */
    @Override
    public Object queryWaybillList(com.cy.search.service.dto.base.PageInfo<DriverIdAndKeywordDTO> pageInfo) {
        com.cy.search.service.dto.base.PageResult<OrderInfoDTO> pageResult = searchOrderService2.searchOrderByDriver(pageInfo);
        if(pageResult==null || !pageResult.isSuccess()){
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询订单列表信息失败");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalRecord",pageResult.getTotalRecord());
        map.put("totalPage",pageResult.getTotalPage());
        map.put("dataList",pageResult.getDataList());
        return map;
    }


    @Override
    public DriverUserInfoDTO getDriverUserInfo(Long driverId) {
        /**
         * 快到网司机为承运方
         */
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> dirver = driverUserInfoService.getDriverUserInfo(driverId);
        if(dirver.getCode() != 0) {
            return null;
        }
        return dirver.getData();
    }

    @Override
    public boolean saveWaybillTrackingByDriver(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = carrierTrackingService.saveWaybillTrackingByDriver(waybillId,trackingSaveDTO,modifyUserDTO);
        if(response==null){
            LOG.debug("调用carrierTrackingService.saveWaybillTrackingByDriver跟踪运单记录失败，失败原因response为空",waybillId, JSONObject.toJSONString(trackingSaveDTO),JSONObject.toJSONString(modifyUserDTO));
            return false;
        }
        if(!response.isSuccess() || !response.getData()){
            LOG.debug("调用carrierTrackingService.saveWaybillTrackingByDriver跟踪运单记录失败，失败原因response={}",response.getMessage(),waybillId, JSONObject.toJSONString(trackingSaveDTO),JSONObject.toJSONString(modifyUserDTO));
            return false;
        }
        return response.getData();
    }


    @Override
    public Boolean saveTrackingByYPS(TrackingSave2DTO trackingSave2DTO, ModifyUserDTO modifyUserDTO) {
        Response<Boolean> response = carrierTrackingService.saveTrackingByYPS(trackingSave2DTO, modifyUserDTO);
        if(response == null){
            LOG.debug("调用carrierTrackingService.saveTrackingByYPS记录运单跟踪信息失败，失败原因response为空,入参trackingSave2DTO={}，modifyUserDTO={}",JSONObject.toJSONString(trackingSave2DTO),JSONObject.toJSONString(modifyUserDTO));
            return false;
        }
        if(!response.isSuccess()){
            LOG.error("调用carrierTrackingService.saveTrackingByYPS记录运单跟踪信息失败，失败原因response={},入参trackingSave2DTO={}，modifyUserDTO={}", JSONObject.toJSONString(trackingSave2DTO),JSONObject.toJSONString(modifyUserDTO));
            return false;
        }
        return response.getData();
    }

    /**
     * 转单运单历史轨迹列表转换
     * @param locationList
     * @return
     */
    public List<DriverLastLocationListRes> getLastLocation(List<LocationDTO> locationList) {
        if(locationList == null) {
            return null;
        }
        List<DriverLastLocationListRes> list = new ArrayList<DriverLastLocationListRes>();
        for(int i = 0;i<locationList.size();i++) {
            DriverLastLocationListRes driverLastLocationListRes = new DriverLastLocationListRes();
            driverLastLocationListRes.setTownAddress(locationList.get(i).getLocation());
            driverLastLocationListRes.setTownTime(DateUtil.dateTimeToStr(locationList.get(i).getCollectTime()));
            list.add(driverLastLocationListRes);
        }
        return list;
    }



    /**
     * 对象转换
     * @param waybillList
     * @return
     */
    public List<WaybillOrderListRes> getOrderList(List<WaybillInfoDTO> waybillList) {
        List<WaybillOrderListRes> list = new ArrayList<WaybillOrderListRes>();
        for(int i = 0;i<waybillList.size();i++) {
            WaybillOrderListRes extensionOrderListRes = new WaybillOrderListRes();
            extensionOrderListRes.setOrderId(String.valueOf(waybillList.get(i).getId()));
            /**
             * 平台编码
             */
            extensionOrderListRes.setSiteCode(waybillList.get(i).getSiteCode());

            /**
             * 转单运单父级运单id（t_waybill_info.id）
             */
            extensionOrderListRes.setParentWaybillId(String.valueOf(waybillList.get(i).getParentWaybillId()));

            /**
             * 运单性质（1业务派单运单、2运输派单运单、4运输揽件运单）
             */
            extensionOrderListRes.setWaybillNature(String.valueOf(waybillList.get(i).getWaybillNature()));

            /**
             * 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
             */
            extensionOrderListRes.setTransportType(String.valueOf(waybillList.get(i).getTransportType()));

            /**
             * 承运方用户名称
             */
            extensionOrderListRes.setTransportUserName(waybillList.get(i).getTransportUserName());

            /**
             * 承运方用户编码
             */
            extensionOrderListRes.setTransportUserId(String.valueOf(waybillList.get(i).getTransportUserId()));

            /**
             * 承运方企业名称
             */
            extensionOrderListRes.setTransportCompanyName(waybillList.get(i).getTransportCompanyName());

            /**
             * 承运方所属主帐号id
             */
            extensionOrderListRes.setTransportOwnUserId(String.valueOf(waybillList.get(i).getTransportOwnUserId()));

            /**
             * 承运方固定电话
             */
            extensionOrderListRes.setTransportTelephone(waybillList.get(i).getTransportTelephone());

            /**
             * 承运方手机号码
             */
            extensionOrderListRes.setTransportMobile(waybillList.get(i).getTransportMobile());

            /**
             * 承运方车牌号码
             */
            extensionOrderListRes.setTransportCarnumber(waybillList.get(i).getTransportCarnumber());

            /**
             * 起始地(省-市-县)
             */
            extensionOrderListRes.setStartAddress(SystemsUtil.buildAddress(waybillList.get(i).getDepartureProvinceValue(),waybillList.get(i).getDepartureCityValue(),waybillList.get(i).getDepartureCountyValue()));

            /**
             * 目的地(省-市-县)
             */
            extensionOrderListRes.setEndAddress(SystemsUtil.buildAddress(waybillList.get(i).getReceiveProvinceValue(),waybillList.get(i).getReceiveCityValue(),waybillList.get(i).getReceiveCountyValue()));

            /**
             * 要求装货时间(yyyy-mm-dd)
             */
            extensionOrderListRes.setRequestStartTime(DateUtil.dateFormat(waybillList.get(i).getNeedStartTime()));

            /**
             * 要求卸货时间(yyyy-mm-dd)
             */
            extensionOrderListRes.setRequestEndTime(DateUtil.dateFormat(waybillList.get(i).getNeedEndTime()));

            /**
             * 总体积
             */
            extensionOrderListRes.setTotalCubage(String.valueOf(waybillList.get(i).getTotalCubage()));

            /**
             * 总件数
             */
            extensionOrderListRes.setTotalQuantity(String.valueOf(waybillList.get(i).getTotalQuantity()));

            /**
             * 总重量
             */
            extensionOrderListRes.setTotalWeight(String.valueOf(waybillList.get(i).getTotalWeight()));

            /**
             * 我的报价运费
             */
            if(waybillList.get(i).getTotalFare() == null) {
                extensionOrderListRes.setTotalFare("");
            }else {
                extensionOrderListRes.setTotalFare(String.valueOf(waybillList.get(i).getTotalFare()));
            }


            /**
             * 我的报价预付款
             */
            if(waybillList.get(i).getPrepayFare() == null) {
                extensionOrderListRes.setPrepayFare("");
            }else {
                extensionOrderListRes.setPrepayFare(String.valueOf(waybillList.get(i).getPrepayFare()));
            }


            /**
             * 承运方报价状态(-2货主不采纳承运方的报价、0承运方未报价、1承运方已报价、2货主已采纳承运方的报价)
             */
            extensionOrderListRes.setTransportFareState(String.valueOf(waybillList.get(i).getTransportFareState()));

            /**
             * 承运方的报价运费
             */
            if(waybillList.get(i).getTransportTotalFare() == null) {
                extensionOrderListRes.setTransportTotalFare("");
            }else {
                extensionOrderListRes.setTransportTotalFare(String.valueOf(waybillList.get(i).getTransportTotalFare()));
            }


            /**
             * 承运方的报价预付款
             */
            if(waybillList.get(i).getTransportPrepayFare() == null) {
                extensionOrderListRes.setTransportPrepayFare("");
            }else {
                extensionOrderListRes.setTransportPrepayFare(String.valueOf(waybillList.get(i).getTransportPrepayFare()));
            }


            /**
             * 运单锁（0正常、1运单已锁定）
             */
            extensionOrderListRes.setWaybillLock(String.valueOf(waybillList.get(i).getWaybillLock()));

            /**
             * 运单状态:1待接单、-1拒绝接单、12已接单、6交易取消、3已装货、7已卸货、9司机未安装App
             */
            extensionOrderListRes.setState(String.valueOf(waybillList.get(i).getState()));

            /**
             * 状态名称
             */
            extensionOrderListRes.setStateName(WayBillConvert.waybillStateName(String.valueOf(waybillList.get(i).getState())));

            /**
             * 转单状态（0未转单、1已转单）
             */
            extensionOrderListRes.setTurnedState(String.valueOf(waybillList.get(i).getTurnedState()));


            /**
             * 是否转单而来（0否 1是）
             */
            extensionOrderListRes.setIsTurned(String.valueOf(waybillList.get(i).getIsTurned()));

            /**
             * 运单号
             */
            extensionOrderListRes.setWaybillNum(waybillList.get(i).getWaybillNum());

            /**
             * 运单名称
             */
            extensionOrderListRes.setWaybillName(waybillList.get(i).getWaybillName());

            /**
             * 承运方认证状态
             */
            extensionOrderListRes.setAuthState(getSumitType(String.valueOf(waybillList.get(i).getTransportType()),waybillList.get(i).getTransportOwnUserId()));
            list.add(extensionOrderListRes);
        }
        return list;
    }


    private WaybillOrderRes getOrderInfo(WaybillDetailDTO waybillDetailDTO) {
        WaybillOrderRes extensionOrderRes = new WaybillOrderRes();
        WaybillInfoDTO waybillInfoDTO = waybillDetailDTO.getWaybillInfoDTO();
        List<CarrierAssemDTO> carrierAssemDTOList = waybillDetailDTO.getCarrierAssemDTOList();
        extensionOrderRes.setOrderId(String.valueOf(waybillInfoDTO.getId()));
        /**
         * 平台编码
         */
        extensionOrderRes.setSiteCode(waybillInfoDTO.getSiteCode());

        /**
         * 转单运单父级运单id（t_waybill_info.id）
         */
        extensionOrderRes.setParentWaybillId(String.valueOf(waybillInfoDTO.getParentWaybillId()));

        /**
         * 运单性质（1业务派单运单、2运输派单运单、4运输揽件运单）
         */
        extensionOrderRes.setWaybillNature(String.valueOf(waybillInfoDTO.getWaybillNature()));

        /**
         * 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
         */
        extensionOrderRes.setTransportType(String.valueOf(waybillInfoDTO.getTransportType()));

        /**
         * 承运方用户名称
         */
        extensionOrderRes.setTransportUserName(waybillInfoDTO.getTransportUserName());

        /**
         * 承运方用户编码
         */
        extensionOrderRes.setTransportUserId(String.valueOf(waybillInfoDTO.getTransportUserId()));

        /**
         * 承运方企业名称
         */
        extensionOrderRes.setTransportCompanyName(waybillInfoDTO.getTransportCompanyName());

        /**
         * 承运方所属主帐号id
         */
        extensionOrderRes.setTransportOwnUserId(String.valueOf(waybillInfoDTO.getTransportOwnUserId()));

        /**
         * 承运方固定电话
         */
        extensionOrderRes.setTransportTelephone(waybillInfoDTO.getTransportTelephone());

        /**
         * 承运方手机号码
         */
        extensionOrderRes.setTransportMobile(waybillInfoDTO.getTransportMobile());

        /**
         * 承运方车牌号码
         */
        extensionOrderRes.setTransportCarnumber(waybillInfoDTO.getTransportCarnumber());


        /**
         * 起始地(省-市-县)
         */
        extensionOrderRes.setStartAddress(SystemsUtil.buildAddress(waybillInfoDTO.getDepartureProvinceValue(),waybillInfoDTO.getDepartureCityValue(),waybillInfoDTO.getDepartureCountyValue()));

        /**
         * 目的地(省-市-县)
         */
        extensionOrderRes.setEndAddress(SystemsUtil.buildAddress(waybillInfoDTO.getReceiveProvinceValue(),waybillInfoDTO.getReceiveCityValue(),waybillInfoDTO.getReceiveCountyValue()));

        /**
         * 要求装货时间(yyyy-mm-dd)
         */
        extensionOrderRes.setRequestStartTime(DateUtil.dateFormat(waybillInfoDTO.getNeedStartTime()));

        /**
         * 要求卸货时间(yyyy-mm-dd)
         */
        extensionOrderRes.setRequestEndTime(DateUtil.dateFormat(waybillInfoDTO.getNeedEndTime()));

        /**
         * 总体积
         */
        extensionOrderRes.setTotalCubage(SystemsUtil.buildVolumeUnit(waybillInfoDTO.getTotalCubage()));

        /**
         * 总件数
         */
        extensionOrderRes.setTotalQuantity(SystemsUtil.buildQuantity(waybillInfoDTO.getTotalQuantity()));

        /**
         * 总重量
         */
        extensionOrderRes.setTotalWeight(SystemsUtil.buildWeightUnit(waybillInfoDTO.getTotalWeight()));

        /**
         * 我的报价运费
         */
        if(waybillInfoDTO.getTotalFare() == null) {
            extensionOrderRes.setTotalFare("");
        }else {
            extensionOrderRes.setTotalFare(String.valueOf(waybillInfoDTO.getTotalFare()));
        }


        /**
         * 我的报价预付款
         */
        if(waybillInfoDTO.getPrepayFare() == null) {
            extensionOrderRes.setPrepayFare("");
        }else {
            extensionOrderRes.setPrepayFare(String.valueOf(waybillInfoDTO.getPrepayFare()));
        }


        /**
         * 承运方报价状态(-2货主不采纳承运方的报价、0承运方未报价、1承运方已报价、2货主已采纳承运方的报价)
         */
        extensionOrderRes.setTransportFareState(String.valueOf(waybillInfoDTO.getTransportFareState()));

        /**
         * 承运方的报价运费
         */
        if(waybillInfoDTO.getTransportTotalFare() == null) {
            extensionOrderRes.setTransportTotalFare("");
        }else {
            extensionOrderRes.setTransportTotalFare(String.valueOf(waybillInfoDTO.getTransportTotalFare()));
        }


        /**
         * 承运方的报价预付款
         */
        if(waybillInfoDTO.getTransportPrepayFare() == null) {
            extensionOrderRes.setTransportPrepayFare("");
        }else {
            extensionOrderRes.setTransportPrepayFare(String.valueOf(waybillInfoDTO.getTransportPrepayFare()));
        }

        /**
         * 运单锁（0正常、1运单已锁定）
         */
        extensionOrderRes.setWaybillLock(String.valueOf(waybillInfoDTO.getWaybillLock()));

        /**
         * 运单状态:1待接单、-1拒绝接单、12已接单、6交易取消、3已装货、7已卸货、9司机未安装App
         */
        extensionOrderRes.setState(String.valueOf(waybillInfoDTO.getState()));

        /**
         * 状态名称
         */
        extensionOrderRes.setStateName(WayBillConvert.waybillStateName(String.valueOf(waybillInfoDTO.getState())));

        /**
         * 转单状态（0未转单、1已转单）
         */
        extensionOrderRes.setTurnedState(String.valueOf(waybillInfoDTO.getTurnedState()));


        /**
         * 是否转单而来（0否 1是）
         */
        extensionOrderRes.setIsTurned(String.valueOf(waybillInfoDTO.getIsTurned()));

        /**
         * 运单号
         */
        extensionOrderRes.setWaybillNum(waybillInfoDTO.getWaybillNum());

        /**
         * 运单名称
         */
        extensionOrderRes.setWaybillName(waybillInfoDTO.getWaybillName());

        /**
         * 承运方认证状态
         */
        extensionOrderRes.setAuthState(getSumitType(String.valueOf(waybillInfoDTO.getTransportType()),waybillInfoDTO.getTransportOwnUserId()));

        /**
         * 装货地联系人
         */
        extensionOrderRes.setDepartureContact(waybillInfoDTO.getDepartureContact());


        /**
         * 发货地联系电话
         */
        if(StringUtils.isNotEmpty(waybillInfoDTO.getDepartureDistrictNumber()) && StringUtils.isNotEmpty(waybillInfoDTO.getDepartureTelephone())) {
            extensionOrderRes.setDepartureTelephone(waybillInfoDTO.getDepartureDistrictNumber()+waybillInfoDTO.getDepartureTelephone());
        }else if(StringUtils.isNotEmpty(waybillInfoDTO.getDepartureTelephone())){
            extensionOrderRes.setDepartureTelephone(waybillInfoDTO.getDepartureTelephone());
        }else {
            extensionOrderRes.setDepartureTelephone("");
        }


        /**
         * 装货地联系手机
         */
        extensionOrderRes.setDepartureMobile(extensionOrderRes.getDepartureMobile());


        /**
         * 卸货地联系人
         */
        extensionOrderRes.setReceiveContact(waybillInfoDTO.getReceiveContact());


        /**
         * 收货地联系电话
         */
        if(StringUtils.isNotEmpty(waybillInfoDTO.getReceiveDistrictNumber()) && StringUtils.isNotEmpty(waybillInfoDTO.getReceiveTelephone())) {
            extensionOrderRes.setReceiveTelephone(waybillInfoDTO.getReceiveDistrictNumber()+waybillInfoDTO.getReceiveTelephone());
        }else if(StringUtils.isNotEmpty(waybillInfoDTO.getReceiveTelephone())){
            extensionOrderRes.setReceiveTelephone(waybillInfoDTO.getReceiveTelephone());
        }else {
            extensionOrderRes.setReceiveTelephone("");
        }


        /**
         * 收货地联系手机
         */
        extensionOrderRes.setReceiveMobile(waybillInfoDTO.getReceiveMobile());

        /**
         * 创建时间
         */
        extensionOrderRes.setOderCreateTime(DateUtil.dateTimeToStr(waybillInfoDTO.getCreateTime()));

        /**
         * 修改时间
         */
        extensionOrderRes.setOrderModifyTime(DateUtil.dateTimeToStr(waybillInfoDTO.getModifyTime()));

        /**
         * 备注
         */
        extensionOrderRes.setRemark(waybillInfoDTO.getRemark());

        /**
         * 运单总待收货款
         */
        extensionOrderRes.setTotalCollectionPayment(String.valueOf(waybillInfoDTO.getTotalCollectionPayment()));

        /**
         * 来自托单总数量（笔）
         */
        extensionOrderRes.setTotalCarrierCount(String.valueOf(carrierAssemDTOList.size()));

        /**
         * 转自运单父级运单编号
         */
        Response<WaybillInfoDTO> waybillInfoDTOResponse = waybillInfoService.queryWayBillInfo(waybillInfoDTO.getParentWaybillId());
        extensionOrderRes.setParentWaybillNum(waybillInfoDTOResponse.getData().getWaybillNum());
        /**
         * 承运方累计交易数
         */
        Response<UserItemStatDTO> userItemStatDTO = userItemStatService.getByUserId(waybillInfoDTO.getTransportUserId());
        if(userItemStatDTO.getCode() != 0) {

        }
        if(userItemStatDTO.getData() == null) {
            extensionOrderRes.setTransportTotalCount("0");
        }else{
            extensionOrderRes.setTransportTotalCount(String.valueOf(userItemStatDTO.getData().getFinishOrderNum()));
        }

        /**
         * 是否评价(0未评价、1已评价)
         */
        if(waybillInfoDTO.getTransporterAssessIdent() == true || waybillInfoDTO.getCreaterAssessIdent() == true) {
            extensionOrderRes.setAssessIdent("1");
        }else {
            extensionOrderRes.setAssessIdent("0");
        }

        /**
         * 承运方头像
         */
        extensionOrderRes.setTransportHeadImgUrl(waybillInfoDTO.getTransportHeadImgUrl());

        /**
         * 运单托单列表
         */
        extensionOrderRes.setListData(getCarrierInfoRes(carrierAssemDTOList));
        return extensionOrderRes;
    }


    /**
     * 获取承运方认证状态
     * @param transportType 承运方类型 1快到网司机 2快到网货主 3区域配送用户
     * @param userId 承运方Id
     * @return
     */
    public String getSumitType(String transportType,Long userId) {
        String authState = "0";
        if("1".equals(transportType)) {
            /**
             * 快到网司机为承运方
             */
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> dirver = driverUserInfoService.getDriverUserInfo(userId);
            authState = String.valueOf(dirver.getData().getSubmitType());
        }else if("2".equals(transportType)) {
        }else if("3".equals(transportType)) {
            Response<UserLoginInfoDTO> response =  userService.getUser(userId);
            if(response.getCode() == 0 && response.getData() != null) {
                //认证标识（0 未提交认证 1 已提交认证 2 认证未通过 3 认证已通过）
                if(response.getData().getSubmitType() != null) {
                    authState = String.valueOf(response.getData().getSubmitType());
                }else {
                    authState = "0";
                }
            }
        }
        return authState;
    }


    public List<CarrierInfoRes> getCarrierInfoRes(List<CarrierAssemDTO> carrierAssemDTOList) {
        List<CarrierInfoRes> listData = new ArrayList<CarrierInfoRes>();
        for(int i = 0;i<carrierAssemDTOList.size();i++) {
            CarrierInfoRes carrierInfoRes = new CarrierInfoRes();
            CarrierBasicInfoDTO infoDTO = carrierAssemDTOList.get(i).getInfoDTO();
            List<CarrierDetailDTO> detailDTOList = carrierAssemDTOList.get(i).getDetailDTOList();
            CarrierAddressDTO addressDTO = carrierAssemDTOList.get(i).getAddressDTO();

            /**
             * 托单ID
             */
            carrierInfoRes.setCarrierId(String.valueOf(infoDTO.getId()));

            /**
             * 托单号
             */
            carrierInfoRes.setCarrierNum(infoDTO.getCarrierNum());

            /**
             * 托单起始地
             */
            StringBuffer carrierStartPcc = new StringBuffer();
            if(StringUtils.isNotEmpty(addressDTO.getDepartureProvinceValue())) {
                carrierStartPcc.append(addressDTO.getDepartureProvinceValue());
                if(StringUtils.isNotEmpty(addressDTO.getDepartureCityValue())) {
                    carrierStartPcc.append("-");
                    carrierStartPcc.append(addressDTO.getDepartureCityValue());
                    if(StringUtils.isNotEmpty(addressDTO.getDepartureCountyValue())) {
                        carrierStartPcc.append("-");
                        carrierStartPcc.append(addressDTO.getDepartureCountyValue());
                    }
                }
            }
            carrierInfoRes.setCarrierStartAddress(carrierStartPcc.toString());

            /**
             * 托单目的地
             */
            StringBuffer carrierEndPcc = new StringBuffer();
            if(StringUtils.isNotEmpty(addressDTO.getReceiveProvinceValue())) {
                carrierEndPcc.append(addressDTO.getReceiveProvinceValue());
                if(StringUtils.isNotEmpty(addressDTO.getReceiveCityValue())) {
                    carrierEndPcc.append("-");
                    carrierEndPcc.append(addressDTO.getReceiveCityValue());
                    if(StringUtils.isNotEmpty(addressDTO.getReceiveCountyValue())) {
                        carrierEndPcc.append("-");
                        carrierEndPcc.append(addressDTO.getReceiveCountyValue());
                    }
                }
            }
            carrierInfoRes.setCarrierEndAddress(carrierEndPcc.toString());

            /**
             * 合计体积(与合计重量二选一，无填写0)
             */
            carrierInfoRes.setTotalCubage(String.valueOf(infoDTO.getTotalCubage()));

            /**
             * 合计件数
             */
            carrierInfoRes.setTotalQuantity(String.valueOf(infoDTO.getTotalQuantity()));

            /**
             * 合计重量(与合计体积二选一，无填写0)
             */
            carrierInfoRes.setTotalWeight(String.valueOf(infoDTO.getTotalWeight()));

            /**
             * 托单货物明细
             */
            carrierInfoRes.setCargoListData(getCarrierCargoInfoRes(detailDTOList));
            listData.add(carrierInfoRes);
        }
        return listData;
    }


    /**
     * 托单货物明细转换
     * @param detailDTOList
     * @return
     */
    public List<CarrierCargoInfoRes> getCarrierCargoInfoRes(List<CarrierDetailDTO> detailDTOList) {
        List<CarrierCargoInfoRes> cargoListData = new ArrayList<CarrierCargoInfoRes>();
        for(int j = 0;j<detailDTOList.size();j++) {
            CarrierCargoInfoRes carrierCargoInfoRes = new CarrierCargoInfoRes();
            /**
             * 主键id
             */
            carrierCargoInfoRes.setCargoId(String.valueOf(detailDTOList.get(j).getId()));
            /**
             * 货品名称
             */
            carrierCargoInfoRes.setCargoName(detailDTOList.get(j).getCargoName());
            /**
             * 货物类型（1重货 2泡货）
             */
            if(detailDTOList.get(j).getWeight() != null) {
                carrierCargoInfoRes.setCargoType(String.valueOf(detailDTOList.get(j).getCargoType()));
            }
            /**
             * 重量
             */
            if(detailDTOList.get(j).getWeight() != null) {
                carrierCargoInfoRes.setWeight(String.valueOf(detailDTOList.get(j).getWeight()));
            }else {
                carrierCargoInfoRes.setWeight("0");
            }
            /**
             * 体积
             */
            if(detailDTOList.get(j).getCubage() != null) {
                carrierCargoInfoRes.setCubage(String.valueOf(detailDTOList.get(j).getCubage()));
            }else {
                carrierCargoInfoRes.setCubage("0");
            }
            /**
             * 件数
             */
            if(detailDTOList.get(j).getQuantity() != null) {
                carrierCargoInfoRes.setQuantity(String.valueOf(detailDTOList.get(j).getQuantity()));
            }else {
                carrierCargoInfoRes.setQuantity("0");
            }
            cargoListData.add(carrierCargoInfoRes);
        }
        return cargoListData;
    }



    /**
     * 返回输入地址的经纬度坐标
     * key lng(经度),lat(纬度)
     */
    public static Map<String,String> getGeocoderLatitude(String address){
        BufferedReader in = null;
        Map<String,String> map = new HashMap<String,String>();
        map.put("lng", "");
        map.put("lat", "");
        try {
            //将地址转换成utf-8的16进制
            address = URLEncoder.encode(address, "UTF-8");
            URL tirc = new URL("http://api.map.baidu.com/geocoder?address="+ address +"&output=json&key="+ KEY_1);
            in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while((res = in.readLine())!=null){
                sb.append(res.trim());
            }
            String str = sb.toString();
            if(StringUtils.isNotEmpty(str)){
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if(lngStart > 0 && lngEnd > 0 && latEnd > 0){
                    String lng = str.substring(lngStart+5, lngEnd);
                    String lat = str.substring(lngEnd+7, latEnd);
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
