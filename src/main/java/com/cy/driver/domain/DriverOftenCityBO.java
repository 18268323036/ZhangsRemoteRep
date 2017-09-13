package com.cy.driver.domain;

/**
 * 司机常跑城市转换类
 * Created by mr on 2015/7/6.
 */
public class DriverOftenCityBO {
    /** city_code - 常跑城市编号 */
    private String cityCode;

    /** city_value - 常跑城市 */
    private String cityValue;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityValue() {
        return cityValue;
    }

    public void setCityValue(String cityValue) {
        this.cityValue = cityValue;
    }
}
