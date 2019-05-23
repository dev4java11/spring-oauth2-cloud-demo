package com.example.demo;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.example.demo.util.OauthSessionInvalidationInterceptor;

/**
 * 
 * @author herna
 * @see <a href="http://blog.marcosbarbero.com/centralized-authorization-jwt-spring-boot2/">http://blog.marcosbarbero.com/centralized-authorization-jwt-spring-boot2/</a>
 * @see <a href="https://stackoverflow.com/questions/47322633/oauth2-confirmation-approval-is-not-working-denying-even-when-i-click-approve">https://stackoverflow.com/questions/47322633/oauth2-confirmation-approval-is-not-working-denying-even-when-i-click-approve</a>
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(AuthorizationServerProperties.class)
@Order(2)
public class AuthorizacionServerConfig extends AuthorizationServerConfigurerAdapter implements ApplicationContextAware{
	
	private AuthorizationServerProperties authorization;
	
	private ApplicationContext ctx;
	
	/**
	 * Autowired DataSource for persist oauth in database
	 */
//	@Autowired
//	private DataSource oauthDT;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
	
	public AuthorizacionServerConfig(AuthorizationServerProperties authorization) {
		this.authorization = authorization;
	}
	
	
	/**
	 * Bean for create password encoder. For example:
	 * From password: 123 to encoded: {bcrypt}$2a$10$CmO6IFHWRP1.H/i9fK3QWeQkeEqc7F.Uhxok.1c7ujl7VCuoWlbqK
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	/**
	 * Bean for create and transform token JWT into OAuthAccesToken Spring
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		
		Resource keyStore = this.ctx.getResource(this.authorization.getJwt().getKeyStore());
		char[] keyStorePassword = this.authorization.getJwt().getKeyStorePassword().toCharArray();
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStore, keyStorePassword);
		
		String keyAlias = this.authorization.getJwt().getKeyAlias();
		char[] keyPassword = Optional.ofNullable(this.authorization.getJwt().getKeyPassword()).map(String::toCharArray).orElse(keyStorePassword);
		converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword));
		
		return converter;
	}
	
	/**
	 * Bean for approval store user and its scope in database
	 * @return
	 */
//	@Bean
//	public ApprovalStore jdbcApprovalStore() {
//		JdbcApprovalStore approval = new JdbcApprovalStore(oauthDT);
//		return approval;
//	}
	
	/**
	 * Bean for approval store user and its scope in memory
	 * @return
	 */
	@Bean 
	public ApprovalStore inMemoryApprovalStore() {
		InMemoryApprovalStore approval = new InMemoryApprovalStore();
		return approval;
	}
	
	/**
	 * Bean for persist oauth token, but with jwt it is not necessary.
	 * @return
	 */
	@Bean
	public TokenStore jwtTokenStoreAuthorizationServer() {
		JwtTokenStore store = new JwtTokenStore(jwtAccessTokenConverter());
		return store;
	}
	
	/**
	 * Bean for oauth clients store en database 
	 * @return
	 */
//	@Bean
//	public ClientDetailsService jdbcClientDetailsService() {
//		JdbcClientDetailsService jdbc = new JdbcClientDetailsService(oauthDT);
//		jdbc.setPasswordEncoder(passwordEncoder());
//		return jdbc;
//	}
	
	/**
	 * Bean for storage in database the code return of grant type authorization_code
	 * @return
	 */
//	@Bean
//	public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
//		JdbcAuthorizationCodeServices jdbc = new JdbcAuthorizationCodeServices(oauthDT);
//		return jdbc;
//	}
	
	/**
	 * Bean for storage in memory the code return of grant type authorization_code
	 * @return
	 */
	@Bean
	public AuthorizationCodeServices inMemoryAuthorizationCodeServices() {
		InMemoryAuthorizationCodeServices inMemory = new InMemoryAuthorizationCodeServices();
		return inMemory;
	}
	
	@Bean
	public UserApprovalHandler userApprovalHandler() {
		DefaultUserApprovalHandler approval = new DefaultUserApprovalHandler();
		return approval;
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.withClientDetails(jdbcClientDetailsService()); // for storage clients in database
		clients	// configure clients in memory
			.inMemory()
				.withClient("frontendapp")
				.secret(passwordEncoder().encode("123"))
				.scopes("read", "create", "update", "delete")
				.authorizedGrantTypes("authorization_code", "refresh_token")
				.redirectUris("http://localhost:8060/ui/login", "http://localhost:8000/ui/login")
				.authorities("ROLE_USER", "ROLE_ADMIN")
				.accessTokenValiditySeconds(1209600)// 14 days
				.autoApprove(false);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
//			.authorizationCodeServices(jdbcAuthorizationCodeServices())// storage in database
			.authorizationCodeServices(inMemoryAuthorizationCodeServices())// storage in memory
			.authenticationManager(authenticationManager)
			.tokenStore(jwtTokenStoreAuthorizationServer())// token store for jwt
			.accessTokenConverter(jwtAccessTokenConverter())// token converter for jwt
			.approvalStore(inMemoryApprovalStore())// storage approval store in memory
//			.userApprovalHandler(userApprovalHandler()) // disable because use aproval with checkbox https://www.oauth.com/oauth2-servers/scope/checkboxes/
			.addInterceptor(new OauthSessionInvalidationInterceptor());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
			.realm(this.authorization.getRealm())
			.tokenKeyAccess(this.authorization.getTokenKeyAccess())
			.checkTokenAccess(this.authorization.getCheckTokenAccess());
	}
	
}
