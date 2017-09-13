package com.cy.driver.api;

import com.cy.driver.action.BaseAction;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.enumer.UtmsBusinessEventEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.saasPay.EnumInfo;
import com.cy.driver.domain.saasPay.PayUserBillInfo;
import com.cy.driver.saasService.SaasPayBankService;
import com.cy.driver.saasService.SaasPayMybankTradeService;
import com.cy.driver.saasService.SaasPayUserService;
import com.cy.driver.service.MainBmikeceService;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import com.cy.platformpay.service.dto.base.Response;
import com.cy.saas.pay.model.dto.AccountBalanceDTO;
import com.cy.saas.pay.model.dto.PayUserBankDTO;
import com.cy.saas.pay.model.dto.UserSafetyDTO;
import com.cy.saas.pay.model.enums.PayAccountTypeEnum;
import com.cy.saas.pay.model.enums.PayUserNatureEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UTMS资金管理
 * Created by nixianjing on 17/8/17.
 */
@Scope("prototype")
@Controller
public class SaasPayController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(SaasPayController.class);

    @Resource
    private SaasPayUserService saasPayUserService;

    @Resource
    private MainBmikeceService mainBmikeceService;
    @Resource
    private SaasPayBankService saasPayBankService;
    @Resource
    private SaasPayMybankTradeService saasPayMybankTradeService;



    /**
     * 我的首页资金余额
     * @return
     */
    @RequestMapping(value = "/queryMyUserAccountInfo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_MY_USER_ACCOUNT_INFO)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object queryUserAccountInfo() {
        try {
            Map<String,String> map = new HashMap<>();
            /**
             * 总资产
             */
            BigDecimal amountPay = new BigDecimal("0.00");
            /**
             * 快到网余额
             */
            BigDecimal kdwPayAmount = new BigDecimal("0.00");
            /**
             * UTMS余额
             */
            BigDecimal utmsPayAmount = new BigDecimal("0.00");
            /**
             * UTMS账户是否开通 0未开通 1已开通
             */
            String utmsPayState = "0";
            /**
             * 快到网
             */
            Response<CapitalAccountDTO> resultTotalMoney = mainBmikeceService.queryAccountInfo(findcapitalAccountId());
            BigDecimal luckyMoney = new BigDecimal("0.00");
            BigDecimal totalMoney = new BigDecimal("0.00");
            if(resultTotalMoney.isSuccess()) {
                if(resultTotalMoney.getData() != null){
                    luckyMoney = resultTotalMoney.getData().getLuckyMoney() == null ?luckyMoney:resultTotalMoney.getData().getLuckyMoney();
                    totalMoney = resultTotalMoney.getData().getTotalMoney() == null ?totalMoney:resultTotalMoney.getData().getTotalMoney();
                    kdwPayAmount = totalMoney.add(luckyMoney);
                }
            }
            /**
             * UTMS账户
             */
            if(saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                utmsPayState = "1";
                AccountBalanceDTO accountBalanceDTO = saasPayUserService.listAccountBalance(PayAccountTypeEnum.BASIC.getCode(),findUserId().toString());
                utmsPayAmount = accountBalanceDTO.getAvailableBalance();
            }
            amountPay = kdwPayAmount.add(utmsPayAmount);
            map.put("amountPay",amountPay.toString());
            map.put("kdwPayAmount",kdwPayAmount.toString());
            map.put("utmsPayAmount",utmsPayAmount.toString());
            map.put("utmsPayState",utmsPayState);
            updRespHeadSuccess(response);
            return map;
        }catch (Exception e) {
            LOG.error("我的首页资金余额查询异常,e={}",e);
        }
        return findException(response);
    }



    /**
     * 查询utms钱包首页
     * @return
     */
    @RequestMapping(value = "/queryMyUtmsAccountInfo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_MY_UTMS_ACCOUNT_INFO)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object queryMyUtmsAccountInfo() {
        try {
            Map<String,Object> map = new HashMap<>();
            /**
             * UTMS余额
             */
            BigDecimal utmsPayAmount = new BigDecimal("0.00");
            /**
             * UTMS账户是否开通 0未开通 1已开通
             */
            Integer bankCount = 0;
            /**
             * 是否设置交易密码 0未设置 1已设置
             */
            Integer tradePasswordIsSet = 0;
            /**
             * UTMS账户
             */
            if(saasPayUserService.accountWhetherClear(PayUserNatureEnum.PERSON.getCode(),findUserId().toString())) {
                AccountBalanceDTO accountBalanceDTO = saasPayUserService.listAccountBalance(PayAccountTypeEnum.BASIC.getCode(),findUserId().toString());
                utmsPayAmount = accountBalanceDTO.getAvailableBalance();
            }
            UserSafetyDTO userSafetyDTO = saasPayUserService.getUserSafety(findUserId().toString());
            if(userSafetyDTO != null) {
                if(userSafetyDTO.getTradePasswordIsSet()) {
                    tradePasswordIsSet = 1;
                }
            }
            /**
             * 查询银行卡列表
             */
            List<PayUserBankDTO> bankList = saasPayBankService.listQueryBank(findUserId().toString());
            if(bankList != null) {
                bankCount = bankList.size();
            }
            map.put("utmsPayAmount",utmsPayAmount.toString());
            map.put("bankCount",bankCount);
            map.put("tradePasswordIsSet",tradePasswordIsSet);
            updRespHeadSuccess(response);
            return map;
        }catch (Exception e) {
            LOG.error("查询utms钱包首页异常,e={}",e);
        }
        return findException(response);
    }


    /**
     * 获取UTMS账单业务类型
     * @return
     */
    @RequestMapping(value = "/getUtmsBusinessEventEnum", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_UTMS_BUSINESS_ENEN_ENUM)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object getUtmsBusinessEventEnum() {
        List<EnumInfo> list = new ArrayList<>();
        list.add(new EnumInfo(UtmsBusinessEventEnum.FREIGHT.getCode(), UtmsBusinessEventEnum.FREIGHT.getName()));
        list.add(new EnumInfo(UtmsBusinessEventEnum.WITHDRAWAL.getCode(), UtmsBusinessEventEnum.WITHDRAWAL.getName()));
        list.add(new EnumInfo(UtmsBusinessEventEnum.PROC_FEE.getCode(), UtmsBusinessEventEnum.PROC_FEE.getName()));
        Map<String,Object> map = new HashMap<>();
        map.put("listData",list);
        updRespHeadSuccess(response);
        return map;
    }


    /**
     * UTMS账单分页查询
     * @param businessEvent 账单类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页面
     * @return
     */
    @RequestMapping(value = "/pageUtmsBillDetailList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_UTMS_BULL_DETAIL_LIST)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object pageUtmsBillDetailList(String businessEvent,String startTime,String endTime,Integer page) {
        PageBase<PayUserBillInfo> pageBase = saasPayUserService.pageBillDetail(businessEvent,startTime,endTime,findUserId().toString(),page);
        if(pageBase == null) {
            return findException(response);
        }
        updRespHeadSuccess(response);
        return pageBase;
    }




    /**
     * UTMS获取提现手续费
     * @param bankId 银行卡ID
     * @param amount 提现金额
     * @return
     */
    @RequestMapping(value = "/getSaasWithdrawalProcFee", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_SAAS_WITHDRAWAL_PROC_FEE)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object getSaasWithdrawalProcFee(String bankId,BigDecimal amount) {
        BigDecimal handlingFee = saasPayMybankTradeService.getWithdrawalProcFee(bankId,amount);
        if(handlingFee == null) {
            return findException(response);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("handlingFee",handlingFee);
        updRespHeadSuccess(response);
        return map;
    }
}
