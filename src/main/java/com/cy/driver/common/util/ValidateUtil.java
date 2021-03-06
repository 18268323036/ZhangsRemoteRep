package com.cy.driver.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

	/**
	 * 隐藏手机号码
	 * @return false:不是; true:是
	 * */
	public static String hideMobile(String mobile){
		return mobile.substring(0, 3) + "****" + mobile.substring(7);
	}

	/**
	 * 车牌校验
	 * @return false:不是; true:是
	 * */
	public static boolean validateCarNumber(String carCard){
		if(StringUtils.isEmpty(carCard)){
			return false;
		}
        String regx = "^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]$|^[a-zA-Z]{2}\\d{7}$";
		Pattern pattern=Pattern.compile(regx);
		Matcher macher=pattern.matcher(carCard);
		return macher.matches();
	}
	
	/**
	 * 判断是否为正确的移动电话号码
	 * @return false:不是; true:是
	 * */
	public static boolean validateTelePhone(String telephone){
		if(StringUtils.isEmpty(telephone)){
			return false;
		}
        String regx = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$";
		Pattern pattern=Pattern.compile(regx);
		Matcher macher=pattern.matcher(telephone);
		return macher.matches();
	}
	
	/**
	 * 判断是否为正确的 固定电话号码（0571-88175786或057188175786）
	 * @return false:不是; true:是
	 * */
	public static boolean validatePhone(String telephone){
		if(StringUtils.isEmpty(telephone)){
			return false;
		}
		Pattern pattern=Pattern.compile("^((0\\d{2,3}))(-{0,})(\\d{7,8})(-(\\d{3,}))?$");
		Matcher macher=pattern.matcher(telephone);
		return macher.matches();
	}
	
	/**
	 * 判断是否为正确的身份证号
	 * @return false:不是; true:是
	 * */
	public static boolean validateIdentityLicenseNum(String identityLicenseNum){
		if(StringUtils.isEmpty(identityLicenseNum)){
			return false;
		}
		Pattern pattern=Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Za-z])$");
		Matcher macher=pattern.matcher(identityLicenseNum);
		return macher.matches();
	}
	
	/**
	 * 
	 * @param mail 邮箱账号
	 * @return 
	 */
	public static boolean isMail(String mail) {
		if(StringUtils.isBlank(mail)) {
			return false;
		}
		Pattern mailRegx = Pattern.compile("^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}$");
		Matcher match = mailRegx.matcher(mail);
		return match.matches();
	}

    /**
     * 是否数字
     * @param str
     * @return
     */
    public static boolean isNumberic(String str) {
        Pattern pattern = Pattern.compile("^\\d*$");
        return pattern.matcher(str).matches();
    }
    
	/**
	 * @param \6~20位英文字母或数字
	 * @return 
	 */
	public static boolean is6_20NumAndEng(String str) {
		if(StringUtils.isBlank(str)) {
			return false;
		}
		Pattern mailRegx = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
		Matcher match = mailRegx.matcher(str);
		return match.matches();
	}
}
