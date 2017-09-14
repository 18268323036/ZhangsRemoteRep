package com.cy.driver.action;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.award.service.dto.AwardAccountDTO;
import com.cy.award.service.dto.AwardTransLogDTO;
import com.cy.award.service.dto.TransLogParamDTO;
import com.cy.driver.action.convert.IntegrationConvert;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.BootPageInfoBO;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.PageComm;
import com.cy.driver.service.BootPageService;
import com.cy.driver.service.IntegrationService;
import com.cy.driver.service.PointFService;
import com.cy.pass.service.dto.ImgDeployInfoDTO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分
 * Created by wyh on 2016/4/25.
 */
@Scope("prototype")
@RestController("integrationAction")
public class IntegrationAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(IntegrationAction.class);

    @Resource
    private PointFService pointFService;
    @Resource
    private IntegrationService integrationService;
    @Resource
    private BootPageService bootPageService;


    /**
     * 积分引导页图片列表
     * @param response
     * @param imgStandard 规格（0: 480x320  1:480x800   2:720x1280 ）
     * @return
     */
    @RequestMapping(value = "/listInterrationBootPage", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.LIST_INTERRATION_BOOTPAGE)
    @Log(type = LogEnum.LIST_INTERRATION_BOOTPAGE)
    public Object listInterrationBootPage(HttpServletResponse response, String imgStandard ) {
        Map<String ,Object > resultMap = new HashMap<String, Object>();
        try {
            List<ImgDeployInfoDTO> resultData = bootPageService.findImgStandard((short)103, imgStandard);
            if(resultData == null){
                updRespHeadError(response);
                return null;
            }
            updRespHeadSuccess(response);
            return convertIntegrationImgInfo(resultData);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("getBootPageList 出错。", e);
            }
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    private List<BootPageInfoBO> convertIntegrationImgInfo(List<ImgDeployInfoDTO> resultData){
        List<BootPageInfoBO> list = new ArrayList<BootPageInfoBO>();
        for (ImgDeployInfoDTO imgDeployInfoDTO : resultData)
        {
            BootPageInfoBO bootPageInfoBO = new BootPageInfoBO();
            bootPageInfoBO.setImgFilemd5(imgDeployInfoDTO.getImgFilemd5());
            bootPageInfoBO.setOrderNum(imgDeployInfoDTO.getOrderNum());
            bootPageInfoBO.setTitle(imgDeployInfoDTO.getImgName());
            bootPageInfoBO.setUrl(imgDeployInfoDTO.getClickResponse());
            list.add(bootPageInfoBO);
        }
        return list;
    }

    /**
     * 积分明细分页列表
     * @param response
     * @param page 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/pageIntegration", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_INTEGRATION)
    @Log(type = LogEnum.PAGE_INTEGRATION)
    public Object pageIntegration(HttpServletResponse response, Integer page) {
        try {
            Long driverId = findUserId();
            if (LOG.isDebugEnabled())
                LOG.error("{}的请求参数：page={}, driverId={}", LogEnum.PAGE_INTEGRATION.getRemark(), page, driverId);
            if (page == null) {
                LOG.error("{}失败，参数page必填", LogEnum.PAGE_INTEGRATION.getRemark());
                return findErrorParam(response);
            }
            AwardAccountDTO awardAccountDTO = integrationService.countByUserId(Constants.AWARD_DRIVER, driverId);
            if (awardAccountDTO == null) {
                return findJSonResponse(response, ApiResultCodeEnum.SER_20302);
            } else {
                PageComm pageComm = new PageComm(page);
                TransLogParamDTO paramDTO = new TransLogParamDTO();
                paramDTO.setAwardAccountCode(awardAccountDTO.getAccountCode());
                PageBase<AwardTransLogDTO> pageBase = pointFService.pageAwardLog(pageComm, paramDTO);
                updRespHeadSuccess(response);
                return IntegrationConvert.pageBO(pageBase);
            }
        } catch (Exception e) {
            LOG.error("{}出现异常", LogEnum.PAGE_INTEGRATION.getRemark(), e);
            return findException(response);
        }
    }

    /**
     * 添加积分
     * @param response
     * @param type 页码从1开始
     * @return
     * @author wyh
     */
    @RequestMapping(value = "/addIntegration", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_INTEGRATION)
    @Log(type = LogEnum.ADD_INTEGRATION)
    public Object addIntegration(HttpServletResponse response, Integer type) {
        try {
            if (type == null) {
                LOG.error("{}失败，参数type为空", LogEnum.ADD_INTEGRATION.getRemark());
                return findErrorParam(response);
            }
            Long driverId = findUserId();
            String eventCode = IntegrationConvert.eventCode(type);
            if (LOG.isDebugEnabled())
                LOG.debug("{}的请求参数：type={},driverId={},eventCode={}", LogEnum.ADD_INTEGRATION.getRemark(), type, driverId, eventCode);
            if (StringUtils.isBlank(eventCode)) {
                LOG.error("{}失败，转换的事件编号为空", LogEnum.ADD_INTEGRATION.getRemark());
                return findErrorParam(response);
            }
            pointFService.pointReward(Constants.AWARD_DRIVER, driverId, Constants.CHECK_MODE_BY_EVENT, eventCode, null, null, convert2InSource(),null);
            updRespHeadSuccess(response);
            return null;
        } catch (Exception e) {
            LOG.error("{}出现异常", LogEnum.ADD_INTEGRATION.getRemark(), e);
            return findException(response);
        }
    }
}
