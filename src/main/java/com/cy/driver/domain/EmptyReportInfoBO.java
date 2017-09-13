package com.cy.driver.domain;

import java.util.List;

/**
 * Created by Administrator on 2015/9/24.
 *
 * 空车上报参数类
 */
public class EmptyReportInfoBO {
    private static final long serialVersionUID = 83802999217981096L;

    private int todayEmptyCarNum;//今日空车上报个数
    private int futureEmptyCarNum;//未来空车上报个数
    private List<TodayEmptyReportBO> todayEmptyCarList;//今日空车上报集合
    private List<BusinessLineBO> futureEmptyCarList;//未来空车上报集合

    public int getTodayEmptyCarNum() {
        return todayEmptyCarNum;
    }

    public void setTodayEmptyCarNum(int todayEmptyCarNum) {
        this.todayEmptyCarNum = todayEmptyCarNum;
    }

    public int getFutureEmptyCarNum() {
        return futureEmptyCarNum;
    }

    public void setFutureEmptyCarNum(int futureEmptyCarNum) {
        this.futureEmptyCarNum = futureEmptyCarNum;
    }

    public List<TodayEmptyReportBO> getTodayEmptyCarList() {
        return todayEmptyCarList;
    }

    public void setTodayEmptyCarList(List<TodayEmptyReportBO> todayEmptyCarList) {
        this.todayEmptyCarList = todayEmptyCarList;
    }

    public List<BusinessLineBO> getFutureEmptyCarList() {
        return futureEmptyCarList;
    }

    public void setFutureEmptyCarList(List<BusinessLineBO> futureEmptyCarList) {
        this.futureEmptyCarList = futureEmptyCarList;
    }
}
