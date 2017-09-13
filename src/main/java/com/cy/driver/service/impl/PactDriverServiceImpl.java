package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.domain.PageBase;
import com.cy.driver.service.PactDriverService;
import com.cy.pass.service.DriverPactService;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.base.PageInfo;
import com.cy.pass.service.dto.base.PageResult;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 合同司机
 * Created by wyh on 2015/7/6.
 */
@Service("pactDriverServiceImpl")
public class PactDriverServiceImpl implements PactDriverService {
    private static final Logger LOG = LoggerFactory.getLogger(PactDriverServiceImpl.class);

    @Resource
    private DriverPactService driverPactService;

    @Override
    public List<VipDriverLineInfoDTO> listByPactLineIds(List<Long> pactLineIds) {
        Response<List<VipDriverLineInfoDTO>> response = driverPactService.listByPactLineIds(pactLineIds);
        if(response == null){
            if(LOG.isErrorEnabled())
                LOG.error("调用底层根据合同id集合查询合同路线失败,返回对象不能为空");
            return null;
        }
        if(!response.isSuccess()){
            if(LOG.isErrorEnabled())
                LOG.error("调用底层根据合同id集合查询合同路线出错，错误信息result={}", JSON.toJSONString(response));
            return null;
        }
        return response.getData();
    }

    @Override
    public PageBase<PactDriverInfo3DTO> queryPactInfoList(long driverId, long page, Byte state){
        PageInfo<PactDriverQueryDTO> pageInfo = new PageInfo<PactDriverQueryDTO>(page);
        PactDriverQueryDTO pactDriverQueryDTO = new PactDriverQueryDTO();
        pactDriverQueryDTO.setDriverId(driverId);
        pactDriverQueryDTO.setPactStart(state);
        pageInfo.setData(pactDriverQueryDTO);
        PageResult<PactDriverInfo3DTO> result = driverPactService.queryPactDriverListByPage(pageInfo);
        if(result == null){
            if(LOG.isErrorEnabled())
                LOG.error("调用底层获取查询企业合同车源分页列表失败,返回对象不能为空");
            return null;
        }
        if(!result.isSuccess()){
            if(LOG.isErrorEnabled())
                LOG.error("调用底层获取查询企业合同车源分页列表出错，错误信息result={}", result);
            return null;
        }
        PageBase<PactDriverInfo3DTO> pageBase = new PageBase<PactDriverInfo3DTO>();
        pageBase.setTotalPage(result.getTotalPage());
        pageBase.setTotalNum(result.getTotalRecord());
        pageBase.setListData(result.getDataList());
        return pageBase;
    }

    @Override
    public Response<PactDriverListDTO> queryPactInfoList(long driverId, long page){
        return driverPactService.queryPactInfo(driverId);
    }

    @Override
    public Response<Boolean> updatePactState(long driverId, long pactDriverId, int pactStart){
        return driverPactService.updatePactStartById(driverId, pactDriverId, pactStart);
    }

    @Override
    public Response<PactDriverDetailsDTO> queryPactDriverDetails(long driverId, long pactDriverId){
        return driverPactService.queryPactDriverDetails(driverId, pactDriverId);
    }

    @Override
    public Response<Boolean> updateVipDriverLineState(long driverId, long vipDriverLineId, int start){
        return driverPactService.updateVipDriverLineById(driverId, vipDriverLineId, start);
    }

    @Override
    public PactDriverInfo2DTO getPactDriverInfo(Long pactDriverId) {
        Response<PactDriverInfo2DTO> result = driverPactService.queryPactById(pactDriverId);
        if (result == null) {
            LOG.error("调用pass服务查询合同车辆详情出错");
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用pass服务查询合同车辆详情失败，返回的信息={}", result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用pass服务查询合同车辆详情失败，返回车辆详情为空");
            return null;
        }
        return result.getData();
    }

    @Override
    public VipDriverLineInfoDTO getLineInfo(Long lineId){
        Response<VipDriverLineInfoDTO> result = driverPactService.findByLineId(lineId);
        if (result == null) {
            LOG.error("调用pass服务查询合同车辆路线详情出错");
            return null;
        }
        if (!result.isSuccess()) {
            LOG.error("调用pass服务查询合同车辆路线详情失败，返回的信息={}", result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            LOG.error("调用pass服务查询合同车辆路线详情失败，返回车辆路线详情为空");
            return null;
        }
        return result.getData();
    }
}
