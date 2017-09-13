package com.cy.driver.service;

import com.cy.cargo.service.dto.QuoteInfoDTO;
import com.cy.cargo.service.dto.base.Response;
import com.cy.driver.domain.QuoteHistoryInfoBO;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.CargoQuoteDTO;

import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
public interface MyQuoteInfoHandleService {

    /**
     * 历史报价
     * @param cargoId
     * @param driverId
     * @return
     */
    public Response<List<QuoteHistoryInfoBO>> queryQuoteHistory(long cargoId, long driverId);

    /**
     * 根据货源id数组查询最新报价
     * @param cargoIds
     * @param driverId
     * @return
     * @author wyh
     */
    List<QuoteInfoDTO> queryLastQuote(List<Long> cargoIds, Long driverId);

    /**
     * 我的报价数量
     * @param driverId
     * @return
     * @author 高建伟
     */
    int myQuoteNum(Long driverId);

    /**
     * 根据货源id集合、用户编号和用户类型查询货源报价集合
     * @param cargoIds 货源id集合，必填
     * @param userCode 用户编号（用户id），必填
     * @param userType 用户类型（1快到网司机 2快到网货主 3区域配送货主），必填
     * @return
     * @author yanst
     */
    List<CargoQuoteDTO> list(List<IdSourceDTO> cargoIds, String userCode, Integer userType);

}
