package com.cy.driver.action;

import com.cy.cargo.service.dto.CargoInfoDTO;
import com.cy.driver.cloudService.CloudCargoService;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.redis.NetWordPhoneClient;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.service.CargoHandlerService;
import com.cy.driver.service.DriverLineTrackService;
import com.cy.location.service.DriverLinePointService;
import com.cy.location.service.dto.ViewCargoLineSaveDTO;
import com.cy.rdcservice.service.dto.CarrierAssemDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/8.
 *
 * 网络电话处理
 */
@Scope("prototype")
@RestController("netWorkPhoneHandlerAction")
public class NetWorkPhoneHandlerAction extends BaseAction{

    private Logger LOG = LoggerFactory.getLogger(NetWorkPhoneHandlerAction.class);

    @Resource
    private NetWordPhoneClient netWordPhoneClient;
    @Resource
    private CargoHandlerService cargoHandleService;
    @Resource
    private DriverLineTrackService driverLineTrackService;
    @Resource
    private CloudCargoService cloudCargoService;
    @Resource
    private DriverLinePointService driverLinePointService;


    /**
     * 限制拨打网络电话
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/dialNetworkPhone", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.DIAL_NETWORK_PHONE)
    @Log(type = LogEnum.DIAL_NETWORK_PHONE)
    public Object dialNetworkPhone(HttpServletRequest request, HttpServletResponse response, String authstate , String cargoId, String source) {
        try {
            if(StringUtils.isEmpty(authstate) || StringUtils.isBlank(cargoId)){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            Long driverId = findUserId(request);
            Map<String, Object> result = new HashMap();
            if (!"3".equals(authstate)) {//未认证用户
                netWordPhoneClient.putNumberLimit(findUserIdStr(request), cargoId);
                result.put("code", netWordPhoneClient.haveCallNumber(findUserIdStr(request), cargoId, new Byte("0")));
            } else {
                result.put("code", "0");
            }

            if(source.equals("1")) {
                //查询货源详情，单独保存货源详情信息中的线路信息
                com.cy.cargo.service.dto.base.Response<CargoInfoDTO> getCargoInfoRes = cargoHandleService.getCargoInfo(Long.parseLong(cargoId));
                if (getCargoInfoRes.isSuccess()) {
                    CargoInfoDTO cargoInfoDTO = getCargoInfoRes.getData();
                    if (cargoInfoDTO != null) {
                        //返回货源详情前埋点记录司机选择货源的起始地和目的地
                        com.cy.location.service.dto.base.Response<Boolean> saveLineRes = driverLineTrackService.saveLine(driverId, cargoInfoDTO, Constants.CALL_OWNER);
                        if (!saveLineRes.isSuccess()) {
                            LOG.debug("locationLineService服务调用失败");
                        }
                        //如果货源要求的装货日是今天，则还需要调用pass服务来保存起始地和目的地
                        String requestTime = DateUtil.dateToStr(cargoInfoDTO.getRequestStartTime());
                        String today = DateUtil.dateToStr(new Date());
                        if (requestTime.equals(today)) {
                            com.cy.pass.service.dto.base.Response<Boolean> saveDayLineRes = driverLineTrackService.saveDayLine(driverId, cargoInfoDTO, Constants.CALL_OWNER);
                            if (!saveDayLineRes.isSuccess()) {
                                LOG.debug("driverDayLineService服务调用失败");
                            }
                        }
                        //3.5保存司机路线(查看货源详情)(新)
                        ViewCargoLineSaveDTO viewCargoLineSaveDTO = new ViewCargoLineSaveDTO();
                        viewCargoLineSaveDTO.setDriverId(driverId);
                        viewCargoLineSaveDTO.setCargoId(Long.valueOf(cargoId));
                        viewCargoLineSaveDTO.setViewType(3);//联系货主时埋点类型
                        viewCargoLineSaveDTO.setCargoSource(1);//来源快到网
                        viewCargoLineSaveDTO.setStartTime(cargoInfoDTO.getRequestStartTime());
                        viewCargoLineSaveDTO.setStartProvince(cargoInfoDTO.getStartProvince());
                        viewCargoLineSaveDTO.setStartCity(cargoInfoDTO.getStartCity());
                        viewCargoLineSaveDTO.setEndProvince(cargoInfoDTO.getEndProvince());
                        viewCargoLineSaveDTO.setEndCity(cargoInfoDTO.getEndCity());
                        driverLinePointService.saveViewCargoLine(viewCargoLineSaveDTO);

                    }
                }

            }else{
                CarrierAssemDTO carrierAssemDTO = cloudCargoService.cargoDetailsRdc(Long.valueOf(cargoId));
                //3.5保存司机路线（报价）（新）
                if(carrierAssemDTO!=null){
                    ViewCargoLineSaveDTO viewCargoLineSaveDTO = new ViewCargoLineSaveDTO();
                    viewCargoLineSaveDTO.setDriverId(driverId);
                    viewCargoLineSaveDTO.setCargoId(Long.valueOf(cargoId));
                    viewCargoLineSaveDTO.setViewType(3);//联系货主时埋点类型
                    viewCargoLineSaveDTO.setCargoSource(2);//来源云配
                    viewCargoLineSaveDTO.setStartTime(carrierAssemDTO.getInfoDTO().getNeedStartTime());
                    viewCargoLineSaveDTO.setStartProvince(carrierAssemDTO.getAddressDTO().getDepartureProvinceValue());
                    viewCargoLineSaveDTO.setStartCity(carrierAssemDTO.getAddressDTO().getDepartureCityValue());
                    viewCargoLineSaveDTO.setEndProvince(carrierAssemDTO.getAddressDTO().getReceiveProvinceValue());
                    viewCargoLineSaveDTO.setEndCity(carrierAssemDTO.getAddressDTO().getReceiveCityValue());
                    driverLinePointService.saveViewCargoLine(viewCargoLineSaveDTO);
                }
            }
            updRespHeadSuccess(response);
            return result;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("拨打网络电话出错。", e);
            }
    }
        updRespHeadError(response);
        return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
    }

    /**
     * 订单拨打电话
     * @param response
     * @return
     */
    @RequestMapping(value = "/orderPhone", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ORDER_PHONE)
    @Log(type = LogEnum.ORDER_PHONE)
    public Object orderPhone(HttpServletResponse response){
        updRespHeadSuccess(response);
        return null;
    }
}
