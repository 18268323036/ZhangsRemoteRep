package com.cy.driver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangxy 2017/9/4 11:54
 */
@Configuration
@PropertySource({"classpath:dubbo/dubbo.properties","/config.properties"})
@ImportResource({"classpath:dubbo/*.xml"})
@Profile("dev")
public class RpcConfig {
}
