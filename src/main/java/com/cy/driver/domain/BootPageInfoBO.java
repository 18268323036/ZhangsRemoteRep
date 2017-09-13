package com.cy.driver.domain;

import java.io.Serializable;

/**
 * @author yanst 2016/4/26 15:10
 */
public class BootPageInfoBO implements Serializable {
    private static final long serialVersionUID = -2686768309285858777L;

    /** 排序号 */
    private Short orderNum;
    /** md5 **/
    private String imgFilemd5;

    /** url */
    private String url;

    /** title */
    private String title;

    public Short getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Short orderNum) {
        this.orderNum = orderNum;
    }

    public String getImgFilemd5() {
        return imgFilemd5;
    }

    public void setImgFilemd5(String imgFilemd5) {
        this.imgFilemd5 = imgFilemd5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
