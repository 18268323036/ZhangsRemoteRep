package com.cy.driver.service;

import com.cy.pass.service.dto.AppReleasesDTO;
import com.cy.pass.service.dto.base.Response;

public interface CheckVersionAndDownService {
	
	public Response<AppReleasesDTO> systemVersionCheckAndDown(String versionCode, String versionName, String source);
	
	@SuppressWarnings("rawtypes")
	public Response appDown(String source, String regFrom);
	
}
