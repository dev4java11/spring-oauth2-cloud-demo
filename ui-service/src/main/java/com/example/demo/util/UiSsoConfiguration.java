package com.example.demo.util;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@Configuration
@EnableOAuth2Sso
public class UiSsoConfiguration extends WebSecurityConfigurerAdapter {
	
    @Bean
    @LoadBalanced
  	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext, OAuth2ProtectedResourceDetails details) {
    	OAuth2RestTemplate oauth = new OAuth2RestTemplate(details, oauth2ClientContext);
    	return oauth;
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
    	web
    		.debug(true)
    		.ignoring()
    			.antMatchers("/css/**", "/js/**", "/favicon.ico", "/webjars/**");
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//			.httpBasic().disable()
			.antMatcher("/**")
	        	.authorizeRequests()
//	        		.antMatchers("/", "/login**", "/error**").permitAll()
	        		.antMatchers("/error**", "/exit").permitAll()
	        		.anyRequest().authenticated()
	        .and()
	        	.logout()
	        		.logoutSuccessUrl("/exit")
	        		.invalidateHttpSession(true)
	        		.clearAuthentication(true)
	        		.deleteCookies("UISESSIONID")
	        .and()
	        	.csrf();
//	        	.ignoringAntMatchers("/login**");
	}
}
