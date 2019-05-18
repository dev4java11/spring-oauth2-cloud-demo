package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient("${spring.application.name}")
public class UiServiceApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(UiServiceApplication.class, args);
	}
}
