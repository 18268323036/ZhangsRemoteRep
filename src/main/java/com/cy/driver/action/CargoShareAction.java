package com.cy.driver.action;


import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.CargoDetailBO;
import com.cy.driver.service.CargoHandlerService;
import com.cy.driver.service.WebUserHandleService;
import com.cy.pass.service.dto.WebUserInfoDTO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2015/8/11.
 */
@Controller("cargoShareAction")
@Scope("prototype")
@RequestMapping(value = "/share")
public class CargoShareAction extends BaseAction{

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CargoHandlerService cargoHandlerService;

    @Resource
    private WebUserHandleService webUserHandleService;

    @Resource
    private SystemData systemData;

    /**
     * 构建视图
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/caogoshare",method = RequestMethod.GET)
    public ModelAndView caogoShare(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mdv = new ModelAndView();
        mdv.setViewName("cargoShare");
        String cargoId = request.getQueryString();
        if(StringUtils.isNotBlank(cargoId)){
            try{
                cargoId = new String(Base64.decodeBase64(cargoId.getBytes("UTF-8")));
                if(LOG.isDebugEnabled()){
                    LOG.debug("获得解码后货源ID：cargoId="+cargoId);
                }
                Long cargoIdLong = Long.parseLong(cargoId);
                if (cargoId == null) {
                    if (LOG.isErrorEnabled())
                        LOG.error("货源详情参数校验：货源id【不为空】。");
                    mdv.addObject("code", "1");
                    mdv.addObject("value","货源详情参数校验：货源id【不为空】。");
                    return mdv;
                }
                Response<CargoDetailDTO> resultData = cargoHandlerService.getCargoDetail(cargoIdLong, 0);
                if (resultData.getCode() == CodeTable.EXCEPTION.getCode()) {
                    mdv.addObject("code", "1");
                    mdv.addObject("value","系统出错。");
                    return mdv;
                }
                if (resultData.isSuccess()) {
                    CargoDetailDTO cargoDetailDTO = resultData.getData();
                    CargoDetailBO cargoDetailBO = new CargoDetailBO();
                    //货物ID
                    cargoDetailBO.setCargoId(cargoDetailDTO.getId());
                    //企业名称
                    cargoDetailBO.setCompanyName(cargoDetailDTO.getCompanyName());

                    com.cy.pass.service.dto.base.Response<WebUserInfoDTO> webUserResult = webUserHandleService.getWebUserByCompanyId(cargoDetailDTO.getCompanyId());
                    if(webUserResult.getData() != null){
                        cargoDetailBO.setCompanyAuthStatus(webUserResult.getData().getSubmitType());
                    }else{
                        cargoDetailBO.setCompanyAuthStatus((byte)0);
                    }

                    //货物名称
                    cargoDetailBO.setCargoName(cargoDetailDTO.getCargoName());

                    //装货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                    String sAddress = SystemsUtil.buildAddress(cargoDetailDTO.getStartTown(), cargoDetailDTO.getStartProvince(), cargoDetailDTO.getStartCity(), cargoDetailDTO.getStartCounty());
                    cargoDetailBO.setsAddress(sAddress);
                    //卸货地 (详情不为空取详情，为空则取省市县拼装，注意：直辖市省市去重)
                    String eAddress = SystemsUtil.buildAddress(cargoDetailDTO.getEndTown(), cargoDetailDTO.getEndProvince(), cargoDetailDTO.getEndCity(), cargoDetailDTO.getEndCounty());
                    cargoDetailBO.seteAddress(eAddress);
                    /**
                     * 2016-3-15 yanst  把要求装货时间、要求卸货时间 的格式由yyyy-mm-dd hh:mm 改为 yyyy-mm-dd ,朱芳芳提出，黄燕跟胡总确认过
                     */
                    //要求装货时间(yyyy-mm-dd hh:mm)
                    cargoDetailBO.setsTime(cargoDetailDTO.getRequestStartTime() == null ? "" : DateUtil.dateFormat(cargoDetailDTO.getRequestStartTime(), DateUtil.F_DATE));
                    //要求卸货时间(yyyy-mm-dd hh:mm)
                    cargoDetailBO.seteTime(cargoDetailDTO.getRequestEndTime() == null ? "" : DateUtil.dateFormat(cargoDetailDTO.getRequestEndTime(), DateUtil.F_DATE));
                    cargoDetailBO.setWeight(
                            SystemsUtil.buildWeightUnit(cargoDetailDTO.getCargoWeight()));//重量(单位：吨)
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
                    //发货人手机号码
                    cargoDetailBO.setConsignorMobile(cargoDetailDTO.getContactMobilephone());
                    //报价是否显示(0否、1是)
                    cargoDetailBO.setIsShowQuote(cargoDetailDTO.getIsShowQuote());
                    //我的报价(带单位：元/车、元/吨、元/方)
                    if (!(cargoDetailDTO.getIsShowQuote() == null || "".equals(cargoDetailDTO.getIsShowQuote()) || "0".equals(cargoDetailDTO.getIsShowQuote()))) {
                        String quoteUnit = SystemsUtil.quoteUnitConver(cargoDetailDTO.getQuoteUnit());
                        String myQuote = cargoDetailDTO.getMyQuote() == null || "".equals(cargoDetailDTO.getMyQuote()) ? "" : (cargoDetailDTO.getMyQuote() + quoteUnit);//我的报价(带单位：元/车、元/吨、元/方)
                        cargoDetailBO.setMyQuote(myQuote);
                        cargoDetailBO.setQuoteTime(DateUtil.dateFormat(cargoDetailDTO.getQuoteTime(), DateUtil.F_DATETOMIN));
                    }
                    mdv.addObject("code", "0");
                    mdv.addObject("cargoDetailBO",cargoDetailBO);
                    return mdv;
                }
            }catch (Exception e){
                LOG.error("系统出错 ", e);
            }
        }
        mdv.addObject("code", "1");
        mdv.addObject("value","系统出错。");
        return mdv;
    }

//    /**
//     * 返回数据
//     * @param request
//     * @param response
//     * @param orderId
//     * @return
//     */
//    @RequestMapping(value = "/caogoshare",method = RequestMethod.POST)
//    @ResponseBody
//    public Object caogoShare(HttpServletRequest request, HttpServletResponse response, Long orderId){
//
//
//        return "cargoShare";
//
//
//    }

}
