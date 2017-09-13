package com.cy.driver.action;

import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.QuoteHistoryInfoBO;
import com.cy.driver.service.MyQuoteInfoHandleService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/22.
 * 报价信息处理
 */
@Scope("prototype")
@Controller("quoteHandlerAction")
public class QuoteHandlerAction extends BaseAction{

    private Logger LOG = LoggerFactory.getLogger(QuoteHandlerAction.class);

    @Resource
    private MyQuoteInfoHandleService myQuoteInfoHandleService;

    /**
     * 报价历史
     *
     * @param request
     * @param response
     * @param cargoId
     * @return
     */
    @RequestMapping(value = "/queryQuoteHistory", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_QUOTE_HISTORY)
    @Log(type = LogEnum.QUERY_QUOTE_HISTORY)
    public Object queryQuoteHistory(HttpServletRequest request, HttpServletResponse response, long cargoId) {
        try {
            //不合法
            if (cargoId <= 0) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            Response<List<QuoteHistoryInfoBO>> resultData = myQuoteInfoHandleService.queryQuoteHistory(cargoId, findUserId(request));
            if(resultData.isSuccess()){
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("quoteHistoryList", resultData.getData());
                updRespHeadSuccess(response);
                return resultMap;
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("报价历史列表出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }
}
