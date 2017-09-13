package com.cy.driver.service;

import com.cy.driver.domain.AppShareBo;
import com.cy.pass.service.dto.ShareUserDTO;
import com.cy.pass.service.dto.base.Response;

/**
 * Created by wyh on 2015/7/4.
 */
public interface AppShareService {

    /**
     * 生成app分享内容
     * @param token
     * @param source 系统类型
     * @return
     * @author wyh
     */
    AppShareBo buildAppShareContent(String token, String source);

    /**
     * 保存分享
     * @param recommendMobile 推荐人手机号码
     * @param beRecommendMobile 被推荐人手机号码
     * @return
     * @author wyh
     */
    Response<Boolean> share(String recommendMobile, String beRecommendMobile);

    /**
     * 保存货主分享信息
     * @param recommendedMobileNum
     * @param recommendMobileNum
     * @return
     * @author wyh
     */
    Long saveOwnerShare(String recommendedMobileNum, String recommendMobileNum);

    /**
     * 注册后修改分享状态
     * @param regMobilePhone
     * @return
     * @author wyh
     */
    ShareUserDTO regUpdateStateByDriver(String regMobilePhone);
}
