package com.cy.driver.service;

import com.cy.cargo.service.dto.MyQuoteInfoDTO;
import com.cy.cargo.service.dto.base.PageResult;
import com.cy.driver.domain.HomeOtherCountsBO;
import com.cy.pass.service.dto.ImgDeployInfoDTO;
import com.cy.pass.service.dto.base.Response;

import java.util.List;

/**
 * Created by Administrator on 2015/7/12.
 */
public interface HomeNumInfoService {

    /**
     * 我的报价列表
     * @param page
     * @param driverId
     * @return
     */
    public PageResult<MyQuoteInfoDTO> queryMyQuoteList(long page, String driverId);

    /**
     * 货源主页其它数量
     * @param driverId
     * @return
     */
    public Response<HomeOtherCountsBO> queryIndexNums(long driverId);

    /**
     * 查看系统部署图片列表
     * @param useFor 100 APP客户端登录图片  101 APP客户端首页轮播图片 102 APP客户端引导页图片  201 web首页轮播广告位,
     * @param standrand 图片规格（0: 480x320   1:480x800   2:720x1280 ）
     * @return
     */
    public Response<List<ImgDeployInfoDTO>> listByUserAndStandrand(Short useFor, String standrand);
}
