package com.cy.driver.action;

import com.cy.basic.service.dto.SystemConfigDTO;
import com.cy.basic.service.dto.SystemProtocolDTO;
import com.cy.driver.cloudService.CloudCommonWaybillService;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.driver.cloudService.SaasOrderInfoService;
import com.cy.driver.cloudService.SaasWaybillInfoService;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.DriverProtocolBO;
import com.cy.driver.domain.ScdProtocolBO;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.driver.service.OrderReceivableService;
import com.cy.driver.service.QueryOrderService;
import com.cy.driver.service.ViewService;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.rdcservice.service.dto.*;
import com.cy.saas.basic.model.po.SettingBusiness;
import com.cy.saas.business.model.dto.WaybillDetailsDTO;
import com.cy.saas.business.model.po.WaybillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2015/9/13.
 */
@Scope("prototype")
@RestController("transportProAction")
public class TransportProAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(AppShareAction.class);

    private String fileServiceUrl;
    @Value("${'fileService.url'}")
    public void setFileServiceUrl(String fileServiceUrl){
        this.fileServiceUrl = fileServiceUrl;
    }

    @Resource
    private OrderReceivableService orderReceivableService;

    @Resource
    private QueryOrderService queryOrderService;

    @Resource
    private ViewService viewService;

    @Resource
    private CloudCommonWaybillService cloudCommonWaybillService;

    @Resource
    private CloudUserService cloudUserService;

    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    @Resource
    private SaasWaybillInfoService saasWaybillInfoService;

    @Resource
    private SaasOrderInfoService saasOrderInfoService;

    /**
     * 查看运输协议
     * @param request
     * @param response
     * @param orderId
     * @param orderSource 订单来源 1快到网 2云配平台
     * @return
     */
    @RequestMapping(value = "/getTransprotPro", method = RequestMethod.GET)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_TRANSPROT_PRO)
//    @Log(type = LogEnum.AUDIT_CANCEL_ORDER)
    public ModelAndView getTransprotPro(HttpServletRequest request, HttpServletResponse response,
                                        Long orderId , String orderSource, String token) {
        ModelAndView mdv = new ModelAndView();
        if(StringUtils.isEmpty(orderSource) || "1".equals(orderSource)) {
            if (orderId != null) {
                try {
                    TransactionInfoDTO orderDTO = queryOrderService.getTransactionInfo(orderId);
                    if (orderDTO != null) {
                        if (orderDTO.getTransactionKind() == Constants.TRANSACTION_KIND_COMMON) {
                            mdv.setViewName("LeaseAgreement2");
                            DriverProtocolBO driverProtocolBO = orderReceivableService.transprotPro(orderId, orderDTO.getDriverId());
                            mdv.addObject("driverProtocolBO", driverProtocolBO);
                        } else {
                            mdv.setViewName("scdProtocol2");
                            ScdProtocolBO protocol = viewService.converterScdProtocol(orderId, null, null);
                            mdv.addObject("protocol", protocol);
                            mdv.addObject("cssType", 1);
                        }
                    }else{
                        DriverProtocolBO driverProtocolBO = new DriverProtocolBO();
                        mdv.setViewName("ypprotocol");
                        mdv.addObject("driverProtocolBO", driverProtocolBO);
                    }
                } catch (Exception e) {
                    logger.error("查看运输协议出现异常", e);
                }
            }
        }else if("2".equals(orderSource)) {
            WaybillDetailDTO waybillDetailDTO = cloudCommonWaybillService.queryWaybillDetail(orderId);
            ProtocolDTO protocolDTO = new ProtocolDTO();
            if(waybillDetailDTO==null || waybillDetailDTO.getWaybillInfoDTO()==null){
                mdv.setViewName("ypDriverRentProtocol");
                mdv.addObject("protocolDTO", protocolDTO);
                return mdv;
            }
            WaybillInfoDTO waybillInfoDTO = waybillDetailDTO.getWaybillInfoDTO();
            List<CarrierAssemDTO> carrierAssemDTOList = waybillDetailDTO.getCarrierAssemDTOList();
            SystemConfigDTO systemConfigDTO = cloudUserService.getBySiteCode(waybillInfoDTO.getSiteCode());
            protocolDTO.setOrderNum(waybillInfoDTO.getWaybillNum());//订单号
            protocolDTO.setPartA(StringUtils.isEmpty(waybillInfoDTO.getCreateUserName())?waybillInfoDTO.getCreateUserContact():waybillInfoDTO.getCreateUserName());//甲方（托运方）
            protocolDTO.setPartB(StringUtils.isEmpty(waybillInfoDTO.getTransportCompanyName())?waybillInfoDTO.getTransportUserName():waybillInfoDTO.getTransportCompanyName());//甲方（托运方）
            protocolDTO.setSendCargoTime(DateUtil.dateFormat(waybillInfoDTO.getNeedStartTime(), DateUtil.F_DATETIME));//发货时间
            protocolDTO.setDepartureAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getDepartureAddress(), waybillInfoDTO.getDepartureProvinceValue(), waybillInfoDTO.getDepartureCityValue(), waybillInfoDTO.getDepartureCountyValue(),""));//装货地
            protocolDTO.setReceiveAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getReceiveAddress(), waybillInfoDTO.getReceiveProvinceValue(), waybillInfoDTO.getReceiveCityValue(), waybillInfoDTO.getReceiveCountyValue(),""));//卸货地
            protocolDTO.setCargoName(carrierAssemDTOList.get(0).getInfoDTO().getCargoName());//货物名称
            protocolDTO.setWeightAndCubage(SystemsUtil.doubleConStr(waybillInfoDTO.getTotalWeight()) + "吨/" + SystemsUtil.doubleConStr(waybillInfoDTO.getTotalCubage()) + "方");//重量/体积
            protocolDTO.setTotalFare(waybillInfoDTO.getTotalFare() + "元");//运费
            protocolDTO.setPrepayFare(waybillInfoDTO.getPrepayFare() + "元");//预付款
            protocolDTO.setSignet(systemConfigDTO.getProtocolSign());//签章
            SystemProtocolDTO systemProtocolDTO = cloudUserService.getProtocolDTOBySiteCode(waybillInfoDTO.getSiteCode());
            if(systemProtocolDTO!=null) {
                protocolDTO.setProtocolText(systemProtocolDTO.getDriverOrderProtocolText());//协议内容
            }
            protocolDTO.setTransportType("普通货运公路运输");
            Response<DriverUserInfoDTO> response1 = driverUserHandlerService.getDriverUserInfo(waybillInfoDTO.getTransportUserId());
            if(response1!=null && response1.isSuccess() && response1.getData()!=null) {
                protocolDTO.setCode(response1.getData().getCode());
                protocolDTO.setCarNumber(response1.getData().getCarNumber());
                protocolDTO.setIdentityLicenseNum(response1.getData().getIdentityLicenseNum());
            }
            mdv.setViewName("ypDriverRentProtocol");
            mdv.addObject("protocolDTO", protocolDTO);
            mdv.addObject("fileServiceUrl",fileServiceUrl);
            return mdv;
        }else if("3".equals(orderSource)){
            WaybillDetailsDTO waybillDetailsDTO = saasWaybillInfoService.getWaybillDetails(Long.valueOf(orderId));
            ProtocolDTO protocolDTO = new ProtocolDTO();
            if(waybillDetailsDTO==null || waybillDetailsDTO.getWaybillInfo()==null){
                mdv.setViewName("ypDriverRentProtocol");
                mdv.addObject("protocolDTO", protocolDTO);
                return mdv;
            }
            WaybillInfo waybillInfo = waybillDetailsDTO.getWaybillInfo();
            SettingBusiness settingBusiness = saasOrderInfoService.getByUserId(waybillInfo.getOwnUserId());
            protocolDTO.setOrderNum(waybillInfo.getWaybillNum());//订单号
            protocolDTO.setPartA(StringUtils.isEmpty(waybillInfo.getCreateUserName())?waybillInfo.getCreateUserContact():waybillInfo.getCreateUserName());//甲方（托运方）
            protocolDTO.setPartB(StringUtils.isEmpty(waybillInfo.getTransportCompanyName())?waybillInfo.getTransportUserName():waybillInfo.getTransportCompanyName());//甲方（托运方）
            protocolDTO.setSendCargoTime(DateUtil.dateFormat(waybillInfo.getNeedStartTime(), DateUtil.F_DATETIME));//发货时间
            protocolDTO.setDepartureAddress(SystemsUtil.buildAllAddress(waybillInfo.getDepartureAddress(), waybillInfo.getDepartureProvinceValue(), waybillInfo.getDepartureCityValue(), waybillInfo.getDepartureCountyValue(),""));//装货地
            protocolDTO.setReceiveAddress(SystemsUtil.buildAllAddress(waybillInfo.getReceiveAddress(), waybillInfo.getReceiveProvinceValue(), waybillInfo.getReceiveCityValue(), waybillInfo.getReceiveCountyValue(),""));//卸货地
            protocolDTO.setCargoName(waybillInfo.getWaybillName());//货物名称
            protocolDTO.setWeightAndCubage(SystemsUtil.doubleConStr(waybillInfo.getTotalWeight()) + "吨/" + SystemsUtil.doubleConStr(waybillInfo.getTotalCubage()) + "方");//重量/体积
            protocolDTO.setTotalFare(waybillInfo.getTotalFare() + "元");//运费
            protocolDTO.setPrepayFare(waybillInfo.getPrepayFare() + "元");//预付款
            protocolDTO.setSignet(settingBusiness.getProtocolSign());//签章
            protocolDTO.setProtocolText(settingBusiness.getDriverOrderProtocolText());//协议内容
            protocolDTO.setTransportType("普通货运公路运输");
            Response<DriverUserInfoDTO> response1 = driverUserHandlerService.getDriverUserInfo(waybillInfo.getTransportUserId());
            if(response1!=null && response1.isSuccess() && response1.getData()!=null) {
                protocolDTO.setCode(response1.getData().getCode());
                protocolDTO.setCarNumber(response1.getData().getCarNumber());
                protocolDTO.setIdentityLicenseNum(response1.getData().getIdentityLicenseNum());
            }
            mdv.setViewName("ypDriverRentProtocol");
            mdv.addObject("protocolDTO", protocolDTO);
            mdv.addObject("fileServiceUrl",fileServiceUrl);
            return mdv;
        }
        return mdv;
    }


    @RequestMapping(value = "/getTurnWaybillTransportPro", method = RequestMethod.GET)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_TURN_WAYBILL_TRANSPORT_PRO)
    public ModelAndView getTurnWaybillTransportPro(String driverId, String carrierId, String carrierType, String orderId, String totalFare, String prepayFare, String departureProvince, String departureCity, String departureCounty, String departureDetailAddress, String needStartTime, String name, String carNum){
        ModelAndView mdv = new ModelAndView();
        ProtocolDTO protocolDTO = new ProtocolDTO();
        if(StringUtils.isEmpty(driverId) || StringUtils.isEmpty(carrierType) || StringUtils.isEmpty(orderId) || StringUtils.isEmpty(totalFare)){
            mdv.setViewName("ypDriverRentProtocol");
            mdv.addObject("protocolDTO", protocolDTO);
            return mdv;
        }
        WaybillDetailDTO waybillDetailDTO = cloudCommonWaybillService.queryWaybillDetail(Long.valueOf(orderId));
        DriverUserInfoDTO userInfoDTO = driverUserHandlerService.getDriverInfo(Long.valueOf(driverId));
        WaybillInfoDTO waybillInfoDTO = waybillDetailDTO.getWaybillInfoDTO();
        List<CarrierAssemDTO> carrierAssemDTOList = waybillDetailDTO.getCarrierAssemDTOList();
        if(carrierType.equals("1")){
            DriverUserInfoDTO partBInfo = driverUserHandlerService.getDriverInfo(Long.valueOf(carrierId));
            protocolDTO.setPartA(userInfoDTO.getCode());
            protocolDTO.setPartB(StringUtils.isEmpty(partBInfo.getName())?partBInfo.getCode():partBInfo.getName());
            protocolDTO.setSendCargoTime(needStartTime);
            protocolDTO.setDepartureAddress(SystemsUtil.buildAllAddress(departureDetailAddress,departureProvince,departureCity,departureCounty,""));
            protocolDTO.setReceiveAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getReceiveAddress(),waybillInfoDTO.getReceiveProvinceValue(),
                    waybillInfoDTO.getReceiveCityValue(),waybillInfoDTO.getReceiveCountyValue(),""));
            protocolDTO.setCargoName(carrierAssemDTOList.get(0).getInfoDTO().getCargoName());
            protocolDTO.setTotalFare(totalFare);
            protocolDTO.setPrepayFare(prepayFare);
            protocolDTO.setCode(partBInfo.getCode());
            protocolDTO.setCarNumber(partBInfo.getCarNumber());
            protocolDTO.setIdentityLicenseNum(partBInfo.getIdentityLicenseNum());
            SystemConfigDTO systemConfigDTO = cloudUserService.getBySiteCode(waybillInfoDTO.getSiteCode());
            protocolDTO.setSignet(systemConfigDTO.getProtocolSign());
            SystemProtocolDTO systemProtocolDTO = cloudUserService.getProtocolDTOBySiteCode(waybillInfoDTO.getSiteCode());
            if(systemProtocolDTO!=null) {
                protocolDTO.setProtocolText(systemProtocolDTO.getLogisticsOrderProtocolText());//协议内容
            }
            protocolDTO.setTransportType("普通货运公路运输");
            mdv.setViewName("ypDriverRentProtocol");
            mdv.addObject("protocolDTO", protocolDTO);
            mdv.addObject("fileServiceUrl",fileServiceUrl);
            return mdv;
        }else if(carrierType.equals("3")){
            UserLoginInfoDTO userLoginInfoDTO = cloudUserService.getOwnerInfo(Long.valueOf(carrierId));
            protocolDTO.setPartA(userInfoDTO.getCode());
            protocolDTO.setPartB(StringUtils.isEmpty(userLoginInfoDTO.getName())?userLoginInfoDTO.getCode():userLoginInfoDTO.getName());
            protocolDTO.setSendCargoTime(needStartTime);
            protocolDTO.setDepartureAddress(SystemsUtil.buildAllAddress(departureDetailAddress,departureProvince,departureCity,departureCounty,""));
            protocolDTO.setReceiveAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getReceiveAddress(),waybillInfoDTO.getReceiveProvinceValue(),
                    waybillInfoDTO.getReceiveCityValue(),waybillInfoDTO.getReceiveCountyValue(),""));
            protocolDTO.setCargoName(carrierAssemDTOList.get(0).getInfoDTO().getCargoName());
            protocolDTO.setTotalFare(totalFare);
            protocolDTO.setPrepayFare(prepayFare);
            protocolDTO.setWeightAndCubage(waybillInfoDTO.getTotalWeight() + "吨/" + waybillInfoDTO.getTotalCubage() + "方");//重量/体积
            SystemConfigDTO systemConfigDTO = cloudUserService.getBySiteCode(waybillInfoDTO.getSiteCode());
            protocolDTO.setSignet(systemConfigDTO.getProtocolSign());
            SystemProtocolDTO systemProtocolDTO = cloudUserService.getProtocolDTOBySiteCode(waybillInfoDTO.getSiteCode());
            if(systemProtocolDTO!=null) {
                protocolDTO.setProtocolText(systemProtocolDTO.getLogisticsOrderProtocolText());//协议内容
            }
            mdv.setViewName("ypTransportProtocol");
            mdv.addObject("protocolDTO", protocolDTO);
            mdv.addObject("fileServiceUrl",fileServiceUrl);
            return mdv;
        }else{
            protocolDTO.setPartA(userInfoDTO.getCode());
            protocolDTO.setPartB(name);
            protocolDTO.setSendCargoTime(needStartTime);
            protocolDTO.setDepartureAddress(SystemsUtil.buildAllAddress(departureDetailAddress,departureProvince,departureCity,departureCounty,""));
            protocolDTO.setReceiveAddress(SystemsUtil.buildAllAddress(waybillInfoDTO.getReceiveAddress(),waybillInfoDTO.getReceiveProvinceValue(),
                    waybillInfoDTO.getReceiveCityValue(),waybillInfoDTO.getReceiveCountyValue(),""));
            protocolDTO.setCargoName(carrierAssemDTOList.get(0).getInfoDTO().getCargoName());
            protocolDTO.setTotalFare(totalFare);
            protocolDTO.setPrepayFare(prepayFare);
            protocolDTO.setCarNumber(carNum);
            protocolDTO.setWeightAndCubage(waybillInfoDTO.getTotalWeight() + "吨/" + waybillInfoDTO.getTotalCubage() + "方");//重量/体积
            SystemConfigDTO systemConfigDTO = cloudUserService.getBySiteCode(waybillInfoDTO.getSiteCode());
            protocolDTO.setSignet(systemConfigDTO.getProtocolSign());
            SystemProtocolDTO systemProtocolDTO = cloudUserService.getProtocolDTOBySiteCode(waybillInfoDTO.getSiteCode());
            if(systemProtocolDTO!=null) {
                protocolDTO.setProtocolText(systemProtocolDTO.getLogisticsOrderProtocolText());//协议内容
            }
            mdv.setViewName("ypTransportProtocol");
            mdv.addObject("protocolDTO", protocolDTO);
            mdv.addObject("fileServiceUrl",fileServiceUrl);
            return mdv;
        }
    }




    /**
     * 云配货运协议
     * @return
     */
//    @RequestMapping(value = "ypOwnerTransportProtocolSaveCargo")
////    @ApiHeadAnnotation( reqHeadCode = ApiReqCodeEnum.CHECK_TRANSPORT_PROTOCOL)
//    public ModelAndView ypOwnerTransportProtocol(Model model, String userId ,String userType,String carrierId, String needStartTime, String cargoName, String totalWeight, String totalCubage, String totalFare, String prepayFare , String departureProvinceValue, String departureCityValue , String departureCountyValue, String departureAddress , String receiveProvinceValue, String receiveCityValue, String receiveCountyValue, String receiveAddress,Long orderId ,String orderSource ) {
//        if (logger.isDebugEnabled())
//            logger.debug("进入转单运单协议");
//        ModelAndView mdv = new ModelAndView();
//        ProtocolDTO protocolDTO = new ProtocolDTO();
//        WaybillDetailDTO waybillDetailDTO = cloudCommonWaybillService.queryWaybillDetail(orderId);
//        if(waybillDetailDTO==null || waybillDetailDTO.getWaybillInfoDTO()==null){
//            mdv.setViewName("ypDriverRentProtocol");
//            mdv.addObject("protocolDTO", protocolDTO);
//            return mdv;
//        }
//        /**
//         * 原始发货地收货地地址
//         */
//        SystemConfigDTO systemConfigDTO = cloudUserService.getBySiteCode(waybillDetailDTO.getWaybillInfoDTO().getSiteCode());//查询平台信息
//        Response<DriverUserInfoDTO> response1 = driverUserHandlerService.getDriverUserInfo(Long.valueOf(userId));//查询甲方信息
//        if(response1==null || !response1.isSuccess() || response1.getData()==null){
//            mdv.setViewName("ypDriverRentProtocol");
//            mdv.addObject("protocolDTO", protocolDTO);
//            return mdv;
//        }
//        if(userType.equals("1")){
//            Response<DriverUserInfoDTO> responsePartB = driverUserHandlerService.getDriverUserInfo(Long.valueOf(carrierId));//查询乙方信息
//
//        }else if(userType.equals("3")){
//            com.cy.top56.common.Response<SiteLineCarrierDetailDTO> partBresponse = lineCarrierService.getSiteLineCarrierDetail(Long.valueOf(carrierId));
//            if(partBresponse!=null && partBresponse.getData()!=null) {
//                protocolDTO.setPartB(com.alibaba.dubbo.common.utils.StringUtils.isEmpty(partBresponse.getData().getLineCarrierDTO().getCarrierName()) ? partBresponse.getData().getLineCarrierDTO().getCarrierMobilphone() : partBresponse.getData().getLineCarrierDTO().getCarrierName());//乙方（承运方）
//            }
//        }else{
//            DriverUserInfoDTO driverUserInfoDTO = driverCopyService.getDriverInfo(Long.valueOf(carrierId));
//            protocolDTO.setPartB(com.alibaba.dubbo.common.utils.StringUtils.isEmpty(driverUserInfoDTO.getName())?driverUserInfoDTO.getCode():driverUserInfoDTO.getName());//乙方（承运方）
//        }
//        protocolDTO.setSendCargoTime(needStartTime);//发货时间
//        protocolDTO.setDepartureAddress(SystemUtil.buildAllAddress(departureAddress,departureProvinceValue,departureCityValue,departureCountyValue,""));//装货地
//        protocolDTO.setReceiveAddress(SystemUtil.buildAllAddress(receiveAddress,receiveProvinceValue,receiveCityValue,receiveCountyValue,""));//卸货地
//        protocolDTO.setCargoName(cargoName);//货物名称
//        protocolDTO.setWeightAndCubage(totalWeight + "吨/" + totalCubage + "方");//重量/体积
//        if(com.alibaba.dubbo.common.utils.StringUtils.isEmpty(totalFare) || totalFare.equals("0.00") || totalFare.equals("0")){
//            totalFare = "运费面议";
//            protocolDTO.setTotalFare(totalFare);//运费
//        }else{
//            protocolDTO.setTotalFare(totalFare + "元");//运费
//        }
//        if(com.alibaba.dubbo.common.utils.StringUtils.isEmpty(prepayFare)){
//            prepayFare = "0.00";
//        }
//        protocolDTO.setPrepayFare(prepayFare + "元");//预付款
//        protocolDTO.setSignet(systemConfigDTO.getProtocolSign());//签章
//        SystemProtocolDTO systemProtocolDTO = systemPlatformService.getProtocolDTOBySiteCode(siteCode);
//        protocolDTO.setProtocolText(systemProtocolDTO.getLogisticsOrderProtocolText());//协议内容
//        protocolDTO.setTransportType("普通货运公路运输");
//        model.addAttribute("protocolDTO",protocolDTO);
//        return "ypprotocol/ypTransportProtocol";
//    }

}
