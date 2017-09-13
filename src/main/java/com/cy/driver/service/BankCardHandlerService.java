package com.cy.driver.service;

import com.cy.platformpay.service.dto.AccountBankDTO;
import com.cy.platformpay.service.dto.BankDTO;
import com.cy.platformpay.service.dto.base.Response;

import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
public interface BankCardHandlerService {

    /**
     * 查询主要银行列表
     *
     * @return
     */
    public Response<List<BankDTO>> listMainBank();

    /**
     * 统计银行卡数量
     *
     * @param accountId 资金账户编号，必填
     * @return 数量
     */
    public Integer countByAccountId(Long accountId);

    /**
     * 查询绑定银行卡信息列表
     *
     * @param accountId 资金账户编号，必填
     * @return
     */
    public List<AccountBankDTO> listByAccountId(Long accountId);

    /**
     * 绑定银行卡
     *
     * @param accountId  资金账户Id，必填
     * @param bankCode   银行码表Code，必填
     * @param cardNo     用户银行卡号，必填
     * @param userName   用户名称，必填
     * @return
     */
    public Response<Boolean> driverTie(Long accountId, String bankCode, String cardNo, String userName);

    /**
     * 更新绑定卡信息
     *
     * @param tieBankId  绑定卡的主键，必填
     * @param bankCode   银行码表Code，必填
     * @param cardNo     用户银行卡号，必填
     * @param userName   用户名称，必填
     * @return
     */
    public Response<Boolean> update(Long tieBankId, String bankCode, String cardNo, String userName);

    /**
     * 删除绑定卡信息
     * @param tieBankId 绑定卡的主键，必填
     * @param accountId 资金账户Id，必填
     * @return
     */
    public Response<Boolean> delete(Long tieBankId, Long accountId);
}
