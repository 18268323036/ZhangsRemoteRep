package com.cy.driver.cloudService;

import com.cy.driver.api.domain.res.SassOrderInfo;
import com.cy.driver.api.domain.res.SassOrderPageRes;
import com.cy.saas.basic.model.po.SettingBusiness;
import com.cy.saas.business.model.dto.ModifyOrderQuoteDTO;
import com.cy.saas.business.model.dto.OrderInfoPageParamDTO;
import com.cy.saas.business.model.dto.PagePartForMyQuoteDTO;
import com.cy.saas.business.model.dto.SaveGrabSingleDTO;
import com.cy.saas.business.model.po.OrderPart;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.top56.common.Response;

/**
 * saas 竞价/抢单
 * Created by nixianjing on 17/5/15.
 */
public interface SaasOrderInfoService {

    /**
     * 接单-抢单分页列表
     * @param page
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    SassOrderPageRes pageRobForReceiving(Integer page, OrderInfoPageParamDTO paramDTO);



    /**
     * 接单-竞价分页列表
     * @param page
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    SassOrderPageRes pageBidForReceiving(Integer page, OrderInfoPageParamDTO paramDTO);


    /**
     * 竞价任务、我的报价
     * @param page
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    SassOrderPageRes pagePartForMyQuote(Integer page, PagePartForMyQuoteDTO paramDTO);

    /**
     * 抢单、竞价详情
     * @param id orderInfo.id,必填
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    SassOrderInfo getDetailForRobBidByInfoId(Long id);


    /**
     * 抢单、竞价详情
     * @param partId orderPart.id,必填
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    SassOrderInfo getDetailForRobBidByPartId(Long partId);


    /**
     * 根据参与方id获取参与方详情
     * @param partId orderPart.id 必填
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    OrderPart getOrderPart(Long partId);

    /**
     * 抢单操作
     * @param saveGrabSingleDTO 参数，必填
     * @return {@link Response.CodeTable#ERROR} 参数错误<br/>
     *         {@link Response.CodeTable#EXCEPTION} 系统异常<br/>
     *         {@link Response.CodeTable#DATA_NONE_EXIST} 订单不存在<br/>
     *         {@link Response.CodeTable#LOCK_WAIT} 锁定等待，订单正在被他人操作<br/>
     *         {@link Response.CodeTable#LOCK_FAIL} 锁定失败<br/>
     *         {@link Response.CodeTable#NONE_PERMISSION} 数据无权限操作<br/>
     *         {@link Response.CodeTable#FAIL1} 抢单失败，订单运费已变动<br/>
     *         {@link Response.CodeTable#FAIL2} 抢单失败<br/>
     */
    Response<WaybillInfo> saveByGrabSingle(SaveGrabSingleDTO saveGrabSingleDTO);


    /**
     * 修改-报价
     * @param modifyOrderQuoteDTO 参数，必填
     * @return {@link Response.CodeTable#ERROR} 参数错误<br/>
     *         {@link Response.CodeTable#EXCEPTION} 系统异常<br/>
     *         {@link Response.CodeTable#DATA_NONE_EXIST} 订单不存在<br/>
     *         {@link Response.CodeTable#NONE_PERMISSION} 数据无权限操作<br/>
     *         {@link Response.CodeTable#FAIL1} 报价失败<br/>
     */
    Response modifyOrderQuote(ModifyOrderQuoteDTO modifyOrderQuoteDTO);

    /**
     * 根据用户获取业务设置详情
     * @param userId 企业主账号ID
     * @return {@link Response.CodeTable#SUCCESS} 成功 <br/>
     *         {@link Response.CodeTable#EXCEPTION} 失败 <br/>
     *         {@link Response.CodeTable#ERROR} 参数错误 <br/>
     */
    SettingBusiness getByUserId(Long userId);

}
