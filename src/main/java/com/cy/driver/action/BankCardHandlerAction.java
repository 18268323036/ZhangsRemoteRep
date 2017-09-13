package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.AccountBankBO;
import com.cy.driver.domain.BankBO;
import com.cy.driver.service.BankCardHandlerService;
import com.cy.platformpay.service.dto.AccountBankDTO;
import com.cy.platformpay.service.dto.BankDTO;
import com.cy.platformpay.service.dto.base.CodeTable;
import com.cy.platformpay.service.dto.base.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/10.
 */
@Scope("prototype")
@Controller("bankCardHandlerAction")
public class BankCardHandlerAction extends BaseAction{

    private Logger LOG = LoggerFactory.getLogger(BankCardHandlerAction.class);

    @Resource
    private BankCardHandlerService bankCardHandlerService;

    /**
     * 删除提现银行卡
     *
     * @param request
     * @param response
     * @param bankId  银行卡ID
     * @return
     */
    @RequestMapping(value = "/deleteMyBankCardInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.DELETE_MYBANKCARD_INFO)
    @Log(type = LogEnum.DELETE_MYBANK_CARDINFO)
    public Object deleteMyBankCardInfo(HttpServletRequest request, HttpServletResponse response, String bankId) {
        try {
            if (StringUtils.isEmpty(bankId)) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            Response<Boolean> resultData = bankCardHandlerService.delete(Long.valueOf(bankId),findcapitalAccountId(request));
            if(resultData.isSuccess()){
                updRespHeadSuccess(response);
                return resultMap;
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("删除提现银行卡出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


    /**
     * 新增我的银行卡
     *
     * @param request
     * @param response
     * @param bankCard  银行卡号
     * @param bankCode 银行码表行号
     * @param accountName  用户名称()
     * @param bankCardType 银行卡类型(储蓄卡、信用卡)
     *
     * @return
     */
    @RequestMapping(value = "/addMyBankCardInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_MYBANKCARD_INFO)
    @Log(type = LogEnum.ADD_MYBANK_CARDINFO)
    public Object addMyBankCardInfo(HttpServletRequest request, HttpServletResponse response, String bankCard, String bankCode, String accountName, String bankCardType) {
        try {

            if (StringUtils.isEmpty(bankCard) || StringUtils.isEmpty(bankCode) || StringUtils.isEmpty(accountName) || StringUtils.isEmpty(bankCardType)) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            Response<Boolean> resultData = bankCardHandlerService.driverTie(findcapitalAccountId(request), bankCode, bankCard.replace(" ",""), accountName);
            if(resultData.isSuccess()){
                updRespHeadSuccess(response);
                return resultMap;
            }
            if(resultData.getCode() == CodeTable.DATA_HAS_EXIST.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20218);
            }
            if(resultData.getCode() == CodeTable.ERROR_BANK_NOT_SUPPORT.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20218);
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("新增我的银行卡出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 查看我的银行卡信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/findMyBankCardInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.FIND_MY_BANKCARD_INFO)
    @Log(type = LogEnum.FIND_MYBANK_CARDINFO)
    public Object findMyBankCardInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<AccountBankDTO> list = bankCardHandlerService.listByAccountId(findcapitalAccountId(request));
            List<AccountBankBO> bankBOList = convertAccountBank(list);
            resultMap.put("bankCardList", bankBOList);
            updRespHeadSuccess(response);
            return resultMap;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("查看我的银行卡信息出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    public List<AccountBankBO> convertAccountBank(List<AccountBankDTO> list){
        List<AccountBankBO> accountBankBOList = new ArrayList<AccountBankBO>();
        if(list != null && list.size() > 0) {
            for (AccountBankDTO accountBankDTO : list) {
                AccountBankBO accountBankBO = new AccountBankBO();
                accountBankBO.setBankId(accountBankDTO.getId());
                accountBankBO.setUsername(accountBankDTO.getAccountName());
                accountBankBO.setBankCard(SystemsUtil.hideBankCard(accountBankDTO.getDecodeAccountCode()));
                accountBankBO.setAccountName(accountBankDTO.getBankName());
                accountBankBO.setBankCardType("0");
                accountBankBO.setImg(accountBankDTO.getIconImgPath());
                accountBankBOList.add(accountBankBO);
            }
        }
        return accountBankBOList;
    }


    /**
     * 银行码表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/bankCodeList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.BANK_CODE_LIST)
    @Log(type = LogEnum.BANK_CODE_LIST)
    public Object bankCodeList(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            Response<List<BankDTO>> resultData = bankCardHandlerService.listMainBank();
            if(resultData.isSuccess()){
                List<BankBO> bankBOList = convertBank(resultData.getData());
                resultMap.put("bankCodeList", bankBOList);
                updRespHeadSuccess(response);
                return resultMap;
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("获取银行码表出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    public List<BankBO> convertBank(List<BankDTO> list){
        List<BankBO> bankBOList = new ArrayList<BankBO>();
        if(list != null && list.size() > 0) {
            for (BankDTO bankDTO : list) {
                BankBO bankBO = new BankBO();
                bankBO.setBankId(bankDTO.getId());
                bankBO.setBankCode(bankDTO.getBankCode());
                bankBO.setBankName(bankDTO.getBankName());
                bankBOList.add(bankBO);
            }
        }
        return bankBOList;
    }

}
