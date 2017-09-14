package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.OrderInvoiceBO;
import com.cy.driver.domain.OrderReceiptBO;
import com.cy.driver.service.OrdersECTNService;
import com.cy.driver.service.PointFService;
import com.cy.driver.service.QueryOrderService;
import com.cy.order.service.dto.OrderInvoiceDTO;
import com.cy.order.service.dto.OrderReceiptPathDTO;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.base.CodeTable;
import com.cy.order.service.dto.base.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/28.
 * 电子单跟踪类
 */
@Scope("prototype")
@RestController("orderECTNAction")
public class OrderECTNAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(PactDriverAction.class);

    @Resource
    private OrdersECTNService ordersECTNService;
    @Resource
    private PointFService pointFService;
    @Resource
    private QueryOrderService queryOrderService;

    /**
     * 查看回单
     * @param request
     * @param response
     * @param orderId  订单ID
     * @return
     */
    @RequestMapping(value = "/lookReceipt", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.LOOK_RECEIPT)
    @Log(type = LogEnum.LOOK_RECEIPT)
    public Object lookReceipt(HttpServletRequest request, HttpServletResponse response,
                              Long orderId) {
        try{
            if(orderId == null){
                return findErrorParam(response);
            }
            Response<List<OrderReceiptPathDTO>> result = ordersECTNService.lookReceipt(orderId);
            if(result.isSuccess()){

                Map<String, Object> resultData = new HashMap<String, Object>();
                if(result.getData()==null||result.getData().size()==0){
                    resultData.put("invoiceList", null);
                    return null;
                }
                List<OrderReceiptBO>  resultList = new ArrayList<OrderReceiptBO>();
                for(OrderReceiptPathDTO orderReceiptPathDTO : result.getData()){
                    OrderReceiptBO orderReceiptBO = new OrderReceiptBO();
                    orderReceiptBO.setImgFileMd5(orderReceiptPathDTO.getReceiptPath());
                    orderReceiptBO.setReceiptId(orderReceiptPathDTO.getId());
                    resultList.add(orderReceiptBO);
                }
                resultData.put("invoiceList", resultList);
                updRespHeadSuccess(response);
                return resultData;
            }
            //该订单不属于该用户
            if(result.getCode()== CodeTable.ORDER_NO_BELONG_USER.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //订单不存在或者已删除
            if(result.getCode()== CodeTable.ORDER_INVALID_CODE.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //订单已经取消
            if(result.getCode()== CodeTable.ORDER_HAS_CANCEL.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            //订单已锁定
            if(result.getCode()== CodeTable.ORDER_HAS_LOCK.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20089);
            }
            if(result.getCode()== CodeTable.INVALID_ARGS.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("查看回单出错", e);
            }
        }
        return findException(response);
    }

    /**
     *上传回单
     * @param request
     * @param response
     * @param orderId  订单ID
     * @param fileMd5s 文件md5(多个以”,”隔开)
     * @return
     */
    @RequestMapping(value = "/uploadReceipt", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPLOAD_RECEIPT)
    @Log(type = LogEnum.UPLOAD_RECEIPT)
    public Object uploadReceipt(HttpServletRequest request, HttpServletResponse response,
                                Long orderId, String fileMd5s) {
        try{
            if(orderId == null||StringUtils.isBlank(fileMd5s)){
                return findErrorParam(response);
            }
            String fileMd5Str=fileMd5s;
            //去掉结尾,
            if(fileMd5s.endsWith(",")){
                fileMd5Str = fileMd5s.substring(0,fileMd5s.length()-1);
            }
            // 文件字符串集合
            List<String> newMd5Str = null;
            String[] FileMd5Ary = fileMd5Str.split(",");
            if(FileMd5Ary.length>0){
                newMd5Str = new ArrayList<String>();
                for(String str:FileMd5Ary){
                    newMd5Str.add(str);
                }
            }
            TransactionInfoDTO orderDTO = queryOrderService.getTransactionInfo(orderId);
            if (orderDTO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
            }
            Response<Boolean> result = ordersECTNService.uploadRecTeipt(orderId, newMd5Str);
            if(result.isSuccess()){
                updRespHeadSuccess(response);
                /** 该订单第一次上传回单才有积分奖励 */
                if (orderDTO.getReceiptNum() == null || orderDTO.getReceiptNum().intValue() <= 0) {
                    //上传回单积分奖励-3.4版本
                    pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT, LogEnum.UPLOAD_RECEIPT.getEventCode(),null,null,convert2InSource(),null);
                }
                return null;
            }
            //该订单不属于该用户
            if(result.getCode()== CodeTable.ORDER_NO_BELONG_USER.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //订单不存在或者已删除
            if(result.getCode()== CodeTable.ORDER_INVALID_CODE.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //订单已经取消
            if(result.getCode()== CodeTable.ORDER_HAS_CANCEL.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            //订单已锁定
            if(result.getCode()== CodeTable.ORDER_HAS_LOCK.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20089);
            }
            if(result.getCode()== CodeTable.INVALID_ARGS.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("上传回单出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 查看发货单
     * @param request
     * @param response
     * @param orderId  订单ID
     * @return
     */
    @RequestMapping(value = "/lookInvoice", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.LOOK_INVOICE)
    @Log(type = LogEnum.LOOK_INVOICE)
    public Object lookInvoice(HttpServletRequest request, HttpServletResponse response,
                              Long orderId) {
        try{
            if(orderId == null){
                return findErrorParam(response);
            }
            Response<OrderInvoiceDTO> result = ordersECTNService.lookInvoice(orderId);
            if(result.isSuccess()){
                if(result.getData()==null){
                    updRespHeadSuccess(response);
                    return null;
                }
                Map<String, Object> resultData = new HashMap<String, Object>();
                resultData.put("invoiceNo", result.getData().getInvoiceNo());
                if(result.getData().getInfoList()==null||result.getData().getInfoList().size()==0){
                    updRespHeadSuccess(response);
                    return resultData;
                }
                List<OrderInvoiceBO>  resultList = new ArrayList<OrderInvoiceBO>();
                for(OrderReceiptPathDTO orderReceiptPathDTO : result.getData().getInfoList()){
                    OrderInvoiceBO orderInvoiceBO = new OrderInvoiceBO();
                    orderInvoiceBO.setImgFileMd5(orderReceiptPathDTO.getReceiptPath());
                    orderInvoiceBO.setInvoiceId(orderReceiptPathDTO.getId());
                    resultList.add(orderInvoiceBO);
                }
                resultData.put("invoiceList", resultList);
                updRespHeadSuccess(response);
                return resultData;
            }
            //该订单不属于该用户
            if(result.getCode()== CodeTable.ORDER_NO_BELONG_USER.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //订单不存在或者已删除
            if(result.getCode()== CodeTable.ORDER_INVALID_CODE.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //订单已经取消
            if(result.getCode()== CodeTable.ORDER_HAS_CANCEL.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            //订单已锁定
            if(result.getCode()== CodeTable.ORDER_HAS_LOCK.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20089);
            }
            if(result.getCode()== CodeTable.INVALID_ARGS.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("查看发货单出错", e);
            }
        }
        return findException(response);
    }

    /**
     * 上传发货单
     * @param request
     * @param response
     * @param orderId  订单ID
     * @param invoiceNo 发货单号
     * @param fileMd5s 文件md5(多个以”,”隔开)
     * @return
     */
    @RequestMapping(value = "/uploadInvoice", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPLOAD_INVOICE)
    @Log(type = LogEnum.UPLOAD_INVOICE)
    public Object uploadInvoice(HttpServletRequest request, HttpServletResponse response,
                                Long orderId, String invoiceNo, String fileMd5s) {
        try{
            if(orderId == null||StringUtils.isBlank(fileMd5s)){
                return findErrorParam(response);
            }
            String fileMd5Str=fileMd5s;
            //去掉结尾,
            if(fileMd5s.endsWith(",")){
                fileMd5Str = fileMd5s.substring(0,fileMd5s.length()-1);
            }

            // 文件字符串集合
            List<String> newMd5Str = null;
            String[] FileMd5Ary = fileMd5Str.split(",");
            if(FileMd5Ary.length>0){
                newMd5Str = new ArrayList<String>();
                    for(String str:FileMd5Ary){
                        newMd5Str.add(str);
                    }
            }
            TransactionInfoDTO orderDTO = queryOrderService.getTransactionInfo(orderId);
            if (orderDTO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20085);
            }
            Response<Boolean> result = ordersECTNService.uploadInvoice(orderId, invoiceNo,newMd5Str);
            if(result.isSuccess()){
                updRespHeadSuccess(response);
                /** 该订单第一次上传发货单 */
                if (orderDTO.getInvoiceNum() == null || orderDTO.getInvoiceNum().intValue() <= 0) {
                    //上传发货单积分奖励-3.4版本
                    pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT, LogEnum.UPLOAD_INVOICE.getEventCode(),null,null,convert2InSource(),null);
                }
                return null;
            }
            //该订单不属于该用户
            if(result.getCode()== CodeTable.ORDER_NO_BELONG_USER.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //订单不存在或者已删除
            if(result.getCode()== CodeTable.ORDER_INVALID_CODE.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //订单已经取消
            if(result.getCode()== CodeTable.ORDER_HAS_CANCEL.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            //订单已锁定
            if(result.getCode()== CodeTable.ORDER_HAS_LOCK.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20089);
            }
            if(result.getCode()== CodeTable.INVALID_ARGS.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("上传发货单出错", e);
            }
        }
        return findException(response);
    }

}
