package com.example.demo.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	@GetMapping(path = "/exit", params = OAuth2Utils.REDIRECT_URI)
	public String exit(@RequestParam Map<String, String> parameters, Model model, HttpServletRequest request) {
		String logout_uri = ServletUriComponentsBuilder.fromContextPath(request).build().toUriString() + "/logout";
		String redirect_uri = parameters.get(OAuth2Utils.REDIRECT_URI);
		log.debug("Logout to" + logout_uri);
		log.debug("Redirect to " + redirect_uri);
		model.addAttribute("logout_uri", logout_uri);
		model.addAttribute(OAuth2Utils.REDIRECT_URI, redirect_uri);
		return "exit";
	}
	
	@GetMapping("/oauth/confirm_access")
	public String confirmAccess() {
		log.debug("Redirect to authorize view.");
		return "authorize";
	}
	
}
