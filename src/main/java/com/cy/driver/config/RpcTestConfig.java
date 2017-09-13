package com.cy.driver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangxy 2017/9/12 17:47
 */
@Configuration
@PropertySource({"classpath:dubbo/dubboTest.properties","/config.properties"})
@ImportResource({"classpath:dubbo/*.xml"})
@Profile("test")
public class RpcTestConfig {
}
