package com.cy.driver.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/13.
 */
public class TransportProtocolBO implements Serializable {
    private static final long serialVersionUID = -6490966605827790474L;

    private int code ;

    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
