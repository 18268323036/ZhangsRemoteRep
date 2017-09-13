package com.cy.driver.saasService.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cy.driver.domain.PageBase;
import com.cy.driver.common.enumer.UtmsBusinessEventEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.saasPay.PayUserBillInfo;
import com.cy.driver.saasService.SaasPayUserService;
import com.cy.saas.pay.model.dto.*;
import com.cy.saas.pay.model.enums.BillTypeEnum;
import com.cy.saas.pay.model.enums.BusinessEventEnum;
import com.cy.saas.pay.model.enums.PayUserTypeEnum;
import com.cy.saas.pay.model.po.PayUserMember;
import com.cy.saas.pay.service.PayUserService;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nixianjing on 17/8/17.
 */
@Service("saasPayUserService")
public class SaasPayUserServiceImpl implements SaasPayUserService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PayUserService utmsPayUserService;


    @Override
    public boolean accountWhetherClear(String userNature, String userId) {
        PayUserDTO payUserDTO = new PayUserDTO();
        payUserDTO.setUserId(userId);
        payUserDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 验证用户是否开户
         * @param payUserDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *
         *         {@link Response#data==true 已开户} {@link Response#data==false 未开户}<br/>
         */
        Response<Boolean> response = utmsPayUserService.validUserIsOpenAccount(payUserDTO);
        if(response.isSuccess()) {
            return response.getData();
        }
        LOG.error("验证用户是否开户失败!入参payUserDTO={},回参code={},message={}", JSONObject.toJSONString(payUserDTO),response.getCode(),response.getMessage());
        return false;
    }

    @Override
    public boolean resetTradePassword(String tradePassword, String userId) {
        TradePasswordModifyDTO modifyDTO = new TradePasswordModifyDTO();
        TradePasswordDTO tradePasswordDTO = new TradePasswordDTO();
        tradePasswordDTO.setTradePassword(tradePassword);
        tradePasswordDTO.setPasswordIsEncrypt(true);
        modifyDTO.setTradePasswordDTO(tradePasswordDTO);
        modifyDTO.setUserId(userId);
        modifyDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 重置交易密码
         * @param modifyDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 未开通资金帐户<br/>
         *         {@link Response.CodeTable#UPDATE_FAIL1} 修改失败<br/>
         */
        Response response = utmsPayUserService.resetTradePassword(modifyDTO);
        if(response.isSuccess()) {
            return true;
        }
        LOG.error("司机重置交易密码失败!入参modifyDTO={},回参code={},message={}", JSONObject.toJSONString(modifyDTO),response.getCode(),response.getMessage());
        return false;
    }

    @Override
    public boolean saveForOpenAccount(String userNature,String name,String mobile,String userId, String password) {
        PayUserMemberSaveDTO saveDTO = new PayUserMemberSaveDTO();
        saveDTO.setUserNature(userNature);
        saveDTO.setName(name);
        saveDTO.setMobile(mobile);
        saveDTO.setUserId(userId);
        saveDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        if(StringUtils.isNotEmpty(password)) {
            TradePasswordDTO tradePasswordDTO = new TradePasswordDTO();
            tradePasswordDTO.setPasswordIsEncrypt(true);
            tradePasswordDTO.setTradePassword(password);
            saveDTO.setTradePasswordDTO(tradePasswordDTO);
        }
        /**
         * 开户-并且开通基本资金帐户
         * @param saveDTO 开户信息，必填
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#LOCK_FAIL} 锁定失败<br/>
         *         {@link Response.CodeTable#LOCK_WAIT} 锁定等待<br/>
         *         {@link Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
         *         {@link Response.CodeTable#FAIL1} 失败<br/>
         */
        Response<PayUserMember> response = utmsPayUserService.saveForOpenAccount(saveDTO);
        if(response.isSuccess()) {
            return true;
        }
        LOG.error("司机开通基本户失败!入参saveDTO={},回参code={},message={}", JSONObject.toJSONString(saveDTO),response.getCode(),response.getMessage());
        return false;
    }

    @Override
    public AccountBalanceDTO listAccountBalance(String accountType, String userId) {
        AccountBalanceQueryDTO queryDTO = new AccountBalanceQueryDTO();
        queryDTO.setAccountType(accountType);
        queryDTO.setUserId(userId);
        queryDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 查询账户余额列表
         * @param queryDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 支付会员信息或者资金帐户不存在<br/>
         *         {@link Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
         *         {@link Response.CodeTable#FAIL1} 失败<br/>
         */
        Response<List<AccountBalanceDTO>> response = utmsPayUserService.listAccountBalance(queryDTO);
        if(response.isSuccess()) {
            return response.getData().get(0);
        }
        LOG.error("查询UTMS基本户余额失败!入参queryDTO={},回参code={},message={}", JSONObject.toJSONString(queryDTO),response.getCode(),response.getMessage());
        return null;
    }

    @Override
    public UserSafetyDTO getUserSafety(String userId) {
        PayUserDTO payUserDTO = new PayUserDTO();
        payUserDTO.setUserId(userId);
        payUserDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 获得用户安全设置信息
         * @param payUserDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 未开通资金帐户<br/>
         */
        Response<UserSafetyDTO> response = utmsPayUserService.getUserSafety(payUserDTO);
        if(response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    @Override
    public PageBase<PayUserBillInfo> pageBillDetail(String businessEvent, String startTime, String endTime, String userId, Integer pageIndex) {
        PageInfo<BillDetailQDTO> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(10);
        pageInfo.setPageIndex(pageIndex);
        BillDetailQDTO billDetailQDTO = new BillDetailQDTO();
        billDetailQDTO.setUserId(userId);
        billDetailQDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        if(StringUtils.isNotEmpty(startTime)) {
            billDetailQDTO.setStartTime(DateUtil.strToDate(startTime));
        }
        if(StringUtils.isNotEmpty(endTime)) {
            billDetailQDTO.setEndTime(DateUtil.strToDate(endTime));
        }
        if(UtmsBusinessEventEnum.FREIGHT.getCode().equals(businessEvent)) {//运费
            /** 账单类型 {@link BillTypeEnum#code} */
            List<String> billTypeList = new ArrayList<>();
            billTypeList.add(BillTypeEnum.PAYMENT.getCode());
            billTypeList.add(BillTypeEnum.COLLECTION.getCode());
            billTypeList.add(BillTypeEnum.REFUND.getCode());
            billDetailQDTO.setBillTypeList(billTypeList);
            /** 业务事件 {@link BusinessEventEnum#code} */
            List<String> businessEventList = new ArrayList<>();
            businessEventList.add(BusinessEventEnum.FREIGHT.getCode());
            billDetailQDTO.setBusinessEventList(businessEventList);
        }else if(UtmsBusinessEventEnum.WITHDRAWAL.getCode().equals(businessEvent)) {//提现
            /** 账单类型 {@link BillTypeEnum#code} */
            List<String> billTypeList = new ArrayList<>();
            billTypeList.add(BillTypeEnum.WITHDRAWAL.getCode());
            billDetailQDTO.setBillTypeList(billTypeList);
            /** 业务事件 {@link BusinessEventEnum#code} */
            List<String> businessEventList = new ArrayList<>();
            businessEventList.add(BusinessEventEnum.WITHDRAWAL.getCode());
            billDetailQDTO.setBusinessEventList(businessEventList);
        }else if(UtmsBusinessEventEnum.PROC_FEE.getCode().equals(businessEvent)) {//手续费
            /** 账单类型 {@link BillTypeEnum#code} */
            List<String> billTypeList = new ArrayList<>();
            billTypeList.add(BillTypeEnum.THIRD_PROC_FEE.getCode());
            billTypeList.add(BillTypeEnum.PROC_FEE.getCode());
            billDetailQDTO.setBillTypeList(billTypeList);
            /** 业务事件 {@link BusinessEventEnum#code} */
            List<String> businessEventList = new ArrayList<>();
            businessEventList.add(BusinessEventEnum.FREIGHT.getCode());
            businessEventList.add(BusinessEventEnum.WITHDRAWAL.getCode());
            billDetailQDTO.setBusinessEventList(businessEventList);
        }
        pageInfo.setData(billDetailQDTO);
        /**
         * 分页查询账单明细
         * @param pageInfo
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         */
        PageResult<PayUserBillDetailDTO> pageResult = utmsPayUserService.pageBillDetail(pageInfo);
        if(pageResult.isSuccess()) {
            PageBase<PayUserBillInfo> pageBase = new PageBase<>();
            pageBase.setTotalNum(pageResult.getTotalRecord());
            pageBase.setTotalPage(pageResult.getTotalPage());
            pageBase.setListData(getPayUserBillInfoList(pageResult.getDataList()));
            return pageBase;
        }
        return null;
    }


    public List<PayUserBillInfo> getPayUserBillInfoList(List<PayUserBillDetailDTO> list){
        List<PayUserBillInfo> utmsList = new ArrayList<>();
        for(PayUserBillDetailDTO payUserBillDetailDTO : list) {
            PayUserBillInfo payUserBillInfo = new PayUserBillInfo();
            payUserBillInfo.setFundFlow(payUserBillDetailDTO.getFundFlow());
            payUserBillInfo.setAmount(payUserBillDetailDTO.getAmount());
            payUserBillInfo.setSubject(payUserBillDetailDTO.getSubject());
            /** 实际发生时间,not null */
            payUserBillInfo.setRealityTime(DateUtil.dateTimeToStr(payUserBillDetailDTO.getRealityTime()));
            payUserBillInfo.setRealityTimeYm(payUserBillDetailDTO.getRealityTimeYm());
            String businessEventValue = "";
            if(BusinessEventEnum.FREIGHT.getCode().equals(payUserBillDetailDTO.getBusinessEvent())) {//运费
                if(BillTypeEnum.PAYMENT.getCode().equals(payUserBillDetailDTO.getBillType())) {//付款
                    businessEventValue = "运费支出";
                }else if(BillTypeEnum.COLLECTION.getCode().equals(payUserBillDetailDTO.getBillType())) {//收款
                    businessEventValue = "运费收入";
                }else if(BillTypeEnum.REFUND.getCode().equals(payUserBillDetailDTO.getBillType())) {//退款
                    businessEventValue = "运费退款";
                }else if(BillTypeEnum.THIRD_PROC_FEE.getCode().equals(payUserBillDetailDTO.getBillType())) {//第三方手续费
                    businessEventValue = "手续费";
                }else if(BillTypeEnum.PROC_FEE.getCode().equals(payUserBillDetailDTO.getBillType())) {//手续费
                    businessEventValue = "手续费";
                }
            }else if(BusinessEventEnum.WITHDRAWAL.getCode().equals(payUserBillDetailDTO.getBusinessEvent())) {//提现
                if(BillTypeEnum.WITHDRAWAL.getCode().equals(payUserBillDetailDTO.getBillType())) {//提现
                    businessEventValue = "提现";
                }else if(BillTypeEnum.THIRD_PROC_FEE.getCode().equals(payUserBillDetailDTO.getBillType())) {//第三方手续费
                    businessEventValue = "手续费";
                }else if(BillTypeEnum.PROC_FEE.getCode().equals(payUserBillDetailDTO.getBillType())) {//手续费
                    businessEventValue = "手续费";
                }
            }else if(BusinessEventEnum.RECHARGE.getCode().equals(payUserBillDetailDTO.getBusinessEvent())) {//充值
                if(BillTypeEnum.ONLINE_RECHARGE.getCode().equals(payUserBillDetailDTO.getBillType())) {//线上充值
                    businessEventValue = "线上充值";
                }else if(BillTypeEnum.OFFLINE_RECHARGE.getCode().equals(payUserBillDetailDTO.getBillType())) {//线下充值
                    businessEventValue = "线下充值";
                }
            }
            payUserBillInfo.setBusinessEventValue(businessEventValue);
            utmsList.add(payUserBillInfo);
        }
        return utmsList;
    }
}
