package com.cy.driver.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("HttpUtils")
public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	private String longUrlToShortxml;

	//2.调新浪微博的API长链接转短链接 返回xml http请求
	public String longUrlToShortUrl(String longUrl)
	{
		try{
			String sinaReqUrl=longUrlToShortxml+"&url_long="+longUrl;
			String json = doGetRequest(sinaReqUrl);
			List<JSONObject> list = JSON.parseArray(json, JSONObject.class);
			JSONObject jo = list.get(0);
			return jo.getString("url_short");
		}catch (Exception e){
			log.error("短链接调用失败。",e);
			return longUrl;
		}
	}


	/**
	 * URL get 请求
	 * @param url
	 * @return    请求结果
	 * @throws IOException
	 */
	public static String doGetRequest(String url) throws IOException {
		if (StringUtils.isBlank(url))
			throw new IllegalArgumentException("url cannot be null! ");

		StringBuilder stringBuilder = new StringBuilder();
		String msg;
		BufferedReader bufferedReader = null;
		try {
			URL httUrl = new URL(url);

			HttpURLConnection httpURLConnection = (HttpURLConnection) httUrl.openConnection();

			//设置连接参数
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type","application/xml; charset=UTF-8");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);

			int resCode = httpURLConnection.getResponseCode();
			if (resCode == 200) {
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				while ((msg = bufferedReader.readLine()) != null) {
					stringBuilder.append(msg);
				}
			} else {
				msg = "Http request error code :" + resCode;
				stringBuilder.append(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return stringBuilder.toString().trim();
	}


    /**
     * post 请求
     * @param url 请求URL
     * @param map 请求参数 key参数名，value参数值
     * @return
     */
    public static String doPostRequest(String url, Map<String, String > map) {
        if (StringUtils.isBlank(url))
            throw new IllegalArgumentException("url cannot be null! ");

        if (map == null)
            throw new IllegalArgumentException("map cannot be null! ");

        HttpClient httpClient = new HttpClient();

        PostMethod postMethod = new PostMethod(url);

        //使用系统系统的默认的恢复策略
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //设置参数编码
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        //设置连接超时30秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        //设置响应超时30秒
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
        //请求参数
        if (map !=null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                postMethod.addParameter(entry.getKey(), entry.getValue());
            }
        }

        String msg = "";
        try {
            httpClient.executeMethod(postMethod);
            InputStream inputStream = postMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String str= "";
            while((str = br.readLine()) != null){
                stringBuffer.append(str );
            }
            msg = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return msg;
    }


	public static String postXml(String urlStr, Map<String, String> params) {
		URL url = null;
		HttpURLConnection conn = null;
		//构建请求参数
		StringBuffer sb = new StringBuffer();
		String str = "";
		if(params!=null){
			for (String key : params.keySet()) {
	    		sb.append(key);
	    		sb.append("=");
	    		sb.append(params.get(key));
	    		sb.append("&");
			}
			str = sb.substring(0,sb.length() - 1);
		}
		
		log.info("send_url:"+urlStr);
		log.info("send_data:" + str);
		//尝试发送请求
		try {
			url = new URL(urlStr);
    		conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("POST");
    		conn.setDoOutput(true);
    		conn.setDoInput(true);
    		conn.setUseCaches(false);
    		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    		OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
    		osw.write(str);
    		osw.flush();
    		osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
    		if (conn != null) {
    			conn.disconnect();
    		}
		}
		//读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
    		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    		String temp;
    		while ((temp = br.readLine()) != null) {
	    		buffer.append(temp);
	    		buffer.append("\n");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	   }

	public String getLongUrlToShortxml() {
		return longUrlToShortxml;
	}

	@Value("${longUrlToShortxml.sina.url}")
	public void setLongUrlToShortxml(String longUrlToShortxml) {
		this.longUrlToShortxml = longUrlToShortxml;
	}

	public static void main(String[] arg0){
			try {
				Map<String ,String > param = new LinkedHashMap<String ,String >();
				param.put("userName","huxi");
				param.put("password", "123");
				String str = doPostRequest("http://172.16.1.10:6611/loginUserInfo.jspx", param);
				System.out.println(str);
			}catch (Exception e){

			}

	}
}
