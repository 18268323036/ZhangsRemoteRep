package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/28.
 * 发货单
 */
public class OrderInvoiceBO implements Serializable {
    private static final long serialVersionUID = -4826780678294524960L;

    private Long invoiceId;//	发货单id
    private String imgFileMd5;//	图片文件md5

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getImgFileMd5() {
        return imgFileMd5;
    }

    public void setImgFileMd5(String imgFileMd5) {
        this.imgFileMd5 = imgFileMd5;
    }
}
