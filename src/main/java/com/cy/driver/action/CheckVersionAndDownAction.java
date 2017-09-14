package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.util.FileOperateUtil;
import com.cy.driver.service.CheckVersionAndDownService;
import com.cy.pass.service.dto.AppReleasesDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 类描述：系统接口
 *  作者 yanst 创建时间： 2015-6-19 下午2:59:10
 */
@Scope("prototype")
@RestController("checkVersionAndDownAction")
public class CheckVersionAndDownAction extends BaseAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String apkPathAndroidUrl;
    private String apkIosUrl;

	@Resource
	private CheckVersionAndDownService checkVersionAndDownService;

	/**
	 * 系统版本检查(系统接口)
	 * @param request
	 * @param response
	 * @param versionCode
	 * @param versionName
	 * @return
	 */
	@RequestMapping(value = "/systemVersionCheckAndDown", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_CHECK_VERSION_DOWN)
//	@Log(type = LogEnum.CHK_APP_VERSION)
	public Object systemVersionCheckAndDown(HttpServletRequest request, HttpServletResponse response, String versionCode, String versionName) {
		try {
			String source = findSource(request);
            String apkVersion = request.getHeader(Constants.REQ_HEAD_APK_VERSION);// 版本号
            if(logger.isDebugEnabled()){
                logger.debug("系统版本检查的参数versionCode={},versionName={},apkVersion={}",versionCode,versionName,apkVersion);
            }
            if("3.0".equals(apkVersion)){
                //兼容设置
                versionCode = "30";
            }
			Response<AppReleasesDTO> Response = checkVersionAndDownService.systemVersionCheckAndDown(versionCode, versionName, source);
			if (Response.isSuccess()) {
				updRespHeadSuccess(response);
				return Response.getData();
			}
			if (Response.getCode() == CodeTable.INVALID_ARGS.getCode()) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
			}
			if (Response.getCode() == CodeTable.VERSION_NOT_FOUND.getCode()) {
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20048);
			}
			updRespHeadSuccess(response);
			return Response.getData();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("systemVersionCheckAndDown 出错。", e);
			}
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	/**
	 * apk 文件下载
	 * @param request
	 * @param response
	 * @param regFrom
	 *            regFrom 来源（0 官网 1 短信 2 电话 3 分享 4 地推 5 应用市场 6 车货匹配系统短信 7
	 *            货主版订单导入
	 * @return
	 */
	@RequestMapping(value = "/appDown")
	@ResponseBody
	@JsonSerialize
//	@Log(type = LogEnum.APP_DOWN)
	public Object appDown(HttpServletRequest request, HttpServletResponse response, final String regFrom) {
		try {
			final String source = "android";
			String androidDownloadFilepath = "";
			String str = "";
			if (source.equalsIgnoreCase(Constants.OS_ANDROID)) {
				str = ".apk";
				androidDownloadFilepath = apkPathAndroidUrl;
			} else if (source.equalsIgnoreCase(Constants.OS_IOS)) {
				str = ".ipa";
				androidDownloadFilepath = apkIosUrl;
			}
			int from = 0;
			if (StringUtils.isNotBlank(regFrom)) {
				from = Integer.parseInt(regFrom);
			}
			String contentType = "application/octet-stream;charset=utf-8";
            String filePath = androidDownloadFilepath + "driver_" + from + str;
            String defaultPath = androidDownloadFilepath + "driver_0" + str;


			ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();
			xrayExecutor.submit(new Runnable() {
				public void run() {
					checkVersionAndDownService.appDown(source, regFrom);
				}
			});
			updRespHeadSuccess(response);
			FileOperateUtil.downloadFromDisk(request, response, filePath, contentType, defaultPath);
			return null;
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("apk下载失败。", e);
			}
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

    @Value("#{propertiesReader['apk.path.android']}")
    public void setApkPathAndroidUrl(String apkPathAndroidUrl) {
        this.apkPathAndroidUrl = apkPathAndroidUrl;
    }

    @Value("#{propertiesReader['apk.path.ios']}")
    public void setApkIosUrl(String apkIosUrl) {
        this.apkIosUrl = apkIosUrl;
    }
}
