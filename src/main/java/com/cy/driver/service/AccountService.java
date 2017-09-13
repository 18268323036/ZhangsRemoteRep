package com.cy.driver.service;

import com.cy.platformpay.service.dto.AccountPassWdDTO;
import com.cy.platformpay.service.dto.base.Response;

/**
 * Created by Administrator on 2015/9/9.
 */
public interface AccountService {

    /**
     * 设置支付密码
     *
     * @param driverId 司机ID，必填
     * @param newPassWd 新支付密码MD5，必填
     * @return true:成功
     */
    public Response<Boolean> resetCashPwd(Long driverId, String newPassWd);

    /**
     * 修改密码
     *
     * @param driverId 司机ID，必填
     * @param newPassWd 新支付密码MD5，必填
     * @return true:成功
     */
    public Response<Boolean> updateCashPwd(Long driverId, String oldPassWd, String newPassWd);

    /**
     * 验证姓名和身份证信息
     *@param driverId 司机Id，必填
     * @param name 姓名，必填
     * @param identityLicenseNum 身份证，必填
     * @return true:成功
     */
    public String findPasswordIdentity(Long driverId, String name, String identityLicenseNum);

    /**
     * 3.6.4	找回提现密码（手机号码验证）
     *@param driverId 司机Id，必填
     * @param name 姓名，必填
     * @param identityLicenseNum 身份证，必填
     * @param  verificationCode 验证码，必填
     * @return true:成功
     */
    public String findPasswordMobile(Long driverId, String name, String identityLicenseNum, String verificationCode);

    /**
     * 3.6.5	找回提现密码
     * @param  captailAccountId  资金账户ID
     *@param driverId 司机Id，必填
     * @param name 姓名，必填
     * @param identityLicenseNum 身份证，必填
     * @param passwordNew 新密码，必填
     * @return true:成功
     */
    public String findWithdrawPassword(Long captailAccountId, Long driverId, String name, String identityLicenseNum, String verificationCode, String passwordNew);


    /**
     * 查询 密码和限额 是否设置
     *
     * @param accountId 资金账户编号，必填
     * @return
     */
    Response<AccountPassWdDTO> passWdOptions(Long accountId);

}
