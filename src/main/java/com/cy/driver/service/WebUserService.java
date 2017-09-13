package com.cy.driver.service;

import com.cy.pass.service.dto.WebUserInfoDTO;

import java.util.List;

/**
 * @author yanst 2016/4/26
 */
public interface WebUserService {

    /**
     * 根据企业id查询企业信息
     * @param companyIds
     * @return
     */
    List<WebUserInfoDTO> listBycompanyIds(List<Long> companyIds);

}
