package com.cy.driver.service;

/**
 * Created by nixianjing on 16/8/8.
 */
public interface IdSourceDTOService {


    /**
     *
     * @param orderId 运单Id
     * @param source 运单来源  1快到网 2云配送
     * @return
     */
    public boolean updateIndex(String orderId, Integer source);
}
