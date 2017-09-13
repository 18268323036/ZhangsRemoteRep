package com.cy.driver.cloudService;

import com.cy.basic.service.dto.SystemConfigDTO;
import com.cy.basic.service.dto.SystemProtocolDTO;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.rdcservice.service.dto.AuthedInfoDTO;
import com.cy.rdcservice.service.dto.UserItemStatDTO;
import com.cy.rdcservice.service.dto.UserLoginInfoDTO;

/**
 * @author zhangxy 2016/7/25
 */
public interface CloudUserService {

    UserLoginInfoDTO getOwnerInfo(Long userId);

    AuthedInfoDTO getCompanyAuthedInfo(Long companyId);

    AuthedInfoDTO getCompanyAuthedInfoByUserId(Long userId);

    UserItemStatDTO getItemInfo(Long userId);

    UserLoginInfoDTO getUserInfoByPhone(String siteCode, String mobilePhone);

    DriverUserInfoDTO getDriverInfoByCode(String code);

    /**
     * 根据平台编号获取详情
     * @param siteCode 平台编号
     * @return
     */
    SystemConfigDTO getBySiteCode(String siteCode);

    /**
     * 根据平台编码查询协议内容
     * @param siteCode
     * @return
     */
    SystemProtocolDTO getProtocolDTOBySiteCode(String siteCode);
}
