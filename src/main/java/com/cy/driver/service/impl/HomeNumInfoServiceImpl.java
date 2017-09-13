package com.cy.driver.service.impl;

import com.cy.cargo.service.QuoteService;
import com.cy.cargo.service.dto.MyQuoteInfoDTO;
import com.cy.cargo.service.dto.base.PageInfo;
import com.cy.cargo.service.dto.base.PageResult;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.HomeOtherCountsBO;
import com.cy.driver.service.HomeNumInfoService;
import com.cy.driver.service.MyQuoteInfoHandleService;
import com.cy.order.service.OrderService;
import com.cy.order.service.dto.Enum.OrderOuterState;
import com.cy.pass.service.ImgDeployInfoService;
import com.cy.pass.service.dto.ImgDeployInfoDTO;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/7/12.
 */
@Service("homeNumInfoService")
public class HomeNumInfoServiceImpl implements HomeNumInfoService {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OrderService orderService;

    @Resource
    private ImgDeployInfoService imgDeployInfoService;

    @Resource
    private QuoteService quoteService;

    @Resource
    private RedisService redisService;

    @Resource
    private MyQuoteInfoHandleService myQuoteInfoHandleService;


    @Override
    public PageResult<MyQuoteInfoDTO> queryMyQuoteList(long page, String driverId) {
        //放入缓存中
        if(page == 1){
            redisService.setStr(driverId, DateUtil.dateFormat(new Date(), DateUtil.F_DATETIME));
        }
        PageInfo<MyQuoteInfoDTO> myQuoteInfoDTOPageInfo = new PageInfo<MyQuoteInfoDTO>(page, Constants.MY_QUOTE_LIST);
        MyQuoteInfoDTO myQuoteInfoDTO = new MyQuoteInfoDTO();
        myQuoteInfoDTO.setDriverId(Long.valueOf(driverId));
        myQuoteInfoDTOPageInfo.setData(myQuoteInfoDTO);
        return quoteService.MyQuteInfoList(myQuoteInfoDTOPageInfo);
    }

    @Override
    public Response<HomeOtherCountsBO> queryIndexNums(long driverId) {
        try{
            HomeOtherCountsBO homeOtherCountsBO = new HomeOtherCountsBO();
            /**
             * 修改时间：2016-04-23 17:00
             * 修改人：王远航
             * 修改依据：方法迁移
             */
            homeOtherCountsBO.setQuoteNums(myQuoteInfoHandleService.myQuoteNum(driverId));

            String orderDateCarriage = redisService.getStr(String.valueOf(driverId));
            com.cy.order.service.dto.base.Response<Integer> resultwaitOrderNums = orderService.countOrders(driverId, OrderOuterState.WAIT_TRANSPORT.getValue(),orderDateCarriage!=null? DateUtil.parseDate(orderDateCarriage+" 00:00:00", DateUtil.F_DATETIME):null );
            //待承运订单数量
            if(resultwaitOrderNums.isSuccess()){
                homeOtherCountsBO.setWaitOrderNums(resultwaitOrderNums.getData());
            }

            String orderDateCarried = redisService.getStr(String.valueOf(driverId));
            com.cy.order.service.dto.base.Response<Integer> resultOrderNums = orderService.countOrders(driverId,  OrderOuterState.TRANSPORT_ING.getValue(), orderDateCarried!=null? DateUtil.parseDate(orderDateCarried+" 00:00:00", DateUtil.F_DATETIME):null);
            //承运订单数量
            if(resultOrderNums.isSuccess()){
                homeOtherCountsBO.setOrderNums(resultOrderNums.getData());
            }
            return new Response<HomeOtherCountsBO>(homeOtherCountsBO);
        }catch(Exception e){
            LOG.isErrorEnabled();LOG.error("首页其它数量出错。", e);
        }
        return new Response<HomeOtherCountsBO>(CodeTable.EXCEPTION);
    }

    @Override
    public Response<List<ImgDeployInfoDTO>> listByUserAndStandrand(Short useFor, String standrand) {
        return imgDeployInfoService.listByUserAndStandrand(useFor, standrand);
    }

}
