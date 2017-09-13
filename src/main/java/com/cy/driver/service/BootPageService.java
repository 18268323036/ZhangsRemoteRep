package com.cy.driver.service;

import com.cy.pass.service.dto.BootPageInfoDTO;
import com.cy.pass.service.dto.ImgDeployInfoDTO;
import com.cy.pass.service.dto.base.Response;

import java.util.List;


public interface BootPageService {

	public Response<List<BootPageInfoDTO>> findImgStandard(String imgStandard);

	/**
	 * 根据类型获取图片信息
	 * @param useFor
	 * @param imgStandard
     * @return
     */
	List<ImgDeployInfoDTO> findImgStandard(Short useFor, String imgStandard);
}
