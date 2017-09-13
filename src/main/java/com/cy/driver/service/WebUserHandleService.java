package com.cy.driver.service;

import com.cy.pass.service.dto.CompanyInfoDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.pass.service.dto.base.Response;

import java.util.List;

/**
 * 企业用户Handle类
 * Created by mr on 2015/7/14.
 */
public interface WebUserHandleService {

    /**
     * 根据企业编号查询企业详细
     * @param companyId
     * @return
     */
    public Response<CompanyInfoDTO> getCompanyDetail(Long companyId);

    /**
     * 根据webUserId查询web货主
     * @param companyId
     * @return
     */
    public Response<WebUserInfoDTO> getWebUserByCompanyId(Long companyId);

    /**
     * 根据id查询企业用户（包含分包商）信息
     * @param userIds 用户id集合
     */
    List<WebUserInfoDTO> ListForWebUserInfo(List<Long> userIds);

    /**
     * 根据webUserId查询web用户信息
     * @param webUserId
     * @return
     * @author wyh
     */
    WebUserInfoDTO getWebUserInfo(Long webUserId);

    /**
     * 获得企业信息
     * @param companyId
     * @return
     * @author wyh
     */
    CompanyInfoDTO getCompanyInfo(Long companyId);

    /**
     * 根据注册手机号码查询web用户信息
     * @param mobilePhone
     * @return
     * @author wyh
     */
    WebUserInfoDTO getWebUserByMobile(String mobilePhone);

    /**
     * 根据企业id查询web用户信息
     * @param companyId
     * @return
     * @author wyh
     */
    WebUserInfoDTO getByCompanyId(Long companyId);
}

