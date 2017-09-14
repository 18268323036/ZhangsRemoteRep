package com.cy.driver.api;

import com.cy.driver.action.BaseAction;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.saasPay.PayUserBankInfo;
import com.cy.driver.saasService.SaasPayBankService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.saas.pay.model.dto.BindBankDTO;
import com.cy.saas.pay.model.dto.PayUserBankDTO;
import com.cy.saas.pay.model.enums.CardAttributeEnum;
import com.cy.saas.pay.model.enums.CardTypeEnum;
import com.cy.saas.pay.model.enums.PayUserTypeEnum;
import com.cy.saas.pay.model.po.PayBank;
import com.cy.saas.pay.model.po.PayMainBank;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UTMS银行卡管理
 * Created by nixianjing on 17/8/17.
 */
@Scope("prototype")
@RestController
public class SaasPayBankController extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(SaasPayBankController.class);

    @Resource
    private SaasPayBankService saasPayBankService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;



    /**
     * 根据银行卡号获取银行信息(总行信息)
     * @param cardNo 银行卡号
     * @return
     */
    @RequestMapping(value = "/getUtmsMainBankByCardNo", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_UTMS_MAIN_BANK_BY_CARD_NO)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object getUtmsMainBankByCardNo(String cardNo) {
        if(StringUtils.isEmpty(cardNo)) {
            //不能为空
        }
        /**
         * 根据银行卡号获取银行信息(总行信息)
         * @param cardNo 银行卡号 必填
         * @return
         */
        Response<PayMainBank> response1 = saasPayBankService.getMainBankByCardNo(cardNo);
        if(response1.isSuccess()) {
            updRespHeadSuccess(response);
            return response1.getData();
        }
        return findException(response);
    }


    /**
     * 分页查询总行
     * @param bankName 总行名称 (支持模糊查询)
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/pageUtmsMainBankList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_UTMS_MAIN_BANK_LIST)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object pageUtmsMainBankList(String bankName,Integer page) {
        PageBase<PayMainBank> pageBase = saasPayBankService.pageMainBank(bankName,page);
        if(pageBase == null) {
            return findException(response);
        }
        updRespHeadSuccess(response);
        return pageBase;
    }



    /**
     * 分页查询支行
     * @param agentBankCode 法人行号（网银互联号），必填
     * @param bankName 支行名称 (支持模糊查询)
     * @param page 页码
     * @return
     */
    @RequestMapping(value = "/pageUtmsBranchBankList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_UTMS_BRANCH_BANK_LIST)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object pageUtmsBranchBankList(String agentBankCode,String bankName,Integer page) {
        if(StringUtils.isEmpty(agentBankCode)) {
            //不能为空
        }
        PageBase<PayBank> pageBase = saasPayBankService.pageBranchBank(agentBankCode,bankName,page);
        if(pageBase == null) {
            return findException(response);
        }
        updRespHeadSuccess(response);
        return pageBase;
    }

    /**
     * 绑卡
     * @param bankNo 总行的联行号，必填
     * @param branchNo 支行号（联行号），必填
     * @param bankAccountNo 银行卡号，必填
     * @param accountName 开户名，必填
     * @return
     */
    @RequestMapping(value = "/saveUtmsBindBank", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAVE_UTMS_BIN_BANK)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object saveUtmsBindBank(String bankNo,String branchNo,String bankAccountNo,String accountName) {
        if(StringUtils.isEmpty(bankNo) || StringUtils.isEmpty(branchNo)
                || StringUtils.isEmpty(bankAccountNo) || StringUtils.isEmpty(accountName)) {
            //不能为空
        }
        if(findAuthState()!=3){
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20217);
        }else{
            DriverUserInfoDTO userInfoDTO = driverUserHandlerService.getDriverInfo(findUserId());
            if(userInfoDTO.getName()!=accountName){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30512);
            }
        }
        BindBankDTO bindBankDTO = new BindBankDTO();
        bindBankDTO.setBankNo(bankNo);
        bindBankDTO.setBranchNo(branchNo);
        bindBankDTO.setBankAccountNo(bankAccountNo);
        bindBankDTO.setAccountName(accountName);
        bindBankDTO.setCardType(CardTypeEnum.DC.getCode());
        bindBankDTO.setCardAttribute(CardAttributeEnum.C.getCode());
        bindBankDTO.setUserId(findUserId().toString());
        bindBankDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        if(saasPayBankService.bindBank(bindBankDTO)) {
            updRespHeadSuccess(response);
            return null;
        }
        return findException(response);
    }

    /**
     * 解绑银行卡
     * @param bankId 银行卡ID
     * @return
     */
    @RequestMapping(value = "/deleteUtmsUnBindBank", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.DELETE_UTMS_UN_BIN_BANK)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object deleteUtmsUnBindBank(String bankId) {
        if(StringUtils.isEmpty(bankId)) {
            //不能为空
        }
        if(saasPayBankService.unbindBank(bankId,findUserId().toString())) {
            updRespHeadSuccess(response);
            return null;
        }
        return findException(response);
    }

    /**
     * 获取银行卡列表
     * @return
     */
    @RequestMapping(value = "/getUtmsListQueryBank", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_UTMS_QUERY_BANK_LIST)
    @ResponseBody
    @Log(type = LogEnum.FIND_SAAS_ORDER_PAGE)
    public Object getUtmsListQueryBank() {
        try {
            List<PayUserBankInfo> bankList = new ArrayList<>();
            List<PayUserBankDTO> list = saasPayBankService.listQueryBank(findUserId().toString());
            if(list != null) {
                for(PayUserBankDTO payUserBankDTO : list) {
                    PayUserBankInfo payUserBankInfo = new PayUserBankInfo();
                    payUserBankInfo.setCardType(payUserBankDTO.getPayUserBank().getCardType());
                    payUserBankInfo.setBankCode(payUserBankDTO.getPayUserBank().getBankCode());
                    payUserBankInfo.setBankId(payUserBankDTO.getPayUserBank().getBankId());
                    payUserBankInfo.setBankName(payUserBankDTO.getPayUserBank().getBankName());
                    payUserBankInfo.setIconImgPath(payUserBankDTO.getIconImgPath());
                    payUserBankInfo.setBankAccountNo(payUserBankDTO.getPayUserBank().getBankAccountNo().substring(payUserBankDTO.getPayUserBank().getBankAccountNo().length()-3,payUserBankDTO.getPayUserBank().getBankAccountNo().length()));
                    bankList.add(payUserBankInfo);
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("listData",bankList);
            updRespHeadSuccess(response);
            return map;
        }catch (Exception e) {
            LOG.error("获取银行卡列表异常,e={}",e);
        }
        return findException(response);
    }



}
