package com.cy.driver.cloudService;

import com.cy.rdcservice.service.dto.CarrierAssessDTO;
import com.cy.top56.common.PageInfo;
import com.cy.top56.common.PageResult;


/**
 * @author zhangxy 2016/7/21
 */
public interface CloudCommentService {

    boolean addComment(CarrierAssessDTO carrierAssessDTO);

    PageResult<CarrierAssessDTO> queryCommentByPage(PageInfo<Long> pageinfo);
}
