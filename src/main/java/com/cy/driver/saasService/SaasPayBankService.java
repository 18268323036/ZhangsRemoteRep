package com.cy.driver.saasService;

import com.cy.driver.domain.PageBase;
import com.cy.saas.pay.model.dto.BindBankDTO;
import com.cy.saas.pay.model.dto.PayUserBankDTO;
import com.cy.saas.pay.model.po.PayBank;
import com.cy.saas.pay.model.po.PayMainBank;
import com.cy.saas.pay.model.po.PayUserBank;
import com.cy.top56.common.Response;

import java.util.List;

/**
 * UTMS用户银行卡管理
 * Created by nixianjing on 17/8/17.
 */
public interface SaasPayBankService {


    /**
     * 绑定银行卡
     * @param bindBankDTO
     * @return
     */
    boolean bindBank(BindBankDTO bindBankDTO);

    /**
     * 解绑银行卡
     * @param bankId 银行卡id，银行卡在交易见证平台的绑卡id，必填
     * @param userId 用户id 必填
     * @return
     */
    boolean unbindBank(String bankId, String userId);

    /**
     * 查询银行卡列表
     * @param userId 用户Id 必填
     * @return
     */
    List<PayUserBankDTO> listQueryBank(String userId);

    /**
     * 根据银行卡号获取银行信息(总行信息)
     * @param cardNo 银行卡号 必填
     * @return
     */
    Response<PayMainBank> getMainBankByCardNo(String cardNo);

    /**
     * 分页查询总行
     * @param bankName 总行名称
     * @param pageIndex 页码
     * @return
     */
    PageBase<PayMainBank> pageMainBank(String bankName, Integer pageIndex);

    /**
     * 分页查询支行
     * @param agentBankCode 法人行号（网银互联号），必填
     * @param bankName 支行名称
     * @param pageIndex 页码 必填
     * @return
     */
    PageBase<PayBank> pageBranchBank(String agentBankCode, String bankName, Integer pageIndex);


    /**
     * 根据用户和银行卡id获取银行卡信息
     * @param userId 用户id
     * @param bankId 银行卡ID
     * @return
     */
    PayUserBank getPayUserBank(String userId, String bankId);
}
