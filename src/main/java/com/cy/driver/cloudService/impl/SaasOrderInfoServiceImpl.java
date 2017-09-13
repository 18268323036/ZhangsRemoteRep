package com.cy.driver.cloudService.impl;

import com.cy.driver.api.domain.res.SassOrderInfo;
import com.cy.driver.api.domain.res.SassOrderList;
import com.cy.driver.api.domain.res.SassOrderPageRes;
import com.cy.driver.cloudService.SaasOrderInfoService;
import com.cy.driver.common.util.DateUtil;
import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import com.cy.saas.basic.model.po.AccountUser;
import com.cy.saas.basic.model.po.SettingBusiness;
import com.cy.saas.basic.service.AccountUserService;
import com.cy.saas.basic.service.SettingBusinessService;
import com.cy.saas.business.model.dto.*;
import com.cy.saas.business.model.po.CarrierDetail;
import com.cy.saas.business.model.po.OrderInfo;
import com.cy.saas.business.model.po.OrderPart;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.saas.business.service.OrderOperService;
import com.cy.saas.business.service.OrderService;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by nixianjing on 17/5/15.
 */
@Service("saasOrderInfoService")
public class SaasOrderInfoServiceImpl implements SaasOrderInfoService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());


    @Resource
    private OrderService orderInfoService;

    @Resource
    private OrderOperService orderOperService;

    @Resource
    private AccountUserService accountUserService;

    @Resource
    private SettingBusinessService settingBusinessService;


    @Override
    public SassOrderPageRes pageRobForReceiving(Integer page, OrderInfoPageParamDTO paramDTO ) {
        PageInfo<OrderInfoPageParamDTO> pageInfo = new PageInfo<>(page,10);
        pageInfo.setData(paramDTO);
        PageResult<OrderRobDTO> pageResult = orderInfoService.pageRobForReceiving(pageInfo);
        if(!pageResult.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询抢单列表信息失败");
            return null;
        }
        SassOrderPageRes sassOrderRes = new SassOrderPageRes();
        sassOrderRes.setTotalNum(String.valueOf(pageResult.getTotalRecord()));
        sassOrderRes.setTotalPage(String.valueOf(pageResult.getTotalPage()));
        sassOrderRes.setListData(getSassOrderListPage(pageResult.getDataList()));
        return sassOrderRes;
    }
    public List<SassOrderList> getSassOrderListPage(List<OrderRobDTO> orderRobDTOList){
        List<SassOrderList> list = new ArrayList<>();
        for (OrderRobDTO orderRobDTO : orderRobDTOList) {
            /** 订单信息 */
            OrderInfo orderInfo = orderRobDTO.getOrderInfo();
            /** 参与方信息 */
            OrderPart orderPart =  orderRobDTO.getOrderPart();
            SassOrderList sassOrderList = new SassOrderList();
            sassOrderList.setOrderId(orderInfo.getId());
            sassOrderList.setOrderNum(orderInfo.getOrderNum());
            sassOrderList.setCarrierName(orderInfo.getCarrierName());
            sassOrderList.setTotalQuantity(orderInfo.getTotalQuantity());
            sassOrderList.setTotalWeight(orderInfo.getTotalWeight());
            sassOrderList.setTotalCubage(orderInfo.getTotalCubage());
            sassOrderList.setOrderType(orderInfo.getOrderType());
            sassOrderList.setExecuteState(orderInfo.getExecuteState());
            sassOrderList.setOrderPartId(orderPart.getId());
            sassOrderList.setValidityEnd(orderInfo.getValidityEnd());
            if(sassOrderList.getValidityEnd() != null) {
                sassOrderList.setValidityEndValue(DateUtil.getDatePoor(orderInfo.getValidityEnd(),new Date()));
            }
            sassOrderList.setQuoteState(orderInfo.getQuoteState());
            sassOrderList.setTotalFare(orderInfo.getTotalFare());
            sassOrderList.setNeedStartTime(orderInfo.getNeedStartTime());
            if(orderInfo.getNeedStartTime() != null) {
                sassOrderList.setNeedStartTimeValue(DateUtil.dateFormatTime(orderInfo.getNeedStartTime()));
            }
            sassOrderList.setDepartureProvinceValue(orderInfo.getDepartureProvinceValue());
            sassOrderList.setDepartureCityValue(orderInfo.getDepartureCityValue());
            sassOrderList.setDepartureCountyValue(orderInfo.getDepartureCountyValue());
            sassOrderList.setDepartureAddress(orderInfo.getDepartureAddress());
            sassOrderList.setReceiveProvinceValue(orderInfo.getReceiveProvinceValue());
            sassOrderList.setReceiveCityValue(orderInfo.getReceiveCityValue());
            sassOrderList.setReceiveCountyValue(orderInfo.getReceiveCountyValue());
            sassOrderList.setReceiveAddress(orderInfo.getReceiveAddress());
            sassOrderList.setCreateParentUserName(orderInfo.getCreateParentUserName());
            sassOrderList.setCreateParentUserId(orderInfo.getCreateParentUserId());
            sassOrderList.setTotalQuantityValue("件");
            sassOrderList.setTotalWeightValue("公斤");
            sassOrderList.setTotalCubageValue("方");
            Map<String,String> map =  getAccountUserDetails2(orderInfo.getCreateParentUserId());
            if(map != null) {
                sassOrderList.setSubmitType(map.get("submitType"));
                sassOrderList.setCompanyName(map.get("companyName"));
            }else {
                sassOrderList.setSubmitType("0");
            }
            list.add(sassOrderList);
        }
        return list;

    }

    @Override
    public SassOrderPageRes pageBidForReceiving(Integer page, OrderInfoPageParamDTO paramDTO ) {
        PageInfo<OrderInfoPageParamDTO> pageInfo = new PageInfo<>(page,10);
        pageInfo.setData(paramDTO);
        PageResult<OrderBidDTO> pageResult = orderInfoService.pageBidForReceiving(pageInfo);
        if(!pageResult.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询抢单列表信息失败");
            return null;
        }
        SassOrderPageRes sassOrderRes = new SassOrderPageRes();
        sassOrderRes.setTotalNum(String.valueOf(pageResult.getTotalRecord()));
        sassOrderRes.setTotalPage(String.valueOf(pageResult.getTotalPage()));
        sassOrderRes.setListData(getSassOrderListBidPage(pageResult.getDataList()));
        return sassOrderRes;
    }
    public List<SassOrderList> getSassOrderListBidPage(List<OrderBidDTO> orderBidDTOList){
        List<SassOrderList> list = new ArrayList<>();
        for (OrderBidDTO orderBidDTO : orderBidDTOList) {
            /** 订单信息 */
            OrderInfo orderInfo = orderBidDTO.getOrderInfo();
            /** 参与方信息 */
            OrderPart orderPart =  orderBidDTO.getOrderPart();
            SassOrderList sassOrderList = new SassOrderList();
            sassOrderList.setOrderId(orderInfo.getId());
            sassOrderList.setOrderNum(orderInfo.getOrderNum());
            sassOrderList.setCarrierName(orderInfo.getCarrierName());
            sassOrderList.setTotalQuantity(orderInfo.getTotalQuantity());
            sassOrderList.setTotalWeight(orderInfo.getTotalWeight());
            sassOrderList.setTotalCubage(orderInfo.getTotalCubage());
            sassOrderList.setOrderType(orderInfo.getOrderType());
            sassOrderList.setExecuteState(orderInfo.getExecuteState());
            sassOrderList.setOrderPartId(orderPart.getId());
            sassOrderList.setValidityEnd(orderInfo.getValidityEnd());
            if(orderInfo.getValidityEnd() != null) {
                sassOrderList.setValidityEndValue(DateUtil.getDatePoor(orderInfo.getValidityEnd(),new Date()));
            }
            sassOrderList.setQuoteState(orderInfo.getQuoteState());
            sassOrderList.setTotalFare(orderInfo.getTotalFare());
            sassOrderList.setNeedStartTime(orderInfo.getNeedStartTime());
            if(orderInfo.getNeedStartTime() != null) {
                sassOrderList.setNeedStartTimeValue(DateUtil.dateFormatTime(orderInfo.getNeedStartTime()));
            }
            sassOrderList.setDepartureProvinceValue(orderInfo.getDepartureProvinceValue());
            sassOrderList.setDepartureCityValue(orderInfo.getDepartureCityValue());
            sassOrderList.setDepartureCountyValue(orderInfo.getDepartureCountyValue());
            sassOrderList.setDepartureAddress(orderInfo.getDepartureAddress());
            sassOrderList.setReceiveProvinceValue(orderInfo.getReceiveProvinceValue());
            sassOrderList.setReceiveCityValue(orderInfo.getReceiveCityValue());
            sassOrderList.setReceiveCountyValue(orderInfo.getReceiveCountyValue());
            sassOrderList.setReceiveAddress(orderInfo.getReceiveAddress());
            sassOrderList.setMyQuote(orderPart.getQuote());
            sassOrderList.setMyQuoteState(orderPart.getQuoteState());
            sassOrderList.setTotalQuantityValue("件");
            sassOrderList.setTotalWeightValue("公斤");
            sassOrderList.setTotalCubageValue("方");
            if(orderBidDTO.getMinPrice() == null || orderBidDTO.getMinPrice().doubleValue() == 0) {
                sassOrderList.setMinPrice(orderInfo.getTotalFare());
            }else {
                sassOrderList.setMinPrice(orderBidDTO.getMinPrice());
            }
            sassOrderList.setCreateParentUserName(orderInfo.getCreateParentUserName());
            sassOrderList.setCreateParentUserId(orderInfo.getCreateParentUserId());
            Map<String,String> map =  getAccountUserDetails2(orderInfo.getCreateParentUserId());
            if(map != null) {
                sassOrderList.setSubmitType(map.get("submitType"));
                sassOrderList.setCompanyName(map.get("companyName"));
            }else {
                sassOrderList.setSubmitType("0");
            }
            list.add(sassOrderList);
        }
        return list;

    }
    @Override
    public SassOrderPageRes pagePartForMyQuote(Integer page, PagePartForMyQuoteDTO paramDTO) {
        PageInfo<PagePartForMyQuoteDTO> pageInfo = new PageInfo<>(page,10);
        pageInfo.setData(paramDTO);
        PageResult<OrderBidDTO> pageResult =  orderInfoService.pagePartForMyQuote(pageInfo);
        if(!pageResult.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询抢单列表信息失败");
            return null;
        }
        SassOrderPageRes sassOrderRes = new SassOrderPageRes();
        sassOrderRes.setTotalNum(String.valueOf(pageResult.getTotalRecord()));
        sassOrderRes.setTotalPage(String.valueOf(pageResult.getTotalPage()));
        sassOrderRes.setListData(getSassOrderListBidPage(pageResult.getDataList()));
        return sassOrderRes;
    }

    @Override
    public SassOrderInfo getDetailForRobBidByInfoId(Long id) {
        Response<OrderInfoDetailDTO> response = orderInfoService.getDetailForRobBidByInfoId(id);
        if(!response.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询抢单-竞价详情信息失败");
            return null;
        }
        return getSassOrderListBidInfo(response.getData());
    }

    @Override
    public SassOrderInfo getDetailForRobBidByPartId(Long partId) {
        Response<OrderInfoDetailDTO> response = orderInfoService.getDetailForRobBidByPartId(partId);
        if(!response.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询抢单-竞价详情信息失败");
            return null;
        }
        return getSassOrderListBidInfo(response.getData());
    }

    public SassOrderInfo getSassOrderListBidInfo(OrderInfoDetailDTO orderInfoDetailDTO){
        OrderInfo orderInfo = orderInfoDetailDTO.getOrderInfo();
        List<CarrierItemDTO> cargoList = orderInfoDetailDTO.getCarrierItemDTOList();
        OrderPart orderPart = orderInfoDetailDTO.getOrderPart();
        if(orderPart == null) {
            orderPart = new OrderPart();
        }
        BigDecimal minPrice = orderInfoDetailDTO.getMinPrice();
        SassOrderInfo sassOrderInfo = new SassOrderInfo();
        sassOrderInfo.setOrderId(orderInfo.getId());
        sassOrderInfo.setOrderNum(orderInfo.getOrderNum());
        sassOrderInfo.setCarrierName(orderInfo.getCarrierName());
        sassOrderInfo.setTotalQuantity(orderInfo.getTotalQuantity());
        sassOrderInfo.setTotalWeight(orderInfo.getTotalWeight());
        sassOrderInfo.setTotalCubage(orderInfo.getTotalCubage());
        sassOrderInfo.setOrderType(orderInfo.getOrderType());
        sassOrderInfo.setExecuteState(orderInfo.getExecuteState());
        sassOrderInfo.setOrderPartId(orderPart.getId());
        sassOrderInfo.setValidityEnd(orderInfo.getValidityEnd());
        if(sassOrderInfo.getValidityEnd() != null) {
            sassOrderInfo.setValidityEndValue(DateUtil.getDatePoor(orderInfo.getValidityEnd(),new Date()));
        }
        sassOrderInfo.setQuoteState(orderInfo.getQuoteState());
        sassOrderInfo.setTotalFare(orderInfo.getTotalFare());
        sassOrderInfo.setNeedStartTime(orderInfo.getNeedStartTime());
        if(orderInfo.getNeedStartTime() != null) {
            sassOrderInfo.setNeedStartTimeValue(DateUtil.dateFormatTime(orderInfo.getNeedStartTime()));
        }
        sassOrderInfo.setNeedEndTime(orderInfo.getNeedEndTime());
        if(orderInfo.getNeedEndTime() != null) {
            sassOrderInfo.setNeedEndTimeValue(DateUtil.dateFormatTime(orderInfo.getNeedEndTime()));
        }
        sassOrderInfo.setDepartureProvinceValue(orderInfo.getDepartureProvinceValue());
        sassOrderInfo.setDepartureCityValue(orderInfo.getDepartureCityValue());
        sassOrderInfo.setDepartureCountyValue(orderInfo.getDepartureCountyValue());
        sassOrderInfo.setDepartureAddress(orderInfo.getDepartureAddress());
        sassOrderInfo.setReceiveProvinceValue(orderInfo.getReceiveProvinceValue());
        sassOrderInfo.setReceiveCityValue(orderInfo.getReceiveCityValue());
        sassOrderInfo.setReceiveCountyValue(orderInfo.getReceiveCountyValue());
        sassOrderInfo.setReceiveAddress(orderInfo.getReceiveAddress());
        sassOrderInfo.setMyQuote(orderPart.getQuote());
        sassOrderInfo.setMyQuoteState(orderPart.getQuoteState());
        if(minPrice == null || minPrice.doubleValue() == 0) {
            sassOrderInfo.setMinPrice(orderInfo.getTotalFare());
        }else {
            sassOrderInfo.setMinPrice(minPrice);
        }
        sassOrderInfo.setCreateUserId(orderInfo.getCreateUserId());
        sassOrderInfo.setCreateUserName(orderInfo.getCreateUserName());
        sassOrderInfo.setCreateUserMobile(orderInfo.getCreateUserMobile());
        sassOrderInfo.setCreateUserHeadImg(orderInfo.getCreateUserHeadImg());
        sassOrderInfo.setCreateParentUserName(orderInfo.getCreateParentUserName());
        sassOrderInfo.setCreateParentUserId(orderInfo.getCreateParentUserId());
        Map<String,String> map =  getAccountUserDetails2(orderInfo.getCreateParentUserId());
        if(map != null) {
            sassOrderInfo.setSubmitType(map.get("submitType"));
            sassOrderInfo.setCompanyName(map.get("companyName"));
        }else {
            sassOrderInfo.setSubmitType("0");
        }
        sassOrderInfo.setCollectionPayment(orderInfo.getTotalCollectionPayment());
        sassOrderInfo.setCargoList(getCarrierDetail(cargoList));
        return sassOrderInfo;
    }

    public List<CarrierDetail> getCarrierDetail(List<CarrierItemDTO> cargoList){
        List<CarrierDetail> list = new ArrayList<CarrierDetail>();
        for(CarrierItemDTO carrierItemDTO : cargoList){
            list.add(carrierItemDTO.getCarrierDetail());
        }
        return list;
    }


    @Override
    public OrderPart getOrderPart(Long partId) {
        Response<OrderPart> response =  orderInfoService.getOrderPart(partId);
        if(!response.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用search服务查询抢单-竞价详情信息失败");
            return null;
        }
        return response.getData();
    }

    /**
     * 根据账户用户id获得帐户用户详情二
     * @param userId 账户用户id，必填
     * @return {@link Response.CodeTable#ERROR} 参数错误<br/>
     *         {@link Response.CodeTable#EXCEPTION} 系统异常<br/>
     */
    public Map<String,String> getAccountUserDetails2(Long userId) {
        Response<AccountUserDetails2DTO> response = accountUserService.getAccountUserDetails2(userId);
        if(!response.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用saas-accountUserService服务查帐户用户详情二信息失败");
            return null;
        }
        AccountUser accountUser = response.getData().getAccountUser();
        Map<String,String> map = new HashMap<String,String>();
        map.put("submitType",accountUser.getSubmitType().toString());
        map.put("companyName",accountUser.getName());
        return map;
    }


    @Override
    public Response<WaybillInfo> saveByGrabSingle(SaveGrabSingleDTO saveGrabSingleDTO) {
        Response<WaybillInfo> response = orderOperService.saveByGrabSingle(saveGrabSingleDTO);
        return response;
    }

    @Override
    public Response modifyOrderQuote(ModifyOrderQuoteDTO modifyOrderQuoteDTO) {
        Response response = orderOperService.modifyOrderQuote(modifyOrderQuoteDTO);
        return response;
    }

    @Override
    public SettingBusiness getByUserId(Long userId) {
        Response<SettingBusiness> response = settingBusinessService.getByUserId(userId);
        if(!response.isSuccess()) {
            if(LOG.isDebugEnabled()) LOG.debug("调用saas-accountUserService服务查帐户用户详情二信息失败");
            return null;
        }
        return response.getData();
    }
}
