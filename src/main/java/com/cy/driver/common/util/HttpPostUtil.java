package com.cy.driver.common.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpPostUtil {

    private static Logger log = LoggerFactory.getLogger(HttpPostUtil.class);


    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(pair.getValue()+ "&");

        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    /**
     * 从输入流中读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

    //请求地址 返回字符串
    public static String loadJSON (String requestUrl, String paramsStr) {
        StringBuffer strBuf;
        strBuf = new StringBuffer();
        try{
            URL url = new URL(requestUrl);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type","application/xml; charset=UTF-8");

//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setRequestMethod("POST");
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(20000);

//            byte[] bypes = paramsStr.toString().getBytes();
//            connection.getOutputStream().write(bypes);// 输入参数
//            InputStream inStream=connection.getInputStream();
//            System.out.println(new String(readInputStream(inStream), "UTF-8"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//转码。
            String line = null;
            while ((line = reader.readLine()) != null)
                strBuf.append(line + " ");
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return strBuf.toString();
    }

    /**
     * post 请求
     * @param url 请求URL
     * @param map 请求参数 key参数名，value参数值
     * @return
     */
    public static String doPostRequest(String url, Map<String, String > map) {
        return doPostRequest(url, map, "UTF-8", "UTF-8");
    }

    public static String postXml(String urlStr, Map<String, String> params) {
        URL url = null;
        HttpURLConnection conn = null;
        //构建请求参数
        StringBuffer sb = new StringBuffer();
        String str = "";
        if (params != null) {
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append("=");
                sb.append(params.get(key));
                sb.append("&");
            }
            str = sb.substring(0, sb.length() - 1);
        }

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

    /**
     * 调用http post请求
     * @author wyh
     */
    public static String doPostRequest(String url, Map<String, String> map, String reqCode, String respCode) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url cannot be null! ");
        }
        if(StringUtils.isEmpty(reqCode)){
            reqCode = "UTF-8";//如果为空话 默认编码UTF-8
        }
        if(StringUtils.isEmpty(respCode)){
            respCode = "UTF-8";//如果为空话 默认编码UTF-8
        }

        HttpClient httpClient = new HttpClient();
        //设置响应超时
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(8000);
        PostMethod postMethod = new PostMethod(url);

        // 数据源测试，http  请求头
       /* postMethod.addRequestHeader("protocalType", "json");
        postMethod.addRequestHeader("protocalVersion", "3.0");
        postMethod.addRequestHeader("source", "android");
        postMethod.addRequestHeader("osVersion", "4.1.1");
        postMethod.addRequestHeader("imei", "20150409160033000001");
        postMethod.addRequestHeader("imsi", "20150409160033000001");
        postMethod.addRequestHeader("compress", "DES");
        postMethod.addRequestHeader("timeStamp", "20150409160033");
        postMethod.addRequestHeader("channelId", "102");
        postMethod.addRequestHeader("messengerId", "20150409160033000001");
        postMethod.addRequestHeader("reqCode", "1005");
        postMethod.addRequestHeader("userId", "100");*/

        //使用系统系统的默认的恢复策略
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //设置参数编码
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, reqCode);

        String send_data = "";
        //请求参数
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                postMethod.addParameter(entry.getKey(), entry.getValue());
                if(!"".equals(send_data)){
                    send_data += "&";
                }
                send_data += entry.getKey() + "=" + entry.getValue();
            }
        }
//        log.info("send_url:" + url);
//        log.info("send_data:" + send_data);
        String msg = "";
        try {
            httpClient.executeMethod(postMethod);
            InputStream inputStream = postMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, respCode));
            StringBuffer stringBuffer = new StringBuffer();
            String str= "";
            while((str = br.readLine()) != null){
                stringBuffer .append(str );
            }
            msg = stringBuffer.toString();
        } catch (IOException e) {
            log.error("http post请求出现io异常", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error("http post请求出现异常", e.getMessage());
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return msg;
    }

    public static void main(String[] args) {

		System.out.println();
	}
}
