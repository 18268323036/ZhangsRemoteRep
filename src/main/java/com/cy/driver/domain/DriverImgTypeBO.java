package com.cy.driver.domain;

/**
 * @author zhangxy 2016/8/6 15:27
 */
public class DriverImgTypeBO {

    private String effectiveDateE;//证件有效期开始时间
    private String effectiveDateS;//证件有效期结束时间
    private String fileMd5;//证件地址
    private String imgType;//证件类型

    public String getEffectiveDateE() {
        return effectiveDateE;
    }

    public void setEffectiveDateE(String effectiveDateE) {
        this.effectiveDateE = effectiveDateE;
    }

    public String getEffectiveDateS() {
        return effectiveDateS;
    }

    public void setEffectiveDateS(String effectiveDateS) {
        this.effectiveDateS = effectiveDateS;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }
}
