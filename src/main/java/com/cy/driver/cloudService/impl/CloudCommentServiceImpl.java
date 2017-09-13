package com.cy.driver.cloudService.impl;

import com.cy.driver.cloudService.CloudCommentService;
import com.cy.rdcservice.service.CarrierAssessService;
import com.cy.rdcservice.service.dto.CarrierAssessDTO;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;
import com.cy.top56.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangxy 2016/7/21 9:44
 */
@Service("cloudCommentService")
public class CloudCommentServiceImpl implements CloudCommentService {

    @Resource
    private CarrierAssessService carrierAssessService;

    private Logger LOG = LoggerFactory.getLogger(this.getClass());


    public boolean addComment(CarrierAssessDTO carrierAssessDTO){
        Response<Long> response = carrierAssessService.save(carrierAssessDTO);
        if(response==null || !response.isSuccess()){
            LOG.isDebugEnabled(); LOG.debug("保存评论内容失败，失败原因");
            return false;
        }
        return response.isSuccess();
    }

    public PageResult<CarrierAssessDTO> queryCommentByPage(PageInfo<Long> pageinfo){
        PageResult<CarrierAssessDTO> pageResult = carrierAssessService.pageByCarrierId(pageinfo);
        if(pageResult==null || !pageResult.isSuccess()){
            LOG.isDebugEnabled(); LOG.debug("查询评论内容失败，失败原因");
            return null;
        }
        return pageResult;
    }
}
