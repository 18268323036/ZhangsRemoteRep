package com.cy.driver.service.impl;

import com.cy.cargo.service.dto.CargoDetailDTO;
import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.domain.FindCargo;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.service.DriverLineTrackService;
import com.cy.driver.service.TrackPointService;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import com.cy.rdcservice.service.dto.WaybillInfoDTO;
import com.cy.saas.business.model.po.WaybillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yanst 2016/7/20 14:42
 */
@Service("TrackPointService")
public class TrackPointServiceImpl implements TrackPointService {
    Logger LOG = LoggerFactory.getLogger("TrackPointServiceImpl");

    @Resource
    private DriverLineTrackService driverLineTrackService;

    public void saveLine(Long driverId, FindCargo findCargo) {
        //保存司机填入的搜货信息作为线路匹配的依据
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, findCargo, Constants.QUERY_CARGO);
        if (!saveLineRes.isSuccess()) {
            LOG.debug("locationLineService服务调用失败");
        }
        //如果司机要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
        String requestTime = findCargo.getsTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date datefm = null;
        try {
            datefm = sdf.parse(requestTime);
        } catch (ParseException e) {
            LOG.error("locationLineService服务调用失败");
        }
        String requestTimefm = DateUtil.dateToStr(datefm);
        String today = DateUtil.dateToStr(new Date());
        if (requestTimefm.equals(today)) {
            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, findCargo, Constants.QUERY_CARGO);
            if (!saveDayLineRes.isSuccess()) {
                LOG.debug("driverDayLineService服务调用失败");
            }
        }
    }

    public void saveLine(Long driverId, CargoDetailDTO cargoDetailDTO) {
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, cargoDetailDTO, Constants.CARGO_DETAIL);
        if (!saveLineRes.isSuccess()) {
            LOG.debug("locationLineService服务调用失败");
        }
        //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
        String requestTime = DateUtil.dateToStr(cargoDetailDTO.getRequestStartTime());
        String today = DateUtil.dateToStr(new Date());
        if (requestTime.equals(today)) {
            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, cargoDetailDTO, Constants.CARGO_DETAIL);
            if (!saveDayLineRes.isSuccess()) {
                LOG.debug("driverDayLineService服务调用失败");
            }
        }
    }

    public void saveLine(Long driverId, CarrierAssemDTO carrierAssemDTO, int type) {
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, carrierAssemDTO, type);
        if (!saveLineRes.isSuccess()) {
            LOG.debug("locationLineService服务调用失败");
        }
        //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
        String requestTime = DateUtil.dateToStr(carrierAssemDTO.getInfoDTO().getNeedEndTime());
        String today = DateUtil.dateToStr(new Date());
        if (requestTime.equals(today)) {
            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, carrierAssemDTO, type);
            if (!saveDayLineRes.isSuccess()) {
                LOG.debug("driverDayLineService服务调用失败");
            }
        }
    }

    /**
     * 保存报价(快到网)
     * @param driverId
     * @param cargoInfoDTO
     */
    public void saveLine(Long driverId, CargoInfoDTO cargoInfoDTO) {
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, cargoInfoDTO, Constants.QUOTE_CARGO);
        if (!saveLineRes.isSuccess()) {
            LOG.debug("locationLineService服务调用失败");
        }
        //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
        String requestTime = DateUtil.dateToStr(cargoInfoDTO.getRequestStartTime());
        String today = DateUtil.dateToStr(new Date());
        if (requestTime.equals(today)) {
            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, cargoInfoDTO, Constants.QUOTE_CARGO);
            if (!saveDayLineRes.isSuccess()) {
                LOG.debug("driverDayLineService服务调用失败");
            }
        }
    }


    /**
     * 保存报价(运单状态变更)
     * @param driverId
     * @param waybillInfoDTO
     */
    public void saveLine(Long driverId, WaybillInfoDTO waybillInfoDTO) {
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, waybillInfoDTO, Constants.ASSEPT_FOR_CARRIAGE);
        if (!saveLineRes.isSuccess()) {
            LOG.debug("locationLineService服务调用失败");
        }
        //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
        String requestTime = DateUtil.dateToStr(waybillInfoDTO.getNeedStartTime());
        String today = DateUtil.dateToStr(new Date());
        if (requestTime.equals(today)) {
            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, waybillInfoDTO, Constants.ASSEPT_FOR_CARRIAGE);
            if (!saveDayLineRes.isSuccess()) {
                LOG.debug("driverDayLineService服务调用失败");
            }
        }
    }

    @Override
    public void saSaveLine(Long driverId, WaybillInfo waybillInfo) {
        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saSaveLine(driverId, waybillInfo, Constants.ASSEPT_FOR_CARRIAGE);
        if (!saveLineRes.isSuccess()) {
            LOG.debug("locationLineService服务调用失败");
        }
        //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
        String requestTime = DateUtil.dateToStr(waybillInfo.getNeedStartTime());
        String today = DateUtil.dateToStr(new Date());
        if (requestTime.equals(today)) {
            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saSaveDayLine(driverId, waybillInfo, Constants.ASSEPT_FOR_CARRIAGE);
            if (!saveDayLineRes.isSuccess()) {
                LOG.debug("driverDayLineService服务调用失败");
            }
        }
    }
}
