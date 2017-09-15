package com.cy.driver.action;

import com.alibaba.fastjson.JSON;
import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.cargo.service.dto.CargoWebUserDTO;
import com.cy.cargo.service.dto.QuoteInfoDTO;
import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.cargo.service.dto.base.Response;
import com.cy.core.redis.RedisData;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.redis.NetWordPhoneClient;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.*;
import com.cy.driver.service.*;
import com.cy.location.service.DriverLinePointService;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.location.service.dto.ViewCargoLineSaveDTO;
import com.cy.order.service.dto.CommentCountDTO;
import com.cy.pass.service.CompanyInfoService;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.Enum.company.EnumCompanyImgType;
import com.cy.pass.service.dto.init.MsgCenterCountDTO;
import com.cy.rdcservice.service.dto.UserLoginInfoDTO;
import com.cy.search.service.dto.response.Cargo2DTO;
import com.cy.search.service.dto.response.CargoDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/14.
 * 货源处理
 */
@Scope("prototype")
@RestController("cargoHandlerAction")
public class CargoHandlerAction extends BaseAction {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private int confineAuthBroNum;
    private int confineNoAuthBroNum;
    private int confineAuthBroPage;
    private int confineNoAuthBroPage;
    private String debangDriverCode;

    @Value("${'protect.data.browseNum.authed'}")
    public void setConfineAuthBroNum(int confineAuthBroNum) {
        this.confineAuthBroNum = confineAuthBroNum;
    }

    @Value("${'protect.data.browseNum.noAuthed'}")
    public void setConfineNoAuthBroNum(int confineNoAuthBroNum) {
        this.confineNoAuthBroNum = confineNoAuthBroNum;
    }

    @Value("${'protect.data.browsePage.authed'}")
    public void setConfineAuthBroPage(int confineAuthBroPage) {
        this.confineAuthBroPage = confineAuthBroPage;
    }

    @Value("${'protect.data.browsePage.noAuthed'}")
    public void setConfineNoAuthBroPage(int confineNoAuthBroPage) {
        this.confineNoAuthBroPage = confineNoAuthBroPage;
    }

    @Value("${'debang.driver.code'}")
    public void setDebangDriverCode(String debangDriverCode) {
        this.debangDriverCode = debangDriverCode;
    }

    @Resource
    private CargoHandlerService cargoHandlerService;
    @Resource
    private HomeNumInfoService homeNumInfoService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private NetWordPhoneClient netWordPhoneClient;
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private MsgCenterService msgCenterService;
    @Resource
    private SystemData systemData;
    @Resource
    private SearchCargoHandlerService searchCargoHandlerService;
    @Resource
    private MyQuoteInfoHandleService myQuoteInfoHandleService;
    @Resource
    private AssessService assessService;
    @Resource
    private CompanyInfoService companyInfoService;
    @Resource
    private OwnerItemService ownerItemService;
    @Resource
    private EmptyCarReportService emptyCarReportService;
    @Resource
    private OftenCityHandleService oftenCityHandleService;
    @Resource
    private DriverLineTrackService driverLineTrackService;
    @Resource
    private RedisService redisService;
    @Resource
    private DriverLinePointService driverLinePointService;
    @Resource
    private LocationService locationService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private CloudUserService cloudUserService;



    private String activityName;

    /**
     * 精准货源列表查询
     */
    @RequestMapping(value = "/accurateCargo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ACCURATE_CARGO)
    @Log(type = LogEnum.ACCURATE_CARGO)
    public Object accurateCargo(HttpServletRequest request, HttpServletResponse response,
                                Long page) {
        try {
            Long driverId = findUserId(request);
            if (page == null || page.longValue() <= 0L)
                page = 1L;
            String showEmptyReport = "0";// 0 显示 1不显示
            String showOftenCity = "0";//0显示 1不显示
            com.cy.pass.service.dto.base.Response<DriverEmptyReportInfoDTO> result = emptyCarReportService.queryDriverEmptyReportInfo(findUserId());
            if (result.isSuccess() && result.getData() != null) {
                DriverEmptyReportInfoDTO driverEmptyReportInfoDTO = result.getData();
                //当日
                List<DriverTodayEmptyReportDTO> todayEmptyReportDTOList = driverEmptyReportInfoDTO.getTodayEmptyCarList();
                if (todayEmptyReportDTOList != null && todayEmptyReportDTOList.size() > 0) {
                    showEmptyReport = "1";//存在空车上报
                }

                List<DriverBusinessLineDTO> businessLineDTOList = driverEmptyReportInfoDTO.getFutureEmptyCarList();
                if (businessLineDTOList != null && businessLineDTOList.size() > 0) {
                    showEmptyReport = "1";//存在空车上报
                }
            }
            com.cy.pass.service.dto.base.Response<List<DriverOftenCityDTO>> serResponse = oftenCityHandleService.list(findUserIdStr(request));
            if (serResponse.isSuccess() && serResponse.getData() != null) {
                showOftenCity = "1";
            }
            /****
             * 德邦查询货源数据(开始)
             */
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUserResponse = driverUserHandlerService.getDriverUserInfo(driverId);
            if(driverUserResponse.isSuccess()) {
                DriverUserInfoDTO driverUser = driverUserResponse.getData();
                Map<String,String> map = getDebangUserCode();
                String driverCity = map.get(driverUser.getCode());
                if(StringUtils.isNotEmpty(driverCity)) {
                    LOG.info("德邦用户查看精准货源列表:page={}",page);
                    com.cy.cargo.service.dto.base.PageResult<CargoInfoDTO> pageResult = searchCargoHandlerService.pageByCustom(driverId,driverCity,page.intValue());
                    Map<String,Object> map1 = new HashMap<String,Object>();
                    if(pageResult.isSuccess()) {
                        map1.put("cargoAllNums", pageResult.getTotalRecord());
                        map1.put("cargoAllPage", pageResult.getPageIndex());
                        map1.put("cargoList", convertCargoInfoDTOList(pageResult.getDataList(), driverId, Boolean.TRUE));
                    }else {
                        map1.put("cargoAllNums", 0);
                        map1.put("cargoAllPage", 0);
                        LOG.error("查询精准货源列表失败，调用search服务搜索精准货源失败，失败消息={}", pageResult.getMessage());
                    }
                    map1.put("showEmptyReport", "1");
                    map1.put("showOftenCity", "1");
                    updRespHeadSuccess(response);
                    return map1;
                }
            }
            /****
             * 德邦查询货源数据(结束)
             */
            LastLocationDTO lastLoc = locationService.queryLastLocation(driverId);
            Double carLength=null;
            Double carWeight=null;
            Double carCubage=null;
            String vehicleType=null;
            String carriageType=null;
            com.cy.pass.service.dto.base.Response<CarDTO> carDTOResponse = driverUserHandlerService.getCarInfo(String.valueOf(findUserId()));
            if(carDTOResponse.isSuccess()){
                carLength = carDTOResponse.getData().getCarLengthNewest()==null?null:carDTOResponse.getData().getCarLengthNewest().doubleValue();
                carWeight = carDTOResponse.getData().getCarWeightNewest()==null?null:carDTOResponse.getData().getCarWeightNewest().doubleValue();
                carCubage = carDTOResponse.getData().getCarCubageNewest()==null?null:carDTOResponse.getData().getCarCubageNewest().doubleValue();
                vehicleType = carDTOResponse.getData().getCarTypesNew();
                carriageType = carDTOResponse.getData().getCarriageType();
            }
            Map<String, Object> map = new HashMap<String, Object>();
            com.cy.search.service.dto.base.PageResult<Cargo2DTO> pageResult = searchCargoHandlerService.accurateCargo3(driverId, page.intValue(),lastLoc,carLength,carWeight,carCubage,vehicleType,carriageType);
            if (pageResult.isSuccess() && pageResult!=null && pageResult.getDataList()!=null) {
                map.put("cargoAllNums", pageResult.getTotalRecord());
                map.put("cargoAllPage", pageResult.getTotalPage());
                for (Cargo2DTO cargoDTO : pageResult.getDataList()) {
                    if(StringUtils.isNotEmpty(cargoDTO.getDeployUserid())) {
                        UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(Long.valueOf(cargoDTO.getDeployUserid()));
                        if (userLoginInfoDTO != null) {
                            cargoDTO.setContactMobilephone(userLoginInfoDTO.getMobilephone());
                        }
                    }
                }
                map.put("cargoList", convertCargo2List(pageResult.getDataList(), driverId, Boolean.TRUE));
            } else {
                map.put("cargoAllNums", 0);
                map.put("cargoAllPage", 0);
                LOG.error("查询精准货源列表失败，调用search服务搜索精准货源失败，失败消息={}", pageResult.getMessage());
            }
            map.put("showEmptyReport", showEmptyReport);
            map.put("showOftenCity", showOftenCity);
            updRespHeadSuccess(response);
            return map;
        } catch (Exception e) {
            LOG.error("获取精准货源列表出错", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }


    /**
     * 附近货源列表查询
     */
    @RequestMapping(value = "/queryNearCargoList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_QUERY_NEAR_CARGO)
    @Log(type = LogEnum.QUERY_NEAR_CARGO_LIST)
    public Object queryNearByCargo(HttpServletRequest request, HttpServletResponse response,
                                   Long page, String longitude, String latitude, Integer sort) {
        try {
            Long driverId = findUserId(request);
            if (page == null || page.longValue() <= 0L)
                page = 1L;
            if (sort == null)
                sort = 0;
            /** 搜索服务的试验点 王远航 修改时间：2016-03-11 15:30 */
            if (findAuthState().intValue() != 3) {
                if (page.intValue() >= confineNoAuthBroPage) {
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                }
            } else {
                if (page.intValue() >= confineAuthBroPage) {
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                }
            }
            com.cy.search.service.dto.base.PageResult<Cargo2DTO> pageResult = searchCargoHandlerService.queryNearCargo(driverId, page.intValue(), sort,longitude,latitude);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("showType", "1");
            if (pageResult.isSuccess()) {
                map.put("cargoAllNums", pageResult.getTotalRecord());
                map.put("cargoAllPage", pageResult.getTotalPage());
                map.put("cargoList", convertCargo2List(pageResult.getDataList(), driverId, Boolean.FALSE));

                /** 感兴趣的货源 司机3.0第一个版本存在，后续就没有下文了 */
//                if (page == 1 && (pageResult.getDataList() == null || pageResult.getDataList().size() == 0)) {
//                    map.put("showType", "2");
//                    Response<List<TasteCargoDTO>> resultList = cargoHandlerService.listCountByProvince();
//
//                    if (resultList.isSuccess()) {
//                        map.put("tasteCargoList", resultList.getData());
//                    } else {
//                        updRespHeadError(response);
//                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
//                    }
//                }
            } else {
                map.put("cargoAllNums", 0);
                map.put("cargoAllPage", 0);
                LOG.error("查询附近货源列表失败，调用search服务搜索附近货源失败，失败消息={}", pageResult.getMessage());
            }
            updRespHeadSuccess(response);
            return map;
        } catch (Exception e) {
            LOG.error("获取附近货源列表出错。", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 附近货源数量
     *
     * @param request
     * @param response
     * @param longitude
     * @param latitude
     * @param province
     * @param city
     * @param county
     * @param town
     * @param address
     * @return
     */
    @RequestMapping(value = "/countNearCargo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.COUNT_NEAR_CARGO)
    @Log(type = LogEnum.COUNT_NEAR_CARGO)
    public Object countNearCargo(HttpServletRequest request, HttpServletResponse response, String longitude, String latitude,
                                 String province, String city, String county, String town, String address) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            //坐标不为空
            if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)) {
                updRespHeadSuccess(response);
                return resultMap;
            }
            Long driverId = findUserId(request);
            /** 搜索服务的试验点 王远航 修改时间：2016-03-11 15:30 */
            long cargoNum = searchCargoHandlerService.countNearCargo(driverId,longitude,latitude);
            resultMap.put("cargoNums", cargoNum);
            //待收运费数量
            int freightNums = queryOrderService.collectFreightNums(driverId);

            MsgCenterCountDTO resultMsgcount = msgCenterService.notReadMessageCount(driverId);
            if (resultMsgcount != null) {
                freightNums = freightNums + resultMsgcount.getNotReadTotalNum();
            }
            resultMap.put("Mynum", freightNums);//我的数量
            com.cy.pass.service.dto.base.Response<HomeOtherCountsBO> resultData1 = homeNumInfoService.queryIndexNums(driverId);
            if (resultData1.isSuccess()) {
                HomeOtherCountsBO homeOtherCountsBO = resultData1.getData();
                if (homeOtherCountsBO != null) {
                    resultMap.put("quoteNums", homeOtherCountsBO.getQuoteNums());
                    resultMap.put("waitOrderNums", homeOtherCountsBO.getWaitOrderNums());
                    resultMap.put("orderNums", homeOtherCountsBO.getOrderNums());
                }
            }

            /** TODO 直接去掉，红包活动已经结束 */
//            com.cy.pass.service.dto.base.Response<OpsActivityInfoDTO> response1 = redPacketsService.getRedPackets(activityName);
//            if(response1.isSuccess()){//如果该用户有红包金额则调用存放用户的红包接口
//                if(response1.getData() != null ){
//                    if(response1.getData().getRewardDriverUnitprice() != null){
//                        //推送消息计数
//                        SysPushCounterParamDTO sysPushCounterParamDTO = new SysPushCounterParamDTO();
//                        sysPushCounterParamDTO.setUserId(findUserId(request));
//                        sysPushCounterParamDTO.setCount(1);
//                        sysPushCounterParamDTO.setPushType(SysPushCounterEnum.PushType.REG_RED_PAC.getCode());
//                        sysPushCounterParamDTO.setUserKind(SysPushCounterEnum.UserKind.DRIVER_USER.getCode());
//                        sysPushCounterParamDTO.setUserId(sysPushCounterParamDTO.getUserId());
//                        pushMsgService.findPushCounter(sysPushCounterParamDTO);
//
//                    }
//                }
//            }
            updRespHeadSuccess(response);
            return resultMap;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("获取附近货源数量出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 首页数量（3.3以上版本）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/countNearCargoNew", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.HOME_COUNT_CARGO_NEW)
    @Log(type = LogEnum.COUNT_NEAR_CARGO)
    public Object countNearCargoNew(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long driverId = findUserId(request);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            //待收运费数量
            int freightNums = queryOrderService.collectFreightNums(driverId);
            /**
             * 修改时间：2016年4月23日，18:02
             * 修改人：  gjw
             */
            MsgCenterCountDTO msgCenterCountDTO = msgCenterService.notReadMessageCount(driverId);
            if (msgCenterCountDTO != null) {
                freightNums = freightNums + msgCenterCountDTO.getNotReadTotalNum();
            }
            com.cy.pass.service.dto.base.Response<HomeOtherCountsBO> resultData1 = homeNumInfoService.queryIndexNums(driverId);
            if (resultData1.isSuccess()) {
                HomeOtherCountsBO homeOtherCountsBO = resultData1.getData();
                if (homeOtherCountsBO != null) {
                    resultMap.put("histOrderNums", String.valueOf(queryOrderService.selectByOrderComplete(driverId)));
                    resultMap.put("quoteNums", homeOtherCountsBO.getQuoteNums());
                    resultMap.put("waitOrderNums", homeOtherCountsBO.getWaitOrderNums());
                    resultMap.put("orderNums", homeOtherCountsBO.getOrderNums());
                    resultMap.put("Mynum", freightNums);
                }
                updRespHeadSuccess(response);
                return resultMap;
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("获取附近货源数量出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 附近货源数量（3.3以上版本）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/nearCargoCountNew", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.NEAR_CARGO_COUNT_NEW)
    @Log(type = LogEnum.COUNT_NEAR_CARGO)
    public Object nearCargoCountNew(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long driverId = findUserId(request);
            LastLocationDTO lastLoc = locationService.queryLastLocation(driverId);
            /** 搜索服务的试验点 王远航 修改时间：2016-03-11 15:30 */
            long count = searchCargoHandlerService.countNearCargo(driverId,lastLoc.getLongitude(),lastLoc.getLatitude());
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("cargoNums", count);
            updRespHeadSuccess(response);
            return resultMap;
        } catch (Exception e) {
            LOG.error("获取附近货源数量出错。", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 搜索货源
     */
    @RequestMapping(value = "/queryCargoList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_CARGO_LIST)
    @Log(type = LogEnum.QUERY_CARGO_LIST)
    public Object queryCargoList(HttpServletRequest request, HttpServletResponse response, SearchCargoParam searchCargoParam) {
        try {
            if (LOG.isDebugEnabled()) LOG.debug("搜索货源接口入参：searchCargoParam={}", JSON.toJSONString(searchCargoParam));
            //不合法
            if (searchCargoParam == null || searchCargoParam.getPage() <= 0) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            //时间不为空
            if (StringUtils.isEmpty(searchCargoParam.getsTime())) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20078);
            }
            //出发省编号不为空

            if (StringUtils.isEmpty(searchCargoParam.getsProCode())) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20069);
            }
            if (findAuthState().intValue() != 3) {
                if (searchCargoParam.getPage() >= confineNoAuthBroPage) {
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                }
            } else {
                if (searchCargoParam.getPage() >= confineAuthBroPage) {
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                }
            }
            Long driverId = findUserId(request);
            //保存司机填入的搜货信息作为线路匹配的依据
            com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, searchCargoParam, Constants.QUERY_CARGO);
            if (!saveLineRes.isSuccess()) {
                LOG.debug("locationLineService服务调用失败");
            }
            //如果司机要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
            String requestTime = searchCargoParam.getsTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date datefm = sdf.parse(requestTime);
            String requestTimefm = DateUtil.dateToStr(datefm);
            String today = DateUtil.dateToStr(new Date());
            if (requestTimefm.equals(today)) {
                com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, searchCargoParam, Constants.QUERY_CARGO);
                if (!saveDayLineRes.isSuccess()) {
                    LOG.debug("driverDayLineService服务调用失败");
                }
            }

            String vehicleTypeCopy = StringUtils.isEmpty(searchCargoParam.getVehicleTypeTwo()) ? searchCargoParam.getVehicleType() : searchCargoParam.getVehicleTypeTwo();
            String carriageTypeCopy = StringUtils.isEmpty(searchCargoParam.getCarriageTypeTwo()) ? searchCargoParam.getCarriageType() : searchCargoParam.getCarriageTypeTwo();
            /** 搜索参数 */
            SearchCargoBO searchBO = new SearchCargoBO();
            //yanst 2016/4/25 3.4 增加字段 装货地省名称、装货地市名称、装货地区名称及编码、 卸货地省名称、卸货地市名称、卸货地区名称及编码
            searchBO.setDriverId(driverId);
            searchBO.setStartProvinceCode(searchCargoParam.getsProCode());
            searchBO.setStartProvinceValue(searchCargoParam.getsProValue());//3.4增加
            if(StringUtils.isNotEmpty(searchCargoParam.getsCityCode())) {
                searchBO.setStartCityCode(searchCargoParam.getsCityCode());
                searchBO.setStartCityValue(searchCargoParam.getsCityValue());//3.4增加
            }
            searchBO.setStartCountyCode(searchCargoParam.getsCountyCode());//3.4增加
            searchBO.setStartCountyValue(searchCargoParam.getsCountyValue());//3.4增加
            searchBO.setEndProvinceCode(searchCargoParam.geteProCode());
            searchBO.setEndProvinceValue(searchCargoParam.geteProValue());//3.4增加
            if(StringUtils.isNotEmpty(searchCargoParam.geteCityCode())) {
                searchBO.setEndCityCode(searchCargoParam.geteCityCode());
                searchBO.setEndCityValue(searchCargoParam.geteCityValue());//3.4增加
            }
            searchBO.setEndCountyCode(searchCargoParam.geteCountyCode());//3.4增加
            searchBO.setEndCountyValue(searchCargoParam.geteCountyValue());//3.4增加
            searchBO.setVehicleType(vehicleTypeCopy);
            searchBO.setCarriageType(carriageTypeCopy);
            if (StringUtils.isNotEmpty(searchCargoParam.getCarLength()))
                searchBO.setCarLength(Double.parseDouble(searchCargoParam.getCarLength()));
            if (StringUtils.isNotEmpty(searchCargoParam.getWeight()))
                searchBO.setCargoWeight(Double.parseDouble(searchCargoParam.getWeight()));
            if (StringUtils.isNotEmpty(searchCargoParam.getVolume()))
                searchBO.setCargoCubage(Double.parseDouble(searchCargoParam.getVolume()));
            searchBO.setStartTime(DateUtil.strToDate(searchCargoParam.getsTime()));
            if (searchCargoParam.getSort() != null) {
                searchBO.setSort(searchCargoParam.getSort());
            } else {
                searchBO.setSort(0);
            }
            searchBO.setPage(searchCargoParam.getPage());
            /** 搜索服务的试验点 王远航 修改时间：2016-03-11 15:30 */
            com.cy.search.service.dto.base.PageResult<CargoDTO> pageResult = searchCargoHandlerService.queryCargoList(searchBO);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (pageResult.isSuccess()) {
                resultMap.put("cargoAllNums", pageResult.getTotalRecord());
                resultMap.put("cargoAllPage", pageResult.getTotalPage());
                resultMap.put("cargoList", convertCargoList(pageResult.getDataList(), driverId, Boolean.FALSE));
            } else {
                resultMap.put("cargoAllNums", 0);
                resultMap.put("cargoAllPage", 0);
                LOG.error("搜索货源失败，返回的失败信息={}", pageResult.getMessage());
            }
            updRespHeadSuccess(response);
            return resultMap;
        } catch (Exception e) {
            LOG.error("搜索货源出现异常。", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    public List<CargoListBO> convert2List(List<CargoWebUserDTO> cargoWebUserDTOs) {
        if (cargoWebUserDTOs == null || cargoWebUserDTOs.isEmpty()) {
            return null;
        }

        List<CargoListBO> cargoListBOs = new ArrayList<CargoListBO>();
        for (CargoWebUserDTO cargoWebUserDTO : cargoWebUserDTOs) {
            CargoListBO cargoListBO = new CargoListBO();
            cargoListBO.setCargoId(cargoWebUserDTO.getId());//货物ID
            cargoListBO.setCompanyName(cargoWebUserDTO.getCompanyName());
            cargoListBO.setCompanyAuthStatus(cargoWebUserDTO.getSubmitType() == null ? (byte) 0 : cargoWebUserDTO.getSubmitType());
            cargoListBO.setStartAddress(StrUtil.strJoint(cargoWebUserDTO.getStartProvince(), cargoWebUserDTO.getStartCity(), cargoWebUserDTO.getStartCounty()));
            cargoListBO.setEndAddress(StrUtil.strJoint(cargoWebUserDTO.getEndProvince(), cargoWebUserDTO.getEndCity(), cargoWebUserDTO.getEndCounty()));
            cargoListBO.setCargoName(cargoWebUserDTO.getCargoName());
            //重量(单位：吨)
            cargoListBO.setWeight(
                    SystemsUtil.buildWeightUnit(cargoWebUserDTO.getCargoWeight()));
            //体积(单位：方)
            cargoListBO.setVolume(
                    SystemsUtil.buildVolumeUnit(cargoWebUserDTO.getCargoCubage()));
            //要求的车长(单位：米)
            cargoListBO.setCarLength(
                    SystemsUtil.buildCarLenUnit(cargoWebUserDTO.getRequestCarLen()));

            //车辆类型名称
            cargoListBO.setVehicleTypeName(systemData.findVehicleTypeName(cargoWebUserDTO.getVehicleType()));

            //车厢类型名称
            cargoListBO.setCarriageTypeName(systemData.findCarriageTypeName(cargoWebUserDTO.getCarriageType()));

            //我的报价(带单位：元/车、元/吨、元/方)
            String quoteUnit = SystemsUtil.quoteUnitConver(cargoWebUserDTO.getQuoteType());
            String myQuote = cargoWebUserDTO.getQuoteFair() == null || "".equals(cargoWebUserDTO.getQuoteFair()) ? "" : (cargoWebUserDTO.getQuoteFair() + quoteUnit);//我的报价(带单位：元/车、元/吨、元/方)
            cargoListBO.setMyQuote(myQuote);
            //装货时间(yyyy-mm-dd hh:mm)
            cargoListBO.setStartTime(SystemsUtil.cargoOrderStartTime(cargoWebUserDTO.getRequestStartTime()));
            //发布时间(例如：几小时前)
            cargoListBO.setPubTime(SystemsUtil.getTimeStr(cargoWebUserDTO.getCreateTime()));
            //运费总价
            cargoListBO.setTotalFare(SystemsUtil.getTotalFare(cargoWebUserDTO.getTotalFare()));
            //预付运费
            cargoListBO.setPrepayFare(SystemsUtil.getTotalFare(cargoWebUserDTO.getPrepayFare()));
            cargoListBOs.add(cargoListBO);
        }
        return cargoListBOs;
    }

    /**
     * 获取货源详情
     *
     * @param cargoId
     * @return
     */
    @RequestMapping(value = "/lookCargoDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.LOOK_CARGO_DETAILS)
    @Log(type = LogEnum.LOOK_CARGO_DETAILS)
    public Object lookCargoDetails(HttpServletResponse response, Long cargoId) {
        try {
            if (LOG.isDebugEnabled()) LOG.debug("获取货源详情接口入参：cargoId={}", cargoId);
            if (cargoId == null) {
                if (LOG.isErrorEnabled())
                    LOG.error("货源详情参数校验：货源id【不为空】。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            Long userId = findUserId(request);
            /****
             * 德邦查询货源数据(开始)
             */
            com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUserResponse = driverUserHandlerService.getDriverUserInfo(userId);
            if(driverUserResponse.isSuccess()) {
                DriverUserInfoDTO driverUser = driverUserResponse.getData();
                Map<String,String> map = getDebangUserCode();
                String driverCity = map.get(driverUser.getCode());
                if(StringUtils.isEmpty(driverCity)) {
                    //限制司机查看货源详情数量
                    String key = "das:cargo:lookcargo:" + userId;
                    Object obj = redisService.getObj(key);
                    if (null == obj) {
                        HashSet<String> set = new HashSet<>();
                        set.add(String.valueOf(cargoId));
                        redisService.setStr(key, set, DateUtil.getTomorrowDate2(new Date(), 1));
                    } else {
                        HashSet<String> set = (HashSet<String>) obj;
                        int count = set.size();
                        if (findAuthState().intValue() != 3) {
                            if (count >= confineNoAuthBroNum) {
                                if (!set.contains(String.valueOf(cargoId))) {
                                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                                }
                            }
                            set.add(String.valueOf(cargoId));
                            redisService.setStr(key, set, DateUtil.getTomorrowDate2(new Date(), 1));
                        } else {
                            if (count >= confineAuthBroNum) {
                                if (!set.contains(String.valueOf(cargoId))) {
                                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                                }
                            }
                            set.add(String.valueOf(cargoId));
                            redisService.setStr(key, set, DateUtil.getTomorrowDate2(new Date(), 1));
                        }
                    }
                }else {
                    LOG.info("德邦用户账号查看货源详情:cargoId={}",cargoId);
                }
            }
            /****
             * 德邦查询货源数据(结束)
             */
            Response<CargoDetailDTO> resultData = cargoHandlerService.getCargoDetail(cargoId, userId);
            if (resultData.getCode() == CodeTable.EXCEPTION.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if (resultData.getCode() == CodeTable.NOT_EXISTS_CARGO.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20066);
            }
            if (resultData.isSuccess()) {
                CargoDetailDTO cargoDetailDTO = resultData.getData();
                CargoDetailBO cargoDetailBO = new CargoDetailBO();
                //货物ID
                cargoDetailBO.setCargoId(cargoDetailDTO.getId());
                //企业名称
                cargoDetailBO.setCompanyName(cargoDetailDTO.getCompanyName());
                if (cargoDetailDTO.getCompanyId() != null
                        && cargoDetailDTO.getCompanyId().intValue() > 1) {
                    cargoDetailBO.setCompanyAuthStatus((byte) 3);
                } else {
                    cargoDetailBO.setCompanyAuthStatus((byte) 0);
                }

                //货物名称
                cargoDetailBO.setCargoName(cargoDetailDTO.getCargoName());

                //装货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                String sAddress = SystemsUtil.buildAddress(cargoDetailDTO.getStartTown(), cargoDetailDTO.getStartProvince(), cargoDetailDTO.getStartCity(), cargoDetailDTO.getStartCounty());
                cargoDetailBO.setsAddress(sAddress);
                //卸货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                String eAddress = SystemsUtil.buildAddress(cargoDetailDTO.getEndTown(), cargoDetailDTO.getEndProvince(), cargoDetailDTO.getEndCity(), cargoDetailDTO.getEndCounty());
                cargoDetailBO.setSaddressInfoPCC(SystemsUtil.buildAddress(cargoDetailDTO.getStartProvince(), cargoDetailDTO.getStartCity(), cargoDetailDTO.getStartCounty()));
                cargoDetailBO.setSaddressInfoDetail(cargoDetailDTO.getStartTown());
                cargoDetailBO.setEaddressInfoPCC(SystemsUtil.buildAddress(cargoDetailDTO.getEndProvince(), cargoDetailDTO.getEndCity(), cargoDetailDTO.getEndCounty()));
                cargoDetailBO.setEaddressInfoDetail(cargoDetailDTO.getEndTown());
                cargoDetailBO.seteAddress(eAddress);
                //判断货源状态  0 待交易 1 交易中 2 交易成功
                if(cargoDetailDTO.getCargoFlag()==(byte)0){
                    String dateStr = DateUtil.dateFormat(new Date(), DateUtil.F_DATE);
                    if(!DateUtil.parseDate(dateStr, DateUtil.F_DATE).after(cargoDetailDTO.getRequestStartTime())){
                        cargoDetailBO.setCargoStateReal("1");
                    }else{
                        cargoDetailBO.setCargoStateReal("2");
                    }
                }else{
                    cargoDetailBO.setCargoStateReal("3");
                }

                WebUserInfoDTO webUserInfoDTO = webUserHandleService.getWebUserInfo(cargoDetailDTO.getDeployUserid());
                if(webUserInfoDTO!=null){
                    cargoDetailBO.setOwnerHeadPhone(webUserInfoDTO.getHeadPortraitImgPath());
                }

                //装货时间(yyyy-mm-dd hh:mm)
                //要求装货时间(yyyy-mm-dd )
                cargoDetailBO.setsTime(SystemsUtil.cargoOrderStartTime(cargoDetailDTO.getRequestStartTime()));
                //要求卸货时间(yyyy-mm-dd )
                cargoDetailBO.seteTime(SystemsUtil.cargoOrderEndTime(cargoDetailDTO.getRequestEndTime()));
                cargoDetailBO.setWeight(
                        SystemsUtil.buildWeightUnit(cargoDetailDTO.getCargoWeight() == null ? BigDecimal.ZERO : cargoDetailDTO.getCargoWeight()));//重量(单位：吨)
                cargoDetailBO.setVolume(
                        SystemsUtil.buildVolumeUnit(cargoDetailDTO.getCargoCubage()));//体积(单位：方)
                cargoDetailBO.setCarLength(
                        SystemsUtil.buildCarLenUnit(cargoDetailDTO.getRequestCarLen()));//要求的车长(单位：米)
                //车辆类型名称
                cargoDetailBO.setVehicleTypeName(systemData.findVehicleTypeName(cargoDetailDTO.getVehicleType()));

                //车厢类型名称
                cargoDetailBO.setCarriageTypeName(systemData.findCarriageTypeName(cargoDetailDTO.getCarriageType()));

                cargoDetailBO.setCargoRemark(cargoDetailDTO.getRemark());

                //发布时间(yyyy-mm-dd hh:mm)
                cargoDetailBO.setPubTime(DateUtil.dateFormat(cargoDetailDTO.getCreateTime(), DateUtil.F_DATETOMIN));

                //点评是否显示(0否、1是)
                cargoDetailBO.setIsShowCommentType(cargoDetailDTO.getIsExistComment());
                if (!(cargoDetailDTO.getIsExistComment() == null || "".equals(cargoDetailDTO.getIsExistComment()) || "0".equals(cargoDetailDTO.getIsExistComment()))) {
                    cargoDetailBO.setCommentDriverMobile(ValidateUtil.hideMobile(cargoDetailDTO.getCommentDriverMobile()));
                    cargoDetailBO.setCommentDriverStatus(cargoDetailDTO.getCommentDriverStatus());
                    cargoDetailBO.setCommentType(cargoDetailDTO.getCommentType());
                    cargoDetailBO.setCommentRemark(cargoDetailDTO.getCommentRemark());
                    cargoDetailBO.setCommentTime(cargoDetailDTO.getCommentTime());
                }
                cargoDetailBO.setCompanyId(cargoDetailDTO.getCompanyId());
                cargoDetailBO.setDeployUserId(cargoDetailDTO.getDeployUserid());
                //发货人姓名(字数小于等于3位的取第一个字拼接"先生"，否者取前2个字拼接"先生")
                cargoDetailBO.setConsignorName(StrUtil.callJoin(cargoDetailDTO.getContactName()));

                //货主联系方式
                if (StringUtils.isNotBlank(cargoDetailDTO.getContactMobilephone())) {
                    cargoDetailBO.setConsignorMobile(cargoDetailDTO.getContactMobilephone());
                } else {
                    cargoDetailBO.setConsignorMobile(cargoDetailDTO.getContactTelephone());
                }

                //报价是否显示(0否、1是)
                cargoDetailBO.setIsShowQuote(cargoDetailDTO.getIsShowQuote());
                //我的报价(带单位：元/车、元/吨、元/方)
                if (!(cargoDetailDTO.getIsShowQuote() == null || "".equals(cargoDetailDTO.getIsShowQuote()) || "0".equals(cargoDetailDTO.getIsShowQuote()))) {
                    String quoteUnit = SystemsUtil.quoteUnitConver(cargoDetailDTO.getQuoteUnit());
                    String myQuote = cargoDetailDTO.getMyQuote() == null || "".equals(cargoDetailDTO.getMyQuote()) ? "" : (cargoDetailDTO.getMyQuote() + quoteUnit);//我的报价(带单位：元/车、元/吨、元/方)
                    cargoDetailBO.setMyQuote(myQuote);
                    cargoDetailBO.setQuoteTime(DateUtil.dateFormat(cargoDetailDTO.getQuoteTime(), DateUtil.F_DATETOMIN));
                }
                //是否允许报价(待交易待承运 可报价1)
                if (cargoDetailDTO.getCargoFlag() != null && cargoDetailDTO.getCargoFlag().intValue() == 0 || cargoDetailDTO.getCargoFlag().intValue() == 3) {
                    cargoDetailBO.setAllowQuote((byte) 1);
                } else {
                    cargoDetailBO.setAllowQuote((byte) 0);
                }
                //运费总价
                cargoDetailBO.setTotalFare(SystemsUtil.getTotalFare(cargoDetailDTO.getTotalFare()));
                //预付运费
                cargoDetailBO.setPrepayFare(SystemsUtil.getTotalFare(cargoDetailDTO.getPrepayFare()));
                //油卡、现金支付
                cargoDetailBO.setOilCard(SystemsUtil.getFare(cargoDetailDTO.getOilFare()));
                cargoDetailBO.setCash(SystemsUtil.getFare(cargoDetailDTO.getCashFare()));
                cargoDetailBO.setOilCardId(cargoDetailDTO.getOilChildCode());
                //限制次数
                Byte driverState = findAuthState();
                if (cargoDetailDTO.getId() != null) {
                    cargoDetailBO.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(findUserIdStr(request), cargoDetailDTO.getId().toString(), driverState));
                } else {
                    cargoDetailBO.setNetworkTelephoneState("0");
                }
                if (Constants.OS_WXIN.equals(findSource(request))) {
                    /** 微信所需字段 */
                    com.cy.pass.service.dto.base.Response<WebUserInfoDTO> webUserResult = webUserHandleService.getWebUserByCompanyId(cargoDetailDTO.getCompanyId());
                    if (webUserResult.getData() != null) {
                        CommentCountDTO commentCountDTO = assessService.countByOwner(webUserResult.getData().getId());
                        if (commentCountDTO != null) {
                            cargoDetailBO.setGood(commentCountDTO.getGood());
                            cargoDetailBO.setMiddle(commentCountDTO.getMiddle());
                            cargoDetailBO.setBad(commentCountDTO.getBad());
                        }
                    }
                    com.cy.pass.service.dto.base.Response<String> response2 = companyInfoService.finImgUrl(cargoDetailDTO.getCompanyId(), EnumCompanyImgType.BUSINESS_LICENCE.getCode());
                    if (response2.isSuccess() && response2.getData() != null) {
                        cargoDetailBO.setBusinessLicense(response2.getData());
                    }
                }
                //累计交易数
                OwnerItemStatDTO ownerItemStatDTO = ownerItemService.getByOwnerId(cargoDetailDTO.getDeployUserid());
                if (ownerItemStatDTO != null) {
                    cargoDetailBO.setTransactionNumber(ownerItemStatDTO.getFinishOrderNum());
                }
                //返回货源详情前埋点记录司机选择货源的起始地和目的地
                com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(userId, cargoDetailDTO, Constants.CARGO_DETAIL);
                if (!saveLineRes.isSuccess()) {
                    LOG.debug("locationLineService服务调用失败");
                }
                //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
                String requestTime = DateUtil.dateToStr(cargoDetailDTO.getRequestStartTime());
                String today = DateUtil.dateToStr(new Date());
                if (requestTime.equals(today)) {
                    com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(userId, cargoDetailDTO, Constants.CARGO_DETAIL);
                    if (!saveDayLineRes.isSuccess()) {
                        LOG.debug("driverDayLineService服务调用失败");
                    }
                }

                //3.5保存司机路线(查看货源详情)(新)
                ViewCargoLineSaveDTO viewCargoLineSaveDTO = new ViewCargoLineSaveDTO();
                viewCargoLineSaveDTO.setDriverId(userId);
                viewCargoLineSaveDTO.setCargoId(cargoId);
                viewCargoLineSaveDTO.setViewType(1);//查看货源详情时埋点类型
                viewCargoLineSaveDTO.setCargoSource(1);//来源快到网
                viewCargoLineSaveDTO.setStartTime(cargoDetailDTO.getRequestStartTime());
                viewCargoLineSaveDTO.setStartProvince(cargoDetailDTO.getStartProvince());
                viewCargoLineSaveDTO.setStartCity(cargoDetailDTO.getStartCity());
                viewCargoLineSaveDTO.setEndProvince(cargoDetailDTO.getEndProvince());
                viewCargoLineSaveDTO.setEndCity(cargoDetailDTO.getEndCity());
                driverLinePointService.saveViewCargoLine(viewCargoLineSaveDTO);

                updRespHeadSuccess(response);
                return cargoDetailBO;
            }

        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("获取货源详情出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 附近货源转换
     */
    private List<CargoListBO> convertCargoList(List<CargoDTO> list, Long driverId, boolean isAccurateCargo) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Long> cargoIds = new ArrayList<Long>();
        for (CargoDTO cargoDTO : list) {
            if (cargoDTO.getId() != null) {
                cargoIds.add(cargoDTO.getId());
            }
        }
        List<QuoteInfoDTO> quoteList = myQuoteInfoHandleService.queryLastQuote(cargoIds, driverId);
        Map<String, QuoteInfoDTO> quoteMap = new HashMap<String, QuoteInfoDTO>();
        if (quoteList != null && quoteList.size() > 0) {
            for (QuoteInfoDTO item : quoteList) {
                quoteMap.put(item.getCargoId() + "-" + driverId, item);
            }
        }
        List<CargoListBO> result = new ArrayList<CargoListBO>();
        CargoListBO cargoBo = null;
        for (CargoDTO cargoDTO : list) {
            cargoBo = new CargoListBO();
            cargoBo.setCargoId(cargoDTO.getId());//货物ID
            cargoBo.setCargoIdStr(String.valueOf(cargoDTO.getId()));
            cargoBo.setCompanyName(cargoDTO.getCompanyName());
            if (cargoDTO.getCompanySubmitType() != null) {
                cargoBo.setCompanyAuthStatus(cargoDTO.getCompanySubmitType());
            } else {
                cargoBo.setCompanyAuthStatus((byte) 0);
            }
            cargoBo.setStartAddress(StrUtil.strJoint(cargoDTO.getStartProvince(), cargoDTO.getStartCity(), cargoDTO.getStartCounty()));
            cargoBo.setEndAddress(StrUtil.strJoint(cargoDTO.getEndProvince(), cargoDTO.getEndCity(), cargoDTO.getEndCounty()));
            cargoBo.setCargoName(cargoDTO.getCargoName());
            //重量(单位：吨)
            cargoBo.setWeight(
                    SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight()));
            //体积(单位：方)
            cargoBo.setVolume(
                    SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));
            //要求的车长(单位：米)
            cargoBo.setCarLength(
                    SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));

            //车辆类型名称
            cargoBo.setVehicleTypeName(systemData.findVehicleTypeName(cargoDTO.getVehicleType()));

            //车厢类型名称
            cargoBo.setCarriageTypeName(systemData.findCarriageTypeName(cargoDTO.getCarriageType()));

            QuoteInfoDTO quoteDTO = quoteMap.get(cargoDTO.getId() + "-" + driverId);
            String myQuote = "";
            if (quoteDTO != null) {
                //我的报价(带单位：元/车、元/吨、元/方)
                String quoteUnit = SystemsUtil.quoteUnitConver(quoteDTO.getQuoteType());
                if (quoteDTO.getQuoteFair() != null) {
                    myQuote = quoteDTO.getQuoteFair() + quoteUnit;
                }
            }
            cargoBo.setMyQuote(myQuote);
            //装货时间(yyyy-mm-dd)
            cargoBo.setStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));
            //卸货时间(yyyy-mm-dd)
            cargoBo.setEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));
            //发布时间(例如：几小时前)
            cargoBo.setPubTime(SystemsUtil.getTimeStr(cargoDTO.getCreateTime()));
            //运费总价
            cargoBo.setTotalFare(SystemsUtil.getTotalFare(cargoDTO.getTotalFare()));
            //预付运费
            cargoBo.setPrepayFare(SystemsUtil.getTotalFare(cargoDTO.getPrepayFare()));

            //如果是精准货源则不执行
            if (!isAccurateCargo) {
                //货主联系方式
                if (StringUtils.isNotBlank(cargoDTO.getContactMobilephone())) {
                    cargoBo.setOwnerPhone(cargoDTO.getContactMobilephone());
                } else {
                    cargoBo.setOwnerPhone(cargoDTO.getContactTelephone());
                }
                //限制次数
                Byte driverState = findAuthState();
                if (cargoDTO.getId() != null) {
                    cargoBo.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(findUserIdStr(request), cargoDTO.getId().toString(), driverState));
                } else {
                    cargoBo.setNetworkTelephoneState("0");
                }
            }
            result.add(cargoBo);
        }
        return result;
    }


    /**
     * 附近货源转换
     */
    private List<CargoListBO> convertCargo2List(List<Cargo2DTO> list, Long driverId, boolean isAccurateCargo) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Long> cargoIds = new ArrayList<Long>();
        for (Cargo2DTO cargo2DTO : list) {
            if (cargo2DTO.getCargoId() != null) {
                cargoIds.add(cargo2DTO.getCargoId());
            }
        }
        List<QuoteInfoDTO> quoteList = myQuoteInfoHandleService.queryLastQuote(cargoIds, driverId);
        Map<String, QuoteInfoDTO> quoteMap = new HashMap<String, QuoteInfoDTO>();
        if (quoteList != null && quoteList.size() > 0) {
            for (QuoteInfoDTO item : quoteList) {
                quoteMap.put(item.getCargoId() + "-" + driverId, item);
            }
        }
        List<CargoListBO> result = new ArrayList<CargoListBO>();
        CargoListBO cargoBo = null;
        for (Cargo2DTO cargo2DTO : list) {
            cargoBo = new CargoListBO();
            cargoBo.setCargoId(cargo2DTO.getCargoId());//货物ID
            cargoBo.setCargoIdStr(String.valueOf(cargo2DTO.getCargoId()));
            cargoBo.setCompanyName(cargo2DTO.getCompanyName());
            if (cargo2DTO.getCompanySubmitType() != null) {
                cargoBo.setCompanyAuthStatus(cargo2DTO.getCompanySubmitType());
            } else {
                cargoBo.setCompanyAuthStatus((byte) 0);
            }
            cargoBo.setStartAddress(StrUtil.strJoint(cargo2DTO.getStartProvince(), cargo2DTO.getStartCity(), cargo2DTO.getStartCounty()));
            cargoBo.setEndAddress(StrUtil.strJoint(cargo2DTO.getEndProvince(), cargo2DTO.getEndCity(), cargo2DTO.getEndCounty()));
            cargoBo.setCargoName(cargo2DTO.getCargoName());
            if(cargo2DTO.getCargoSource()!=null && cargo2DTO.getCargoSource()==1) {
                //重量(单位：吨)
                cargoBo.setWeight(
                        SystemsUtil.buildWeightUnit(cargo2DTO.getCargoWeight() == null ? BigDecimal.ZERO : cargo2DTO.getCargoWeight()));
            }else{
                cargoBo.setWeight(
                        SystemsUtil.buildWeightUnit(cargo2DTO.getCargoWeight() == null ? BigDecimal.ZERO : cargo2DTO.getCargoWeight()));
            }
            //体积(单位：方)
            cargoBo.setVolume(
                    SystemsUtil.buildVolumeUnit(cargo2DTO.getCargoCubage()));
            //要求的车长(单位：米)
            cargoBo.setCarLength(
                    SystemsUtil.buildCarLenUnit(cargo2DTO.getRequestCarLen()));

            //车辆类型名称
            cargoBo.setVehicleTypeName(systemData.findVehicleTypeName(cargo2DTO.getVehicleType()));

            //车厢类型名称
            cargoBo.setCarriageTypeName(systemData.findCarriageTypeName(cargo2DTO.getCarriageType()));

            QuoteInfoDTO quoteDTO = quoteMap.get(cargo2DTO.getCargoId() + "-" + driverId);
            String myQuote = "";
            if (quoteDTO != null) {
                //我的报价(带单位：元/车、元/吨、元/方)
                String quoteUnit = SystemsUtil.quoteUnitConver(quoteDTO.getQuoteType());
                if (quoteDTO.getQuoteFair() != null) {
                    myQuote = quoteDTO.getQuoteFair() + quoteUnit;
                }
            }
            cargoBo.setMyQuote(myQuote);
            //装货时间(yyyy-mm-dd)
            cargoBo.setStartTime(SystemsUtil.cargoOrderStartTime(cargo2DTO.getRequestStartTime()));
            //卸货时间(yyyy-mm-dd)
            cargoBo.setEndTime(SystemsUtil.cargoOrderEndTime(cargo2DTO.getRequestEndTime()));
            //发布时间(例如：几小时前)
            cargoBo.setPubTime(SystemsUtil.getTimeStr(cargo2DTO.getCreateTime()));
            //运费总价
            cargoBo.setTotalFare(SystemsUtil.getTotalFare(cargo2DTO.getTotalFare()));
            //预付运费
            cargoBo.setPrepayFare(SystemsUtil.getTotalFare(cargo2DTO.getPrepayFare()));

            //如果是精准货源则不执行
            if (!isAccurateCargo) {
                //货主联系方式
                if (StringUtils.isNotBlank(cargo2DTO.getContactMobilephone())) {
                    cargoBo.setOwnerPhone(cargo2DTO.getContactMobilephone());
                } else {
                    cargoBo.setOwnerPhone(cargo2DTO.getContactTelephone());
                }
                //限制次数
                Byte driverState = findAuthState();
                if (cargo2DTO.getCargoId() != null) {
                    cargoBo.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(findUserIdStr(request), cargo2DTO.getCargoId().toString(), driverState));
                } else {
                    cargoBo.setNetworkTelephoneState("0");
                }
            }
            result.add(cargoBo);
        }
        return result;
    }



    /**
     * 附近货源转换
     */
    private List<CargoListBO> convertCargoInfoDTOList(List<CargoInfoDTO> list, Long driverId, boolean isAccurateCargo) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Long> cargoIds = new ArrayList<Long>();
        for (CargoInfoDTO cargoInfoDTO : list) {
            if (cargoInfoDTO.getId() != null) {
                cargoIds.add(cargoInfoDTO.getId());
            }
        }
        List<QuoteInfoDTO> quoteList = myQuoteInfoHandleService.queryLastQuote(cargoIds, driverId);
        Map<String, QuoteInfoDTO> quoteMap = new HashMap<String, QuoteInfoDTO>();
        if (quoteList != null && quoteList.size() > 0) {
            for (QuoteInfoDTO item : quoteList) {
                quoteMap.put(item.getCargoId() + "-" + driverId, item);
            }
        }
        List<CargoListBO> result = new ArrayList<CargoListBO>();
        CargoListBO cargoBo = null;
        for (CargoInfoDTO cargoInfoDTO : list) {
            cargoBo = new CargoListBO();
            cargoBo.setCargoId(cargoInfoDTO.getId());//货物ID
            cargoBo.setCargoIdStr(String.valueOf(cargoInfoDTO.getId()));
            cargoBo.setCompanyName(cargoInfoDTO.getCompanyName());
            if (cargoInfoDTO.getCompanyId() != null && cargoInfoDTO.getCompanyId().longValue() > 1) {
                cargoBo.setCompanyAuthStatus((byte) 3);
            } else {
                cargoBo.setCompanyAuthStatus((byte) 0);
            }
            cargoBo.setStartAddress(StrUtil.strJoint(cargoInfoDTO.getStartProvince(), cargoInfoDTO.getStartCity(), cargoInfoDTO.getStartCounty()));
            cargoBo.setEndAddress(StrUtil.strJoint(cargoInfoDTO.getEndProvince(), cargoInfoDTO.getEndCity(), cargoInfoDTO.getEndCounty()));
            cargoBo.setCargoName(cargoInfoDTO.getCargoName());
            //重量(单位：吨)
            cargoBo.setWeight(
                    SystemsUtil.buildWeightUnit(cargoInfoDTO.getCargoWeight()));
            //体积(单位：方)
            cargoBo.setVolume(
                    SystemsUtil.buildVolumeUnit(cargoInfoDTO.getCargoCubage()));
            //要求的车长(单位：cargoInfoDTO
            cargoBo.setCarLength(
                    SystemsUtil.buildCarLenUnit(cargoInfoDTO.getRequestCarLen()));

            //车辆类型名称
            cargoBo.setVehicleTypeName(systemData.findVehicleTypeName(cargoInfoDTO.getVehicleType()));

            //车厢类型名称
            cargoBo.setCarriageTypeName(systemData.findCarriageTypeName(cargoInfoDTO.getCarriageType()));

            QuoteInfoDTO quoteDTO = quoteMap.get(cargoInfoDTO.getId() + "-" + driverId);
            String myQuote = "";
            if (quoteDTO != null) {
                //我的报价(带单位：元/车、元/吨、元/方)
                String quoteUnit = SystemsUtil.quoteUnitConver(quoteDTO.getQuoteType());
                if (quoteDTO.getQuoteFair() != null) {
                    myQuote = quoteDTO.getQuoteFair() + quoteUnit;
                }
            }
            cargoBo.setMyQuote(myQuote);
            //装货时间(yyyy-mm-dd)
            cargoBo.setStartTime(SystemsUtil.cargoOrderStartTime(cargoInfoDTO.getRequestStartTime()));
            //卸货时间(yyyy-mm-dd)
            cargoBo.setEndTime(SystemsUtil.cargoOrderEndTime(cargoInfoDTO.getRequestEndTime()));
            //发布时间(例如：几小时前)
            cargoBo.setPubTime(SystemsUtil.getTimeStr(cargoInfoDTO.getCreateTime()));
            //运费总价
            cargoBo.setTotalFare(SystemsUtil.getTotalFare(cargoInfoDTO.getTotalFare()));
            //预付运费
            cargoBo.setPrepayFare(SystemsUtil.getTotalFare(cargoInfoDTO.getPrepayFare()));

            //如果是精准货源则不执行
            if (!isAccurateCargo) {
                //货主联系方式
                if (StringUtils.isNotBlank(cargoInfoDTO.getContactMobilephone())) {
                    cargoBo.setOwnerPhone(cargoInfoDTO.getContactMobilephone());
                } else {
                    cargoBo.setOwnerPhone(cargoInfoDTO.getContactTelephone());
                }
                //限制次数
                Byte driverState = findAuthState();
                if (cargoInfoDTO.getId() != null) {
                    cargoBo.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(findUserIdStr(request), cargoInfoDTO.getId().toString(), driverState));
                } else {
                    cargoBo.setNetworkTelephoneState("0");
                }
            }
            result.add(cargoBo);
        }
        return result;
    }


    /**
     * 获取宁波德邦用户
     * @return
     */
    public Map<String ,String> getDebangUserCode(){
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isNotEmpty(debangDriverCode)) {
            String[] userCode = debangDriverCode.split(",");
            for(int i = 0;i<userCode.length;i++) {
                String userCodeS = userCode[i];
                if(StringUtils.isNotEmpty(userCodeS)) {
                    String[] cityCode = userCodeS.split("-");
                    if(cityCode.length == 2) {
                        map.put(cityCode[0].toString(),cityCode[1].toString());
                    }else if(cityCode.length == 1) {
                        map.put(cityCode[0].toString(),"");
                    }

                }
            }
        }
        return map;
    }


    @Value("${'newUser.redPackets.activityName'}")
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
