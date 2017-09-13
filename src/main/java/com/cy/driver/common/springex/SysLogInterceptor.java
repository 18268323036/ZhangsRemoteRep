package com.cy.driver.common.springex;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.util.IPUtil;
import com.cy.syslog.service.DriverLogService;
import com.cy.syslog.service.dto.LogDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志计录拦截器
 * Created by wyh on 2015/7/30.
 */
public class SysLogInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(SysLogInterceptor.class);

    @Resource
    private DriverLogService driverLogService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            saveLog(request, handler);
		} catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("日志拦截中出现错误", e);
            }
		}
        super.postHandle(request, response, handler, modelAndView);
    }

    /** 保存日志记录 */
    private void saveLog(HttpServletRequest request, Object handler){
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Log log = method.getAnnotation(Log.class);
            if (log == null) {
                //无需日志记录
                return;
            }
            String token = request.getHeader(Constants.REQ_HEAD_TOKEN);
            Long driverId = null;
            if(StringUtils.isNotBlank(token)){
                //获得司机id
                driverId = findDriverId(request);
                if(driverId == null){
                    if(logger.isWarnEnabled()){
                        logger.warn("配置了日志记录，但是driverId=空，记录日志失败");
                    }
                }
            }else{
                if(logger.isWarnEnabled()){
                    logger.warn("配置了日志记录，但是token=空，记录日志失败");
                }
            }
            //获得访问者的ip
            String ip = IPUtil.findRequestIp(request);
            if(logger.isDebugEnabled()){
                logger.debug("向日志记录线程中添加日志对象....当前请求IP:{}", ip);
            }
            LogDTO logDTO = new LogDTO();
            logDTO.setOperationType(log.type().getOperationType());
            logDTO.setOperationName(log.type().getOperationName());
            logDTO.setRemark(log.type().getRemark());
            logDTO.setRequestFromIp(ip);
            logDTO.setCreateTime(new Date());
            logDTO.setUserDriverId(driverId);
            driverLogService.saveLog(logDTO);
        }
    }

    /** 获得司机id */
    private Long findDriverId(HttpServletRequest request){
        try {
            Object userId = request.getAttribute("userId");
            String userIdStr = userId == null ? "" : userId.toString();
            if (StringUtils.isBlank(userIdStr)) {
                return null;
            }
            return Long.valueOf(userIdStr);
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error("系统日志计录拦截器获得司机id出错", e);
            }
        }
        return null;
    }
}
