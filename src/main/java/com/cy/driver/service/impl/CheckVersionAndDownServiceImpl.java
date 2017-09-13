package com.cy.driver.service.impl;

import com.cy.driver.service.CheckVersionAndDownService;
import com.cy.pass.service.AppReleasesService;
import com.cy.pass.service.dto.AppReleasesDTO;
import com.cy.pass.service.dto.base.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("checkVersionAndDownService")
public class CheckVersionAndDownServiceImpl implements CheckVersionAndDownService {
    @Resource
	private AppReleasesService appReleasesService;

	@Override
	public Response<AppReleasesDTO> systemVersionCheckAndDown(String versionCode,String versionName,String source){
		return appReleasesService.systemVersionCheckAndDown( versionCode, versionName, source);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Response appDown(String source,String regFrom){
		return appReleasesService.appDown(source,regFrom);
	}
}
