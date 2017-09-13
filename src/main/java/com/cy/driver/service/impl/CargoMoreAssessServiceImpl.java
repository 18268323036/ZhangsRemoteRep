package com.cy.driver.service.impl;

import com.cy.cargo.service.CargoAssessService;
import com.cy.cargo.service.dto.CargoAssessInfoDTO;
import com.cy.cargo.service.dto.CargoAssessInfoListDTO;
import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.cargo.service.dto.base.PageInfo;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.domain.CargoMoreAssessBO;
import com.cy.driver.domain.CargoMoreAssessListBO;
import com.cy.driver.service.CargoMoreAssessService;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.dto.DriverInfoDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/12.
 */
@Service("cargoMoreAssessService")
public class CargoMoreAssessServiceImpl implements CargoMoreAssessService {

    @Resource
    private CargoAssessService cargoAssessService;

    @Resource
    private DriverUserInfoService driverUserInfoService;

    /**
     * 更多点评
     * @param driverId
     * @param page
     * @return
     */
    @Override
    public Response<CargoMoreAssessListBO> getCargoMoreAssess(long driverId, int page , long cargoId){
        PageInfo queryPage = new PageInfo(page);
        Response<CargoAssessInfoListDTO> cargoAssessInfoListDTOResponse = cargoAssessService.getAssessInfo(cargoId, queryPage);
        if (cargoAssessInfoListDTOResponse.isSuccess()) {
            CargoAssessInfoListDTO cargoAssessInfoListDTO = cargoAssessInfoListDTOResponse.getData();
            if (cargoAssessInfoListDTO.getDriverCargoAssessInfoList().size() == 0) {
                return new Response<CargoMoreAssessListBO>(CodeTable.INVALID_CODE);
            }
            CargoMoreAssessListBO cargoMoreAssessListBO = new CargoMoreAssessListBO();
            List<CargoMoreAssessBO> cargoAssessInfoDTOList = new ArrayList<CargoMoreAssessBO>();
            for (CargoAssessInfoDTO cargoAssessInfoDTO : cargoAssessInfoListDTO.getDriverCargoAssessInfoList()) {
                CargoMoreAssessBO cargoMoreAssessBO = new CargoMoreAssessBO();
                cargoMoreAssessBO.setAssessId(cargoAssessInfoDTO.getId());
                cargoMoreAssessBO.setAssessInfo(cargoAssessInfoDTO.getAssessInfo());
                cargoMoreAssessBO.setCreateTime(cargoAssessInfoDTO.getCreateTime());
                cargoMoreAssessBO.setType(cargoAssessInfoDTO.getType());
                com.cy.pass.service.dto.base.Response resultDriverInfo = driverUserInfoService.getInfoNotDriverImg(cargoAssessInfoDTO.getDriverId());
                if(resultDriverInfo.getCode()== com.cy.pass.service.dto.base.CodeTable.INVALID_ARGS.getCode())
                {
                    return new Response<CargoMoreAssessListBO>(CodeTable.INVALID_ARGS);
                }
                if (!resultDriverInfo.isSuccess()) {
                    return new Response<CargoMoreAssessListBO>(CodeTable.EXCEPTION);
                }
                DriverInfoDTO driverInfoDTO = (DriverInfoDTO) resultDriverInfo.getData();
                cargoMoreAssessBO.setMobilePhone(driverInfoDTO.getMobilePhone());
                cargoMoreAssessBO.setAuthStatus(driverInfoDTO.getAuthStatus());
                cargoAssessInfoDTOList.add(cargoMoreAssessBO);
            }
            cargoMoreAssessListBO.setCargoAssessCount(cargoAssessInfoListDTO.getCargoAssessCount());
            cargoMoreAssessListBO.setCargoAssessTotalPages(cargoAssessInfoListDTO.getCargoAssessTotalPages());
            cargoMoreAssessListBO.setCargoMoreAssessBOList(cargoAssessInfoDTOList);
            return new Response<CargoMoreAssessListBO>(cargoMoreAssessListBO);
        }
        return new Response<CargoMoreAssessListBO>(CodeTable.EXCEPTION);
    }
}
