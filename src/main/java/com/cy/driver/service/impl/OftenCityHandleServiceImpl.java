package com.cy.driver.service.impl;

import com.cy.driver.service.OftenCityHandleService;
import com.cy.pass.service.DriverOftenCitysService;
import com.cy.pass.service.dto.DriverOftenCityDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 司机常跑车辆处理类
 * Created by mr on 2015/7/6.
 */
@Service("oftenCityHandleService")
public class OftenCityHandleServiceImpl implements OftenCityHandleService {
    private static final Logger LOG = LoggerFactory.getLogger(OftenCityHandleServiceImpl.class);

    @Resource
    private DriverOftenCitysService driverOftenCitysService;

    @Override
    public Response<String> saveOrUpdate(List<String> cityCodeList, String driverId) {
        return driverOftenCitysService.saveOrUpdate(cityCodeList,driverId);
    }

    @Override
    public Response<List<DriverOftenCityDTO>> list(String driverId) {
        return driverOftenCitysService.list(driverId);
    }

    /**
     * 查询司机的常跑城市列表
     */
    @Override
    public List<DriverOftenCityDTO> queryList(Long driverId) {
        if (driverId == null) {
            LOG.error("查询司机的常跑城市列表失败，司机id为空");
            return null;
        }
        Response<List<DriverOftenCityDTO>> response = driverOftenCitysService.list(driverId.toString());
        if (!response.isSuccess()) {
            LOG.error("调用pass服务查询司机的常跑城市列表失败,driverId={},返回信息={}", driverId, response.getMessage());
            return null;
        }
        if (response.getData() == null) {
            LOG.error("调用pass服务查询司机的常跑城市列表失败,driverId={},返回常跑城市列表为空", driverId);
            return null;
        }
        return response.getData();
    }
}
