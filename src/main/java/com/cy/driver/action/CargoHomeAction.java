package com.cy.driver.action;

import com.cy.cargo.service.dto.MyQuoteInfoDTO;
import com.cy.cargo.service.dto.base.PageResult;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.HomeInfoBO;
import com.cy.driver.domain.HomeOtherCountsBO;
import com.cy.driver.domain.ImgDeployInfoBO;
import com.cy.driver.domain.QuoteInfoBO;
import com.cy.driver.service.HomeNumInfoService;
import com.cy.driver.service.QueryOrderService;
import com.cy.driver.service.SearchDriverHandlerService;
import com.cy.pass.service.dto.Enum.DriverImgType;
import com.cy.pass.service.dto.Enum.ImgDeployType;
import com.cy.pass.service.dto.Enum.ImgStandradType;
import com.cy.pass.service.dto.ImgDeployInfoDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import com.cy.search.service.dto.response.DriverImgDTO;
import com.cy.search.service.dto.response.DriverUserInfoDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 货源首页信息处理类
 * Created by Administrator on 2015/7/12.
 */
@Scope("prototype")
@RestController("cargoHomeAction")
public class CargoHomeAction extends BaseAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private HomeNumInfoService homeNumInfoService;
    @Resource
    private RedisService redisService;
    @Resource
    private SystemData systemData;
    @Resource
    private SearchDriverHandlerService searchDriverHandlerService;
    @Resource
    private QueryOrderService queryOrderService;

    /**
     * 点击首页页面的提交
     *
     * @param request
     * @param response
     * @param clickType
     * @return
     */
    @RequestMapping(value = "/clickIndexSubmit", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CLICK_INDEX_SUBMIT)
    @Log(type = LogEnum.CLICK_INDEX_SUBMIT)
    public Object clickIndexSubmit(HttpServletRequest request, HttpServletResponse response, String clickType) {
        try {
            //不合法
            if (StringUtils.isEmpty(clickType)) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            if("1".equals(clickType)){
                redisService.setStr(findUserIdStr(request), DateUtil.dateFormat(new Date(), DateUtil.F_DATETIME));
                updRespHeadSuccess(response);
                return null;
            }
             if("2".equals(clickType)){
                 redisService.setStr(findUserIdStr(request), DateUtil.dateFormat(new Date(), DateUtil.F_DATETIME));
                 updRespHeadSuccess(response);
                 return null;
            }
            if("3".equals(clickType)){
                redisService.setStr(findUserIdStr(request), DateUtil.dateFormat(new Date(), DateUtil.F_DATETIME));
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("点击首页页面的提交出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


    /**
     * 首页其它数量
     *
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "/queryIndexNums", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_INDEX_NUMS)
    public Object queryIndexNums(HttpServletRequest request, HttpServletResponse response) {
        try {
            Response<HomeOtherCountsBO> resultData = homeNumInfoService.queryIndexNums(findUserId(request));
            if(resultData.isSuccess()){
                updRespHeadSuccess(response);
                return resultData.getData();
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("获取首页其它数量出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 我的报价列表
     *
     * @param request
     * @param response
     * @param page
     * @return
     */
    @RequestMapping(value = "/queryMyQuoteList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_MY_QUOTE_LIST)
    @Log(type = LogEnum.QUERY_MY_QUOTE_LIST)
    public Object queryMyQuoteList(HttpServletRequest request, HttpServletResponse response, long page) {
        try {
            //不合法
            if (page <= 0) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            PageResult<MyQuoteInfoDTO> pageResult = homeNumInfoService.queryMyQuoteList(page, findUserIdStr(request));
            if(pageResult.isSuccess()){
                Map<String, Object> resultMap = new HashMap<String, Object>();
                List<QuoteInfoBO> quoteInfoBOList = new ArrayList<QuoteInfoBO>();
                resultMap.put("allNums", pageResult.getTotalRecord());
                resultMap.put("allPage", pageResult.getTotalPage());
                if(pageResult.getDataList() ==null || pageResult.getDataList().size()==0){
                    updRespHeadSuccess(response);
                    return resultMap;
                }
                for (MyQuoteInfoDTO myQuoteInfoDTO :pageResult.getDataList()){
                    QuoteInfoBO quoteInfoBO = new QuoteInfoBO();
                    quoteInfoBO.setCargoId(myQuoteInfoDTO.getId());
                    quoteInfoBO.setCompanyName(myQuoteInfoDTO.getCompanyName());
                    quoteInfoBO.setStartTime(SystemsUtil.cargoOrderStartTime(myQuoteInfoDTO.getRequestStartTime()));
                    quoteInfoBO.setStartAddress(StrUtil.strJoint(myQuoteInfoDTO.getStartProvince(), myQuoteInfoDTO.getStartCity(), myQuoteInfoDTO.getStartCounty()));
                    quoteInfoBO.setEndAddress(StrUtil.strJoint(myQuoteInfoDTO.getEndProvince(), myQuoteInfoDTO.getEndCity(), myQuoteInfoDTO.getEndCounty()));
                    quoteInfoBO.setCargoName(myQuoteInfoDTO.getCargoName());
                    //重量(单位：吨)
                    quoteInfoBO.setWeight(
                            SystemsUtil.buildWeightUnit(myQuoteInfoDTO.getCargoWeight()));
                    //体积(单位：方)
                    quoteInfoBO.setVolume(
                            SystemsUtil.buildVolumeUnit(myQuoteInfoDTO.getCargoCubage()));
                    //要求的车长(单位：米)
                    quoteInfoBO.setCarLength(
                            SystemsUtil.buildCarLenUnit(myQuoteInfoDTO.getRequestCarLen()));
                    //车辆类型名称
                    quoteInfoBO.setVehicleTypeName(systemData.findVehicleTypeName(myQuoteInfoDTO.getVehicleType()));
                    //车厢类型名称
                    quoteInfoBO.setCarriageTypeName(systemData.findCarriageTypeName(myQuoteInfoDTO.getCarriageType()));

                    //我的报价(带单位：元/车、元/吨、元/方)
                    String quoteUnit = SystemsUtil.quoteUnitConver(myQuoteInfoDTO.getQuoteType());
                    String myQuote = myQuoteInfoDTO.getQuoteFair() == null || "".equals(myQuoteInfoDTO.getQuoteFair()) ? "" : (myQuoteInfoDTO.getQuoteFair() + quoteUnit);//我的报价(带单位：元/车、元/吨、元/方)
                    quoteInfoBO.setMyQuote(myQuote);
                    quoteInfoBO.setQuoteTime(DateUtil.dateFormat(myQuoteInfoDTO.getQuoteTime(), DateUtil.F_DATETOMIN));
                    String statusCode="";//状态编号(-1货源已撤销、1未承运、2已承运)
                    String statusName="";//状态名称
                    if(myQuoteInfoDTO.getDeletedFlag()!=0) {
                        statusCode = "-1";
                        statusName = "货源已撤销";
                    }else if(myQuoteInfoDTO.getCargoFlag()==0||myQuoteInfoDTO.getCargoFlag()==3) {
                        statusCode = "1";
                        statusName = "未承运";
                    }else
                    {
                        statusCode ="2";
                        statusName ="已承运";
                    }
                    quoteInfoBO.setStatusCode(statusCode);
                    quoteInfoBO.setStatusName(statusName);
                    //运费总价
                    quoteInfoBO.setTotalFare(SystemsUtil.getTotalFare(myQuoteInfoDTO.getTotalFare()));
                    //预付运费
                    quoteInfoBO.setPrepayFare(SystemsUtil.getTotalFare(myQuoteInfoDTO.getPrepayFare()));


                    quoteInfoBOList.add(quoteInfoBO);
                }

                resultMap.put("quoteList", quoteInfoBOList);
                updRespHeadSuccess(response);
                return resultMap;
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("获取我的报价列表出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }
    /**
     * 首页信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryIndexInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_INDEX_INFO)
    @Log(type = LogEnum.QUERY_INDEX_INFO)
    public Object queryIndexInfo(HttpServletRequest request, HttpServletResponse response) {
        try {

            /** 搜索服务的试验点 王远航 修改时间：2016-03-11 15:30 */
            Long driverId = findUserId(request);
            HomeInfoBO homeInfoBO = buildHomeInfo(driverId);
            if (homeInfoBO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20095);
            } else {
                homeInfoBO.setOrderCounts(String.valueOf(queryOrderService.selectByOrderComplete(driverId)));
                updRespHeadSuccess(response);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cargoHomeinfo", homeInfoBO);
                return map;
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("首页信息出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 首页信息（3.3以上版本）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryIndexInfoNew", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_INDEX_INFO_NEW)
    @Log(type = LogEnum.QUERY_INDEX_INFO)
    public Object queryIndexInfoNew(HttpServletRequest request, HttpServletResponse response) {
        try {

            /** 搜索服务的试验点 王远航 修改时间：2016-03-11 15:30 */
            Long driverId = findUserId(request);
            HomeInfoBO homeInfoBO = buildHomeInfo(driverId);
            if (homeInfoBO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20095);
            } else {
                updRespHeadSuccess(response);
                return homeInfoBO;
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("首页信息出错 - " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 组装首页信息
     */
    private HomeInfoBO buildHomeInfo(Long driverId) {
        DriverUserInfoDTO driverDTO = searchDriverHandlerService.findDriverInfo(driverId);
        if (driverDTO == null) {
            return null;
        }
        HomeInfoBO homeInfoBO = new HomeInfoBO();
        if (driverDTO.getSubmitType() != null) {
            homeInfoBO.setAuthStatus(driverDTO.getSubmitType().toString());
        } else {
            homeInfoBO.setAuthStatus("0");
        }
        if (StringUtils.isNotBlank(driverDTO.getCarNumber())) {
            homeInfoBO.setCarCard(driverDTO.getCarNumber());
            homeInfoBO.setShowType("1");
        } else {
            homeInfoBO.setShowType("0");
        }
        if (driverDTO.getWorkStatus() != null) {
            homeInfoBO.setWorkStatus(driverDTO.getWorkStatus().toString());
        } else {
            homeInfoBO.setWorkStatus("0");
        }
        DriverImgDTO img = searchDriverHandlerService.findDriverImg(driverId, (int) DriverImgType.PERSON_PHOTO.getValue());
        if (img != null) {
            homeInfoBO.setPhotosAddress(img.getImgPath());
        }
        return homeInfoBO;
    }

    /**
     * 货源首页轮播图片信息
     *
     * @param request
     * @param response
     * @param imgStandrad
     * @return
     */
    @RequestMapping(value = "/queryCarouselPicture", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SEQ_CODE_CARGO_LOOP_PHOTO)
    @Log(type = LogEnum.QUERY_CAROUSEL_PICTURE)
    public Object queryCarouselPicture(HttpServletRequest request, HttpServletResponse response, Integer imgStandrad) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if(imgStandrad == null || StringUtils.isBlank(ImgStandradType.contain(imgStandrad, ImgStandradType.values()))){
            if (logger.isErrorEnabled()) logger.error("查询app货源首页轮播图片信息校验:开关数据为空或者值不正确.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        try{
            Response<List<ImgDeployInfoDTO>> serResponse = homeNumInfoService.listByUserAndStandrand(ImgDeployType.APP_HOME_LOOP_PHOTO.getValue(), imgStandrad.toString());
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (logger.isErrorEnabled()) logger.error("查询app货源首页轮播图片(服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            //返回数据整合
            List<ImgDeployInfoBO> resList = null;

            List<ImgDeployInfoDTO> dataList = serResponse.getData();

            if(dataList != null && !dataList.isEmpty()){
                resList = new ArrayList<ImgDeployInfoBO>();

                for(ImgDeployInfoDTO infoDTO : dataList){
                    ImgDeployInfoBO infoBO = new ImgDeployInfoBO();
                    infoBO.setPictureMd5(infoDTO.getImgFilemd5());
                    infoBO.setImgName(infoDTO.getImgName());
                    infoBO.setImgPath(infoDTO.getImgPath());
                    infoBO.setImgStandard(infoDTO.getImgStandard());
                    infoBO.setPictureOrder(infoDTO.getOrderNum());
                    infoBO.setUrl(infoDTO.getClickResponse());
                    infoBO.setBrowserType(infoDTO.getBrowserType()==null?"1":String.valueOf(infoDTO.getBrowserType()));//跳转内部浏览器
                    resList.add(infoBO);
                }
            }

            if(resList != null){
                resultMap.put("pictureNums",resList.size());
                resultMap.put("pictureList",resList);
            }

            updRespHeadSuccess(response);
            return resultMap;
        }catch (Exception e){
            if(logger.isErrorEnabled())logger.error("查询app货源首页轮播图片出现异常.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }



    /**
     * 广告弹屏图片信息
     *
     * @param request
     * @param response
     * @param imgStandrad
     * @return
     */
    @RequestMapping(value = "/queryAdvertisement", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SEQ_ADVERTISEMENT_LOOP_PHOTO)
    @Log(type = LogEnum.QUERY_ADVERTISEMENT)
    public Object queryAdvertisement(HttpServletRequest request, HttpServletResponse response, Integer imgStandrad) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if(imgStandrad == null || StringUtils.isBlank(ImgStandradType.contain(imgStandrad, ImgStandradType.values()))){
            if (logger.isErrorEnabled()) logger.error("查询app广告弹屏图片信息校验:开关数据为空或者值不正确.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        try{
            Response<List<ImgDeployInfoDTO>> serResponse = homeNumInfoService.listByUserAndStandrand((short)104, imgStandrad.toString());
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (logger.isErrorEnabled()) logger.error("查询app广告弹屏图片(服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            //返回数据整合
            List<ImgDeployInfoBO> resList = null;

            List<ImgDeployInfoDTO> dataList = serResponse.getData();

            if(dataList != null && !dataList.isEmpty()){
                resList = new ArrayList<ImgDeployInfoBO>();

                for(ImgDeployInfoDTO infoDTO : dataList){
                    ImgDeployInfoBO infoBO = new ImgDeployInfoBO();
                    infoBO.setPictureMd5(infoDTO.getImgFilemd5());
                    infoBO.setImgName(infoDTO.getImgName());
                    infoBO.setImgPath(infoDTO.getImgPath());
                    infoBO.setImgStandard(infoDTO.getImgStandard());
                    infoBO.setPictureOrder(infoDTO.getOrderNum());
                    infoBO.setUrl(infoDTO.getClickResponse());
                    infoBO.setBrowserType(String.valueOf(infoDTO.getBrowserType()));
                    resList.add(infoBO);
                }
            }

            if(resList != null){
                resultMap.put("pictureNums",resList.size());
                resultMap.put("pictureList",resList);
            }

            updRespHeadSuccess(response);
            return resultMap;
        }catch (Exception e){
            if(logger.isErrorEnabled())logger.error("查询app广告弹屏图片出现异常.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }
}
