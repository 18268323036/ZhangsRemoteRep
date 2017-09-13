package com.cy.driver.common.util;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "url")
@XmlType(propOrder = { "urlShort", "urlLong", "type" })
public class ShortUrl implements Serializable {
    @XmlElement(name = "url_short")
    private String urlShort;//接入号
    @XmlElement(name = "url_long")
    private String urlLong;//接入号
    @XmlElement
    private String type;//接入号

    public String getUrlShort() {
        return urlShort;
    }

    public void setUrlShort(String urlShort) {
        this.urlShort = urlShort;
    }

    public String getUrlLong() {
        return urlLong;
    }

    public void setUrlLong(String urlLong) {
        this.urlLong = urlLong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
