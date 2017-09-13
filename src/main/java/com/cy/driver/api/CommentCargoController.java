package com.cy.driver.api;

import com.cy.cargo.service.dto.CargoAssessInfoDTO;
import com.cy.cargo.service.dto.CargoAssessInfoListDTO;
import com.cy.cargo.service.dto.DriverCargoAssessInfoDTO;
import com.cy.driver.action.BaseAction;
import com.cy.driver.api.domain.res.CommentList;
import com.cy.driver.cloudService.CloudCommentService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.PageBase;
import com.cy.driver.service.AssessService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.rdcservice.service.dto.CarrierAssessDTO;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 货源评论
 *
 * @author yanst 2016/5/26 15:43
 */
@Scope("prototype")
@Controller
public class CommentCargoController extends BaseAction {

    @Resource
    private CloudCommentService cloudCommentService;
    @Resource
    private AssessService assessService;
    @Resource
    private DriverUserHandlerService driverUserHandlerService;

    private Logger LOG = LoggerFactory.getLogger(CommentCargoController.class);

    /**
     * 评论列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/cloudCommentList", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.SAAS_ORDER_PAGE)
    @ResponseBody
    @Log(type = LogEnum.COMMENT_LIST)
    public Object commentList(HttpServletResponse response, Integer page, Integer cargoSource, Long cargoId) {
        try {
            if(cargoSource==null )
                if (page == null || page.intValue() <= 0L)
                    page = 1;
            if (cargoSource == 2) {
                PageInfo<Long> pageInfo = new PageInfo<>();
                pageInfo.setPageIndex(page);
                pageInfo.setData(cargoId);
                PageResult<CarrierAssessDTO> pageResult= cloudCommentService.queryCommentByPage(pageInfo);
                if(pageResult==null || !pageResult.isSuccess()){
                    LOG.debug("调用rdcservice根据货源ID查询评价列表失败");
                    return null;
                }
                if (pageResult.getDataList()==null) {
                    return null;
                }
                List<CommentList> commentLists = new ArrayList<>();
                for (CarrierAssessDTO carrierAssessDTO : pageResult.getDataList()) {
                    CommentList commentList = new CommentList();
                    commentList.setCargoState(String.valueOf(carrierAssessDTO.getType()));
                    commentList.setCommentAuthState(String.valueOf(carrierAssessDTO.getUserAuthState()));
                    commentList.setCommentContent(carrierAssessDTO.getAssessInfo());
                    commentList.setCommentPerHeadPhone(carrierAssessDTO.getUserHeadImg());
                    commentList.setMobilePhone(carrierAssessDTO.getUserPhone());
                    commentList.setCommentTime(DateUtil.dateFormat(carrierAssessDTO.getCreateTime(), DateUtil.F_DATE));
                    commentLists.add(commentList);
                }
                PageBase<CommentList> pageBase = new PageBase<>();
                pageBase.setListData(commentLists);
                pageBase.setTotalNum(pageResult.getTotalRecord());
                pageBase.setTotalPage(pageResult.getTotalPage());
                updRespHeadSuccess(response);
                return pageBase;
            }else {
                com.cy.cargo.service.dto.base.PageInfo pageInfo = new com.cy.cargo.service.dto.base.PageInfo(page);
                CargoAssessInfoListDTO cargoAssessInfoListDTO = assessService.queryCommentInfo(pageInfo, cargoId);
                if(cargoAssessInfoListDTO==null){
                    LOG.debug("根据货源ID查询评价列表失败");
                    return null;
                }
                List<CargoAssessInfoDTO> cargoAssessInfoDTOs = cargoAssessInfoListDTO.getDriverCargoAssessInfoList();
                List<CommentList> commentLists = new ArrayList<>();
                for (CargoAssessInfoDTO cargoAssessInfoDTO : cargoAssessInfoDTOs) {
                    CommentList commentList = new CommentList();
                    commentList.setCargoState(String.valueOf(cargoAssessInfoDTO.getType()));
                    commentList.setCommentContent(cargoAssessInfoDTO.getAssessInfo());
                    commentList.setCommentTime(cargoAssessInfoDTO.getCreateTime());
                    commentList.setCommentPerHeadPhone(driverUserHandlerService.getImgInfo(cargoAssessInfoDTO.getDriverId(), (byte) Constants.HEAD_PORTRAIT_IMG).getData());
                    Response<DriverUserInfoDTO> findDriverInfo = driverUserHandlerService.getDriverUserInfo(cargoAssessInfoDTO.getDriverId());
                    commentList.setCommentAuthState(String.valueOf(findDriverInfo.getData().getSubmitType()));
                    commentList.setMobilePhone(findDriverInfo.getData().getCode());
                    commentLists.add(commentList);
                }
                PageBase<CommentList> pageBase = new PageBase<>();
                pageBase.setTotalPage((int)cargoAssessInfoListDTO.getCargoAssessTotalPages());
                pageBase.setTotalNum((int)cargoAssessInfoListDTO.getCargoAssessCount());
                pageBase.setListData(commentLists);
                updRespHeadSuccess(response);
                return pageBase;
            }
        } catch (Exception e) {
            LOG.error("获取评论列表出错", e);
        }
        return findException(response);
    }

    /**
     * 添加评论
     *
     * @param cargoState     货源状态
     * @param commentContent 评论内容
     * @return
     */
    @RequestMapping(value = "/cloudAddComment", method = RequestMethod.POST)
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_COMMENT)
    @ResponseBody
    @Log(type = LogEnum.ADD_COMMENT)
    public Object addComment(HttpServletResponse response, Byte cargoState, String commentContent, Integer cargoSource, Long cargoId) {
        try {
            if (LOG.isDebugEnabled())
                LOG.debug("添加评论信息，入参信息，cargoState:{},commentContent:{}", cargoState, commentContent);
            if (StringUtils.isEmpty(cargoState)) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("参数错误，货源状态不能为空，", cargoState);
                }
                return findErrorParam(response);
            }
            Long driverId = findUserId();
            Byte authState = findAuthState();
            Map<String,Object> map = new HashMap<>();
            boolean isSuccess = false;
            if (cargoSource == 2) {
                CarrierAssessDTO carrierAssessDTO = new CarrierAssessDTO();
                carrierAssessDTO.setAssessInfo(commentContent);
                carrierAssessDTO.setCarrierId(cargoId);
                carrierAssessDTO.setType(cargoState);
                carrierAssessDTO.setUserAuthState(authState);
                carrierAssessDTO.setUserId(String.valueOf(driverId));
                carrierAssessDTO.setUserType((byte) Constants.USER_TYPE_KDWSJ);
                Response<String> findImg = driverUserHandlerService.getImgInfo(driverId, (byte) Constants.HEAD_PORTRAIT_IMG);
                if (!findImg.isSuccess()) {
                    LOG.error("获取司机头像服务调用错误");
                    carrierAssessDTO.setUserHeadImg("");
                }else{
                    carrierAssessDTO.setUserHeadImg(findImg.getData());
                }
                Response<DriverUserInfoDTO> findDriverInfo = driverUserHandlerService.getDriverUserInfo(driverId);
                if (!findDriverInfo.isSuccess()) {
                    LOG.error("获取司机信息服务调用错误");
                    carrierAssessDTO.setUserName("");
                    carrierAssessDTO.setUserPhone("");
                }else {
                    carrierAssessDTO.setUserName(findDriverInfo.getData().getName());
                    carrierAssessDTO.setUserPhone(findDriverInfo.getData().getCode());
                }
                isSuccess = cloudCommentService.addComment(carrierAssessDTO);
            } else {
                DriverCargoAssessInfoDTO driverCargoAssessInfoDTO = new DriverCargoAssessInfoDTO();
                driverCargoAssessInfoDTO.setAssessInfo(commentContent);
                driverCargoAssessInfoDTO.setCargoId(cargoId);
                driverCargoAssessInfoDTO.setDriverId(driverId);
                driverCargoAssessInfoDTO.setType(Byte.valueOf(cargoState));
                isSuccess = assessService.addComment(driverCargoAssessInfoDTO);
            }
            if(isSuccess){
                updRespHeadSuccess(response);
                return map;
            }else{
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20002);
            }
        } catch (Exception e) {
            LOG.error("保存点评信息出现异常{}", e);
        }
        return findException(response);
    }

}
