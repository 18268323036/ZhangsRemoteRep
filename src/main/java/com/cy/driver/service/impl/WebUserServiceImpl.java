package com.cy.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.cy.driver.service.WebUserService;
import com.cy.pass.service.dto.WebUserInfoDTO;
import com.cy.pass.service.dto.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author yanst 2016/4/26 10:01
 */
@Service("webUserService")
public class WebUserServiceImpl implements WebUserService {

    private Logger LOG = LoggerFactory.getLogger(WebUserServiceImpl.class);

    @Resource
    private com.cy.pass.service.WebUserInfoService webUserInfoService;

    @Override
    public List<WebUserInfoDTO> listBycompanyIds(List<Long> companyIds) {

        Response<List<WebUserInfoDTO>> response = webUserInfoService.listByCompanyIds(companyIds);
        if(response == null){
            if(LOG.isErrorEnabled())LOG.error("调用底层接口根据企业id查询企业信息失败，返回对象为空");
        }
        if(!response.isSuccess()){
            if(LOG.isErrorEnabled())LOG.error("调用底层接口根据企业id查询企业信息出错，出错信息response={}", JSON.toJSONString(response));
        }
        return response.getData();
    }
}
