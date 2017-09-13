package com.cy.driver.service;

import com.cy.cargo.service.dto.CargoAssessInfoListDTO;
import com.cy.cargo.service.dto.DriverCargoAssessInfoDTO;
import com.cy.driver.domain.OrderAssess;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.PageComm;
import com.cy.order.service.dto.CommentCountDTO;
import com.cy.order.service.dto.assess.OrderAssessDTO;

/**
 * 评价
 * Created by wyh on 2016/4/1.
 */
public interface AssessService {

    /**
     * 司机查看普通订单评价
     * @param orderId
     * @return
     * @author wyh
     */
    OrderAssessDTO commonByDriver(Long orderId);

    /**
     * 保存司机的评价
     * @param orderAssess
     * @return
     * @author wyh
     */
    Long saveDriverAssess(OrderAssess orderAssess);

    /**
     * 统计司机的好中差评价数量
     * @param driverId 司机id
     * @return
     * @author wyh
     */
    CommentCountDTO countByDriver(Long driverId);

    /**
     * 统计货主的好中差评价数量
     * @param ownerUserId 货主用户id
     * @return
     * @author wyh
     */
    CommentCountDTO countByOwner(Long ownerUserId);

    /**
     * 查看对司机评价信息列表
     * @param pageComm 分页信息
     * @param driverId 司机id
     * @param assessType 评价类型 1:差评 2:中评 3:好评 null:全部
     * @return
     * @author wyh
     */
    PageBase<OrderAssessDTO> pageListByDriver(PageComm pageComm, Long driverId, Integer assessType);

    /**
     * 查看对货主评价信息列表
     * @param pageComm 分页信息
     * @param onwerId 货主id
     * @param assessType 评价类型 1:差评 2:中评 3:好评 null:全部
     * @return
     */
    PageBase<OrderAssessDTO> pageListByOnwer(PageComm pageComm, Long onwerId, Integer assessType);

    /**
     * 保存货源点评信息
     */
    boolean addComment(DriverCargoAssessInfoDTO driverCargoAssessInfoDTO);

    /**
     * 货源点评信息列表
     * @param pageInfo
     * @param cargoId
     * @return
     */
    CargoAssessInfoListDTO queryCommentInfo(com.cy.cargo.service.dto.base.PageInfo pageInfo, Long cargoId);

}
