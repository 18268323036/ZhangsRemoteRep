package com.cy.driver.action;

import com.cy.driver.domain.ScdProtocolBO;
import com.cy.driver.service.ViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 页面请求
 * Created by wyh on 2016/1/14.
 */
@Scope("prototype")
@Controller
@RequestMapping("/view")
public class ViewAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ViewAction.class);
    @Resource
    private ViewService viewService;

    /**
     * 司机承运签订协议
     * @param orderId 订单id
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/scdProtocol", method = RequestMethod.GET)
    public ModelAndView scdProtocol(Long orderId){
        ModelAndView mdv = new ModelAndView();
        mdv.setViewName("/scdProtocol2");
        mdv.addObject("cssType", 2);
        ScdProtocolBO protocol = null;
        try {
            if (orderId != null) {
                protocol = viewService.converterScdProtocol(orderId, null, null);
            }
        } catch (Exception e) {
            LOG.error("司机承运签订协议出现异常", e);
        }
        if (protocol == null) {
            protocol = new ScdProtocolBO();
        }
        mdv.addObject("protocol", protocol);
        return mdv;
    }






}
