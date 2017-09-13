package com.cy.driver.api.domain.res;

import com.cy.rdcservice.service.dto.WaybillListQueryDTO;

/**
 * Created by nixianjing on 16/8/2.
 */
public class WaybillListQueryRes extends WaybillListQueryDTO {


    /**
     * 创建用户编码
     */
    private Long	createUserId;

    /**
     * 1快到网司机 3区域配送用户
     */
    private Byte	createUserType;

    /**
     * 装货时间起始
     */
    private java.util.Date	needStartTimeBegin;

    /**
     * 装货时间终止
     */
    private java.util.Date	needStartTimeEnd;

    /**
     * 所属主帐号id(如果是主帐号是创建人，则与create_user_id相同)
     */
    private Long	ownUserId;

    /**
     * 模糊搜索项：货物名称、托单号、运单号、发货方
     */
    private String	searchStr;

    /**
     * 平台编码
     */
    private String	siteCode;

    /**
     * 承运方所属主帐号id
     */
    private Long	transportOwnUserId;

    /**
     * 承运方类型（1快到网司机 2快到网货主 3区域配送用户）
     */
    private Byte	transportType;

    /**
     * 承运方用户编码
     */
    private Long	transportUserId;

    /**
     * 运单性质（1业务派单运单、2运输派单运单、4运输揽件运单）
     */
    private Byte	waybillNature;

    /**
     * 1,我接到的运单；2，我派出的运单
     */
    private Byte	waybillType;

    private String startProvince;
    private String startProvinceCode;
    private String startCity;
    private String startCityCode;
    private String startCounty;
    private String startCountyCode;

    private String endProvince;
    private String endProvinceCode;
    private String endCity;
    private String endCityCode;
    private String endCounty;
    private String endCountyCode;



}
