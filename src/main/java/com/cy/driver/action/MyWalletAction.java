package com.cy.driver.action;

import com.cy.award.service.dto.AwardAccountDTO;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.MyWalletInfo;
import com.cy.driver.service.*;
import com.cy.platformpay.service.dto.AccountPassWdDTO;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author yanst 2016/4/19 19:34
 */
@Scope("prototype")
@RestController("myWalletAction")
public class MyWalletAction extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(MyWalletAction.class);

    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private AccountService accountService;
    @Resource
    private BankCardHandlerService bankCardHandlerService;
    @Resource
    private MainBmikeceService mainBmikeceService;
    @Resource
    private IntegrationService integrationService;
    /**
     * 我的钱包信息
     *
     * @return
     */
    @RequestMapping(value = "/myWallet", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MY_WALLET_INFO)
    @Log(type = LogEnum.MY_WALLET_INFO)
    public Object myWalletInfo(HttpServletResponse response) {
        try {
            Long userId = findUserId();
            Long accountId = findcapitalAccountId();
            //待收运费数量
            Integer freightNums = queryOrderService.collectFreightNums(userId);

            //银行卡张数
            Integer bankNums = bankCardHandlerService.countByAccountId(accountId);

            //获取是否有设置密码
            com.cy.platformpay.service.dto.base.Response<AccountPassWdDTO> accountPassWdDTOResponse = accountService.passWdOptions(accountId);
            Boolean isSetUpPassword = true;//默认未设置
            if(accountPassWdDTOResponse.isSuccess())
            {
                if(accountPassWdDTOResponse.getData()!=null){
                    /**
                     * 修改时间：2016-03-29 10:30
                     * 修改人：王远航
                     * 修改依据：支付密码和提现密码合成交易密码，数据库保留支付密码。兼容货主app1.1.1
                     */
                    isSetUpPassword = accountPassWdDTOResponse.getData().getHasPayPassWd();
                }
            }

            //账户余额
            com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO> resultTotalMoney = mainBmikeceService.queryAccountInfo(accountId);
            String totalMoney = "0.00";
            if(resultTotalMoney.isSuccess())
            {
                if(resultTotalMoney.getData()!=null){
                    BigDecimal totalMoney1 = resultTotalMoney.getData().getTotalMoney() == null ? new BigDecimal("0.00") : resultTotalMoney.getData().getTotalMoney();
                    BigDecimal LuckyMoney1 = resultTotalMoney.getData().getLuckyMoney() == null ? new BigDecimal("0.00") : resultTotalMoney.getData().getLuckyMoney();
                    totalMoney =totalMoney1.add(LuckyMoney1)+"";
                }
            }
            MyWalletInfo myWalletInfo = new MyWalletInfo();
            myWalletInfo.setAmount(totalMoney);
            myWalletInfo.setBankCardNum(bankNums);
            myWalletInfo.setCapitalFreightNum(freightNums);
            //todo 3.4 版本不做缺优惠券接口
            myWalletInfo.setCouponsNum(3);//优惠券数量
            myWalletInfo.setIdSetUpTradersPassword(isSetUpPassword ? "0" : "1");
            //积分
            AwardAccountDTO awardAccountDTO = integrationService.countByUserId(Constants.AWARD_DRIVER,findUserId());
            if(awardAccountDTO != null){
                myWalletInfo.setIntegral(awardAccountDTO.getPointAmount());
            }
            updRespHeadSuccess(response);
            return myWalletInfo;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("拨打网络电话出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

}
