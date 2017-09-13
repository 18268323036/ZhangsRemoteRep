package com.cy.driver.service.impl;

import com.cy.driver.service.BankCardHandlerService;
import com.cy.platformpay.service.AccountBankService;
import com.cy.platformpay.service.dto.AccountBankDTO;
import com.cy.platformpay.service.dto.BankDTO;
import com.cy.platformpay.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@Service("bankCardHandlerService")
public class BankCardHandlerServiceImpl implements BankCardHandlerService {
    private static final Logger LOG = LoggerFactory.getLogger(BankCardHandlerServiceImpl.class);

    @Resource
    private AccountBankService accountBankService;

    /**
     * 查询主要银行列表
     *
     * @return
     */
    @Override
    public Response<List<BankDTO>> listMainBank(){
       return accountBankService.listMainBank();
    }

    /**
     * 统计银行卡数量
     *
     * @param accountId 资金账户编号，必填
     * @return 数量
     */
    @Override
    public Integer countByAccountId(Long accountId){
        Response<Integer> resultBankNums = accountBankService.countByAccountId(accountId);
        int bankNums = 0;
        if(resultBankNums.isSuccess())
        {
            if(resultBankNums.getData()!=null){
                bankNums = resultBankNums.getData();
            }
        }
        return bankNums;
    }

    /**
     * 查询绑定银行卡信息列表
     *
     * @param accountId 资金账户编号，必填
     * @return
     */
    @Override
    public List<AccountBankDTO> listByAccountId(Long accountId){
        Response<List<AccountBankDTO>> result = accountBankService.listByAccountId(accountId);
        if (result == null) {
            LOG.error("调用order服务查询绑定银行卡信息列表出错,accountId={}", accountId);
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用order服务查询绑定银行卡信息列表失败,accountId={},返回信息={}", accountId, result.getMessage());
            return null;
        }
        return result.getData();
    }

    /**
     * 绑定银行卡
     *
     * @param accountId  资金账户Id，必填
     * @param bankCode   银行码表Code，必填
     * @param cardNo     用户银行卡号，必填
     * @param userName   用户名称，必填
     * @return
     */
    @Override
    public Response<Boolean> driverTie(Long accountId, String bankCode, String cardNo, String userName)
    {
        return accountBankService.personTie(accountId, bankCode, cardNo, userName);
    }

    /**
     * 更新绑定卡信息
     *
     * @param tieBankId  绑定卡的主键，必填
     * @param bankCode   银行码表Code，必填
     * @param cardNo     用户银行卡号，必填
     * @param userName   用户名称，必填
     * @return
     */
    @Override
    public Response<Boolean> update(Long tieBankId, String bankCode, String cardNo, String userName){
        return accountBankService.update(tieBankId, bankCode, cardNo, userName);
    }

    /**
     * 删除绑定卡信息
     * @param tieBankId 绑定卡的主键，必填
     * @param accountId 资金账户Id，必填
     * @return
     */
    @Override
    public Response<Boolean> delete(Long tieBankId,Long accountId){
        return accountBankService.delete(tieBankId, accountId);
    }

}
