package com.cy.driver.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by mr on 2015/7/3.
 */
@Component
public class EnvConfig {
    /*app货源分享*/
    private String cargoShareSubUrl;

    @Value(value = "${cargo.app.share.url}")
    public void setCargoShareSubUrl(String cargoShareSubUrl) {
        this.cargoShareSubUrl = cargoShareSubUrl;
    }

    public String getCargoShareSubUrl() {
        return cargoShareSubUrl;
    }



}
