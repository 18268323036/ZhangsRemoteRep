package com.cy.driver.common.constants;

import java.io.Serializable;

/**
 * 推送详情
 * Created by wyh on 2015/12/9.
 */
public class PushDetails implements Serializable {
    private static final long serialVersionUID = 1352582236919484140L;

    /** 标题 */
    private String pushTitle;

    /** 内容 */
    private String pushContent;

    /** 跳转方式 */
    private Integer jumpType;

    /** 推送性质：1 通知 2 交易 */
    private Byte pushKind;

    /** 用途 */
    private Integer useFor;

    /** 跳转地址 */
    private String url;

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public Integer getJumpType() {
        return jumpType;
    }

    public void setJumpType(Integer jumpType) {
        this.jumpType = jumpType;
    }

    public Byte getPushKind() {
        return pushKind;
    }

    public void setPushKind(Byte pushKind) {
        this.pushKind = pushKind;
    }

    public Integer getUseFor() {
        return useFor;
    }

    public void setUseFor(Integer useFor) {
        this.useFor = useFor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
