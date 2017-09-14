package com.cy.driver.api;

import com.alibaba.fastjson.JSON;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.cargo.service.dto.base.Response;
import com.cy.core.redis.RedisData;
import com.cy.driver.action.BaseAction;
import com.cy.driver.api.convert.NearAndFindCargoConvert;

import com.cy.driver.api.domain.res.CargoDetail;
import com.cy.driver.cloudService.CloudCargoService;
import com.cy.driver.cloudService.CloudCommentService;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.redis.NetWordPhoneClient;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.CargoDetailBO;
import com.cy.driver.domain.FindCargo;
import com.cy.driver.service.*;
import com.cy.location.service.DriverLinePointService;
import com.cy.location.service.dto.LastLocationDTO;
import com.cy.location.service.dto.SearchCargoLineSaveDTO;
import com.cy.location.service.dto.ViewCargoLineSaveDTO;
import com.cy.pass.service.dto.CarDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.MessageRemindDTO;
import com.cy.pass.service.dto.OwnerItemStatDTO;
import com.cy.rdcservice.service.dto.*;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.Cargo2DTO;
import com.cy.search.service.dto.response.CargoQuoteDTO;
import com.cy.top56.common.PageInfo;
import org.apache.commons.codec.binary.Base64;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 找货附近货源
 *
 * @author yanst 2016/5/26 13:57
 */
@Scope("prototype")
@RestController
public class NearAndFindCargoController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(NearAndFindCargoController.class);

    private String debangDriverCode;

    @Resource
    private NetWordPhoneClient netWordPhoneClient;
    @Resource
    private MyQuoteInfoHandleService myQuoteInfoHandleService;
    @Resource
    private RedisData redisData;
    @Resource
    private TrackPointService trackPointService;
    @Resource
    private CloudCargoService cloudCargoService;
    @Resource
    private CloudUserService cloudUserService;
    @Resource
    private CloudCommentService cloudCommentService;
    @Resource
    private DriverLinePointService driverLinePointService;
    @Resource
    private LocationService locationService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private CargoHandlerService cargoHandlerService;
    @Resource
    private OwnerItemService ownerItemService;

    @Value("#{propertiesReader['debang.driver.code']}")
    public void setDebangDriverCode(String debangDriverCode) {
        this.debangDriverCode = debangDriverCode;
    }

    /**
     * @param page 页码
     * @param sort 排序
     *             附近货源
     * @return
     */
    @RequestMapping(value = "/cloudNearCargo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.NEAR_CARGO)
    @ResponseBody
    @Log(type = LogEnum.NEAR_CARGO)
    public Object nearCargo(HttpServletResponse response, Integer page, Integer sort) {
        try {
            Long driverId = findUserId();
            if (page == null || page.intValue() <= 0L)
                page = 1;
            if (sort == null)
                sort = 0;
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
            //获取货源列表
            PageResult<Cargo2DTO> pageResult = cloudCargoService.queryNearCargo3(driverId, page, sort,lastLoc,carLength,carWeight,carCubage,vehicleType,carriageType);
            if (pageResult == null || !pageResult.isSuccess()) {
                updRespHeadSuccess(response);
                return null;
            }
            if (pageResult.getDataList() == null || pageResult.getDataList().size() == 0) {
                updRespHeadSuccess(response);
                return null;
            }
            List<Cargo2DTO> cargoList = pageResult.getDataList();
            List<IdSourceDTO> cargoIds = new ArrayList<>();
            for (Cargo2DTO cargo2DTO : cargoList) {
                if (cargo2DTO.getCargoId() != null) {
                    IdSourceDTO idSourceDTO = new IdSourceDTO();
                    idSourceDTO.setId(cargo2DTO.getCargoId());
                    idSourceDTO.setSource(cargo2DTO.getCargoSource());
                    cargoIds.add(idSourceDTO);
                }
                if(StringUtils.isNotEmpty(cargo2DTO.getDeployUserid())) {
                    UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(Long.valueOf(cargo2DTO.getDeployUserid()));
                    if (userLoginInfoDTO != null) {
                        cargo2DTO.setContactMobilephone(userLoginInfoDTO.getMobilephone());
                    }
                }
            }
            //获取报价信息
            List<CargoQuoteDTO> quoteList = myQuoteInfoHandleService.list(cargoIds, driverId.toString(), Constants.USER_TYPE_KDWSJ);
            Map<String, CargoQuoteDTO> quoteMap = new HashMap<>();
            if (quoteList != null && quoteList.size() > 0) {
                for (CargoQuoteDTO item : quoteList) {
                    quoteMap.put(item.getCargoId() + "-" + driverId, item);
                }
            }
            //货源列表转换并返回
            updRespHeadSuccess(response);
            return NearAndFindCargoConvert.nearCargoConvert(pageResult, findUserId(), findAuthState(), false, netWordPhoneClient, quoteMap);
        } catch (Exception e) {
            LOG.error("获取找货信息出错{}", e);
        }
        return findException(response);
    }


    /**
     * 搜索货源
     *
     * @return
     */
    @RequestMapping(value = "/cloudFindCargo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_CARGO)
    @ResponseBody
    @Log(type = LogEnum.FIND_CARGO)
    public Object findCargo(HttpServletResponse response, FindCargo findCargo) {
        if (LOG.isDebugEnabled())
            LOG.debug("找货入参信息:{}", JSON.toJSONString(findCargo));
        Long driverId = findUserId(request);
        /**
         * 判断德邦的账号是否存在(开始)
         */
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverUserResponse = driverUserHandlerService.getDriverUserInfo(driverId);
        if(driverUserResponse.isSuccess()) {
            DriverUserInfoDTO driverUser = driverUserResponse.getData();
            Map<String,String> map = getDebangUserCode();
            String driverCity = map.get(driverUser.getCode());
            if(StringUtils.isNotEmpty(driverCity)) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30306);
            }
        }
        /**
         * 判断德邦的账号是否存在(结束)
         */
        //不合法
        if (findCargo == null || findCargo.getPage() <= 0) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
        }
        //时间不为空
        if (StringUtils.isEmpty(findCargo.getsTime())) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20078);
        }
        //出发省编号不为空
        if (StringUtils.isEmpty(findCargo.getsProCode())) {
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20069);
        }
        Map<String,Object> map = new HashMap<>();

        //3.5司机路线埋点(司机搜索货源)(新)
        SearchCargoLineSaveDTO searchCargoLineSaveDTO = new SearchCargoLineSaveDTO();
        searchCargoLineSaveDTO.setStartDate(DateUtil.parseDate(findCargo.getsTime(), DateUtil.F_DATE));
        searchCargoLineSaveDTO.setStartProvince(findCargo.getsProValue());
        searchCargoLineSaveDTO.setStartCity(findCargo.getsCityValue());
        searchCargoLineSaveDTO.setEndProvince(findCargo.geteProValue());
        searchCargoLineSaveDTO.setEndCity(findCargo.geteCityValue());
        searchCargoLineSaveDTO.setDriverId(driverId);
        driverLinePointService.saveSearchCargoLine(searchCargoLineSaveDTO);


        //3.5司机线路埋点(旧)
        trackPointService.saveLine(driverId, findCargo);
        try {
            if (findCargo.getPage() <= 0L)
                findCargo.setPage(1);
            PageResult<Cargo2DTO> pageResult = cloudCargoService.findCargo(findCargo, driverId);
            if (pageResult == null || pageResult.getDataList() == null || pageResult.getDataList().size() == 0) {
                updRespHeadSuccess(response);
                return map;
            }
            List<IdSourceDTO> idSourceDTOs = new ArrayList<>();
            for (Cargo2DTO cargoDTO : pageResult.getDataList()) {
                if (cargoDTO.getCargoId() != null) {
                    IdSourceDTO idSourceDTO = new IdSourceDTO();
                    idSourceDTO.setSource(cargoDTO.getCargoSource());
                    idSourceDTO.setId(cargoDTO.getCargoId());
                    idSourceDTOs.add(idSourceDTO);
                }
                if(StringUtils.isNotEmpty(cargoDTO.getDeployUserid())) {
                    UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(Long.valueOf(cargoDTO.getDeployUserid()));
                    if (userLoginInfoDTO != null) {
                        cargoDTO.setContactMobilephone(userLoginInfoDTO.getMobilephone());
                    }
                }
            }
            //获取报价信息
            List<CargoQuoteDTO> quoteList = myQuoteInfoHandleService.list(idSourceDTOs, driverId.toString(), Constants.USER_TYPE_KDWSJ);
            Map<String, CargoQuoteDTO> quoteMap = new HashMap<String, CargoQuoteDTO>();
            if (quoteList != null && quoteList.size() > 0) {
                for (CargoQuoteDTO item : quoteList) {
                    quoteMap.put(item.getCargoId() + "-" + driverId, item);
                }
            }
            updRespHeadSuccess(response);
            return NearAndFindCargoConvert.findCargoConvert(pageResult, findUserId(), findAuthState(), false, netWordPhoneClient, quoteMap);
        } catch (Exception e) {
            LOG.error("获取找货信息出错{}", e);
        }
        return findException(response);
    }


    /**
     * 货源详情(客户端调用接口前判断货物来源来决定调用的接口)
     *
     * @return
     */
    @RequestMapping(value = "/cloudCargoDetail")
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CARGO_DETAIL)
    @ResponseBody
    @Log(type = LogEnum.CARGO_DETAIL)
    public Object cargoDetail(Long cargoId, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) LOG.debug("获取货源详情接口入参：cargoId={}", cargoId);
        if (StringUtils.isEmpty(String.valueOf(cargoId))) {
            LOG.error("货源详情,cargoId为空!");
            return findErrorParam(response);
        }
        Long userId = findUserId();
        Byte authstate = findAuthState();
        //检查用户浏览的货源详情数量
        checkCount(Long.valueOf(cargoId), userId);

        CarrierAssemDTO carrierAssemDTO = cloudCargoService.cargoDetailsRdc(cargoId);
        if (carrierAssemDTO == null) {
            updRespHeadError(response);
            return null;
        }
        //将服务返回的CarrierAssemDTO对象信息放进CargoDetail对象
        CargoDetail cargoDetail = NearAndFindCargoConvert.carrierAssemDTOToCargoDetail(carrierAssemDTO);
        //查询用户头像信息
        UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(carrierAssemDTO.getInfoDTO().getCreateUserId());
        if(userLoginInfoDTO==null){
            if (LOG.isDebugEnabled()) LOG.debug("调用userService.getUser服务用户详情失败");
        }else{
            if(userLoginInfoDTO.getHeadImg()!=null) {
                cargoDetail.setPhotosAddress(userLoginInfoDTO.getHeadImg());
            }
        }
        //查询公司认证信息
        UserLoginInfoDTO companyInfo = cloudUserService.getOwnerInfo(carrierAssemDTO.getInfoDTO().getOwnUserId());
        if(companyInfo==null){
            if (LOG.isDebugEnabled()) LOG.debug("调用userService.getUser服务用户详情失败");
        }else{
            if(cargoDetail.getCompanyName()==null){
                cargoDetail.setCompanyName(companyInfo.getName());
            }
            cargoDetail.setCompanyAuthStatus(companyInfo.getSubmitType());
            if(companyInfo.getSubmitType()==3) {
                //公司已认证的时候，查询公司联系人信息
                AuthedInfoDTO authedInfoDTO = cloudUserService.getCompanyAuthedInfoByUserId(carrierAssemDTO.getInfoDTO().getOwnUserId());
                if (authedInfoDTO == null) {
                    LOG.debug("调用rdc服务userAuthService查询货主信息失败");
                } else {
                    cargoDetail.setCompanyMobile(authedInfoDTO.getContactWay());//发货公司联系人手机
                    cargoDetail.setCompanyContactName(authedInfoDTO.getContactPerson());//发货公司联系人姓名
                    cargoDetail.setCompanyId(String.valueOf(authedInfoDTO.getId()));//公司id
                    //发货人手机号码直接改成货主手机号码
                    cargoDetail.setConsignorMobilePhone(authedInfoDTO.getContactWay());//发货人手机
                }
            }else{
                //公司未认证的时候，查询公司联系人信息查询
                UserLoginInfoDTO companyContactInfo = cloudUserService.getOwnerInfo(companyInfo.getParentId()==0?companyInfo.getId():companyInfo.getParentId());
                if(companyContactInfo!=null){
                    cargoDetail.setCompanyMobile(companyContactInfo.getMobilephone());//发货公司联系人手机
                    cargoDetail.setCompanyContactName(companyContactInfo.getName());//发货公司联系人姓名
                    //发货人手机号码直接改成货主手机号码
                    cargoDetail.setConsignorMobilePhone(companyContactInfo.getMobilephone());//发货人手机
                }
            }
        }

        //查询货主业务信息
        UserItemStatDTO userItemStatDTO = cloudUserService.getItemInfo(Long.valueOf(cargoDetail.getOwnerUserId()));
        if(userItemStatDTO==null){
            LOG.debug("调用rdc服务userItemStatService查询货主信息失败");
        }else {
            cargoDetail.setTransactionNumber(userItemStatDTO.getFinishOrderNum());//订单完成数
        }
        //查询货源点评信息
        PageInfo<Long> pageInfo = new PageInfo<>();
        pageInfo.setPageIndex(1);
        pageInfo.setData(cargoId);
        com.cy.top56.common.PageResult<CarrierAssessDTO> pageResult= cloudCommentService.queryCommentByPage(pageInfo);
        if(pageResult.isSuccess()) {
            if (pageResult.getDataList().size() == 0) {
                cargoDetail.setHasComment("0");
            } else {
                cargoDetail.setHasComment("1");
                cargoDetail.setCommentMobilePhone(pageResult.getDataList().get(0).getUserPhone());
                cargoDetail.setCommentAuthState(String.valueOf(pageResult.getDataList().get(0).getUserAuthState()));
                cargoDetail.setCargoState(String.valueOf(pageResult.getDataList().get(0).getType()));
                cargoDetail.setCommentTime(DateUtil.dateFormat(pageResult.getDataList().get(0).getCreateTime(), DateUtil.F_DATETIME));
                cargoDetail.setCommentContent(pageResult.getDataList().get(0).getAssessInfo());
            }
        }else {
            cargoDetail.setHasComment("0");
        }
        //是否可以拨打电话
        if (!"3".equals(authstate)) {//未认证用户
            netWordPhoneClient.putNumberLimit(String.valueOf(userId), String.valueOf(cargoId));
            cargoDetail.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(String.valueOf(userId), String.valueOf(cargoId), new Byte("0")));
        } else {
            cargoDetail.setNetworkTelephoneState("0");
        }

        //返回货源详情前埋点记录司机选择货源的起始地和目的地(旧)
        trackPointService.saveLine(userId, carrierAssemDTO, Constants.CARGO_DETAIL);


        //3.5司机路线埋点(司机查看货源详情)(新)
        ViewCargoLineSaveDTO viewCargoLineSaveDTO = new ViewCargoLineSaveDTO();
        viewCargoLineSaveDTO.setDriverId(userId);
        viewCargoLineSaveDTO.setCargoId(cargoId);
        viewCargoLineSaveDTO.setViewType(1);//查看货源详情时埋点类型
        viewCargoLineSaveDTO.setCargoSource(2);//来源云配
        viewCargoLineSaveDTO.setStartTime(carrierAssemDTO.getInfoDTO().getNeedStartTime());
        viewCargoLineSaveDTO.setStartProvince(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue());
        viewCargoLineSaveDTO.setStartCity(carrierAssemDTO.getAddressDTO().getDepartureCityValue());
        viewCargoLineSaveDTO.setEndProvince(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue());
        viewCargoLineSaveDTO.setEndCity(carrierAssemDTO.getAddressDTO().getReceiveCityValue());
        driverLinePointService.saveViewCargoLine(viewCargoLineSaveDTO);
        updRespHeadSuccess(response);
        return cargoDetail;
    }


    /**
     * 缓存记录用户点开货源详情次数，认证过的用户超过100直接返回，未认证的用户50
     */
    public Object checkCount(Long cargoId, Long userId) {
        String key = "das:cargo:lookcargo:" + userId;
        Object obj = redisData.get(key);
        if (null == obj) {
            HashSet<String> set = new HashSet<>();
            set.add(String.valueOf(cargoId));
            redisData.put(key, set, DateUtil.getTomorrowDate2(new Date(), 1));
            return null;
        } else {
            HashSet<String> set = (HashSet<String>) obj;
            int count = set.size();
            if (findAuthState().intValue() != 3) {
                if (count >= 50) {
                    if (!set.contains(String.valueOf(cargoId))) {
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                    }
                }
                set.add(String.valueOf(cargoId));
                redisData.put(key, set, DateUtil.getTomorrowDate2(new Date(), 1));
                return null;
            } else {
                if (count >= 100) {
                    if (!set.contains(String.valueOf(cargoId))) {
                        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20303);
                    }
                }
                set.add(String.valueOf(cargoId));
                redisData.put(key, set, DateUtil.getTomorrowDate2(new Date(), 1));
                return null;
            }
        }
    }

    /**
     * 短信链接跳转到具体货源详情
     * @param key
     * @return
     */
    @RequestMapping(value = "/messageToBusinessDetail")
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MESSAGE_TO_BUSINESSDETAIL)
    @Log(type = LogEnum.MESSAGE_TO_BUSINESSDETAIL)
    public ModelAndView messageToBusinessDetail(String key){
        if (LOG.isDebugEnabled()) LOG.debug("短信转向业务详情接口参数:key={}", key);
        if (StringUtils.isEmpty(String.valueOf(key))) {
            LOG.error("业务详情查询失败,key为空!");
            return null;
        }
        ModelAndView modelAndView = new ModelAndView();
        byte[] keybyte = Base64.decodeBase64(key);
        String keyStr = new String(keybyte);
        MessageRemindDTO messageRemindDTO = driverUserHandlerService.getBusinessInfo(Long.valueOf(keyStr));
        if(messageRemindDTO==null || messageRemindDTO.getBusinessId()==null){
            return null;
        }
        if(messageRemindDTO.getBusinessType()==2) {
            if (messageRemindDTO.getBusinessSource() == 1) {
                Response<CargoInfoDTO> resultData = cargoHandlerService.getCargoInfo(messageRemindDTO.getBusinessId());
                if(resultData==null || resultData.getData()==null){
                    return null;
                }
                CargoDetailBO cargoDetailBO = NearAndFindCargoConvert.cargoInfoDTOConvert(resultData.getData());
                //累计交易数
                OwnerItemStatDTO ownerItemStatDTO = ownerItemService.getByOwnerId(resultData.getData().getDeployUserid());
                if (ownerItemStatDTO != null) {
                    cargoDetailBO.setTransactionNumber(ownerItemStatDTO.getFinishOrderNum());
                }
                //判断货源状态 1.待承运 2.已过期 3.已被承运
                if(resultData.getData().getCargoFlag()==(byte)0){
                    String dateStr = DateUtil.dateFormat(new Date(), DateUtil.F_DATE);
                    if(!DateUtil.parseDate(dateStr, DateUtil.F_DATE).after(resultData.getData().getRequestStartTime())){
                        cargoDetailBO.setCargoStateReal("1");
                    }else{
                        cargoDetailBO.setCargoStateReal("2");
                    }
                }else{
                    cargoDetailBO.setCargoStateReal("3");
                }
                cargoDetailBO.setCargoSource("1");
                modelAndView.addObject("cargoDetailBO",cargoDetailBO);
                modelAndView.addObject("key",key);
                modelAndView.setViewName("kdCargoDetail");
            } else {
                CarrierAssemDTO carrierAssemDTO = cloudCargoService.cargoDetailsRdc(messageRemindDTO.getBusinessId());
                if (carrierAssemDTO == null) {
                    updRespHeadError(response);
                    return null;
                }
                //将服务返回的CarrierAssemDTO对象信息放进CargoDetail对象
                CargoDetail cargoDetail = NearAndFindCargoConvert.carrierAssemDTOToCargoDetail(carrierAssemDTO);
                //查询用户头像信息
                UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(carrierAssemDTO.getInfoDTO().getCreateUserId());
                if(userLoginInfoDTO==null){
                    if (LOG.isDebugEnabled()) LOG.debug("调用userService.getUser服务用户详情失败");
                }else{
                    if(userLoginInfoDTO.getHeadImg()!=null) {
                        cargoDetail.setPhotosAddress(userLoginInfoDTO.getHeadImg());
                    }
                }
                //查询货主认证信息
                UserLoginInfoDTO ownerUserInfo = cloudUserService.getOwnerInfo(carrierAssemDTO.getInfoDTO().getOwnUserId());
                if(ownerUserInfo==null){
                    if (LOG.isDebugEnabled()) LOG.debug("调用userService.getUser服务用户详情失败");
                }else{
                    if(cargoDetail.getCompanyName()==null){
                        cargoDetail.setCompanyName(ownerUserInfo.getName());
                    }
                    cargoDetail.setCompanyAuthStatus(ownerUserInfo.getSubmitType());
                    if(ownerUserInfo.getSubmitType()==3) {
                        //公司已认证的时候，查询公司联系人信息
                        AuthedInfoDTO authedInfoDTO = cloudUserService.getCompanyAuthedInfo(Long.valueOf(cargoDetail.getCompanyId()));
                        if (authedInfoDTO == null) {
                            LOG.debug("调用rdc服务userAuthService查询货主信息失败");
                        } else {
                            cargoDetail.setCompanyMobile(authedInfoDTO.getContactWay());//发货公司联系人手机
                            cargoDetail.setCompanyContactName(authedInfoDTO.getContactPerson());//发货公司联系人姓名
                        }
                    }else{
                        //公司未认证的时候，查询公司联系人信息查询
                        UserLoginInfoDTO companyContactInfo = cloudUserService.getOwnerInfo(ownerUserInfo.getParentId()==0?ownerUserInfo.getId():ownerUserInfo.getParentId());
                        if(companyContactInfo!=null){
                            cargoDetail.setCompanyMobile(companyContactInfo.getMobilephone());//发货公司联系人手机
                            cargoDetail.setCompanyContactName(companyContactInfo.getName());//发货公司联系人姓名
                        }
                    }
                }
                //查询货主业务信息
                UserItemStatDTO userItemStatDTO = cloudUserService.getItemInfo(Long.valueOf(cargoDetail.getOwnerUserId()));
                if(userItemStatDTO==null){
                    LOG.debug("调用rdc服务userItemStatService查询货主信息失败");
                }else {
                    cargoDetail.setTransactionNumber(userItemStatDTO.getFinishOrderNum());//订单完成数
                }
                cargoDetail.setCargoSource("2");
                modelAndView.addObject("cargoDetail",cargoDetail);
                modelAndView.addObject("key",key);
                modelAndView.setViewName("ypCargoDetail");
            }
        }
        return modelAndView;
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
}
