package com.example.demo.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.util.Api;
import com.example.demo.util.AuthorizatizationServer;
import com.example.demo.util.DtoMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UIController {
	
	@Autowired
	AuthorizatizationServer auth;

	@Autowired
	OAuth2RestTemplate template;
	
	@Autowired
	Api api;
	
	@GetMapping({"/", "/index", "/login"})
	public String index(Model model) {
		log.debug("Redirect to Index page");
		ResponseEntity<Map<String, String>> response = getAllMessages();
		if(response.getStatusCode().is2xxSuccessful()) {
			
			List<DtoMessage> messages = null;
			if(response.getBody() != null && !response.getBody().isEmpty()) {
				messages = response.getBody()
						.entrySet()
						.stream()
						.map(entry -> new DtoMessage(entry.getKey(), entry.getValue()))
						.collect(Collectors.toList());
			}else {
				messages = new ArrayList<>();
			}
			
			model.addAttribute("messages", messages);
		}else if(response.getStatusCode().is4xxClientError()) {
			model.addAttribute("error", response.getBody().get("error"));
			model.addAttribute("token", new ArrayList<>());
		}
		
		ResponseEntity<Map<String, String>> responseTokenKey = getTokenKey();
		if(responseTokenKey.getStatusCode().is2xxSuccessful()) {
			model.addAttribute("algorithm", responseTokenKey.getBody().get("alg"));
			model.addAttribute("publicKey", responseTokenKey.getBody().get("value"));
		}else {
			model.addAttribute("algorithm", "");
			model.addAttribute("publicKey", "");
			model.addAttribute("errorTokenKey", responseTokenKey.getBody().get("error"));
		}
		model.addAttribute("token", template.getAccessToken());
		model.addAttribute("tokenJSON", printToJson(template.getAccessToken()));
		return "index";
	}
	
	@RequestMapping("/exit")
	public String exit(HttpServletRequest request) {
		String redirect = ServletUriComponentsBuilder.fromContextPath(request).build().toUriString() + "/login";
		String url = auth.getExit().expand(redirect).toString();
		return "redirect:" + url;
	}
	
	private ResponseEntity<Map<String, String>> getAllMessages(){
		try {
			ResponseEntity<Map<String, String>> response = template.exchange(api.getGreetings().getMessage().toString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, String>>(){});
			return response;
		}catch(OAuth2Exception ex) {
			Map<String, String> map = new LinkedHashMap<>();
			map.put("error", ex.getSummary());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
		}
	}
	
	private ResponseEntity<Map<String, String>> getTokenKey(){
		try {
			ResponseEntity<Map<String, String>> response = template.exchange(api.getOauth().getTokenKey().toString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, String>>(){});
			return response;
		}catch(OAuth2Exception ex) {
			Map<String, String> map = new LinkedHashMap<>();
			map.put("error", ex.getSummary());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
		}
	}
	
	private String printToJson(Object obj) {
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			return "";
		}
	}
}
