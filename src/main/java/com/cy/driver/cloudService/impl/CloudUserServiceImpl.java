package com.cy.driver.cloudService.impl;

import com.cy.basic.service.SystemConfigService;
import com.cy.basic.service.SystemProtocolService;
import com.cy.basic.service.dto.SystemConfigDTO;
import com.cy.basic.service.dto.SystemProtocolDTO;
import com.cy.driver.cloudService.CloudUserService;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.dto.DriverUserInfoDTO;
import com.cy.rdcservice.service.UserAuthService;
import com.cy.rdcservice.service.UserItemStatService;
import com.cy.rdcservice.service.UserService;
import com.cy.rdcservice.service.dto.AuthedInfoDTO;
import com.cy.rdcservice.service.dto.UserItemStatDTO;
import com.cy.rdcservice.service.dto.UserLoginInfoDTO;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yanst 2016/7/25 15:36
 */
@Service
public class CloudUserServiceImpl implements CloudUserService {

    @Resource
    private UserService userService;
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private UserItemStatService userItemStatService;
    @Resource
    private DriverUserInfoService driverUserInfoService;
    @Resource
    private SystemConfigService systemConfigService;
    @Resource
    private SystemProtocolService systemProtocolService;


    Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Override
    public UserLoginInfoDTO getOwnerInfo(Long userId) {
        Response<UserLoginInfoDTO> response = userService.getUser(userId);
        if(response == null){
            LOG.error("获取用户信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取用户信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }

    /**
     * 获取用户认证信息（根据企业id）
     * @param companyId
     * @return
     */
    @Override
    public AuthedInfoDTO getCompanyAuthedInfo(Long companyId) {
        Response<AuthedInfoDTO> response = userAuthService.get(companyId);
        if(response == null){
            LOG.error("获取用户认证信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取用户认证信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }


    /**
     * 获取用户认证信息（根据企业id）
     * @param userId
     * @return
     */
    @Override
    public AuthedInfoDTO getCompanyAuthedInfoByUserId(Long userId) {
        Response<AuthedInfoDTO> response = userAuthService.getByUserId(userId);
        if(response == null){
            LOG.error("获取用户认证信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取用户认证信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }

    /**
     * 获取用户业务信息
     * @param userId
     * @return
     */
    @Override
    public UserItemStatDTO getItemInfo(Long userId) {
        Response<UserItemStatDTO> response = userItemStatService.getByUserId(userId);
        if(response == null){
            LOG.error("获取用户业务信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取用户业务信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }


    /**
     * 根据手机号获得用户信息
     * @param siteCode  平台编码
     * @param mobilePhone   手机号
     * @return
     */
    @Override
    public UserLoginInfoDTO getUserInfoByPhone(String siteCode, String mobilePhone) {
        Response<UserLoginInfoDTO> response = userService.getBySiteCodeMobile(siteCode,mobilePhone);
        if(response == null){
            LOG.error("获取用户认证信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取用户认证信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }


    @Override
    public DriverUserInfoDTO getDriverInfoByCode(String code) {
        com.cy.pass.service.dto.base.Response<DriverUserInfoDTO> response = driverUserInfoService.findByCode(code);
        if(response == null){
            LOG.error("获取司机信息失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            LOG.error("获取司机信息异常，返回信息：{}", response.getMessage());
            return null;
        }
        return response.getData();
    }


    /**
     * 根据平台编号获取详情
     * @param siteCode 平台编号
     * @return
     */
    @Override
    public SystemConfigDTO getBySiteCode(String siteCode) {
        try {
            LOG.debug("CLOUDSERVICE:根据平台编号获取详情接口开始.入参siteCode={}",siteCode);
            Response<SystemConfigDTO> response = systemConfigService.getBySiteCode(siteCode);
            if(response == null) {
                LOG.error("CLOUDSERVICE:根据平台编号获取详情接口网络异常.入参siteCode={}",siteCode);
                return null;
            }
            if(response.getCode() != 0) {
                LOG.error("CLOUDSERVICE:根据平台编号获取详情接口失败.入参siteCode={},返回code={},message={}",siteCode,response.getCode(),response.getMessage());
                return null;
            }
            if(response.getData() == null) {
                LOG.debug("CLOUDSERVICE:根据平台编号获取详情接口查询成功,平台编码不存在.入参siteCode={},返回code={},message={}",siteCode,response.getCode(),response.getMessage());
                return null;
            }
            return response.getData();
        }catch (Exception e) {
            LOG.error("CLOUDSERVICE:根据平台编号获取详情接口异常.入参siteCode={},异常信息E={}",siteCode,e);
        }
        return null;
    }


    @Override
    public SystemProtocolDTO getProtocolDTOBySiteCode(String siteCode) {
        Response<SystemProtocolDTO> response = systemProtocolService.getSiteProtocol(siteCode);
        if(response==null){
            LOG.debug("调用systemProtocolService.getSiteProtocol查询平台信息异常，异常原因response为null");
            return null;
        }
        if(!response.isSuccess()){
            LOG.debug("调用systemProtocolService.getSiteProtocol查询平台信息异常，异常原因={}",response.getMessage());
            return null;
        }
        return response.getData();
    }


}
