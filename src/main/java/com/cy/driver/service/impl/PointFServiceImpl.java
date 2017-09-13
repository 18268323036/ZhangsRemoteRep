package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cy.award.service.AccountService;
import com.cy.award.service.ExchangeActivityService;
import com.cy.award.service.PointService;
import com.cy.award.service.dto.AwardAccountDTO;
import com.cy.award.service.dto.AwardTransLogDTO;
import com.cy.award.service.dto.PointParamDTO;
import com.cy.award.service.dto.TransLogParamDTO;
import com.cy.award.service.dto.base.PageInfo;
import com.cy.award.service.dto.base.PageResult;
import com.cy.award.service.dto.base.Response;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.throwex.ValidException;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.PageComm;
import com.cy.driver.service.PointFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rui.mao
 * @Type PointFServiceImpl
 * @Desc
 * @date 2016/4/25
 */
@Service("pointFService")
public class PointFServiceImpl implements PointFService {
    private static final Logger LOG = LoggerFactory.getLogger(PointFServiceImpl.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private PointService pointService;
    @Autowired
    private ExchangeActivityService exchangeActivityService;

    @Override
    public String saveAccount(Long driverId) {
        AwardAccountDTO paramDTO = new AwardAccountDTO();
        paramDTO.setUserId(driverId);
        paramDTO.setUserType(Constants.AWARD_DRIVER);
        Response<String> response = accountService.saveAccount(paramDTO);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            LOG.error("开通奖励账户:[award服务返回空],param={}", driverId);
            return null;
        }
        if (!response.isSuccess()) {
            LOG.error("开通奖励账户:[award服务返回错误],param={},response={}", driverId, JSONObject.toJSONString(response));
            return null;
        }
        if (response.getData() == null) {
            LOG.error("开通奖励账户:[award服务data为空],param={},response={}", driverId, JSONObject.toJSONString(response));
            return null;
        }
        return response.getData();
    }

    /**
     * 该方法废除，采用推送的方式做弹框提醒
     */
//    @Override
//    public PointInfoDTO pointReward(Byte userType, Long userId, Integer checkMode, String eventValue, Integer amount, String remark, Integer source) {
//        PointParamDTO paramDTO = new PointParamDTO();
//        paramDTO.setUserId(userId);
//        paramDTO.setUserType(userType);
//        paramDTO.setCheckMode(checkMode);
//        paramDTO.setEventValue(eventValue);
//        paramDTO.setAmount(amount);
//        paramDTO.setRemark(remark);
//        paramDTO.setSource(source);
//        //验证是否满足积分奖励条件
//        Response<PointInfoDTO> validResponse = pointService.calculateAmount(paramDTO);
//        if(validResponse == null || !validResponse.isSuccess() ||
//                validResponse.getData() == null || validResponse.getData().getAmount() == null
//                || validResponse.getData().getAmount().intValue() == 0){
//            LOG.error("事件积分奖励:[验证是否满足加积分奖励条件，不满足],param={}", JSONObject.toJSONString(paramDTO));
//            return null;
//        }
//        //积分奖励
//        Response<Boolean> response = pointService.pointReward(paramDTO);
//        if (response == null) {
//            LOG.error("事件积分奖励:[award服务返回空],param={}", JSONObject.toJSONString(paramDTO));
//            return null;
//        }
//        if (!response.isSuccess()) {
//            LOG.error("事件积分奖励:[award服务返回错误],param={},response={}", JSONObject.toJSONString(paramDTO), JSONObject.toJSONString(response));
//            return null;
//        }
//        if (response.getData() == null) {
//            LOG.error("事件积分奖励:[award服务data为空],param={},response={}", JSONObject.toJSONString(paramDTO), JSONObject.toJSONString(response));
//            return null;
//        }
//        return validResponse.getData();
//    }
    @Override
    public boolean pointReward(Byte userType, Long userId, Integer checkMode, String eventValue, Integer amount, String remark, Integer source, String alertMsg) {
        PointParamDTO paramDTO = new PointParamDTO();
        paramDTO.setUserId(userId);
        paramDTO.setUserType(userType);
        paramDTO.setCheckMode(checkMode);
        paramDTO.setEventValue(eventValue);
        paramDTO.setAmount(amount);
        paramDTO.setRemark(remark);
        paramDTO.setSource(source);
        paramDTO.setAlertMessage(alertMsg);
        //积分奖励
        Response<Boolean> response = pointService.pointReward(paramDTO);
        if (response == null) {
            LOG.error("事件积分奖励:[award服务返回空],param={}", JSONObject.toJSONString(paramDTO));
            return false;
        }
        if (!response.isSuccess()) {
            LOG.error("事件积分奖励:[award服务返回错误],param={},response={}", JSONObject.toJSONString(paramDTO), JSONObject.toJSONString(response));
            return false;
        }
        if (response.getData() == null) {
            LOG.error("事件积分奖励:[award服务data为空],param={},response={}", JSONObject.toJSONString(paramDTO), JSONObject.toJSONString(response));
            return false;
        }
        return response.getData();
    }

    @Override
    public Boolean exchangePoint(String redeemCode, String accountCode, int source) {
        Response<Boolean> response = exchangeActivityService.exchangePoint(redeemCode, accountCode, source);
        if (response == null) {
            LOG.error("兑换码奖励积分:[award服务返回空],param={},{},{}", redeemCode, accountCode, source);
            return null;
        }
        if (!response.isSuccess()) {
            LOG.error("兑换码奖励积分:[award服务返回错误],param={},{},{},response={}", redeemCode, accountCode, source, JSONObject.toJSONString(response));
            return null;
        }
        if (response.getData() == null) {
            LOG.error("兑换码奖励积分:[award服务data为空],param={},{},{},response={}", redeemCode, accountCode, source, JSONObject.toJSONString(response));
            return null;
        }
        return response.getData();
    }

    @Override
    public PageBase<AwardTransLogDTO> pageAwardLog(PageComm page, TransLogParamDTO paramDTO) {
        PageInfo<TransLogParamDTO> pageInfo = new PageInfo<TransLogParamDTO>(page.getPage(), page.getPageSize());
        pageInfo.setData(paramDTO);
        PageResult<AwardTransLogDTO> result = pointService.pageLog(pageInfo);
        if (!result.isSuccess()) {
            LOG.error("分页查询积分明细:[award服务返回错误].param={},response={}", JSONObject.toJSONString(pageInfo), JSONObject.toJSONString(result));
            throw new ValidException(ValidException.QUERY_ERROR, "分页查询积分明细");
        }
        return new PageBase<AwardTransLogDTO>(result.getTotalRecord(), result.getTotalPage(), result.getDataList());
    }

}
