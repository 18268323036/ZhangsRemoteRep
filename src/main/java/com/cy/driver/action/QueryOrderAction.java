package com.cy.driver.action;

import com.alibaba.fastjson.JSON;
import com.cy.driver.action.convert.OrderConvert;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.throwex.ValidException;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.*;
import com.cy.driver.domain.push.*;
import com.cy.driver.service.*;
import com.cy.order.service.ReceivablesService;
import com.cy.order.service.dto.*;
import com.cy.order.service.dto.assess.OrderAssessDTO;
import com.cy.order.service.dto.base.CodeTable;
import com.cy.order.service.dto.base.Response;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.order.service.dto.distribute.DistributeOrderDTO;
import com.cy.order.service.dto.order.ApplyPayInfoDTO;
import com.cy.pass.service.UserFeedInfoService;
import com.cy.pass.service.dto.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询订单
 * Created by wyh on 2015/7/25.
 */
@Scope("prototype")
@Controller("queryOrderAction")
public class QueryOrderAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(QueryOrderAction.class);
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private SystemData systemData;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private PushSendService pushSendService;
    @Resource
    private ProtocolService protocolService;
    @Resource
    private ReceivablesService receivablesService;
    @Resource
    private UserFeedInfoService userFeedInfoService;
    @Resource
    private AssessService assessService;
    @Resource
    private OwnerItemService ownerItemService;
    @Resource
    private OrderPayFService orderPayFService;
    @Resource
    private PointFService pointFService;
    @Resource
    private TransactionService transactionService;
    @Resource
    private DriverLineTrackService driverLineTrackService;
    @Resource
    private IdSourceDTOService idSourceDTOService;


    /**
     * 是否同意取消订单
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/auditCancelOrder", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.AUDIT_CANCEL_ORDER)
    @Log(type = LogEnum.AUDIT_CANCEL_ORDER)
    public Object auditCancelOrder(HttpServletRequest request, HttpServletResponse response,
                                   Long orderId, Integer auditState) {
        try {
            if (orderId == null || auditState == null) {
                return findErrorParam(response);
            }
            Integer state = SystemsUtil.buildAuditCancelOrder(auditState);
            if (state == null) {
                return findErrorParam(response);
            }
            Long driverId = findUserId(request);
            OrderAndCargoDTO orderAndCargo = queryOrderService.getOrderDetails(orderId);
            if (orderAndCargo == null) {
                logger.error("是否同意取消订单失败，订单信息不存在");
                return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
            }
            if (orderAndCargo.getOrderInfo().getRealApplicationPayedFair().compareTo(BigDecimal.ZERO) > 0) {
                if (state == 6) {
                    logger.debug("订单已被申请付款，无法取消");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20114);
                }
            }
            Response<OrderStateRespDTO> result = queryOrderService.auditCancelOrder(driverId, orderId, state);
            boolean updateIndex = idSourceDTOService.updateIndex(String.valueOf(orderId), Constants.ORDER_FROM_KUAIDAO);
            if (!updateIndex) {
                logger.debug("调用同步运单状态服务失败");
            }
            if (result.isSuccess()) {
                /** 司机是否同意取消订单的推送 */
                auditCancelOrderPush(auditState, orderAndCargo);

                updRespHeadSuccess(response);
                return null;
            }
            if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            //订单不存在或者已删除
            if (result.getCode() == CodeTable.ORDER_INVALID_CODE.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //订单已取消
            if (result.getCode() == CodeTable.ORDER_HAS_CANCEL.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            //订单已锁定
            if (result.getCode() == CodeTable.ORDER_HAS_LOCK.getCode()) {
                updRespHeadError(response);
            }
            //订单不属于该用户
            if (result.getCode() == CodeTable.ORDER_NO_BELONG_USER.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //参数不合法
            if (result.getCode() == CodeTable.INVALID_ARGS.getCode() || result.getCode() == CodeTable.ERROR.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            if (result.getCode() == CodeTable.ORDER_STATE_CANOT_MATCH.getCode()) {
                return findJSonResponse(response, ApiResultCodeEnum.RULE_30105);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("取消订单出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 司机是否同意取消订单的推送
     *
     * @param auditState
     * @param orderAndCargo
     * @return
     * @author wyh
     */
    private boolean auditCancelOrderPush(Integer auditState, OrderAndCargoDTO orderAndCargo) {
        try {
            TransactionInfoDTO orderDTO = orderAndCargo.getOrderInfo();
            TransactionCargoDTO cargoDTO = orderAndCargo.getCargoInfo();
            TransactionCarryDriverDTO driverDTO = orderAndCargo.getDriverInfo();
            if (orderDTO == null) {
                logger.error("司机是否同意取消订单的推送失败，订单信息不存在");
                return false;
            }
            if (auditState.intValue() == 1) {
                /** 司机同意取消订单 */
                if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    if (orderDTO.getTradeCancelOrigin().intValue() == 2) {
                        /** 货主取消订单，司机同意 */
                        /** 推送给货主 */
                        PushCargo info = new PushCargo();
                        if (cargoDTO != null) {
                            info.setStartTime(cargoDTO.getRequestStartTime());
                            info.setStartCity(cargoDTO.getStartCity());
                            info.setEndCity(cargoDTO.getEndCity());
                            info.setCargoName(cargoDTO.getCargoName());
                        }
                        info.setBusinessId(orderDTO.getDistributeId());
                        info.setBusinessId2(orderDTO.getId());
                        info.setUserId(orderDTO.getDeployUserid());
                        pushSendService.sjtyPushOwn(info);
                        /** 推送给分包商 */
                        DistributeInfoDTO distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                        if (distributeDTO == null) {
                            logger.error("司机是否同意取消订单的推送失败，派单信息不存在distributeId={}", orderDTO.getDistributeId());
                            return false;
                        }
                        info.setUserId(distributeDTO.getSubcontractorUserId());
                        return pushSendService.sjtyPushOwn(info);
                    } else if (orderDTO.getTradeCancelOrigin().intValue() == 5) {
                        /** 分包商取消订单，司机同意 */
                        DistributeInfoDTO distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                        if (distributeDTO == null) {
                            logger.error("司机是否同意取消订单的推送失败，派单信息不存在distributeId={}", orderDTO.getDistributeId());
                            return false;
                        }
                        PushSubCon info = new PushSubCon();
                        if (cargoDTO != null) {
                            info.setStartTime(cargoDTO.getRequestStartTime());
                            info.setStartCity(cargoDTO.getStartCity());
                            info.setEndCity(cargoDTO.getEndCity());
                            info.setCargoName(cargoDTO.getCargoName());
                        }
                        info.setUserId(orderDTO.getDeployUserid());
                        info.setSubConName(distributeDTO.getSubcontractorName());
                        info.setBusinessId(orderDTO.getDistributeId());
                        info.setBusinessId2(orderDTO.getId());
                        return pushSendService.sjtyPushOwn(info);
                    } else {
                        logger.error("司机是否同意取消订单的推送失败，取消来源不正常tradeCancelOrigin={}", orderDTO.getTradeCancelOrigin());
                        return false;
                    }
                } else {
                    /** 货主取消订单，司机同意 */
                    PushCargo info = new PushCargo();
                    if (cargoDTO != null) {
                        info.setStartTime(cargoDTO.getRequestStartTime());
                        info.setStartCity(cargoDTO.getStartCity());
                        info.setEndCity(cargoDTO.getEndCity());
                        info.setCargoName(cargoDTO.getCargoName());
                    }
                    info.setUserId(orderDTO.getDeployUserid());
                    info.setBusinessId(orderDTO.getId());
                    return pushSendService.sjtyPushOwn2(info);
                }
            } else {
                /** 司机不同意取消订单 */
                PushDriver info = new PushDriver();
                if (cargoDTO != null) {
                    info.setStartTime(cargoDTO.getRequestStartTime());
                    info.setStartCity(cargoDTO.getStartCity());
                    info.setEndCity(cargoDTO.getEndCity());
                    info.setCargoName(cargoDTO.getCargoName());
                }
                if (driverDTO != null) {
                    info.setDriverName(driverDTO.getDriverName());
                }
                if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    info.setBusinessId(orderDTO.getDistributeId());
                    info.setBusinessId2(orderDTO.getId());
                    if (orderDTO.getTradeCancelOrigin().intValue() == 2) {
                        /** 货主取消订单，司机不同意 */
                        /** 推送给货主 */
                        info.setUserId(orderDTO.getDeployUserid());
                        pushSendService.sjbtyPushOwn(info);
                        /** 推送给分包商 */
                        DistributeInfoDTO distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                        if (distributeDTO == null) {
                            logger.error("司机是否同意取消订单的推送失败，派单信息不存在distributeId={}", orderDTO.getDistributeId());
                            return false;
                        }
                        info.setUserId(distributeDTO.getSubcontractorUserId());
                        return pushSendService.sjbtyPushOwn(info);
                    } else if (orderDTO.getTradeCancelOrigin().intValue() == 5) {
                        /** 分包商取消订单，司机不同意 */
                        DistributeInfoDTO distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                        if (distributeDTO == null) {
                            logger.error("司机是否同意取消订单的推送失败，派单信息不存在distributeId={}", orderDTO.getDistributeId());
                            return false;
                        }
                        info.setUserId(distributeDTO.getSubcontractorUserId());
                        return pushSendService.sjbtyPushSubCon(info);
                    } else {
                        logger.error("司机是否同意取消订单的推送失败，订单的取消来源不正确tradeCancelOrigin={}", orderDTO.getTradeCancelOrigin());
                        return false;
                    }
                } else {
                    /** 货主取消订单，司机不同意 */
                    info.setUserId(orderDTO.getDeployUserid());
                    info.setBusinessId(orderDTO.getId());
                    return pushSendService.sjbtyPushOwn2(info);
                }
            }
        } catch (Exception e) {
            logger.error("司机是否同意取消订单的推送出现异常，auditState={}", auditState, e);
            return false;
        }
    }

    /**
     * 取消订单
     *
     * @param request
     * @param response
     * @param orderId
     * @param cancelReason 取消原因，司机3.4版本新增，必填
     * @return
     */
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CANCEL_ORDER)
    @Log(type = LogEnum.CANCEL_ORDER)
    public Object cancelOrder(HttpServletRequest request, HttpServletResponse response,
                              Long orderId, String cancelReason) {
        try {
            if (orderId == null) {
                return findErrorParam(response);
            }
            OrderAndCargoDTO orderAndCargo = queryOrderService.getOrderDetails(orderId);
            if (orderAndCargo == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
            }
            //如果订单已被申请付款，就不允许取消订单
            if (orderAndCargo.getOrderInfo().getRealApplicationPayedFair().compareTo(BigDecimal.ZERO) > 0) {
                logger.debug("订单已被申请付款，无法取消");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20114);
            }
            //
            Response<OrderStateRespDTO> result = queryOrderService.cancelOrder(findUserId(request), orderId, 4, cancelReason);
            boolean updateIndex = idSourceDTOService.updateIndex(String.valueOf(orderId), Constants.ORDER_FROM_KUAIDAO);
            if (!updateIndex) {
                logger.debug("调用同步运单状态服务失败");
            }

            if (result.isSuccess()) {
                /** 申请取消订单的推送 修改时间：2016-02-02 17:00 */
                cancelOrderPush(orderAndCargo);

                updRespHeadSuccess(response);
                return null;
            }
            //订单不存在或者已删除
            if (result.getCode() == CodeTable.ORDER_INVALID_CODE.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //订单已锁定
            if (result.getCode() == CodeTable.ORDER_HAS_LOCK.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20089);
            }
            //不能操作订单
            if (result.getCode() == CodeTable.ORDER_CANNOT_OPT.getCode()) {
                return findHandleFail(response);
            }
            //订单不属于该用户
            if (result.getCode() == CodeTable.ORDER_NO_BELONG_USER.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("取消订单出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 申请取消订单的推送
     *
     * @param orderAndCargo
     * @return
     * @autor wyh
     */
    private boolean cancelOrderPush(OrderAndCargoDTO orderAndCargo) {
        try {
            TransactionInfoDTO orderDTO = orderAndCargo.getOrderInfo();
            TransactionCargoDTO cargoDTO = orderAndCargo.getCargoInfo();
            TransactionCarryDriverDTO driverDTO = orderAndCargo.getDriverInfo();
            if (orderDTO == null) {
                logger.error("申请取消订单的推送失败，订单不存在");
                return false;
            }
            PushDriver info = new PushDriver();
            if (cargoDTO != null) {
                info.setStartTime(cargoDTO.getRequestStartTime());
                info.setStartCity(cargoDTO.getStartCity());
                info.setEndCity(cargoDTO.getEndCity());
                info.setCargoName(cargoDTO.getCargoName());
            }
            if (driverDTO != null) {
                info.setDriverName(driverDTO.getDriverName());
            }
            if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                DistributeInfoDTO distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                if (distributeDTO == null) {
                    logger.error("申请取消订单的推送失败，派单不存在");
                    return false;
                }
                info.setUserId(distributeDTO.getSubcontractorUserId());
                info.setBusinessId(orderDTO.getDistributeId());
                info.setBusinessId2(orderDTO.getId());
                return pushSendService.sjqxddPushSubCon(info);
            } else {
                info.setUserId(orderDTO.getDeployUserid());
                info.setBusinessId(orderDTO.getId());
                return pushSendService.sjqxddPushOwn(info);
            }
        } catch (Exception e) {
            logger.error("申请取消订单的推送出现异常", e);
            return false;
        }
    }

    /**
     * 查看评价
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/getComment", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_COMMENT)
    @Log(type = LogEnum.LOOK_ASSESS)
    public Object getComment(HttpServletRequest request, HttpServletResponse response,
                             Long orderId) {
        try {
            if (orderId == null) {
                return findErrorParam(response);
            }
            OrderAssessDTO orderAssessDTO = assessService.commonByDriver(orderId);
            CommentBO commentBO = new CommentBO();
            if (orderAssessDTO != null) {
                commentBO.setAssessId(orderAssessDTO.getId());
                AssessScore assessScore = JSON.parseObject(orderAssessDTO.getCol1(), AssessScore.class);
                if (assessScore != null) {
                    if (assessScore.getDescribeScore() != null) {
                        commentBO.setDescribeScore(assessScore.getDescribeScore().byteValue());//描述相符分数(0-5)
                    }
                    if (assessScore.getSpeedScore() != null) {
                        commentBO.setRespSpeedScore(assessScore.getSpeedScore().byteValue());//响应速度(0-5)
                    }
                    if (assessScore.getServiceScore() != null) {
                        commentBO.setServiceScore(assessScore.getServiceScore().byteValue());//服务态度
                    }
                }
                commentBO.setAssessContent(orderAssessDTO.getContent());//评价内容
            }
            updRespHeadSuccess(response);
            return commentBO;
        } catch (Exception e) {
            logger.error("查看评价出错", e);
        }
        return findException(response);
    }


    /**
     * 提交评价
     *
     * @param request
     * @param response
     * @param describeScore  描述相符分数(0-5)
     * @param respSpeedScore 响应速度(0-5)
     * @param serviceScore   服务态度(0-5)
     * @param assessContent  评价内容
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/saveComment", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAVE_COMMENT)
    @Log(type = LogEnum.SUBMIT_ASSESS)
    public Object saveComment(HttpServletRequest request, HttpServletResponse response,
                              Integer describeScore, Integer respSpeedScore, Integer serviceScore, String assessContent, Long orderId) {
        try {
            if (orderId == null || describeScore == null || respSpeedScore == null || serviceScore == null) {
                return findErrorParam(response);
            }

            if (describeScore == null || describeScore.intValue() <= 0 || respSpeedScore == null || respSpeedScore <= 0
                    || serviceScore == null || serviceScore <= 0) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20110);
            }
            Long driverId = findUserId(request);
            DriverUserInfoDTO driverInfo = driverUserHandlerService.getDriverInfo(driverId);
            if (driverInfo == null) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }
            OrderAssess orderAssess = new OrderAssess();
            orderAssess.setOrderId(orderId);
            orderAssess.setOrgUserId(driverId);
            orderAssess.setOrgUserName(driverInfo.getName());
            /**
             * 描述相符星个数  X  2/5  +  响应速度星个数  X  2/5   +  服务态度星个数  X  1/5
             * 以上计算结果四舍五入取整 ，最终星个数转化为【好、中、差】
             * 5颗星—— 好 3颗星—— 中 1颗星—— 差
             */
            double scoreDb = (describeScore * 0.4) + (respSpeedScore * 0.4) + (serviceScore * 0.2);
            long scoreLg = Math.round(scoreDb);
            Integer score = null;
            if (scoreLg >= 5) {
                score = Constants.ASSESS_SCORE_GOOD;
            } else if (scoreLg < 5 && scoreLg >= 3) {
                score = Constants.ASSESS_SCORE_MIDDLE;
            } else {
                score = Constants.ASSESS_SCORE_BAD;
            }
            orderAssess.setAssessScore(score);
            orderAssess.setContent(assessContent);
            orderAssess.setNameHidden((byte) 0);

            AssessScore assessScore = new AssessScore();
            assessScore.setDescribeScore(describeScore);
            assessScore.setSpeedScore(respSpeedScore);
            assessScore.setServiceScore(serviceScore);
            orderAssess.setCol1(JSON.toJSONString(assessScore));

            assessService.saveDriverAssess(orderAssess);
            boolean updateIndex = idSourceDTOService.updateIndex(String.valueOf(orderId), Constants.ORDER_FROM_KUAIDAO);
            if (!updateIndex) {
                logger.debug("调用同步运单状态服务失败");
            }

            updRespHeadSuccess(response);
            return null;
        } catch (ValidException e) {
//            return findJSonResponse(response, String.valueOf(e.getCode()), e.getMessage());
//        } catch (Exception e) {
            logger.error("提交评价出现异常", e);
            return findException(response);
        }
    }


    /**
     * 查看发货人详情（订单）
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/queryOrderConsignorDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_ORDER_CONSIGNOR_DETAILS)
    @Log(type = LogEnum.QUERY_ORDER_CONSIGNOR_DETAILS)
    public Object queryOrderConsignorDetails(HttpServletRequest request, HttpServletResponse response,
                                             Long orderId) {
        try {
            if (orderId == null) {
                return findErrorParam(response);
            }
            OrderAndCargoDTO orderAndCargoDTO = queryOrderService.getOrderDetails(orderId);
            if (orderAndCargoDTO != null) {
                updRespHeadSuccess(response);
                ConsignorDetailBO bo = buildConsignorDetailsBo(orderAndCargoDTO.getOrderInfo(), orderAndCargoDTO.getCargoInfo());
                return bo;
            }
            return findJSonResponse(response, ApiResultCodeEnum.SER_20226);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("查看发货人详情（订单）", e);
            }
        }
        return findException(response);
    }

    private ConsignorDetailBO buildConsignorDetailsBo(TransactionInfoDTO orderInfo, TransactionCargoDTO cargoInfo) {
        ConsignorDetailBO consignorDetailBO = new ConsignorDetailBO();
        if (orderInfo != null) {
        }
        if (cargoInfo != null) {
            //企业认证状态
            com.cy.pass.service.dto.base.Response<WebUserInfoDTO> resultData = webUserHandleService.getWebUserByCompanyId(cargoInfo.getCompanyId());

            com.cy.pass.service.dto.base.Response<CompanyInfoDTO> resultData1 = webUserHandleService.getCompanyDetail(cargoInfo.getCompanyId());
            if (resultData.isSuccess()) {
                if (resultData.getData() != null) {
                    consignorDetailBO.setAuthStatus(resultData.getData().getSubmitType());
                    consignorDetailBO.setAuthTime(DateUtil.dateFormat(resultData.getData().getAuditTime(), DateUtil.F_DATE));
                }
            }
            if (resultData1.isSuccess()) {
                if (resultData1.getData() != null) {
                    consignorDetailBO.setCompanyAddress(resultData1.getData().getCompanyAddress());
                }
            }
            consignorDetailBO.setCompanyName(cargoInfo.getCompanyName());//公司名称
            consignorDetailBO.setContactName(StrUtil.callJoin(cargoInfo.getContactName()));//发货人姓名
            consignorDetailBO.setMobile(ValidateUtil.hideMobile(cargoInfo.getContactMobilephone()));// 发货人手机号码
            consignorDetailBO.setTelephone(cargoInfo.getContactTelephone());
        }
        return consignorDetailBO;
    }

    /**
     * 查看订单详情
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/getOrderDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.LOOK_ORDER_DETAILS)
    @Log(type = LogEnum.LOOK_ORDER_DETAILS)
    public Object lookOrderDetails(HttpServletRequest request, HttpServletResponse response,
                                   Long orderId) {
        try {
            if (orderId == null) {
                return findErrorParam(response);
            }
            OrderAndCargoDTO orderAndCargoDTO = queryOrderService.getOrderDetails2(orderId);
            if (orderAndCargoDTO == null) {
                logger.error("调用order服务查询订单信息出错");
                return findJSonResponse(response, ApiResultCodeEnum.SER_20226);
            }
            TransactionInfoDTO orderDTO = orderAndCargoDTO.getOrderInfo();
            DistributeInfoDTO distributeDTO = null;
            if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                if (distributeDTO == null) {
                    logger.error("查看订单详情失败，派单信息不存在");
                    return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
                }
            }
            updRespHeadSuccess(response);
            OrderInfoDetailBO bo = buildOrderInfoDetailBo(orderDTO, orderAndCargoDTO.getCargoInfo(), distributeDTO);
            return bo;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("查看订单详情", e);
            }
        }
        return findException(response);
    }

    private OrderInfoDetailBO buildOrderInfoDetailBo(TransactionInfoDTO orderInfo, TransactionCargoDTO cargoInfo, DistributeInfoDTO distributeDTO) {
        OrderInfoDetailBO orderInfoDetailBO = new OrderInfoDetailBO();
        /** 订单信息 */
        if (orderInfo != null) {
            orderInfoDetailBO.setOrderId(orderInfo.getId());
            orderInfoDetailBO.setOrderNo(orderInfo.getOrderNumber());//订单号
            orderInfoDetailBO.setBidPrice(
                    SystemsUtil.buildAmountUnit(orderInfo.getRealNeedpayFair()));//成交价格(单位：元)

            orderInfoDetailBO.setInvoiceNum(orderInfo.getInvoiceNum());// 回单上传数量(从0开始)
            orderInfoDetailBO.setReceiptNum(orderInfo.getReceiptNum());// 评价标识(0未评价、1已评价)
            orderInfoDetailBO.setOrderLock(orderInfo.getOrderLock());//订单锁定状态(0正常、1司机发起订单锁定、2货主发起订单锁定)
            orderInfoDetailBO.setDriverAssessIdent(orderInfo.getDriverAssessIdent());// 订单锁定状态(0正常、1司机发起订单锁定、2货主发起订单锁定)
            orderInfoDetailBO.setActualStartTime(DateUtil.dateFormat(orderInfo.getInstallCargoTime(), DateUtil.F_DATETIME)); //实际装货时间

            orderInfoDetailBO.setTotalFare(SystemsUtil.getTotalFare(orderInfo.getRealNeedpayFair()));//总运费
            orderInfoDetailBO.setPrepayFare(SystemsUtil.showAppMoney(orderInfo.getNeedPrepayFair()));//预付
            orderInfoDetailBO.setPayeeAmount(SystemsUtil.showAppMoney(orderInfo.getNeedReceiveFair()));
            orderInfoDetailBO.setIsSignProtocol(SystemsUtil.isSignProtocol(SystemsUtil.bigAdd(orderInfo.getRealReceviedFair(),orderInfo.getRealReceiveOilFare())));
            orderInfoDetailBO.setIsShowProceeds(SystemsUtil.isShowProceeds(SystemsUtil.bigAdd(orderInfo.getNeedReceiveFair(),orderInfo.getNeedReceiveOilFare())));
//            //油卡费用
//            orderInfoDetailBO.setOilCard(SystemsUtil.getFare(orderInfo.getOilFare()));
//            orderInfoDetailBO.setCash(SystemsUtil.getFare(orderInfo.getCashFare()));
//            orderInfoDetailBO.setNeedReceiceOilCard(SystemsUtil.getFare(orderInfo.getNeedReceiveOilFare()));
//            orderInfoDetailBO.setNeedReceiveCash(SystemsUtil.getFare(orderInfo.get));
            if (orderInfo.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                orderInfoDetailBO.setOrderStatusCode(
                        SystemsUtil.findAppDistributeOrderCode(orderInfo.getTradeStart(),
                                orderInfo.getOrderLock(),
                                orderInfo.getTradeCancelOrigin(), orderInfo.getCancelStep()));//客户端订单状态编号
                orderInfoDetailBO.setOrderStatusName(
                        SystemsUtil.findAppDistributeOrderValue(orderInfo.getTradeStart(),
                                orderInfo.getOrderLock(),
                                orderInfo.getTradeCancelOrigin(), orderInfo.getCancelStep()));//客户端订单状态编号名称
                if (distributeDTO.getTotalFareAllotModel().byteValue() == Constants.DISTRIBUTE_MODEL_ALL) {
                    orderInfoDetailBO.setHaveCarrierSign(1);
                } else {
                    orderInfoDetailBO.setHaveCarrierSign(0);
                }
            } else {
                orderInfoDetailBO.setOrderStatusCode(
                        SystemsUtil.findAppOrderCode(orderInfo.getTradeStart(),
                                orderInfo.getOrderLock(),
                                orderInfo.getTradeCancelOrigin()));//客户端订单状态编号
                orderInfoDetailBO.setOrderStatusName(
                        SystemsUtil.findAppOrderValue(orderInfo.getTradeStart(),
                                orderInfo.getOrderLock(),
                                orderInfo.getTradeCancelOrigin()));//客户端订单状态编号名称
                orderInfoDetailBO.setHaveCarrierSign(0);
            }
            /** 货源信息 */
            if (cargoInfo != null) {
                if (orderInfo.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    orderInfoDetailBO.setCompanyName(distributeDTO.getSubcontractorName());//公司名称
                    orderInfoDetailBO.setCompanyAuthStatus((byte) 3);
                    orderInfoDetailBO.setConsignorName(StrUtil.callJoin(distributeDTO.getSubcontractorName()));//发货人姓名
                    orderInfoDetailBO.setConsignorMobile(distributeDTO.getSubcontractorMobilePhone());// 发货人手机号码
                } else {
                    //企业认证状态
                    com.cy.pass.service.dto.base.Response<WebUserInfoDTO> resultData = webUserHandleService.getWebUserByCompanyId(cargoInfo.getCompanyId());
                    if (resultData.isSuccess()) {
                        if (resultData.getData() != null) {
                            orderInfoDetailBO.setCompanyAuthStatus(resultData.getData().getSubmitType());
                        }
                    }
                    orderInfoDetailBO.setCompanyName(cargoInfo.getCompanyName());//公司名称
                    orderInfoDetailBO.setConsignorName(StrUtil.callJoin(cargoInfo.getContactName()));//发货人姓名
                    orderInfoDetailBO.setConsignorMobile(cargoInfo.getContactMobilephone());// 发货人手机号码
                }

                orderInfoDetailBO.setStartAddress(
                        SystemsUtil.buildAddress(cargoInfo.getStartProvince(),
                                cargoInfo.getStartCity(),
                                cargoInfo.getStartCounty()));//起始地
                orderInfoDetailBO.setEndAddress(SystemsUtil.buildAddress(cargoInfo.getEndProvince(),
                        cargoInfo.getEndCity(),
                        cargoInfo.getEndCounty()));//目的地
                orderInfoDetailBO.setRequestStartTime(SystemsUtil.cargoOrderStartTime(cargoInfo.getRequestStartTime()));//要求装货时间
                orderInfoDetailBO.setRequestEndTime(SystemsUtil.cargoOrderEndTime(cargoInfo.getRequestEndTime()));//要求卸货时间
                orderInfoDetailBO.setCargoName(cargoInfo.getCargoName());//货物名称
                orderInfoDetailBO.setWeight(
                        SystemsUtil.buildWeightUnit(cargoInfo.getCargoWeight()));//重量(单位：吨)
                orderInfoDetailBO.setVolume(
                        SystemsUtil.buildVolumeUnit(cargoInfo.getCargoCubage()));//体积(单位：方)
                orderInfoDetailBO.setCarLength(
                        SystemsUtil.buildCarLenUnit(cargoInfo.getRequestCarLen()));//要求的车长(单位：米)
                orderInfoDetailBO.setCarriageTypeName(
                        systemData.findCarriageTypeName(cargoInfo.getCarriageType()));//车厢类型名称
                orderInfoDetailBO.setVehicleTypeName(
                        systemData.findVehicleTypeName(cargoInfo.getVehicleType()));//车辆类型名称

                orderInfoDetailBO.setCargoRemark(cargoInfo.getRemark());
                orderInfoDetailBO.setPubTime(DateUtil.dateFormat(cargoInfo.getCreateTime(), DateUtil.F_DATETIME));//发布时间
            }
            if (orderInfo.getTradeStart().intValue() == SystemsUtil.SERVICE_ORDER_JYQX
                    || (orderInfo.getOrderLock() != null
                    && orderInfo.getOrderLock().intValue() == 1)) {
                orderInfoDetailBO.setCancelCause(orderInfo.getCancelCause());
            }
        }
        return orderInfoDetailBO;
    }

    /**
     * 订单状态变更
     *
     * @param request
     * @param response
     * @param orderId
     * @param orderStatus 状态(-1取消承运、1确定承运、2确定装货、3确定卸货)
     * @return
     */
    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_ORDER_STATUS)
    @Log(type = LogEnum.UPDATE_ORDER_STATUS)
    public Object updateOrderStatus(HttpServletRequest request, HttpServletResponse response,
                                    Long orderId, Integer orderStatus) {
        try {
            if (orderStatus == null || orderId == null) {
                return findErrorParam(response);
            }
            long driverId = findUserId(request);
            DriverUserInfoDTO driver = driverUserHandlerService.getDriverInfo(driverId);
            if (driver == null) {
                return findException(response);
            }
            switch (orderStatus.intValue()) {
                case -1:
                    break;
                case 1:
                    if (driver.getSubmitType().byteValue() != 3) {
                        logger.error("确定承运失败，司机信息未认证");
                        return findJSonResponse(response, ApiResultCodeEnum.SER_20217);
                    }
                    /** 司机承运签订分包协议 */
                    Object json = signProtocol(orderId, response);
                    if (json != null) {
                        return json;
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    return findErrorParam(response);
            }
            OrderAndCargoDTO orderAndCargo = queryOrderService.getOrderDetails(orderId);
            if (orderAndCargo == null) {
                logger.error("订单状态变更失败，订单信息不存在");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            Response<OrderStateRespDTO> result = queryOrderService.updateOrderStatus(driverId, orderStatus, orderId, driver.getName());
            boolean updateIndex = idSourceDTOService.updateIndex(String.valueOf(orderId), Constants.ORDER_FROM_KUAIDAO);
            if (!updateIndex) {
                logger.debug("调用同步运单状态服务失败");
            }
            if (result.isSuccess()) {
                if (orderStatus.intValue() == 1) {
                    //司机承运订单时保存订单路线
                    com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, orderAndCargo, Constants.ASSEPT_FOR_CARRIAGE);
                    if (!saveLineRes.isSuccess()) {
                        logger.debug("locationLineService服务调用失败");
                    }
                    //如果货源要求的装货日是今天，则还需要调用pass服务来保存订单路线
                    String requestTime = DateUtil.dateToStr(orderAndCargo.getCargoInfo().getRequestStartTime());
                    String today = DateUtil.dateToStr(new Date());
                    if (requestTime.equals(today)) {
                        com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, orderAndCargo, Constants.ASSEPT_FOR_CARRIAGE);
                        if (!saveDayLineRes.isSuccess()) {
                            logger.debug("driverDayLineService服务调用失败");
                        }
                    }
                }
                if (orderStatus.intValue() == 2) {//确认装货
                    /** 修改订单是否存在定位信息异常 */
                    transactionService.updateOrderWarn(orderAndCargo.getOrderInfo(), orderAndCargo.getCargoInfo(), 1);
                } else if (orderStatus.intValue() == 3) {//确认卸货
                    /** 修改订单是否存在定位信息异常 */
                    transactionService.updateOrderWarn(orderAndCargo.getOrderInfo(), orderAndCargo.getCargoInfo(), 2);
                }

                Map<String, Object> resultData = new HashMap<String, Object>();
                OrderStateRespDTO orderUpdateStateDTO = result.getData();
                //客户端订单状态编号
                resultData.put("orderStatusCode", SystemsUtil.findAppOrderCode(orderUpdateStateDTO.getTradeStart(),
                        orderUpdateStateDTO.getOrderLock(),
                        orderUpdateStateDTO.getTradeCancelOrigin()));
                //客户端订单状态编号名称
                resultData.put("orderStatusName", SystemsUtil.findAppOrderValue(orderUpdateStateDTO.getTradeStart(),
                        orderUpdateStateDTO.getOrderLock(),
                        orderUpdateStateDTO.getTradeCancelOrigin()));
                /** 订单状态变更的推送提醒 */
                updateOrderStatusPush(orderId, orderStatus, driver, orderAndCargo);
                updRespHeadSuccess(response);
                //承运、装货、卸货积分奖励-3.4版本
                if (orderStatus.intValue() == 1 || orderStatus.intValue() == 2 || orderStatus.intValue() == 3) {
                    String eventCode = "";
                    if (orderStatus.intValue() == 1) {
                        eventCode = LogEnum.DRIVER_ORDER_CHENGYUN;
                    } else if (orderStatus.intValue() == 2) {
                        eventCode = LogEnum.DRIVER_ORDER_ZHUANGHUO;
                    } else if (orderStatus.intValue() == 3) {
                        eventCode = LogEnum.DRIVER_ORDER_XIEHUO;
                    }
                    pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(request), Constants.CHECK_MODE_BY_EVENT, eventCode, null, null, convert2InSource(), null);
                }
                return resultData;
            }
            logger.error("调用订单order服务订单状态变更失败，失败信息={}", result.getMessage());
            //订单不存在或者已删除
            if (result.getCode() == CodeTable.ORDER_INVALID_CODE.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //该订单不属于该用户
            if (result.getCode() == CodeTable.ORDER_NO_BELONG_USER.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //订单已取消
            if (result.getCode() == CodeTable.ORDER_HAS_CANCEL.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            //订单已锁定
            if (result.getCode() == CodeTable.ORDER_HAS_LOCK.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20089);
            }
            //未找到更新数据
            if (result.getCode() == CodeTable.UPDATE_FAILD.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20202);
            }
            //参数不合法
            if (result.getCode() == CodeTable.INVALID_ARGS.getCode() || result.getCode() == CodeTable.ERROR.getCode()) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("订单状态变更", e);
            }
        }
        return findException(response);
    }

    /**
     * 司机承运签订分包协议
     *
     * @param orderId 订单id
     * @return
     * @author wyh
     */
    private Object signProtocol(Long orderId, HttpServletResponse response) {
        OrderAndCargoDTO orderAndCargoDTO = queryOrderService.getOrderDetails(orderId);
        if (orderAndCargoDTO == null) {
            return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
        }
        TransactionInfoDTO orderDTO = orderAndCargoDTO.getOrderInfo();
        if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_COMMON) {
            return null;
        }
        if (orderDTO.getDistributeId() == null) {
            logger.error("司机承运时签订协议失败，订单中的派单id为空,orderId={}", orderId);
            return findJSonResponse(response, ApiResultCodeEnum.SER_20227);
        }
        DistributeInfoDTO distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
        if (distributeDTO == null) {
            logger.error("司机承运时签订协议失败，订单中的派单信息不存在,orderId={}", orderId);
            return findJSonResponse(response, ApiResultCodeEnum.SER_20227);
        }
        if (distributeDTO.getTotalFareAllotModel().byteValue() != Constants.DISTRIBUTE_MODEL_ALL) {
            /** 运费和信息费，司机和分包商一起收费 */
            return null;
        }
        ScdProtocolBO proto = protocolService.signElectronicProtocol(null, orderAndCargoDTO, distributeDTO);
        if (proto == null) {
            return findJSonResponse(response, ApiResultCodeEnum.SER_20228);
        }
        TransactionElectronicContractDTO dto = new TransactionElectronicContractDTO();
        dto.setDocPath(proto.getDocUrl());
        dto.setTransactionId(orderId);
        dto.setContractType(Constants.PROTOCOL_DRIVER);
        dto.setContractCode(Constants.PROTOCOL_DRIVER_STR + proto.getOrderNumber());
        dto.setPartyAName(proto.getFirstName());
        dto.setPartyBName(proto.getSecondName());
        dto.setEffectiveBegin(new Date());
        Response<Long> result = receivablesService.saveContract(dto);
        if (!result.isSuccess()) {
            logger.error("司机承运签订分包协议失败，调用order服务保存电子合同失败，orderId={},返回信息={}", orderId, result.getMessage());
            return findJSonResponse(response, ApiResultCodeEnum.SER_20228);
        }
        if (result.getData() == null) {
            logger.error("司机承运签订分包协议失败，调用order服务保存电子合同失败，orderId={},返回保存的记录id为空", orderId);
            return findJSonResponse(response, ApiResultCodeEnum.SER_20228);
        }
        return null;
    }

    /**
     * 订单状态变更的推送提醒
     *
     * @param orderId     订单id
     * @param orderStatus 状态(-1取消承运、1确定承运、2确定装货、3确定卸货)
     * @param driver      司机信息
     * @return
     */
    private boolean updateOrderStatusPush(Long orderId, Integer orderStatus, DriverUserInfoDTO driver, OrderAndCargoDTO orderAndCargo) {
        try {
            TransactionInfoDTO orderDTO = orderAndCargo.getOrderInfo();
            TransactionCargoDTO cargoDTO = orderAndCargo.getCargoInfo();
            if (orderDTO == null) {
                logger.error("订单状态变更的推送提醒失败，订单信息不存在");
                return false;
            }
            DistributeInfoDTO distributeDTO = null;
            if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                if (distributeDTO == null) {
                    logger.error("订单状态变更的推送提醒失败，派单信息不存在");
                    return false;
                }
            }
            if (orderStatus.intValue() == -1) {/** 取消承运 */
                PushCYTZ info = new PushCYTZ();
                if (cargoDTO != null) {
                    info.setStartTime(cargoDTO.getRequestStartTime());
                    info.setStartAddrCity(cargoDTO.getStartCity());
                    info.setEndAddrCity(cargoDTO.getEndCity());
                    info.setCargoName(cargoDTO.getCargoName());
                }
                if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    info.setUserId(distributeDTO.getSubcontractorUserId());
                    info.setBusinessId(distributeDTO.getId());
                    return pushSendService.jjcytzPushSubCon(info);
                } else {
                    info.setUserId(orderDTO.getDeployUserid());
                    info.setBusinessId(orderDTO.getId());
                    return pushSendService.jjcytzPushOwn(info);
                }
            } else if (orderStatus.intValue() == 1) {/** 确定承运 */
                /** 确定承运保存承运动态信息 */
                saveFeedInfo(cargoDTO, driver);
                PushCYTZ info = new PushCYTZ();
                if (cargoDTO != null) {
                    info.setStartTime(cargoDTO.getRequestStartTime());
                    info.setStartAddrCity(cargoDTO.getStartCity());
                    info.setEndAddrCity(cargoDTO.getEndCity());
                    info.setCargoName(cargoDTO.getCargoName());
                }
                if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    /** 分包订单的推送 */
                    info.setBusinessId(distributeDTO.getId());
                    info.setBusinessId2(orderId);
                    /** 推送给分包商 */
                    info.setUserId(distributeDTO.getSubcontractorUserId());
                    pushSendService.cytzPushSubCon(info);
                    /** 推送给货主 */
                    info.setUserId(orderDTO.getDeployUserid());
                    return pushSendService.cytzPushSubCon(info);
                } else {
                    /** 普通订单的推送 */
                    info.setUserId(orderDTO.getDeployUserid());
                    info.setBusinessId(orderId);
                    return pushSendService.cytzPushOwn(info);
                }
            } else if (orderStatus.intValue() == 2) {/** 确定装货 */
                PushZHTZ info = new PushZHTZ();
                if (cargoDTO != null) {
                    info.setStartAddrCity(cargoDTO.getStartCity());
                    info.setEndAddrCity(cargoDTO.getEndCity());
                    info.setCargoName(cargoDTO.getCargoName());
                }
                info.setUserId(orderDTO.getDeployUserid());
                if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    info.setBusinessId(distributeDTO.getId());
                    info.setBusinessId2(orderId);
                    return pushSendService.zhtzSubConPushOwn(info);
                } else {
                    info.setBusinessId(orderId);
                    return pushSendService.zhtzCommonPushOwn(info);
                }
            } else if (orderStatus.intValue() == 3) {/** 确定卸货 */
                PushZHTZ info = new PushZHTZ();
                if (cargoDTO != null) {
                    info.setStartAddrCity(cargoDTO.getStartCity());
                    info.setEndAddrCity(cargoDTO.getEndCity());
                    info.setCargoName(cargoDTO.getCargoName());
                }
                info.setUserId(orderDTO.getDeployUserid());
                if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                    info.setBusinessId(distributeDTO.getId());
                    info.setBusinessId2(orderId);
                    return pushSendService.xhtzSubConPushOwn(info);
                } else {
                    info.setBusinessId(orderId);
                    return pushSendService.xhtzCommonPushOwn(info);
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("订单状态变更的推送提醒出现异常", e);
            return false;
        }
    }

    /**
     * 确定承运保存承运动态信息
     *
     * @param cargoDTO
     * @param driver
     * @author wyh
     */
    private void saveFeedInfo(TransactionCargoDTO cargoDTO, DriverUserInfoDTO driver) {
        try {
            UserFeedInfoDTO addDTO = new UserFeedInfoDTO();
            /** 1 货主动态 2 司机动态 */
            addDTO.setEventKind(new Byte("2"));
            /** 1 确定承运 2 支付运费 */
            addDTO.setFeedType(1);
            /** 浙A8**69的饶*婷承运了杭州风驰****公司的服装包3.50吨 */
            StringBuffer feedContent = new StringBuffer();
            if (StringUtils.isNotBlank(driver.getCarNumber())) {
                feedContent.append(driver.getCarNumber());
                if (driver.getCarNumber().length() >= 5) {
                    feedContent.replace(3, 5, "**");
                }
                feedContent.append("的");
            }
            if (StringUtils.isNotBlank(driver.getName())) {
                StringBuffer sbName = new StringBuffer();
                sbName.append(driver.getName());
                if (driver.getName().length() >= 2) {
                    sbName.replace(1, 2, "*");
                }
                feedContent.append(sbName.toString());
            }
            feedContent.append("承运了");
            if (StringUtils.isNotBlank(cargoDTO.getCompanyName())) {
                int companyNameLen = cargoDTO.getCompanyName().length();
                int len = companyNameLen / 3;
                if (companyNameLen % 3 > 0) {
                    len += 1;
                }
                StringBuffer sbName = new StringBuffer();
                sbName.append(cargoDTO.getCompanyName());
                if (companyNameLen > 5) {
                    String str = "";
                    int strLen = companyNameLen - len - 2;
                    for (int i = 0; i < strLen; i++) {
                        str += "*";
                    }
                    sbName.replace(len, companyNameLen - 2, str);
                }
                feedContent.append(sbName.toString());
            }
            feedContent.append("的");
            if (StringUtils.isNotBlank(cargoDTO.getCargoName())) {
                feedContent.append(cargoDTO.getCargoName());
            }
            if (cargoDTO.getCargoWeight() != null
                    && cargoDTO.getCargoWeight().compareTo(BigDecimal.ZERO) == 1) {
                feedContent.append(cargoDTO.getCargoWeight().toString()).append("吨");
            } else {
                if (cargoDTO.getCargoCubage() != null
                        && cargoDTO.getCargoCubage().compareTo(BigDecimal.ZERO) == 1) {
                    feedContent.append(cargoDTO.getCargoCubage().toString()).append("方");
                }
            }

            addDTO.setFeedContent(feedContent.toString());
            addDTO.setDriverId(driver.getId());
            addDTO.setCompanyId(cargoDTO.getCompanyId());
            userFeedInfoService.add(addDTO);
        } catch (Exception e) {
            logger.error("确定承运保存承运动态信息出现异常", e);
        }
    }

    /**
     * 查询订单列表
     *
     * @param orderStatus 列表状态(0全部订单、1待承运、2承运中)
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/transactionOrderList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.TRANSACTION_ORDER_LIST)
    @Log(type = LogEnum.TRANSACTION_ORDER_LIST)
    public Object transactionOrderList(HttpServletRequest request, HttpServletResponse response,
                                       Integer orderStatus, Integer page) {
        try {
            if (orderStatus == null || page == null || page.intValue() < 1) {
                return findErrorParam(response);
            }
            switch (orderStatus.intValue()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    return findErrorParam(response);
            }
            long driverId = findUserId(request);
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.queryOrderList(driverId, orderStatus, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder1(pageBase);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("查询订单列表出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 待承运订单分页列表（司机3.4版本）
     *
     * @param page 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageOrderWaitCarrier", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_ORDER_WAIT_CARRIER)
    @Log(type = LogEnum.PAGE_ORDER_WAIT_CARRIER)
    public Object pageOrderWaitCarrier(HttpServletResponse response, Integer page) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_ORDER_WAIT_CARRIER.getRemark(), page, driverId);
            if (page == null) {
                logger.error("{}的失败，参数page为空", LogEnum.PAGE_ORDER_WAIT_CARRIER.getRemark());
                return findErrorParam(response);
            }
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.queryOrderList(driverId, Constants.WAIT_CARRIER, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder2(pageBase);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.PAGE_ORDER_WAIT_CARRIER.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 待装货订单分页列表（司机3.4版本）
     *
     * @param page 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageOrderWaitLoad", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_ORDER_WAIT_LOAD)
    @Log(type = LogEnum.PAGE_ORDER_WAIT_LOAD)
    public Object pageOrderWaitLoad(HttpServletResponse response, Integer page) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_ORDER_WAIT_LOAD.getRemark(), page, driverId);
            if (page == null) {
                logger.error("{}的失败，参数page为空", LogEnum.PAGE_ORDER_WAIT_LOAD.getRemark());
                return findErrorParam(response);
            }
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.queryOrderList(driverId, Constants.WAIT_LOAD, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder2(pageBase);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.PAGE_ORDER_WAIT_LOAD.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 待卸货订单分页列表（司机3.4版本）
     *
     * @param page 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageOrderWaitUnload", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_ORDER_WAIT_UNLOAD)
    @Log(type = LogEnum.PAGE_ORDER_WAIT_UNLOAD)
    public Object pageOrderWaitUnload(HttpServletResponse response, Integer page) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_ORDER_WAIT_UNLOAD.getRemark(), page, driverId);
            if (page == null) {
                logger.error("{}的失败，参数page为空", LogEnum.PAGE_ORDER_WAIT_UNLOAD.getRemark());
                return findErrorParam(response);
            }
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.queryOrderList(driverId, Constants.WAIT_UNLOAD, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder2(pageBase);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.PAGE_ORDER_WAIT_UNLOAD.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 待评价订单分页列表（司机3.4版本）
     *
     * @param page 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageOrderWaitEval", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_ORDER_WAIT_EVAL)
    @Log(type = LogEnum.PAGE_ORDER_WAIT_EVAL)
    public Object pageOrderWaitEval(HttpServletResponse response, Integer page) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_ORDER_WAIT_EVAL.getRemark(), page, driverId);
            if (page == null) {
                logger.error("{}的失败，参数page为空", LogEnum.PAGE_ORDER_WAIT_EVAL.getRemark());
                return findErrorParam(response);
            }
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.queryOrderList(driverId, Constants.WAIT_EVALUATION, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder2(pageBase);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.PAGE_ORDER_WAIT_EVAL.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 其他订单分页列表（司机3.4版本）
     *
     * @param page 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageOrderWaitOther", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_ORDER_WAIT_OTHER)
    @Log(type = LogEnum.PAGE_ORDER_WAIT_OTHER)
    public Object pageOrderWaitOther(HttpServletResponse response, Integer page) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_ORDER_WAIT_OTHER.getRemark(), page, driverId);
            if (page == null) {
                logger.error("{}的失败，参数page为空", LogEnum.PAGE_ORDER_WAIT_OTHER.getRemark());
                return findErrorParam(response);
            }
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.queryOrderList(driverId, Constants.OTHER, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder2(pageBase);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.PAGE_ORDER_WAIT_OTHER.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 统计订单数量（司机3.4版本）
     *
     * @return
     */
    @RequestMapping(value = "/countOrderNum", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.COUNT_ORDER_NUM)
    @Log(type = LogEnum.COUNT_ORDER_NUM)
    public Object countOrderNum(HttpServletResponse response) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：driverId={}", LogEnum.COUNT_ORDER_NUM.getRemark(), driverId);
            /** 待承运的订单数量 */
            Integer waitCarrierNum = queryOrderService.countOrderNumByState(driverId, Constants.WAIT_CARRIER);
            if (waitCarrierNum == null) {
                waitCarrierNum = 0;
            }
            /**
             * 待装货的订单数量
             */
            Integer waitLoadNum = queryOrderService.countOrderNumByState(driverId, Constants.WAIT_LOAD);
            if (waitLoadNum == null) {
                waitLoadNum = 0;
            }
            /**
             * 待卸货的订单数量
             */
            Integer waitUnloadNum = queryOrderService.countOrderNumByState(driverId, Constants.WAIT_UNLOAD);
            if (waitUnloadNum == null) {
                waitUnloadNum = 0;
            }
            /**
             * 待评价的订单数量
             */
            Integer waitEvalNum = queryOrderService.countOrderNumByState(driverId, Constants.WAIT_EVALUATION);
            if (waitEvalNum == null) {
                waitEvalNum = 0;
            }
            CountOrderNumBO result = new CountOrderNumBO();
            result.setWaitLoadNum(waitLoadNum);
            result.setWaitCarrierNum(waitCarrierNum);
            result.setWaitEvalNum(waitEvalNum);
            result.setWaitUnloadNum(waitUnloadNum);
            updRespHeadSuccess(response);
            return result;
        } catch (Exception e) {
            logger.error("{}出现异常。", LogEnum.COUNT_ORDER_NUM.getRemark(), e);
        }
        return findException(response);
    }

    /**
     * 订单详情（司机3.4版本）
     *
     * @param response
     * @param orderId  订单id
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/orderAllDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ORDER_ALL_DETAILS)
    @Log(type = LogEnum.ORDER_ALL_DETAILS)
    public Object orderAllDetails(HttpServletResponse response, Long orderId) {
        try {
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：orderId={}", LogEnum.ORDER_ALL_DETAILS.getRemark(), orderId);
            if (orderId == null) {
                logger.error("{}失败，参数orderId为空", LogEnum.ORDER_ALL_DETAILS.getRemark());
                return findErrorParam(response);
            }
            OrderAndCargoDTO orderAndCargoDTO = queryOrderService.getOrderDetails2(orderId);
            if (orderAndCargoDTO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20226);
            }
            TransactionInfoDTO orderDTO = orderAndCargoDTO.getOrderInfo();
            DistributeInfoDTO distributeDTO = null;
            Long ownerUserId = null;
            /** 货主头像路径 */
            String ownerHeadImg = "";
            if (orderDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
                distributeDTO = queryOrderService.getDistributeInfo(orderDTO.getDistributeId());
                if (distributeDTO == null) {
                    return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
                }
                ownerUserId = distributeDTO.getSubcontractorUserId();
                ownerHeadImg = distributeDTO.getSubcontractorHeadImg();
            } else {
                WebUserInfoDTO owner = webUserHandleService.getByCompanyId(orderDTO.getCompanyId());
                if (owner == null) {
                    return findJSonResponse(response, ApiResultCodeEnum.SER_20226);
                }
                ownerUserId = owner.getId();
                ownerHeadImg = owner.getHeadPortraitImgPath();
            }
            OwnerItemStatDTO ownerItemStatDTO = ownerItemService.getByOwnerId(ownerUserId);
            ApplyPayInfoDTO applyPayInfoDTO = orderPayFService.getPayInfo(orderId);
            updRespHeadSuccess(response);
            return OrderConvert.orderDetailsBO(orderAndCargoDTO, distributeDTO, ownerItemStatDTO, ownerHeadImg, applyPayInfoDTO);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.ORDER_ALL_DETAILS.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 搜索订单分页列表（司机3.4版本）
     *
     * @param page          页码从1开始
     * @param searchContent 搜索内容
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageSearchOrder", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_SEARCH_ORDER)
    @Log(type = LogEnum.PAGE_SEARCH_ORDER)
    public Object pageSearchOrder(HttpServletResponse response, Integer page, String searchContent) {
        try {
            Long driverId = findUserId();
            if (logger.isDebugEnabled())
                logger.debug("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_SEARCH_ORDER.getRemark(), page, driverId);
            if (page == null || StringUtils.isBlank(searchContent)) {
                logger.error("{}的失败，参数page,searchContent为空", LogEnum.PAGE_SEARCH_ORDER.getRemark());
                return findErrorParam(response);
            }
            PageBase<DistributeOrderDTO> pageBase = queryOrderService.searchOrderList(driverId, searchContent, page);
            if (pageBase == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20300);
            }
            updRespHeadSuccess(response);
            return OrderConvert.pageOrder2(pageBase);
        } catch (Exception e) {
            logger.error("{}出现异常", LogEnum.PAGE_SEARCH_ORDER.getRemark(), e);
            return findException(response);
        }
    }
}

