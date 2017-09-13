package com.cy.driver.service.impl;

import com.cy.cargo.service.QuoteService;
import com.cy.cargo.service.dto.QuoteInfoDTO;
import com.cy.cargo.service.dto.base.CodeTable;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.common.util.SystemsUtil;
import com.cy.driver.domain.QuoteHistoryInfoBO;
import com.cy.driver.service.MyQuoteInfoHandleService;
import com.cy.search.service.SearchCargoQuoteService;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.CargoQuoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 * 报价信息处理
 *
 */
@Service("myQuoteInfoHandleService")
public class MyQuoteInfoHandleServiceImpl implements MyQuoteInfoHandleService {
    private static final Logger LOG = LoggerFactory.getLogger(MyQuoteInfoHandleServiceImpl.class);

    @Resource
    private QuoteService quoteService;
    @Resource
    private SearchCargoQuoteService searchCargoQuoteService;
    @Resource
    private RedisService redisService;

    @Override
    public Response<List<QuoteHistoryInfoBO>> queryQuoteHistory(long cargoId, long driverId) {
        QuoteInfoDTO quoteInfoDTO = new QuoteInfoDTO();
        quoteInfoDTO.setCargoId(cargoId);
        quoteInfoDTO.setDriverId(driverId);
        Response<List<QuoteInfoDTO>> resultData = quoteService.MyQuteInfoHistoryList(quoteInfoDTO);

        List<QuoteHistoryInfoBO> quoteHistoryInfoBOList = new ArrayList<QuoteHistoryInfoBO>();

        if(resultData.isSuccess()){
            for (QuoteInfoDTO quoteInfoDTO1 : resultData.getData()){
                QuoteHistoryInfoBO quoteHistoryInfoBO = new QuoteHistoryInfoBO();
                quoteHistoryInfoBO.setQuoteId(quoteInfoDTO1.getId());
                //我的报价(带单位：元/车、元/吨、元/方)
                String quoteUnit = SystemsUtil.quoteUnitConver(quoteInfoDTO1.getQuoteType());
                String myQuote = quoteInfoDTO1.getQuoteFair() == null || "".equals(quoteInfoDTO1.getQuoteFair()) ? "" : (quoteInfoDTO1.getQuoteFair() + quoteUnit);//我的报价(带单位：元/车、元/吨、元/方)
                quoteHistoryInfoBO.setMyQuote(myQuote);
                quoteHistoryInfoBO.setQuoteTime(DateUtil.dateFormat(quoteInfoDTO1.getCreateTime(), DateUtil.F_DATETIME));
                quoteHistoryInfoBOList.add(quoteHistoryInfoBO);
            }
            return new Response<List<QuoteHistoryInfoBO>>(quoteHistoryInfoBOList);
        }
        return new Response<List<QuoteHistoryInfoBO>>(CodeTable.EXCEPTION);
    }

    /**
     * 我的报价数量
     */
    @Override
    public int myQuoteNum(Long driverId) {
        String quoteDate = redisService.getStr(String.valueOf(driverId));
        Response<Integer> response = quoteService.queryMyQuoteCounts(driverId, quoteDate);
        if (!response.isSuccess()) {
            LOG.error("调用cargo服务查询我的报价数量失败，失败信息={}", response.getMessage());
            return 0;
        }
        if (response.getData() == null)
            return 0;
        return response.getData().intValue();
    }

    /**
     * 根据货源id数组查询最新报价
     */
    @Override
    public List<QuoteInfoDTO> queryLastQuote(List<Long> cargoIds, Long driverId) {
        if (cargoIds == null || cargoIds.size() == 0) {
            LOG.error("根据货源id数组查询最新报价失败,cargoIds为空");
            return null;
        }
        if (driverId == null) {
            LOG.error("根据货源id数组查询最新报价失败,driverId为空");
            return null;
        }
        Response<List<QuoteInfoDTO>> response = quoteService.queryLastQuoteList(driverId, cargoIds);
        if (!response.isSuccess()) {
            LOG.error("调用cargo服务根据货源id数组查询最新报价失败，返回错误信息={}", response.getMessage());
        }
        return response.getData();
    }


    @Override
    public List<CargoQuoteDTO> list(List<IdSourceDTO> cargoIds, String userCode, Integer userType) {
        com.cy.search.service.dto.base.Response<List<CargoQuoteDTO>> response = searchCargoQuoteService.list(cargoIds,userCode, userType);
        if (response == null) {
            LOG.error("调用底层search服务的报价信息失败，响应数据Response为空");
            return null;
        }
        if (!response.isSuccess()) {
            LOG.error("调用底层search服务的报价信息出错，出错信息code={},message={}", response.getCode(), response.getMessage());
            return null;
        }
        return response.getData();
    }
}
