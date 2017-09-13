package com.cy.driver.common.util;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by wyh on 2015/7/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "urls")
@XmlType(propOrder = { "url" })
public class ShortUrls implements Serializable {
    @XmlElement
    private ShortUrl url;

    public ShortUrl getUrl() {
        return url;
    }

    public void setUrl(ShortUrl url) {
        this.url = url;
    }
}
