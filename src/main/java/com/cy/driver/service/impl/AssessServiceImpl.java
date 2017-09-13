package com.cy.driver.service.impl;

import com.cy.cargo.service.CargoAssessService;
import com.cy.cargo.service.dto.CargoAssessInfoListDTO;
import com.cy.cargo.service.dto.DriverCargoAssessInfoDTO;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.throwex.ValidException;
import com.cy.driver.domain.OrderAssess;
import com.cy.driver.domain.PageBase;
import com.cy.driver.domain.PageComm;
import com.cy.driver.service.AssessService;
import com.cy.driver.service.QueryOrderService;
import com.cy.order.service.OrderAssessService;
import com.cy.order.service.dto.CommentCountDTO;
import com.cy.order.service.dto.Enum.AssessType;
import com.cy.order.service.dto.Enum.AssessUserType;
import com.cy.order.service.dto.TransactionInfoDTO;
import com.cy.order.service.dto.assess.AssessSearchParamDTO;
import com.cy.order.service.dto.assess.OrderAssessDTO;
import com.cy.order.service.dto.base.CodeTable;
import com.cy.order.service.dto.base.PageInfo;
import com.cy.order.service.dto.base.PageResult;
import com.cy.order.service.dto.base.Response;
import com.cy.order.service.dto.distribute.DistributeInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 评价
 * Created by wyh on 2016/4/1.
 */
@Service("assessService")
public class AssessServiceImpl implements AssessService {
    private static final Logger LOG = LoggerFactory.getLogger(AssessServiceImpl.class);

    @Resource
    private OrderAssessService orderAssessService;
    @Resource
    private QueryOrderService queryOrderService;
    @Resource
    private CargoAssessService cargoAssessService;

    /**
     * 司机查看普通订单评价
     */
    @Override
    public OrderAssessDTO commonByDriver(Long orderId) {
        List<OrderAssessDTO> list = listByOrder(orderId);
        if (list != null && list.size() > 0) {
            for (OrderAssessDTO item : list) {
                if (item.getOrgUserType().byteValue() == AssessUserType.DRIVER.getValue()) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 保存司机的评价
     */
    @Override
    public Long saveDriverAssess(OrderAssess orderAssess) {
        /** 订单信息 */
        TransactionInfoDTO transactionInfoDTO = queryOrderService.getTransactionInfo(orderAssess.getOrderId());
        if (transactionInfoDTO == null) {
            throw new ValidException(ValidException.QUERY_ERROR, "订单不存在");
        }
        if (transactionInfoDTO.getDriverAssessIdent() != null
                && transactionInfoDTO.getDriverAssessIdent().intValue() == 1) {
            /** 该订单已评价 */
            LOG.error("订单已评价，无需进行评价，orderId={}", orderAssess.getOrderId());
            throw new ValidException(ValidException.LOGIC_ERROR, "订单已评价");
        }
        Byte aimUserType = null;
        Long aimUserId = null;
        String aimUserName = "";
        if (transactionInfoDTO.getTransactionKind() != null
                && transactionInfoDTO.getTransactionKind().byteValue() == Constants.TRANSACTION_KIND_SUBCONTRACTOR) {
            aimUserType = AssessUserType.SUB_CONTRACTOR.getValue();
            DistributeInfoDTO distributeInfoDTO = queryOrderService.getDistributeInfo(transactionInfoDTO.getDistributeId());
            if (distributeInfoDTO == null) {
                throw new ValidException(ValidException.QUERY_ERROR, "分包订单不存在");
            }
            aimUserId = distributeInfoDTO.getSubcontractorUserId();
            aimUserName = distributeInfoDTO.getSubcontractorName();
        } else {
            aimUserType = AssessUserType.COMPANY.getValue();
            aimUserId = transactionInfoDTO.getDeployUserid();
            aimUserName = transactionInfoDTO.getDeployUsername();
        }

        OrderAssessDTO orderAssessDTO = new OrderAssessDTO();
        orderAssessDTO.setTransactionId(orderAssess.getOrderId());
        orderAssessDTO.setOrgUserType(AssessUserType.DRIVER.getValue());
        orderAssessDTO.setOrgUserId(orderAssess.getOrgUserId());
        orderAssessDTO.setOrgUserName(orderAssess.getOrgUserName());
        orderAssessDTO.setAimUserType(aimUserType);
        orderAssessDTO.setAimUserId(aimUserId);
        orderAssessDTO.setAimUserName(aimUserName);
        Integer assessType = buildAssessType(orderAssess.getAssessScore());
        orderAssessDTO.setAssessType(assessType);
        orderAssessDTO.setContent(orderAssess.getContent());
        orderAssessDTO.setNameHidden(orderAssess.getNameHidden());
        orderAssessDTO.setCol1(orderAssess.getCol1());
        return saveAssess(orderAssessDTO);
    }

    /**
     * 统计司机的好中差评价数量
     */
    @Override
    public CommentCountDTO countByDriver(Long driverId) {
        return countByAimUser(AssessUserType.DRIVER.getValue(), driverId);
    }

    /**
     * 统计货主的好中差评价数量
     */
    @Override
    public CommentCountDTO countByOwner(Long ownerUserId) {
        return countByAimUser(AssessUserType.COMPANY.getValue(), ownerUserId);
    }

    /**
     * 查看对司机评价信息列表
     */
    @Override
    public PageBase<OrderAssessDTO> pageListByDriver(PageComm pageComm, Long driverId, Integer assessType) {
        AssessSearchParamDTO assessSearchParamDTO = new AssessSearchParamDTO();
        assessSearchParamDTO.setAssessType(assessType);
        assessSearchParamDTO.setAimUserType(AssessUserType.DRIVER.getValue());
        assessSearchParamDTO.setAimUserId(driverId);
        return pageListByUser(pageComm, assessSearchParamDTO);
    }

    /**
     * 根据订单id查询评价列表信息
     */
    private List<OrderAssessDTO> listByOrder(Long orderId) {
        Response<List<OrderAssessDTO>> result = orderAssessService.listByOrderAndUser(orderId, null, null);
        if (!result.isSuccess()) {
            LOG.error("调用order服务根据订单id查询评价列表信息失败，失败信息={}", result.getMessage());
            throw new ValidException(ValidException.QUERY_ERROR, "查询订单评价失败");
        }
        return result.getData();
    }

    /**
     * 保存单条评价信息
     */
    private Long saveAssess(OrderAssessDTO orderAssessDTO) {
        List<OrderAssessDTO> list = new ArrayList<OrderAssessDTO>();
        list.add(orderAssessDTO);
        return saveAssessList(list).get(0);
    }

    /**
     * 保存多条评价信息
     */
    private List<Long> saveAssessList(List<OrderAssessDTO> list) {
        Response<List<Long>> result = orderAssessService.saveAssess(list);
        if (!result.isSuccess()) {
            LOG.error("调用order服务保存多条评价信息失败，失败信息={}", result.getMessage());
            if (result.getCode() == CodeTable.VALID_CODE.getCode()) {
                /** 该订单已评价 */
                throw new ValidException(ValidException.LOGIC_ERROR, "订单已评价");
            } else {
                throw new ValidException(ValidException.SAVE_ERROR, "订单评价失败");
            }
        }
        if (result.getData() == null || result.getData().size() == 0 || result.getData().size() != list.size()) {
            LOG.error("调用order服务保存多条评价信息失败，失败原因：返回的保存主键id为空");
            throw new ValidException(ValidException.SAVE_ERROR, "订单评价失败");
        }
        return result.getData();
    }

    /**
     * 评价的评分转换成好中差
     */
    private Integer buildAssessType(Integer assessScore) {
        Integer assessType = null;
        if (assessScore != null) {
            switch (assessScore.intValue()) {
                case Constants.ASSESS_SCORE_GOOD:
                    assessType = AssessType.GOOD.getValue();
                    break;
                case Constants.ASSESS_SCORE_MIDDLE:
                    assessType = AssessType.MIDDLE.getValue();
                    break;
                case Constants.ASSESS_SCORE_BAD:
                    assessType = AssessType.BAD.getValue();
                    break;
            }
        }
        return assessType;
    }

    /**
     * 查询用户评价数统计
     */
    private CommentCountDTO countByAimUser(Byte userType, Long userId) {
        Response<CommentCountDTO> result = orderAssessService.countByAimUser(userType, userId);
        if (!result.isSuccess()) {
            LOG.error("调用order服务查询用户评价数统计失败，失败信息={}", result.getMessage());
            throw new ValidException(ValidException.QUERY_ERROR, "查询评价数量失败");
        }
        CommentCountDTO commentCountDTO = result.getData();
        if (commentCountDTO != null) {
            if (commentCountDTO.getBad() == null) {
                commentCountDTO.setBad(0);
            }
            if (commentCountDTO.getGood() == null) {
                commentCountDTO.setGood(0);
            }
            if (commentCountDTO.getMiddle() == null) {
                commentCountDTO.setMiddle(0);
            }
        }
        return commentCountDTO;
    }

    /**
     * 根据用户类型分页查询评价列表
     */
    private PageBase<OrderAssessDTO> pageListByUser(PageComm pageComm, AssessSearchParamDTO assessSearchParamDTO) {
        PageInfo<AssessSearchParamDTO> page = new PageInfo<AssessSearchParamDTO>(pageComm.getPage(), pageComm.getPageSize());
        page.setData(assessSearchParamDTO);
        PageResult<OrderAssessDTO> pageResult = orderAssessService.pageByUser(page);
        if (!pageResult.isSuccess()) {
            LOG.error("调用order服务根据用户类型分页查询评价列表失败，失败信息={}", pageResult.getMessage());
            throw new ValidException(ValidException.QUERY_ERROR, "查询评价列表失败");
        }
        PageBase<OrderAssessDTO> pageBase = new PageBase<OrderAssessDTO>();
        pageBase.setTotalPage(pageResult.getTotalPage());
        pageBase.setTotalNum(pageResult.getTotalRecord());
        pageBase.setListData(pageResult.getDataList());
        return pageBase;
    }

    /**
     * 查看对货主评价信息列表
     */
    @Override
    public PageBase<OrderAssessDTO> pageListByOnwer(PageComm pageComm, Long onwerId, Integer assessType) {
        AssessSearchParamDTO assessSearchParamDTO = new AssessSearchParamDTO();
        assessSearchParamDTO.setAssessType(assessType);
        assessSearchParamDTO.setAimUserType(AssessUserType.COMPANY.getValue());
        assessSearchParamDTO.setAimUserId(onwerId);
        return pageListByUser(pageComm, assessSearchParamDTO);
    }

    /**
     * 保存货源点评信息
     */
    public boolean addComment(DriverCargoAssessInfoDTO driverCargoAssessInfoDTO) {
        com.cy.cargo.service.dto.base.Response<Boolean> addAssess = cargoAssessService.addCargoAssess(driverCargoAssessInfoDTO);
        if (!addAssess.isSuccess()) {
            LOG.error("调用cargo服务保存评价信息失败");
            return false;
        }
        return addAssess.getData();
    }

    /**
     * 查看货源点评信息(分页)
     */
    public CargoAssessInfoListDTO queryCommentInfo(com.cy.cargo.service.dto.base.PageInfo pageInfo, Long cargoId) {
        com.cy.cargo.service.dto.base.Response<CargoAssessInfoListDTO> queryComment = cargoAssessService.getAssessInfo(cargoId, pageInfo);
        if (queryComment.isSuccess()) {
            return queryComment.getData();
        }
        LOG.error("调用cargo服务查询评价信息失败");
        return null;
    }


}
