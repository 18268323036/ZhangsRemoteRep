package com.cy.driver.config;

import com.cy.driver.common.initdata.ConfigData;
//import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.redis.PasswordClient;
import com.cy.driver.common.redis.impl.PasswordClientImpl;
import com.cy.driver.service.InitDataService;
import com.cy.driver.service.LocationService;
import com.cy.driver.service.VehicleCarriageService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author zhangxy 2017/9/6 17:17
 */
@Configuration
public class AppConfig{

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

    @Order(value=9)
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

}

