package com.cy.driver.api.convert;

import com.cy.rdcservice.service.dto.ModifyUserDTO;
import com.cy.rdcservice.service.dto.TrackingSaveDTO;

/**
 * @author yanst 2016/8/9 11:07
 */
public class TrackingSaveConvert {

    public static TrackingSaveDTO makeTrackingSaveDTO(String operateType, String province, String city, String county, String address, String longitude, String latitude) {
        TrackingSaveDTO trackingSaveDTO = new TrackingSaveDTO();
        if (Integer.valueOf(operateType) == 22) {
            trackingSaveDTO.setOperateType(1);//状态为接单
        } else if (Integer.valueOf(operateType) == 23) {
            trackingSaveDTO.setOperateType(3);//状态为装货
        } else if (Integer.valueOf(operateType) == 24) {
            trackingSaveDTO.setOperateType(4);//状态为卸货
        }else if(Integer.valueOf(operateType) == 6){
            trackingSaveDTO.setOperateType(6);//状态为签收
        }
        trackingSaveDTO.setProvince(province);
        trackingSaveDTO.setCounty(county);
        trackingSaveDTO.setCity(city);
        trackingSaveDTO.setCounty(county);
        trackingSaveDTO.setAddress(address);
        trackingSaveDTO.setLatitude(latitude);
        trackingSaveDTO.setLongitude(longitude);
        return trackingSaveDTO;
    }


    public static com.cy.saas.business.model.dto.TrackingSaveDTO saasMakeTrackingSaveDTO(String operateType, String province, String city, String county, String address, String longitude, String latitude) {
        com.cy.saas.business.model.dto.TrackingSaveDTO trackingSaveDTO = new com.cy.saas.business.model.dto.TrackingSaveDTO();
        if (Integer.valueOf(operateType) == 22) {
            trackingSaveDTO.setOperateType(1);//状态为接单
        } else if (Integer.valueOf(operateType) == 23) {
            trackingSaveDTO.setOperateType(3);//状态为装货
        } else if (Integer.valueOf(operateType) == 24) {
            trackingSaveDTO.setOperateType(4);//状态为卸货
        }else if(Integer.valueOf(operateType) == 6){
            trackingSaveDTO.setOperateType(6);//状态为签收
        }
        trackingSaveDTO.setProvince(province);
        trackingSaveDTO.setCounty(county);
        trackingSaveDTO.setCity(city);
        trackingSaveDTO.setCounty(county);
        trackingSaveDTO.setAddress(address);
        trackingSaveDTO.setLatitude(latitude);
        trackingSaveDTO.setLongitude(longitude);
        return trackingSaveDTO;
    }


    public static ModifyUserDTO makeModifyUserDTO(Long modifyUserId, String modifyUserName) {
        ModifyUserDTO modifyUserDTO = new ModifyUserDTO();
        modifyUserDTO.setModifyUserName(modifyUserName);
        modifyUserDTO.setModifyOwnUserId(modifyUserId);
        modifyUserDTO.setModifyUserId(modifyUserId);
        modifyUserDTO.setModifyUserType((byte) 1);
        modifyUserDTO.setModifyUserAccountType(null);
        return modifyUserDTO;
    }

    public static com.cy.saas.business.model.dto.ModifyUserDTO saasMakeModifyUserDTO(Long modifyUserId, String modifyUserName) {
        com.cy.saas.business.model.dto.ModifyUserDTO modifyUserDTO = new com.cy.saas.business.model.dto.ModifyUserDTO();
        modifyUserDTO.setModifyUserName(modifyUserName);
        modifyUserDTO.setModifyOwnUserId(modifyUserId);
        modifyUserDTO.setModifyUserId(modifyUserId);
        modifyUserDTO.setModifyUserType((byte) 1);
        modifyUserDTO.setModifyUserAccountType(null);
        return modifyUserDTO;
    }
}
