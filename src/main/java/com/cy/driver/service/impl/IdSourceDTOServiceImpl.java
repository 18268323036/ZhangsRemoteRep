package com.cy.driver.service.impl;

import com.cy.driver.service.IdSourceDTOService;
import com.cy.search.service.BuildOrderIndexService;
import com.cy.search.service.dto.base.Response;
import com.cy.search.service.dto.request.IdSourceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by nixianjing on 16/8/8.
 */
@Service("idSourceDTOService")
public class IdSourceDTOServiceImpl implements IdSourceDTOService {

    private static final Logger LOG = LoggerFactory.getLogger(IdSourceDTOServiceImpl.class);

    @Resource
    private BuildOrderIndexService buildOrderIndexService;


    @Override
    public boolean updateIndex(String orderId, Integer source) {
        IdSourceDTO idSourceDTO = new IdSourceDTO();
        idSourceDTO.setId(Long.valueOf(orderId));
        idSourceDTO.setSource(source);
        Response<Boolean> response = buildOrderIndexService.updateIndex(idSourceDTO);
        if(response == null || response.getCode() != 0) {
            LOG.error("调用底层同步运单信息异常,返回对象不能为空");
        }
        return response.getData();
    }
}
