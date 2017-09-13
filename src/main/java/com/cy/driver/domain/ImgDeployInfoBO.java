package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by mr on 2015/7/14.
 */
public class ImgDeployInfoBO implements Serializable{
    private static final long serialVersionUID = 3120318314645642539L;

    /** order_num - 排序号 */
    private Short pictureOrder;

    /** img_name - 图片文件名 */
    private String imgName;

    /** img_path - 图片文件路径 */
    private String imgPath;

    /** img_filemd5 - 图片文件MD5 */
    private String pictureMd5;

    /** img_standard - 图片规格（0: 480x320   1:480x800   2:720x1280 ） */
    private String imgStandard;

    /** 点击结果 */
    private String url;

    /** 浏览器类型（1 内部浏览器 2外部浏览器） */
    private String browserType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Short getPictureOrder() {
        return pictureOrder;
    }

    public void setPictureOrder(Short pictureOrder) {
        this.pictureOrder = pictureOrder;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getPictureMd5() {
        return pictureMd5;
    }

    public void setPictureMd5(String pictureMd5) {
        this.pictureMd5 = pictureMd5;
    }

    public String getImgStandard() {
        return imgStandard;
    }

    public void setImgStandard(String imgStandard) {
        this.imgStandard = imgStandard;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }
}
