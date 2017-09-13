package com.cy.driver.api.convert;

import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.api.domain.res.CargoDetail;
import com.cy.driver.api.domain.res.CargoDetailList;
import com.cy.driver.api.domain.res.NearAndFindCargoList;
import com.cy.driver.common.enumer.PackTypeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.redis.NetWordPhoneClient;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.CargoDetailBO;
import com.cy.driver.domain.PageBase;
import com.cy.rdcservice.service.dto.CarrierAddressDTO;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.CarrierBasicInfoDTO;
import com.cy.rdcservice.service.dto.CarrierDetailDTO;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.response.Cargo2DTO;
import com.cy.search.service.dto.response.CargoQuoteDTO;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yanst 2016/5/27 10:28
 */
public class NearAndFindCargoConvert {


    /**
     * 附近货源列表转换
     * @param pageResult 货源信息（包含分页信息）
     * @param driverId 司机id
     * @param driverState 司机认证状态
     * @param isAccurateCargo 是否是精准货源接口调用
     * @param quoteMap 报价信息
     * @return
     */
    public static PageBase<NearAndFindCargoList> nearCargoConvert(PageResult<Cargo2DTO> pageResult, Long driverId, Byte driverState, boolean isAccurateCargo, NetWordPhoneClient netWordPhoneClient, Map<String, CargoQuoteDTO> quoteMap){
        PageBase<NearAndFindCargoList> pageBase = new PageBase<>();
        pageBase.setTotalPage((int)pageResult.getTotalPage());
        pageBase.setTotalNum((int)pageResult.getTotalRecord());
        List<NearAndFindCargoList> list = new ArrayList<NearAndFindCargoList>();
        for (Cargo2DTO cargo2DTO : pageResult.getDataList()){
            list.add(nearCargoObjConvert(cargo2DTO, driverId, driverState, isAccurateCargo, netWordPhoneClient, quoteMap));
        }
        pageBase.setListData(list);
        return pageBase;
    }

    /**
     * 找货列表转换
     * @param
     * @return
     */
    public static PageBase<NearAndFindCargoList> findCargoConvert(PageResult<Cargo2DTO> pageResult, Long driverId, Byte driverState, boolean isAccurateCargo, NetWordPhoneClient netWordPhoneClient, Map<String, CargoQuoteDTO> quoteMap){
        PageBase<NearAndFindCargoList> pageBase = new PageBase<NearAndFindCargoList>();
        pageBase.setTotalPage((int)pageResult.getTotalPage());
        pageBase.setTotalNum((int)pageResult.getTotalRecord());
        List<NearAndFindCargoList> list = new ArrayList<NearAndFindCargoList>();
        for (Cargo2DTO cargoDTO : pageResult.getDataList()) {
            list.add(findCargoObjList(cargoDTO, driverId, driverState, isAccurateCargo, netWordPhoneClient, quoteMap));
        }
        pageBase.setListData(list);
        return pageBase;
    }

    public static NearAndFindCargoList findCargoObjList(Cargo2DTO cargoDTO, Long driverId, Byte driverState, boolean isAccurateCargo, NetWordPhoneClient netWordPhoneClient, Map<String, CargoQuoteDTO> quoteMap){
        NearAndFindCargoList cargoBo = new NearAndFindCargoList();
        cargoBo.setCargoIdStr(cargoDTO.getCargoId().toString());//货物ID
        cargoBo.setCompanyName(cargoDTO.getCompanyName());
        if (cargoDTO.getCompanySubmitType() != null) {
            cargoBo.setCompanyAuthStatus(cargoDTO.getCompanySubmitType());
        } else {
            cargoBo.setCompanyAuthStatus((byte)0);
        }
        cargoBo.setStartAddress(cargoDTO.getStartCity());
        cargoBo.setEndAddress(cargoDTO.getEndCity());
        cargoBo.setCargoName(cargoDTO.getCargoName());
        //重量(单位：吨)
        if(cargoDTO.getCargoSource()!=null && cargoDTO.getCargoSource()==1) {
            cargoBo.setWeight(
                    SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight() == null ? BigDecimal.ZERO : cargoDTO.getCargoWeight()));
        }else{
            cargoBo.setWeight(
                    SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight() == null ? BigDecimal.ZERO : cargoDTO.getCargoWeight()));
        }
        //体积(单位：方)
        cargoBo.setVolume(
                SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));
        //要求的车长(单位：米)
        cargoBo.setCarLength(
                SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));

        //车辆类型名称
        cargoBo.setVehicleTypeName(SystemData.findVehicleTypeName(cargoDTO.getVehicleType()));

        //车厢类型名称
        cargoBo.setCarriageTypeName(SystemData.findCarriageTypeName(cargoDTO.getCarriageType()));

        CargoQuoteDTO quoteDTO = quoteMap.get(cargoDTO.getCargoId() + "-" + driverId);
        String myQuote = "";
        if (quoteDTO != null) {
            //我的报价(带单位：元/车、元/吨、元/方)
            String quoteUnit = SystemsUtil.quoteUnitConver((byte)quoteDTO.getQuoteType().intValue());
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
        if(!isAccurateCargo){
            //货主联系方式
            if (StringUtils.isNotBlank(cargoDTO.getContactMobilephone())){
                cargoBo.setOwnerPhone(cargoDTO.getContactMobilephone());
            } else {
                cargoBo.setOwnerPhone(cargoDTO.getContactTelephone());
            }
            //限制次数
            if (cargoDTO.getCargoId() != null) {
                cargoBo.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(driverId.toString(), cargoDTO.getCargoId().toString(), driverState));
            } else {
                cargoBo.setNetworkTelephoneState("0");
            }
        }
        //区域平台名称
        cargoBo.setDistributionCenterName(cargoDTO.getPlatformName());
        //区域平台编号
        cargoBo.setPlatformCode(cargoDTO.getPlatformCode());
        //货源来源（1快到网货源、2区域配送托单）
        cargoBo.setCargoSource(cargoDTO.getCargoSource());
        return cargoBo;
    }

    /**
     * 货源详情
     * @param
     * @return
     */
    public static CargoDetail cargoDetailConvert(){
        CargoDetail cargoDetail = new CargoDetail();

        //TODO
        return cargoDetail;
    }

    /**
     *  CarrierAssemDTO->CargoDetail(货源详情信息转换)
     */
    public static CargoDetail carrierAssemDTOToCargoDetail(CarrierAssemDTO carrierAssemDTO){
        CarrierBasicInfoDTO carrierBasicInfoDTO = carrierAssemDTO.getInfoDTO();
        CarrierAddressDTO carrierAddressDTO = carrierAssemDTO.getAddressDTO();
        CargoDetail cargoDetail = new CargoDetail();
        cargoDetail.setCargoIdStr(String.valueOf(carrierBasicInfoDTO.getId()));
        cargoDetail.setCompanyName(carrierBasicInfoDTO.getCompanyName());
        cargoDetail.setConsignorName(carrierAddressDTO.getDepartureContact());
        cargoDetail.setCargoName(carrierBasicInfoDTO.getCargoName());
        cargoDetail.setTotalNumber(carrierBasicInfoDTO.getTotalQuantity());
        cargoDetail.setWeight(carrierBasicInfoDTO.getTotalWeight()==null?BigDecimal.ZERO:BigDecimal.valueOf(carrierBasicInfoDTO.getTotalWeight()));//重量
        cargoDetail.setVolume(carrierBasicInfoDTO.getTotalCubage()==null?BigDecimal.ZERO:BigDecimal.valueOf(carrierBasicInfoDTO.getTotalCubage()));//体积
        cargoDetail.setOwnerUserId(String.valueOf(carrierBasicInfoDTO.getCreateUserId()));//发布用户ID
        cargoDetail.setCreateUserType(String.valueOf(carrierBasicInfoDTO.getCreateUserType()));
        cargoDetail.setConsignorAddress(SystemsUtil.buildAddress(carrierAddressDTO.getDepartureAddress(), carrierAddressDTO.getDepartureProvinceValue(), carrierAddressDTO.getDepartureCityValue(), carrierAddressDTO.getDepartureCountyValue()));
        cargoDetail.setConsigneeName(carrierAddressDTO.getReceiveContact());//收货人姓名
        cargoDetail.setConsigneeMobilePhone(carrierAddressDTO.getReceiveMobile());//收货人手机
        cargoDetail.setConsigneeTelePhone(carrierAddressDTO.getReceiveTelephone());//收货人电话
        cargoDetail.setConsigneeAddress(SystemsUtil.buildAddress(carrierAddressDTO.getReceiveAddress(), carrierAddressDTO.getReceiveProvinceValue(), carrierAddressDTO.getReceiveCityValue(), carrierAddressDTO.getReceiveCountyValue()));
        cargoDetail.setShippingMethod(carrierBasicInfoDTO.getSendWay());//送货方式（0自取 1送货上门）
        cargoDetail.setCollectionDelivery(carrierBasicInfoDTO.getCollectionPayment());//代收货款
        cargoDetail.setFreightPrice(carrierBasicInfoDTO.getTotalFare());//运费总价
        cargoDetail.setPrepayment(carrierBasicInfoDTO.getPrepayFare());//预付运费
        cargoDetail.setCargoRemark(carrierBasicInfoDTO.getRemark());//备注
        cargoDetail.setsAddress(SystemsUtil.buildAddress(carrierAddressDTO.getDepartureAddress(), carrierAddressDTO.getDepartureProvinceValue(), carrierAddressDTO.getDepartureCityValue(), carrierAddressDTO.getDepartureCountyValue()));
        cargoDetail.seteAddress(SystemsUtil.buildAddress(carrierAddressDTO.getReceiveAddress(), carrierAddressDTO.getReceiveProvinceValue(), carrierAddressDTO.getReceiveCityValue(), carrierAddressDTO.getReceiveCountyValue()));
        cargoDetail.setsTime(SystemsUtil.cargoOrderStartTime(carrierBasicInfoDTO.getNeedStartTime()));//要求装货时间
        cargoDetail.seteTime(SystemsUtil.cargoOrderEndTime(carrierBasicInfoDTO.getNeedEndTime()));//要求卸货时间
        cargoDetail.setPubTime(DateUtil.dateFormat(carrierBasicInfoDTO.getCreateTime(), DateUtil.F_DATETOMIN));//发布时间
        cargoDetail.setSaddressInfoPCC(SystemsUtil.buildAddress(carrierAddressDTO.getDepartureProvinceValue(), carrierAddressDTO.getDepartureCityValue(), carrierAddressDTO.getDepartureCountyValue()));
        cargoDetail.setSaddressInfoDetail(carrierAddressDTO.getDepartureAddress());
        cargoDetail.setEaddressInfoPCC(SystemsUtil.buildAddress(carrierAddressDTO.getReceiveProvinceValue(), carrierAddressDTO.getReceiveCityValue(), carrierAddressDTO.getReceiveCountyValue()));
        cargoDetail.setEaddressInfoDetail(carrierAddressDTO.getReceiveAddress());
        //判断货源状态 1.待承运 2.已过期 3.已被承运
        if(carrierBasicInfoDTO.getState()==11){
            String dateStr = DateUtil.dateFormat(new Date(), DateUtil.F_DATE);
            if(!DateUtil.parseDate(dateStr, DateUtil.F_DATE).after(carrierBasicInfoDTO.getNeedStartTime())){
                cargoDetail.setCargoStateReal("1");
            }else{
                cargoDetail.setCargoStateReal("2");
            }
        }else{
            cargoDetail.setCargoStateReal("3");
        }
        List<CargoDetailList> list= new ArrayList<>();
        for(CarrierDetailDTO carrierDetailDTO: carrierAssemDTO.getDetailDTOList()){
            CargoDetailList cargoDetailList = new CargoDetailList();
            cargoDetailList.setCargoName(carrierDetailDTO.getCargoName());
            cargoDetailList.setCargoType(String.valueOf(carrierDetailDTO.getCargoType()));
            cargoDetailList.setPack(PackTypeEnum.get(carrierDetailDTO.getPacking()).getType());
            cargoDetailList.setTotalNumber(carrierDetailDTO.getQuantity()==null?"":String.valueOf(carrierDetailDTO.getQuantity()));
            cargoDetailList.setVolume(carrierDetailDTO.getCubage()==null?"":String.valueOf(carrierDetailDTO.getCubage()));
            cargoDetailList.setWeight(carrierDetailDTO.getWeight()==null?"":String.valueOf(carrierDetailDTO.getWeight()/1000));
            list.add(cargoDetailList);
        }
        cargoDetail.setCargoList(list);
        return cargoDetail;
    }

    /**
     *  CargoDetailDTO->CargoDetailBO(货源详情信息转换)
     */
    public static CargoDetailBO cargoDetailDTOToCargoDetailBO(CargoDetailDTO cargoDetailDTO){
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
        cargoDetailBO.seteAddress(eAddress);
        //装货时间(yyyy-mm-dd hh:mm)
        //要求装货时间(yyyy-mm-dd )
        cargoDetailBO.setsTime(SystemsUtil.cargoOrderStartTime(cargoDetailDTO.getRequestStartTime()));
        //要求卸货时间(yyyy-mm-dd )
        cargoDetailBO.seteTime(SystemsUtil.cargoOrderEndTime(cargoDetailDTO.getRequestEndTime()));
        cargoDetailBO.setWeight(
                SystemsUtil.buildWeightUnit(cargoDetailDTO.getCargoWeight()));//重量(单位：吨)
        cargoDetailBO.setVolume(
                SystemsUtil.buildVolumeUnit(cargoDetailDTO.getCargoCubage()));//体积(单位：方)
        cargoDetailBO.setCarLength(
                SystemsUtil.buildCarLenUnit(cargoDetailDTO.getRequestCarLen()));//要求的车长(单位：米)
        //车辆类型名称
        cargoDetailBO.setVehicleTypeName(SystemData.findVehicleTypeName(cargoDetailDTO.getVehicleType()));

        //车厢类型名称
        cargoDetailBO.setCarriageTypeName(SystemData.findCarriageTypeName(cargoDetailDTO.getCarriageType()));

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

        return cargoDetailBO;
    }


    /**
     * 附近货源列表转换
     * @param cargo2DTO 单条货源信息
     * @param driverId 司机id
     * @param driverState 司机认证状态
     * @param isAccurateCargo 是否是精准货源接口调用
     * @param netWordPhoneClient
     * @param quoteMap 报价信息
     * @return
     */
    public static NearAndFindCargoList nearCargoObjConvert(Cargo2DTO cargo2DTO, Long driverId, Byte driverState, boolean isAccurateCargo, NetWordPhoneClient netWordPhoneClient, Map<String, CargoQuoteDTO> quoteMap){
        NearAndFindCargoList cargoInfo = new NearAndFindCargoList();
        cargoInfo.setCargoIdStr(cargo2DTO.getCargoId().toString());
        cargoInfo.setCompanyName(cargo2DTO.getCompanyName());
        cargoInfo.setCargoSource(cargo2DTO.getCargoSource());
        if (cargo2DTO.getCompanySubmitType() != null) {
            cargoInfo.setCompanyAuthStatus(cargo2DTO.getCompanySubmitType());
        } else {
            cargoInfo.setCompanyAuthStatus((byte)0);
        }
        cargoInfo.setStartAddress(cargo2DTO.getStartCity());
        cargoInfo.setEndAddress(cargo2DTO.getEndCity());
        cargoInfo.setCargoName(cargo2DTO.getCargoName());
        //重量(单位：吨)
        if(cargo2DTO.getCargoSource()!=null && cargo2DTO.getCargoSource()==1) {
            cargoInfo.setWeight(
                    SystemsUtil.buildWeightUnit(cargo2DTO.getCargoWeight() == null ? BigDecimal.ZERO : cargo2DTO.getCargoWeight()));
        }else{
            cargoInfo.setWeight(
                    SystemsUtil.buildWeightUnit(cargo2DTO.getCargoWeight() == null ? BigDecimal.ZERO : cargo2DTO.getCargoWeight()));
        }
        //体积(单位：方)
        cargoInfo.setVolume(
                SystemsUtil.buildVolumeUnit(cargo2DTO.getCargoCubage()));
        //要求的车长(单位：米)
        cargoInfo.setCarLength(
                SystemsUtil.buildCarLenUnit(cargo2DTO.getRequestCarLen()));

        //车辆类型名称
        cargoInfo.setVehicleTypeName(SystemData.findVehicleTypeName(cargo2DTO.getVehicleType()));

        //车厢类型名称
        cargoInfo.setCarriageTypeName(SystemData.findCarriageTypeName(cargo2DTO.getCarriageType()));

        CargoQuoteDTO quoteDTO = quoteMap.get(cargo2DTO.getCargoId() + "-" + driverId);
        String myQuote = "";
        if (quoteDTO != null) {
            //我的报价(带单位：元/车、元/吨、元/方)
            String quoteUnit = SystemsUtil.quoteUnitConver((byte)quoteDTO.getQuoteType().intValue());
            if (quoteDTO.getQuoteFair() != null) {
                myQuote = quoteDTO.getQuoteFair() + quoteUnit;
            }
        }
        cargoInfo.setMyQuote(myQuote);
        //装货时间(yyyy-mm-dd)
        cargoInfo.setStartTime(SystemsUtil.cargoOrderStartTime(cargo2DTO.getRequestStartTime()));
        //卸货时间(yyyy-mm-dd)
        cargoInfo.setEndTime(SystemsUtil.cargoOrderEndTime(cargo2DTO.getRequestEndTime()));
        //发布时间(例如：几小时前)
        cargoInfo.setPubTime(SystemsUtil.getTimeStr(cargo2DTO.getCreateTime()));
        //运费总价
        cargoInfo.setTotalFare(SystemsUtil.getTotalFare(cargo2DTO.getTotalFare()));
        //预付运费
        cargoInfo.setPrepayFare(SystemsUtil.getTotalFare(cargo2DTO.getPrepayFare()));

        //如果是精准货源则不执行
        if(!isAccurateCargo){
            //货主联系方式
            if (StringUtils.isNotBlank(cargo2DTO.getContactMobilephone())){
                cargoInfo.setOwnerPhone(cargo2DTO.getContactMobilephone());
            } else {
                cargoInfo.setOwnerPhone(cargo2DTO.getContactTelephone());
            }
            //限制次数
            if (cargo2DTO.getCargoId() != null) {
                cargoInfo.setNetworkTelephoneState(netWordPhoneClient.haveCallNumber(driverId.toString(), cargo2DTO.getCargoId().toString(), driverState));
            } else {
                cargoInfo.setNetworkTelephoneState("0");
            }
        }

        return cargoInfo;
    }

    /**
     * CargoInfoDTO->CargoDetail
     */
    public static CargoDetailBO cargoInfoDTOConvert(CargoInfoDTO cargoInfoDTO){
        CargoDetailBO cargoDetailBO = new CargoDetailBO();
        //货物ID
        cargoDetailBO.setCargoId(cargoInfoDTO.getId());
        //企业名称
        cargoDetailBO.setCompanyName(cargoInfoDTO.getCompanyName());
        if (cargoInfoDTO.getCompanyId() != null
                && cargoInfoDTO.getCompanyId().intValue() > 1) {
            cargoDetailBO.setCompanyAuthStatus((byte) 3);
        } else {
            cargoDetailBO.setCompanyAuthStatus((byte) 0);
        }
        cargoDetailBO.setConsignorName(cargoInfoDTO.getDeployUserName());
        String mobileStr = cargoInfoDTO.getContactMobilephone().substring(4,8);
        cargoDetailBO.setConsignorMobile(cargoInfoDTO.getContactMobilephone().replace(mobileStr,"****"));
        cargoDetailBO.setsTime(DateUtil.dateFormat(cargoInfoDTO.getRequestStartTime(), DateUtil.F_DATE));
        cargoDetailBO.seteTime(DateUtil.dateFormat(cargoInfoDTO.getRequestEndTime(), DateUtil.F_DATE));
        cargoDetailBO.setPubTime(DateUtil.dateFormat(cargoInfoDTO.getCreateTime(), DateUtil.F_DATE));
        cargoDetailBO.setCargoName(cargoInfoDTO.getCargoName());
        cargoDetailBO.setWeight(SystemsUtil.buildWeightUnit(cargoInfoDTO.getCargoWeight()));//重量(单位：吨)
        cargoDetailBO.setVolume(SystemsUtil.buildVolumeUnit(cargoInfoDTO.getCargoCubage()));//体积(单位：方)
        cargoDetailBO.setCarLength(SystemsUtil.buildCarLenUnit(cargoInfoDTO.getRequestCarLen()));//要求的车长(单位：米)
        //车辆类型名称
        cargoDetailBO.setVehicleTypeName(SystemData.findVehicleTypeName(cargoInfoDTO.getVehicleType()));
        //车厢类型名称
        cargoDetailBO.setCarriageTypeName(SystemData.findCarriageTypeName(cargoInfoDTO.getCarriageType()));
        cargoDetailBO.setsAddress(SystemsUtil.buildAddress(cargoInfoDTO.getStartTown(),cargoInfoDTO.getStartProvince(),cargoInfoDTO.getStartCity(),cargoInfoDTO.getStartCounty()));
        cargoDetailBO.seteAddress(SystemsUtil.buildAddress(cargoInfoDTO.getEndTown(),cargoInfoDTO.getEndProvince(),cargoInfoDTO.getEndCity(),cargoInfoDTO.getEndCounty()));
        cargoDetailBO.setSaddressInfoPCC(SystemsUtil.buildAddress(cargoInfoDTO.getStartProvince(),cargoInfoDTO.getStartCity(),cargoInfoDTO.getStartCounty()));
        cargoDetailBO.setSaddressInfoDetail(cargoInfoDTO.getStartTown());
        cargoDetailBO.setEaddressInfoPCC(SystemsUtil.buildAddress(cargoInfoDTO.getEndProvince(),cargoInfoDTO.getEndCity(),cargoInfoDTO.getEndCounty()));
        cargoDetailBO.setEaddressInfoDetail(cargoInfoDTO.getEndTown());
        cargoDetailBO.setPrepayFare(String.valueOf(cargoInfoDTO.getPrepayFare()));
        cargoDetailBO.setTotalFare(String.valueOf(cargoInfoDTO.getTotalFare()));
        cargoDetailBO.setCargoRemark(cargoInfoDTO.getRemark());
        return cargoDetailBO;
    }



}
