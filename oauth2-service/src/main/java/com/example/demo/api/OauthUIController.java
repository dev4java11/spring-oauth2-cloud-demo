package com.example.demo.api;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class OauthUIController {

	@GetMapping("/login")
	public String login() {
		log.debug("Redirect to login view.");
		return "login";
	}
	
	@GetMapping(path = "/login", params = OAuth2Utils.REDIRECT_URI)
	public String redirectAfterErrorAuthentication(@RequestParam Map<String, String> parameters) {
		String url = parameters.get(OAuth2Utils.REDIRECT_URI);
		log.debug("Redirect to " + url);
		SecurityContextHolder.clearContext();
		return "redirect:" + url;
	}
	
	@GetMapping("/oauth/confirm_access")
	public String confirmAccess() {
		log.debug("Redirect to authorize view.");
		return "authorize";
	}
	
}
