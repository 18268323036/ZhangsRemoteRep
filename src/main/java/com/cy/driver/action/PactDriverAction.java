package com.cy.driver.action;

import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.PactDriverInfoBO;
import com.cy.driver.domain.PactLineBO;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.push.PushSjhtc;
import com.cy.driver.domain.push.PushSjhtxl;
import com.cy.driver.service.PactDriverService;
import com.cy.driver.service.PointFService;
import com.cy.driver.service.PushSendService;
import com.cy.driver.service.WebUserService;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.base.Response;
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
 * 合同司机
 * Created by wyh on 2015/7/6.
 */
@Scope("prototype")
@RestController("pactDriverAction")
public class PactDriverAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(PactDriverAction.class);
    @Resource
    private PactDriverService pactDriverService;
    @Resource
    private PushSendService pushSendService;
    @Resource
    private PointFService pointFService;
    @Resource
    private WebUserService webUserService;
    /**
     * 查询合同客户列表
     * @return
     */
    @RequestMapping(value = "/paqePactInfo", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAQE_PACT_INFO)
    @Log(type = LogEnum.QUERY_PACT_INFO_LIST)
    public Object paqePactInfo(HttpServletResponse response, Byte pactStart, Long page){
        try {
            if(page == null && page.longValue() < 0 && pactStart == null){
                logger.error("参数错误");
                return findErrorParam(response);
            }
            long driverId = findUserId(request);
            PageBase<PactDriverInfo3DTO> pageBase = pactDriverService.queryPactInfoList(driverId, page, pactStart);
            updRespHeadSuccess(response);
            if(pageBase != null){
                return convertPactInfo(pageBase);
            }else {
                return null;
            }

        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("查询合同客户列表出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    public PageBase<PactDriverInfoBO> convertPactInfo(PageBase<PactDriverInfo3DTO> pageBase){
        PageBase<PactDriverInfoBO> pageBase1 = new PageBase<>();
        pageBase1.setTotalNum(pageBase.getTotalNum());
        pageBase1.setTotalPage(pageBase.getTotalPage());
        List<Long> companyIds = new ArrayList<Long>();
        List<Long> pactDriverIds = new ArrayList<Long>();
        List<PactDriverInfoBO> resultList = new ArrayList<>();
        List<PactDriverInfo3DTO> list = pageBase.getListData();
        if(list != null){
            for (PactDriverInfo3DTO pactInfo : list) {
                PactDriverInfoBO pactDriverInfoBO = new PactDriverInfoBO();
                if(pactInfo.getCompanyId() != null){
                    companyIds.add(pactInfo.getCompanyId());
                    pactDriverInfoBO.setCompanyId(pactInfo.getCompanyId());
                }
                pactDriverIds.add(pactInfo.getId());
                pactDriverInfoBO.setPactDriverId(pactInfo.getId());
                pactDriverInfoBO.setPactStartTime(DateUtil.dateFormat(pactInfo.getPactStartTime()));
                pactDriverInfoBO.setPactStart(pactInfo.getPactStart());
                resultList.add(pactDriverInfoBO);
            }
            List<WebUserInfoDTO> webUserInfoList = webUserService.listBycompanyIds(companyIds);
            List<VipDriverLineInfoDTO>  vipDriverLineInfoList = pactDriverService.listByPactLineIds(pactDriverIds);
            //组装企业名称 认证信息 合同线路
            for (PactDriverInfoBO pactDriverInfoBO : resultList){
                for(WebUserInfoDTO webUserInfoDTO : webUserInfoList){
                    if(pactDriverInfoBO.getCompanyId() != null && webUserInfoDTO.getCompanyId().longValue() == pactDriverInfoBO.getCompanyId().longValue()){
                        pactDriverInfoBO.setAuthState(webUserInfoDTO.getSubmitType());
                        pactDriverInfoBO.setCompanyName(webUserInfoDTO.getName());
                        continue;
                    }

                }
                List<PactLineBO> listData = new ArrayList<>();
                for(VipDriverLineInfoDTO vipDriverLineInfoDTO : vipDriverLineInfoList){
                    if(vipDriverLineInfoDTO.getPactDriverId() != null && vipDriverLineInfoDTO.getPactDriverId().longValue() == pactDriverInfoBO.getPactDriverId().longValue()){
                        PactLineBO pactLineBO = new PactLineBO();
                        String sAddr = SystemsUtil.buildAddress(vipDriverLineInfoDTO.getStartProvince(), vipDriverLineInfoDTO.getStartCity(),
                                vipDriverLineInfoDTO.getStartCounty());
                        String eAddr = SystemsUtil.buildAddress(vipDriverLineInfoDTO.getEndProvince(), vipDriverLineInfoDTO.getEndCity(),
                                vipDriverLineInfoDTO.getEndCounty());
                        pactLineBO.setStartAddress(sAddr);
                        pactLineBO.setEndAddress(eAddr);
                        pactLineBO.setPactLineId(vipDriverLineInfoDTO.getId());
                        listData.add(pactLineBO);
                    }
                }
                pactDriverInfoBO.setListData(listData);
            }
            pageBase1.setListData(resultList);
        }
        return pageBase1;
    }



    /**
     * 查询合同客户列表
     * @return
     */
    @RequestMapping(value = "/queryPactInfoList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_PACT_INFO_LIST)
    @Log(type = LogEnum.QUERY_PACT_INFO_LIST)
    public Object queryPactInfoList(HttpServletRequest request, HttpServletResponse response){
        try {
            long driverId = findUserId(request);
            int page = 1;
            Response<PactDriverListDTO> result = pactDriverService.queryPactInfoList(driverId, page);
            return findResponseData(result, response);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("查询合同客户列表出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    /**
     * 合同客户确认
     * @param pactDriverId 合同司机id
     * @param pactStart 状态（-1拒绝  1同意）
     * @return
     */
    @RequestMapping(value = "/updatePactState", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_PACT_STATE)
    @Log(type = LogEnum.UPDATE_PACT_STATE)
    public Object updatePactState(HttpServletRequest request, HttpServletResponse response
            , Long pactDriverId, Integer pactStart){
        try {
            if(pactDriverId == null || pactStart == null){
                return findErrorParam(response);
            }
            long driverId = findUserId(request);
            Response<Boolean> result = pactDriverService.updatePactState(driverId, pactDriverId, pactStart);
            if (result.isSuccess()) {
                /** 合同客户确认的推送 */
                updatePactStatePush(pactStart, pactDriverId);
                //首次成为别人的合同客户积分奖励-3.4版本
                if(pactStart.intValue() == 1){
                    pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                            LogEnum.UPDATE_PACT_STATE.getEventCode(), null, null, convert2InSource(),null);
                }
            }
            return findResponseBoolean(result, response);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("合同客户确认出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    /**
     * 合同客户确认的推送
     * @param pactStart
     * @param pactDriverId
     * @return
     * @author wyh
     */
    private boolean updatePactStatePush(Integer pactStart, Long pactDriverId){
        try {
            PactDriverInfo2DTO pactDriverDTO = pactDriverService.getPactDriverInfo(pactDriverId);
            if (pactDriverDTO == null) {
                logger.error("合同客户确认的推送失败，合同车辆详情pactDriverDTO为空");
                return false;
            }
            PushSjhtc info = new PushSjhtc();
            info.setUserId(pactDriverDTO.getUserId());
            info.setBusinessId(pactDriverId);
            info.setDriverName(pactDriverDTO.getDriverName());
            info.setCarNumber(pactDriverDTO.getCarNumber());
            if (pactStart.intValue() == 1) {
                /** 同意成为合同车辆 */
                return pushSendService.sjtycwhtcPushOwn(info);
            } else {
                /** 拒绝成为合同车辆 */
                return pushSendService.sjjjcwhtcPushOwn(info);
            }
        } catch (Exception e) {
            logger.error("合同客户确认的推送出现异常", e);
        }
        return false;
    }

    /**
     * 查询合同客户线路详情
     * @param pactDriverId 合同司机id
     * @return
     */
    @RequestMapping(value = "/queryPactDriverDetails", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.QUERY_PACT_DRIVER_DETAILS)
    @Log(type = LogEnum.QUERY_PACT_DRIVER_DETAILS)
    public Object queryPactDriverDetails(HttpServletRequest request, HttpServletResponse response
            , Long pactDriverId){
        try {
            if(pactDriverId == null){
                return findErrorParam(response);
            }
            long driverId = findUserId(request);
            Response<PactDriverDetailsDTO> result = pactDriverService.queryPactDriverDetails(driverId, pactDriverId);
            return findResponseData(result, response);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("查询合同客户线路详情出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    /**
     * 合同客户线路确认
     * @param vipDriverLineId 合同线路id
     * @param start 状态（1拒绝 0同意）
     * @return
     */
    @RequestMapping(value = "/updateVipDriverLineState", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_VIP_DRIVER_LINE_STATE)
    @Log(type = LogEnum.UPDATE_VIP_DRIVER_LINE_STATE)
    public Object updateVipDriverLineState(HttpServletRequest request, HttpServletResponse response
            , Long vipDriverLineId, Integer start){
        try {
            if(vipDriverLineId == null || start == null){
                return findErrorParam(response);
            }
            long driverId = findUserId(request);
            Response<Boolean> result = pactDriverService.updateVipDriverLineState(driverId, vipDriverLineId, start);
            if (result.isSuccess() && result.getData() != null && result.getData().booleanValue()) {
                /** 合同客户线路确认成功的推送 */
                updateVipDriverLineStatePush(vipDriverLineId, start);
            }
            return findResponseBoolean(result, response);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("合同客户线路确认出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    /**
     * 合同客户线路确认成功的推送
     * @param vipDriverLineId
     * @param start
     * @return
     * @author wyh
     */
    private boolean updateVipDriverLineStatePush(Long vipDriverLineId, Integer start){
        try {
            VipDriverLineInfoDTO lineInfoDTO = pactDriverService.getLineInfo(vipDriverLineId);
            if (lineInfoDTO == null) {
                logger.error("合同客户线路确认成功的推送失败，合同车辆路线详情lineInfoDTO为空");
                return false;
            }
            PactDriverInfo2DTO pactDriverDTO = pactDriverService.getPactDriverInfo(lineInfoDTO.getPactDriverId());
            if (pactDriverDTO == null) {
                logger.error("合同客户线路确认成功的推送失败，合同车辆详情pactDriverDTO为空");
                return false;
            }
            PushSjhtxl info = new PushSjhtxl();
            info.setUserId(pactDriverDTO.getUserId());
            info.setBusinessId(lineInfoDTO.getPactDriverId());
            info.setDriverName(pactDriverDTO.getDriverName());
            info.setCarNumber(pactDriverDTO.getCarNumber());
            info.setStartCity(lineInfoDTO.getStartCity());
            info.setEndCity(lineInfoDTO.getEndCity());
            if (start.intValue() == 0) {
                /** 司机同意合同线路 */
                return pushSendService.sjtyhtxlPushOwn(info);
            } else {
                /** 司机拒绝合同线路 */
                return pushSendService.sjjjhtxlPushOwn(info);
            }
        } catch (Exception e) {
            logger.error("合同客户线路确认成功的推送出现异常", e);
        }
        return false;
    }
}
