package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/28.
 * 回单
 */
public class OrderReceiptBO implements Serializable {
    private static final long serialVersionUID = 7266151835416149890L;

    private Long receiptId;//	发货单id

    private String  imgFileMd5;//	图片文件md5

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getImgFileMd5() {
        return imgFileMd5;
    }

    public void setImgFileMd5(String imgFileMd5) {
        this.imgFileMd5 = imgFileMd5;
    }
}
