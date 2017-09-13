package com.cy.driver.saasService.impl;

import com.cy.driver.domain.PageBase;
import com.cy.driver.saasService.SaasPayBankService;
import com.cy.saas.pay.model.dto.*;
import com.cy.saas.pay.model.enums.PayUserTypeEnum;
import com.cy.saas.pay.model.po.PayBank;
import com.cy.saas.pay.model.po.PayMainBank;
import com.cy.saas.pay.model.po.PayUserBank;
import com.cy.saas.pay.service.PayBankService;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;
import com.cy.top56.common.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by nixianjing on 17/8/17.
 */
@Service("saasPayBankService")
public class SaasPayBankServiceImpl implements SaasPayBankService {

    @Resource
    private PayBankService utmsPayBankService;

    @Override
    public boolean bindBank(BindBankDTO bindBankDTO) {
        /**
         * 绑定银行卡
         * @param bindBankDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 支付会员信息不存在<br/>
         *         {@link Response.CodeTable#LOCK_FAIL} 锁定失败<br/>
         *         {@link Response.CodeTable#LOCK_WAIT} 锁定等待<br/>
         *         {@link Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
         *         {@link Response.CodeTable#FAIL1} 失败<br/>
         */
        Response<String> response = utmsPayBankService.bindBank(bindBankDTO);
        if(response.isSuccess()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean unbindBank(String bankId,String userId) {
        UnbindBankDTO unbindBankDTO = new UnbindBankDTO();
        unbindBankDTO.setBankId(bankId);
        unbindBankDTO.setUserId(userId);
        unbindBankDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 解绑银行卡
         * @param unbindBankDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 支付会员信息不存在<br/>
         *         {@link Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
         *         {@link Response.CodeTable#FAIL1} 失败<br/>
         */
        Response response = utmsPayBankService.unbindBank(unbindBankDTO);
        if(response.isSuccess()) {
            return true;
        }
        return false;
    }

    @Override
    public List<PayUserBankDTO> listQueryBank(String userId) {
        PayUserDTO payUserDTO = new PayUserDTO();
        payUserDTO.setUserId(userId);
        payUserDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 查询银行卡列表
         * @param payUserDTO
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 支付会员信息不存在<br/>
         *         {@link Response.CodeTable#GATEWAY_FAIL} 网关调用失败<br/>
         *         {@link Response.CodeTable#FAIL1} 失败<br/>
         */
        Response<List<PayUserBankDTO>> response = utmsPayBankService.listQueryBank(payUserDTO);
        if(response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    @Override
    public Response<PayMainBank> getMainBankByCardNo(String cardNo) {
        /**
         * 根据卡号获得总行信息
         * @param cardNo 银行卡号，必填
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         */
        Response<PayMainBank> response = utmsPayBankService.getMainBankByCardNo(cardNo);
        return response;
    }


    @Override
    public PageBase<PayMainBank> pageMainBank(String bankName, Integer pageIndex) {
        PageInfo<MainBankQDTO > pageInfo = new PageInfo<>();
        MainBankQDTO mainBankQDTO = new MainBankQDTO();
        mainBankQDTO.setBankName(bankName);
        mainBankQDTO.setEffectivity(true);
        pageInfo.setPageSize(10);
        pageInfo.setPageIndex(pageIndex);
        pageInfo.setData(mainBankQDTO);
        /**
         * 分页查询总行
         * @param pageInfo 分页参数，必填
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         */
        PageResult<PayMainBank> pageResult = utmsPayBankService.pageMainBank(pageInfo);
        if(pageResult.isSuccess()) {
            PageBase<PayMainBank> pageBase = new PageBase<>();
            pageBase.setTotalNum(pageResult.getTotalRecord());
            pageBase.setTotalPage(pageResult.getTotalPage());
            pageBase.setListData(pageResult.getDataList());
            return pageBase;
        }
        return null;
    }

    @Override
    public PageBase<PayBank> pageBranchBank(String agentBankCode, String bankName, Integer pageIndex) {
        PageInfo< BranchBankQDTO > pageInfo = new PageInfo<>();
        BranchBankQDTO branchBankQDTO = new BranchBankQDTO();
        branchBankQDTO.setAgentBankCode(agentBankCode);
        branchBankQDTO.setBankName(bankName);
        pageInfo.setPageSize(10);
        pageInfo.setPageIndex(pageIndex);
        pageInfo.setData(branchBankQDTO);
        /**
         * 分页查询支行
         * @param pageInfo 分页参数，必填
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         */
        PageResult<PayBank> pageResult = utmsPayBankService.pageBranchBank(pageInfo);
        if(pageResult.isSuccess()) {
            PageBase<PayBank> pageBase = new PageBase<>();
            pageBase.setTotalNum(pageResult.getTotalRecord());
            pageBase.setTotalPage(pageResult.getTotalPage());
            pageBase.setListData(pageResult.getDataList());
            return pageBase;
        }
        return null;
    }

    @Override
    public PayUserBank getPayUserBank(String userId, String bankId) {
        PayUserDTO payUserDTO = new PayUserDTO();
        payUserDTO.setUserId(userId);
        payUserDTO.setUserType(PayUserTypeEnum.DRIVER.getCode());
        /**
         * 根据用户和银行卡id获取银行卡信息
         * @param payUserDTO
         * @param bankId 银行卡id
         * @return {@link Response.CodeTable#SUCCESS} 成功<br/>
         *         {@link Response.CodeTable#ERROR} 参数错误<br/>
         *         {@link Response.CodeTable#EXCEPTION} 程序出错<br/>
         *         {@link Response.CodeTable#DATA_NONE_EXIST} 支付会员信息不存在<br/>
         */
        Response<PayUserBank> response = utmsPayBankService.getPayUserBank(payUserDTO,bankId);
        if(response.isSuccess()) {
            return response.getData();
        }
        return null;
    }
}
