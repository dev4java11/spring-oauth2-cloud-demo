package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Oauth2ServiceApplicationTests {
	
	@Autowired
	private PasswordEncoder enconder;

	@Test
	public void contextLoads() {
		System.out.println(enconder.encode("restapp"));
	}

}
