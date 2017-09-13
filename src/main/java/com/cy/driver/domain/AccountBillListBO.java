package com.cy.driver.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 * 账户余额
 */
public class AccountBillListBO implements Serializable {

    private static final long serialVersionUID = 7805450258958777604L;

    private List<AccountBillBO> accountBillBOList;//	账单种类名称（提现和收款）  见：列表明细

    private int allPage;//	总页数

    private int allNUms;//	总条数

    private String mainBmikece;//	账户余额

    public List<AccountBillBO> getAccountBillBOList() {
        return accountBillBOList;
    }

    public void setAccountBillBOList(List<AccountBillBO> accountBillBOList) {
        this.accountBillBOList = accountBillBOList;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public int getAllNUms() {
        return allNUms;
    }

    public void setAllNUms(int allNUms) {
        this.allNUms = allNUms;
    }

    public String getMainBmikece() {
        return mainBmikece;
    }

    public void setMainBmikece(String mainBmikece) {
        this.mainBmikece = mainBmikece;
    }
}
