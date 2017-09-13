package com.cy.driver.domain;

import java.io.Serializable;

/**
 * app分享
 * Created by wyh on 2015/7/14.
 */
public class AppShareBo implements Serializable {
    private static final long serialVersionUID = -817669145939353496L;

    private String appShareContent;//app分享内容
    private String appShareUrl;//app分享url

    public String getAppShareContent() {
        return appShareContent;
    }

    public void setAppShareContent(String appShareContent) {
        this.appShareContent = appShareContent;
    }

    public String getAppShareUrl() {
        return appShareUrl;
    }

    public void setAppShareUrl(String appShareUrl) {
        this.appShareUrl = appShareUrl;
    }
}
