package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.DriverOftenCityBO;
import com.cy.driver.service.OftenCityHandleService;
import com.cy.driver.service.PointFService;
import com.cy.pass.service.dto.DriverOftenCityDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mr on 2015/7/6.
 */
@Scope("prototype")
@Controller("driverOftenCityAction")
public class DriverOftenCityAction extends BaseAction{
    private Logger LOG = LoggerFactory.getLogger(DriverOftenCityAction.class);
    @Resource
    private OftenCityHandleService oftenCityHandleService;
    @Resource
    private PointFService pointFService;

    @RequestMapping(value = "/addOftenRunCitys", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_OFTEN_CITY_ADD)
    @Log(type = LogEnum.ADD_OFTEN_RUN_CITYS)
    public Object addOftenRunCitys(HttpServletRequest request, HttpServletResponse response, String oftenRunCitys) {
    	Map<Object, Object> resultMap = new HashMap<Object, Object>();
        //去掉结尾,
        if(oftenRunCitys.endsWith(",")){
            oftenRunCitys = oftenRunCitys.substring(0,oftenRunCitys.length()-1);
        }
        List<String> newCityCodeList = null;
		if (StringUtils.isNotBlank(oftenRunCitys) ) {
			// 去重
			String[] cityCodeList = oftenRunCitys.split(",");
			newCityCodeList = new ArrayList<String>();
			for (String code : cityCodeList) {
				if (!newCityCodeList.contains(code)) {
					newCityCodeList.add(code);
				}
			}
			if (cityCodeList.length > 6) {
				if (LOG.isErrorEnabled())
					LOG.error("添加常跑城市信息校验：长跑城市超过6个。");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20065);
			}
		}
       
        String userId = findUserIdStr(request);
        Response<String> serResponse =  oftenCityHandleService.saveOrUpdate(newCityCodeList, userId);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(CodeTable.ERROR.getCode() == serResponse.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("添加常跑城市信息（服务端）校验：server.message="+serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("添加常跑城市信息（服务端）校验：server.message="+serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }

        //3.5新增埋点  保存常跑城市




        //常跑城市首次填写积分奖励-3.4版本
        pointFService.pointReward(Constants.AWARD_DRIVER, findUserId(request), Constants.CHECK_MODE_BY_EVENT,
                LogEnum.ADD_OFTEN_RUN_CITYS.getEventCode(), null, null, convert2InSource(),null);
        updRespHeadSuccess(response);
        return resultMap;
    }

    @RequestMapping(value = "/oftenRunCitysList", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_OFTEN_CITY_LIST)
    @Log(type = LogEnum.OFTEN_RUN_CITYS_LIST)
    public Object oftenRunCitysList(HttpServletRequest request, HttpServletResponse response) {

        Response<List<DriverOftenCityDTO>> serResponse = oftenCityHandleService.list(findUserIdStr(request));
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(CodeTable.ERROR.getCode() == serResponse.getCode()){
                if (LOG.isErrorEnabled()) LOG.error("查询常跑城市列表（服务端）校验：server.message="+serResponse.getMessage());
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            if (LOG.isErrorEnabled()) LOG.error("查询常跑城市列表（服务端）校验：server.message="+serResponse.getMessage());
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }

        List<DriverOftenCityBO> cityBOList = new ArrayList<DriverOftenCityBO>();

        List<DriverOftenCityDTO> cityList = serResponse.getData();
        if(cityList != null && !cityList.isEmpty()){
            for(DriverOftenCityDTO city : cityList){
                DriverOftenCityBO cityBO = new DriverOftenCityBO();
                cityBO.setCityCode(city.getCityCode());
                cityBO.setCityValue(city.getCityValue());

                cityBOList.add(cityBO);
            }
        }
        updRespHeadSuccess(response);
        Map<String, Object> resultMap = new HashMap<String ,Object>();
        resultMap.put("cityBOList", cityBOList);
        return resultMap;
    }

}
