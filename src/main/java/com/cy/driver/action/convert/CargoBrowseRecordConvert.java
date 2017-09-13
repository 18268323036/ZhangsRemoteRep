package com.cy.driver.action.convert;

import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.util.StrUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.CargoListBO;
import com.cy.driver.domain.PageBase;
import com.cy.search.service.dto.response.CargoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanst 2016/4/20 17:17
 */
public class CargoBrowseRecordConvert {

    /**
     * 货源浏览记录
     * @param pageBase
     * @return
     */
    public static PageBase<CargoListBO> ConvertCargoBrowseRecordInfo(PageBase<CargoDTO> pageBase){
        PageBase<CargoListBO> pageBase1 = new PageBase<>();
        if(pageBase == null ){
            return null;
        }
        pageBase1.setTotalPage(pageBase.getTotalPage());
        pageBase1.setTotalNum(pageBase.getTotalNum());
        if(pageBase.getListData() == null || pageBase.getListData().size() == 0){
            pageBase1.setListData(null);
        }else {
            List<CargoListBO> listBOs = new ArrayList<CargoListBO>();
            for (CargoDTO cargoDTO : pageBase.getListData()){
                CargoListBO cargoListBO = new CargoListBO();
                cargoListBO.setCargoId(cargoDTO.getId());//货物ID
                cargoListBO.setCompanyName(cargoDTO.getCompanyName());
                if (cargoDTO.getCompanySubmitType() != null) {
                    cargoListBO.setCompanyAuthStatus(cargoDTO.getCompanySubmitType());
                } else {
                    cargoListBO.setCompanyAuthStatus((byte)0);
                }
                cargoListBO.setStartAddress(StrUtil.strJoint(cargoDTO.getStartProvince(), cargoDTO.getStartCity(), cargoDTO.getStartCounty()));
                cargoListBO.setEndAddress(StrUtil.strJoint(cargoDTO.getEndProvince(), cargoDTO.getEndCity(), cargoDTO.getEndCounty()));
                cargoListBO.setCargoName(cargoDTO.getCargoName());
                //重量(单位：吨)
                cargoListBO.setWeight(
                        SystemsUtil.buildWeightUnit(cargoDTO.getCargoWeight()));
                //体积(单位：方)
                cargoListBO.setVolume(
                        SystemsUtil.buildVolumeUnit(cargoDTO.getCargoCubage()));
                //要求的车长(单位：米)
                cargoListBO.setCarLength(
                        SystemsUtil.buildCarLenUnit(cargoDTO.getRequestCarLen()));

                //车辆类型名称
                cargoListBO.setVehicleTypeName(SystemData.findVehicleTypeName(cargoDTO.getVehicleType()));

                //车厢类型名称
                cargoListBO.setCarriageTypeName(SystemData.findCarriageTypeName(cargoDTO.getCarriageType()));

                //装货时间(yyyy-mm-dd)
                cargoListBO.setStartTime(SystemsUtil.cargoOrderStartTime(cargoDTO.getRequestStartTime()));
                //卸货时间(yyyy-mm-dd)
                cargoListBO.setEndTime(SystemsUtil.cargoOrderEndTime(cargoDTO.getRequestEndTime()));
                //浏览时间(例如：几小时前)
                cargoListBO.setPubTime(SystemsUtil.getTimeStr(cargoDTO.getCreateTime()));
                //运费总价
                cargoListBO.setTotalFare(SystemsUtil.getTotalFare(cargoDTO.getTotalFare()));
                //预付运费
                cargoListBO.setPrepayFare(SystemsUtil.getTotalFare(cargoDTO.getPrepayFare()));
                listBOs.add(cargoListBO);
            }
            pageBase1.setListData(listBOs);

        }

        return pageBase1;
    }
}
