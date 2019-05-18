package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.util.Api;
import com.example.demo.util.DtoMessage;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UIController {

	@Autowired
	OAuth2RestTemplate template;
	
	@Autowired
	Api api;
	
	@GetMapping({"/", "/index", "/login"})
	public String index(Model model) {
		log.debug("Redirect to Index page");
		ResponseEntity<Map<String, String>> response = template.exchange(api.getGreetings().getMessage().toString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, String>>(){});
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
		}
		return "index";
	}
}
