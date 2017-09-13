package com.cy.driver.action.convert;

import com.cy.award.service.dto.AwardTransLogDTO;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.DateUtil;
import com.cy.driver.domain.IntegrationPageBO;
import com.cy.driver.domain.PageBase;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分转换
 * Created by wyh on 2016/4/25.
 */
public class IntegrationConvert {

    /**
     * 积分明细列表信息转换
     */
    public static PageBase<IntegrationPageBO> pageBO(PageBase<AwardTransLogDTO> pageBase) {
        PageBase<IntegrationPageBO> result = new PageBase();
        if (pageBase != null) {
            result.setTotalNum(pageBase.getTotalNum());
            result.setTotalPage(pageBase.getTotalPage());
            if (pageBase.getListData() != null && pageBase.getListData().size() > 0) {
                List<IntegrationPageBO> list = new ArrayList();
                for (AwardTransLogDTO item : pageBase.getListData()) {
                    list.add(integrationPageBO(item));
                }
                result.setListData(list);
            }
        } else {
            result.setTotalNum(0);
            result.setTotalPage(0);
        }
        return result;
    }

    /**
     * 积分明细转换
     */
    public static IntegrationPageBO integrationPageBO(AwardTransLogDTO awardTransLogDTO) {
        IntegrationPageBO integrationPageBO = new IntegrationPageBO();
        integrationPageBO.setIntegrationId(awardTransLogDTO.getId());
        integrationPageBO.setName(awardTransLogDTO.getBusinessValue());
        integrationPageBO.setFundFlow(awardTransLogDTO.getFundFlow());
        integrationPageBO.setAmount(awardTransLogDTO.getAmount());
        integrationPageBO.setCreateTime(DateUtil.dateTimeToStr(awardTransLogDTO.getCreateTime()));
        return integrationPageBO;
    }

    /**
     * 新增积分类型转换成事件编号
     */
    public static String eventCode(Integer integrationType) {
        if (integrationType == null) {
            return null;
        } else {
            switch (integrationType.intValue()) {
                case 1:
                    return LogEnum.BUILD_CARGO_SHARE_CONTENT.getEventCode();
                default:
                    return null;
            }
        }
    }
}
