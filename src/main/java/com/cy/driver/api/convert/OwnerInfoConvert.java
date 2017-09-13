package com.cy.driver.api.convert;

import com.cy.driver.api.domain.res.OwnerInfoDetail;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.StrUtil;
import com.cy.pass.service.dto.CompanyInfoDTO;
import com.cy.pass.service.dto.OwnerItemStatDTO;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.rdcservice.service.dto.AuthedInfoDTO;
import com.cy.rdcservice.service.dto.UserItemStatDTO;
import com.cy.rdcservice.service.dto.UserLoginInfoDTO;

/**
 * @author yanst 2016/5/31 14:33
 */
public class OwnerInfoConvert {

    /**
     * 快到网用户信息转换
     * @param companyId
     * @param companyInfoDTO
     * @param webUserInfoDTO
     * @param ownerItemStatDTO
     * @return
     */
    public static OwnerInfoDetail kdwOwnerInfoConvert(Long companyId, CompanyInfoDTO companyInfoDTO, WebUserInfoDTO webUserInfoDTO, OwnerItemStatDTO ownerItemStatDTO){
        OwnerInfoDetail ownerInfoDetail =  new OwnerInfoDetail();
        if(companyInfoDTO != null){
            if(companyId.longValue() == 1){
                ownerInfoDetail.setCompanyName(companyInfoDTO.getCompanyName());//公司名称
            }else {
                ownerInfoDetail.setCompanyName("货主");//公司名称
            }

            ownerInfoDetail.setCompanyAddress(companyInfoDTO.getCompanyAddress());//公司所在地
            ownerInfoDetail.setTelephone(companyInfoDTO.getTelephone());//固定电话
        }

        if(webUserInfoDTO != null){
            if(companyId.longValue() == 1) {
                ownerInfoDetail.setAuthStatus("0");
            }else {
                //认证状态
                ownerInfoDetail.setAuthStatus(webUserInfoDTO.getSubmitType() == null ? null : webUserInfoDTO.getSubmitType().toString());
                if(webUserInfoDTO.getSubmitType() != null && webUserInfoDTO.getSubmitType().intValue() == 3){
                    ownerInfoDetail.setAuthTime(DateUtil.dateFormat(webUserInfoDTO.getAuditTime(), DateUtil.F_DATE));//认证时间
                }
            }
            ownerInfoDetail.setContactName(webUserInfoDTO.getContracter());//联系人
            ownerInfoDetail.setMobile(webUserInfoDTO.getContactMobiphone());//手机
        }
        //信用
        if(ownerItemStatDTO != null){
            ownerInfoDetail.setCredit(ownerItemStatDTO.getCreditGrade());
        }
        return ownerInfoDetail;
    }


    public static OwnerInfoDetail qypsOwnerInfoConvert(UserLoginInfoDTO userLoginInfoDTO, AuthedInfoDTO authedInfoDTO, UserItemStatDTO userItemStatDTO){
        OwnerInfoDetail ownerInfoDetail =  new OwnerInfoDetail();
        ownerInfoDetail.setAuthStatus("3");
        ownerInfoDetail.setAuthTime(DateUtil.dateFormat(authedInfoDTO.getCreateTime()));
        ownerInfoDetail.setCompanyAddress(StrUtil.strJoint(authedInfoDTO.getProvinceName(),authedInfoDTO.getCityName(),authedInfoDTO.getCountyName(),authedInfoDTO.getAddress()));
        ownerInfoDetail.setCompanyName(authedInfoDTO.getAuthName());
        ownerInfoDetail.setContactName(userLoginInfoDTO.getName());
        ownerInfoDetail.setMobile(userLoginInfoDTO.getMobilephone());
        ownerInfoDetail.setTelephone(authedInfoDTO.getContactWay());
        ownerInfoDetail.setCredit(userItemStatDTO.getCreditGrade());
        return ownerInfoDetail;
    }
}
