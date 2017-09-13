package com.cy.driver.action;

import com.alibaba.fastjson.JSON;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.CollectFreightBO;
import com.cy.driver.domain.CollectFreightListBO;
import com.cy.driver.service.QueryOrderService;
import com.cy.order.service.dto.CollectFreightDTO;
import com.cy.order.service.dto.base.CodeTable;
import com.cy.order.service.dto.base.PageResult;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 * 运费处理
 */
@Scope("prototype")
@Controller("fareHandlerAction")
public class FareHandlerAction extends BaseAction {
    private Logger LOG = LoggerFactory.getLogger(FareHandlerAction.class);

    @Resource
    private QueryOrderService queryOrderService;


    /**
     * 待收运费列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/collectFreightList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.COLLECT_FREIGHT_LIST)
    @Log(type = LogEnum.COLLECT_FREIGHT_LIST)
    public Object dialNetworkPhone(HttpServletRequest request, HttpServletResponse response , Integer page) {
        try {
            LOG.debug("代收运费运单列表入参page={}",page);
            if(page == null || page.intValue() < 1){
                return findErrorParam(response);
            }
            PageResult<CollectFreightDTO> collectFreightDTOPageResult = queryOrderService.collectFreightList(page, findUserId(request));
            if(collectFreightDTOPageResult.getCode()== CodeTable.ERROR.getCode())
            {
                updRespHeadError(response);
                if(LOG.isDebugEnabled())LOG.debug("系统内部参数不合法！");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10002);
            }
            if(collectFreightDTOPageResult.isSuccess()){
                updRespHeadSuccess(response);
                return collectFreightBuildDtoToBo(collectFreightDTOPageResult);
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("待收运费列表列表出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    public CollectFreightListBO collectFreightBuildDtoToBo(PageResult<CollectFreightDTO> collectFreightDTOs){
        LOG.debug("代收运费运单列表查询结果collectFreightDTOs={}", JSON.toJSONString(collectFreightDTOs));
        CollectFreightListBO collectFreightListBO = new CollectFreightListBO();
        collectFreightListBO.setCollectFreightNums(collectFreightDTOs.getTotalRecord());
        collectFreightListBO.setCollectFreightPage(collectFreightDTOs.getTotalPage());
        List<CollectFreightBO> list = new ArrayList<CollectFreightBO>();
        for (CollectFreightDTO collectFreightDTO : collectFreightDTOs.getDataList()){
            CollectFreightBO collectFreightBO = new CollectFreightBO();
            collectFreightBO.setOrderId(collectFreightDTO.getId().toString());
            collectFreightBO.setNeedReceiveFair(SystemsUtil.showAppMoney(collectFreightDTO.getNeedReceiveFair()));
            collectFreightBO.setCargoName(collectFreightDTO.getCargoName());
            collectFreightBO.setCompanyName(collectFreightDTO.getCompanyName());
            collectFreightBO.setFreightPrice(SystemsUtil.getTotalFare(collectFreightDTO.getRealNeedpayFair()));
            collectFreightBO.setLoadTime(DateUtil.dateFormat(collectFreightDTO.getInstallCargoTime(), DateUtil.F_DATETOMIN));
            //待收油卡费用
            collectFreightBO.setNeedReceiveOilCard(SystemsUtil.getFare(collectFreightDTO.getNeedReceiveOilFare()));
            list.add(collectFreightBO);
        }
        collectFreightListBO.setCollectFreightLists(list);
        return collectFreightListBO;
    }


}
