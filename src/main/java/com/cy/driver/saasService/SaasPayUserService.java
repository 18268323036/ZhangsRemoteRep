package com.cy.driver.saasService;

import com.cy.driver.domain.PageBase;
import com.cy.driver.common.enumer.UtmsBusinessEventEnum;
import com.cy.driver.domain.saasPay.PayUserBillInfo;
import com.cy.saas.pay.model.dto.AccountBalanceDTO;
import com.cy.saas.pay.model.dto.UserSafetyDTO;
import com.cy.saas.pay.model.enums.PayAccountTypeEnum;
import com.cy.saas.pay.model.enums.PayUserNatureEnum;

/**
 * UTMS用戶支付用戶操作
 * Created by nixianjing on 17/8/17.
 */
public interface SaasPayUserService {

    /**
     * 验证UTMS账号是否开通
     * @param userNature 用户属性，必填 {@link PayUserNatureEnum}
     * @param userId 用户id，必填
     * @return
     */
    boolean accountWhetherClear(String userNature, String userId);

    /**
     * UTMS重置交易密码
     * @param tradePassword 交易密码（明文）,必填
     * @param userId 用户id，必填
     * @return
     */
     boolean resetTradePassword(String tradePassword, String userId);

    /**
     * 开通UTMS基本户
     * @param userNature 用户属性，必填 {@link PayUserNatureEnum}
     * @param name 姓名
     * @param mobile 手机号码
     * @param userId 用户id，必填
     * @param password 密码
     * @return
     */
    boolean saveForOpenAccount(String userNature, String name, String mobile, String userId, String password);

    /**
     * 查询UTMS基本账户余额
     * @param accountType 账户类型（不传查询全部帐户余额） {@link PayAccountTypeEnum}
     * @param userId 用户id，必填
     * @return
     */
    AccountBalanceDTO listAccountBalance(String accountType, String userId);


    /**
     * 分页查询账单明细
     * @param businessEvent  账单业务类型 {@link UtmsBusinessEventEnum}
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @param userId 用户ID
     * @return
     */
    PageBase<PayUserBillInfo> pageBillDetail(String businessEvent, String startTime, String endTime, String userId, Integer pageIndex);


    /**
     * 获得用户安全设置信息
     * @param userId 用户ID
     * @return
     */
    UserSafetyDTO getUserSafety(String userId);
}
