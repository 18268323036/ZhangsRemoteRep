package com.cy.driver.service.impl;

import com.cy.cargo.service.CargoAssessService;
import com.cy.cargo.service.CargoService;
import com.cy.cargo.service.QuoteService;
import com.cy.cargo.service.dto.*;
import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.domain.CargoCityParamBO;
import com.cy.driver.service.CargoHandlerService;
import com.cy.pass.service.DriverUserInfoService;
import com.cy.pass.service.dto.DriverBusinessLineDTO;
import com.cy.pass.service.dto.DriverEmptyReportInfoDTO;
import com.cy.pass.service.dto.DriverInfoDTO;
import com.cy.pass.service.dto.DriverTodayEmptyReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Administrator on 2015/7/14.
 */
@Service("cargoHandlerService")
public class CargoHandlerServiceImpl implements CargoHandlerService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DriverUserInfoService driverUserInfoService;

    @Resource
    private CargoService cargoService;

    @Resource
    private CargoAssessService cargoAssessService;

    @Resource
    private QuoteService quoteService;

    /**
     * 	省列表
     * @return
     */
    @Override
    public Response<List<TasteCargoDTO>> listCountByProvince () throws Exception
    {
        return cargoService.listCountByProvince();
    }

    /**
     * //集合按照时间进行排序
     * @param reportInfoDTOResponse
     */
    private List<CityParamDTO> convertCargoParamData( com.cy.pass.service.dto.base.Response<DriverEmptyReportInfoDTO> reportInfoDTOResponse){
        List<CargoCityParamBO> cityParamDTOList = new ArrayList<CargoCityParamBO>();

        if(reportInfoDTOResponse.isSuccess() && reportInfoDTOResponse.getData() != null){
            List<DriverTodayEmptyReportDTO>  todayEmptyReportDTOList = reportInfoDTOResponse.getData().getTodayEmptyCarList();
            for (DriverTodayEmptyReportDTO driverTodayEmptyReportDTO : todayEmptyReportDTOList){
                CargoCityParamBO cityParamDTO = new CargoCityParamBO();
                cityParamDTO.setStartProvince(driverTodayEmptyReportDTO.getStartProvinceValue());
                cityParamDTO.setStartProviceCode(driverTodayEmptyReportDTO.getStartProvinceCode());
                cityParamDTO.setStartCity(driverTodayEmptyReportDTO.getStartCityValue());
                cityParamDTO.setStartCityCode(driverTodayEmptyReportDTO.getStartCityCode());

                cityParamDTO.setEndProvince(driverTodayEmptyReportDTO.getEndProvinceValue());
                cityParamDTO.setEndProviceCode(driverTodayEmptyReportDTO.getEndProvinceCode());
                cityParamDTO.setEndCity(driverTodayEmptyReportDTO.getEndCityValue());
                cityParamDTO.setEndCityCode(driverTodayEmptyReportDTO.getEndCityCode());
                cityParamDTO.setStartDate(driverTodayEmptyReportDTO.getStartTime());
                cityParamDTO.setEndDate(driverTodayEmptyReportDTO.getStartTime());
                cityParamDTO.setSortCode(driverTodayEmptyReportDTO.getStartTime().getTime());
                cityParamDTOList.add(cityParamDTO);
            }

            List<DriverBusinessLineDTO> businessLineDTOList = reportInfoDTOResponse.getData().getFutureEmptyCarList();
            for (DriverBusinessLineDTO driverBusinessLineDTO : businessLineDTOList){
                CargoCityParamBO cityParamDTO = new CargoCityParamBO();
                cityParamDTO.setStartProvince(driverBusinessLineDTO.getStartProvinceValue());
                cityParamDTO.setStartProviceCode(driverBusinessLineDTO.getStartProvinceCode());
                cityParamDTO.setStartCity(driverBusinessLineDTO.getStartCityValue());
                cityParamDTO.setStartCityCode(driverBusinessLineDTO.getStartCityCode());

                cityParamDTO.setEndProvince(driverBusinessLineDTO.getEndProvinceValue());
                cityParamDTO.setEndProviceCode(driverBusinessLineDTO.getEndProvinceCode());
                cityParamDTO.setEndCity(driverBusinessLineDTO.getEndCityValue());
                cityParamDTO.setEndCityCode(driverBusinessLineDTO.getEndCityCode());
                cityParamDTO.setStartDate(driverBusinessLineDTO.getStartTime());
                cityParamDTO.setEndDate(driverBusinessLineDTO.getEndTime());
                Date date = driverBusinessLineDTO.getModifyTime();
                if(date == null){
                    date = driverBusinessLineDTO.getStartTime();
                }
                cityParamDTO.setSortCode(date.getTime());
                cityParamDTOList.add(cityParamDTO);
            }

        }
        List<CityParamDTO> list = new ArrayList<CityParamDTO>();
        if(cityParamDTOList == null || cityParamDTOList.size() == 0){
            return list;
        }

        Collections.sort(cityParamDTOList, new Comparator<CargoCityParamBO>() {
            public int compare(CargoCityParamBO arg0, CargoCityParamBO arg1) {
                return arg0.getSortCode().compareTo(arg1.getSortCode());
            }
        });

        //封装成CityParamDTO
        int j=0;
        for (int i = cityParamDTOList.size()-1; i >= 0 ; i--) {
            CargoCityParamBO cargoCityParamBO = cityParamDTOList.get(i);
            CityParamDTO cityParamDTO = new CityParamDTO();
            cityParamDTO.setStartProvince(cargoCityParamBO.getStartProvince());
            cityParamDTO.setStartProviceCode(cargoCityParamBO.getStartProviceCode());
            cityParamDTO.setStartCity(cargoCityParamBO.getStartCity());
            cityParamDTO.setStartCityCode(cargoCityParamBO.getStartCityCode());
            cityParamDTO.setEndProvince(cargoCityParamBO.getEndProvince());
            cityParamDTO.setEndProviceCode(cargoCityParamBO.getEndProviceCode());
            cityParamDTO.setEndCity(cargoCityParamBO.getEndCity());
            cityParamDTO.setEndCityCode(cargoCityParamBO.getEndCityCode());
            cityParamDTO.setStartDate(cargoCityParamBO.getStartDate());
            cityParamDTO.setEndDate(cargoCityParamBO.getEndDate());
            cityParamDTO.setSortCode(j);
            j++;
            list.add(cityParamDTO);
        }

//        int index = 0;
//        for (CargoCityParamBO cargoCityParamBO : cityParamDTOList){
//
//            index ++;
//        }

        return list;
    }

    /**
     * 获取货源详情
     *
     * @param cargoId
     * @return
     */
    @Override
    public Response<CargoDetailDTO> getCargoDetail(long cargoId ,long driverId) {
        Response<CargoDetailDTO> response = cargoService.getCargoDetail(cargoId, driverId);
        if (response.getCode() == CodeTable.NOT_EXISTS_CARGO.getCode()) {
            return new Response<CargoDetailDTO>(CodeTable.NOT_EXISTS_CARGO);
        }
        if (response.isSuccess()) {
            CargoDetailDTO cargoDetailDTO = response.getData();
            if (cargoDetailDTO.getDriverId() != null) {
                com.cy.pass.service.dto.base.Response resultDriverInfo = driverUserInfoService.getInfoNotDriverImg(cargoDetailDTO.getDriverId());
                if (response.isSuccess() && resultDriverInfo.getData() != null) {
                    DriverInfoDTO driverInfoDTO = (DriverInfoDTO) resultDriverInfo.getData();
                    cargoDetailDTO.setCommentDriverMobile(driverInfoDTO.getMobilePhone());
                    cargoDetailDTO.setCommentDriverStatus(driverInfoDTO.getAuthStatus());
                }
            }
            return new Response<CargoDetailDTO>(cargoDetailDTO);
        }
        return new Response<CargoDetailDTO>(CodeTable.EXCEPTION);
    }

    @Override
    public Response<CargoInfoDTO> getCargoInfo(Long cargoId) {
        return cargoService.getInfo(cargoId);
    }

    @Override
    public Response<Boolean> addCargoAssess(DriverCargoAssessInfoDTO cargoAssessDTO) {
        return cargoAssessService.addCargoAssess(cargoAssessDTO);
    }

    @Override
    public Response<Boolean> addQuoteInfo(QuoteInfoDTO quoteDTO) {
        return quoteService.addInfo(quoteDTO);
    }

}
