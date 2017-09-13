package com.cy.driver.common.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

	private static final Logger logger = LoggerFactory.getLogger(StrUtil.class);

	/** 称呼 */
	private static final String callName = "先生";

	public static void main(String[] args) {
		System.out.println(callJoin("欧阳明月"));
		System.out.println(callJoin("欧明月"));
	}

	/**
	 * 称呼拼接
	 * @param str
	 * @return
	 */
	public static String  callJoin(String str){
        if(StringUtils.isBlank(str)){
            return "";
        }
		if(str.length()<=3){
			return str.substring(0,1)+callName;
		}
		return str.substring(0,2)+callName;
	}

	/**
	 * 判断是字符串中是否包含某个汉字
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str){
		return (str.getBytes().length == str.length())?false:true;
	}

	/**
	 * 字符串按照顺序拼接
	 * @param strs
	 * @return
	 */
	public static String strJoint(String ...strs){
		String strData = "";
		for (String str : strs) {
			if(str != null){
				strData =strData + str ;
			}
		}
		return strData;
	}

	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
//		if (obj instanceof Collection) {
//
//			if (((Collection) obj).size() == 0) {
//
//				return ((Collection) obj).isEmpty();
//			}
//		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}
		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}
	
//	public static String objResStr(Object obj) {
//		return (isNullOrEmpty(obj) == true ? "" : obj.toString());
//
//	}

	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	/**
	 * 十六进制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0" + stmp);
			else
				hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	// 字节数组转化为有空格的字节字符串
	public static String byte2hexWithSpace(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0" + stmp);
			else
				hs.append(stmp);
			if (n != b.length - 1)
				hs.append(" ");
		}
		return hs.toString().toUpperCase();
	}

	/**
	 * 字节转化为二进制字符串
	 */
	public static String byteToBinary(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
		for (int i = 0; i < Byte.SIZE * bytes.length; i++)
			sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0'
					: '1');
		return sb.toString();
	}

	public static int byteToInt(byte intByte) {
		int n = intByte & 0xFF;
		return n;
	}

	public static int bytesToInt(byte[] intByte, int byteLength) {
		int fromByte = 0;
		for (int i = 0; i < byteLength; i++) {
			int n = (intByte[i] & 0xff) << (8 * (byteLength - 1 - i));
			fromByte += n;
		}
		return fromByte;
	}

	public static String byteToBin(byte binary, int bitLength) {
		String binStr = Integer.toBinaryString(byteToInt(binary));
		if (binStr.length() < bitLength) {
			StringBuilder formatted = new StringBuilder();
			formatted.append(binStr);
			do {
				formatted.insert(0, "0");
			} while (formatted.length() < bitLength);
			return formatted.toString();
		}
		return binStr;
	}

	/**
	 * 四舍五入 保留两位小数
	 */
	public static double doubleToTwoDecimal(double f) {
		BigDecimal bg = new BigDecimal(f);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * 是否为空白,包括null和""
	 */
	public static boolean isEmpty(Object str) {
		return str == null || str.toString().length() == 0;
	}

	/**
	 * 16进制字符串转化为字节数组
	 * 
	 */

	public static byte[] hex2byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;

		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer
						.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 10进制数转化为4位的2进制数
	 * 
	 * @param i
	 * @return
	 */
	public static String intToBin(int i) {
		String binStr = Integer.toBinaryString(i);
		if (binStr.length() < 4) {
			do {
				binStr = "0" + binStr;
			} while (binStr.length() < 4);
		}
		return binStr;
	}

	/**
	 * 得到十六进制数的静态方法
	 * 
	 * @param decimalNumber
	 *            十进制数
	 * @return 四位十六进制数字符串
	 */
	public static String getHexString(int decimalNumber) {
		// 将十进制数转为十六进制数
		String hex = Integer.toHexString(decimalNumber);
		// 转为大写
		hex = hex.toUpperCase();
		// 加长到四位字符，用0补齐
		while (hex.length() < 4) {
			hex = "0" + hex;
		}
		return hex;
	}

	// 转化字符串为十六进制编码
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 99以下
	 * 
	 * @param decimalNumber
	 *            十进制数
	 * @return 2位十六进制数字符串
	 */
	public static String getHexString1to99(int decimalNumber) {
		// 将十进制数转为十六进制数
		String hex = Integer.toHexString(decimalNumber);
		// 转为大写
		hex = hex.toUpperCase();
		// 加长到四位字符，用0补齐
		while (hex.length() < 2) {
			hex = "0" + hex;
		}
		return hex;
	}

	public static String format8Str(String str) {
		String[] ss = { "00000000", "0000000", "000000", "00000", "0000",
				"000", "00", "0" };
		str = ss[str.length()] + str;
		return str;
	}

    /**
     * 首字母转成大写
     * @param str
     * @return
     * @author wyh
     */
    public static String toIndexUpperCase(String str){
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 判断字符串是否是数字
     * @param str 字符串
     * @return 是true  否false
     * @author wyh
     */
    public static boolean isNumber(String str){
        if( str == null ){
            return false;
        }
        //正整数||负整数||(-0.000~-9.999)||(-10.999)
        String p = "([0-9]+)||(-[1-9][0-9]*)||(-?[0-9].[0-9]+)||(-?[1-9][0-9]*.[0-9]+)";
        Pattern pattern = Pattern.compile(p);
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是整数
     * @param str
     * @return 是true  否false
     * @author wyh
     */
    public static boolean isIntNumber(String str){
        if( str == null ){
            return false;
        }
        //整数
        String p = "-?[0-9]+";
        Pattern pattern = Pattern.compile(p);
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * string转换成int
     * @param str
     * @return
     * @author wyh
     */
    public static Integer buildInt(String str){
        if(isIntNumber(str)){
            return Integer.parseInt(str);
        }
        return null;
    }

    /**
     * string转换成long
     * @param str
     * @return
     * @author wyh
     */
    public static Long buildLong(String str){
        if(isIntNumber(str)){
            return Long.parseLong(str);
        }
        return null;
    }
}
