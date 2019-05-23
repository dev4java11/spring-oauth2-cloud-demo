package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/css/**", "/js/**", "/favicon.ico", "/webjars/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/do-login")
			.and()
				.logout()
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("OAUTHSESSIONID")
			.and()
				.authorizeRequests()
				.antMatchers("/login", "/do-login", "/exit").permitAll()
				.antMatchers(HttpMethod.GET, "/oauth/authorize").permitAll()
				.anyRequest().authenticated()
			.and()
				.csrf();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("user").password(passwordEncoder.encode("p1")).roles("USER")
			.and()
			.withUser("admin").password(passwordEncoder.encode("p2")).roles("ADMIN")
			.and()
			.passwordEncoder(passwordEncoder);
	}
}
