package com.cy.driver.action;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 兑换码
 * Created by wyh on 2016/4/25.
 */
@Scope("prototype")
@Controller("conversionCodeAction")
public class ConversionCodeAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ConversionCodeAction.class);

    /**
     * 兑换兑换码
     * @param response
     * @param code
     * @return
     */
    @RequestMapping(value = "/convertCode", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.CONVERT_CODE)
    @Log(type = LogEnum.CONVERT_CODE)
    public Object convertCode(HttpServletResponse response, String code) {
        try {
            if (StringUtils.isBlank(code)) {
                LOG.error("{}失败，参数code必填", LogEnum.CONVERT_CODE.getRemark());
                return findErrorParam(response);
            }
            Long driverId = findUserId();
            if (LOG.isDebugEnabled())
                LOG.debug("{}的请求参数：code={},driverId={}", code, driverId);
            return findJSonResponse(response, ApiResultCodeEnum.RULE_30200);
        } catch (Exception e) {
            LOG.error("{}出现异常", LogEnum.CONVERT_CODE.getRemark());
            return findException(response);
        }
    }
}
