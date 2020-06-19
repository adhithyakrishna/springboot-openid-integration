package com.example.demo.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.RefreshTokenOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;


@Service
public class Oauth2TokenService {

	private final OAuth2AuthorizedClientService clientService;
	private final RefreshTokenOAuth2AuthorizedClientProvider clientProvider;

	@Autowired
	public Oauth2TokenService(
			OAuth2AuthorizedClientService authorizedClientService) {

		this.clientService = authorizedClientService;
		this.clientProvider = new RefreshTokenOAuth2AuthorizedClientProvider();
	}

	/**
	 * get oauth2 access token.
	 */
	public String getAccessToken() {

		OAuth2AccessToken token = getOAuth2AuthorizedClient().getAccessToken();

		if (isExpiredToken(token)) {
			token = refreshedAccessToken();
		}

		String value = token.getTokenValue();
		
		return value;
	}

	/**
	 * get oauth2 refresh token.
	 */
	public String getRefreshToken() {

		OAuth2RefreshToken token = getOAuth2AuthorizedClient().getRefreshToken();

		String value = token.getTokenValue();
		return value;
	}

	/**
	 * access token not expired.
	 */
	private boolean isExpiredToken(OAuth2AccessToken accessToken) {
		return accessToken.getExpiresAt().isBefore(Instant.now());
	}

	private OAuth2AccessToken refreshedAccessToken() {		

		OAuth2AuthenticationToken authentication = getAuthentication();
		OAuth2AuthorizedClient oldClient = getOAuth2AuthorizedClient();

		OAuth2AuthorizationContext.Builder builder;
		builder = OAuth2AuthorizationContext.withAuthorizedClient(oldClient);

		OAuth2AuthorizationContext context = builder.principal(authentication).build();
		OAuth2AuthorizedClient refreshedClient = clientProvider.authorize(context);
		
		clientService.removeAuthorizedClient(
				oldClient.getClientRegistration().getRegistrationId(),
				oldClient.getPrincipalName());
		
		clientService.saveAuthorizedClient(refreshedClient, authentication);
		
		return refreshedClient.getAccessToken();
	}

	OAuth2AuthorizedClient getOAuth2AuthorizedClient() {		
		OAuth2AuthenticationToken auth = getAuthentication();
		return clientService.loadAuthorizedClient(
				auth.getAuthorizedClientRegistrationId(), auth.getName());
	}

	OAuth2AuthenticationToken getAuthentication() {

		return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	}
}
