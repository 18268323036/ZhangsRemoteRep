package com.cy.driver.common.springex;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.cy.driver.action.BaseAction;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.util.IPUtil;
import com.cy.syslog.service.DriverLogService;
import com.cy.syslog.service.dto.LogDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author zhangxy 2017/4/6 11:34
 */
@Aspect
@Component
public class ApiControllerProxy {

    Logger LOG = LoggerFactory.getLogger(ApiControllerProxy.class);

    @Resource
    private DriverLogService driverLogService;

    @Pointcut("@annotation(com.cy.driver.common.annotate.ReqRespHeadCode)")
    public void cutController(){}

    @Around(value = "cutController()")
    public Object proceedController(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ReqRespHeadCode reqRespHeadCode = method.getAnnotation(ReqRespHeadCode.class);
        /** 方法名称 */
        String methodName = method.getName();
        /** 类名称 */
        String targetName = point.getTarget().getClass().getName();
        BaseAction baseAction = (BaseAction)point.getTarget();
        //获取request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        Log log = method.getAnnotation(Log.class);
        //往数据库记录日志
        if(log!=null) {
            LogDTO logDTO = new LogDTO();
            logDTO.setOperationType(log.type().getOperationType());
            logDTO.setOperationName(log.type().getOperationName());
            logDTO.setRemark(log.type().getRemark());
            //获得访问者的ip
            String ip = IPUtil.findRequestIp(request);
            logDTO.setRequestFromIp(ip);
            logDTO.setCreateTime(new Date());
            //获得司机id
            Long driverId = findDriverId(request);
            logDTO.setUserDriverId(driverId);
            driverLogService.saveLog(logDTO);
        }
        LOG.debug(getRequestInfo(request));
        Object obj = null;
        try {
            obj = point.proceed();
        }catch (Exception e){
            baseAction.findException(baseAction.getResponse());
            LOG.error("{}.{}(){}出现异常", targetName, methodName, reqRespHeadCode.reqHeadCode().getDesc(), e);
        }
        return obj;
    }

    public String getRequestInfo(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        Enumeration enumeration = request.getParameterNames();
        sb.append("接口").append(request.getRequestURI()).append("请求参数-----------");
        while (enumeration.hasMoreElements()){
            String name = (String) enumeration.nextElement();
            String value = request.getParameter(name);
            if(StringUtils.isNotEmpty(value)) {
                sb.append(name).append("=").append(value).append(" & ");
            }
        }
        return sb.substring(0,sb.length()-3);
    }


    /** 获得司机id */
    private Long findDriverId(HttpServletRequest request){
        try {
            Object userId = request.getAttribute("userId");
            String userIdStr = userId == null ? "" : userId.toString();
            if (org.apache.commons.lang.StringUtils.isBlank(userIdStr)) {
                return null;
            }
            return Long.valueOf(userIdStr);
        } catch (Exception e) {
            if(LOG.isErrorEnabled()){
                LOG.error("系统日志计录拦截器获得司机id出错", e);
            }
        }
        return null;
    }

}
