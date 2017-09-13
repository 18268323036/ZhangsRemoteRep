package com.cy.driver.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * ip工具类
 * Created by wyh on 2015/7/30.
 */
public class IPUtil {
    private static Logger logger = LoggerFactory.getLogger(IPUtil.class);

    private IPUtil(){}

    /**
     * 从请求中获得ip
     */
    public static String findRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
//        if (logger.isDebugEnabled()) {
//            logger.debug("request.getHeader(\"x-forwarded-for\")=" + ip);
//        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
//            if (logger.isDebugEnabled()) {
//                logger.debug("request.getHeader(\"X-Forwarded-For\")=" + ip);
//            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
//            if (logger.isDebugEnabled()) {
//                logger.debug("request.getHeader(\"Proxy-Client-IP\")=" + ip);
//            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
//            if (logger.isDebugEnabled()) {
//                logger.debug("request.getHeader(\"WL-Proxy-Client-IP\")=" + ip);
//            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
//            if (logger.isDebugEnabled()) {
//                logger.debug("request.getHeader(\"HTTP_CLIENT_IP\")=" + ip);
//            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//            if (logger.isDebugEnabled()) {
//                logger.debug("request.getHeader(\"HTTP_X_FORWARDED_FOR\")=" + ip);
//            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
//            if (logger.isDebugEnabled()) {
//                logger.debug("request.getRemoteAddr()=" + ip);
//            }
        }

        if (null != ip && ip.indexOf(',') != -1) {
            //如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串 IP 值
            //取X-Forwarded-For中第一个非unknown的有效IP字符串
            //如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
            //用户真实IP为： 192.168.1.110
            //注意:当访问地址为 localhost 时 地址格式为 0:0:0:0:0:0:1
//            if(logger.isDebugEnabled()){
//                logger.debug("ip=" + ip);
//            }
            String[] ips = ip.split(",");
            for (int i = 0; i < ips.length; i++) {
                if (null != ips[i] && !"unknown".equalsIgnoreCase(ips[i])) {
                    ip = ips[i];
                    break;
                }
            }
            if ("0:0:0:0:0:0:1".equals(ip)) {
                if(logger.isErrorEnabled()){
                    logger.error("由于客户端访问地址使用localhost，获取客户端真实IP地址错误，请使用IP方式访问");
                }
            }
        }

        if ("unknown".equalsIgnoreCase(ip)) {
            if(logger.isErrorEnabled()) {
                logger.error("由于客户端通过Squid反向代理软件访问，获取客户端真实IP地址错误，请更改squid.conf配置文件forwarded_for项默认是为on解决");
            }
        }
        return ip;
    }
}
