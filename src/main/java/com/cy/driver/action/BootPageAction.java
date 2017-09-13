package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.util.FileOperateUtil;
import com.cy.driver.service.BootPageService;
import com.cy.pass.service.dto.BootPageInfoDTO;
import com.cy.pass.service.dto.base.Response;
import org.codehaus.jackson.map.annotate.JsonSerialize;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类描述：     引导页接口	
 * 作者		yanst
 * 创建时间：	2015-6-24 下午8:29:35
 */
@Scope("prototype")
@Controller("bootPageAction")
public class BootPageAction extends BaseAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BootPageService bootPageService;

	/**
	 * 引导页图片列表
	 * @param request
	 * @param response
	 * @param imgStandard 规格（0: 480x320  1:480x800   2:720x1280 ）
	 * @return
	 */
	@RequestMapping(value = "/getBootPageList", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.GET_BOOT_PAGE_LIST)
	public Object getBootPageList(HttpServletRequest request, HttpServletResponse response, String imgStandard ) {
		Map<String ,Object > resultMap = new HashMap<String, Object>();
		try {
			Response<List<BootPageInfoDTO>> resultData = bootPageService.findImgStandard(imgStandard);
			if (resultData.isSuccess()) {
				updRespHeadSuccess(response);
				resultMap.put("pictures", resultData.getData());
				return resultMap;
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("getBootPageList 出错。", e);
			}
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	//TODO
	/**
	 * 引导页图片下载
	 * @param request
	 * @param response
	 * @param fileType 1 ：引导页
	 * @param  fileMD5
	 * @return
	 */
//	@RequestMapping(value = "/downFile")
	@ResponseBody
	@JsonSerialize
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.BOOT_PAGE_PICTURE_DOWN)
	public Object downFile(HttpServletRequest request, HttpServletResponse response, String fileType, String fileMD5) {
		try {
			String contentType = "image/png;charset=utf-8";
			updRespHeadSuccess(response);
			FileOperateUtil.downloadFromDisk(request, response, fileMD5, contentType);
			return null;
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("bootPagePictureDown 下载失败。", e);
			}
			updRespHeadError(response);
			return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
		}
		
	}
	
}
