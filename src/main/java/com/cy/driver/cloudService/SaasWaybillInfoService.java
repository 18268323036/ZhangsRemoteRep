package com.cy.driver.cloudService;

import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import com.cy.saas.basic.model.dto.SystemProtocolDTO;
import com.cy.saas.business.model.dto.*;
import com.cy.saas.business.model.po.WaybillInfo;
import com.cy.top56.common.Response;

import java.util.List;

/**
 * saas运单service
 * Created by nixianjing on 17/5/31.
 */
public interface SaasWaybillInfoService {

    /**
     * 根据运单id获得运单和运单生成的托单详情
     * @param waybillId 运单ID，必填
     * @return {@link Response.CodeTable#ERROR} 参数错误<br/>
     *         {@link Response.CodeTable#EXCEPTION} 系统异常<br/>
     */
    WaybillDetailsDTO getWaybillDetails(Long waybillId);


    /**
     * 根据父级运单id查询运单信息
     * @param parentWaybillId 父级运单id，必填
     * @return {@link Response.CodeTable#ERROR} 参数错误<br/>
     *         {@link Response.CodeTable#EXCEPTION} 系统异常<br/>
     */
    WaybillInfo getWaybillByParentWaybillId(Long parentWaybillId);

    /**
     * 根据来源运单id查询待签收托单数量
     * @param sourceWaybillIds
     * @return
     */
    List<Integer> countWaitSignBySourceWaybillIds(List<Long> sourceWaybillIds);

    /**
     * 根据账户用户id获得帐户用户详情二
     * @param userId 账户用户id，必填
     * @return {@link Response.CodeTable#ERROR} 参数错误<br/>
     *         {@link Response.CodeTable#EXCEPTION} 系统异常<br/>
     */
    AccountUserDetails2DTO getAccountUserDetails2(Long userId);

    /**
     * 根据用户id获得用户业务统计信息
     * @param userId 用户id，必填
     * @return
     */
    UserItemStatDTO getByUserId(Long userId);

    /**
     * 修改运单状态第二版
     * @param waybillStateModifyDTO 运单状态修改入参
     * @param protocol2DTO 运单电子合同协议信息入参
     * @return Response.CodeTable.ERROR 参数错误<br/>
     *         Response.CodeTable.DATA_NONE_EXIST 运单不存在<br/>
     *         Response.CodeTable.NONE_PERMISSION 运单不能进行修改<br/>
     *         Response.CodeTable.EXCEPTION 操作失败<br/>
     *         Response.CodeTable.UPDATE_FAIL1 扣除信息费的帐户余额不足<br/>
     * @author wyh
     */
    boolean modifyWaybillStateNew(WaybillStateModifyDTO waybillStateModifyDTO, Protocol2DTO protocol2DTO);

    /**
     * 云配送用户保存跟踪记录
     * @param waybillId
     * @param trackingSaveDTO
     * @param modifyUserDTO
     * @return
     */
    boolean saveTrackingByYPS(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO);

    /**
     * 司机用户保存运单跟踪记录
     * @param waybillId 运单id，必填
     * @param trackingSaveDTO trackingSaveDTO.operateType 操作类型（1接单、3装货、4卸货），必填<br/>
     *                        trackingSaveDTO.province 省，必填<br/>
     *                        trackingSaveDTO.city 市，必填<br/>
     *                        trackingSaveDTO.longitude 经度，必填<br/>
     *                        trackingSaveDTO.latitude 纬度，必填<br/>
     * @param modifyUserDTO 修改用户，必填
     * @return
     */
    boolean saveWaybillTrackingByDriver(Long waybillId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO);

    /**
     * 司机用户保存托单跟踪记录
     * @param carrierId 托单id，必填
     * @param trackingSaveDTO trackingSaveDTO.operateType 操作类型（6签收），必填<br/>
     *                        trackingSaveDTO.province 省，必填<br/>
     *                        trackingSaveDTO.city 市，必填<br/>
     *                        trackingSaveDTO.longitude 经度，必填<br/>
     *                        trackingSaveDTO.latitude 纬度，必填<br/>
     * @param modifyUserDTO 修改用户，必填
     * @return
     */
    boolean saveCarrierTrackingByDriver(Long carrierId, TrackingSaveDTO trackingSaveDTO, ModifyUserDTO modifyUserDTO);

    /**
     * 获取平台协议
     * @param siteCode 平台编码
     * @return
     */
    SystemProtocolDTO getSiteProtocol(String siteCode);

    /**
     * 查询托单详细
     * @param carrierId
     * @return
     */
    @Deprecated
    CarrierAssemDTO findAssem(Long carrierId);

    /**
     * 签收(托单)
     * @param carrierId 托单id，必填
     * @param signNumber 签收码，必填
     * @param modifyUserDTO 修改用户信息，必填
     * @return Response.CodeTable.ERROR 参数错误
     *         Response.CodeTable.DATA_NONE_EXIST 签收码不存在
     *         Response.CodeTable.NONE_PERMISSION 已签收或者已下架
     *         Response.CodeTable.VALID_ERROR 验证码不正确
     *         Response.CodeTable.EXCEPTION 操作失败
     */
    Response<Boolean> carrierSign(Long carrierId, String signNumber, ModifyUserDTO modifyUserDTO);


}
