package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 
 * @author herna
 * @see <a href="https://www.oauth.com/">https://www.oauth.com/</a>
 */
@SpringBootApplication
@EnableEurekaClient
public class Oauth2ServiceApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ServiceApplication.class, args);
	}
}
