package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.service.ProblemService;
import com.cy.order.service.dto.base.CodeTable;
import com.cy.order.service.dto.base.Response;
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
import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 */
@Scope("prototype")
@RestController("orderProblemAction")
public class OrderProblemAction  extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(PactDriverAction.class);

    @Resource
    private ProblemService problemService;

    /**
     * 问题上报
     * @param request
     * @param response
     * @param orderId  订单ID
     * @return
     */
    @RequestMapping(value = "/submitOrderProblem", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SUBMIT_ORDER_PROBLEM)
    @Log(type = LogEnum.SUBMIT_ORDER_PROBLEM)
    public Object submitOrderProblem(HttpServletRequest request, HttpServletResponse response,
                                     Long orderId, String content, String imgUrl) {
        try{
            if(orderId == null){
                return findErrorParam(response);
            }
            String fileMd5Str=imgUrl;
            //去掉结尾,
            if(imgUrl.endsWith(",")){
                fileMd5Str = imgUrl.substring(0,imgUrl.length()-1);
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
            Response<Boolean> result = problemService.saveProblem(findUserId(request),orderId,content,newMd5Str );
            if(result.isSuccess()){
                if(result.getData()){
                    updRespHeadSuccess(response);
                    return null;
                }else{
                    return findHandleFail(response);
                }
            }
            //订单不存在或者已删除
            if(result.getCode()== CodeTable.ORDER_INVALID_CODE.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20085);
            }
            //该订单不属于该用户
            if(result.getCode()== CodeTable.ORDER_NO_BELONG_USER.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20087);
            }
            //订单已取消
            if(result.getCode()== CodeTable.ORDER_HAS_CANCEL.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20088);
            }
            if(result.getCode()== CodeTable.INVALID_ARGS.getCode()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error(" 问题上报出错", e);
            }
        }
        return findException(response);
    }



}
