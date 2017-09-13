package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.domain.PageBase;
import com.cy.driver.service.CargoBrowseRecordService;
import com.cy.search.service.BuildBrowseCargoIndexService;
import com.cy.search.service.SearchBrowseCargoService;
import com.cy.search.service.dto.base.PageInfo;
import com.cy.search.service.dto.base.PageResult;
import com.cy.search.service.dto.base.Response;
import com.cy.search.service.dto.request.BrowseCargoDTO;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.CargoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 货源浏览记录
 * @author yanst 2016/4/20 16:00
 */
@Service("cargoBrowseRecordService")
public class CargoBrowseRecordServiceImpl implements CargoBrowseRecordService {

    private Logger LOG = LoggerFactory.getLogger(CargoBrowseRecordServiceImpl.class);

    @Resource
    private BuildBrowseCargoIndexService buildBrowseCargoIndexService;
    @Resource
    private SearchBrowseCargoService searchBrowseCargoService;

    /**
     * 添加货源浏览记录
     * @param driverId
     * @param cargoId
     * @param userType
     * @return
     */
    @Override
    public Boolean add(Long driverId, Long cargoId, int userType, Integer cargoSource){
        BrowseCargoDTO browseCargoDTO = new BrowseCargoDTO();
        IdSourceDTO idSourceDTO = new IdSourceDTO();
        if(cargoSource==null){
            cargoSource=1;
        }
        idSourceDTO.setSource(cargoSource);
        idSourceDTO.setId(cargoId);
        browseCargoDTO.setUserId(driverId);
        browseCargoDTO.setIdSourceDTO(idSourceDTO);
        browseCargoDTO.setUserType(userType);
        Response<Boolean> response = buildBrowseCargoIndexService.addIndex(browseCargoDTO);
        if(response == null){
            if(LOG.isDebugEnabled())LOG.debug("添加货源浏览记录失败，返回对象为空");
            return Boolean.FALSE;
        }
        if(response.getData() == null || !response.isSuccess()){
            if(LOG.isDebugEnabled())LOG.debug("添加货源浏览记录失败，失败信息={}", JSON.toJSONString(response));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 浏览记录列表
     * @param driverId
     * @param page
     * @return
     */
    @Override
    public PageBase<CargoDTO> pageList(Long driverId, Integer page){
        PageInfo<Long> pageInfo = new PageInfo<Long>(page);
        pageInfo.setData(driverId);
        PageResult<CargoDTO> response = searchBrowseCargoService.searchByDriver(pageInfo);
        if(response == null){
            if(LOG.isDebugEnabled())LOG.debug("查看浏览记录列表失败，返回对象为空");
            return null;
        }
        if(!response.isSuccess()){
            if(LOG.isDebugEnabled())LOG.debug("查看浏览记录列表失败，失败信息response={}", JSON.toJSONString(response));
            return null;
        }
        PageBase<CargoDTO> pageBase = new PageBase<CargoDTO>();
        pageBase.setListData(response.getDataList());
        pageBase.setTotalNum((int)response.getTotalRecord());
        pageBase.setTotalPage((int)response.getTotalPage());
        return pageBase;
    }

    /**
     * 删除浏览记录
     * @param cargoIds
     * @param driverId
     * @return
     */
    @Override
    public Boolean deletes(List<IdSourceDTO> cargoIds, Long driverId) {
        Response<Boolean> response =  buildBrowseCargoIndexService.deleteByCargoIds(cargoIds, driverId);
        if(response == null){
            if(LOG.isDebugEnabled())LOG.debug("批量删除货源浏览记录失败，返回对象为空");
            return Boolean.FALSE;
        }
        if(response.getData() == null || !response.isSuccess()){
            if(LOG.isDebugEnabled())LOG.debug("批量删除货源浏览记录失败，失败信息={}", JSON.toJSONString(response));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Long count(Long driverId) {
        Response<Long> response = searchBrowseCargoService.countByDriver(driverId);
        if(response == null){
            if(LOG.isDebugEnabled())LOG.debug("获取货源浏览记录数失败，返回对象为空");
            return 0L;
        }
        if(response.getData() == null || !response.isSuccess()){
            if(LOG.isDebugEnabled())LOG.debug("获取货源浏览记录数失败，失败信息={}", JSON.toJSONString(response));
            return 0L;
        }
        return response.getData();
    }

}
