package com.cy.driver.service;

import com.cy.pass.service.dto.DriverImgDTO;
import com.cy.pass.service.dto.base.Response;

import java.util.List;


public interface DriverImgService {

	/**
	 * 修改司机图片信息
	 * @param token
	 * @param fileMd5
	 * @param driverImgType
	 * @return
	 */
	public Response<Boolean> uploadDriverImg(String token, String fileMd5, Byte driverImgType);

	/**
	 * 获取图片信息
	 * @param driverId
	 * @param type
	 * @return
	 */
	public String findByType(Long driverId, Byte type);

	List<DriverImgDTO> queryImgInfo(Long driverId, List<Byte> imgTypeList);
}
