package com.cy.driver.cloudService.impl;

import com.cy.driver.cloudService.CloudQuoteService;
import com.cy.driver.common.util.DateUtil;
import com.cy.rdcservice.service.CarrierQuoteService;
import com.cy.rdcservice.service.dto.CarrierQuoteDTO;
import com.cy.search.service.SearchCargoQuoteService;
import com.cy.search.service.dto.request.QuoteUserDTO;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author yanst 2016/7/21 17:17
 */
@Service("cloudQuoteService")
public class CloudQuoteServicleImpl implements CloudQuoteService {

    @Resource
    private CarrierQuoteService carrierQuoteService;
    @Resource
    private SearchCargoQuoteService searchCargoQuoteService;

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean saveQuoteInfo(CarrierQuoteDTO carrierQuoteDTO) {
        Response<Long> response = carrierQuoteService.save(carrierQuoteDTO);
        if(response.isSuccess() && response.getData()!=null){
            return true;
        }
        return false;
    }

    @Override
    public long countByUser(Long driverId,Integer userType) {
        QuoteUserDTO quoteUserDTO = new QuoteUserDTO();
        quoteUserDTO.setUserId(driverId.toString());
        quoteUserDTO.setUserType(userType);
        quoteUserDTO.setCreateTimeStart(DateUtil.strToDate(DateUtil.dateToStr(new Date())));//当日0时0分0秒
        com.cy.search.service.dto.base.Response<Long> response = searchCargoQuoteService.countByUser(quoteUserDTO);
        if(response == null){
            LOG.error("获取货源报价数量失败，返回对象为空");
            return 0L;
        }
        if(!response.isSuccess()){
            LOG.error("获取货源报价数量异常，返回信息：{}", response.getMessage());
            return 0L;
        }
        return response.getData();
    }
}
