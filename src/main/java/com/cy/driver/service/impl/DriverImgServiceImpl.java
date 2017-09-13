package com.cy.driver.service.impl;

import com.cy.pass.service.DriverImgService;
import com.cy.pass.service.dto.DriverImgDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("driverImgService")
public class DriverImgServiceImpl implements com.cy.driver.service.DriverImgService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DriverImgService rpcDriverImgService;

    /**
     * 修改司机图片信息
     *
     * @param token
     * @param fileMd5
     * @param driverImgType
     * @return
     */
    @Override
    public Response<Boolean> uploadDriverImg(String token, String fileMd5, Byte driverImgType) {
        return rpcDriverImgService.uploadDriverImg(token, fileMd5, driverImgType);
    }

    @Override
    public String findByType(Long driverId, Byte type) {
        Response<String> response = rpcDriverImgService.findByType(driverId, type);
        if (response == null) {
            LOG.debug("调用获取图片信息，返回对象为空,driverId={},type={}", driverId, type);
            return null;
        }
        if (StringUtils.isEmpty(response.getData())) {
            LOG.debug("调用获取图片信息，返回数据为空,driverId={},type={}", driverId, type);
            return null;
        }
        return response.getData();
    }

    @Override
    public List<DriverImgDTO> queryImgInfo(Long driverId, List<Byte> imgTypeList) {
        Response<List<DriverImgDTO>> response = rpcDriverImgService.list(driverId,imgTypeList);
        if(response==null){
            LOG.isDebugEnabled(); LOG.debug("查询图片认证信息失败");
        }
        if(!response.isSuccess()){
            LOG.isDebugEnabled(); LOG.debug("查询图片认证信息失败，失败原因{}",response.getMessage());
        }
        return response.getData();
    }


}
