package com.cy.driver;

import com.cy.driver.common.initdata.SystemData;
import com.cy.driver.common.redis.RedisService;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.saas.basic.model.dto.AccountUserDetails2DTO;
import com.cy.saas.basic.service.AccountUserService;
import com.cy.top56.common.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
public class DasApplication {

	@Resource
	private DriverUserHandlerService driverUserHandlerService;
	@Resource
	private SystemData systemData;
	@Resource
	private RedisService redisService;


	public static void main(String[] args) {
		SpringApplication.run(DasApplication.class, args);
	}

	@RequestMapping("/")
	public String greeting() {
		System.out.println(driverUserHandlerService.getCarInfo(String.valueOf(100)));
		System.out.println(systemData);
		String s = redisService.getStr("hello");
		redisService.setStr("hello","World",20);
		return "Hello world";
	}
}
