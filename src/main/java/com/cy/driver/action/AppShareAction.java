package com.cy.driver.action;

import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.throwex.ValidException;
import com.cy.driver.common.util.EnvConfig;
import com.cy.driver.common.util.HttpUtils;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.AppShareBo;
import com.cy.driver.service.AppShareService;
import com.cy.driver.service.CargoHandlerService;
import com.cy.driver.service.PointFService;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.apache.commons.codec.binary.Base64;
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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * app分享
 * Created by wyh on 2015/7/4.
 */
@Scope("prototype")
@RestController("appShareAction")
public class AppShareAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(AppShareAction.class);
    @Resource
    private EnvConfig envConfig;
    @Resource
    private AppShareService appShareService;
    @Resource
    private CargoHandlerService cargoHandleService;
    @Resource
    private HttpUtils httpUtils;
    @Resource
    private PointFService pointFService;

//    public Object cargoShare(HttpServletRequest request, HttpServletResponse response, String orderId ){}

    /**
     * 获得app分享的内容
     * @author wyh
     */
    @RequestMapping(value = "/buildAppShareContent", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.BUILD_APP_SHARE_CONTENT)
    public Object buildAppShareContent(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = findToken(request);
            String source = findSource(request);
            AppShareBo result = appShareService.buildAppShareContent(token, source);
            String shortUrl = httpUtils.longUrlToShortUrl(result.getAppShareUrl());
            result.setAppShareUrl(shortUrl);
            if (result != null) {
                if(logger.isDebugEnabled()){
                    logger.debug("获得app分享的内容url={},content={}", result.getAppShareUrl(), result.getAppShareContent());
                }
                updRespHeadSuccess(response);
                return result;
            }
            return findException(response);
        } catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("获得app分享的内容出错", e);
            }
            e.printStackTrace();
        }
        return findException(response);
    }

    /**
     * 保存app分享信息
     * @author wyh
     */
    @RequestMapping(value = "/saveAppShareInfo")
    @ResponseBody
    public Object saveAppShareInfo(HttpServletRequest request, HttpServletResponse response,
                                   String recommendMobileNum, String recommendedMobileNum, Integer downType){
        Map<String, String> param = new HashMap<String, String>();
        String code = "-8";
        String errorMessage = "保存app分享信息成功";
        try {
            if (downType == null) {
                errorMessage = "保存app分享信息出错";
            } else {
                if (!ValidateUtil.validateTelePhone(recommendedMobileNum)) {
                    errorMessage = "请输入您正确的手机号码..";
                }else{
                    if (!ValidateUtil.validateTelePhone(recommendMobileNum)) {
                        errorMessage = "操作失败";
                    }else{
                        if (downType.intValue() == 1) {
                            Response<Boolean> result = appShareService.share(recommendMobileNum, recommendedMobileNum);
                            if (result.isSuccess()) {
                                if(result.getData()){
                                    code = "0";
                                }else{
                                    code = "-2";
                                    errorMessage = "您已注册,可直接登录";
                                }
                            } else {
                                if (result.getCode() == CodeTable.EXCEPTION.getCode()) {
                                    errorMessage = "系统出错";
                                } else {
                                    errorMessage = "参数出错";
                                }
                            }
                        } else {
                            appShareService.saveOwnerShare(recommendedMobileNum, recommendMobileNum);
                            code = "0";
                        }
                    }
                }
            }

        } catch (ValidException e) {
            if (e.isLogicError()) {
                code = "-2";
                errorMessage = "您已注册,可直接登录";
            } else {
                errorMessage = e.getMessage();
            }
        } catch (Exception e){
            errorMessage = "系统出错";
            if(logger.isErrorEnabled()){
                logger.error("获得app分享的内容出错", e);
            }
            e.printStackTrace();
        }
        param.put("result", code);
        param.put("message", errorMessage);
        return param;
    }

    /**
     * base64解码
     * @author wyh
     */
    @RequestMapping(value = "/decodeBase64")
    @ResponseBody
    public Object decodeBase64(HttpServletRequest request, HttpServletResponse response,
                               String telephone){
        Map<String, String> param = new HashMap<String, String>();
        String code = "-8";
        String message = "";
        if (StringUtils.isEmpty(telephone)) {
            message = "信息丟失";
        }else{
            if (telephone.contains("&")) {
                telephone = telephone.split("&")[0];
            }
            try {
                message = new String(Base64.decodeBase64(telephone.getBytes("UTF-8")));
                code = "0";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        param.put("result", code);
        param.put("message", message);
        return param;
    }

    @RequestMapping(value = "/buildCargoShareContent", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_GET_CARGO_SHARE_CONTENT)
    @Log(type = LogEnum.BUILD_CARGO_SHARE_CONTENT)
    public Object buildCargoShareContent(HttpServletResponse response, Long cargoId){
        if(cargoId == null){
            if (logger.isErrorEnabled()) logger.error("获得分享内容校验信息校验:货源编号为空.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        try{
            com.cy.cargo.service.dto.base.Response<CargoInfoDTO> serResponse = cargoHandleService.getCargoInfo(cargoId);
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (logger.isErrorEnabled()) logger.error("获得分享内容校验信息(服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            if(serResponse.getData() == null){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20066);
            }
            AppShareBo share = new AppShareBo();
            share.setAppShareContent(Constants.CARGO_SHARE_PREFIX_CONTENT);
            String longUrl=envConfig.getCargoShareSubUrl() + "?"
                    + Base64.encodeBase64String(serResponse.getData().getId().toString().getBytes("UTF-8"));
            String shortUrl = httpUtils.longUrlToShortUrl(longUrl);
            share.setAppShareUrl(shortUrl);
            if(logger.isDebugEnabled()){
                logger.debug("查询货源分享内容url={},content={}", share.getAppShareUrl(), share.getAppShareContent());
            }
            updRespHeadSuccess(response);
            return share;
        }catch (Exception e) {
            logger.error("查询货源分享内容出错.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }
}
