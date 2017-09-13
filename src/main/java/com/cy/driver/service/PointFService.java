package com.cy.driver.service;

import com.cy.award.service.dto.AwardTransLogDTO;
import com.cy.award.service.dto.TransLogParamDTO;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.PageComm;

/**
 * @author rui.mao
 * @Type PointFService
 * @Desc 积分服务
 * @date 2016/4/25
 */
public interface PointFService {

    /**
     * 保存奖励账户
     * @return
     */
    String saveAccount(Long driverId);

    /**
     * 积分奖励
     * @param userType
     * @param userId
     * @param checkMode
     * @param eventValue
     * @param amount
     * @param remark
     * @param source
     * @param alertMsg
     */
    boolean pointReward(Byte userType, Long userId, Integer checkMode, String eventValue, Integer amount, String remark, Integer source, String alertMsg);

    /**
     * 兑换码兑换积分
     * @param redeemCode 兑换码
     * @param accountCode 用户积分账户code
     * @param source 用户渠道：0 web、1 安卓、2 IOS、3 WP、 4 其它、5 微信
     * @return
     * @author sunhh
     */
    Boolean exchangePoint(String redeemCode, String accountCode, int source);

    /**
     * 分页查询积分账单
     * @param page
     * @param paramDTO
     * @return
     */
    PageBase<AwardTransLogDTO> pageAwardLog(PageComm page, TransLogParamDTO paramDTO);
}
