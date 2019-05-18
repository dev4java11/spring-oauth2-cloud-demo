package com.example.demo;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@Order(3)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	private ResourceServerProperties resource;
	
	private TokenStore tokenStore;
	
	public ResourceServerConfig(ResourceServerProperties resource, TokenStore tokenStore) {
		this.resource = resource;
		this.tokenStore = tokenStore;
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer config) throws Exception {
		config
			.resourceId(resource.getId())
			.tokenStore(tokenStore);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		super.configure(http);
	}
}
