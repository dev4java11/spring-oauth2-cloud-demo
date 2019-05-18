package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	private static final String PATH_API = "/api/**";
	
	@Autowired
	private ResourceServerProperties resource;
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setVerifierKey(resource.getJwt().getKeyValue());
		return converter;
	}
	
	@Bean
	public TokenStore jwtTokenStore() {
		JwtTokenStore store = new JwtTokenStore(jwtAccessTokenConverter());
		return store;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, PATH_API).access("#oauth2.hasScope('read') and isAuthenticated()")
				.antMatchers(HttpMethod.POST, PATH_API).access("#oauth2.hasScope('write') and isAuthenticated()")
				.antMatchers(HttpMethod.PUT, PATH_API).access("#oauth2.hasScope('write') and isAuthenticated()")
				.antMatchers(HttpMethod.PATCH, PATH_API).access("#oauth2.hasScope('write') and isAuthenticated()")
				.antMatchers(HttpMethod.DELETE, PATH_API).access("#oauth2.hasScope('write') and isAuthenticated()")
				.anyRequest().authenticated();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer config) throws Exception {
		config
			.resourceId(resource.getId())
			.tokenStore(jwtTokenStore());
	}
}
