package com.cy.driver.action;

import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.CargoMoreAssessListBO;
import com.cy.driver.domain.OnwerAssessListBO;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.PageComm;
import com.cy.driver.service.AssessService;
import com.cy.driver.service.CargoMoreAssessService;
import com.cy.driver.service.SearchDriverHandlerService;
import com.cy.driver.service.WebUserHandleService;
import com.cy.order.service.dto.Enum.AssessUserType;
import com.cy.order.service.dto.assess.OrderAssessDTO;
import com.cy.pass.service.dto.Enum.DriverImgType;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.search.service.dto.response.DriverImgDTO;
import com.cy.search.service.dto.response.DriverUserInfoDTO;
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
 * Created by Administrator on 2015/7/12.
 */
@Scope("prototype")
@RestController("cargoMoreAssessAction")
public class CargoMoreAssessAction extends BaseAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CargoMoreAssessService cargoMoreAssessService;

    @Resource
    private AssessService assessService;

    @Resource
    private WebUserHandleService webUserHandleService;

    @Resource
    private SearchDriverHandlerService searchDriverHandlerService;


    /**
     * 查看更多评价
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/lookMoreComment", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.LOOK_MORE_COMMENT)
    @Log(type = LogEnum.LOOK_MORE_COMMENT)
    public Object lookMoreComment(HttpServletRequest request, HttpServletResponse response, String cargoId, String page) {
        try {
            if (StringUtils.isEmpty(cargoId) || StringUtils.isEmpty(page) ) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
           Response<CargoMoreAssessListBO> result = cargoMoreAssessService.getCargoMoreAssess(findUserId(request), Integer.valueOf(page), Long.valueOf(cargoId));
            if (result.isSuccess()){
                Map<String ,Object> resultMap = new HashMap<String ,Object>();
                resultMap.put("MoreConmment",result.getData());
                updRespHeadSuccess(response);
                return resultMap;
            }
            if(result.getCode()== CodeTable.INVALID_CODE.getCode()||result.getCode()== CodeTable.INVALID_ARGS.getCode()){
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("查看更多评价- " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }


    /**
     * 查询货主评论列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getOnwerAccessList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_ONWER_ACCESS_LIST)
    @Log(type = LogEnum.GET_ONWER_ACCESS_LIST)
    public Object getOnwerAccessList(HttpServletRequest request, HttpServletResponse response, Integer page, Long onwerUserId) {
        try {
            if(logger.isDebugEnabled())logger.debug("查询货主评论列表接口入参：page={},onwerUserId={}", page, onwerUserId);
            if (page == null || page.intValue() <= 0 || onwerUserId == null || onwerUserId.longValue() <= 0) {
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            PageBase<OrderAssessDTO> result = assessService.pageListByOnwer(new PageComm(page), onwerUserId, null);
            if (result != null){

                PageBase<OnwerAssessListBO> pageBase = new PageBase<>();
                pageBase.setTotalNum(result.getTotalNum());
                pageBase.setTotalPage(result.getTotalPage());
                List<OnwerAssessListBO> lists = new ArrayList<>();
                List<Long> driverIds = new ArrayList<Long>();
                List<Long> subIds = new ArrayList<Long>();
                if(result.getListData() != null){
                    for (OrderAssessDTO orderAssessDTO : result.getListData()) {
                        OnwerAssessListBO assessInfo = new OnwerAssessListBO();
                        if(AssessUserType.SUB_CONTRACTOR.getValue() == orderAssessDTO.getOrgUserType() && orderAssessDTO.getOrgUserId() != null){//司机ID

                            driverIds.add(orderAssessDTO.getOrgUserId());
                        }else if(AssessUserType.DRIVER.getValue() == orderAssessDTO.getOrgUserType() && orderAssessDTO.getOrgUserId() != null){//分包商ID
                            subIds.add(orderAssessDTO.getOrgUserId());
                        }
                        assessInfo.setAssessUserId(orderAssessDTO.getOrgUserId());
                        assessInfo.setAssessId(orderAssessDTO.getId());
                        assessInfo.setAuthState(Constants.AUTH_YES+"");//认证状态
                        assessInfo.setAccessTime(DateUtil.dateFormat(orderAssessDTO.getCreateTime(), DateUtil.F_DATE));//评论时间
                        assessInfo.setAccessType(orderAssessDTO.getAssessType());//评价类型
                        assessInfo.setAccessContent(orderAssessDTO.getContent());//内容
                        lists.add(assessInfo);
                    }

                    List<WebUserInfoDTO> listWebInfo = webUserHandleService.ListForWebUserInfo(subIds);

                    List<DriverImgDTO> listImgInfo = searchDriverHandlerService.findDriverImgs(driverIds,(int) DriverImgType.PERSON_PHOTO.getValue());
                    List<DriverUserInfoDTO> listDriverInfo = searchDriverHandlerService.findDriverInfo(driverIds);

                    List<OnwerAssessListBO> accessList = new ArrayList<>();
                    for (OnwerAssessListBO onwerAssessListBO : lists) {
                        //保存分包商头像和手机号码
                        if(listWebInfo != null && listWebInfo.size() > 0){
                            for (WebUserInfoDTO webUserInfoDTO : listWebInfo){
                                if(onwerAssessListBO.getAssessUserId() == webUserInfoDTO.getId()){
                                    onwerAssessListBO.setOnwerHeadImg(webUserInfoDTO.getHeadPortraitImgPath());
                                    onwerAssessListBO.setMobilePhone(webUserInfoDTO.getMobilephone());
                                }
                            }
                        }

                        //保存司机头像
                        if(listImgInfo != null && listImgInfo.size() > 0){
                            for (DriverImgDTO driverImgDTO : listImgInfo){
                                onwerAssessListBO.setOnwerHeadImg(driverImgDTO.getImgPath());
                            }
                        }

                        //保存司机手机号码
                        if(listDriverInfo != null && listDriverInfo.size() > 0) {
                            for (DriverUserInfoDTO driverUserInfoDTO : listDriverInfo) {
                                onwerAssessListBO.setMobilePhone(driverUserInfoDTO.getCode());
                            }
                        }
                        accessList.add(onwerAssessListBO);
                    }
                    pageBase.setListData(accessList);
                }
                updRespHeadSuccess(response);
                return pageBase;
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("查询货主评论列表- " + e.getMessage());
            }
            e.printStackTrace();
        }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

}
