package com.example.demo.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GreetingsController {

	private Map<String, String> map = new LinkedHashMap<>();
	
	private String uuid() {
		return UUID.randomUUID().toString();
	}
	
	@PostConstruct
	public void init() {
		map.put(uuid(), "Hello World");
		map.put(uuid(), "Spring is awesome");
		map.put(uuid(), "Oauth 2 is very hard");
	}
	
	@GetMapping("/messages")
	public ResponseEntity<?> get(){
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/messages/{uuid}")
	public ResponseEntity<?> get(@PathVariable String uuid){
		return ResponseEntity.ok("Hello World!!");
	}
	
	@PostMapping("/messages")
	public ResponseEntity<?> create(@RequestParam String message) {
		String uuid = uuid();
		map.put(uuid, message);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/messages/{uuid}")
	public ResponseEntity<?> update(@PathVariable String uuid, @RequestParam String message) {
		map.replace(uuid, message);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/messages/{uuid}")
	public ResponseEntity<?> delete(@PathVariable String uuid) {
		map.remove(uuid);
		return ResponseEntity.ok().build();
	}
}
