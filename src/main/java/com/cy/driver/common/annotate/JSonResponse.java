package com.cy.driver.common.annotate;

/**
 * Created by Richie.Lee on 2014/9/12.
 */
public class JSonResponse {
	private String error_code;
	private String error_msg;

	public JSonResponse(String error_code, String error_msg) {
		this.error_code = error_code;
		this.error_msg = error_msg;
	}

	public JSonResponse() {
	}

	public static JSonResponse makeHasContentJSonRespone(ApiResultCodeEnum apiResultCodeEnum,
														 String... error_msg) {
		JSonResponse jSonResponse = new JSonResponse();
		jSonResponse.error_code = apiResultCodeEnum.getCode();
		jSonResponse.error_msg = ApiResultCodeEnum.findErrorCodeStr(apiResultCodeEnum.getMessage(), error_msg);
		return jSonResponse;
	}

	public static JSonResponse resultJSonResponse(ApiResultCodeEnum apiResultCodeEnum) {
		JSonResponse jSonResponse = new JSonResponse();
		jSonResponse.error_code = apiResultCodeEnum.getCode();
		jSonResponse.error_msg = apiResultCodeEnum.getMessage();
		return jSonResponse;
	}

	/**
	 * 系统错误的返回
	 * @author wyh
	 */
	public static JSonResponse makeException(){
		JSonResponse jSonResponse = new JSonResponse();
		jSonResponse.error_code = ApiResultCodeEnum.SYS_10001.getCode();
		jSonResponse.error_msg = ApiResultCodeEnum.findErrorCodeStr(ApiResultCodeEnum.SYS_10001.getMessage());
		return jSonResponse;
	}

	/**
	 * 参数错误的返回
	 * @author wyh
	 */
	public static JSonResponse makeErrorParam(){
		JSonResponse jSonResponse = new JSonResponse();
		jSonResponse.error_code = ApiResultCodeEnum.SER_20001.getCode();
		jSonResponse.error_msg = ApiResultCodeEnum.findErrorCodeStr(ApiResultCodeEnum.SER_20001.getMessage());
		return jSonResponse;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public Object getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

}
