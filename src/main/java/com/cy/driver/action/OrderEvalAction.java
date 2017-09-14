package com.cy.driver.action;

import com.cy.driver.action.convert.OrderEvalConvert;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.OrderAssess;
import com.cy.driver.service.*;
import com.cy.order.service.dto.OrderAndCargoDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.assess.OrderAssessDTO;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.OwnerItemStatDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单评价
 * Created by wyh on 2016/4/23.
 */
@Scope("prototype")
@RestController("orderEvalAction")
public class OrderEvalAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(OrderEvalAction.class);

    @Resource
    private DriverUserHandlerService driverUserHandlerService;
    @Resource
    private AssessService assessService;
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private OwnerItemService ownerItemService;
    @Resource
    private PointFService pointFService;
    @Resource
    private IdSourceDTOService idSourceDTOService;

    /**
     * 订单评价详情（司机3.4版本）
     * @param response
     * @param orderId 订单id
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/orderEvalDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ORDER_EVAL_DETAILS)
    @Log(type = LogEnum.ORDER_EVAL_DETAILS)
    public Object orderEvalDetails(HttpServletResponse response, Long orderId) {
        try {
            Long driverId = findUserId();
            if (LOG.isDebugEnabled())
                LOG.debug("{}的请求参数：orderId={}", LogEnum.ORDER_EVAL_DETAILS.getRemark(), orderId);
            if (orderId == null) {
                LOG.error("{}失败，orderId必填", LogEnum.ORDER_EVAL_DETAILS.getRemark());
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
            OrderAssessDTO orderAssessDTO = null;
            if (orderDTO.getDriverAssessIdent() != null
                    && orderDTO.getDriverAssessIdent().intValue() == 1) {
                orderAssessDTO = assessService.commonByDriver(orderDTO.getId());
            }
            updRespHeadSuccess(response);
            return OrderEvalConvert.orderEvalDetailsBO(orderAndCargoDTO, distributeDTO, ownerItemStatDTO, ownerHeadImg, orderAssessDTO);
        } catch (Exception e) {
            LOG.error("{}出现异常", LogEnum.ORDER_EVAL_DETAILS.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 保存订单评价（司机3.4版本）
     * @param response
     * @param orderId 订单id，必填
     * @param eval 评价分数 3 好评、6 中评、9差评，必填
     * @param evalContent 评价内容
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/saveOrderEval", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAVE_ORDER_EVAL)
    @Log(type = LogEnum.SAVE_ORDER_EVAL)
    public Object saveOrderEval(HttpServletResponse response, Long orderId, Integer eval, String evalContent) {
        try {
            Long driverId = findUserId();
            if (LOG.isDebugEnabled())
                LOG.debug("{}的请求参数：orderId={}, eval={}, evalContent={}, driverId={}", LogEnum.SAVE_ORDER_EVAL.getRemark(), orderId, eval, evalContent, driverId);
            if (orderId == null || eval == null) {
                LOG.error("{}失败，orderId或者eval必填", LogEnum.SAVE_ORDER_EVAL.getRemark());
                return findErrorParam(response);
            }
            DriverUserInfoDTO driverInfo = driverUserHandlerService.getDriverInfo(driverId);
            if (driverInfo == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20008);
            }
            OrderAssess orderAssess = new OrderAssess();
            orderAssess.setOrderId(orderId);
            orderAssess.setOrgUserId(driverId);
            orderAssess.setOrgUserName(driverInfo.getName());
            orderAssess.setAssessScore(eval);
            orderAssess.setContent(evalContent);
            orderAssess.setNameHidden((byte)0);
            assessService.saveDriverAssess(orderAssess);
            boolean updateIndex = idSourceDTOService.updateIndex(String.valueOf(orderId), Constants.ORDER_FROM_KUAIDAO);
            if (!updateIndex) {
                LOG.debug("调用同步运单状态服务失败");
            }
            updRespHeadSuccess(response);
            //订单评价积分奖励-3.4版本
            pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                    LogEnum.SAVE_ORDER_EVAL.getEventCode(), null, null, convert2InSource(),null);
            return null;
        } catch (Exception e) {
            LOG.error("{}出现异常", LogEnum.SAVE_ORDER_EVAL.getRemark(), e);
            return findException(response);
        }
    }
}
