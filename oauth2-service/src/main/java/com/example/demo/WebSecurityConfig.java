package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
//			.authorizeRequests()//nv
//				.antMatchers("/", "/login", "/do-logout").permitAll() //nv
//				.anyRequest().authenticated()//nv
//			.and() //nv
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/do-login")
				.permitAll()// nv
			.and()// nv
				.requestMatchers() //nv
				.antMatchers("/", "/login", "/do-login", "/oauth/authorize", "/oauth/confirm_access", "/exit")// nv
			.and()
				.logout()
//				.logoutUrl("/do-logout") nv
			.and()
				.authorizeRequests()
				.anyRequest().authenticated()
			.and()
				.csrf();
//			.and()
//				.requiresChannel()
//				.antMatchers(HttpMethod.GET, "/login", "/oauth/authorize").requiresSecure();
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
