package com.cy.driver.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Richie.Lee on 2014/9/15.
 */
public interface WebAction {
	void setRequest(HttpServletRequest request);

	void setResponse(HttpServletResponse response);

}
