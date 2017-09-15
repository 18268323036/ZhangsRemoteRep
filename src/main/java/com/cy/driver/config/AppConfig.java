package com.cy.driver.config;

import com.cy.driver.common.initdata.ConfigData;
//import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.redis.PasswordClient;
import com.cy.driver.common.redis.impl.PasswordClientImpl;
import com.cy.driver.common.springex.AuthenticationInterceptor;
import com.cy.driver.common.springex.PermissionValidationInterceptor;
import com.cy.driver.common.springex.ReqRespHeadInterceptor;
import com.cy.driver.common.springex.SysLogInterceptor;
import com.cy.driver.service.InitDataService;
import com.cy.driver.service.LocationService;
import com.cy.driver.service.VehicleCarriageService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zhangxy 2017/9/6 17:17
 */
@Configuration
@ComponentScan(basePackages="com.cy.driver")
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ObjectMapper ObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        return objectMapper;
    }

    @Bean
    public ConfigData configData(){
        ConfigData configData = new ConfigData();
        return configData;
    }

    @Bean(initMethod = "execu")
    public SystemData systemData(){
        SystemData systemData = new SystemData();
        return systemData;
    }

    @Bean
    public PasswordClient passwordClient(){
        PasswordClientImpl passwordClientImpl = new PasswordClientImpl();
        return passwordClientImpl;
    }

    @Bean
    public ReqRespHeadInterceptor getReqRespHeadInterceptor(){
        return new ReqRespHeadInterceptor();
    }

    @Bean
    public PermissionValidationInterceptor getPermissionValidationInterceptor(){
        return new PermissionValidationInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(getReqRespHeadInterceptor())
                .addPathPatterns("/*")
                .addPathPatterns("/safeSSL/*")
                .excludePathPatterns("/images")
                .excludePathPatterns("/image")
                .excludePathPatterns("/css/*.css")
                .excludePathPatterns("/*.jsp")
                .excludePathPatterns("/*.html")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/downFile")
                .excludePathPatterns("/appDown")
                .excludePathPatterns("/decodeBase64")
                .excludePathPatterns("/saveAppShareInfo")
                .excludePathPatterns("/share/*")
                .excludePathPatterns("/getTransprotPro")
                .excludePathPatterns("/messageToBusinessDetail")
                .excludePathPatterns("/modifyMessageState")
                .excludePathPatterns("/getTurnWaybillTransportPro")
                .excludePathPatterns("/httpsTest");
        registry.addInterceptor(getPermissionValidationInterceptor())
                .addPathPatterns("/*")
                .addPathPatterns("/safeSSL/*")
                .excludePathPatterns("/images")
                .excludePathPatterns("/image")
                .excludePathPatterns("/css/*.css")
                .excludePathPatterns("/*.jsp")
                .excludePathPatterns("/*.html")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/getBootPageList")
                .excludePathPatterns("/downFile")
                .excludePathPatterns("/downloadImage")
                .excludePathPatterns("/systemVersionCheckAndDown")
                .excludePathPatterns("/appDown")
                .excludePathPatterns("/loginDriverUserInfo")
                .excludePathPatterns("/quickRegistration")
                .excludePathPatterns("/nextForgetPassword")
                .excludePathPatterns("/resetPassword")
                .excludePathPatterns("/getVerificationCode")
                .excludePathPatterns("/fileSingleUpload")
                .excludePathPatterns("/fileMultiUpload")
                .excludePathPatterns("/downSingleFile")
                .excludePathPatterns("/decodeBase64")
                .excludePathPatterns("/saveAppShareInfo")
                .excludePathPatterns("/getInitializationData")
                .excludePathPatterns("/share/*")
                .excludePathPatterns("/getTransprotPro")
                .excludePathPatterns("/messageToBusinessDetail")
                .excludePathPatterns("/modifyMessageState")
                .excludePathPatterns("/getTurnWaybillTransportPro")
                .excludePathPatterns("/httpsTest");
        super.addInterceptors(registry);
    }


}

