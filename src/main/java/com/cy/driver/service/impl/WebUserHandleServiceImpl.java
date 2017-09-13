package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.common.throwex.ValidException;
import com.cy.driver.service.WebUserHandleService;
import com.cy.pass.service.WebUserInfoService;
import com.cy.pass.service.dto.CompanyInfoDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 企业货主Handle实现类
 * Created by mr on 2015/7/14.
 */
@Service
public class WebUserHandleServiceImpl implements WebUserHandleService {
    private static final Logger LOG = LoggerFactory.getLogger(WebUserHandleServiceImpl.class);
    @Resource
    private WebUserInfoService webUserInfoService;


    @Override
    public Response<CompanyInfoDTO> getCompanyDetail(Long companyId) {
        return webUserInfoService.getCompanyDetail(companyId);
    }

    @Override
    public Response<WebUserInfoDTO> getWebUserByCompanyId(Long companyId) {
        return webUserInfoService.getWebUserByCompanyId(companyId);
    }

    /**
     * 根据id查询企业用户（包含分包商）信息
     */
    @Override
    public List<WebUserInfoDTO> ListForWebUserInfo(List<Long> userIds){
        Response<List<WebUserInfoDTO>> result = webUserInfoService.listByUserIds(userIds);
        if (result == null) {
            LOG.error("调用pass服务根据id查询企业用户（包含分包商）信息出错,对象信息为空。userIds={}", JSON.toJSONString(userIds));
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用pass服务根据id查询企业用户（包含分包商）信息失败,userIds={},返回信息={}", JSON.toJSONString(userIds), result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用pass服务查询web用户信息失败,userIds={},返回web用户信息为空", JSON.toJSONString(userIds));
            return null;
        }
        return result.getData();
    }


    /**
     * 根据webUserId查询web用户信息
     */
    @Override
    public WebUserInfoDTO getWebUserInfo(Long webUserId){
        Response<WebUserInfoDTO> result = webUserInfoService.getWebUserInfo(webUserId);
        if (result == null) {
            LOG.error("调用pass服务查询web用户信息出错,webUserId={}", webUserId);
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用pass服务查询web用户信息失败,webUserId={},返回信息={}", webUserId, result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用pass服务查询web用户信息失败,webUserId={},返回web用户信息为空", webUserId);
            return null;
        }
        return result.getData();
    }

    /**
     * 获得企业信息
     */
    @Override
    public CompanyInfoDTO getCompanyInfo(Long companyId) {
        Response<CompanyInfoDTO> result = webUserInfoService.getCompanyDetail(companyId);
        if (result == null) {
            LOG.error("调用pass服务查询企业信息出错,companyId={}", companyId);
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用pass服务查询企业信息失败,companyId={},返回信息={}", companyId, result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用pass服务查询企业信息失败,companyId={},返回企业信息为空", companyId);
            return null;
        }
        return result.getData();
    }

    /**
     * 根据注册手机号码查询web用户信息
     */
    @Override
    public WebUserInfoDTO getWebUserByMobile(String mobilePhone) {
        Response<WebUserInfoDTO> result = webUserInfoService.findByPhone(mobilePhone);
        if (!result.isSuccess()) {
            LOG.error("调用pass服务根据注册手机号码查询web用户信息失败，失败信息=", result.getMessage());
            throw new ValidException(ValidException.QUERY_ERROR, "根据注册手机号码查询web用户信息查询失败");
        }
        return result.getData();
    }

    /**
     * 根据企业id查询web用户信息
     */
    @Override
    public WebUserInfoDTO getByCompanyId(Long companyId) {
        Response<WebUserInfoDTO> response = webUserInfoService.getWebUserByCompanyId(companyId);
        if (!response.isSuccess()) {
            LOG.error("调用pass服务根据企业id查询web用户信息失败，失败信息={}", response.getMessage());
        }
        return response.getData();
    }
}
