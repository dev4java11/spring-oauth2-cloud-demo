package com.example.demo.api;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserAuthenticationController {

	@RequestMapping("/user/me")
	public ResponseEntity<?> currentUser(Principal principal){
		return ResponseEntity.ok(principal);
	}
}
