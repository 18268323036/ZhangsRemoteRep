package com.cy.driver.service.impl;

import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.driver.service.AccountService;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.Enum.AuthCodeType;
import com.cy.platformpay.service.CapitalAccountService;
import com.cy.platformpay.service.dto.AccountPassWdDTO;
import com.cy.platformpay.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/9/9.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    private CapitalAccountService capitalAccountService;

    @Resource
    private DriverUserInfoService driverUserInfoService;

    /**
     * 设置支付密码
     *
     * @param driverId 司机ID，必填
     * @param newPassWd 新支付密码MD5，必填
     * @return true:成功
     */
    @Override
    public Response<Boolean> resetCashPwd(Long driverId, String newPassWd){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverInfoResultData = driverUserInfoService.getDriverUserInfo(driverId);
        DriverUserInfoDTO driverUserInfoDTO = driverInfoResultData.getData();
        if(driverUserInfoDTO!=null){
            /**
             * 修改时间：2016-03-29 11:10
             * 修改人：王远航
             * 修改依据：支付密码和提现密码合成交易密码，数据库保留支付密码。兼容货主app1.1.1
             */
            /** 从原来的重置提现密码改成重置支付密码 */
            return capitalAccountService.resetPayPwd(driverUserInfoDTO.getCapitalAccountId(), newPassWd);
        }
        return new Response<Boolean>(Boolean.FALSE);
    }

    /**
     * 3.6.2	修改提现密码
     *
     * @param driverId 司机ID，必填
     * @param newPassWd 新支付密码MD5，必填
     * @return true:成功
     */
    @Override
    public Response<Boolean> updateCashPwd(Long driverId, String oldPassWd, String newPassWd){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverInfoResultData = driverUserInfoService.getDriverUserInfo(driverId);
        DriverUserInfoDTO driverUserInfoDTO = driverInfoResultData.getData();
        if(driverUserInfoDTO!=null){
            /**
             * 修改时间：2016-03-29 11:16
             * 修改人：王远航
             * 修改依据：支付密码和提现密码合成交易密码，数据库保留支付密码。兼容货主app1.1.1
             */
            /** 从原来的修改提现密码改成修改支付密码 */
            return capitalAccountService.updatePayPwd(driverUserInfoDTO.getCapitalAccountId(), oldPassWd, newPassWd);
        }
        return new Response<Boolean>(Boolean.FALSE);
    }


    /**
     * 验证姓名和身份证信息
     *@param driverId 司机Id，必填
     * @param name 姓名，必填
     * @param identityLicenseNum 身份证，必填
     * @return String :0  ok
     */
    @Override
    public String findPasswordIdentity(Long driverId, String name, String identityLicenseNum){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverInfoResultData = driverUserInfoService.getDriverUserInfo(driverId);
        DriverUserInfoDTO driverUserInfoDTO = driverInfoResultData.getData();
        if(driverUserInfoDTO!=null){
            //验证身份证
            if(!name.equals(driverUserInfoDTO.getName()))
            {
                return "1";
            }
            if(!identityLicenseNum.equals(driverUserInfoDTO.getIdentityLicenseNum())){
                return "2";
            }
            return "0";
        }
        return "-1";
    }

    /**
     * 验证姓名和身份证信息
     *@param driverId 司机Id，必填
     * @param name 姓名，必填
     * @param identityLicenseNum 身份证，必填
     * @return true:成功
     */
    @Override
    public String findPasswordMobile(Long driverId, String name, String identityLicenseNum, String verificationCode){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverInfoResultData = driverUserInfoService.getDriverUserInfo(driverId);
        DriverUserInfoDTO driverUserInfoDTO = driverInfoResultData.getData();
        if(driverUserInfoDTO!=null){
            //验证身份证
            if(!name.equals(driverUserInfoDTO.getName()))
            {
                return "1";
            }
            if(!identityLicenseNum.equals(driverUserInfoDTO.getIdentityLicenseNum())){
                return "2";
            }
            //验证验证码
            com.cy.pass.service.dto.base.Response<Boolean> validCodeData = driverUserInfoService.validAuthCode(driverUserInfoDTO.getCode(), verificationCode, AuthCodeType.FIND_CASHRMB_PASSWD.getValue());
            if( validCodeData.getCode()== CodeTable.LI_VALID_TIMES.getCode()){
                return "3";//有效期超过限制
            }
            if(!validCodeData.isSuccess()){
                return "-1";//异常
            }
            return "0";
        }
        return "-1";
    }

    /**
     * 3.6.5	找回提现密码
     * @param  captailAccountId  资金账户ID
     *@param driverId 司机Id，必填
     * @param name 姓名，必填
     * @param identityLicenseNum 身份证，必填
     * @param passwordNew 新密码，必填
     * @return true:成功
     */
    @Override
    public String findWithdrawPassword(Long captailAccountId, Long driverId, String name, String identityLicenseNum, String verificationCode, String passwordNew){
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> driverInfoResultData = driverUserInfoService.getDriverUserInfo(driverId);
        DriverUserInfoDTO driverUserInfoDTO = driverInfoResultData.getData();
        if(driverUserInfoDTO!=null){
            //验证身份证
            if(!name.equals(driverUserInfoDTO.getName()))
            {
                return "1";
            }
            if(!identityLicenseNum.equals(driverUserInfoDTO.getIdentityLicenseNum())){
                return "2";
            }
            //验证验证码
            com.cy.pass.service.dto.base.Response<Boolean> validCodeData = driverUserInfoService.validAuthCode(driverUserInfoDTO.getCode(), verificationCode, AuthCodeType.FIND_CASHRMB_PASSWD.getValue());
            if( validCodeData.getCode()== CodeTable.LI_VALID_TIMES.getCode()){
                return "3";//有效期超过限制
            }
            if(!validCodeData.isSuccess()){
                return "-1";//异常
            }

            /**
             * 修改时间：2016-03-29 11:20
             * 修改人：王远航
             * 修改依据：支付密码和提现密码合成交易密码，数据库保留支付密码。兼容货主app1.1.1
             */

            /** 从原来的重置提现密码改成重置支付密码 */
            //找回密码
            Response resultReset = capitalAccountService.resetPayPwd(driverInfoResultData.getData().getCapitalAccountId(), passwordNew);
            if((Boolean)resultReset.getData())
            {
                /** 从原来的提现密码解锁改成支付密码解锁 */
                capitalAccountService.payPwdUnlock(captailAccountId);
                return "0";
            }
            return "4";//找回密码失败
        }
        return "-1";
    }


    /**
     * 查询 密码和限额 是否设置
     *
     * @param accountId 资金账户编号，必填
     * @return
     */
    @Override
    public  Response<AccountPassWdDTO> passWdOptions(Long accountId){
        return capitalAccountService.passWdOptions(accountId);
    }

}
