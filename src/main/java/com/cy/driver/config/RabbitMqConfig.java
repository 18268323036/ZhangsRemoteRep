package com.cy.driver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author zhangxy 2017/9/7 9:42
 */
@Configuration
@ImportResource({"classpath*:com/cy/core/rabbitmq/spring-rabbitmq-extension-producer.xml"})
public class RabbitMqConfig {

}
