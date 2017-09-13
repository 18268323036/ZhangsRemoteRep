package com.cy.driver.service.impl;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.throwex.ValidException;
import com.cy.driver.domain.AppShareBo;
import com.cy.driver.service.AppShareService;
import com.cy.driver.service.WebUserHandleService;
import com.cy.pass.service.AppShareInfoService;
import com.cy.pass.service.DriverAppShareService;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.dto.AppShareInfoDTO;
import com.cy.pass.service.dto.ShareUserDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wyh on 2015/7/4.
 */
@Service("appShareServiceImpl")
public class AppShareServiceImpl implements AppShareService {
    private static Logger logger = LoggerFactory.getLogger(AppShareServiceImpl.class);
    private String appShareUrl;//分享地址
    @Resource
    private DriverAppShareService driverAppShareService;
    @Resource
    private DriverUserInfoService driverUserInfoService;
    @Resource
    private WebUserHandleService webUserHandleService;
    @Resource
    private AppShareInfoService appShareInfoService;

    @Override
    public AppShareBo buildAppShareContent(String token, String source) {
        try {
            AppShareBo appShareBo = new AppShareBo();
            String mobilePhone = driverUserInfoService.findDriverMobile(token);
            String url = appShareUrl + "?" + Base64.encodeBase64String(mobilePhone.getBytes("UTF-8"));
            String msg = "【快到网】我正在使用快到网手机配货，免费、安全、货多、更新快，提前预约回程货，点击下载安装！让货主联系咱！快到网服务热线：4001515856";
            appShareBo.setAppShareUrl(url);
            appShareBo.setAppShareContent(msg);
            return appShareBo;
        }catch (Exception e){
            if(logger.isErrorEnabled()){
                logger.error("生成app分享内容出现异常", e);
            }
        }
        return null;
    }

    @Override
    public Response<Boolean> share(String recommendMobile, String beRecommendMobile) {
        return driverAppShareService.share(recommendMobile, beRecommendMobile);
    }

    /**
     * 保存货主分享信息
     */
    @Override
    public Long saveOwnerShare(String recommendedMobileNum, String recommendMobileNum) {
        WebUserInfoDTO user = webUserHandleService.getWebUserByMobile(recommendedMobileNum);
        if (user != null) {
            throw new ValidException(ValidException.LOGIC_ERROR, "手机号码已注册货主");
        }
        AppShareInfoDTO appShareInfoDTO = new AppShareInfoDTO();
        appShareInfoDTO.setRecommendedMobileNum(recommendedMobileNum);
        appShareInfoDTO.setRecommendMobileNum(recommendMobileNum);
        appShareInfoDTO.setAppKind(Constants.APP_KIND_OWNER);
        appShareInfoDTO.setShareType(Constants.SHARE_TYPE_APP);
        Response<Long> result = appShareInfoService.saveAppShare(appShareInfoDTO);
        if (!result.isSuccess() || result.getData() == null) {
            throw new ValidException(ValidException.SAVE_ERROR, "保存分享信息失败");
        }
        return result.getData();
    }

    /**
     * 注册后修改分享状态
     */
    @Override
    public ShareUserDTO regUpdateStateByDriver(String regMobilePhone) {
        Response<ShareUserDTO> response = appShareInfoService.regUpdateState(regMobilePhone, ShareUserDTO.DRIVER);
        if (!response.isSuccess()) {
            logger.error("调用pass服务注册后修改分享状态失败，失败信息={}", response.getMessage());
        }
        return response.getData();
    }

    @Value("${driver.app.share.url}")
    public void setAppShareUrl(String appShareUrl) {
        this.appShareUrl = appShareUrl;
    }
}
