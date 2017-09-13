package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.service.BootPageService;
import com.cy.pass.service.ImgDeployInfoService;
import com.cy.pass.service.dto.BootPageInfoDTO;
import com.cy.pass.service.dto.ImgDeployInfoDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bootPageService")
public class BootPageServiceImpl implements BootPageService {

	private Logger LOG = LoggerFactory.getLogger(BootPageServiceImpl.class);

    @Resource
	private ImgDeployInfoService imgDeployInfoService;

	@Override
	public Response<List<BootPageInfoDTO>> findImgStandard(String imgStandard) {
		return imgDeployInfoService.findImgStandard(imgStandard);
	}

	@Override
	public List<ImgDeployInfoDTO> findImgStandard(Short useFor, String imgStandard) {
		Response<List<ImgDeployInfoDTO>> response = imgDeployInfoService.listByUserAndStandrand(useFor, imgStandard);
		if(response == null){
			LOG.error("调用底层接口获取图片信息失败，返回对象为空");
		}
		if(!response.isSuccess()){
			LOG.error("调用底层接口出错，错误信息response={}", JSON.toJSONString(response));
		}
		return response.getData();
	}

}
