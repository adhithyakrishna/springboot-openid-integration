package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class OAuth2LoginConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest()
		.authenticated().and()
		.oauth2Login()
		.defaultSuccessUrl("/", true).and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/login?logout"))
		.logoutSuccessUrl("/exit");
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() 
	{
		return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
	}

	public ClientRegistration googleClientRegistration() {

		return ClientRegistration
				.withRegistrationId("google")
				.clientId("-- insert client id --") // insert client id 
				.clientSecret("-- insert client secret --") // insert client secret
				.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.scope("openid", "profile", "email", "https://www.googleapis.com/auth/drive.file") //add your scopes here
				.redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
				.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
				.tokenUri("https://www.googleapis.com/oauth2/v4/token")
				.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
				.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
				.clientName("Google")
				.userNameAttributeName(IdTokenClaimNames.SUB)
				.build();
	}

}
